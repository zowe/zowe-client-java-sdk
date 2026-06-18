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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for WorkflowDelete.
 *
 * @author Muhammad Imran
 * @author Frank Giordano
 * @version 7.0
 */
public class WorkflowDeleteTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private DeleteJsonZosmfRequest mockJsonDeleteRequest;
    private DeleteJsonZosmfRequest mockJsonDeleteRequestToken;

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        WorkflowDelete workflowDelete = new WorkflowDelete(connection, request);
        assertNotNull(workflowDelete);
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDelete(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithNullRequestFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDelete(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowDelete(connection, request)
        );
        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeletePrimaryConstructorWithValidConnectionSuccess() {
        WorkflowDelete workflowDelete = new WorkflowDelete(connection);
        assertNotNull(workflowDelete);
    }

    @Test
    public void tstWorkflowDeletePrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDelete(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSuccess() throws ZosmfRequestException {
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
    public void tstWorkflowDeleteWithNullWorkflowKeyFailure() {
        WorkflowDelete workflowDelete = new WorkflowDelete(connection);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowDelete.delete(null)
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteSecondaryConstructorObjectCreateSuccess() {
        DeleteJsonZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        boolean noError = false;
        try {
            new WorkflowDelete(connection, request);
        } catch (IllegalStateException e) {
            noError = true;
        }
        assertFalse(noError);
    }

    @Test
    public void tstWorkflowDeleteUrlGenerationSuccess() throws ZosmfRequestException {
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

    @Test
    public void tstArchivedWorkflowDeleteUrlGenerationSuccess() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        DeleteJsonZosmfRequest mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockDeleteRequest.executeRequest()).thenReturn(new Response(new JSONObject(), 200, "success"));

        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();

        WorkflowDelete workflowDelete = new WorkflowDelete(connection, mockDeleteRequest);

        workflowDelete.deleteArchived("TESTKEY");
        assertEquals("https://1:443/zosmf" + WorkflowConstants.ARCHIVED_WORKFLOWS_RESOURCE + "/TESTKEY",
                mockDeleteRequest.getUrl());
    }

    @Test
    public void tstWorkflowDeleteKeyEmptyFailure() {
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        DeleteJsonZosmfRequest mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        WorkflowDelete workflowDelete = new WorkflowDelete(connection, mockDeleteRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowDelete.delete("")
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteArchivedKeyEmptyFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        DeleteJsonZosmfRequest mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        WorkflowDelete workflowDelete = new WorkflowDelete(connection, mockDeleteRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowDelete.deleteArchived("")
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteTokenSuccess() throws ZosmfRequestException {
        DeleteJsonZosmfRequest mockJsonDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockJsonDeleteRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockJsonDeleteRequest).getUrl();

        mockJsonDeleteRequestToken = Mockito.mock(DeleteJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonDeleteRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonDeleteRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonDeleteRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonDeleteRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonDeleteRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonDeleteRequestToken).getUrl();

        WorkflowDelete workflowDelete = new WorkflowDelete(tokenConnection, mockJsonDeleteRequestToken);
        Response response = workflowDelete.delete("test-key");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonDeleteRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/test-key",
                mockJsonDeleteRequestToken.getUrl());
    }

    @Test
    public void tstWorkflowArchivedDeleteTokenSuccess() throws ZosmfRequestException {
        DeleteJsonZosmfRequest mockJsonDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockJsonDeleteRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockJsonDeleteRequest).getUrl();

        mockJsonDeleteRequestToken = Mockito.mock(DeleteJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonDeleteRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonDeleteRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonDeleteRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonDeleteRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonDeleteRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonDeleteRequestToken).getUrl();

        WorkflowDelete workflowDelete = new WorkflowDelete(tokenConnection, mockJsonDeleteRequestToken);
        Response response = workflowDelete.deleteArchived("test-key");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonDeleteRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows/test-key",
                mockJsonDeleteRequestToken.getUrl());
    }

}
