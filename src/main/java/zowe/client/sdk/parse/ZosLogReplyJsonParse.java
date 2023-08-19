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

    /**
     * Represents one singleton instance
     */
    private static JsonParse INSTANCE;

    private List<ZosLogItem> zosLogItems;

    /**
     * JSON data value to be parsed
     */
    private JSONObject data;

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
     * @return ZosLogReplyParseResponse object
     * @author Frank Giordano
     */
    public synchronized static JsonParse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ZosLogReplyJsonParse();
        }
        return INSTANCE;
    }

    /**
     * Transform data into ZosLogReply object
     *
     * @return ZosLogReply object
     * @author Frank Giordano
     */
    @Override
    public ZosLogReply parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
        ValidateUtils.checkNullParameter(zosLogItems == null, ParseConstants.REQUIRED_ACTION_ZOS_LOG_ITEMS_MSG);
        final ZosLogReply zosLogReply = new ZosLogReply(
                data.get("timezone") != null ? (Long) data.get("timezone") : 0,
                data.get("nextTimestamp") != null ? (Long) data.get("nextTimestamp") : 0,
                data.get("source") != null ? (String) data.get("source") : null,
                data.get("totalitems") != null ? (Long) data.get("totalitems") : 0,
                zosLogItems);
        data = null;
        zosLogItems = null;
        return zosLogReply;
    }

    /**
     * Set zosLogItems value
     *
     * @param zosLogItems list of ZosLogItem objects
     * @author Frank Giordano
     */
    public void setZosLogItems(final List<ZosLogItem> zosLogItems) {
        ValidateUtils.checkNullParameter(zosLogItems == null, "zosLogItems is null");
        this.zosLogItems = zosLogItems;
    }

    /**
     * Set the data to be parsed
     *
     * @param data json data to parse
     * @return JsonParseResponse this object
     * @author Frank Giordano
     */
    @Override
    public JsonParse setJsonObject(final JSONObject data) {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.DATA_NULL_MSG);
        this.data = data;
        return this;
    }

}
