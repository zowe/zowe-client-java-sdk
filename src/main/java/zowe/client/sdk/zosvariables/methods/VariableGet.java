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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.UrlConstants;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosvariables.VariableConstants;
import zowe.client.sdk.zosvariables.input.factory.VariableGetInputData;
import zowe.client.sdk.zosvariables.response.VariableGetResponse;
import java.util.List;

/**
 * Class to handle retrieval of z/OS system variables.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-system-variables">
 * z/OSMF REST API
 * </a>
 *
 * @author Adithe Das
 * @version 7.0
 */
public class VariableGet {

    /**
     * JSON parsing context.
     */
    private static final String GET_CONTEXT = "getVariables";

    /**
     * z/OSMF connection object.
     */
    private final ZosConnection connection;

    /**
     * z/OSMF request object.
     */
    private final ZosmfRequest request;

    /**
     * VariableGet Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public VariableGet(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
    }

    /**
     * Alternative VariableGet constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request any compatible ZosmfRequest Interface object
     */
    VariableGet(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");

        this.connection = connection;

        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }

        this.request = request;
    }

    /**
     * Retrieve z/OS system variables.
     * <p>
     * This method retrieves system variables from either a specified
     * z/OS system or the local system based on the supplied input data.
     * Optional variable name and variable type filters may also be specified.
     *
     * @param inputData input parameters for retrieving system variables
     * @return VariableGetResponse object
     * @throws ZosmfRequestException request error state
     */
    public VariableGetResponse get(final VariableGetInputData inputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(inputData, "inputData");
        final StringBuilder url = new StringBuilder();

        if (inputData.isLocal()) {
            url.append(connection.getZosmfUrl()).append(VariableConstants.RESOURCE).append(UrlConstants.URL_PATH_DELIM).append("local");

        } else {

            final String sysplexName = inputData.getSysplexName().orElse(null);
            final String systemName = inputData.getSystemName().orElse(null);

            ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
            ValidateUtils.checkIllegalParameter(systemName, "systemName");

            url.append(connection.getZosmfUrl())
                    .append(VariableConstants.RESOURCE)
                    .append(UrlConstants.URL_PATH_DELIM)
                    .append(EncodeUtils.encodeURIComponent(sysplexName))
                    .append(".").append(EncodeUtils.encodeURIComponent(systemName));
        }

        boolean first = true;
        final List<String> variableNames = inputData.getVariableNames().orElse(null);
        if (variableNames != null && !variableNames.isEmpty()) {

            for (final String variableName : variableNames) {
                ValidateUtils.checkIllegalParameter(variableName, "variableName");
                url.append(first ? "?" : "&");
                first = false;
                url.append("var-name=").append(EncodeUtils.encodeURIComponent(variableName));
            }
        }

        final String variableType = inputData.getVariableType().orElse(null);

        if (variableType != null && !variableType.trim().isEmpty()) {
            url.append(first ? "?" : "&");
            url.append("variable-type=").append(EncodeUtils.encodeURIComponent(variableType));
        }

        request.setUrl(url.toString());
        final String response = request.executeRequest().getResponsePhrase().orElseThrow(() -> new IllegalStateException("no get variables response phrase")).toString();

        return JsonUtils.parseResponse(response, VariableGetResponse.class, GET_CONTEXT);
    }

}
