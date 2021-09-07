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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.Response;
import zostso.StartStopResponse;
import zostso.TsoConstants;
import zostso.zosmf.*;

import java.util.*;

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
     * Populate either a Tso start or stop command phase
     *
     * @param zosmfResponse zosmf repsonse info, see zosmfResponse
     * @return StartStopResponse object
     * @author Frank Giordano
     */
    public static StartStopResponse populateStartAndStop(ZosmfTsoResponse zosmfResponse) {
        StartStopResponse startStopResponse = new StartStopResponse(false, zosmfResponse,
                zosmfResponse.getServletKey().orElse(""));

        startStopResponse.setSuccess(zosmfResponse.getServletKey().isPresent());
        if (zosmfResponse.getMsgData().isPresent()) {
            ZosmfMessages zosmfMsg = zosmfResponse.getMsgData().get().get(0);
            String msgText = zosmfMsg.getMessageText().orElse(TsoConstants.ZOSMF_UNKNOWN_ERROR);
            startStopResponse.setFailureResponse(msgText);
        }

        return startStopResponse;
    }

    /**
     * Retrieve parsed Json Tso Stop Response
     *
     * @param obj JSONObject object
     * @return populated console response, see ZosmfTsoResponse object
     * @author Frank Giordano
     */
    public static ZosmfTsoResponse parseJsonStopResponse(JSONObject obj) {
        Util.checkNullParameter(obj == null, "no obj to parse");
        return new ZosmfTsoResponse.Builder().ver((String) obj.get("ver")).servletKey((String) obj.get("servletKey"))
                .reused((boolean) obj.get("reused")).timeout((boolean) obj.get("timeout")).build();
    }

    /**
     * Retrieve Tso response
     *
     * @param response Response object
     * @return ZosmfTsoResponse object
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

    @SuppressWarnings("unchecked")
    private static ZosmfTsoResponse parseJsonTsoResponse(JSONObject result) throws Exception {
        Util.checkNullParameter(result == null, "no results to parse");

        ZosmfTsoResponse response;
        try {
            response = new ZosmfTsoResponse.Builder().queueId((String) result.get("queueID"))
                    .ver((String) result.get("ver")).servletKey((String) result.get("servletKey"))
                    .reused((boolean) result.get("reused")).timeout((boolean) result.get("timeout")).build();
        } catch (Exception e) {
            throw new Exception("missing one of the following json field values: queueID, ver, servletKey, " +
                    "reused and timeout");
        }

        List<TsoMessages> tsoMessagesLst = new ArrayList<>();
        Optional<JSONArray> tsoData = Optional.ofNullable((JSONArray) result.get("tsoData"));

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

    @SuppressWarnings("unchecked")
    private static void parseJsonTsoMessage(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages
            tsoMessages) {
        Map<String, String> tsoMessageMap = ((Map<String, String>) obj.get(TsoConstants.TSO_MESSAGE));
        if (tsoMessageMap != null) {
            TsoMessage tsoMessage = new TsoMessage();
            tsoMessageMap.forEach((key, value) -> {
                if ("DATA".equals(key))
                    tsoMessage.setData(Optional.of(value));
                if ("VERSION".equals(key))
                    tsoMessage.setVersion(Optional.of(value));
            });
            tsoMessages.setTsoMessage(Optional.of(tsoMessage));
            tsoMessagesLst.add(tsoMessages);
        }
    }

    @SuppressWarnings("unchecked")
    private static void parseJsonTsoPrompt(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages
            tsoMessages) {
        Map<String, String> tsoPromptMap = ((Map<String, String>) obj.get(TsoConstants.TSO_PROMPT));
        if (tsoPromptMap != null) {
            TsoPromptMessage tsoPromptMessage = new TsoPromptMessage();
            tsoPromptMap.forEach((key, value) -> {
                if ("VERSION".equals(key))
                    tsoPromptMessage.setVersion(Optional.of(value));
                if ("HIDDEN".equals(key))
                    tsoPromptMessage.setHidden(Optional.of(value));
            });
            tsoMessages.setTsoPrompt(Optional.of(tsoPromptMessage));
            tsoMessagesLst.add(tsoMessages);
        }
    }

}
