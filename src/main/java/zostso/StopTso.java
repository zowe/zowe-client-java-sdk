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
import zostso.input.StopTsoParms;
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
     * @param connection connection object, see ZOSConnection object
     * @author Frank Giordano
     */
    public StopTso(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Sends REST call to z/OSMF for stoping active TSO address space
     *
     * @param commandParms command parameters, see StopTsoParms object
     * @return z/OSMF response object, see ZosmfTsoResponse object
     * @throws Exception error on TSO sto command
     * @author Frank Giordano
     */
    public ZosmfTsoResponse stopCommon(StopTsoParms commandParms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParms == null, "commandParms is null");
        Util.checkStateParameter(!commandParms.getServletKey().isPresent(), "servletKey not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + commandParms.getServletKey().get();
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

        StopTsoParms commandParms = new StopTsoParms(servletKey);
        ZosmfTsoResponse zosmfResponse = stopCommon(commandParms);

        // TODO
        return UtilTso.populateStartAndStop(zosmfResponse);
    }

}
