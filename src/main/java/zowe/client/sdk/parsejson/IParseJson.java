package zowe.client.sdk.json;

import org.json.simple.JSONObject;

public interface IJson<T> {

    T parseJsonObject(JSONObject jsonObject);

}
