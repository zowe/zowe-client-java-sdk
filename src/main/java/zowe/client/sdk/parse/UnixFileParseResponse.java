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
import zowe.client.sdk.zosfiles.uss.response.UnixFile;

/**
 * Extract UNIX file from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class UnixFileParseResponse implements JsonParseResponse {

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
    private UnixFileParseResponse() {
    }

    /**
     * Get singleton instance
     *
     * @return UnixFileParseResponse object
     * @author Frank Giordano
     */
    public synchronized static JsonParseResponse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UnixFileParseResponse();
        }
        return INSTANCE;
    }

    /**
     * Transform data into UnixFile object
     *
     * @return UssItem object
     * @author Frank Giordano
     */
    @Override
    public Object parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
        UnixFile unixFile = new UnixFile.Builder()
                .name(data.get("name") != null ? (String) data.get("name") : null)
                .mode(data.get("mode") != null ? (String) data.get("mode") : null)
                .size(data.get("size") != null ? (Long) data.get("size") : null)
                .uid(data.get("uid") != null ? (Long) data.get("uid") : null)
                .user(data.get("user") != null ? (String) data.get("user") : null)
                .gid(data.get("gid") != null ? (Long) data.get("gid") : null)
                .group(data.get("group") != null ? (String) data.get("group") : null)
                .mtime(data.get("mtime") != null ? (String) data.get("mtime") : null)
                .build();
        data = null;
        return unixFile;
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
