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
import utility.Util;
import zostso.input.StartTsoParams;
import zostso.zosmf.ZosmfTsoResponse;

public class StartTso {

    public static StartStopResponses start(ZOSConnection connection, String accountNumber, StartTsoParams parms) {
        Util.checkNullParameter(accountNumber == null, "accountNumber is null");
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(accountNumber.isEmpty(), "accountNumber not specified");

        StartTsoParams customParms;
        if (parms == null) {
            customParms = setDefaultAddressSpaceParams(null, Util.encodeURIComponent(accountNumber));
        } else {
            customParms = setDefaultAddressSpaceParams(parms, Util.encodeURIComponent(accountNumber));
        }

        ZosmfTsoResponse zosmfResponse = startCommon(connection, customParms);

        // TODO
        return null;
    }

    public static ZosmfTsoResponse startCommon(ZOSConnection connection, StartTsoParams commandParms) {
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParms == null, "commandParms is null");

        String url = getResourcesQuery(commandParms);

        // TODO
        return null;
    }

    private static StartTsoParams setDefaultAddressSpaceParams(StartTsoParams parms, String accountNumber) {
        String proc = (parms.logonProcedure == null) ? TsoConstants.DEFAULT_PROC : parms.getLogonProcedure().get();
        String chset = (parms.characterSet == null) ? TsoConstants.DEFAULT_CHSET : parms.getCharacterSet().get();
        String cpage = (parms.codePage == null) ? TsoConstants.DEFAULT_CPAGE : parms.getCodePage().get();
        String rowNum = (parms.rows == null) ? TsoConstants.DEFAULT_ROWS : parms.getRows().get();
        String cols = (parms.columns == null) ? TsoConstants.DEFAULT_COLS : parms.getColumns().get();
        String rSize = (parms.regionSize== null) ? TsoConstants.DEFAULT_RSIZE : parms.getRegionSize().get();

        return new StartTsoParams(proc, chset, cpage, rowNum, cols, accountNumber, rSize);
    }

    private static String getResourcesQuery(StartTsoParams parms) {
        String query = TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "?";
        query += TsoConstants.PARM_ACCT + "=" + parms.account + "&";
        query += TsoConstants.PARM_PROC + "=" + parms.logonProcedure+  "&";
        query += TsoConstants.PARM_CHSET + "=" + parms.characterSet + "&";
        query += TsoConstants.PARM_CPAGE + "=" + parms.codePage + "&";
        query += TsoConstants.PARM_ROWS + "=" + parms.rows + "&";
        query += TsoConstants.PARM_COLS + "=" + parms.columns + "&";
        query += TsoConstants.PARM_RSIZE + "=" + parms.regionSize;

        return query;
    }

}
