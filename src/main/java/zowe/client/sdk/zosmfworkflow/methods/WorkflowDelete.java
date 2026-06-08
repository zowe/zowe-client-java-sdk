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
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;

/**
 * Delete a z/OSMF workflow instance.
 */
public class WorkflowDelete {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * WorkflowDelete constructor.
     *
     * @param connection z/OS connection
     */
    public WorkflowDelete(final ZosConnection connection) {

        ValidateUtils.checkNullParameter(connection, "connection");

        this.connection = connection;
    }

    /**
     * Alternative WorkflowDelete constructor for unit testing.
     *
     * @param connection z/OS connection
     * @param request z/OSMF request instance
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
     * @param workflowKey workflow key
     * @return response object
     * @throws ZosmfRequestException error executing request
     */
    public Response delete(final String workflowKey)

            throws ZosmfRequestException {

        ValidateUtils.checkNullParameter(workflowKey, "workflowKey");

        final String url =
                connection.getZosmfUrl()
                        + WorkflowConstants.RESOURCE
                        + "/"
                        + workflowKey;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(
                    connection,
                    ZosmfRequestType.DELETE_JSON);
        }

        request.setUrl(url);

        return request.executeRequest();
    }
}
