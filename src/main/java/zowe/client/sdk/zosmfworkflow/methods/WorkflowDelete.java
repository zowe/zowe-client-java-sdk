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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;

/**
 * Delete a z/OSMF workflow instance.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-delete-workflow"> z/OSMF REST API </a>
 *
 * @author Adithe Das
 * @author Muhammad Imran
 * @author Frank Giordano
 * @version 7.0
 */
public class WorkflowDelete {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowDelete constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public WorkflowDelete(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowDelete constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    compatible ZosmfRequest interface object
     */
    WorkflowDelete(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Delete a workflow instance.
     *
     * @param workflowKey workflow key of the workflow to delete
     * @return http response object
     * @throws ZosmfRequestException error executing request
     */
    public Response delete(final String workflowKey) throws ZosmfRequestException {
        return deleteCommon(workflowKey, false);
    }

    /**
     * Delete an archived workflow instance.
     *
     * @param workflowKey workflow key of the archived workflow to delete
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response deleteArchived(final String workflowKey) throws ZosmfRequestException {
        return deleteCommon(workflowKey, true);
    }

    /**
     * Common method to control deletion type: workflow or archived workflow instance.
     *
     * @param workflowKey workflow key of either a workflow or archived workflow to delete
     * @return http response object
     * @throws ZosmfRequestException error executing request
     */
    private Response deleteCommon(final String workflowKey, final boolean isArchived) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(workflowKey, "workflowKey");

        final String url = connection.getZosmfUrl() +
                (!isArchived ? WorkflowConstants.WORKFLOWS_RESOURCE : WorkflowConstants.ARCHIVED_WORKFLOWS_RESOURCE) +
                QueryConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(workflowKey);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }

        request.setUrl(url);
        return request.executeRequest();
    }

}
