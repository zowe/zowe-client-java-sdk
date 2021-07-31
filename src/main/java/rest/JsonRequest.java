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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.Util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JsonRequest implements IZoweRequest {

    private static final Logger LOG = LogManager.getLogger(JsonRequest.class);

    private ZOSConnection connection;
    private HttpGet getRequest;
    private HttpPut putRequest;
    private HttpPost postRequest;
    private HttpDelete deleteRequest;
    private Optional<String> body;
    private Map<String, String> headers = new HashMap<>();
    private HttpClient client;
    private final ResponseHandler<String> handler = new BasicResponseHandler();
    private HttpContext localContext = new BasicHttpContext();
    private HttpResponse httpResponse;

    public JsonRequest(ZOSConnection connection, HttpGet getRequest) {
        this.connection = connection;
        this.getRequest = getRequest;
        this.setup();
    }

    public JsonRequest(ZOSConnection connection, HttpPut putRequest, Optional<String> body) {
        this.connection = connection;
        this.putRequest = putRequest;
        this.body = body;
        this.setup();
    }

    public JsonRequest(ZOSConnection connection, HttpPost postRequest) {
        this.connection = connection;
        this.postRequest = postRequest;
        this.setup();
    }

    public JsonRequest(ZOSConnection connection, HttpDelete deleteRequest) {
        this.connection = connection;
        this.deleteRequest = deleteRequest;
        this.setup();
    }

    @Override
    public <T> T httpGet() throws Exception {
        if (!headers.isEmpty()) headers.forEach((key, value) -> getRequest.setHeader(key, value));

        this.httpResponse = client.execute(getRequest, localContext);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        boolean isHttpError = !(statusCode >= 200 && statusCode <= 299);
        if (isHttpError) {
            return (T) new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.ofNullable(statusCode));
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.info("JsonRequest::httpGet - result = {}", result);

            JSONParser parser = new JSONParser();
            try {
                return (T) new Response(Optional.ofNullable(parser.parse(result)), Optional.ofNullable(statusCode));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public <T> T httpPut() throws Exception {
        if (!headers.isEmpty()) headers.forEach((key, value) -> putRequest.setHeader(key, value));
        putRequest.setEntity(new StringEntity(body.orElse("")));

        this.httpResponse = client.execute(putRequest, localContext);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.info("JsonRequest::httpPost - Response statusCode {}, Response {}", httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        boolean isHttpError = !(statusCode >= 200 && statusCode <= 299);
        if (isHttpError) {
            return (T) new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.ofNullable(statusCode));
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.info("JsonRequest::httpPut - result = {}", result);

            JSONParser parser = new JSONParser();
            try {
                return (T) new Response(Optional.ofNullable(parser.parse(result)), Optional.ofNullable(statusCode));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public <T> T httpPost() throws Exception {
        String result = null;
        if (!headers.isEmpty()) headers.forEach((key, value) -> postRequest.setHeader(key, value));

        this.httpResponse = client.execute(postRequest, localContext);
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.info("Response statusCode {}, Response {}", httpResponse.getStatusLine().getStatusCode(),
                httpResponse.toString());

        boolean isHttpError = !(statusCode >= 200 && statusCode <= 299);
        if (isHttpError) {
            return (T) new Response(Optional.ofNullable(httpResponse.getStatusLine().getReasonPhrase()),
                    Optional.ofNullable(statusCode));
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
            LOG.info("Response result {}", result);
        }

        JSONParser parser = new JSONParser();
        try {
            return (T) new Response(Optional.ofNullable(parser.parse(result)), Optional.ofNullable(statusCode));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T httpDelete() throws Exception {
        if (!headers.isEmpty()) headers.forEach((key, value) -> deleteRequest.setHeader(key, value));
        String result = client.execute(deleteRequest, handler);
        LOG.info("JsonRequest::httpDelete - result = {}", result);

        JSONParser parser = new JSONParser();
        try {
            return (T) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public void setGetRequest(HttpGet getRequest) {
        this.getRequest = getRequest;
        this.setup();
    }

    @Override
    public void setPutRequest(HttpPut putRequest) {
        this.putRequest = putRequest;
        this.setup();
    }

    @Override
    public void setPostRequest(HttpPost postRequest) {
        this.postRequest = postRequest;
        this.setup();
    }

    @Override
    public void setDeleteRequest(HttpDelete deleteRequest) {
        this.deleteRequest = deleteRequest;
        this.setup();
    }

    private void setup() {
        this.setStandardHeaders();
        try {
            client = HttpClients
                    .custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setStandardHeaders() {
        String key = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
        String value = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);

        if (putRequest != null) {
            putRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            putRequest.setHeader("Content-Type", "application/json");
            putRequest.setHeader(key, value);
        } else if (getRequest != null) {
            getRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            getRequest.setHeader("Content-Type", "application/json");
            getRequest.setHeader(key, value);
        } else if (postRequest != null) {
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            postRequest.setHeader("Content-Type", "application/json");
            postRequest.setHeader(key, value);
        } else if (deleteRequest != null) {
            deleteRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            deleteRequest.setHeader("Content-Type", "application/json");
            deleteRequest.setHeader(key, value);
        }
    }

}
