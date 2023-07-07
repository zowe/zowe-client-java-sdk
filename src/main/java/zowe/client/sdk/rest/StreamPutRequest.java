/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;

/**
 * Http put stream operation with binary content type
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class StreamPutRequest extends ZoweRequest {

    /**
     * Binary data representation
     */
    private byte[] body;

    /**
     * ZoweRequest constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public StreamPutRequest(ZosConnection connection) {
        super(connection);
    }

    /**
     * Perform the http rest request
     *
     * @author Frank Giordano
     */
    @Override
    public Response executeRequest() throws UnirestException {
        ValidateUtils.checkNullParameter(url == null, "url is null");
        ValidateUtils.checkNullParameter(body == null, "body is null");
        HttpResponse<JsonNode> reply = Unirest.put(url).headers(headers).body(body).asJson();
        if (reply.getStatusText().contains("No Content")) {
            return new Response(reply.getStatusText(), reply.getStatus(), reply.getStatusText());
        }
        return getJsonResponse(reply);
    }

    /**
     * Set the body byte array value for request
     *
     * @param body byte array value
     * @author Frank Giordano
     */
    @Override
    public void setBody(Object body) {
        this.body = (byte[]) body;
    }

    /**
     * Set the standard headers for the http request
     *
     * @author Frank Giordano
     */
    @Override
    public void setStandardHeaders() {
        headers.put("Authorization", "Basic " + EncodeUtils.getAuthEncoding(connection));
        headers.put("Content-Type", "binary");
        headers.put(X_CSRF_ZOSMF_HEADER_KEY, X_CSRF_ZOSMF_HEADER_VALUE);
    }

}