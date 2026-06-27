/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.UrlConstants;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosvariables.VariableConstants;

import java.util.List;

/**
 * Class to handle deleting of z/OS system variables.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-delete-system-variables">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class VariableDelete {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * VariableDelete constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public VariableDelete(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
    }

    /**
     * Alternative VariableDelete constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZosmfRequest Interface object
     */
    VariableDelete(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Delete the entire system variable pool for the target system.
     * <p>
     * A delete request with no request body removes the system variable pool from the system.
     *
     * @param sysplexName name of the sysplex (e.g. 'PLEX1')
     * @param systemName  name of the system (e.g. 'SYS1')
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response deleteAll(final String sysplexName, final String systemName) throws ZosmfRequestException {
        return deleteCommon(sysplexName, systemName, null, true);
    }

    /**
     * Delete the specified system variables from the target system's variable pool.
     * <p>
     * The request body is an array of strings, each representing the name of a system variable to delete.
     * If {@code variableNames} is empty, the request succeeds but no variables are deleted, as defined by the
     * z/OSMF REST API.
     *
     * @param sysplexName   name of the sysplex (e.g. 'PLEX1')
     * @param systemName    name of the system (e.g. 'SYS1')
     * @param variableNames names of the system variables to delete
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response delete(final String sysplexName, final String systemName, final List<String> variableNames)
            throws ZosmfRequestException {
        return deleteCommon(sysplexName, systemName, variableNames, false);
    }

    /**
     * Common method to handle deleting of system variables.
     *
     * @param sysplexName   name of the sysplex (e.g. 'PLEX1')
     * @param systemName    name of the system (e.g. 'SYS1')
     * @param variableNames names of the system variables to delete, ignored when deleteAll is true
     * @param deleteAll     true to delete the entire system variable pool with no request body
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    private Response deleteCommon(final String sysplexName,
                                  final String systemName,
                                  final List<String> variableNames,
                                  final boolean deleteAll)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
        ValidateUtils.checkIllegalParameter(systemName, "systemName");

        final String url = connection.getZosmfUrl() +
                VariableConstants.RESOURCE +
                UrlConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(sysplexName) + "." +
                EncodeUtils.encodeURIComponent(systemName);

        if (deleteAll) {
            // whole-pool delete: clear any prior body so the reused request issues a true bodyless request
            request.setBody(null);
        } else {
            ValidateUtils.checkNullParameter(variableNames, "variableNames");
            try {
                request.setBody(OBJECT_MAPPER.writeValueAsString(variableNames));
            } catch (JsonProcessingException e) {
                throw new IllegalStateException("error serializing variable names", e);
            }
        }

        request.setUrl(url);
        return request.executeRequest();
    }

}
