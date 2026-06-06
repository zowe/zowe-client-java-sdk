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
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosvariables.ZosmfVariablesConstants;
import zowe.client.sdk.zosvariables.input.SystemVariablesInputData;
import zowe.client.sdk.zosvariables.response.SystemVariablesResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This class retrieves z/OSMF system variables and system symbols through the z/OSMF APIs.
 */
public class ZosmfSystemVariables {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * ZosmfSystemVariables constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public ZosmfSystemVariables(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative ZosmfSystemVariables constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZosmfRequest Interface object
     */
    ZosmfSystemVariables(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get z/OSMF variables from the local system.
     *
     * @return SystemVariablesResponse object
     * @throws ZosmfRequestException request error state
     */
    public SystemVariablesResponse getLocal() throws ZosmfRequestException {
        return get(new SystemVariablesInputData());
    }

    /**
     * Get z/OSMF variables or system symbols.
     *
     * @param inputData system variables input data
     * @return SystemVariablesResponse object
     * @throws ZosmfRequestException request error state
     */
    public SystemVariablesResponse get(final SystemVariablesInputData inputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(inputData, "inputData");

        final String url = buildUrl(inputData);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no z/osmf system variables response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, SystemVariablesResponse.class, "get");
    }

    private String buildUrl(final SystemVariablesInputData inputData) {
        final StringBuilder url = new StringBuilder(connection.getZosmfUrl())
                .append(ZosmfVariablesConstants.SYSTEM_VARIABLES);

        if (inputData.isLocal()) {
            url.append(ZosmfVariablesConstants.LOCAL);
        } else {
            url.append("/")
                    .append(encode(inputData.getSysplexName()))
                    .append(".")
                    .append(encode(inputData.getSystemName()));
        }

        final List<String> queryParameters = buildQueryParameters(inputData);
        if (!queryParameters.isEmpty()) {
            url.append("?").append(String.join("&", queryParameters));
        }

        return url.toString();
    }

    private List<String> buildQueryParameters(final SystemVariablesInputData inputData) {
        final List<String> queryParameters = new ArrayList<>();

        if (inputData.getSource() != null) {
            queryParameters.add(ZosmfVariablesConstants.SOURCE_QUERY_PARAMETER + "=" +
                    encode(inputData.getSource().getValue()));
        }

        for (String variableName : inputData.getVariableNames()) {
            queryParameters.add(ZosmfVariablesConstants.VAR_NAME_QUERY_PARAMETER + "=" + encode(variableName));
        }

        return queryParameters;
    }

    private String encode(final String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding is not supported", e);
        }
    }

}