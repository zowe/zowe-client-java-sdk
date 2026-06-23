/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.methods;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfworkflow.input.WorkflowStartInputData;
import zowe.client.sdk.zosmfworkflow.types.StartConflictType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for WorkflowStart.
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class WorkflowStartTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));

    private static WorkflowStartInputData createInputData(final String workflowKey) {
        return new WorkflowStartInputData.Builder(workflowKey)
                .resolveConflictByUsing(StartConflictType.OUTPUT_FILE_VALUE)
                .stepName("Step1")
                .performSubsequent(Boolean.TRUE)
                .notificationUrl("https://example.com/notification")
                .targetSystemuid("remoteuser")
                .targetSystempwd("remotepwd")
                .build();
    }

    @Test
    public void testWorkflowStartSuccess() throws Exception {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 202, "Accepted"));

        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
        doCallRealMethod().when(mockPutRequest).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(mockConnection, mockPutRequest);
        final WorkflowStartInputData inputData = createInputData("workflow-key-123");
        final Response response = workflowStart.startCommon(inputData);

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPutRequest).setBody(bodyCaptor.capture());
        final String body = bodyCaptor.getValue().toString();
        final JSONObject requestBody = (JSONObject) new JSONParser().parse(body);

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutRequest.getUrl());
        assertEquals("outputFileValue", requestBody.get("resolveConflictByUsing"));
        assertEquals("Step1", requestBody.get("stepName"));
        assertEquals(Boolean.TRUE, requestBody.get("performSubsequent"));
        assertEquals("https://example.com/notification", requestBody.get("notificationUrl"));
        assertEquals("remoteuser", requestBody.get("targetSystemuid"));
        assertEquals("remotepwd", requestBody.get("targetSystempwd"));

        assertEquals(202, response.getStatusCode().orElse(-1));
        assertEquals("Accepted", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void testWorkflowStartWithOnlyWorkflowKey() throws Exception {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 202, "Accepted"));

        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
        doCallRealMethod().when(mockPutRequest).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(mockConnection, mockPutRequest);
        final Response response = workflowStart.start("workflow-key-123");

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPutRequest).setBody(bodyCaptor.capture());
        final String body = bodyCaptor.getValue().toString();

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutRequest.getUrl());
        assertTrue(body.contains("workflow-key-123") || body.equals("{}"));
        assertEquals(202, response.getStatusCode().orElse(-1));
        assertEquals("Accepted", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void testWorkflowStartWithSpecialCharactersInWorkflowKey() throws Exception {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 202, "Accepted"));

        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
        doCallRealMethod().when(mockPutRequest).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(mockConnection, mockPutRequest);
        final WorkflowStartInputData inputData = createInputData("workflow key/with spaces");
        workflowStart.startCommon(inputData);

        assertTrue(mockPutRequest.getUrl().contains("workflow%20key%2Fwith%20spaces"));
    }

    @Test
    public void testWorkflowStartUrlGenerationSuccess() throws ZosmfRequestException {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 202, "Accepted"));

        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
        doCallRealMethod().when(mockPutRequest).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(mockConnection, mockPutRequest);
        workflowStart.start("TESTKEY");

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/TESTKEY/operations/start",
                mockPutRequest.getUrl());
    }

    @Test
    public void testWorkflowStartTokenSuccess() throws Exception {
        PutJsonZosmfRequest mockPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPutRequestToken.executeRequest()).thenReturn(new Response(new JSONObject(), 202, "Accepted"));

        doCallRealMethod().when(mockPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockPutRequestToken).getHeaders();
        doCallRealMethod().when(mockPutRequestToken).getUrl();
        doCallRealMethod().when(mockPutRequestToken).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutRequestToken);
        final Response response = workflowStart.start("workflow-key-123");

        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutRequestToken.getHeaders().toString());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutRequestToken.getUrl());
        assertEquals(202, response.getStatusCode().orElse(-1));
    }

    @Test
    public void testWorkflowStartSecondaryConstructorWithValidRequestType() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        WorkflowStart workflowStart = new WorkflowStart(connection, request);
        assertNotNull(workflowStart);
    }

    @Test
    public void testWorkflowStartSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowStart(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void testWorkflowStartSecondaryConstructorWithNullRequest() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowStart(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void testWorkflowStartSecondaryConstructorWithInvalidRequestType() {
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowStart(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

    @Test
    public void testWorkflowStartPrimaryConstructorWithValidConnection() {
        WorkflowStart workflowStart = new WorkflowStart(connection);
        assertNotNull(workflowStart);
    }

    @Test
    public void testWorkflowStartPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowStart(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void testWorkflowStartCommonWithNullInputData() {
        final WorkflowStart workflowStart = new WorkflowStart(connection);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowStart.startCommon(null)
        );
        assertEquals("startInputData is null", exception.getMessage());
    }

}
