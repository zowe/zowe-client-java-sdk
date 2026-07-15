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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.UrlConstants;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.TsoUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.response.TsoCommonResponse;

/**
 * This class handles sending the request to end the TSO session via z/OSMF
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-end-tsoe-address-space">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class TsoStop {

    private final ZosConnection connection;
    private final ZosmfRequest request;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * TsoStop constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public TsoStop(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
    }

    /**
     * Alternative TsoStop constructor with ZoweRequest object. This is mainly used for internal code unit
     * testing with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link DeleteJsonZosmfRequest} implementation object
     * @author Frank Giordano
     */
    TsoStop(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Stop the TSO session by session id (servletKey)
     *
     * @param sessionId servletKey id retrieved from start TSO request
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("DuplicatedCode")
    public TsoCommonResponse stop(final String sessionId) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sessionId, "sessionId");
        final String url = connection.getZosmfUrl() +
                TsoConstants.RESOURCE +
                UrlConstants.URL_PATH_DELIM +
                TsoConstants.RES_START_TSO +
                UrlConstants.URL_PATH_DELIM +
                sessionId;

        request.setUrl(url);

        final String responseStr = TsoUtils.getResponseStr(request);

        TsoCommonResponse tsoCommonResponse;
        try {
            tsoCommonResponse = objectMapper.readValue(responseStr, TsoCommonResponse.class);
        } catch (JsonProcessingException e) {
            // check for msdData error message if it exists
            final String errMsg = TsoUtils.getMsgDataText(responseStr);
            throw new ZosmfRequestException(errMsg.isBlank() ? e.getMessage() : errMsg);
        }

        return tsoCommonResponse;
    }

}
