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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowsConstants;
import zowe.client.sdk.zosmfworkflow.input.WorkflowListArchivedInputData;
import zowe.client.sdk.zosmfworkflow.response.WorkflowArchivedResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles listing archived workflows on z/OS.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-list-archived-workflows-system">z/OSMF REST API</a>
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class WorkflowListArchived {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowListArchived constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public WorkflowListArchived(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowListArchived constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     */
    WorkflowListArchived(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get all archived workflows on z/OS.
     *
     * @param inputData workflow list archived input parameters
     * @return list of WorkflowArchivedResponse objects
     * @throws ZosmfRequestException request error state
     */
    public List<WorkflowArchivedResponse> get(final WorkflowListArchivedInputData inputData)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(inputData, "inputData");

        final StringBuilder urlBuilder = new StringBuilder(
                connection.getZosmfUrl() + WorkflowsConstants.RESOURCE + "/archivedworkflows");

        boolean hasParam = false;
        if (inputData.getOrderBy() != null) {
            urlBuilder.append("?orderBy=").append(inputData.getOrderBy());
            hasParam = true;
        }
        if (inputData.getView() != null) {
            urlBuilder.append(hasParam ? "&" : "?").append("view=").append(inputData.getView());
        }
        final String url = urlBuilder.toString();

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no list archived workflows response phrase"))
                .toString();

        final List<WorkflowArchivedResponse> workflows = new ArrayList<>();
        try {
            final JSONObject jsonObject = (JSONObject) new JSONParser().parse(responsePhrase);
            final JSONArray results = (JSONArray) jsonObject.get("archivedWorkflows");
            if (results != null) {
                for (final Object obj : results) {
                    workflows.add(JsonUtils.parseResponse(
                            String.valueOf(obj), WorkflowArchivedResponse.class, "get"));
                }
            }
        } catch (ParseException e) {
            throw new ZosmfRequestException(e.getMessage());
        }

        return workflows;
    }

}