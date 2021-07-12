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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.Util;
import zostso.StartTso;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonRequest implements IZoweRequest {

    private static final Logger LOG = LogManager.getLogger(StartTso.class);

    private ZOSConnection connection;
    private HttpGet getRequest;
    private HttpPut putRequest;
    private HttpPost postRequest;
    private HttpDelete deleteRequest;
    private String body;
    private Map<String, String> headers = new HashMap<>();
    private final HttpClient client = HttpClientBuilder.create().build();
    private final ResponseHandler<String> handler = new BasicResponseHandler();
    private HttpContext localContext = new BasicHttpContext();

    // disable end user from calling default constructor
    private JsonRequest() {
    }

    public JsonRequest(ZOSConnection connection, HttpGet getRequest) {
        this.connection = connection;
        this.getRequest = getRequest;
        this.setup();
    }

    public JsonRequest(ZOSConnection connection, HttpPut putRequest, String body) {
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
    public <T> T httpGet() throws IOException {
        if (!headers.isEmpty()) headers.forEach((key, value) -> getRequest.setHeader(key, value));
        String result = client.execute(getRequest, handler);
        LOG.info("JsonRequest::httpGet - result = {}", result);

        JSONParser parser = new JSONParser();
        try {
            return (T) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T httpPut() throws IOException {
        if (!headers.isEmpty()) headers.forEach((key, value) -> putRequest.setHeader(key, value));        
        putRequest.setEntity(new StringEntity(body));
        String result = client.execute(putRequest, handler);
        LOG.info("JsonRequest::httpPut - result = {}", result);

        JSONParser parser = new JSONParser();
        try {
            return (T) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T httpPost() throws Exception {
        String result = null;
        if (!headers.isEmpty()) headers.forEach((key, value) -> postRequest.setHeader(key, value));

        HttpResponse response = client.execute(postRequest, localContext);
        LOG.info("JsonRequest::httpPost - result = {}", response);

        int statusCode = response.getStatusLine().getStatusCode();

        LOG.info("Response statusLins {}, Response {}, Response content {}",
                response.getStatusLine().getStatusCode(), response.toString(), response.getEntity().getContent());

        if (statusCode != 200) {
            return (T) new Response(response.getStatusLine().getReasonPhrase(), statusCode);
        }

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            result = EntityUtils.toString(entity);
        }

        JSONParser parser = new JSONParser();
        try {
            return (T) new Response(parser.parse(result), statusCode);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public <T> T httpDelete() throws IOException {
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

    private void setup() {
        this.setStandardHeaders();
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
