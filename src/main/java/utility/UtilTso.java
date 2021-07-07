package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zostso.TsoConstants;
import zostso.zosmf.TsoMessage;
import zostso.zosmf.TsoMessages;
import zostso.zosmf.TsoPromptMessage;
import zostso.zosmf.ZosmfTsoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UtilTso {

    private static final Logger LOG = LogManager.getLogger(UtilTso.class);

    /*
    following json parsing is being constructed (currently partial) to conform to the following
    format: https://www.ibm.com/docs/en/zos/2.1.0?topic=services-tsoe-address-space
    */

    public static ZosmfTsoResponse parseJsonTsoResponse(JSONObject result) {
        Util.checkNullParameter(result == null, "No results for tso command.");

        LOG.info(result);

        ZosmfTsoResponse response = new ZosmfTsoResponse.Builder().queueId((String) result.get("queueID"))
                        .ver((String) result.get("ver")).servletKey((String) result.get("servletKey"))
                        .reused((boolean) result.get("reused")).timeout((boolean) result.get("timeout")).build();

        List<TsoMessages> tsoMessagesLst = new ArrayList<>();
        JSONArray tsoData = (JSONArray) result.get("tsoData");
        tsoData.forEach(item -> {
            JSONObject obj = (JSONObject) item;
            TsoMessages tsoMessages = new TsoMessages();
            parseJsonTsoMessage(tsoMessagesLst, obj, tsoMessages);
            parseJsonTsoPrompt(tsoMessagesLst, obj, tsoMessages);
        });
        response.setTsoData(tsoMessagesLst);

        return response;
    }

    public static boolean parseJsonTsoMessage(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages tsoMessages) {
        Map tsoMessageMap = ((Map) obj.get(TsoConstants.TSO_MESSAGE));
        if (tsoMessageMap != null) {
            TsoMessage tsoMessage = new TsoMessage();
            tsoMessageMap.forEach((key, value) -> {
                if ("DATA".equals(key))
                    tsoMessage.setData((String) value);
                if ("VERSION".equals(key))
                    tsoMessage.setVersion((String) value);
            });
            tsoMessages.setTsoMessage(tsoMessage);
            tsoMessagesLst.add(tsoMessages);
            return true;
        }
        return false;
    }

    public static boolean parseJsonTsoPrompt(List<TsoMessages> tsoMessagesLst, JSONObject obj, TsoMessages tsoMessages) {
        Map tsoPromptMap = ((Map) obj.get(TsoConstants.TSO_PROMPT));
        if (tsoPromptMap != null) {
            TsoPromptMessage tsoPromptMessage = new TsoPromptMessage();
            tsoPromptMap.forEach((key, value) -> {
                if ("VERSION".equals(key))
                    tsoPromptMessage.setVersion((String) value);
                if ("HIDDEN".equals(key))
                    tsoPromptMessage.setHidden((String) value);
            });
            tsoMessages.setTsoPrompt(tsoPromptMessage);
            tsoMessagesLst.add(tsoMessages);
            return true;
        }
        return false;
    }

    // TODO

}
