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

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.utility.Util;
import zowe.client.sdk.utility.UtilRest;

import java.util.Map;
import java.util.Optional;

/**
 * Http get stream operation with Json content type
 *
 * @version 1.0
 */
public class StreamGetRequest extends ZoweRequest {

    private static final Logger LOG = LoggerFactory.getLogger(StreamGetRequest.class);

    private HttpGet request;

    /**
     * StreamGetRequest constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @param url        rest url value
     * @throws Exception error setting constructor variables
     */
    public StreamGetRequest(ZOSConnection connection, String url) throws Exception {
        super(connection, ZoweRequestType.VerbType.GET_STREAM);
        if (!UtilRest.isUrlValid(url)) {
            throw new Exception("url is invalid");
        }
        request = new HttpGet(url);
        setup();
    }

    /**
     * Execute the formulated http request
     *
     * @author Frank Giordano
     */
    @Override
    public Response executeRequest() throws Exception {
        LOG.debug("StreamGetRequest::executeRequest");
        return executeStreamRequest(request);
    }

    /**
     * Set the standard headers for the http request
     *
     * @author Frank Giordano
     */
    @Override
    public void setStandardHeaders() {
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
        request.setHeader("Content-Type", "application/json");
        request.setHeader(X_CSRF_ZOSMF_HEADER_KEY, X_CSRF_ZOSMF_HEADER_VALUE);
    }

    /**
     * Set any headers needed for the http request
     *
     * @param headers headers to add to the request
     * @author Frank Giordano
     */
    @Override
    public void setHeaders(Map<String, String> headers) {
        headers.forEach((key, value) -> request.setHeader(key, value));
    }

    /**
     * Set the following incoming url with a new http request
     *
     * @param url rest url end point
     * @throws Exception error setting the http request
     * @author Frank Giordano
     */
    @Override
    public void setRequest(String url) throws Exception {
        request = new HttpGet(Optional.ofNullable(url).orElseThrow(() -> new Exception("url not specified")));
        setup();
    }

}
