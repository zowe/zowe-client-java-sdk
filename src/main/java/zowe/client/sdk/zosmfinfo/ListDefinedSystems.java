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
import zowe.client.sdk.parsejson.ParseZosmfListDefinedSystemsJson;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.utility.UtilRest;
import zowe.client.sdk.zosmfinfo.response.ZosmfListDefinedSystemsResponse;

/**
 * This class is used to list the systems defined to z/OSMF through the z/OSMF APIs.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ListDefinedSystems {

    private static final Logger LOG = LoggerFactory.getLogger(ListDefinedSystems.class);

    private final ZOSConnection connection;
    private ZoweRequest request;
    private IParseJson<ZosmfListDefinedSystemsResponse> listDefinedSystemsParseJson = new ParseZosmfListDefinedSystemsJson();

    /**
     * ListDefinedSystems Constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public ListDefinedSystems(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative ListDefinedSystems constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public ListDefinedSystems(ZOSConnection connection, ZoweRequest request) throws Exception {
        Util.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonGetRequest)) {
            throw new Exception("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * List systems defined to z/OSMF
     *
     * @return ZosmfListDefinedSystemsResponse object
     * @throws Exception problem with response
     */
    public ZosmfListDefinedSystemsResponse listDefinedSystems() throws Exception {
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosmfConstants.RESOURCE + ZosmfConstants.TOPOLOGY + ZosmfConstants.SYSTEMS;

        LOG.debug(url);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.VerbType.GET_JSON);
        }
        request.setRequest(url);

        Response response = request.executeRequest();
        if (response.isEmpty()) {
            throw new Exception("response is empty");
        }

        UtilRest.checkHttpErrors(response);
        return listDefinedSystemsParseJson.parse((JSONObject) response.getResponsePhrase().get());
    }

}
