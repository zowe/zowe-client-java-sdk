/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.Util;
import utility.UtilRest;
import utility.UtilTso;
import zostso.input.StopTsoParams;
import zostso.zosmf.ZosmfTsoResponse;

/**
 * Stop active TSO address space using servlet key
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class StopTso {

    private static final Logger LOG = LogManager.getLogger(StopTso.class);

    private final ZOSConnection connection;

    /**
     * StopTso constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public StopTso(ZOSConnection connection) {
        this.connection = connection;
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
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParams == null, "commandParams is null");
        Util.checkStateParameter(commandParams.getServletKey().isEmpty(), "servletKey not specified");
        Util.checkStateParameter(commandParams.getServletKey().get().isEmpty(), "servletKey not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + commandParams.getServletKey().get();
        LOG.debug("StopTso::stopCommon url {}", url);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.DELETE_JSON);
        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return new ZosmfTsoResponse.Builder().build();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            throw new Exception("Failed to stop active TSO address space. " + errorMsg);
        }
        JSONObject result = (JSONObject) response.getResponsePhrase().get();
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
        Util.checkStateParameter(servletKey.isEmpty(), "servletKey not specified");

        StopTsoParams commandParams = new StopTsoParams(servletKey);
        ZosmfTsoResponse zosmfResponse = stopCommon(commandParams);

        // TODO
        return UtilTso.populateStartAndStop(zosmfResponse);
    }

}
