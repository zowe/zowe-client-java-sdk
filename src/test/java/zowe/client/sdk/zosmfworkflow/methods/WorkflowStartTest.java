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
import org.junit.jupiter.api.BeforeEach;
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
import zowe.client.sdk.zosmfworkflow.response.WorkflowStartResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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
    private PutJsonZosmfRequest mockPutJsonZosmfRequest;
    private PutJsonZosmfRequest mockPutJsonZosmfRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        JSONObject workflowJson = getJsonObject();

        mockPutJsonZosmfRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutJsonZosmfRequest.executeRequest()).thenReturn(
                new Response(workflowJson, 202, "Accepted"));
        doCallRealMethod().when(mockPutJsonZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequest).getUrl();
        doCallRealMethod().when(mockPutJsonZosmfRequest).setBody(any());

        mockPutJsonZosmfRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPutJsonZosmfRequestToken.executeRequest()).thenReturn(
                new Response(workflowJson, 202, "Accepted"));
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).getHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).getUrl();
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setBody(any());
    }

    private static JSONObject getJsonObject() {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("statusMessage", "Workflow started successfully");
        return new JSONObject(jsonMap);
    }

    private static WorkflowStartInputData createInputData() {
        return WorkflowStartInputData.builder()
                .resolveConflictByUsing("outputFileValue")
                .stepName("Step1")
                .performSubsequent(Boolean.TRUE)
                .notificationUrl("https://example.com/notification")
                .targetSystemuid("remoteuser")
                .targetSystempwd("remotepwd")
                .build();
    }

    @Test
    public void testWorkflowStartSuccess() throws Exception {
        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutJsonZosmfRequest);
        final WorkflowStartResponse response = workflowStart.start("workflow-key-123", createInputData());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPutJsonZosmfRequest).setBody(bodyCaptor.capture());
        final String body = bodyCaptor.getValue().toString();
        final JSONObject requestBody = (JSONObject) new JSONParser().parse(body);

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutJsonZosmfRequest.getUrl());
        assertEquals("outputFileValue", requestBody.get("resolveConflictByUsing"));
        assertEquals("Step1", requestBody.get("stepName"));
        assertEquals(Boolean.TRUE, requestBody.get("performSubsequent"));
        assertEquals("https://example.com/notification", requestBody.get("notificationUrl"));
        assertEquals("remoteuser", requestBody.get("targetSystemuid"));
        assertEquals("remotepwd", requestBody.get("targetSystempwd"));

        assertEquals("Workflow started successfully", response.getStatusMessage());
    }

    @Test
    public void testWorkflowStartWithoutInputData() throws Exception {
        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutJsonZosmfRequest);
        final WorkflowStartResponse response = workflowStart.start("workflow-key-123");

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPutJsonZosmfRequest).setBody(bodyCaptor.capture());
        final String body = bodyCaptor.getValue().toString();

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutJsonZosmfRequest.getUrl());
        assertEquals("{}", body);
        assertEquals("Workflow started successfully", response.getStatusMessage());
    }

    @Test
    public void testWorkflowStartWithSpecialCharactersInWorkflowKey() throws Exception {
        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutJsonZosmfRequest);
        final WorkflowStartResponse response = workflowStart.start("workflow key/with spaces", createInputData());

        assertTrue(mockPutJsonZosmfRequest.getUrl().contains("workflow%20key%2Fwith%20spaces"));
        assertEquals("Workflow started successfully", response.getStatusMessage());
    }

    @Test
    public void testWorkflowStartTokenSuccess() throws Exception {
        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutJsonZosmfRequestToken);
        final WorkflowStartResponse response = workflowStart.start("workflow-key-123", createInputData());

        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutJsonZosmfRequestToken.getHeaders().toString());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutJsonZosmfRequestToken.getUrl());
        assertEquals("Workflow started successfully", response.getStatusMessage());
    }

    @Test
    public void testWorkflowStartJsonStringResponseSuccess() throws Exception {
        final JSONObject workflowJson = getJsonObject();
        final PutJsonZosmfRequest mockPutJsonZosmfRequestString = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutJsonZosmfRequestString.executeRequest()).thenReturn(
                new Response(workflowJson.toString(), 202, "Accepted"));
        doCallRealMethod().when(mockPutJsonZosmfRequestString).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequestString).getUrl();
        doCallRealMethod().when(mockPutJsonZosmfRequestString).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutJsonZosmfRequestString);
        final WorkflowStartResponse response = workflowStart.start("workflow-key-123", createInputData());

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutJsonZosmfRequestString.getUrl());
        assertEquals("Workflow started successfully", response.getStatusMessage());
    }

    @Test
    public void testWorkflowStartSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
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
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowStart(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void testWorkflowStartSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowStart(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

    @Test
    public void testWorkflowStartPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
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
    public void testWorkflowStartWithNullWorkflowKey() {
        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutJsonZosmfRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowStart.start(null, createInputData())
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void testWorkflowStartWithEmptyWorkflowKey() {
        final WorkflowStart workflowStart = new WorkflowStart(connection, mockPutJsonZosmfRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowStart.start("", createInputData())
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

}
