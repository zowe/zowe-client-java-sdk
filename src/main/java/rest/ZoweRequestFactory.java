package rest;

import core.ZOSConnection;

import java.util.Optional;

public class ZoweRequestFactory {

    public static ZoweRequest buildRequest(ZOSConnection connection, String url, String body,
                                           ZoweRequestType.RequestType type) throws Exception {
        ZoweRequest request;
        switch (type) {
            case GET_JSON:
                request = new JsonGetRequest(connection, Optional.ofNullable(url));
                break;
            case PUT_JSON:
                request = new JsonPutRequest(connection, Optional.ofNullable(url), Optional.ofNullable(body));
                break;
            case POST_JSON:
                request = new JsonPostRequest(connection, Optional.ofNullable(url));
                break;
            case DELETE_JSON:
                request = new JsonDeleteRequest(connection, Optional.ofNullable(url));
                break;
            case GET_TEXT:
                request = new TextGetRequest(connection, Optional.ofNullable(url));
                break;
            case PUT_TEXT:
                request = new TextPutRequest(connection, Optional.ofNullable(url), Optional.ofNullable(body));
                break;
            default:
                throw new Exception("no valid type specified");
        }
        return request;
    }

}
