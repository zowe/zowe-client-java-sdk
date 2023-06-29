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

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosconsole.input.IssueParams;
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

    private JsonPutRequest jsonGetRequest;
    private ZosConnection connection;

    @Before
    public void init() {
        jsonGetRequest = Mockito.mock(JsonPutRequest.class);
        connection = new ZosConnection("1", "1", "1", "1");
    }

    @Test
    public void tstIssueCommandCmdResponseAttributeSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "student");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        final IssueConsole issueCommand = new IssueConsole(connection, jsonGetRequest);
        final IssueParams issueParams = new IssueParams();
        issueParams.setCommand("test");
        final ConsoleResponse response = issueCommand.issueCommand(issueParams);
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
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));
        IssueConsole issueCommand = new IssueConsole(connection, jsonGetRequest);
        IssueParams issueParams = new IssueParams();
        issueParams.setCommand("test");
        ConsoleResponse response = issueCommand.issueCommand(issueParams);
        assertEquals("student",
                response.getCmdResponseUrl()
                        .orElse("n/a")
                        .replaceAll("\\r", "")
                        .replaceAll("\\n", ""));
    }

    @Test
    public void tstIssueCommandHttpErrorFailure() throws Exception {
        final String obj = "Unauthorized";
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(
                new Response(obj, 401, "Unauthorized"));
        final IssueConsole issueCommand = new IssueConsole(connection, jsonGetRequest);
        final IssueParams issueParams = new IssueParams();
        issueParams.setCommand("test");
        String errorMsg = "";
        try {
            issueCommand.issueCommand(issueParams);
        } catch (Exception e) {
            errorMsg = e + "";
        }
        assertEquals("java.lang.Exception: http status error code: 401, status text: Unauthorized", errorMsg);
    }

}
