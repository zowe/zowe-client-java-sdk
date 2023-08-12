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

/**
 * Extract UNIX zfs from json response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosLogParseResponse extends JsonParseResponse {

    private boolean isProcessResponse;

    /**
     * JsonParseResponse constructor
     *
     * @param data json data value to be parsed
     */
    public ZosLogParseResponse(JSONObject data) {
        super(data);
    }

    /**
     * Transform data into ZosLogItem object
     *
     * @return ZosLogItem object
     * @author Frank Giordano
     */
    @Override
    public ZosLogItem parseResponse() {
        final String message = processMessage(data, isProcessResponse);
        return new ZosLogItem.Builder()
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

}
