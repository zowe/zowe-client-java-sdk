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
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Util;
import utility.UtilRest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StreamGetRequest extends ZoweRequest {

    private static final Logger LOG = LogManager.getLogger(StreamGetRequest.class);

    private HttpGet request;
    private Map<String, String> additionalHeaders = new HashMap<>();

    public StreamGetRequest(ZOSConnection connection, String url) throws Exception {
        super(connection, ZoweRequestType.VerbType.GET_STREAM);
        this.request = new HttpGet(Optional.ofNullable(url).orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

    @Override
    public Response executeHttpRequest() throws IOException {
        // add any additional headers...
        additionalHeaders.forEach((key, value) -> request.setHeader(key, value));

        try {
            this.httpResponse = client.execute(request, localContext);
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(Optional.empty(), Optional.empty());
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.debug("StreamGetRequest::httpGet - Response statusCode {}, Response {}",
                httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        if (UtilRest.isHttpError(statusCode)) {
            return new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.of(statusCode));
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            return new Response(Optional.ofNullable(entity.getContent()), Optional.of(statusCode));
        }

        return new Response(Optional.empty(), Optional.of(statusCode));
    }

    @Override
    public void setStandardHeaders() {
        request.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
        request.setHeader("Content-Type", "application/json");
        request.setHeader(X_CSRF_ZOSMF_HEADER_KEY, X_CSRF_ZOSMF_HEADER_VALUE);
    }

    @Override
    public void setAdditionalHeaders(Map<String, String> additionalHeaders) {
        this.additionalHeaders = additionalHeaders;
    }

    @Override
    public void setRequest(String url) throws Exception {
        Optional<String> str = Optional.ofNullable(url);
        this.request = new HttpGet(str.orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

}
