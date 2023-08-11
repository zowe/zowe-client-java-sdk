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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Base abstract class that conforms to Http CRUD operations
 *
 * @author Frank Giordano
 * @version 2.0
 */
public abstract class ZoweRequest {

    private static final Logger LOG = LoggerFactory.getLogger(ZoweRequest.class);
    public static final String X_CSRF_ZOSMF_HEADER_KEY = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
    public static final String X_CSRF_ZOSMF_HEADER_VALUE = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);
    protected final ZosConnection connection;
    protected final Map<String, String> headers = new HashMap<>();
    protected String url;

    /**
     * ZoweRequest constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZoweRequest(ZosConnection connection) {
        this.connection = connection;
        this.setup();
    }

    /**
     * Retrieve the http response information
     *
     * @param reply json http response object
     * @return Response object
     * @author Frank Giordano
     */
    protected static Response getJsonResponse(HttpResponse<JsonNode> reply) {
        if (reply.getBody().isArray()) {
            return new Response(reply.getBody().getArray(), reply.getStatus(), reply.getStatusText());
        }
        return new Response(reply.getBody().getObject(), reply.getStatus(), reply.getStatusText());
    }

    /**
     * Setup to be used first in setting up the http request
     *
     * @author Frank Giordano
     */
    private void setup() {
        Unirest.config().verifySsl(false);
        this.setStandardHeaders();
    }

    /**
     * Perform the http rest request
     *
     * @return Response object
     * @throws UnirestException processing request error
     * @author Frank Giordano
     */
    public abstract Response executeRequest() throws UnirestException;

    /**
     * Set the body information for the http request
     *
     * @param body object value
     * @throws UnirestException error setting body
     * @author Frank Giordano
     */
    public abstract void setBody(Object body) throws UnirestException;

    /**
     * Set any headers needed for the http request
     *
     * @param headers headers to add to the request
     * @author Frank Giordano
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers.clear();
        this.setStandardHeaders();
        this.headers.putAll(headers);
    }

    /**
     * Set the standard headers for the http request
     *
     * @author Frank Giordano
     */
    public abstract void setStandardHeaders();

    /**
     * Set the url needed for the http request
     *
     * @param url rest url end point
     * @throws IllegalArgumentException error setting valid url string
     * @author Frank Giordano
     */
    public void setUrl(String url) throws IllegalArgumentException {
        ValidateUtils.checkNullParameter(url == null, "url is null");
        ValidateUtils.checkIllegalParameter(url.isBlank(), "url not specified");
        if (RestUtils.isUrlNotValid(url)) {
            throw new IllegalArgumentException("url is invalid");
        }
        this.url = url;
        LOG.debug(this.url);
    }

}
