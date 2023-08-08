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

import java.util.HashMap;
import java.util.Map;

/**
 * Parse json response into property list
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class PropsParseResponse extends JsonParseResponse {

    /**
     * PropsParseResponse constructor
     *
     * @param data json object
     * @author Frank Giordano
     */
    public PropsParseResponse(JSONObject data) {
        super(data);
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
