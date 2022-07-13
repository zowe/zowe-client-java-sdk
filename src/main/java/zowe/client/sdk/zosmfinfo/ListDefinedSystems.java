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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.utility.UtilRest;
import zowe.client.sdk.zosmfinfo.response.DefinedSystem;
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
     * List systems defined to z/OSMF
     *
     * @return ZosmfListDefinedSystemsResponse object
     * @throws Exception problem with response
     */
    public ZosmfListDefinedSystemsResponse listDefinedSystems() throws Exception {
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosmfConstants.RESOURCE + ZosmfConstants.TOPOLOGY + ZosmfConstants.SYSTEMS;

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
        return parseJSONResponse((JSONObject) response.getResponsePhrase().get());
    }

    private ZosmfListDefinedSystemsResponse parseJSONResponse(JSONObject json) {
        ZosmfListDefinedSystemsResponse.Builder systemsResponse = new ZosmfListDefinedSystemsResponse.Builder()
                .numRows((Long) json.get("numRows"));

        JSONArray items = (JSONArray) json.get("items");
        if (items != null) {
            int size = items.size();
            DefinedSystem[] definedSystems = new DefinedSystem[size];
            for (int i = 0; i < size; i++) {
                JSONObject obj = (JSONObject) items.get(i);
                definedSystems[i] = new DefinedSystem.Builder()
                        .systemNickName((String) obj.get("systemNickName"))
                        .groupNames((String) obj.get("groupNames"))
                        .cpcSerial((String) obj.get("cpcSerial"))
                        .zosVR((String) obj.get("zosVR"))
                        .systemName((String) obj.get("systemName"))
                        .jesType((String) obj.get("jesType"))
                        .sysplexName((String) obj.get("sysplexName"))
                        .jesMemberName((String) obj.get("jesMemberName"))
                        .httpProxyName((String) obj.get("httpProxyName"))
                        .ftpDestinationName((String) obj.get("ftpDestinationName"))
                        .url((String) obj.get("url"))
                        .cpcName((String) obj.get("cpcName")).build();
            }
            return systemsResponse.definedSystems(definedSystems).build();
        }

        return systemsResponse.build();
    }

}
