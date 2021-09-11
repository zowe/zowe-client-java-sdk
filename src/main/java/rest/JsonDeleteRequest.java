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
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpDelete;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Util;
import utility.UtilRest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Http delete operation with Json content type
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class JsonDeleteRequest extends ZoweRequest {

    private static final Logger LOG = LogManager.getLogger(JsonDeleteRequest.class);

    private HttpDelete request;
    private Map<String, String> additionalHeaders = new HashMap<>();

    /**
     * JsonDeleteRequest constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @param url        rest url value
     * @throws Exception error setting constructor variables
     * @author Frank Giordano
     */
    public JsonDeleteRequest(ZOSConnection connection, String url) throws Exception {
        super(connection, ZoweRequestType.VerbType.DELETE_JSON);
        this.request = new HttpDelete(Optional.ofNullable(url).orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

    /**
     * Execute the formulated http request
     *
     * @author Frank Giordano
     */
    @Override
    public Response executeHttpRequest() throws Exception {
        // add any additional headers...
        additionalHeaders.forEach((key, value) -> request.setHeader(key, value));
        try {
            this.httpResponse = client.execute(request, localContext);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(null, null);
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.debug("JsonDeleteRequest::httpDelete - Response statusCode {}, Response {}",
                httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        if (UtilRest.isHttpError(statusCode)) {
            return new Response(httpResponse.getStatusLine().getReasonPhrase(), statusCode);
        }

        return new Response(UtilRest.getJsonResponseEntity(httpResponse), statusCode);
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
     * Set additional headers needed for the http request
     *
     * @param additionalHeaders additional headers to add to the request
     * @author Frank Giordano
     */
    @Override
    public void setAdditionalHeaders(Map<String, String> additionalHeaders) {
        this.additionalHeaders = additionalHeaders;
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
        this.request = new HttpDelete(Optional.ofNullable(url).orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

}
