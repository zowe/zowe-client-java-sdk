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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
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

/**
 * Handles retrieval of workflows from z/OSMF.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-archived-workflows-system">z/OSMF REST API</a>
 *
 * @author Muhammad Imran
 * @author Jorge Samaniego
 * @version 7.0
 */
public class WorkflowList {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String ARCHIVED_CONTEXT = "getArchivedCommon";
    private static final String WORKFLOWS_CONTEXT = "getWorkflowsCommon";
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
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
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
        final JsonNode root;
        try {
            root = OBJECT_MAPPER.readTree(responsePhrase);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException("Failed to parse archived workflows response", e);
        }
        final JsonNode nodes = root.path("archivedWorkflows");

        if (nodes.isArray()) {
            for (final JsonNode node : nodes) {
                results.add(JsonUtils.parseResponse(node.toString(), WorkflowArchivedResponse.class, ARCHIVED_CONTEXT));
            }
        }

        return results;
    }

    /**
     * Get all workflows on z/OS.
     *
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public List<WorkflowListResponse> getWorkflows() throws ZosmfRequestException {
        return getWorkflowsCommon(WorkflowListInputData.builder().build());
    }

    /**
     * Get all workflows on z/OS filtered by workflow name.
     *
     * @param workflowName descriptive workflow name to filter by
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public List<WorkflowListResponse> getWorkflowsByName(final String workflowName) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(workflowName, "workflowName");
        return getWorkflowsCommon(WorkflowListInputData.builder().workflowName(workflowName).build());
    }

    /**
     * Get all workflows on z/OS filtered by owner.
     *
     * @param owner workflow owner user ID to filter by
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public List<WorkflowListResponse> getWorkflowsByOwner(final String owner) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(owner, "owner");
        return getWorkflowsCommon(WorkflowListInputData.builder().owner(owner).build());
    }

    /**
     * Get all workflows on z/OS filtered by system.
     *
     * @param system z/OS system name to filter by
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public List<WorkflowListResponse> getWorkflowsBySystem(final String system) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(system, "system");
        return getWorkflowsCommon(WorkflowListInputData.builder().system(system).build());
    }

    /**
     * Get workflows on z/OS with full input control.
     *
     * @param listInputData workflow list input parameters
     * @return list of WorkflowListResponse objects
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public List<WorkflowListResponse> getWorkflowsCommon(final WorkflowListInputData listInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(listInputData, "listInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl() +
                WorkflowConstants.WORKFLOWS_RESOURCE);

        final StringBuilder query = new StringBuilder();
        listInputData.getWorkflowName().ifPresent(v -> appendQueryParam(query, "workflowName", v));
        listInputData.getCategory().ifPresent(v -> appendQueryParam(query, "category", v));
        listInputData.getSystem().ifPresent(v -> appendQueryParam(query, "system", v));
        listInputData.getStatusName().ifPresent(v -> appendQueryParam(query, "statusName", v));
        listInputData.getOwner().ifPresent(v -> appendQueryParam(query, "owner", v));
        listInputData.getVendor().ifPresent(v -> appendQueryParam(query, "vendor", v));
        url.append(query);

        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no list workflows response phrase"))
                .toString();

        final List<WorkflowListResponse> results = new ArrayList<>();
        final JsonNode root;
        try {
            root = OBJECT_MAPPER.readTree(responsePhrase);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException("Failed to parse workflows response", e);
        }
        final JsonNode nodes = root.path("workflows");

        if (nodes.isArray()) {
            for (final JsonNode node : nodes) {
                results.add(JsonUtils.parseResponse(node.toString(), WorkflowListResponse.class, WORKFLOWS_CONTEXT));
            }
        }

        return results;
    }

    /**
     * Append a query parameter to the query string, using '?' for the first
     * parameter and '&' for subsequent parameters. Values are URL-encoded.
     *
     * @param query query string being built
     * @param name  query parameter name
     * @param value query parameter value
     */
    private static void appendQueryParam(final StringBuilder query, final String name, final String value) {
        query.append(query.length() == 0 ? "?" : "&")
                .append(name)
                .append("=")
                .append(EncodeUtils.encodeURIComponent(value));
    }

}