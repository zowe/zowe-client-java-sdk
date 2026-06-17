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
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for WorkflowDeleteArchived.
 */
public class WorkflowDeleteArchivedTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private DeleteJsonZosmfRequest mockJsonDeleteRequest;
    private DeleteJsonZosmfRequest mockJsonDeleteRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockJsonDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
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
    }

    @Test
    public void tstWorkflowDeleteArchivedSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        boolean noError = false;
        try {
            new WorkflowDeleteArchived(connection, request);
        } catch (IllegalStateException e) {
            noError = true;
        }
        assertFalse(noError);
    }

    @Test
    public void tstWorkflowDeleteArchivedSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDeleteArchived(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteArchivedSecondaryConstructorWithNullRequestFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDeleteArchived(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteArchivedSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowDeleteArchived(connection, request)
        );
        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteArchivedPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowDeleteArchived(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteArchivedSuccess() throws ZosmfRequestException {
        final WorkflowDeleteArchived workflowDeleteArchived =
                new WorkflowDeleteArchived(connection, mockJsonDeleteRequest);
        final Response response = workflowDeleteArchived.delete("test-key");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows/test-key",
                mockJsonDeleteRequest.getUrl());
    }

    @Test
    public void tstWorkflowDeleteArchivedTokenSuccess() throws ZosmfRequestException {
        final WorkflowDeleteArchived workflowDeleteArchived =
                new WorkflowDeleteArchived(connection, mockJsonDeleteRequestToken);
        final Response response = workflowDeleteArchived.delete("test-key");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonDeleteRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows/test-key",
                mockJsonDeleteRequestToken.getUrl());
    }

    @Test
    public void tstWorkflowDeleteArchivedKeyNullFailure() {
        final WorkflowDeleteArchived workflowDeleteArchived =
                new WorkflowDeleteArchived(connection, mockJsonDeleteRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowDeleteArchived.delete(null)
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowDeleteArchivedKeyEmptyFailure() {
        final WorkflowDeleteArchived workflowDeleteArchived =
                new WorkflowDeleteArchived(connection, mockJsonDeleteRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowDeleteArchived.delete("")
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

}
