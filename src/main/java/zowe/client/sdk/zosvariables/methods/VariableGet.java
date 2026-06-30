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
    private static final String GET_CONTEXT = "getVariables";
    private final ZosConnection connection;
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
     * This is mainly used for internal code unit testing with Mockito, and it is not recommended to be used by the larger community.
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
     * Retrieve variables for a specified system.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @return VariableGetResponse object
     * @throws ZosmfRequestException request error state
     */
    public VariableGetResponse get(final String sysplexName, final String systemName) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
        ValidateUtils.checkIllegalParameter(systemName, "systemName");
        return getCommon(sysplexName, systemName, false, null, null);
    }

    /**
     * Retrieve variables for a specified system with optional filters.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @param variableNames variable names to retrieve
     * @param variableType variable type filter
     * @return VariableGetResponse object
     * @throws ZosmfRequestException request error state
     */
    public VariableGetResponse get(final String sysplexName, final String systemName, final List<String> variableNames, final String variableType) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
        ValidateUtils.checkIllegalParameter(systemName, "systemName");
        return getCommon(sysplexName, systemName, false, variableNames, variableType);
    }

    /**
     * Retrieve variables for the local system.
     *
     * @return VariableGetResponse object
     * @throws ZosmfRequestException request error state
     */
    public VariableGetResponse getLocal() throws ZosmfRequestException {
        return getCommon(null, null, true, null, null);
    }

    /**
     * Retrieve variables for the local system with optional filters.
     *
     * @param variableNames variable names to retrieve
     * @param variableType variable type filter
     * @return VariableGetResponse object
     * @throws ZosmfRequestException request error state
     */
    public VariableGetResponse getLocal(final List<String> variableNames, final String variableType) throws ZosmfRequestException {
        return getCommon(null, null, true, variableNames, variableType);
    }

    /**
     * Common implementation for retrieving variables.
     *
     * @param sysplexName sysplex name
     * @param systemName system name
     * @param isLocal true to retrieve the local system
     * @param variableNames optional variable names
     * @param variableType optional variable type
     * @return VariableGetResponse object
     * @throws ZosmfRequestException request error state
     */
    private VariableGetResponse getCommon(final String sysplexName, final String systemName, final boolean isLocal, final List<String> variableNames, final String variableType) throws ZosmfRequestException {
        final StringBuilder url = new StringBuilder();

        if (isLocal) {
            url.append(connection.getZosmfUrl())
                    .append(VariableConstants.RESOURCE)
                    .append(UrlConstants.URL_PATH_DELIM).append("local");
        } else {
            url.append(connection.getZosmfUrl())
                    .append(VariableConstants.RESOURCE)
                    .append(UrlConstants.URL_PATH_DELIM)
                    .append(EncodeUtils.encodeURIComponent(sysplexName))
                    .append(".").append(EncodeUtils.encodeURIComponent(systemName));
        }

        boolean first = true;

        if (variableNames != null && !variableNames.isEmpty()) {
            for (final String variableName : variableNames) {
                ValidateUtils.checkIllegalParameter(variableName, "variableName");
                url.append(first ? "?" : "&");
                first = false;
                url.append("var-name=").append(EncodeUtils.encodeURIComponent(variableName));
            }
        }

        if (variableType != null && !variableType.trim().isEmpty()) {
            url.append(first ? "?" : "&");
            url.append("variable-type=").append(EncodeUtils.encodeURIComponent(variableType));
        }

        request.setUrl(url.toString());

        final String response = request.executeRequest().getResponsePhrase().orElseThrow(() -> new IllegalStateException("no get variables response phrase")).toString();
        return JsonUtils.parseResponse(response, VariableGetResponse.class, GET_CONTEXT);
    }

}
