/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package rest;

import core.ZOSConnection;

/**
 * Request factory that generates the desire CRUD operation
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZoweRequestFactory {

    /**
     * Assign the request to the Http verb type request object
     *
     * @param connection connection information, see ZOSConnection object
     * @param url        rest url value
     * @param body       content for request body
     * @param type       request http type, see ZoweRequestType.VerbType object
     * @author Frank Giordano
     */
    public static ZoweRequest buildRequest(ZOSConnection connection, String url, String body,
                                           ZoweRequestType.VerbType type) throws Exception {
        ZoweRequest request;
        switch (type) {
            case GET_JSON:
                request = new JsonGetRequest(connection, url);
                break;
            case PUT_JSON:
                request = new JsonPutRequest(connection, url, body);
                break;
            case POST_JSON:
                request = new JsonPostRequest(connection, url, body);
                break;
            case DELETE_JSON:
                request = new JsonDeleteRequest(connection, url);
                break;
            case GET_TEXT:
                request = new TextGetRequest(connection, url);
                break;
            case PUT_TEXT:
                request = new TextPutRequest(connection, url, body);
                break;
            case GET_STREAM:
                request = new StreamGetRequest(connection, url);
                break;
            default:
                throw new Exception("no valid type specified");
        }
        return request;
    }

}
