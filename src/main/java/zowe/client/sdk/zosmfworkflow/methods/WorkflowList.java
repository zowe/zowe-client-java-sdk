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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.UrlConstants;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;
import zowe.client.sdk.zosmfworkflow.input.WorkflowListArchivedInputData;
import zowe.client.sdk.zosmfworkflow.input.WorkflowListInputData;
import zowe.client.sdk.zosmfworkflow.response.WorkflowArchivedResponse;
import zowe.client.sdk.zosmfworkflow.response.WorkflowListResponse;
import zowe.client.sdk.zosmfworkflow.types.OrderByType;
import zowe.client.sdk.zosmfworkflow.types.ViewType;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Handles retrieval of workflows from z/OSMF.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-workflows-system-sysplex">z/OSMF REST API LIST</a>
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-archived-workflows-system">z/OSMF REST API LIST ARCHIVED</a>
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class WorkflowList {

    private static final String ARCHIVED_WORKFLOW_CONTEXT = "getArchivedCommon";
    private static final String ARCHIVED_WORKFLOWS = "archivedWorkflows";
    private static final String WORKFLOW_CONTEXT = "getWorkflowsCommon";
    private static final String WORKFLOWS = "workflows";
    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * WorkflowList constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Muhammad Imran
     */
    public WorkflowList(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
    }

    /**
     * Alternative WorkflowList constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link GetJsonZosmfRequest} implementation object
     * @author Muhammad Imran
     */
    WorkflowList(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get all workflows for a system or sysplex on z/OS.
     *
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public List<WorkflowListResponse> getWorkflows() throws ZosmfRequestException {
        return getWorkflowsCommon(WorkflowListInputData.builder().build());
    }

    /**
     * Get all workflows for a system or sysplex on z/OS filtered by workflow name.
     *
     * @param workflowName workflow name; a regular expression can be specified to match desired workflow names
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public List<WorkflowListResponse> getWorkflowsByName(final String workflowName) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(workflowName, "workflowName");
        return getWorkflowsCommon(WorkflowListInputData.builder().workflowName(workflowName).build());
    }

    /**
     * Get all workflows for a system or sysplex on z/OS filtered by owner.
     *
     * @param owner workflow owner, a valid z/OS user ID
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public List<WorkflowListResponse> getWorkflowsByOwner(final String owner) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(owner, "owner");
        return getWorkflowsCommon(WorkflowListInputData.builder().owner(owner).build());
    }

    /**
     * Get all workflows for a system or sysplex on z/OS filtered by system.
     *
     * @param system nickname of the system on which the workflow is to be performed
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public List<WorkflowListResponse> getWorkflowsBySystem(final String system) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(system, "system");
        return getWorkflowsCommon(WorkflowListInputData.builder().system(system).build());
    }

    /**
     * Get all workflows for a system or sysplex on z/OS with full input control.
     *
     * @param listInputData workflow list input parameters
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Ashish Kumar Dash
     */
    public List<WorkflowListResponse> getWorkflowsCommon(final WorkflowListInputData listInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(listInputData, "listInputData");

        // all query parameters are optional; a joiner keeps the "?" and "&" delimiters correct for any combination
        final StringJoiner queryParams = new StringJoiner(UrlConstants.COMBO_ID);
        listInputData.getWorkflowName()
                .ifPresent(name -> queryParams.add("workflowName=" + getEncodeURIComponent(name)));
        listInputData.getCategory()
                .ifPresent(category -> queryParams.add("category=" + category.getValue()));
        listInputData.getSystem()
                .ifPresent(system -> queryParams.add("system=" + getEncodeURIComponent(system)));
        listInputData.getStatusName()
                .ifPresent(status -> queryParams.add("statusName=" + status.getValue()));
        listInputData.getOwner()
                .ifPresent(owner -> queryParams.add("owner=" + getEncodeURIComponent(owner)));
        listInputData.getVendor()
                .ifPresent(vendor -> queryParams.add("vendor=" + getEncodeURIComponent(vendor)));

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl() +
                WorkflowConstants.WORKFLOWS_RESOURCE);
        if (queryParams.length() > 0) {
            url.append(UrlConstants.QUERY_ID).append(queryParams);
        }

        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no list workflows response phrase"))
                .toString();

        final List<WorkflowListResponse> results = new ArrayList<>();
        final JsonNode root = JsonUtils.parse(responsePhrase);
        final ArrayNode nodes = JsonUtils.getArrayByField(root, WORKFLOWS);

        for (final JsonNode node : nodes) {
            results.add(JsonUtils.parseResponse(
                    node.toString(),
                    WorkflowListResponse.class,
                    WORKFLOW_CONTEXT));
        }

        return results;
    }

    /**
     * Get all archived workflows on z/OS using default ordering.
     *
     * @return list of WorkflowArchivedResponse objects
     * @throws ZosmfRequestException request error state
     * @author Muhammad Imran
     */
    public List<WorkflowArchivedResponse> getArchived() throws ZosmfRequestException {
        return getArchivedCommon(WorkflowListArchivedInputData.builder().build());
    }

    /**
     * Get all archived workflows on z/OS ordered by the given order type.
     *
     * @param orderByType order type for sorting archived workflow instances
     * @return list of WorkflowArchivedResponse objects
     * @throws ZosmfRequestException request error state
     * @author Muhammad Imran
     */
    public List<WorkflowArchivedResponse> getArchivedByOrderBy(final OrderByType orderByType)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(orderByType, "orderByType");
        return getArchivedCommon(WorkflowListArchivedInputData.builder().orderBy(orderByType).build());
    }

    /**
     * Get all archived workflows on z/OS filtered by the given view type.
     *
     * @param viewType view type for filtering archived workflow instances
     * @return list of WorkflowArchivedResponse objects
     * @throws ZosmfRequestException request error state
     * @author Muhammad Imran
     */
    public List<WorkflowArchivedResponse> getArchivedByView(final ViewType viewType) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(viewType, "viewType");
        return getArchivedCommon(WorkflowListArchivedInputData.builder().view(viewType).build());
    }

    /**
     * Get all archived workflows on z/OS with full input control.
     *
     * @param listInputData workflow list archived input parameters
     * @return list of WorkflowArchivedResponse objects
     * @throws ZosmfRequestException request error state
     * @author Muhammad Imran
     */
    public List<WorkflowArchivedResponse> getArchivedCommon(final WorkflowListArchivedInputData listInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(listInputData, "listInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl() +
                WorkflowConstants.ARCHIVED_WORKFLOWS_RESOURCE);

        // orderBy is always present; WorkflowListArchivedInputData enforces this invariant
        listInputData.getOrderBy().ifPresent(orderBy -> url.append("?orderBy=").append(orderBy.getValue()));
        listInputData.getView().ifPresent(view -> url.append("&view=").append(view.getValue()));

        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no list archived workflows response phrase"))
                .toString();

        final List<WorkflowArchivedResponse> results = new ArrayList<>();
        final JsonNode root = JsonUtils.parse(responsePhrase);
        final ArrayNode nodes = JsonUtils.getArrayByField(root, ARCHIVED_WORKFLOWS);

        for (final JsonNode node : nodes) {
            results.add(JsonUtils.parseResponse(
                    node.toString(),
                    WorkflowArchivedResponse.class,
                    ARCHIVED_WORKFLOW_CONTEXT));
        }

        return results;
    }

    /**
     * Helper wrapper method.
     *
     * @param str String value
     * @return string value
     * @author Frank Giordano
     */
    private static String getEncodeURIComponent(final String str) {
        return EncodeUtils.encodeURIComponent(str);
    }

}
