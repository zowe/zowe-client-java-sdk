package zowe.client.sdk.parsejson;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.zosmfinfo.response.DefinedSystem;
import zowe.client.sdk.zosmfinfo.response.ZosmfListDefinedSystemsResponse;

public class ParseZosmfListDefinedSystemsJson implements IParseJson<ZosmfListDefinedSystemsResponse> {

    @Override
    public ZosmfListDefinedSystemsResponse parse(JSONObject jsonObject) {
        ZosmfListDefinedSystemsResponse.Builder systemsResponse = new ZosmfListDefinedSystemsResponse.Builder()
                .numRows((Long) jsonObject.get("numRows"));

        JSONArray items = (JSONArray) jsonObject.get("items");
        if (items != null) {
            int size = items.size();
            DefinedSystem[] definedSystems = new DefinedSystem[size];
            for (int i = 0; i < size; i++) {
                JSONObject obj = (JSONObject) items.get(i);
                IParseJson<DefinedSystem> definedSystemIJson = new ParseZosmfDefinedSystemJson();
                definedSystems[i] = definedSystemIJson.parse((JSONObject) items.get(i));
            }
            return systemsResponse.definedSystems(definedSystems).build();
        }

        return systemsResponse.build();
    }

}
