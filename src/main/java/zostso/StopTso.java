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
import utility.UtilTso;
import zostso.input.StopTsoParms;
import zostso.zosmf.ZosmfTsoResponse;

public class StopTso {

    private static final Logger LOG = LogManager.getLogger(StopTso.class);

    public static ZosmfTsoResponse stopCommon(ZOSConnection connection, StopTsoParms commandParms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParms == null, "commandParms is null");
        Util.checkStateParameter(!commandParms.getServletKey().isPresent(), "servletKey not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" + commandParms.getServletKey().get();
        LOG.debug("StopTso::stopCommon url {}", url);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.RequestType.DELETE_JSON);
        Response response = request.executeHttpRequest();
        JSONObject result = (JSONObject) response.getResponsePhrase().get();
        return UtilTso.parseJsonStopResponse(result);
    }

    /**
     * Stop TSO address space and populates response with StartStopResponse
     */
    public static StartStopResponse stop(ZOSConnection connection, String servletKey) throws Exception {
        Util.checkNullParameter(servletKey == null, "servletKey is null");
        Util.checkStateParameter(servletKey.isEmpty(), "servletKey not specified");

        StopTsoParms commandParms = new StopTsoParms(servletKey);
        ZosmfTsoResponse zosmfResponse = stopCommon(connection, commandParms);

        // TODO
        return TsoResponseService.populateStartAndStop(zosmfResponse);
    }

}
