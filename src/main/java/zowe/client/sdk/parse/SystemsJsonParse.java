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
import zowe.client.sdk.zosmfinfo.response.DefinedSystem;
import zowe.client.sdk.zosmfinfo.response.ZosmfSystemsResponse;

/**
 * Parse json response from z/OSMF defined systems request
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class SystemsJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final SystemsJsonParse instance = new SystemsJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private SystemsJsonParse() {
    }

    /**
     * Get singleton instance
     *
     * @return SystemsJsonParse object
     * @author Frank Giordano
     */
    public static SystemsJsonParse getInstance() {
        return SystemsJsonParse.Holder.instance;
    }

    /**
     * Transform data into ZosmfListDefinedSystemsResponse object
     *
     * @param args json data to parse
     * @return ZosmfListDefinedSystemsResponse object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        final ZosmfSystemsResponse.Builder systemsResponse = new ZosmfSystemsResponse.Builder()
                .numRows((Long) data.get("numRows"));

        final JSONArray items = (JSONArray) data.get("items");
        if (items != null) {
            int size = items.size();
            final DefinedSystem[] definedSystems = new DefinedSystem[size];
            for (int i = 0; i < size; i++) {
                definedSystems[i] = parseDefinedSystem((JSONObject) items.get(i));
            }
            return systemsResponse.definedSystems(definedSystems).build();
        }
        return systemsResponse.build();
    }

    /**
     * Transform json into DefinedSystem object
     *
     * @param data json object
     * @return DefinedSystem object
     * @author Frank Giordano
     */
    private static DefinedSystem parseDefinedSystem(final JSONObject data) {
        return new DefinedSystem.Builder()
                .systemNickName(data.get("systemNickName") != null ? (String) data.get("systemNickName") : null)
                .groupNames(data.get("groupNames") != null ? (String) data.get("groupNames") : null)
                .cpcSerial(data.get("cpcSerial") != null ? (String) data.get("cpcSerial") : null)
                .zosVR(data.get("zosVR") != null ? (String) data.get("zosVR") : null)
                .systemName(data.get("systemName") != null ? (String) data.get("systemName") : null)
                .jesType(data.get("jesType") != null ? (String) data.get("jesType") : null)
                .sysplexName(data.get("sysplexName") != null ? (String) data.get("sysplexName") : null)
                .jesMemberName(data.get("jesMemberName") != null ? (String) data.get("jesMemberName") : null)
                .httpProxyName(data.get("httpProxyName") != null ? (String) data.get("httpProxyName") : null)
                .ftpDestinationName(data.get("ftpDestinationName") != null ? (String) data.get("ftpDestinationName") : null)
                .url(data.get("url") != null ? (String) data.get("url") : null)
                .cpcName(data.get("cpcName") != null ? (String) data.get("cpcName") : null)
                .build();
    }

}
