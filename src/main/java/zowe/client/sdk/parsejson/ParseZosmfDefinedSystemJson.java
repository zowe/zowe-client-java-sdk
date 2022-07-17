package zowe.client.sdk.json;

import org.json.simple.JSONObject;
import zowe.client.sdk.zosmfinfo.response.DefinedSystem;

public class ZosmfDefinedSystemJson implements IJson<DefinedSystem> {

    @Override
    public DefinedSystem parseJsonObject(JSONObject jsonObject) {
        return new DefinedSystem.Builder()
                .systemNickName((String) jsonObject.get("systemNickName"))
                .groupNames((String) jsonObject.get("groupNames"))
                .cpcSerial((String) jsonObject.get("cpcSerial"))
                .zosVR((String) jsonObject.get("zosVR"))
                .systemName((String) jsonObject.get("systemName"))
                .jesType((String) jsonObject.get("jesType"))
                .sysplexName((String) jsonObject.get("sysplexName"))
                .jesMemberName((String) jsonObject.get("jesMemberName"))
                .httpProxyName((String) jsonObject.get("httpProxyName"))
                .ftpDestinationName((String) jsonObject.get("ftpDestinationName"))
                .url((String) jsonObject.get("url"))
                .cpcName((String) jsonObject.get("cpcName")).build();
    }

}
