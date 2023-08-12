package zowe.client.sdk.parse;

import org.json.simple.JSONObject;
import zowe.client.sdk.zoslogs.response.ZosLogItem;
import zowe.client.sdk.zoslogs.response.ZosLogReply;

import java.util.ArrayList;
import java.util.List;

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
