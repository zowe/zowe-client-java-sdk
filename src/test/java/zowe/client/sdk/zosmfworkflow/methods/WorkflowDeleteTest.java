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

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * Class containing unit tests for WorkflowDelete.
 * <p>
 * Version: 7.0
 * Author: Adithe Das
 */
public class WorkflowDeleteTest {

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        WorkflowDelete workflowDelete = new WorkflowDelete(connection, request);
        assertNotNull(workflowDelete);
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new WorkflowDelete(null, request));
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new WorkflowDelete(connection, null));
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new WorkflowDelete(connection, request));
        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeletePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        WorkflowDelete workflowDelete = new WorkflowDelete(connection);
        assertNotNull(workflowDelete);
    }

    @Test
    public void tstWorkflowDeletePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new WorkflowDelete(null));
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSuccess() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        DeleteJsonZosmfRequest mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockDeleteRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 200, "success"));

        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();

        WorkflowDelete workflowDelete = new WorkflowDelete(connection, mockDeleteRequest);
        Response response = workflowDelete.delete("TESTKEY");

        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
    }

    @Test
    public void tstWorkflowDeleteWithNullWorkflowKey() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        WorkflowDelete workflowDelete = new WorkflowDelete(connection);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> workflowDelete.delete(null));
        assertEquals("workflowKey is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorObjectCreated() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DeleteJsonZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        assertNotNull(new WorkflowDelete(connection, request));
    }

    @Test
    public void tstWorkflowDeleteUrlContainsWorkflowKey() throws ZosmfRequestException {

        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        DeleteJsonZosmfRequest mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockDeleteRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 200, "success"));

        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();

        WorkflowDelete workflowDelete = new WorkflowDelete(connection, mockDeleteRequest);
        workflowDelete.delete("MYWORKFLOW");
        assertTrue(mockDeleteRequest.getUrl().contains("MYWORKFLOW"));
    }

    @Test
    public void tstWorkflowDeleteUrlGeneration() throws ZosmfRequestException {

        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        DeleteJsonZosmfRequest mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockDeleteRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 200, "success"));

        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();

        WorkflowDelete workflowDelete = new WorkflowDelete(connection, mockDeleteRequest);

        workflowDelete.delete("TESTKEY");
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/TESTKEY", mockDeleteRequest.getUrl());
    }
}
