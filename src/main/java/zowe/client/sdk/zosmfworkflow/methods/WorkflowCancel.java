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
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.UrlConstants;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;
import zowe.client.sdk.zosmfworkflow.response.WorkflowCancelResponse;

/**
 * Provides cancel workflow functionality through the z/OSMF workflow REST API.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-cancel-workflow">z/OSMF REST API</a>
 *
 * @author Jorge Samaniego
 * @version 7.0
 */
public class WorkflowCancel {

    private static final String CANCEL_CONTEXT = "cancel";

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowCancel constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Jorge Samaniego
     */
    public WorkflowCancel(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowCancel constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link PutJsonZosmfRequest} implementation object
     * @author Jorge Samaniego
     */
    WorkflowCancel(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Cancel a z/OSMF workflow on a z/OS system.
     * <p>
     * Canceling a workflow does not undo any actions already performed.
     * A canceled workflow cannot be resumed.
     *
     * @param workflowKey workflow key identifying the workflow to cancel
     * @return WorkflowCancelResponse object containing the canceled workflow name
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     */
    public WorkflowCancelResponse cancel(final String workflowKey) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(workflowKey, "workflowKey");

        final String url = connection.getZosmfUrl() +
                WorkflowConstants.WORKFLOWS_RESOURCE +
                UrlConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(workflowKey) +
                WorkflowConstants.OPERATIONS_CANCEL;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no workflow cancel response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, WorkflowCancelResponse.class, CANCEL_CONTEXT);
    }

}
