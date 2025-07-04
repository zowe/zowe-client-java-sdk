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
import zowe.client.sdk.zosfiles.dsn.response.Member;

/**
 * Extract Member from json response
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class MemberJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final MemberJsonParse instance = new MemberJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private MemberJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return MemberJsonParse object
     * @author Frank Giordano
     */
    public static MemberJsonParse getInstance() {
        return Holder.instance;
    }

    /**
     * Transform data into a Member object
     *
     * @param args json data to parse
     * @return Member object
     * @author Frank Giordano
     */
    @Override
    public synchronized Object parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        return new Member.Builder()
                .member(data.get("member") != null ? (String) data.get("member") : null)
                .vers(data.get("vers") != null ? (Long) data.get("vers") : 0)
                .mod(data.get("mod") != null ? (Long) data.get("mod") : 0)
                .c4date(data.get("c4date") != null ? (String) data.get("c4date") : null)
                .m4date(data.get("m4date") != null ? (String) data.get("m4date") : null)
                .cnorc(data.get("cnorc") != null ? (Long) data.get("cnorc") : 0)
                .inorc(data.get("inorc") != null ? (Long) data.get("inorc") : 0)
                .mnorc(data.get("mnorc") != null ? (Long) data.get("mnorc") : 0)
                .mtime(data.get("mtime") != null ? (String) data.get("mtime") : null)
                .msec(data.get("msec") != null ? (String) data.get("msec") : null)
                .user(data.get("user") != null ? (String) data.get("user") : null)
                .sclm(data.get("sclm") != null ? (String) data.get("sclm") : null)
                .build();
    }

}
