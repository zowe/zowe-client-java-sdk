package zowe.client.sdk.parsejson;

import org.json.simple.JSONObject;

public interface IParseJson<T> {

    T parse(JSONObject jsonObject);

}
