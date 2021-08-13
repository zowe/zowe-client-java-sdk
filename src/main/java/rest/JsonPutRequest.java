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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonPutRequest extends ZoweRequest {

    private static final Logger LOG = LogManager.getLogger(JsonPutRequest.class);

    private HttpPut request;
    private final String body;
    private Map<String, String> additionalHeaders = new HashMap<>();

    public JsonPutRequest(ZOSConnection connection, String url, String body) throws Exception {
        super(connection, ZoweRequestType.RequestType.PUT_JSON);
        this.body = body;
        this.request = new HttpPut(Optional.ofNullable(url).orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

    @Override
    public Response executeHttpRequest() throws Exception {
        // add any additional headers...
        additionalHeaders.forEach((key, value) -> request.setHeader(key, value));
        request.setEntity(new StringEntity(Optional.ofNullable(body).orElse("")));

        try {
            this.httpResponse = client.execute(request, localContext);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.debug("JsonPutRequest::httpPut - Response statusCode {}, Response {}",
                httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        if (Util.isHttpError(statusCode)) {
            return new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.ofNullable(statusCode));
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("JsonPutRequest::httpPut - result = {}", result);

            JSONParser parser = new JSONParser();
            try {
                if (result.isEmpty()) {
                    return new Response(Optional.empty(), Optional.ofNullable(statusCode));
                } else {
                    return new Response(Optional.ofNullable(parser.parse(result)), Optional.ofNullable(statusCode));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
        this.request = new HttpPut(Optional.ofNullable(url).orElseThrow(() -> new Exception("url not specified")));
        this.setup();
    }

}
