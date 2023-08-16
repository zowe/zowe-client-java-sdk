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

/**
 * Extract ZosLogItem from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosLogItemParseResponse implements JsonParseResponse {

    /**
     * Represents one singleton instance
     */
    private static JsonParseResponse INSTANCE;

    private boolean isProcessResponse;

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private ZosLogItemParseResponse() {
    }

    /**
     * JSON data value to be parsed
     */
    private JSONObject data;

    /**
     * Get singleton instance
     *
     * @return ZosLogItemParseResponse object
     * @author Frank Giordano
     */
    public synchronized static JsonParseResponse getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ZosLogItemParseResponse();
        }
        return INSTANCE;
    }

    /**
     * Transform data into ZosLogItem object
     *
     * @return ZosLogItem object
     * @author Frank Giordano
     */
    @Override
    public ZosLogItem parseResponse() {
        ValidateUtils.checkNullParameter(data == null, ParseConstants.REQUIRED_ACTION_MSG);
        final String message = processMessage(data, isProcessResponse);
        final ZosLogItem zosLogItem = new ZosLogItem.Builder()
                .cart(data.get("cart") != null ? (String) data.get("cart") : null)
                .color(data.get("color") != null ? (String) data.get("color") : null)
                .jobName(data.get("jobName") != null ? (String) data.get("jobName") : null)
                .message(message)
                .messageId(data.get("messageId") != null ? (String) data.get("messageId") : null)
                .replyId(data.get("replyId") != null ? (String) data.get("replyId") : null)
                .system(data.get("system") != null ? (String) data.get("system") : null)
                .type(data.get("type") != null ? (String) data.get("type") : null)
                .subType(data.get("subType") != null ? (String) data.get("subType") : null)
                .time(data.get("time") != null ? (String) data.get("time") : null)
                .timeStamp(data.get("timestamp") != null ? (Long) data.get("timestamp") : 0)
                .build();
        data = null;
        return zosLogItem;
    }

    /**
     * Process response message; message contains a log line statement.
     * Perform special newline replacement if applicable.
     *
     * @param data JSONObject object
     * @return string value of the message processed
     * @author Frank Giordano
     */
    private static String processMessage(JSONObject data, boolean isProcessResponse) {
        try {
            String message = (String) data.get("message");
            if (isProcessResponse) {
                if (message.contains("\r")) {
                    message = message.replace('\r', '\n');
                }
                if (message.contains("\n\n")) {
                    message = message.replaceAll("\n\n", "\n");
                }
            }
            return message;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Set isProcessResponse boolean value
     *
     * @param processResponse boolean value
     * @author Frank Giordano
     */
    public void setProcessResponse(boolean processResponse) {
        isProcessResponse = processResponse;
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
