/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest.unirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import zowe.client.sdk.core.ZOSConnection;

/**
 * Http get operation with Json content type
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class JsonGetRequest extends ZoweRequest {

    /**
     * JsonGetRequest constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public JsonGetRequest(ZOSConnection connection) {
        super(connection);
        this.setStandardHeaders();
    }

    /**
     * Perform the http rest request
     *
     * @author Frank Giordano
     */
    @Override
    public Response executeRequest() throws Exception {
        HttpResponse<JsonNode> reply = Unirest.get(url).headers(headers).asJson();
        return getJsonResponse(reply);
    }

    /**
     * Method to set the body information for the http request which is not used for this request.
     *
     * @author Frank Giordano
     */
    @Override
    public void setBody(String body) throws Exception {
        throw new Exception("setting body for this request is invalid");
    }

}
