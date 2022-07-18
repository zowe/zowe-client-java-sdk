package zowe.client.sdk.zosconsole;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosconsole.input.IssueParams;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

public class IssueCommandTest {

    private JsonPutRequest jsonGetRequest;
    private ZOSConnection connection;

    @Before
    public void init() {
        jsonGetRequest = Mockito.mock(JsonPutRequest.class);
        connection = new ZOSConnection("1", "1", "1", "1");
    }

    @Test
    public void tstIssueCommandCmdResponseAttributeSuccess() throws Exception {
        JSONObject json = new JSONObject();
        json.put("cmd-response", "student");
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(new Response(json, 200));
        Mockito.doNothing().when(jsonGetRequest).setRequest(anyString());
        IssueCommand issueCommand = new IssueCommand(connection, jsonGetRequest);
        IssueParams issueParams = new IssueParams();
        issueParams.setCommand("test");
        ConsoleResponse response = issueCommand.issue(issueParams);
        assertEquals("student",
                response.getCommandResponse()
                        .orElse("n/a")
                        .replaceAll("\\r", "")
                        .replaceAll("\\n", ""));
    }

    @Test
    public void tstIssueCommandCmdResponseUrlAttributeSuccess() throws Exception {
        JSONObject json = new JSONObject();
        json.put("cmd-response-url", "student");
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(new Response(json, 200));
        Mockito.doNothing().when(jsonGetRequest).setRequest(anyString());
        IssueCommand issueCommand = new IssueCommand(connection, jsonGetRequest);
        IssueParams issueParams = new IssueParams();
        issueParams.setCommand("test");
        ConsoleResponse response = issueCommand.issue(issueParams);
        assertEquals("student",
                response.getCmdResponseUrl()
                        .orElse("n/a")
                        .replaceAll("\\r", "")
                        .replaceAll("\\n", ""));
    }

    @Test
    public void tstIssueCommandHttpErrorFailure() throws Exception {
        String obj = "Unauthorized";
        Mockito.when(jsonGetRequest.executeRequest()).thenReturn(new Response(obj, 401));
        Mockito.doNothing().when(jsonGetRequest).setRequest(anyString());
        IssueCommand issueCommand = new IssueCommand(connection, jsonGetRequest);
        IssueParams issueParams = new IssueParams();
        issueParams.setCommand("test");
        String errorMsg = "";
        try {
            issueCommand.issue(issueParams);
        } catch (Exception e) {
            errorMsg = e + "";
        }
        assertEquals("java.lang.Exception: Http error code 401 Unauthorized.",
                errorMsg);
    }

}