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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StartTsoParams;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.CollectedResponses;
import zowe.client.sdk.zostso.response.StartStopResponses;
import zowe.client.sdk.zostso.service.TsoResponseService;

/**
 * Start TSO address space and receive servlet key
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class StartTso {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * StartTso constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public StartTso(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative StartTso constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public StartTso(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
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
    private String getResourcesQuery(final StartTsoParams params) {
        String query = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "");
        query += TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "?";
        query += TsoConstants.PARAM_ACCT + "=" + params.account
                .orElseThrow(() -> new IllegalStateException("account num not specified")) + "&";
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
     * @return StartTsoParams object
     * @author Frank Giordano
     */
    private StartTsoParams setDefaultAddressSpaceParams(StartTsoParams params, final String accountNumber) {
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
     * @param accountNumber this key of StartTsoParams required because it cannot be default.
     * @param params        optional object with required parameters, see StartTsoParams
     * @return command response on resolve, @see IStartStopResponses
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public StartStopResponses start(final String accountNumber, final StartTsoParams params)
            throws ZosmfRequestException {
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
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosmfTsoResponse startCommon(final StartTsoParams commandParams) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(commandParams == null, "commandParams is null");

        final String url = getResourcesQuery(commandParams);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        return new TsoResponseService(request.executeRequest()).getZosmfTsoResponse();
    }

}
