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

/**
 * Extract UNIX zfs from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosLogParseResponse extends JsonParseResponse {

    /**
     * JsonParseResponse constructor
     *
     * @param data json data value to be parsed
     */
    public ZosLogParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform zos log json into ZosLogReply object
     *
     * @return UssZfsItem object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        return null;
    }

}
