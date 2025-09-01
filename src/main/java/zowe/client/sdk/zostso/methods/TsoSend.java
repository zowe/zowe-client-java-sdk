/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.methods;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ResponseUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;

/**
 * This class handles sending the TSO command to be performed via z/OSMF
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoSend {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * TsoSend constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public TsoSend(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative TsoSend constructor with ZoweRequest object. This is mainly used for internal code unit
     * testing with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    TsoSend(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Make the second request to send TSO the command to perform via z/OSMF
     *
     * @param sessionId servletKey id retrieved from start TSO request
     * @param command   tso command
     * @return response string representing the returned request payload
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String sendCommand(final String sessionId, final String command) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sessionId, "sessionId");
        ValidateUtils.checkIllegalParameter(command, "command");
        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + sessionId + TsoConstants.RES_DONT_READ_REPLY;
        final String body = "{\"TSO RESPONSE\":{\"VERSION\":\"0100\",\"DATA\":\"" + command + "\"}}";

        if (request == null || !(request instanceof PutJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        return ResponseUtil.getResponseStr(request);
    }

}
