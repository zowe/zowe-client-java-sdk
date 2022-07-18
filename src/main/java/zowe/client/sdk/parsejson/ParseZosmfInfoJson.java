/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parsejson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

/**
 * Class to transform JSON into ZosmfInfoResponse object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ParseZosmfInfoJson implements IParseJson<ZosmfInfoResponse> {

    /**
     * Transform JSON into ZosmfInfoResponse object
     *
     * @param jsonObject
     * @return ZosmfInfoResponse object
     * @author Frank Giordano
     */
    @Override
    public ZosmfInfoResponse parse(JSONObject jsonObject) {
        ZosmfInfoResponse.Builder zosmfInfoResponse = new ZosmfInfoResponse.Builder()
                .zosVersion((String) jsonObject.get("zos_version"))
                .zosmfPort((String) jsonObject.get("zosmf_port"))
                .zosmfVersion((String) jsonObject.get("zosmf_version"))
                .zosmfHostName((String) jsonObject.get("zosmf_hostname"))
                .zosmfSafRealm((String) jsonObject.get("zosmf_saf_realm"))
                .apiVersion((String) jsonObject.get("api_version"))
                .zosmfFullVersion((String) jsonObject.get("zosmf_full_version"));

        JSONArray plugins = (JSONArray) jsonObject.get("plugins");
        if (plugins != null) {
            int size = plugins.size();
            ZosmfPluginInfo[] zosmfPluginsInfo = new ZosmfPluginInfo[size];
            for (int i = 0; i < size; i++) {
                IParseJson<ZosmfPluginInfo> zosmfPluginInfoIJson = new ParseZosmfPluginInfoJson();
                zosmfPluginsInfo[i] = zosmfPluginInfoIJson.parse((JSONObject) plugins.get(i));
            }
            return zosmfInfoResponse.zosmfPluginsInfo(zosmfPluginsInfo).build();
        }

        return zosmfInfoResponse.build();
    }

}
