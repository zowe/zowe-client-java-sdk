package zowe.client.sdk.parsejson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;
import zowe.client.sdk.zosmfinfo.response.ZosmfPluginInfo;

public class ParseZosmfInfoJson implements IParseJson<ZosmfInfoResponse> {

    @Override
    public ZosmfInfoResponse parse(JSONObject jsonObject) {
        ZosmfInfoResponse.Builder zosmfInfoResponse = new ZosmfInfoResponse.Builder()
                .zosVersion((String) jsonObject.get("zos_version"))
                .zosmfPort((String) jsonObject.get("zosmf_port"))
                .zosmfVersion((String) jsonObject.get("zosmf_version"))
                .zosmfHostName((String) jsonObject.get("zosmf_hostname"))
                .zosmfSafRealm((String) jsonObject.get("zosmf_saf_realm"))
                .apiVersion((String) jsonObject.get("api_version"))
                .zosmfFullVersion((String) jsonObject.get("zosmf_full_version"));

        JSONArray plugins = (JSONArray) jsonObject.get("plugins");
        if (plugins != null) {
            int size = plugins.size();
            ZosmfPluginInfo[] zosmfPluginsInfo = new ZosmfPluginInfo[size];
            for (int i = 0; i < size; i++) {
                IParseJson<ZosmfPluginInfo> zosmfPluginInfoIJson = new ParseZosmfPluginInfoJson();
                zosmfPluginsInfo[i] = zosmfPluginInfoIJson.parse((JSONObject) plugins.get(i));
            }
            return zosmfInfoResponse.zosmfPluginsInfo(zosmfPluginsInfo).build();
        }

        return zosmfInfoResponse.build();
    }

}
