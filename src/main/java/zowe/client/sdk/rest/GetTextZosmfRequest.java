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
import zowe.client.sdk.utility.ValidateUtils;

/**
 * Http get operation with text content type
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class GetTextZosmfRequest extends ZosmfRequest {

    /**
     * GetTextZosmfRequest constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public GetTextZosmfRequest(final ZosConnection connection) {
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
        HttpResponse<String> reply = Unirest.get(url).headers(headers).asString();
        return buildResponse(reply);
    }

    /**
     * Method to set the body information for the http request which is not used for this request.
     *
     * @author Frank Giordano
     */
    @Override
    public void setBody(Object body) throws UnirestException {
        throw new UnirestException("setting body for this request is invalid");
    }

    /**
     * Set the standard headers for the http request
     *
     * @author Frank Giordano
     */
    @Override
    public void setStandardHeaders() {
        headers.put("Authorization", "Basic " + EncodeUtils.encodeAuthComponent(connection));
        headers.put("Content-Type", "text/plain; charset=UTF-8");
        headers.put(X_CSRF_ZOSMF_HEADER_KEY, X_CSRF_ZOSMF_HEADER_VALUE);
    }

}
