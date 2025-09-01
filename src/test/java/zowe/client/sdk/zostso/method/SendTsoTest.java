/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ResponseUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TsoSendService class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class SendTsoTest {

    private final ZosConnection mockConnection = mock(ZosConnection.class);
    private final PutJsonZosmfRequest mockPutRequest = mock(PutJsonZosmfRequest.class);

    public SendTsoTest() {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf:1443");
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMocks();
        // This ensures any leftover static mocks are cleared between tests
        Mockito.clearAllCaches();
    }

    /**
     * Test sendCommand sets the correct headers in the request.
     */
    @Test
    public void tstSendCommandSetsCorrectHeadersSuccess() throws Exception {
        PutJsonZosmfRequest putJsonZosmfRequest = Mockito.mock(
                PutJsonZosmfRequest.class,
                withSettings().useConstructor(
                        ZosConnectionFactory.createBasicConnection("1", "1", "1", "1")
                )
        );

        doCallRealMethod().when(putJsonZosmfRequest).setStandardHeaders();
        doCallRealMethod().when(putJsonZosmfRequest).setHeaders(anyMap());
        doCallRealMethod().when(putJsonZosmfRequest).getHeaders();

        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{}");

            final SendTso sendTso = new SendTso(mockConnection, putJsonZosmfRequest);
            sendTso.sendCommand("SERVKEY123", "LISTCAT");

            Map<String, String> headers = putJsonZosmfRequest.getHeaders();

            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("true", headers.get("X-CSRF-ZOSMF-HEADER"));
            String authHeaderValue = headers.get("Authorization");
            assertNotNull(authHeaderValue);
            assertTrue(authHeaderValue.startsWith("Basic "));
        }
    }

    /**
     * Test sendCommand sets the correct URL in the request.
     */
    @Test
    public void tstSendCommandSetsCorrectUrlAndBodySuccess() throws Exception {
        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).setBody(any());
        doCallRealMethod().when(mockPutRequest).getUrl();

        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{}");

            final SendTso sendTso = new SendTso(mockConnection, mockPutRequest);
            sendTso.sendCommand("SERVKEY123", "LISTCAT");

            final String actualUrl = mockPutRequest.getUrl();

            assertEquals("https://zosmf:1443/tsoApp/tso/SERVKEY123?readReply=false", actualUrl);
        }
    }

    /**
     * Test sendCommand builds a new request when the request is null.
     * Test does not use alternative constructor.
     */
    @Test
    public void tstSendCommandBuildsNewRequestWhenRequestIsNullSuccess() throws Exception {
        try (MockedStatic<ZosmfRequestFactory> factoryMock = mockStatic(ZosmfRequestFactory.class);
             MockedStatic<ResponseUtil> responseMock = mockStatic(ResponseUtil.class)) {

            factoryMock.when(() -> ZosmfRequestFactory.buildRequest(mockConnection, ZosmfRequestType.PUT_JSON))
                    .thenReturn(mockPutRequest);
            responseMock.when(() -> ResponseUtil.getResponseStr(eq(mockPutRequest), anyString()))
                    .thenReturn("{\"status\":\"ok\"}");

            final SendTso sendTso = new SendTso(mockConnection);
            String result = sendTso.sendCommand("SERVKEY999", "TIME");

            assertEquals("{\"status\":\"ok\"}", result);
            verify(mockPutRequest).setUrl(contains("SERVKEY999"));
            verify(mockPutRequest).setBody("{\"TSO RESPONSE\":{\"VERSION\":\"0100\",\"DATA\":\"TIME\"}}");
        }
    }

    /**
     * Test sendCommand propagates ZosmfRequestException from ResponseUtil.
     */
    @Test
    public void tstSendCommandThrowsZosmfRequestExceptionOnResponseErrorFailure() {
        try (MockedStatic<ResponseUtil> responseMock = mockStatic(ResponseUtil.class)) {
            responseMock.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenThrow(new ZosmfRequestException("Send failed"));

            final SendTso sendTso = new SendTso(mockConnection, mockPutRequest);

            ZosmfRequestException ex = assertThrows(
                    ZosmfRequestException.class,
                    () -> sendTso.sendCommand("SERVKEY123", "LISTCAT")
            );

            assertEquals("Send failed", ex.getMessage());
        }
    }

    /**
     * Test sendCommand succeeds when the response is valid.
     */
    @Test
    public void tstSendCommandSuccess() throws Exception {
        try (MockedStatic<ResponseUtil> responseMock = mockStatic(ResponseUtil.class)) {
            responseMock.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{\"status\":\"ok\"}");

            final SendTso sendTso = new SendTso(mockConnection, mockPutRequest);
            String result = sendTso.sendCommand("SERVKEY123", "LISTCAT");

            assertEquals("{\"status\":\"ok\"}", result);
        }
    }

    /**
     * Test that the alternative constructor throws an exception if the connection is null.
     */
    @Test
    public void tstAlternativeConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new SendTso(null, mockPutRequest)
        );
        assertEquals("connection is null", ex.getMessage());
    }

    /**
     * Test that the alternative constructor throws an exception if the request is null.
     */
    @Test
    public void tstAlternativeConstructorNullRequestFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new SendTso(mockConnection, null)
        );
        assertEquals("request is null", ex.getMessage());
    }

    /**
     * Test that the alternative constructor throws an exception if a request type is not PutJsonZosmfRequest.
     */
    @Test
    public void tstAlternativeConstructorWrongRequestTypeFailure() {
        final ZosmfRequest wrongRequest = mock(ZosmfRequest.class);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> new SendTso(mockConnection, wrongRequest)
        );
        assertEquals("PUT_JSON request type required", ex.getMessage());
    }

    /**
     * Test that sendCommand throws when sessionId is null or empty.
     */
    @Test
    public void tstSendCommandNullSessionIdFailure() {
        final SendTso sendTso = new SendTso(mockConnection, mockPutRequest);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> sendTso.sendCommand(null, "CMD")
        );
        assertEquals("sessionId is either null or empty", ex.getMessage());
    }

    /**
     * Test that sendCommand throws when the command is null or empty.
     */
    @Test
    public void tstSendCommandNullCommandFailure() {
        final SendTso sendTso = new SendTso(mockConnection, mockPutRequest);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> sendTso.sendCommand("SERVKEY", null)
        );
        assertEquals("command is either null or empty", ex.getMessage());
    }

    /**
     * Test that the public constructor throws when the connection is null.
     */
    @Test
    public void tstPublicConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new SendTso(null)
        );
        assertEquals("connection is null", ex.getMessage());
    }
}
