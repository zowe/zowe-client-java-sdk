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
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfworkflow.response.WorkflowCancelResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for WorkflowCancel.
 *
 * @author Jorge Samaniego
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowCancelTest {

    private static final String CANCEL_JSON =
            "{\"workflowName\":\"AutomationExample|Canceled|1423679433714\"}";

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));

    @Test
    public void tstWorkflowCancelSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        WorkflowCancel workflowCancel = new WorkflowCancel(connection, request);
        assertNotNull(workflowCancel);
    }

    @Test
    public void tstWorkflowCancelSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowCancel(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowCancelSecondaryConstructorWithNullRequestFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowCancel(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowCancelSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowCancel(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowCancelPrimaryConstructorWithValidConnectionSuccess() {
        WorkflowCancel workflowCancel = new WorkflowCancel(connection);
        assertNotNull(workflowCancel);
    }

    @Test
    public void tstWorkflowCancelPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowCancel(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowCancelSuccess() throws ZosmfRequestException {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest())
                .thenReturn(new Response(CANCEL_JSON, 200, "success"));
        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();

        WorkflowCancel workflowCancel = new WorkflowCancel(mockConnection, mockPutRequest);
        WorkflowCancelResponse response = workflowCancel.cancel("TESTKEY");

        assertEquals("AutomationExample|Canceled|1423679433714", response.getWorkflowName());
        Mockito.verify(mockPutRequest, Mockito.never()).setBody(any());
    }

    @Test
    public void tstWorkflowCancelWithNullWorkflowKeyFailure() {
        WorkflowCancel workflowCancel = new WorkflowCancel(connection);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowCancel.cancel(null)
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowCancelSecondaryConstructorObjectCreateSuccess() {
        PutJsonZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        boolean noError = false;
        try {
            new WorkflowCancel(connection, request);
        } catch (IllegalStateException e) {
            noError = true;
        }
        assertFalse(noError);
    }

    @Test
    public void tstWorkflowCancelUrlGenerationSuccess() throws ZosmfRequestException {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutRequest.executeRequest())
                .thenReturn(new Response(CANCEL_JSON, 200, "success"));
        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();

        WorkflowCancel workflowCancel = new WorkflowCancel(mockConnection, mockPutRequest);
        workflowCancel.cancel("TESTKEY");

        assertEquals(
                "https://1:443/zosmf/workflow/rest/1.0/workflows/TESTKEY/operations/cancel",
                mockPutRequest.getUrl()
        );
    }

    @Test
    public void tstWorkflowCancelKeyEmptyFailure() {
        ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConnection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        PutJsonZosmfRequest mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);

        WorkflowCancel workflowCancel = new WorkflowCancel(mockConnection, mockPutRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowCancel.cancel("")
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowCancelTokenSuccess() throws ZosmfRequestException {
        PutJsonZosmfRequest mockPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPutRequestToken.executeRequest()).thenReturn(
                new Response(CANCEL_JSON, 200, "success"));
        doCallRealMethod().when(mockPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockPutRequestToken).getHeaders();
        doCallRealMethod().when(mockPutRequestToken).getUrl();

        WorkflowCancel workflowCancel = new WorkflowCancel(tokenConnection, mockPutRequestToken);
        WorkflowCancelResponse response = workflowCancel.cancel("test-key");

        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutRequestToken.getHeaders().toString());
        assertEquals("AutomationExample|Canceled|1423679433714", response.getWorkflowName());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows/test-key/operations/cancel",
                mockPutRequestToken.getUrl());
    }

}
