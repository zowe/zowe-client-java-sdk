package zowe.client.sdk.parsejson;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosconsole.zosmf.ZosmfIssueResponse;

public class ParseIssueCommandJson implements IParseJson<ZosmfIssueResponse> {

    @Override
    public ZosmfIssueResponse parse(JSONObject jsonObject) {
        ZosmfIssueResponse zosmfIssueResponse = new ZosmfIssueResponse();
        zosmfIssueResponse.setCmdResponseKey((String) jsonObject.get("cmd-response-key"));
        zosmfIssueResponse.setCmdResponseUrl((String) jsonObject.get("cmd-response-url"));
        zosmfIssueResponse.setCmdResponseUri((String) jsonObject.get("cmd-response-uri"));
        zosmfIssueResponse.setCmdResponse((String) jsonObject.get("cmd-response"));
        zosmfIssueResponse.setSolKeyDetected((String) jsonObject.get("sol-key-detected"));
        return zosmfIssueResponse;
    }

}
