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

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;

/**
 * Http put operation with text content type
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class PutTextZosmfRequest extends ZosmfRequest {

    private static final Logger LOG = LoggerFactory.getLogger(PutTextZosmfRequest.class);

    /**
     * Text representation
     */
    private String body;

    /**
     * PutTextZosmfRequest constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public PutTextZosmfRequest(final ZosConnection connection) {
        super(connection);
    }

    /**
     * Perform the http rest request
     *
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @Override
    public Response executeRequest() throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(url == null, "url is null");
        ValidateUtils.checkNullParameter(body == null, "body is null");
        HttpResponse<String> reply;
        try {
            reply = cookie != null ? Unirest.put(url).cookie(cookie).headers(headers).body(body).asString() :
                    Unirest.put(url).headers(headers).body(body).asString();
        } catch (UnirestException e) {
            throw new ZosmfRequestException(e.getMessage(), e);
        }
        return buildResponse(reply);
    }

    /**
     * Set the body information for the http request
     *
     * @param body String value
     * @author Frank Giordano
     */
    @Override
    public void setBody(final Object body) {
        this.body = (String) body;
        LOG.debug(this.body);
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
