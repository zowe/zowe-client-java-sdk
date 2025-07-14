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

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StopTsoParams;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.StartStopResponse;
import zowe.client.sdk.zostso.service.TsoResponseService;

/**
 * Stop active TSO address space using a servlet key
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class StopTso {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * StopTso constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public StopTso(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative StopTso constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public StopTso(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Stop TSO address space and populates response with StartStopResponse, @see StartStopResponse
     *
     * @param servletKey unique servlet entry identifier
     * @return start stop response, see StartStopResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public StartStopResponse stop(final String servletKey) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(servletKey == null, "servletKey is null");
        ValidateUtils.checkIllegalParameter(servletKey.isBlank(), "servletKey not specified");

        final StopTsoParams commandParams = new StopTsoParams(servletKey);
        final ZosmfTsoResponse zosmfResponse = stopCommon(commandParams);

        // TODO
        return new TsoResponseService(zosmfResponse).setStartStopResponse();
    }

    /**
     * Sends REST call to z/OSMF for stopping active TSO address space
     *
     * @param commandParams command parameters, see commandParams object
     * @return z/OSMF response object, see ZosmfTsoResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosmfTsoResponse stopCommon(final StopTsoParams commandParams) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(commandParams == null, "commandParams is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO + "/" +
                commandParams.getServletKey().orElseThrow(() -> new IllegalArgumentException("servletKey not specified"));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }
        request.setUrl(url);

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no tso stop response phrase")).toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        return (ZosmfTsoResponse) JsonParseFactory.buildParser(ParseType.TSO_STOP).parseResponse(jsonObject);
    }

}
