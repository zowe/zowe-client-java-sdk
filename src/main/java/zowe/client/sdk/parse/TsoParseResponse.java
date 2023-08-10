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
 * Parse json response from Tso request
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TsoParseResponse extends JsonParseResponse {

    /**
     * TsoParseResponse constructor
     *
     * @param data json object
     * @author Frank Giordano
     */
    public TsoParseResponse(JSONObject data) {
        super(data);
    }

    /*
    following json parsing is being constructed to conform to the following format:
    https://www.ibm.com/docs/en/zos/2.1.0?topic=services-tsoe-address-space
    */

    /**
     * Parse a Tso command response into ZosmfTsoResponse object
     *
     * @return ZosmfTsoResponse object
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    @Override
    public ZosmfTsoResponse parseResponse() {
        ZosmfTsoResponse response = new ZosmfTsoResponse.Builder()
                .queueId(data.get("queueID") != null ? (String) data.get("queueID") : null)
                .ver(data.get("ver") != null ? (String) data.get("ver") : null)
                .servletKey(data.get("servletKey") != null ? (String) data.get("servletKey") : null)
                .reused(data.get("reused") != null ? (boolean) data.get("reused") : null)
                .timeout(data.get("timeout") != null ? (boolean) data.get("timeout") : null)
                .build();

        List<TsoMessages> tsoMessagesLst = new ArrayList<>();
        final Optional<JSONArray> tsoData = Optional.ofNullable((JSONArray) data.get("tsoData"));

        tsoData.ifPresent(data -> {
            data.forEach(item -> {
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
    private static void parseJsonTsoMessage(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages tsoMessages) {
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
    private static void parseJsonTsoPrompt(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages tsoMessages) {
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
