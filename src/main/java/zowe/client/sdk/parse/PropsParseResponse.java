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
 * @version 2.0
 */
public class PropsParseResponse implements JsonParseResponse {

    /**
     * Represents one singleton instance
     */
    private static JsonParseResponse INSTANCE;

    /**
     * JSON data value to be parsed
     */
    private JSONObject data;

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private PropsParseResponse() {
    }

    /**
     * Get singleton instance
     *
     * @return PropsParseResponse object
     * @author Frank Giordano
     */
    public synchronized static JsonParseResponse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PropsParseResponse();
        }
        return INSTANCE;
    }

    /**
     * Parse team config's properties json representation into a Map object
     *
     * @return hashmap of property values
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
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
        data = null;
        return props;
    }

    /**
     * Set the data to be parsed
     *
     * @param data json data to parse
     * @return JsonParseResponse this object
     * @author Frank Giordano
     */
    @Override
    public JsonParseResponse setJsonObject(final JSONObject data) {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.DATA_NULL_MSG);
        this.data = data;
        return this;
    }

}
