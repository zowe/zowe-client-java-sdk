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
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.Util;
import utility.UtilRest;
import utility.UtilTso;
import zostso.input.StartTsoParams;
import zostso.zosmf.ZosmfTsoResponse;

public class StartTso {

    private static final Logger LOG = LogManager.getLogger(StartTso.class);

    private final ZOSConnection connection;

    public StartTso(ZOSConnection connection) {
        this.connection = connection;
    }

    public StartStopResponses start(String accountNumber, StartTsoParams parms)
            throws Exception {
        Util.checkNullParameter(accountNumber == null, "accountNumber is null");
        Util.checkStateParameter(accountNumber.isEmpty(), "accountNumber not specified");

        StartTsoParams customParms;
        if (parms == null) {
            customParms = setDefaultAddressSpaceParams(null, Util.encodeURIComponent(accountNumber));
        } else {
            customParms = setDefaultAddressSpaceParams(parms, Util.encodeURIComponent(accountNumber));
        }

        ZosmfTsoResponse zosmfResponse = startCommon(customParms);

        // TODO
        return new StartStopResponses(zosmfResponse);
    }

    public ZosmfTsoResponse startCommon(StartTsoParams commandParms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParms == null, "commandParms is null");

        String url = getResourcesQuery(commandParms);
        LOG.debug("StartTso::startCommon - url {}", url);

        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.POST_JSON);
        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return new ZosmfTsoResponse.Builder().build();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            throw new Exception("No results from executing tso command while setting up TSO address space. " + errorMsg);
        }
        return UtilTso.getZosmfTsoResponse(response);
    }

    private StartTsoParams setDefaultAddressSpaceParams(StartTsoParams parms, String accountNumber) {
        String proc = (parms == null || parms.logonProcedure.isEmpty())
                ? TsoConstants.DEFAULT_PROC : parms.getLogonProcedure().get();
        String chset = (parms == null || parms.characterSet.isEmpty())
                ? TsoConstants.DEFAULT_CHSET : parms.getCharacterSet().get();
        String cpage = (parms == null || parms.codePage.isEmpty())
                ? TsoConstants.DEFAULT_CPAGE : parms.getCodePage().get();
        String rowNum = (parms == null || parms.rows.isEmpty())
                ? TsoConstants.DEFAULT_ROWS : parms.getRows().get();
        String cols = (parms == null || parms.columns.isEmpty())
                ? TsoConstants.DEFAULT_COLS : parms.getColumns().get();
        String rSize = (parms == null || parms.regionSize.isEmpty())
                ? TsoConstants.DEFAULT_RSIZE : parms.getRegionSize().get();

        return new StartTsoParams(proc, chset, cpage, rowNum, cols, accountNumber, rSize);
    }

    private String getResourcesQuery(StartTsoParams parms) {
        String query = "https://" + connection.getHost() + ":" + connection.getPort();
        query += TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "?";
        query += TsoConstants.PARM_ACCT + "=" + parms.account.get() + "&";
        query += TsoConstants.PARM_PROC + "=" + parms.logonProcedure.get() + "&";
        query += TsoConstants.PARM_CHSET + "=" + parms.characterSet.get() + "&";
        query += TsoConstants.PARM_CPAGE + "=" + parms.codePage.get() + "&";
        query += TsoConstants.PARM_ROWS + "=" + parms.rows.get() + "&";
        query += TsoConstants.PARM_COLS + "=" + parms.columns.get() + "&";
        query += TsoConstants.PARM_RSIZE + "=" + parms.regionSize.get();

        return query;
    }

}
