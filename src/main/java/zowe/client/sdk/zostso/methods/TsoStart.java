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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.TsoUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StartTsoInputData;
import zowe.client.sdk.zostso.response.TsoStartResponse;

/**
 * This class handles sending the request to start the TSO session via z/OSMF
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoStart {

    private final ZosConnection connection;
    private ZosmfRequest request;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * TsoStart constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public TsoStart(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative TsoStart constructor with ZoweRequest object. This is mainly used for internal code unit
     * testing with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    TsoStart(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Make the first TSO request to start the TSO session and retrieve its session id (servletKey).
     *
     * @param inputData parameters for start tso call, see StartTsoInputData
     * @return string value representing the session id (servletKey)
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public TsoStartResponse start(final StartTsoInputData inputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(inputData == null, "inputData is null");
        final String url = connection.getZosmfUrl() + TsoConstants.RESOURCE + "/" + TsoConstants.RES_START_TSO +
                "?" + "acct" + "=" + EncodeUtils.encodeURIComponent(inputData.getAccount()
                .orElseThrow(() -> new ZosmfRequestException("accountNumber is not specified"))) +
                "&" + "proc" + "=" + inputData.getLogonProcedure().orElse(TsoConstants.DEFAULT_PROC) +
                "&" + "chset" + "=" + inputData.getCharacterSet().orElse(TsoConstants.DEFAULT_CHSET) +
                "&" + "cpage" + "=" + inputData.getCodePage().orElse(TsoConstants.DEFAULT_CPAGE) +
                "&" + "rows" + "=" + inputData.getRows().orElse(TsoConstants.DEFAULT_ROWS) +
                "&" + "cols" + "=" + inputData.getColumns().orElse(TsoConstants.DEFAULT_COLS) +
                "&" + "rsize" + "=" + inputData.getRegionSize().orElse(TsoConstants.DEFAULT_RSIZE);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody("");

        final String responseStr = TsoUtils.getResponseStr(request);

        final JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(responseStr);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException(e.getMessage());
        }

        final JsonNode keyNode = rootNode.get("servletKey");
        if (keyNode == null || keyNode.isNull()) {
            final String errMsg = "Response missing servletKey: " + responseStr;
            throw new ZosmfRequestException(errMsg);
        }
        final String servletKey = keyNode.asText();
        if (servletKey == null || servletKey.trim().isEmpty() || "null".equalsIgnoreCase(servletKey)) {
            final String errMsg = "Response servletKey is empty: " + responseStr;
            throw new ZosmfRequestException(errMsg);
        }

        if (responseStr.contains("IKJ56455I")) { // successful logon
            return new TsoStartResponse(true, servletKey, responseStr);
        } else {
            return new TsoStartResponse(false, servletKey, responseStr);
        }
    }

}
