/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.unirest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.rest.unirest.JsonGetRequest;
import zowe.client.sdk.rest.unirest.Response;
import zowe.client.sdk.rest.unirest.ZoweRequest;
import zowe.client.sdk.rest.unirest.ZoweRequestFactory;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.ZosmfUtils;
import zowe.client.sdk.utility.unirest.UniRestUtils;
import zowe.client.sdk.zosmfinfo.ZosmfConstants;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

/**
 * This class holds the helper functions that are used to gather zosmf information through the z/OSMF APIs.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class CheckStatus {

    private static final Logger LOG = LoggerFactory.getLogger(CheckStatus.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * CheckStatus Constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public CheckStatus(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative CheckStatus constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public CheckStatus(ZOSConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
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
     */
    public ZosmfInfoResponse getZosmfInfo() throws Exception {
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosmfConstants.RESOURCE + ZosmfConstants.INFO;

        LOG.debug(url);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }
        request.setUrl(url);

        Response response;
        try {
            response = UniRestUtils.getResponse(request);
        } catch (Exception e) {
            throw e;
        }

        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return ZosmfUtils.parseZosmfInfo(
                ((JSONObject) new JSONParser().parse(response.getResponsePhrase().get().toString())));
    }

}
