package zowe.client.sdk.json;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

public class ZosmfPluginInfoJson implements IJson<ZosmfPluginInfo> {

    @Override
    public ZosmfPluginInfo parseJsonObject(JSONObject jsonObject) {
        return new ZosmfPluginInfo.Builder()
                .pluginVersion((String) jsonObject.get("pluginVersion"))
                .pluginDefaultName((String) jsonObject.get("pluginDefaultName"))
                .pluginStatus((String) jsonObject.get("pluginStatus")).build();
    }

}
