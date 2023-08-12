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
import zowe.client.sdk.zosfiles.uss.response.UnixFile;

/**
 * Extract UNIX file from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UnixFileParseResponse extends JsonParseResponse {

    /**
     * UnixFileParseResponse constructor
     *
     * @param data json data value to be parsed
     * @author Frank Giordano
     */
    public UnixFileParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform data into UnixFile object
     *
     * @return UssItem object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        return new UnixFile.Builder()
                .name(data.get("name") != null ? (String) data.get("name") : null)
                .mode(data.get("mode") != null ? (String) data.get("mode") : null)
                .size(data.get("size") != null ? (Long) data.get("size") : null)
                .uid(data.get("uid") != null ? (Long) data.get("uid") : null)
                .user(data.get("user") != null ? (String) data.get("user") : null)
                .gid(data.get("gid") != null ? (Long) data.get("gid") : null)
                .group(data.get("group") != null ? (String) data.get("group") : null)
                .mtime(data.get("mtime") != null ? (String) data.get("mtime") : null)
                .build();
    }

}
