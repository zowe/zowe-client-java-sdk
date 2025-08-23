/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.zostso.method.IssueTso;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the IssueTso class.
 * <p>
 * These tests validate constructor checks, private method behavior,
 * request execution, and the full TSO command flow with mocked dependencies.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class IssueTsoTest {

    private ZosConnection mockConnection;
    private ZosmfRequest mockRequest;
    private Response mockResponse;
    private IssueTso issueTso;

    @Before
    public void setUp() {
        mockConnection = mock(ZosConnection.class);
        mockRequest = mock(ZosmfRequest.class);
        mockResponse = mock(Response.class);

        when(mockConnection.getZosmfUrl()).thenReturn("http://zosmf:1234");
        issueTso = new IssueTso(mockConnection, "ACCT123");
    }

    /**
     * Verifies that the constructor throws an NullPointerException when the connection is null.
     */
    @Test
    public void tstIssueTsoConnectionNullFailure() {
        assertThrows(NullPointerException.class, () -> new IssueTso(null, "1"));
    }

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the account number is null.
     */
    @Test
    public void tstIssueTsoAccountNumberNullFailure() {
        assertThrows(IllegalArgumentException.class, () -> new IssueTso(mockConnection, null));
    }

    /**
     * Verifies that issueCommand throws an IllegalArgumentException when the command string is null.
     */
    @Test
    public void tstIssueCommendNullFailure() {
        assertThrows(IllegalArgumentException.class, () -> issueTso.issueCommand(null));
    }

    /**
     * Verifies that getJsonNode throws a ZosmfRequestException when given invalid JSON input.
     */
    @Test
    public void tstGetJsonNodeFailure() {
        assertThrows(ZosmfRequestException.class,
                () -> invokeUnwrap(issueTso,
                        new Class[]{String.class, String.class},
                        "not-json", "Failed"));
    }

    /**
     * Verifies that getJsonNode successfully parses valid JSON and returns a JsonNode object.
     */
    @Test
    public void tstGetJsonNodeSuccess() throws Throwable {
        JsonNode node = (JsonNode) invokeUnwrap(issueTso,
                new Class[]{String.class, String.class},
                "{\"servletKey\":\"123\"}", "msg");

        assertEquals("123", node.get("servletKey").asText());
    }

    /**
     * Helper method to invoke a private method with reflection
     * and unwrap InvocationTargetException to expose the real cause.
     *
     * @param target     the object under test
     * @param paramTypes the parameter type array
     * @param args       the arguments to pass
     * @return result of the invoked method
     * @throws Throwable the underlying exception if thrown
     */
    private Object invokeUnwrap(Object target, Class<?>[] paramTypes, Object... args) throws Throwable {
        java.lang.reflect.Method method = target.getClass().getDeclaredMethod("getJsonNode", paramTypes);
        method.setAccessible(true);
        try {
            return method.invoke(target, args);
        } catch (java.lang.reflect.InvocationTargetException e) {
            throw e.getCause(); // unwrap to the real exception
        }
    }

    /**
     * Verifies that executeRequest returns a valid response string when the request is
     * successful with status code 200.
     */
    @Test
    public void tstExecuteRequestResponseSuccess() throws Exception {
        when(mockResponse.getResponsePhrase()).thenReturn(Optional.of("{\"servletKey\":\"abc\"}"));
        when(mockResponse.getStatusCode()).thenReturn(OptionalInt.of(200));
        when(mockRequest.executeRequest()).thenReturn(mockResponse);

        java.lang.reflect.Method method =
                IssueTso.class.getDeclaredMethod("executeRequest", ZosmfRequest.class, String.class);
        method.setAccessible(true);

        // Here we are accessing private method "executeRequest" for an IssueTso object.
        // Explanation: method.invoke(issueTso, mockRequest, "Failed")
        // * invoke actually calls the private method on the given object.
        // * issueTso → the instance of your class under test.
        // * mockRequest → the first argument (the mocked ZosmfRequest).
        // * "Failed" → the second argument (the error message string).
        // As a result, this is equivalent to calling:
        // String result = issueTso.executeRequest(mockRequest, "Failed");
        // But since the method is private, you can’t call it directly.
        // Reflection lets you bypass access restrictions.
        String result = (String) method.invoke(issueTso, mockRequest, "Failed");
        assertTrue(result.contains("servletKey"));
    }

    /**
     * Verifies that executeRequest throws a ZosmfRequestException when the
     * response body is empty.
     */
    @Test
    public void tstExecuteRequestEmptyBodyFailure() throws Exception {
        when(mockResponse.getResponsePhrase()).thenReturn(Optional.of(""));
        when(mockResponse.getStatusCode()).thenReturn(OptionalInt.of(200));
        when(mockRequest.executeRequest()).thenReturn(mockResponse);

        java.lang.reflect.Method method =
                IssueTso.class.getDeclaredMethod("executeRequest", ZosmfRequest.class, String.class);
        method.setAccessible(true);

        Exception thrown = assertThrows(Exception.class,
                () -> method.invoke(issueTso, mockRequest, "Failed"));

        // unwrap if it's an InvocationTargetException
        Throwable cause = thrown instanceof java.lang.reflect.InvocationTargetException
                ? thrown.getCause()
                : thrown;

        assertInstanceOf(ZosmfRequestException.class, cause);
    }

    /**
     * Verifies that executeRequest throws a ZosmfRequestException when the HTTP status code
     * is not in the 2xx range.
     */
    @Test
    public void tstExecuteRequestStatusCodeFailure() throws Exception {
        when(mockResponse.getResponsePhrase()).thenReturn(Optional.of("Bad"));
        when(mockResponse.getStatusCode()).thenReturn(OptionalInt.of(500));
        when(mockRequest.executeRequest()).thenReturn(mockResponse);

        java.lang.reflect.Method method =
                IssueTso.class.getDeclaredMethod("executeRequest", ZosmfRequest.class, String.class);
        method.setAccessible(true);

        Exception thrown = assertThrows(Exception.class,
                () -> method.invoke(issueTso, mockRequest, "Failed"));

        // unwrap if it's an InvocationTargetException
        Throwable cause = thrown instanceof java.lang.reflect.InvocationTargetException
                ? thrown.getCause()
                : thrown;

        assertInstanceOf(ZosmfRequestException.class, cause);
    }

    /**
     * Verifies that processTsoData correctly extracts TSO MESSAGE and TSO PROMPT
     * from a JSON structure and stores them in internal lists.
     */
    @Test
    public void testProcessTsoDataSuccess() throws Exception {
        String json = "{ \"tsoData\": [" +
                "{ \"TSO MESSAGE\": { \"DATA\": \"Hello\" } }," +
                "{ \"TSO PROMPT\": { \"HIDDEN\": \"Yes\" } }" +
                "] }";
        JsonNode node = new ObjectMapper().readTree(json).get("tsoData");

        java.lang.reflect.Method method = IssueTso.class.getDeclaredMethod("processTsoData", JsonNode.class);
        method.setAccessible(true);
        method.invoke(issueTso, node);

        // verify private lists are updated
        java.lang.reflect.Field msgLstField = IssueTso.class.getDeclaredField("msgLst");
        msgLstField.setAccessible(true);
        List<String> msgLst = (List<String>) msgLstField.get(issueTso);

        java.lang.reflect.Field promptLstField = IssueTso.class.getDeclaredField("promptLst");
        promptLstField.setAccessible(true);
        List<String> promptLst = (List<String>) promptLstField.get(issueTso);

        assertEquals(List.of("Hello"), msgLst);
        assertEquals(List.of("Yes"), promptLst);
    }

    /**
     * Verifies that issueCommand executes the full TSO command flow, including start,
     * send command, send follow-up, and stop, and correctly returns the collected TSO messages.
     */
    @Test
    public void tstIssueCommandFullSuccess() throws Exception {
        ZosmfRequest mockRequest = mock(ZosmfRequest.class);

        Response start = mock(Response.class);
        when(start.getResponsePhrase()).thenReturn(Optional.of("{\"servletKey\":\"KEY123\"}"));
        when(start.getStatusCode()).thenReturn(OptionalInt.of(200));

        Response sendCmd = mock(Response.class);
        when(sendCmd.getResponsePhrase()).thenReturn(Optional.of(
                "{ \"tsoData\": [{ \"TSO MESSAGE\": { \"DATA\": \"Message1\" } }] }"));
        when(sendCmd.getStatusCode()).thenReturn(OptionalInt.of(200));

        Response send = mock(Response.class);
        when(send.getResponsePhrase()).thenReturn(Optional.of(
                "{ \"tsoData\": [{ \"TSO PROMPT\": { \"HIDDEN\": \"Prompt1\" } }] }"));
        when(send.getStatusCode()).thenReturn(OptionalInt.of(200));

        Response stop = mock(Response.class);
        when(stop.getResponsePhrase()).thenReturn(Optional.of("{\"stopped\":true}"));
        when(stop.getStatusCode()).thenReturn(OptionalInt.of(200));

        try (MockedStatic<ZosmfRequestFactory> mockedFactory = mockStatic(ZosmfRequestFactory.class)) {
            mockedFactory.when(() -> ZosmfRequestFactory.buildRequest(any(), any(ZosmfRequestType.class)))
                    .thenReturn(mockRequest);

            // Return the four responses in the exact order the code calls executeRequest():
            // startTso -> sendTsoCommand -> sendTso -> stopTso
            when(mockRequest.executeRequest()).thenReturn(start, sendCmd, send, stop);

            // The ZosConnection mock passed into IssueTso is a dummy dependency —
            // it never actually matters in this test, because all request-building logic is intercepted by
            // the mocked static factory.
            IssueTso issueTso = new IssueTso(mock(ZosConnection.class), "ACCT123");

            List<String> messages = issueTso.issueCommand("LU TESTUSER");
            assertEquals(List.of("Message1"), messages);

            verify(mockRequest, times(4)).executeRequest();
            // optional: verify factory call counts
            mockedFactory.verify(() -> ZosmfRequestFactory.buildRequest(any(), eq(ZosmfRequestType.POST_JSON)), times(1));
            mockedFactory.verify(() -> ZosmfRequestFactory.buildRequest(any(), eq(ZosmfRequestType.PUT_JSON)), atLeast(2));
            mockedFactory.verify(() -> ZosmfRequestFactory.buildRequest(any(), eq(ZosmfRequestType.DELETE_JSON)), times(1));
        }
    }

}
