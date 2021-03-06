/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.utility.UtilRest;
import zowe.client.sdk.utility.UtilTso;
import zowe.client.sdk.zostso.input.StopTsoParams;
import zowe.client.sdk.zostso.zosmf.ZosmfTsoResponse;

/**
 * Stop active TSO address space using servlet key
 *
 * @author Frank Giordano
 * @version 1.0
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
        Util.checkConnection(connection);
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
        Util.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonDeleteRequest)) {
            throw new Exception("DELETE_JSON request type required");
        }
        this.request = request;
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
        Util.checkNullParameter(commandParams == null, "commandParams is null");
        Util.checkIllegalParameter(commandParams.getServletKey().isEmpty(), "servletKey not specified");
        Util.checkIllegalParameter(commandParams.getServletKey().get().isEmpty(), "servletKey not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + commandParams.getServletKey().get();
        LOG.debug("StopTso::stopCommon url {}", url);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.VerbType.DELETE_JSON);
        }
        request.setRequest(url);
        Response response = request.executeRequest();
        if (response.isEmpty()) {
            return new ZosmfTsoResponse.Builder().build();
        }

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            throw new Exception("Failed to stop active TSO address space. " + errorMsg);
        }
        JSONObject result = (JSONObject) response.getResponsePhrase().orElse(null);
        //noinspection ConstantConditions
        return UtilTso.parseJsonStopResponse(result);
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
        Util.checkNullParameter(servletKey == null, "servletKey is null");
        Util.checkIllegalParameter(servletKey.isEmpty(), "servletKey not specified");

        StopTsoParams commandParams = new StopTsoParams(servletKey);
        ZosmfTsoResponse zosmfResponse = stopCommon(commandParams);

        // TODO
        return UtilTso.populateStartAndStop(zosmfResponse);
    }

}
