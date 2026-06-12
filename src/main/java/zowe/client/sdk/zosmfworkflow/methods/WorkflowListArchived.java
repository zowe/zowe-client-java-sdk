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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowsConstants;
import zowe.client.sdk.zosmfworkflow.response.WorkflowKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles listing archived workflows on z/OS.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-zosmf-workflow">z/OSMF REST API</a>
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
     * @author Muhammad Imran
     */
    public WorkflowListArchived(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowListArchived constructor with ZosmfRequest object. This is mainly used
     * for internal code unit testing with Mockito, and it is not recommended to be used by the
     * larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Muhammad Imran
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
     * List all archived workflows on z/OS.
     *
     * @return list of WorkflowKey objects
     * @throws ZosmfRequestException request error state
     * @author Muhammad Imran
     */
    public List<WorkflowKey> listArchived() throws ZosmfRequestException {
        return listArchivedCommon();
    }

    /**
     * List all archived workflows on z/OS.
     *
     * @return list of WorkflowKey objects
     * @throws ZosmfRequestException request error state
     * @author Muhammad Imran
     */
    private List<WorkflowKey> listArchivedCommon() throws ZosmfRequestException {
        final String url = connection.getZosmfUrl() + WorkflowsConstants.RESOURCE_ARCHIVED;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no list archived workflows response phrase"))
                .toString();

        final List<WorkflowKey> workflows = new ArrayList<>();
        final JSONArray results = JsonUtils.parseArray(responsePhrase);
        for (final Object obj : results) {
            workflows.add(JsonUtils.parseResponse(String.valueOf(obj), WorkflowKey.class, "listArchivedCommon"));
        }

        return workflows;
    }

}
