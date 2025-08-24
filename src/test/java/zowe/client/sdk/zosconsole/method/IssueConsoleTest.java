/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.method;

import kong.unirest.core.Cookie;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.zosconsole.input.IssueConsoleInputData;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/**
 * Class containing unit tests for IssueCommand.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class IssueConsoleTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonGetRequest;

    @Before
    public void init() {
        mockJsonGetRequest = Mockito.mock(PutJsonZosmfRequest.class);
    }

    @Test
    public void tstIssueConsoleIssueCommandCmdResponseSuccess() throws ZosmfRequestException {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "student");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        final ConsoleResponse response = issueCommand.issueCommand("command");
        assertEquals("student", response.getCommandResponse().orElse("n/a"));
    }

    @Test
    public void tstIssueConsoleIssueCommandCmdResponseToggleTokenSuccess() throws ZosmfRequestException {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "student");
        final JSONObject json = new JSONObject(jsonMap);

        PutJsonZosmfRequest mockJsonGetRequestAuth = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonGetRequestAuth.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestAuth).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestAuth).setStandardHeaders();
        doCallRealMethod().when(mockJsonGetRequestAuth).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestAuth).getHeaders();

        final IssueConsole issueCommand = new IssueConsole(tokenConnection, mockJsonGetRequestAuth);

        ConsoleResponse response = issueCommand.issueCommand("command");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonGetRequestAuth.getHeaders().toString());
        assertEquals("student", response.getCommandResponse().orElse("n/a"));
    }

    @Test
    public void tstIssueConsoleIssueCommandCmdResponseWithInvalidBasePathFailure() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("1", "1", "1", "1", "consoles//");
        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        final IssueConsole issueCommand = new IssueConsole(connection, request);
        assertThrows(IllegalArgumentException.class, () -> issueCommand.issueCommand("command"));
    }

    @Test
    public void tstIssueConsoleIssueCommandCmdResponseWithEmptyStringSuccess() throws ZosmfRequestException {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        final ConsoleResponse response = issueCommand.issueCommand("command");
        assertEquals("", response.getCommandResponse().orElse("n/a"));
    }

    @Test
    public void tstIssueConsoleIssueCommandCmdResponseWithEmptyStringAndIsProcessRequestSuccess() throws ZosmfRequestException {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        final IssueConsoleInputData consoleInputData = new IssueConsoleInputData("command");
        consoleInputData.setProcessResponse();
        consoleInputData.setSolKey("foo");
        final ConsoleResponse response = issueCommand.issueCommandCommon("consolename", consoleInputData);
        assertEquals("", response.getCommandResponse().orElse("n/a"));
    }

    @Test
    public void tstIssueConsoleIssueCommandCmdResponseUrlSuccess() throws ZosmfRequestException {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response-url", "student");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        ConsoleResponse response = issueCommand.issueCommand("command");
        assertEquals("student", response.getCmdResponseUrl().orElse("n/a"));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void tstIssueConsoleIssueCommandHttpErrorFailure() throws ZosmfRequestException {
        final HttpResponse<JsonNode> mockReply = Mockito.mock(HttpResponse.class);
        Mockito.when(mockReply.getStatusText()).thenReturn("Unauthorized");
        Mockito.when(mockReply.getStatus()).thenReturn(401);
        Mockito.when(mockReply.getBody()).thenReturn(null);

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errorMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (ZosmfRequestException e) {
            errorMsg = e.getMessage();
        }

        Mockito.when(mockJsonGetRequest.executeRequest()).thenThrow(new IllegalStateException(errorMsg));

        final IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        try {
            issueCommand.issueCommand("test");
        } catch (IllegalStateException e) {
            errorMsg = String.valueOf(e);
        }
        final String expectedMsg = "java.lang.IllegalStateException: http status error code: 401, " +
                "status text: Unauthorized, response phrase: null";
        assertEquals(expectedMsg, errorMsg);
    }

    @Test
    public void tstIssueConsoleNullConnectionFailure() {
        try {
            new IssueConsole(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

    @Test
    public void tstIssueConsoleSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        IssueConsole issueConsole = new IssueConsole(connection, request);
        assertNotNull(issueConsole);
    }

    @Test
    public void tstIssueConsoleSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> new IssueConsole(null, request)
        );
        Assertions.assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstIssueConsoleSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> new IssueConsole(connection, null)
        );
        Assertions.assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstIssueConsoleSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = Assertions.assertThrows(
                IllegalStateException.class,
                () -> new IssueConsole(connection, request)
        );
        Assertions.assertEquals("PUT_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstIssueConsolePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        IssueConsole issueConsole = new IssueConsole(connection);
        assertNotNull(issueConsole);
    }

    @Test
    public void tstDsnCopyPrimaryConstructorWithNullConnection() {
        // When/Then
        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> new IssueConsole(null)
        );
        Assertions.assertEquals("connection is null", exception.getMessage());
    }

}
