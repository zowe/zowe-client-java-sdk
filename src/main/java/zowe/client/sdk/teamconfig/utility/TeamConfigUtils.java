/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.utility;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * TeamConfigUtils static class provides helper method(s).
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class TeamConfigUtils {

    /**
     * Parse Json representation of properties section.
     *
     * @param obj JSONObject object
     * @return hashmap of property values
     * @author Frank Giordano
     */
    public static Map<String, String> parseJsonPropsObj(JSONObject obj) {
        // example of props json value to parse properties='{"rejectUnauthorized":false,"host":"mvsxe47.lvn.company.net"}'
        final Map<String, String> props = new HashMap<>();
        if (obj == null) {
            return props;
        }
        for (Object keyValObj : obj.keySet()) {
            final String key = (String) keyValObj;
            String value = null;
            try {
                value = (String) obj.get(key);
            } catch (Exception e) {
                if (e.getMessage().contains("java.lang.Long")) {
                    value = String.valueOf(obj.get(key));
                }
            }
            props.put(key, value);
        }
        return props;
    }

}
