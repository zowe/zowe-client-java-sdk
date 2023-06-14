/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.lifecycle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.JsonDeleteRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.TsoUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.unirest.UniRestUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StopTsoParams;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.StartStopResponse;

/**
 * Stop active TSO address space using servlet key
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class StopTso {

    private static final Logger LOG = LoggerFactory.getLogger(StopTso.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * StopTso constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public StopTso(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative StopTso constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public StopTso(ZOSConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonDeleteRequest)) {
            throw new Exception("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Stop TSO address space and populates response with StartStopResponse, @see StartStopResponse
     *
     * @param servletKey unique servlet entry identifier
     * @return start stop response, see StartStopResponse object
     * @throws Exception error on TSO sto command
     * @author Frank Giordano
     */
    public StartStopResponse stop(String servletKey) throws Exception {
        ValidateUtils.checkNullParameter(servletKey == null, "servletKey is null");
        ValidateUtils.checkIllegalParameter(servletKey.isBlank(), "servletKey not specified");

        final StopTsoParams commandParams = new StopTsoParams(servletKey);
        final ZosmfTsoResponse zosmfResponse = stopCommon(commandParams);

        // TODO
        return TsoUtils.populateStartAndStop(zosmfResponse);
    }

    /**
     * Sends REST call to z/OSMF for stopping active TSO address space
     *
     * @param commandParams command parameters, see commandParams object
     * @return z/OSMF response object, see ZosmfTsoResponse object
     * @throws Exception error on TSO sto command
     * @author Frank Giordano
     */
    public ZosmfTsoResponse stopCommon(StopTsoParams commandParams) throws Exception {
        ValidateUtils.checkNullParameter(commandParams == null, "commandParams is null");
        ValidateUtils.checkIllegalParameter(commandParams.getServletKey().isEmpty(), "servletKey not specified");
        ValidateUtils.checkIllegalParameter(commandParams.getServletKey().get().isBlank(), "servletKey not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + commandParams.getServletKey().get();
        LOG.debug("StopTso::stopCommon url {}", url);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.DELETE_JSON);
        }
        request.setUrl(url);

        final Response response = UniRestUtils.getResponse(request);
        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return TsoUtils.parseJsonStopResponse(
                (JSONObject) new JSONParser().parse(response.getResponsePhrase().get().toString()));
    }

}
