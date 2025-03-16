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

import org.json.simple.JSONObject;
import zowe.client.sdk.utility.ValidateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Parse json response into property list
 *
 * @author Frank Giordano
 * @version 3.0
 */
public final class PropsJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final PropsJsonParse instance = new PropsJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private PropsJsonParse() {
    }

    /**
     * Get singleton instance
     *
     * @return PropsJsonParse object
     * @author Frank Giordano
     */
    public static PropsJsonParse getInstance() {
        return PropsJsonParse.Holder.instance;
    }

    /**
     * Parse team config's properties json representation into a Map object
     *
     * @param args json data to parse
     * @return hashmap of property values
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        // i.e. properties='{"rejectUnauthorized":false,"host":"mvsxe47.lvn.company.net"}'
        final Map<String, String> props = new HashMap<>();
        data.keySet().forEach(key -> {
            String value = null;
            try {
                value = (String) data.get(key);
            } catch (Exception e) {
                if (e.getMessage().contains("java.lang.Long")) {
                    value = String.valueOf(data.get(key));
                }
            }
            props.put((String) key, value);
        });
        return props;
    }

}
