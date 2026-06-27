/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflows.methods;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * Class containing unit tests for WorkflowList.
 *
 * @author Jorge Samaniego
 * @version 7.0
 */
public class WorkflowListTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private GetJsonZosmfRequest mockGetJsonZosmfRequest;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockGetJsonZosmfRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetJsonZosmfRequest.executeRequest()).thenReturn(
                new Response("{\"workflows\":[]}", 200, "success"));
        doCallRealMethod().when(mockGetJsonZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockGetJsonZosmfRequest).getUrl();
    }

    @Test
    public void tstWorkflowListAllSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        final String response = workflowList.listAll();
        assertEquals("{\"workflows\":[]}", response);
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows", mockGetJsonZosmfRequest.getUrl());
    }

    @Test
    public void tstWorkflowListByNameSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        final String response = workflowList.listByName("MyWorkflow");
        assertEquals("{\"workflows\":[]}", response);
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?workflowName=MyWorkflow",
                mockGetJsonZosmfRequest.getUrl());
    }

    @Test
    public void tstWorkflowListByOwnerSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        final String response = workflowList.listByOwner("IBMUSER");
        assertEquals("{\"workflows\":[]}", response);
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?owner=IBMUSER",
                mockGetJsonZosmfRequest.getUrl());
    }

    @Test
    public void tstWorkflowListBySystemSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        final String response = workflowList.listBySystem("SY1");
        assertEquals("{\"workflows\":[]}", response);
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?system=SY1",
                mockGetJsonZosmfRequest.getUrl());
    }

    @Test
    public void tstWorkflowListCommonWithAllParamsSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        final String response = workflowList.listCommon(
                "MyWorkflow", "general", "SY1", "in-progress", "IBMUSER", "IBM");
        assertEquals("{\"workflows\":[]}", response);
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows" +
                        "?workflowName=MyWorkflow&category=general&system=SY1" +
                        "&statusName=in-progress&owner=IBMUSER&vendor=IBM",
                mockGetJsonZosmfRequest.getUrl());
    }

    @Test
    public void tstWorkflowListCommonWithSomeParamsSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        final String response = workflowList.listCommon(null, null, "SY1", "complete", null, null);
        assertEquals("{\"workflows\":[]}", response);
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?system=SY1&statusName=complete",
                mockGetJsonZosmfRequest.getUrl());
    }

    @Test
    public void tstWorkflowListSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        WorkflowList workflowList = new WorkflowList(connection, request);
        assertNotNull(workflowList);
    }

    @Test
    public void tstWorkflowListSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowList(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListSecondaryConstructorWithNullRequestFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowList(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowList(connection, request)
        );
        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowListPrimaryConstructorWithValidConnectionSuccess() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        WorkflowList workflowList = new WorkflowList(connection);
        assertNotNull(workflowList);
    }

    @Test
    public void tstWorkflowListPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowList(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListByNameWithNullFailure() {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowList.listByName(null)
        );
        assertTrue(exception.getMessage().contains("workflowName is either null or empty"));
    }

    @Test
    public void tstWorkflowListByNameWithEmptyFailure() {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowList.listByName("")
        );
        assertTrue(exception.getMessage().contains("workflowName is either null or empty"));
    }

    @Test
    public void tstWorkflowListByOwnerWithNullFailure() {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowList.listByOwner(null)
        );
        assertTrue(exception.getMessage().contains("owner is either null or empty"));
    }

    @Test
    public void tstWorkflowListBySystemWithNullFailure() {
        final WorkflowList workflowList = new WorkflowList(connection, mockGetJsonZosmfRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowList.listBySystem(null)
        );
        assertTrue(exception.getMessage().contains("system is either null or empty"));
    }

}