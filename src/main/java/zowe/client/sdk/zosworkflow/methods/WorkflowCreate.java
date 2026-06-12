/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflow.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosworkflow.WorkflowConstants;
import zowe.client.sdk.zosworkflow.input.WorkflowCreateInputData;
import zowe.client.sdk.zosworkflow.response.WorkflowCreateResponse;

/**
 * Provides create workflow functionality through the z/OSMF workflow REST API.
 * <p><a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-create-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowCreate {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowCreate constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public WorkflowCreate(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowCreate constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     */
    WorkflowCreate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Create a z/OSMF workflow on the target system.
     *
     * @param createInputData workflow creation parameters
     * @return workflow details returned by z/OSMF
     * @throws ZosmfRequestException request error state
     */
    public WorkflowCreateResponse create(final WorkflowCreateInputData createInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(createInputData, "createInputData");
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowName(), "workflowName");
        ValidateUtils.checkIllegalParameter(createInputData.getWorkflowDefinitionFile(), "workflowDefinitionFile");
        ValidateUtils.checkIllegalParameter(createInputData.getSystem(), "system");
        ValidateUtils.checkIllegalParameter(createInputData.getOwner(), "owner");

        final String url = connection.getZosmfUrl() + WorkflowConstants.RESOURCE + "/workflows";

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);

        try {
            request.setBody(OBJECT_MAPPER.writeValueAsString(createInputData));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("error serializing workflow create request", e);
        }

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no workflow create response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, WorkflowCreateResponse.class, "create");
    }

}
