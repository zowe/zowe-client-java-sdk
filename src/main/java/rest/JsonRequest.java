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
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utility.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonRequest implements IZoweRequest {

    private ZOSConnection connection;
    private HttpGet getRequest;
    private HttpPut putRequest;
    private String body;
    private Map<String, String> headers = new HashMap<>();
    private final HttpClient client = HttpClientBuilder.create().build();
    private final ResponseHandler<String> handler = new BasicResponseHandler();

    public JsonRequest(ZOSConnection connection, HttpGet getRequest) {
        this.connection = connection;
        this.getRequest = getRequest;
    }

    public JsonRequest(ZOSConnection connection, HttpPut putRequest, String body) {
        this.connection = connection;
        this.putRequest = putRequest;
        this.body = body;
    }

    @Override
    public <T> T httpGet() throws IOException {
        this.setStandardHeaders();
        if (!headers.isEmpty()) headers.entrySet().stream().forEach(e -> getRequest.setHeader(e.getKey(), e.getValue()));

        String result = client.execute(getRequest, handler);

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
        this.setStandardHeaders();
        if (!headers.isEmpty()) headers.entrySet().stream().forEach(e -> putRequest.setHeader(e.getKey(), e.getValue()));
        putRequest.setEntity(new StringEntity(body));

        String result = client.execute(putRequest, handler);

        JSONParser parser = new JSONParser();
        try {
            return (T) parser.parse(result);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setStandardHeaders() {
        String key = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
        String value = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);

        if (putRequest != null) {
            putRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            putRequest.setHeader("Content-Type", "application/json");
            putRequest.setHeader(key, value);
        }
        if (getRequest != null) {
            getRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            getRequest.setHeader("Content-Type", "application/json");
            getRequest.setHeader(key, value);
        }
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

}
