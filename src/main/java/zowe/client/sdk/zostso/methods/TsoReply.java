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
 * This class handles sending a request to z/OSMF TSO for additional TSO message data
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoReply {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * TsoReply constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public TsoReply(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative TsoReply constructor with ZoweRequest object. This is mainly used for internal code unit
     * testing with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    TsoReply(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Send a request to z/OSMF TSO for additional TSO message data from a valid already started TSO session.
     *
     * @param sessionId servletKey id retrieved from start TSO request
     * @return response string representing the returned request payload
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String reply(final String sessionId) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sessionId, "sessionId");
        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" +
                TsoConstants.RES_START_TSO + "/" + sessionId;

        if (request == null || !(request instanceof PutJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        return ResponseUtil.getResponseStr(request);
    }

}
