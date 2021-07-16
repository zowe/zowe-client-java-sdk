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
import zostso.TsoConstants;
import zostso.zosmf.*;

import java.util.*;

public class UtilTso {

    /*
    following json parsing is being constructed to conform to the following format:
    https://www.ibm.com/docs/en/zos/2.1.0?topic=services-tsoe-address-space
    */

    public static ZosmfTsoResponse parseJsonStopResponse(JSONObject obj) {
        Util.checkNullParameter(obj == null, "no obj to parse");
        return new ZosmfTsoResponse.Builder().ver((String) obj.get("ver")).servletKey((String) obj.get("servletKey"))
                .reused((boolean) obj.get("reused")).timeout((boolean) obj.get("timeout")).build();
    }

    public static ZosmfTsoResponse getZosmfTsoResponse(Response response) throws Exception {
        Util.checkNullParameter(response == null, "response is null");
        ZosmfTsoResponse result;
        if (response.getStatusCode().isPresent() && response.getStatusCode().get() != 200) {
            String errorMsg = (String) response.getResult().orElseThrow(() -> new Exception("results not available"));
            ZosmfMessages zosmfMsg = new ZosmfMessages(Optional.of(errorMsg), Optional.empty(), Optional.empty());
            result = new ZosmfTsoResponse.Builder().msgData(Arrays.asList(zosmfMsg)).build();
        } else {
            result = UtilTso.parseJsonTsoResponse((JSONObject) (response.getResult().isPresent() ?
                    response.getResult().get() : null));
        }

        return result;
    }

    private static ZosmfTsoResponse parseJsonTsoResponse(JSONObject result) throws Exception {
        Util.checkNullParameter(result == null, "no results to parse");

        ZosmfTsoResponse response = null;
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

    private static boolean parseJsonTsoMessage(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages
            tsoMessages) {
        Map tsoMessageMap = ((Map) obj.get(TsoConstants.TSO_MESSAGE));
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
            return true;
        }
        return false;
    }

    private static boolean parseJsonTsoPrompt(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages
            tsoMessages) {
        Map tsoPromptMap = ((Map) obj.get(TsoConstants.TSO_PROMPT));
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
            return true;
        }
        return false;
    }

}
