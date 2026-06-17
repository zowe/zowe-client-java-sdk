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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosworkflows.WorkflowConstants;

/**
 * Provides z/OSMF workflow list functionality.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-workflows-system-sysplex">z/OSMF REST API</a>
 *
 * @author Jorge Samaniego
 * @version 7.0
 */
public class WorkflowList {

    private final ZosConnection connection;
    private ZosmfRequest request;
    private String url;

    /**
     * WorkflowList constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Jorge Samaniego
     */
    public WorkflowList(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowList constructor with ZosmfRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZosmfRequest Interface object
     * @author Jorge Samaniego
     */
    WorkflowList(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        this.request = request;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
    }

    /**
     * List all workflows.
     *
     * @return JSON string response containing list of workflows
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public String listAll() throws ZosmfRequestException {
        return listCommon(null, null, null, null, null, null);
    }

    /**
     * List workflows filtered by workflow name.
     *
     * @param workflowName workflow name or regular expression to match workflow names
     * @return JSON string response containing list of workflows
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public String listByName(final String workflowName) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(workflowName, "workflowName");
        return listCommon(workflowName, null, null, null, null, null);
    }

    /**
     * List workflows filtered by owner.
     *
     * @param owner workflow owner (a valid z/OS user ID)
     * @return JSON string response containing list of workflows
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public String listByOwner(final String owner) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(owner, "owner");
        return listCommon(null, null, null, null, owner, null);
    }

    /**
     * List workflows filtered by system.
     *
     * @param system nickname of the system on which the workflow is to be performed
     * @return JSON string response containing list of workflows
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public String listBySystem(final String system) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(system, "system");
        return listCommon(null, null, system, null, null, null);
    }

    /**
     * List workflows with all available filter options.
     *
     * @param workflowName workflow name or regular expression, can be null
     * @param category     category of the workflow (general or configuration), can be null
     * @param system       nickname of the system, can be null
     * @param statusName   workflow status (in-progress, complete, automation-in-progress, canceled), can be null
     * @param owner        workflow owner, can be null
     * @param vendor       name of the vendor that provided the workflow definition file, can be null
     * @return JSON string response containing list of workflows
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public String listCommon(final String workflowName, final String category, final String system,
                             final String statusName, final String owner, final String vendor)
            throws ZosmfRequestException {
        url = connection.getZosmfUrl() + WorkflowConstants.RESOURCE;

        boolean firstParam = true;
        if (workflowName != null && !workflowName.isEmpty()) {
            url += WorkflowConstants.QUERY_ID + WorkflowConstants.QUERY_WORKFLOW_NAME + workflowName;
            firstParam = false;
        }
        if (category != null && !category.isEmpty()) {
            url += (firstParam ? WorkflowConstants.QUERY_ID : WorkflowConstants.COMBO_ID) +
                    WorkflowConstants.QUERY_CATEGORY + category;
            firstParam = false;
        }
        if (system != null && !system.isEmpty()) {
            url += (firstParam ? WorkflowConstants.QUERY_ID : WorkflowConstants.COMBO_ID) +
                    WorkflowConstants.QUERY_SYSTEM + system;
            firstParam = false;
        }
        if (statusName != null && !statusName.isEmpty()) {
            url += (firstParam ? WorkflowConstants.QUERY_ID : WorkflowConstants.COMBO_ID) +
                    WorkflowConstants.QUERY_STATUS_NAME + statusName;
            firstParam = false;
        }
        if (owner != null && !owner.isEmpty()) {
            url += (firstParam ? WorkflowConstants.QUERY_ID : WorkflowConstants.COMBO_ID) +
                    WorkflowConstants.QUERY_OWNER + owner;
            firstParam = false;
        }
        if (vendor != null && !vendor.isEmpty()) {
            url += (firstParam ? WorkflowConstants.QUERY_ID : WorkflowConstants.COMBO_ID) +
                    WorkflowConstants.QUERY_VENDOR + vendor;
        }

        if (request == null || !(request instanceof GetJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        return request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no list workflows response phrase"))
                .toString();
    }

    /**
     * Get url specified for rest processing.
     *
     * @return url
     * @author Jorge Samaniego
     */
    public String getUrl() {
        return url;
    }

}