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
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

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
     * Alternative CheckStatus constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public CheckStatus(ZOSConnection connection, ZoweRequest request) throws Exception {
        Util.checkConnection(connection);
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
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosmfConstants.RESOURCE + ZosmfConstants.INFO;

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
        return parseJSONResponse((JSONObject) response.getResponsePhrase().get());
    }

    private ZosmfInfoResponse parseJSONResponse(JSONObject json) {
        ZosmfInfoResponse.Builder zosmfInfoResponse = new ZosmfInfoResponse.Builder()
                .zosVersion((String) json.get("zos_version"))
                .zosmfPort((String) json.get("zosmf_port"))
                .zosmfVersion((String) json.get("zosmf_version"))
                .zosmfHostName((String) json.get("zosmf_hostname"))
                .zosmfSafRealm((String) json.get("zosmf_saf_realm"))
                .apiVersion((String) json.get("api_version"))
                .zosmfFullVersion((String) json.get("zosmf_full_version"));

        JSONArray plugins = (JSONArray) json.get("plugins");
        if (plugins != null) {
            int size = plugins.size();
            ZosmfPluginInfo[] zosmfPluginsInfo = new ZosmfPluginInfo[size];
            for (int i = 0; i < size; i++) {
                JSONObject obj = (JSONObject) plugins.get(i);
                zosmfPluginsInfo[i] = new ZosmfPluginInfo.Builder()
                        .pluginVersion((String) obj.get("pluginVersion"))
                        .pluginDefaultName((String) obj.get("pluginDefaultName"))
                        .pluginStatus((String) obj.get("pluginStatus")).build();
            }
            return zosmfInfoResponse.zosmfPluginsInfo(zosmfPluginsInfo).build();
        }

        return zosmfInfoResponse.build();
    }

}
