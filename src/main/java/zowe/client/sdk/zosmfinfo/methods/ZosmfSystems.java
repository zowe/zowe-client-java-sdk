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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfinfo.ZosmfConstants;
import zowe.client.sdk.zosmfinfo.response.ZosmfSystemsResponse;

/**
 * This class is used to list the systems defined to z/OSMF through the z/OSMF APIs.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosmfSystems {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * ListDefinedSystems Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosmfSystems(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative ListDefinedSystems constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public ZosmfSystems(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * List systems defined to z/OSMF
     *
     * @return ZosmfListDefinedSystemsResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosmfSystemsResponse get() throws ZosmfRequestException {
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosmfConstants.RESOURCE + ZosmfConstants.TOPOLOGY + ZosmfConstants.SYSTEMS;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no z/osmf info response phrase")).toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        return (ZosmfSystemsResponse) JsonParseFactory.buildParser(ParseType.ZOSMF_SYSTEMS).parseResponse(jsonObject);
    }

}
