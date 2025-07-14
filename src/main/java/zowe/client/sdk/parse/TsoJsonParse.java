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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.message.TsoMessage;
import zowe.client.sdk.zostso.message.TsoMessages;
import zowe.client.sdk.zostso.message.TsoPromptMessage;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Parse JSON response from Tso request
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class TsoJsonParse implements JsonParse {

    private static class Holder {

        /**
         * Represents one singleton instance
         */
        private static final TsoJsonParse instance = new TsoJsonParse();

    }

    /**
     * Private constructor defined to avoid public instantiation of class
     *
     * @author Frank Giordano
     */
    private TsoJsonParse() {
    }

    /**
     * Get a singleton instance
     *
     * @return TsoJsonParse object
     * @author Frank Giordano
     */
    public static TsoJsonParse getInstance() {
        return TsoJsonParse.Holder.instance;
    }

    /*
    The following JSON parsing is being constructed to conform to the following format:
    https://www.ibm.com/docs/en/zos/2.1.0?topic=services-tsoe-address-space
    */

    /**
     * Parse a Tso command response into ZosmfTsoResponse object
     *
     * @param args json data to parse
     * @return ZosmfTsoResponse object
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    @Override
    public synchronized ZosmfTsoResponse parseResponse(final Object... args) {
        ValidateUtils.checkNullParameter(args[0] == null, ParseConstants.DATA_NULL_MSG);
        final JSONObject data = (JSONObject) args[0];
        final ZosmfTsoResponse response = new ZosmfTsoResponse.Builder()
                .queueId(data.get("queueID") != null ? (String) data.get("queueID") : null)
                .ver(data.get("ver") != null ? (String) data.get("ver") : null)
                .servletKey(data.get("servletKey") != null ? (String) data.get("servletKey") : null)
                .reused(data.get("reused") != null && (boolean) data.get("reused"))
                .timeout(data.get("timeout") != null && (boolean) data.get("timeout"))
                .build();

        final List<TsoMessages> tsoMessagesLst = new ArrayList<>();
        final Optional<JSONArray> tsoData = Optional.ofNullable((JSONArray) data.get("tsoData"));

        tsoData.ifPresent(d -> {
            d.forEach(item -> {
                final JSONObject obj = (JSONObject) item;
                final TsoMessages tsoMessages = new TsoMessages();
                parseJsonTsoMessage(tsoMessagesLst, obj, tsoMessages);
                parseJsonTsoPrompt(tsoMessagesLst, obj, tsoMessages);
            });
            response.setTsoData(tsoMessagesLst);
        });
        return response;
    }

    @SuppressWarnings("unchecked")
    private static void parseJsonTsoMessage(final List<TsoMessages> tsoMessagesLst, final JSONObject obj,
                                            final TsoMessages tsoMessages) {
        final Map<String, String> tsoMessageMap = ((Map<String, String>) obj.get(TsoConstants.TSO_MESSAGE));
        if (tsoMessageMap != null) {
            final TsoMessage tsoMessage = new TsoMessage();
            tsoMessageMap.forEach((key, value) -> {
                if ("DATA".equals(key)) {
                    tsoMessage.setData(value);
                }
                if ("VERSION".equals(key)) {
                    tsoMessage.setVersion(value);
                }
            });
            tsoMessages.setTsoMessage(tsoMessage);
            tsoMessagesLst.add(tsoMessages);
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseJsonTsoPrompt(final List<TsoMessages> tsoMessagesLst, final JSONObject obj,
                                           final TsoMessages tsoMessages) {
        final Map<String, String> tsoPromptMap = ((Map<String, String>) obj.get(TsoConstants.TSO_PROMPT));
        if (tsoPromptMap != null) {
            TsoPromptMessage tsoPromptMessage = new TsoPromptMessage();
            tsoPromptMap.forEach((key, value) -> {
                if ("VERSION".equals(key)) {
                    tsoPromptMessage.setVersion(value);
                }
                if ("HIDDEN".equals(key)) {
                    tsoPromptMessage.setHidden(value);
                }
            });
            tsoMessages.setTsoPrompt(tsoPromptMessage);
            tsoMessagesLst.add(tsoMessages);
        }
    }

}
