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
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.utility.EncodeUtils;

/**
 * Http get stream operation with Json content type
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class StreamGetRequest extends ZoweRequest {

    /**
     * StreamGetRequest constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public StreamGetRequest(ZosConnection connection) {
        super(connection);
    }

    /**
     * Perform the http rest request
     *
     * @author Frank Giordano
     */
    @Override
    public Response executeRequest() throws UnirestException {
        HttpResponse<byte[]> reply = Unirest.get(url).headers(headers).asBytes();
        if (reply.getStatusText().contains("No Content")) {
            return new Response(reply.getStatusText(), reply.getStatus(), reply.getStatusText());
        }
        return new Response(reply.getBody(), reply.getStatus(), reply.getStatusText());
    }

    /**
     * Method to set the body information for the http request which is not used for this request.
     *
     * @author Frank Giordano
     */
    @Override
    public void setBody(String body) throws UnirestException {
        throw new UnirestException("setting body for this request is invalid");
    }

    /**
     * Set the standard headers for the http request
     *
     * @author Frank Giordano
     */
    @Override
    public void setStandardHeaders() {
        headers.put("Authorization", "Basic " + EncodeUtils.getAuthEncoding(connection));
        headers.put("Content-Type", "application/json");
        headers.put(X_CSRF_ZOSMF_HEADER_KEY, X_CSRF_ZOSMF_HEADER_VALUE);
    }

}
