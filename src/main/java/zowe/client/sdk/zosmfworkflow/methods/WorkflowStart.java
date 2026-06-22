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
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.UrlConstants;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;
import zowe.client.sdk.zosmfworkflow.input.WorkflowStartInputData;

/**
 * Provides start workflow functionality through the z/OSMF workflow REST API.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-start-workflow">z/OSMF REST API</a>
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class WorkflowStart {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowStart constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public WorkflowStart(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowStart constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     */
    WorkflowStart(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Start a z/OSMF workflow on the target system.
     *
     * @param workflowKey workflow key identifying the workflow to start
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response start(final String workflowKey) throws ZosmfRequestException {
        return startCommon(new WorkflowStartInputData.Builder(workflowKey).build());
    }

    /**
     * Start a z/OSMF workflow on the target system with input parameters.
     *
     * @param startInputData workflow start parameters
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response startCommon(final WorkflowStartInputData startInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(startInputData, "startInputData");

        final String url = connection.getZosmfUrl() + WorkflowConstants.WORKFLOWS_RESOURCE
                + UrlConstants.URL_PATH_DELIM + EncodeUtils.encodeURIComponent(startInputData.getWorkflowKey())
                + UrlConstants.URL_PATH_DELIM + "operations" + UrlConstants.URL_PATH_DELIM + "start";

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);

        try {
            request.setBody(OBJECT_MAPPER.writeValueAsString(startInputData));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("error serializing workflow start request", e);
        }

        return request.executeRequest();
    }

}
