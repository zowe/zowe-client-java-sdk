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
 * @version 3.0
 */
public final class ZosLogItemJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final ZosLogItemJsonParse instance = new ZosLogItemJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private ZosLogItemJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return ZosLogItemJsonParse object
     * @author Frank Giordano
     */
    public static ZosLogItemJsonParse getInstance() {
        return ZosLogItemJsonParse.Holder.instance;
    }

    /**
     * Transform data into ZosLogItem object
     *
     * @param args first arg JSON data to parse, second arg isProcessResponse boolean value
     * @return ZosLogItem object
     * @author Frank Giordano
     */
    @Override
    public synchronized ZosLogItem parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        ValidateUtils.checkNullParameter(args[1] == null, ParseConstants.IS_PROCESS_RESPONSE_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        final String message = processMessage(data, (boolean) args[1]);
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
    private static String processMessage(final JSONObject data, final boolean isProcessResponse) {
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

}
