/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.parsejson.IParseJson;
import zowe.client.sdk.parsejson.ParseZosmfInfoJson;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.utility.UtilRest;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

/**
 * This class holds the helper functions that are used to gather zosmf information through the z/OSMF APIs.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class CheckStatus {

    private static final Logger LOG = LoggerFactory.getLogger(CheckStatus.class);

    private final ZOSConnection connection;
    private ZoweRequest request;
    private IParseJson<ZosmfInfoResponse> zosmfInfoParseJson = new ParseZosmfInfoJson();

    /**
     * CheckStatus Constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public CheckStatus(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Get z/OSMF information
     *
     * @return ZosmfInfoResponse object
     * @throws Exception problem with response
     */
    public ZosmfInfoResponse getZosmfInfo() throws Exception {
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosmfConstants.RESOURCE + ZosmfConstants.INFO;

        LOG.debug(url);

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.VerbType.GET_JSON);
        } else {
            request.setRequest(url);
        }

        Response response = request.executeRequest();
        if (response.isEmpty()) {
            throw new Exception("response is empty");
        }

        UtilRest.checkHttpErrors(response);
        return zosmfInfoParseJson.parse((JSONObject) response.getResponsePhrase().get());
    }

}
