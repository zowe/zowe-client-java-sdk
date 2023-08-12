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
import zowe.client.sdk.zoslogs.response.ZosLogItem;
import zowe.client.sdk.zoslogs.response.ZosLogReply;

import java.util.ArrayList;
import java.util.List;

/**
 * Extract ZosLogReply from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosLogReplyParseResponse extends JsonParseResponse {

    private List<ZosLogItem> zosLogItems = new ArrayList<>();

    /**
     * JsonParseResponse constructor
     *
     * @param data json data value to be parsed
     */
    public ZosLogReplyParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform data into ZosLogReply object
     *
     * @return ZosLogReply object
     * @author Frank Giordano
     */
    @Override
    public ZosLogReply parseResponse() {
        return new ZosLogReply(
                data.get("timezone") != null ? (Long) data.get("timezone") : 0,
                data.get("nextTimestamp") != null ? (Long) data.get("nextTimestamp") : 0,
                data.get("source") != null ? (String) data.get("source") : null,
                data.get("totalitems") != null ? (Long) data.get("totalitems") : null,
                zosLogItems);
    }

    /**
     * Set zosLogItems value
     *
     * @param zosLogItems list of ZosLogItem objects
     * @author Frank Giordano
     */
    public void setZosLogItems(List<ZosLogItem> zosLogItems) {
        this.zosLogItems = zosLogItems;
    }

}
