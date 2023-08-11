/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.methods;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseResponse;
import zowe.client.sdk.parse.JsonParseResponseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.JsonGetRequest;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfinfo.ZosmfConstants;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

/**
 * This class holds the helper functions that are used to gather z/OSMF information through the z/OSMF APIs.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfStatus {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * CheckStatus Constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public ZosmfStatus(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative CheckStatus constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ZosmfStatus(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonGetRequest)) {
            throw new Exception("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get z/OSMF information
     *
     * @return ZosmfInfoResponse object
     * @throws Exception problem with response
     * @author Frank Giordano
     */
    public ZosmfInfoResponse get() throws Exception {
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosmfConstants.RESOURCE + ZosmfConstants.INFO;

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String jsonStr = RestUtils.getResponse(request).getResponsePhrase()
                .orElseThrow(() -> new Exception("no z/osmf status response phase")).toString();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
        final JsonParseResponse parser = JsonParseResponseFactory.buildParser(jsonObject, ParseType.ZOSMF_INFO);
        return (ZosmfInfoResponse) parser.parseResponse();
    }

}
