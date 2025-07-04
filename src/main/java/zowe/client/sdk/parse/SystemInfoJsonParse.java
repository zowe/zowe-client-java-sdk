/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.parse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

/**
 * Parse json response from z/OSMF status request
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class SystemInfoJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final SystemInfoJsonParse instance = new SystemInfoJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private SystemInfoJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return SystemInfoJsonParse object
     * @author Frank Giordano
     */
    public static SystemInfoJsonParse getInstance() {
        return SystemInfoJsonParse.Holder.instance;
    }

    /**
     * Transform data into ZosmfInfoResponse object
     *
     * @param args json data to parse
     * @return ZosmfInfoResponse object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        final ZosmfInfoResponse.Builder zosmfInfoResponse = new ZosmfInfoResponse.Builder()
                .zosVersion(data.get("zos_version") != null ? (String) data.get("zos_version") : null)
                .zosmfPort(data.get("zosmf_port") != null ? (String) data.get("zosmf_port") : null)
                .zosmfVersion(data.get("zosmf_version") != null ? (String) data.get("zosmf_version") : null)
                .zosmfHostName(data.get("zosmf_hostname") != null ? (String) data.get("zosmf_hostname") : null)
                .zosmfSafRealm(data.get("zosmf_saf_realm") != null ? (String) data.get("zosmf_saf_realm") : null)
                .apiVersion(data.get("api_version") != null ? (String) data.get("api_version") : null)
                .zosmfFullVersion(data.get("zosmf_full_version") != null ? (String) data.get("zosmf_full_version") : null);

        JSONArray plugins = null;
        try {
            plugins = data.get("plugins") != null ? (JSONArray) data.get("plugins") : null;
        } catch (Exception ignored) {
        }
        if (plugins != null) {
            int size = plugins.size();
            ZosmfPluginInfo[] zosmfPluginsInfo = new ZosmfPluginInfo[size];
            for (int i = 0; i < size; i++) {
                zosmfPluginsInfo[i] = parseZosmfPluginInfo((JSONObject) plugins.get(i));
            }
            return zosmfInfoResponse.zosmfPluginsInfo(zosmfPluginsInfo).build();
        }
        return zosmfInfoResponse.build();
    }

    /**
     * Transform json into ZosmfPluginInfo object
     *
     * @param data json object
     * @return ZosmfPluginInfo object
     * @author Frank Giordano
     */
    private static ZosmfPluginInfo parseZosmfPluginInfo(final JSONObject data) {
        return new ZosmfPluginInfo.Builder()
                .pluginVersion(data.get("pluginVersion") != null ? (String) data.get("pluginVersion") : null)
                .pluginDefaultName(data.get("pluginDefaultName") != null ? (String) data.get("pluginDefaultName") : null)
                .pluginStatus(data.get("pluginStatus") != null ? (String) data.get("pluginStatus") : null)
                .build();
    }

}
