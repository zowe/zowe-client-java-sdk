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
 * Extract UNIX file from JSON response
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class UnixFileJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final UnixFileJsonParse instance = new UnixFileJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private UnixFileJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return TsoStopJsonParse object
     * @author Frank Giordano
     */
    public static UnixFileJsonParse getInstance() {
        return UnixFileJsonParse.Holder.instance;
    }

    /**
     * Transform data into UnixFile object
     *
     * @param args json data to parse
     * @return UssItem object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        return new UnixFile.Builder()
                .name(data.get("name") != null ? (String) data.get("name") : null)
                .mode(data.get("mode") != null ? (String) data.get("mode") : null)
                .size(data.get("size") != null ? (Long) data.get("size") : null)
                .uid(data.get("uid") != null ? (Long) data.get("uid") : null)
                .user(data.get("user") != null ? (String) data.get("user") : null)
                .gid(data.get("gid") != null ? (Long) data.get("gid") : null)
                .group(data.get("group") != null ? (String) data.get("group") : null)
                .mtime(data.get("mtime") != null ? (String) data.get("mtime") : null)
                .target(data.get("target") != null ? (String) data.get("target") : null)
                .build();
    }

}
