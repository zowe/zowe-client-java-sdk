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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.JsonPostRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.TsoUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StartTsoParams;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.CollectedResponses;
import zowe.client.sdk.zostso.response.StartStopResponses;

/**
 * Start TSO address space and receive servlet key
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class StartTso {

    private static final Logger LOG = LoggerFactory.getLogger(StartTso.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * StartTso constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public StartTso(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative StartTso constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public StartTso(ZOSConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonPostRequest)) {
            throw new Exception("POST_JSON request type required");
        }
        this.request = request;
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

    /**
     * Set default TSO address space parameters
     *
     * @param params        object with required parameters, see StartTsoParams
     * @param accountNumber user's account number for permission
     * @return response object, see StartTsoParams
     * @author Frank Giordano
     */
    private StartTsoParams setDefaultAddressSpaceParams(StartTsoParams params, String accountNumber) {
        if (params == null) {
            params = new StartTsoParams();
        }
        final String proc = params.getLogonProcedure().orElse(TsoConstants.DEFAULT_PROC);
        final String chset = params.getCharacterSet().orElse(TsoConstants.DEFAULT_CHSET);
        final String cpage = params.getCodePage().orElse(TsoConstants.DEFAULT_CPAGE);
        final String rowNum = params.getRows().orElse(TsoConstants.DEFAULT_ROWS);
        final String cols = params.getColumns().orElse(TsoConstants.DEFAULT_COLS);
        final String rSize = params.getRegionSize().orElse(TsoConstants.DEFAULT_RSIZE);
        return new StartTsoParams(proc, chset, cpage, rowNum, cols, accountNumber, rSize);
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
        ValidateUtils.checkNullParameter(accountNumber == null, "accountNumber is null");
        ValidateUtils.checkIllegalParameter(accountNumber.isBlank(), "accountNumber not specified");

        StartTsoParams customParams;
        if (params == null) {
            customParams = setDefaultAddressSpaceParams(null, EncodeUtils.encodeURIComponent(accountNumber));
        } else {
            customParams = setDefaultAddressSpaceParams(params, EncodeUtils.encodeURIComponent(accountNumber));
        }

        final ZosmfTsoResponse zosmfResponse = startCommon(customParams);

        CollectedResponses collectedResponses = null;
        if (zosmfResponse.getServletKey().isPresent()) {
            final SendTso sendTso = new SendTso(connection);
            collectedResponses = sendTso.getAllResponses(zosmfResponse);
        }

        return new StartStopResponses(zosmfResponse, collectedResponses);
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
        ValidateUtils.checkNullParameter(commandParams == null, "commandParams is null");

        final String url = getResourcesQuery(commandParams);
        LOG.debug("StartTso::startCommon - url {}", url);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        final Response response = RestUtils.getResponse(request);
        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return TsoUtils.getZosmfTsoResponse(response);
    }

}
