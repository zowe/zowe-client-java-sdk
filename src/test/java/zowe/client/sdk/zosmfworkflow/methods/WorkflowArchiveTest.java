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

import kong.unirest.core.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for WorkflowArchive.
 *
 * Version: 7.0
 * Author: Adithe Das
 */
public class WorkflowArchiveTest {

    @Test
    public void tstWorkflowArchiveSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        WorkflowArchive workflowArchive = new WorkflowArchive(connection, request);
        assertNotNull(workflowArchive);
    }

    @Test
    public void tstWorkflowArchiveSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new WorkflowArchive(null, request));
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowArchiveSecondaryConstructorWithNullRequest() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new WorkflowArchive(connection, null));
        assertEquals("request is null", exception.getMessage());
    }
    @Test
    public void tstWorkflowArchiveSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> new WorkflowArchive(connection, request));
        assertEquals("request not PostJsonZosmfRequest", exception.getMessage());
    }

    @Test
    public void tstWorkflowArchivePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        WorkflowArchive workflowArchive = new WorkflowArchive(connection);
        assertNotNull(workflowArchive);
    }

    @Test
    public void tstWorkflowArchivePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(NullPointerException.class,() -> new WorkflowArchive(null));
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowArchiveSuccess() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        PostJsonZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 200, "success"));

        doCallRealMethod().when(mockRequest).setUrl(any());
        doCallRealMethod().when(mockRequest).getUrl();

        WorkflowArchive workflowArchive = new WorkflowArchive(connection, mockRequest);
        Response response = workflowArchive.archive("TESTKEY");
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n/a"));
    }

    @Test
    public void tstWorkflowArchiveWithEmptyWorkflowKey() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        WorkflowArchive workflowArchive = new WorkflowArchive(connection);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,() -> workflowArchive.archive(""));
        assertEquals("workflowKey is either null or empty",exception.getMessage());
    }

    @Test
    public void tstWorkflowArchiveUrlGeneration() throws ZosmfRequestException {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        PostJsonZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 200, "success"));

        doCallRealMethod().when(mockRequest).setUrl(any());
        doCallRealMethod().when(mockRequest).getUrl();

        WorkflowArchive workflowArchive = new WorkflowArchive(connection, mockRequest);
        workflowArchive.archive("TESTKEY");
        assertTrue(mockRequest.getUrl().contains("/zosmf/workflow/rest/1.0/workflows/TESTKEY/operations/archive"));
    }

}