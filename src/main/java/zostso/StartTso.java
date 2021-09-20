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

/**
 * Start TSO address space and receive servlet key
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class StartTso {

    private static final Logger LOG = LogManager.getLogger(StartTso.class);

    private final ZOSConnection connection;

    /**
     * StartTso constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public StartTso(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Start TSO address space with provided parameters.
     *
     * @param accountNumber this key of StartTsoParams required, because it cannot be default.
     * @param params        optional object with required parameters, see StartTsoParams
     * @return command response on resolve, @see IStartStopResponses
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    public StartStopResponses start(String accountNumber, StartTsoParams params) throws Exception {
        Util.checkNullParameter(accountNumber == null, "accountNumber is null");
        Util.checkIllegalParameter(accountNumber.isEmpty(), "accountNumber not specified");

        StartTsoParams customParams;
        if (params == null) {
            customParams = setDefaultAddressSpaceParams(null, Util.encodeURIComponent(accountNumber));
        } else {
            customParams = setDefaultAddressSpaceParams(params, Util.encodeURIComponent(accountNumber));
        }

        ZosmfTsoResponse zosmfResponse = startCommon(customParams);

        // TODO
        return new StartStopResponses(zosmfResponse);
    }

    /**
     * Start TSO address space with provided parameters
     *
     * @param commandParams object with required parameters, see StartTsoParams
     * @return z/OSMF response object, see ZosmfTsoResponse
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    public ZosmfTsoResponse startCommon(StartTsoParams commandParams) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(commandParams == null, "commandParams is null");

        String url = getResourcesQuery(commandParams);
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

    /**
     * Set default TSO address space parameters
     *
     * @param params        object with required parameters, see StartTsoParams
     * @param accountNumber user's account number for permission
     * @return response object, see StartTsoParams
     * @author Frank Giordano
     */
    private StartTsoParams setDefaultAddressSpaceParams(StartTsoParams params, String accountNumber) {
        if (params == null) params = new StartTsoParams();
        String proc = params.getLogonProcedure().orElse(TsoConstants.DEFAULT_PROC);
        String chset = params.getCharacterSet().orElse(TsoConstants.DEFAULT_CHSET);
        String cpage = params.getCodePage().orElse(TsoConstants.DEFAULT_CPAGE);
        String rowNum = params.getRows().orElse(TsoConstants.DEFAULT_ROWS);
        String cols = params.getColumns().orElse(TsoConstants.DEFAULT_COLS);
        String rSize = params.getRegionSize().orElse(TsoConstants.DEFAULT_RSIZE);
        return new StartTsoParams(proc, chset, cpage, rowNum, cols, accountNumber, rSize);
    }

    /**
     * Set default TSO address space parameters
     *
     * @param params object with required parameters, see StartTsoParams
     * @return generated url
     * @author Frank Giordano
     */
    private String getResourcesQuery(StartTsoParams params) throws Exception {
        String query = "https://" + connection.getHost() + ":" + connection.getZosmfPort();
        query += TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "?";
        query += TsoConstants.PARAM_ACCT + "=" +
                params.account.orElseThrow(() -> new Exception("account num not specified")) + "&";
        query += TsoConstants.PARAM_PROC + "=" + params.logonProcedure.orElse(TsoConstants.DEFAULT_PROC) + "&";
        query += TsoConstants.PARAM_CHSET + "=" + params.characterSet.orElse(TsoConstants.DEFAULT_CHSET) + "&";
        query += TsoConstants.PARAM_CPAGE + "=" + params.codePage.orElse(TsoConstants.DEFAULT_CPAGE) + "&";
        query += TsoConstants.PARAM_ROWS + "=" + params.rows.orElse(TsoConstants.DEFAULT_ROWS) + "&";
        query += TsoConstants.PARAM_COLS + "=" + params.columns.orElse(TsoConstants.DEFAULT_COLS) + "&";
        query += TsoConstants.PARAM_RSIZE + "=" + params.regionSize.orElse(TsoConstants.DEFAULT_RSIZE);
        return query;
    }

}
