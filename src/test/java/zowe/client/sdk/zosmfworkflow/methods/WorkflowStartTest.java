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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.zosmfworkflow.input.WorkflowStartInputData;
import zowe.client.sdk.zosmfworkflow.types.ConflictStartType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for WorkflowStart.
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class WorkflowStartTest {

    private final ObjectMapper mapper = new ObjectMapper();

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));

    private static WorkflowStartInputData createInputData(final String workflowKey) {
        return new WorkflowStartInputData.Builder(workflowKey)
                .resolveConflictByUsing(ConflictStartType.OUTPUT_FILE_VALUE)
                .stepName("Step1")
                .performSubsequent(Boolean.TRUE)
                .notificationUrl("https://example.com/notification")
                .targetSystemuid("remoteuser")
                .targetSystempwd("remotepwd")
                .build();
    }

    @Test
    public void tstWorkflowStartSuccess() throws Exception {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(new Response("{}", 202, "Accepted"));

        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
        doCallRealMethod().when(mockPutRequest).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(mockConnection, mockPutRequest);
        final WorkflowStartInputData inputData = createInputData("workflow-key-123");
        final Response response = workflowStart.startCommon(inputData);

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPutRequest).setBody(bodyCaptor.capture());
        final String body = bodyCaptor.getValue().toString();
        final JsonNode requestBody = mapper.readTree(body);

        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/workflow-key-123/operations/start",
                mockPutRequest.getUrl());
        assertEquals("outputFileValue", requestBody.get("resolveConflictByUsing").asText());
        assertEquals("Step1", requestBody.get("stepName").asText());
        assertEquals(true, requestBody.get("performSubsequent").asBoolean());
        assertEquals("https://example.com/notification", requestBody.get("notificationUrl").asText());
        assertEquals("remoteuser", requestBody.get("targetSystemuid").asText());
        assertEquals("remotepwd", requestBody.get("targetSystempwd").asText());

        assertEquals(202, response.getStatusCode().orElse(-1));
        assertEquals("Accepted", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstWorkflowStartWithOnlyWorkflowKey() throws Exception {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(new Response("{}", 202, "Accepted"));

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
        assertEquals("{}", body);
        assertEquals(202, response.getStatusCode().orElse(-1));
        assertEquals("Accepted", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstWorkflowStartWithSpecialCharactersInWorkflowKey() throws Exception {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(new Response("{}", 202, "Accepted"));

        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
        doCallRealMethod().when(mockPutRequest).setBody(any());

        final WorkflowStart workflowStart = new WorkflowStart(mockConnection, mockPutRequest);
        final WorkflowStartInputData inputData = createInputData("workflow key/with spaces");
        workflowStart.startCommon(inputData);

        assertTrue(mockPutRequest.getUrl().contains("workflow%20key%2Fwith%20spaces"));
    }

    @Test
    public void tstWorkflowStartTokenSuccess() throws Exception {
        PutJsonZosmfRequest mockPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPutRequestToken.executeRequest()).thenReturn(new Response("{}", 202, "Accepted"));

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
    public void tstWorkflowStartSecondaryConstructorWithValidRequestType() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        WorkflowStart workflowStart = new WorkflowStart(connection, request);
        assertNotNull(workflowStart);
    }

    @Test
    public void tstWorkflowStartSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowStart(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowStartSecondaryConstructorWithNullRequest() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowStart(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowStartSecondaryConstructorWithInvalidRequestType() {
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowStart(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowStartPrimaryConstructorWithValidConnection() {
        WorkflowStart workflowStart = new WorkflowStart(connection);
        assertNotNull(workflowStart);
    }

    @Test
    public void tstWorkflowStartPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowStart(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowStartCommonWithNullInputData() {
        final WorkflowStart workflowStart = new WorkflowStart(connection);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowStart.startCommon(null)
        );
        assertEquals("startInputData is null", exception.getMessage());
    }

}
