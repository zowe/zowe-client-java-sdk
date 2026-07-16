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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kong.unirest.core.Cookie;
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
import zowe.client.sdk.zosmfworkflow.input.WorkflowListInputData;
import zowe.client.sdk.zosmfworkflow.response.WorkflowArchivedResponse;
import zowe.client.sdk.zosmfworkflow.response.WorkflowListResponse;
import zowe.client.sdk.zosmfworkflow.types.CategoryType;
import zowe.client.sdk.zosmfworkflow.types.OrderByType;
import zowe.client.sdk.zosmfworkflow.types.StatusNameType;
import zowe.client.sdk.zosmfworkflow.types.ViewType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for WorkflowList.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class WorkflowListTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private GetJsonZosmfRequest mockJsonGetRequest;
    private final ObjectMapper mapper = new ObjectMapper();
    private GetJsonZosmfRequest mockJsonGetRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        final ObjectNode archivedObj = mapper.createObjectNode();
        archivedObj.set("archivedWorkflows", mapper.createArrayNode());
        archivedObj.set("workflows", mapper.createArrayNode());

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
    public void tstWorkflowListSecondaryConstructorWithValidRequestTypeSuccess() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        boolean noError = false;
        try {
            new WorkflowList(connection, request);
        } catch (IllegalStateException e) {
            noError = true;
        }
        assertFalse(noError);
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
    public void tstWorkflowListPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new WorkflowList(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListGetWorkflowsSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final List<WorkflowListResponse> result = workflowList.getWorkflows();
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows", mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListGetWorkflowsTokenSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequestToken);
        final List<WorkflowListResponse> result = workflowList.getWorkflows();
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonGetRequestToken.getHeaders().toString());
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows", mockJsonGetRequestToken.getUrl());
    }

    @Test
    public void tstWorkflowListGetWorkflowsByNameSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final List<WorkflowListResponse> result = workflowList.getWorkflowsByName("AutomationExample.*");
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?workflowName=AutomationExample.*",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListGetWorkflowsByOwnerSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final List<WorkflowListResponse> result = workflowList.getWorkflowsByOwner("zosmfad");
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?owner=zosmfad",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListGetWorkflowsBySystemSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final List<WorkflowListResponse> result = workflowList.getWorkflowsBySystem("SY1");
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?system=SY1",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListGetWorkflowsByOwnerEmptyFailure() {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowList.getWorkflowsByOwner("  ")
        );
        assertEquals("owner is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowListGetWorkflowsBySystemEmptyFailure() {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowList.getWorkflowsBySystem(null)
        );
        assertEquals("system is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowListGetWorkflowsCommonSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final WorkflowListInputData inputData = WorkflowListInputData.builder()
                .workflowName("test")
                .category(CategoryType.CONFIGURATION)
                .system("SY1")
                .statusName(StatusNameType.IN_PROGRESS)
                .owner("zosmfad")
                .vendor("IBM")
                .build();
        final List<WorkflowListResponse> result = workflowList.getWorkflowsCommon(inputData);
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/workflows?workflowName=test&category=configuration" +
                        "&system=SY1&statusName=in-progress&owner=zosmfad&vendor=IBM",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListGetWorkflowsResponseParsingSuccess() throws ZosmfRequestException {
        final ObjectNode workflow = mapper.createObjectNode();
        workflow.put("workflowName", "AutomationExample|Canceled|1423679433714");
        workflow.put("workflowKey", "d043b5f1-adab-48e7-b7c3-d41cd95fa4b0");
        workflow.put("workflowDescription", "Sample demonstrating the use of automated steps in workflow.");
        workflow.put("workflowID", "automationSample");
        workflow.put("workflowVersion", "1.0");
        workflow.put("workflowDefinitionFileMD5Value", "a8825b7497793bc620b0edffa8b97cd9");
        workflow.put("instanceURI", "/zosmf/workflow/rest/1.0/workflows/d043b5f1-adab-48e7-b7c3-d41cd95fa4b0");
        workflow.put("owner", "zosmfad");
        workflow.put("vendor", "IBM");
        workflow.put("access", "Public");
        final ObjectNode json = mapper.createObjectNode();
        json.set("workflows", mapper.createArrayNode().add(workflow));

        final GetJsonZosmfRequest mockRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockRequest.executeRequest()).thenReturn(new Response(json, 200, "success"));
        doCallRealMethod().when(mockRequest).setUrl(any());

        final List<WorkflowListResponse> result = new WorkflowList(connection, mockRequest).getWorkflows();
        assertEquals(1, result.size());
        final WorkflowListResponse item = result.get(0);
        assertEquals("AutomationExample|Canceled|1423679433714", item.getWorkflowName());
        assertEquals("d043b5f1-adab-48e7-b7c3-d41cd95fa4b0", item.getWorkflowKey());
        assertEquals("Sample demonstrating the use of automated steps in workflow.", item.getWorkflowDescription());
        assertEquals("automationSample", item.getWorkflowID());
        assertEquals("1.0", item.getWorkflowVersion());
        assertEquals("a8825b7497793bc620b0edffa8b97cd9", item.getWorkflowDefinitionFileMD5Value());
        assertEquals("/zosmf/workflow/rest/1.0/workflows/d043b5f1-adab-48e7-b7c3-d41cd95fa4b0", item.getInstanceURI());
        assertEquals("zosmfad", item.getOwner());
        assertEquals("IBM", item.getVendor());
        assertEquals("Public", item.getAccess());
    }

    @Test
    public void tstWorkflowListGetWorkflowsCommonNullInputFailure() {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowList.getWorkflowsCommon(null)
        );
        assertEquals("listInputData is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListGetWorkflowsByNameEmptyFailure() {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workflowList.getWorkflowsByName("  ")
        );
        assertEquals("workflowName is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedGetSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final List<WorkflowArchivedResponse> result = workflowList.getArchived();
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetTokenSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequestToken);
        final List<WorkflowArchivedResponse> result = workflowList.getArchived();
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonGetRequestToken.getHeaders().toString());
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc",
                mockJsonGetRequestToken.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetByOrderBySuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final List<WorkflowArchivedResponse> result = workflowList.getArchivedByOrderBy(OrderByType.ASC);
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=asc",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetByViewSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final List<WorkflowArchivedResponse> result = workflowList.getArchivedByView(ViewType.DOMAIN);
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc&view=domain",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetCommonSuccess() throws ZosmfRequestException {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        final WorkflowListArchivedInputData inputData = WorkflowListArchivedInputData.builder()
                .orderBy(OrderByType.DESC)
                .view(ViewType.USER)
                .build();
        final List<WorkflowArchivedResponse> result = workflowList.getArchivedCommon(inputData);
        assertTrue(result.isEmpty());
        assertEquals("https://1:443/zosmf/workflow/rest/1.0/archivedworkflows?orderBy=desc&view=user",
                mockJsonGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowListArchivedGetNullInputFailure() {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowList.getArchivedCommon(null)
        );
        assertEquals("listInputData is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedGetByOrderByNullFailure() {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowList.getArchivedByOrderBy(null)
        );
        assertEquals("orderByType is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowListArchivedGetByViewNullFailure() {
        final WorkflowList workflowList =
                new WorkflowList(connection, mockJsonGetRequest);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> workflowList.getArchivedByView(null)
        );
        assertEquals("viewType is null", exception.getMessage());
    }

}
