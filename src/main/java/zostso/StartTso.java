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
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import rest.IZoweRequest;
import rest.JsonRequest;
import utility.Util;
import zostso.input.StartTsoParams;
import zostso.zosmf.ZosmfTsoResponse;

public class StartTso {

    private static final Logger LOG = LogManager.getLogger(StartTso.class);

    public static StartStopResponses start(ZOSConnection connection, String accountNumber, StartTsoParams parms) throws Exception {
        Util.checkNullParameter(accountNumber == null, "accountNumber is null");
        Util.checkStateParameter(accountNumber.isEmpty(), "accountNumber not specified");

        StartTsoParams customParms;
        if (parms == null) {
            customParms = setDefaultAddressSpaceParams(null, Util.encodeURIComponent(accountNumber));
        } else {
            customParms = setDefaultAddressSpaceParams(parms, Util.encodeURIComponent(accountNumber));
        }

        ZosmfTsoResponse zosmfResponse = startCommon(connection, customParms);

        // TODO
        return new StartStopResponses(zosmfResponse);
    }

    public static ZosmfTsoResponse startCommon(ZOSConnection connection, StartTsoParams commandParms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParms == null, "commandParms is null");

        String url = getResourcesQuery(connection, commandParms);
        LOG.info("url {}", url);
        
        IZoweRequest request = new JsonRequest(connection, new HttpPost(url));
        JSONObject result = request.httpPost();

        if (result == null) {
            throw new Exception("No results for tso command.");
        }
        LOG.info(result);

        ZosmfTsoResponse response = new ZosmfTsoResponse();
        // TODO
//        response.setCmdResponseKey((String) result.get("cmd-response-key"));

        return response;
    }

    private static StartTsoParams setDefaultAddressSpaceParams(StartTsoParams parms, String accountNumber) {
        String proc = (parms == null || !parms.logonProcedure.isPresent()) ? TsoConstants.DEFAULT_PROC : parms.getLogonProcedure().get();
        String chset = (parms == null || !parms.characterSet.isPresent()) ? TsoConstants.DEFAULT_CHSET : parms.getCharacterSet().get();
        String cpage = (parms == null || !parms.codePage.isPresent()) ? TsoConstants.DEFAULT_CPAGE : parms.getCodePage().get();
        String rowNum = (parms == null || !parms.rows.isPresent()) ? TsoConstants.DEFAULT_ROWS : parms.getRows().get();
        String cols = (parms == null || !parms.columns.isPresent()) ? TsoConstants.DEFAULT_COLS : parms.getColumns().get();
        String rSize = (parms == null || !parms.regionSize.isPresent()) ? TsoConstants.DEFAULT_RSIZE : parms.getRegionSize().get();

        return new StartTsoParams(proc, chset, cpage, rowNum, cols, accountNumber, rSize);
    }

    private static String getResourcesQuery(ZOSConnection connection, StartTsoParams parms) {
        String query = "https://" + connection.getHost() + ":" + connection.getPort();
        query += TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "?";
        query += TsoConstants.PARM_ACCT + "=" + parms.account.get() + "&";
        query += TsoConstants.PARM_PROC + "=" + parms.logonProcedure.get() +  "&";
        query += TsoConstants.PARM_CHSET + "=" + parms.characterSet.get() + "&";
        query += TsoConstants.PARM_CPAGE + "=" + parms.codePage.get() + "&";
        query += TsoConstants.PARM_ROWS + "=" + parms.rows.get() + "&";
        query += TsoConstants.PARM_COLS + "=" + parms.columns.get() + "&";
        query += TsoConstants.PARM_RSIZE + "=" + parms.regionSize.get();

        return query;
    }

}
