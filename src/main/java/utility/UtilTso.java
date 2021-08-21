/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import org.json.JSONArray;
import org.json.JSONObject;
import rest.Response;
import zostso.TsoConstants;
import zostso.zosmf.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Utility Class for Tso command related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class UtilTso {

    /*
    following json parsing is being constructed to conform to the following format:
    https://www.ibm.com/docs/en/zos/2.1.0?topic=services-tsoe-address-space
    */

    /**
     * Retrieve parsed Json Tso Stop Response
     *
     * @param obj JSONObject object
     * @return ZosmfTsoResponse populated console response, @see ZosmfTsoResponse
     * @author Frank Giordano
     */
    public static ZosmfTsoResponse parseJsonStopResponse(JSONObject obj) {
        Util.checkNullParameter(obj == null, "no obj to parse");
        Supplier<Stream<String>> keys = Util.getStreamSupplier(obj);
        return new ZosmfTsoResponse.Builder()
                .ver(keys.get().filter("ver"::equals).findFirst().isPresent() ? (String) obj.get("ver") : null)
                .servletKey(keys.get().filter("servletKey"::equals).findFirst().isPresent() ? (String) obj.get("servletKey") : null)
                .reused(keys.get().filter("reused"::equals).findFirst().isPresent() ? (boolean) obj.get("reused") : null)
                .timeout(keys.get().filter("timeout"::equals).findFirst().isPresent() ? (boolean) obj.get("timeout") : null)
                .build();
    }

    /**
     * Retrieve Tso response
     *
     * @param response Response object
     * @return ZosmfTsoResponse ZosmfTsoResponse object
     * @throws Exception error processing response
     * @author Frank Giordano
     */
    public static ZosmfTsoResponse getZosmfTsoResponse(Response response) throws Exception {
        Util.checkNullParameter(response == null, "response is null");
        ZosmfTsoResponse result;
        int statusCode = response.getStatusCode().get();
        if (response.getStatusCode().isPresent() && UtilRest.isHttpError(statusCode)) {
            String errorMsg = (String) response.getResponsePhrase().orElseThrow(() -> new Exception("results not available"));
            ZosmfMessages zosmfMsg = new ZosmfMessages(Optional.of(errorMsg), Optional.empty(), Optional.empty());
            result = new ZosmfTsoResponse.Builder().msgData(Arrays.asList(zosmfMsg)).build();
        } else {
            result = UtilTso.parseJsonTsoResponse((JSONObject) (response.getResponsePhrase().isPresent() ?
                    response.getResponsePhrase().get() : null));
        }

        return result;
    }

    private static ZosmfTsoResponse parseJsonTsoResponse(JSONObject result) throws Exception {
        Util.checkNullParameter(result == null, "no results to parse");

        Supplier<Stream<String>> keys = Util.getStreamSupplier(result);

        ZosmfTsoResponse response;
        try {
            response = new ZosmfTsoResponse.Builder()
                    .queueId(keys.get().filter("queueID"::equals).findFirst().isPresent() ? (String) result.get("queueID") : null)
                    .ver(keys.get().filter("ver"::equals).findFirst().isPresent() ? (String) result.get("ver") : null)
                    .servletKey(keys.get().filter("servletKey"::equals).findFirst().isPresent() ? (String) result.get("servletKey") : null)
                    .reused(keys.get().filter("reused"::equals).findFirst().isPresent() ? (boolean) result.get("reused") : null)
                    .timeout(keys.get().filter("timeout"::equals).findFirst().isPresent() ? (boolean) result.get("timeout") : null)
                    .build();
        } catch (Exception e) {
            throw new Exception("missing one of the following json field values: queueID, ver, servletKey, " +
                    "reused and timeout");
        }

        List<TsoMessages> tsoMessagesLst = new ArrayList<>();
        Optional<JSONArray> tsoData = Optional.ofNullable(
                keys.get().filter("tsoData"::equals).findFirst().isPresent() ? (JSONArray) result.get("tsoData") : null);

        if (tsoData.isPresent()) {
            tsoData.get().forEach(item -> {
                JSONObject obj = (JSONObject) item;
                TsoMessages tsoMessages = new TsoMessages();
                parseJsonTsoMessage(tsoMessagesLst, obj, tsoMessages);
                parseJsonTsoPrompt(tsoMessagesLst, obj, tsoMessages);
            });
            response.setTsoData(tsoMessagesLst);
        }

        return response;
    }

    private static void parseJsonTsoMessage(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages
            tsoMessages) {
        Supplier<Stream<String>> keys = Util.getStreamSupplier(obj);
        Map tsoMessageMap = (keys.get().filter(
                TsoConstants.TSO_MESSAGE::equals).findFirst().isPresent()
                ? (Map) obj.getJSONObject(TsoConstants.TSO_MESSAGE).toMap() : null);
        if (tsoMessageMap != null) {
            TsoMessage tsoMessage = new TsoMessage();
            tsoMessageMap.forEach((key, value) -> {
                if ("DATA".equals(key))
                    tsoMessage.setData(Optional.of((String) value));
                if ("VERSION".equals(key))
                    tsoMessage.setVersion(Optional.of((String) value));
            });
            tsoMessages.setTsoMessage(Optional.of(tsoMessage));
            tsoMessagesLst.add(tsoMessages);
        }
    }

    private static void parseJsonTsoPrompt(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages
            tsoMessages) {
        Supplier<Stream<String>> keys = Util.getStreamSupplier(obj);
        Map tsoPromptMap = (keys.get().filter(
                TsoConstants.TSO_PROMPT::equals).findFirst().isPresent()
                ? (Map) obj.getJSONObject(TsoConstants.TSO_PROMPT).toMap() : null);
        if (tsoPromptMap != null) {
            TsoPromptMessage tsoPromptMessage = new TsoPromptMessage();
            tsoPromptMap.forEach((key, value) -> {
                if ("VERSION".equals(key))
                    tsoPromptMessage.setVersion(Optional.of((String) value));
                if ("HIDDEN".equals(key))
                    tsoPromptMessage.setHidden(Optional.of((String) value));
            });
            tsoMessages.setTsoPrompt(Optional.of(tsoPromptMessage));
            tsoMessagesLst.add(tsoMessages);
        }
    }

}
