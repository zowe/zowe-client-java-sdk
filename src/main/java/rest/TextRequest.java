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
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import utility.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TextRequest implements IZoweRequest {

    private ZOSConnection connection;
    private HttpGet getRequest;
    private HttpPut putRequest;
    private HttpPost postRequest;
    private HttpDelete deleteRequest;
    private Optional<String> body;
    private Map<String, String> headers = new HashMap<>();
    private HttpClient client;
    private final ResponseHandler<String> handler = new BasicResponseHandler();

    public TextRequest(ZOSConnection connection, HttpGet getRequest) {
        this.connection = connection;
        this.getRequest = getRequest;
        this.setup();
    }

    public TextRequest(ZOSConnection connection, HttpPut putRequest, Optional<String> body) {
        this.connection = connection;
        this.putRequest = putRequest;
        this.body = body;
        this.setup();
    }

    // this disables end user from calling default constructor
    private TextRequest() {}

    @Override
    public <T> T httpGet() throws IOException {
        if (!headers.isEmpty()) headers.forEach((key, value) -> getRequest.setHeader(key, value));
        return (T) client.execute(getRequest, handler);
    }

    @Override
    public <T> T httpPut() throws IOException {
        if (!headers.isEmpty()) headers.forEach((key, value) -> putRequest.setHeader(key, value));
        putRequest.setEntity(new StringEntity(body.orElse("")));

        return (T) client.execute(putRequest, handler);
    }

    @Override
    public <T> T httpPost() throws IOException {
        // TODO
        return null;
    }

    @Override
    public <T> T httpDelete() throws IOException {
        // TODO
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
            putRequest.setHeader("Content-Type", "text/plain; charset=UTF-8");
            putRequest.setHeader(key, value);
        }
        if (getRequest != null) {
            getRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            getRequest.setHeader("Content-Type", "text/plain; charset=UTF-8");
            getRequest.setHeader(key, value);
        }
        if (postRequest != null) {
            postRequest.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + Util.getAuthEncoding(connection));
            postRequest.setHeader("Content-Type", "text/plain; charset=UTF-8");
            postRequest.setHeader(key, value);
        }
    }

}
