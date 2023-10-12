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
import zowe.client.sdk.zoslogs.response.ZosLogItem;
import zowe.client.sdk.zoslogs.response.ZosLogReply;

import java.util.List;

/**
 * Extract ZosLogReply from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class ZosLogReplyJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final ZosLogReplyJsonParse instance = new ZosLogReplyJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private ZosLogReplyJsonParse() {
    }

    /**
     * Get singleton instance
     *
     * @return ZosLogItemJsonParse object
     * @author Frank Giordano
     */
    public static ZosLogReplyJsonParse getInstance() {
        return ZosLogReplyJsonParse.Holder.instance;
    }

    /**
     * Transform data into ZosLogReply object
     *
     * @param args first arg json data to parse, second arg list of ZosLogItem objects
     * @return ZosLogReply object
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized ZosLogReply parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        ValidateUtils.checkNullParameter(args[1] == null, ParseConstants.LIST_OF_ZOS_LOG_ITEM_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        final List<ZosLogItem> zosLogItems = (List<ZosLogItem>) args[1];
        return new ZosLogReply(
                data.get("timezone") != null ? (Long) data.get("timezone") : 0,
                data.get("nextTimestamp") != null ? (Long) data.get("nextTimestamp") : 0,
                data.get("source") != null ? (String) data.get("source") : null,
                data.get("totalitems") != null ? (Long) data.get("totalitems") : 0,
                zosLogItems);
    }

}
