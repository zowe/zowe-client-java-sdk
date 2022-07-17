package zowe.client.sdk.parsejson;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

public class ParseZosmfPluginInfoJson implements IParseJson<ZosmfPluginInfo> {

    @Override
    public ZosmfPluginInfo parse(JSONObject jsonObject) {
        return new ZosmfPluginInfo.Builder()
                .pluginVersion((String) jsonObject.get("pluginVersion"))
                .pluginDefaultName((String) jsonObject.get("pluginDefaultName"))
                .pluginStatus((String) jsonObject.get("pluginStatus")).build();
    }

}
