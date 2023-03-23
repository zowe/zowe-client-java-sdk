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
import com.mashape.unirest.http.Unirest;
import zowe.client.sdk.core.ZOSConnection;

/**
 * Http get operation with text content type
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TextGetRequest extends ZoweRequest {

    /**
     * TextGetRequest constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public TextGetRequest(ZOSConnection connection) {
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
        HttpResponse<String> reply = Unirest.get(url).headers(headers).asString();
        return new Response(reply.getBody(), reply.getStatus(), reply.getStatusText());
    }

    /**
     * Set the body information for the http request
     *
     * @author Frank Giordano
     */
    @Override
    public void setBody(String body) throws Exception {
        throw new Exception("setting body for this request is invalid");
    }

}
