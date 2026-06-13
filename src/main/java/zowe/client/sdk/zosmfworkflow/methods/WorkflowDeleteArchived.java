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
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowsConstants;

/**
 * Handles deleting an archived workflow on z/OS.
 * <p><a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-delete-archived-workflow-instance">z/OSMF REST API</a>
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class WorkflowDeleteArchived {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowDeleteArchived constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public WorkflowDeleteArchived(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowDeleteArchived constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     */
    WorkflowDeleteArchived(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Delete an archived workflow on z/OS.
     *
     * @param workflowKey workflow key of the archived workflow to delete
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response delete(final String workflowKey) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(workflowKey, WorkflowsConstants.WORKFLOW_KEY_ILLEGAL_MSG);

        final String url = connection.getZosmfUrl() +
                WorkflowsConstants.RESOURCE + "/archivedworkflows" +
                WorkflowsConstants.FILE_DELIM + workflowKey;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }
        request.setUrl(url);

        return request.executeRequest();
    }

}