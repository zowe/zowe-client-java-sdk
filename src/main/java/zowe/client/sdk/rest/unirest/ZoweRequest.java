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

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.ZosmfHeaders;
import zowe.client.sdk.utility.RestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Base abstract class that conforms to Http CRUD operations
 *
 * @author Frank Giordano
 * @version 2.0
 */
public abstract class ZoweRequest {

    public static final String X_CSRF_ZOSMF_HEADER_KEY = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
    public static final String X_CSRF_ZOSMF_HEADER_VALUE = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);
    protected final ZOSConnection connection;
    protected final Map<String, String> headers = new HashMap<>();
    protected String url;

    /**
     * ZoweRequest constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public ZoweRequest(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Retrieve the http response information
     *
     * @param reply json http response object
     * @return response object
     * @author Frank Giordano
     */
    protected static Response getJsonResponse(HttpResponse<JsonNode> reply) {
        if (reply.getBody().isArray()) {
            return new Response(reply.getBody().getArray(), reply.getStatus(), reply.getStatusText());
        }
        return new Response(reply.getBody().getObject(), reply.getStatus(), reply.getStatusText());
    }

    /**
     * Perform the http rest request
     *
     * @author Frank Giordano
     */
    public abstract Response executeRequest() throws Exception;

    /**
     * Set the body information for the http request
     *
     * @author Frank Giordano
     */
    public abstract void setBody(String body) throws Exception;

    /**
     * Set any headers needed for the http request
     *
     * @param headers headers to add to the request
     * @author Frank Giordano
     */
    public void setHeaders(Map<String, String> headers) {
        headers.forEach(headers::put);
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
     * @throws Exception error setting the http request
     * @author Frank Giordano
     */
    public void setUrl(String url) throws Exception {
        if (RestUtils.isUrlNotValid(url)) {
            throw new Exception("url is invalid");
        }
        this.url = url;
    }

}
