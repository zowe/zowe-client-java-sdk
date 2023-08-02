/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.zosmfinfo.response.DefinedSystem;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;
import zowe.client.sdk.zosmfinfo.response.ZosmfListDefinedSystemsResponse;
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

/**
 * Utility class contains helper methods for z/OSMF status related processing
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class ZosmfUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ZosmfUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Transform JSON into DefinedSystem object
     *
     * @param jsonObject JSON object
     * @return DefinedSystem object
     * @author Frank Giordano
     */
    private static DefinedSystem parseDefinedSystem(JSONObject jsonObject) {
        return new DefinedSystem.Builder()
                .systemNickName((String) jsonObject.get("systemNickName"))
                .groupNames((String) jsonObject.get("groupNames"))
                .cpcSerial((String) jsonObject.get("cpcSerial"))
                .zosVR((String) jsonObject.get("zosVR"))
                .systemName((String) jsonObject.get("systemName"))
                .jesType((String) jsonObject.get("jesType"))
                .sysplexName((String) jsonObject.get("sysplexName"))
                .jesMemberName((String) jsonObject.get("jesMemberName"))
                .httpProxyName((String) jsonObject.get("httpProxyName"))
                .ftpDestinationName((String) jsonObject.get("ftpDestinationName"))
                .url((String) jsonObject.get("url"))
                .cpcName((String) jsonObject.get("cpcName")).build();
    }

    /**
     * Transform JSON into ZosmfListDefinedSystemsResponse object
     *
     * @param jsonObject JSON object
     * @return ZosmfListDefinedSystemsResponse object
     * @author Frank Giordano
     */
    public static ZosmfListDefinedSystemsResponse parseListDefinedSystems(JSONObject jsonObject) {
        ZosmfListDefinedSystemsResponse.Builder systemsResponse = new ZosmfListDefinedSystemsResponse.Builder()
                .numRows((Long) jsonObject.get("numRows"));

        JSONArray items = (JSONArray) jsonObject.get("items");
        if (items != null) {
            int size = items.size();
            DefinedSystem[] definedSystems = new DefinedSystem[size];
            for (int i = 0; i < size; i++) {
                definedSystems[i] = parseDefinedSystem((JSONObject) items.get(i));
            }
            return systemsResponse.definedSystems(definedSystems).build();
        }

        return systemsResponse.build();
    }

    /**
     * Transform JSON into ZosmfInfoResponse object
     *
     * @param jsonObject JSON object
     * @return ZosmfInfoResponse object
     * @author Frank Giordano
     */
    public static ZosmfInfoResponse parseZosmfInfo(JSONObject jsonObject) {
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
                zosmfPluginsInfo[i] = parseZosmfPluginInfo((JSONObject) plugins.get(i));
            }
            return zosmfInfoResponse.zosmfPluginsInfo(zosmfPluginsInfo).build();
        }

        return zosmfInfoResponse.build();
    }

    /**
     * Transform JSON into ZosmfPluginInfo object
     *
     * @param jsonObject JSON object
     * @return ZosmfPluginInfo object
     * @author Frank Giordano
     */
    private static ZosmfPluginInfo parseZosmfPluginInfo(JSONObject jsonObject) {
        return new ZosmfPluginInfo.Builder()
                .pluginVersion((String) jsonObject.get("pluginVersion"))
                .pluginDefaultName((String) jsonObject.get("pluginDefaultName"))
                .pluginStatus((String) jsonObject.get("pluginStatus")).build();
    }

}
