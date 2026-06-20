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
 * Archive a z/OSMF workflow instance.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-archive-workflow-instance">z/OSMF REST API</a>
 *
 * @author Adithe Das
 * @version 7.0
 */
public class WorkflowArchive {

    public static final String OPERATIONS_RESOURCE = "operations";
    public static final String ARCHIVE_RESOURCE = "archive";
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowArchive Constructor.
     *
     * @param connection ZosConnection object
     */
    public WorkflowArchive(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowArchive constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection z/OS connection information
     * @param request    compatible ZosmfRequest interface object
     */
    WorkflowArchive(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");

        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.connection = connection;
        this.request = request;
    }

    /**
     * Archive a z/OSMF workflow instance.
     *
     * @param workflowKey unique workflow key identifying the workflow instance to archive
     * @return response object
     * @throws ZosmfRequestException error executing request
     */
    public Response archive(final String workflowKey) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(workflowKey, "workflowKey");

        final String url = connection.getZosmfUrl() +
                WorkflowConstants.WORKFLOWS_RESOURCE +
                QueryConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(workflowKey) +
                QueryConstants.URL_PATH_DELIM +
                OPERATIONS_RESOURCE +
                QueryConstants.URL_PATH_DELIM + ARCHIVE_RESOURCE;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }

        request.setUrl(url);
        return request.executeRequest();
    }

}
