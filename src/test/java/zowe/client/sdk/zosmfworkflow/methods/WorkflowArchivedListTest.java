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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfworkflow.input.WorkflowListArchivedInputData;
import zowe.client.sdk.zosmfworkflow.response.WorkflowArchivedResponse;
import zowe.client.sdk.zosmfworkflow.types.OrderByType;
import zowe.client.sdk.zosmfworkflow.types.ViewType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for WorkflowListArchived.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class WorkflowArchivedListTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private GetJsonZosmfRequest mockJsonGetRequest;
    private GetJsonZosmfRequest mockJsonGetRequestToken;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void init() throws ZosmfRequestException {
        final JSONObject archivedObj = new JSONObject();
        archivedObj.put("archivedWorkflows", new JSONArray());

        mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(archivedObj, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequest).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequest).getUrl();

        mockJsonGetRequestToken = Mockito.mock(GetJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonGetRequestToken.executeRequest()).thenReturn(
                new Response(archivedObj, 200, "success"));
        doCallRealMethod().when(mockJsonGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonGetRequestToken).getUrl();
    }

    @Test
    public void tstWorkflowListArchivedSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        boolean noError = false;
        try {
            new WorkflowArchivedList(connection, request);
        } catch (IllegalStateException e) {
            noError = true;
        }
        assertFalse(noError);
    }

    @Test
    public void tstWorkflowListArchivedSecondaryConstructorWithNullConnectionFailure() {
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowArchivedList(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedSecondaryConstructorWithNullRequestFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowArchivedList(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedSecondaryConstructorWithInvalidRequestTypeFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new WorkflowArchivedList(connection, request)
        );
        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowArchivedList(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedGetSuccess() throws ZosmfRequestException {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequest);
        final List<WorkflowArchivedResponse> result = workflowArchivedList.get();
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetTokenSuccess() throws ZosmfRequestException {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequestToken);
        final List<WorkflowArchivedResponse> result = workflowArchivedList.get();
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonGetRequestToken.getHeaders().toString());
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc",
                mockJsonGetRequestToken.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetByOrderBySuccess() throws ZosmfRequestException {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequest);
        final List<WorkflowArchivedResponse> result = workflowArchivedList.getByOrderBy(OrderByType.ASC);
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=asc",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetByViewSuccess() throws ZosmfRequestException {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequest);
        final List<WorkflowArchivedResponse> result = workflowArchivedList.getByView(ViewType.DOMAIN);
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc&view=domain",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetCommonSuccess() throws ZosmfRequestException {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequest);
        final WorkflowListArchivedInputData inputData = WorkflowListArchivedInputData.builder()
                .orderBy(OrderByType.DESC)
                .view(ViewType.USER)
                .build();
        final List<WorkflowArchivedResponse> result = workflowArchivedList.getCommon(inputData);
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc&view=user",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetNullInputFailure() {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequest);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowArchivedList.getCommon(null)
        );
        assertEquals("listInputData is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedGetByOrderByNullFailure() {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequest);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowArchivedList.getByOrderBy(null)
        );
        assertEquals("orderByType is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedGetByViewNullFailure() {
        final WorkflowArchivedList workflowArchivedList =
                new WorkflowArchivedList(connection, mockJsonGetRequest);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowArchivedList.getByView(null)
        );
        assertEquals("viewType is null", exception.getMessage());
    }

}
