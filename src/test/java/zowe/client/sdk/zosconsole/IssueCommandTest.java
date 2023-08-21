/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.zosconsole.method.IssueConsole;
import zowe.client.sdk.zosconsole.response.ConsoleResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for IssueCommand.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class IssueCommandTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private PutJsonZosmfRequest mockJsonGetRequest;

    @Before
    public void init() {
        mockJsonGetRequest = Mockito.mock(PutJsonZosmfRequest.class);
    }

    @Test
    public void tstIssueCommandCmdResponseAttributeSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "student");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        final ConsoleResponse response = issueCommand.issueCommand("test");
        assertEquals("student",
                response.getCommandResponse()
                        .orElse("n/a")
                        .replaceAll("\\r", "")
                        .replaceAll("\\n", ""));
    }

    @Test
    public void tstIssueCommandCmdResponseUrlAttributeSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response-url", "student");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        ConsoleResponse response = issueCommand.issueCommand("test");
        assertEquals("student",
                response.getCmdResponseUrl()
                        .orElse("n/a")
                        .replaceAll("\\r", "")
                        .replaceAll("\\n", ""));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void tstIssueCommandHttpErrorFailure() {
        HttpResponse<JsonNode> mockReply = Mockito.mock(HttpResponse.class);
        Mockito.when(mockReply.getStatusText()).thenReturn("Unauthorized");
        Mockito.when(mockReply.getStatus()).thenReturn(401);
        Mockito.when(mockReply.getBody()).thenReturn(null);

        ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errorMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }

        Mockito.when(mockJsonGetRequest.executeRequest()).thenThrow(new IllegalStateException(errorMsg));

        final IssueConsole issueCommand = new IssueConsole(connection, mockJsonGetRequest);
        try {
            issueCommand.issueCommand("test");
        } catch (Exception e) {
            errorMsg = String.valueOf(e);
        }
        final String expectedMsg = "java.lang.IllegalStateException: http status error code: 401, " +
                "status text: Unauthorized, response phrase: null";
        assertEquals(expectedMsg, errorMsg);
    }

}
