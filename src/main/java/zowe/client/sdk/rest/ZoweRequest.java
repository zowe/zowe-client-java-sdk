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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;

import java.util.Map;

/**
 * Base abstract class that conforms to Http CRUD operations
 *
 * @author Frank Giordano
 * @version 1.0
 */
public abstract class ZoweRequest {

    private static final Logger LOG = LoggerFactory.getLogger(ZoweRequest.class);
    public static final String X_CSRF_ZOSMF_HEADER_KEY = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
    public static final String X_CSRF_ZOSMF_HEADER_VALUE = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);
    private final ZoweRequestType requestType;
    protected final ZOSConnection connection;
    protected HttpClient client;
    protected final HttpContext localContext = new BasicHttpContext();
    protected HttpResponse httpResponse;

    /**
     * ZoweRequest constructor.
     *
     * @param connection  connection information, see ZOSConnection object
     * @param requestType request type, see ZoweRequestType.VerbType object
     * @author Frank Giordano
     */
    public ZoweRequest(ZOSConnection connection, ZoweRequestType requestType) {
        this.connection = connection;
        this.requestType = requestType;
    }

    /**
     * Abstraction method. Execute the formulated http request
     *
     * @return Response value
     * @throws Exception error executing request
     * @author Frank Giordano
     */
    public abstract Response executeRequest() throws Exception;

    /**
     * Abstraction method. Set standard headers for the http request
     *
     * @author Frank Giordano
     */
    public abstract void setStandardHeaders();

    /**
     * Abstraction method. Set any headers needed for the http request
     *
     * @param headers headers to add to the request
     * @author Frank Giordano
     */
    public abstract void setHeaders(Map<String, String> headers);

    /**
     * Abstraction method. Initialize the http request object with an url value
     *
     * @param url rest url end point
     * @throws Exception error setting the http request
     * @author Frank Giordano
     */
    public abstract void setRequest(String url) throws Exception;

    /**
     * Abstraction method. Initialize the http request object with an url and body values
     *
     * @param url  rest url end point
     * @param body data to be sent with request
     * @throws Exception error setting the http request
     * @author Frank Giordano
     */
    public abstract void setRequest(String url, String body) throws Exception;

    /**
     * Setup to be used first in setting up the http request
     *
     * @author Frank Giordano
     */
    protected void setup() {
        setStandardHeaders();
        try {
            client = HttpClients.custom()
                    .setSSLContext(
                            new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute a Zowe rest call request and return a Json object
     *
     * @param request http verb request
     * @param <T>     http verb type
     * @return response object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    protected <T> Response executeJsonRequest(T request) throws Exception {
        int statusCode = execute(request);

        if (RestUtils.isHttpError(statusCode)) {
            return new Response(httpResponse.getStatusLine().getReasonPhrase(), statusCode);
        }

        return new Response(RestUtils.getJsonResponseEntity(httpResponse), statusCode);
    }

    /**
     * Execute a Zowe rest call request and return a text object
     *
     * @param request http verb request
     * @param <T>     http verb type
     * @return response object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    protected <T> Response executeTextRequest(T request) throws Exception {
        int statusCode = execute(request);

        if (RestUtils.isHttpError(statusCode)) {
            return new Response(httpResponse.getStatusLine().getReasonPhrase(), statusCode);
        }

        return new Response(RestUtils.getTextResponseEntity(httpResponse), statusCode);
    }

    /**
     * Execute a Zowe rest call request and return a stream object
     *
     * @param request http verb request
     * @param <T>     http verb type
     * @return response object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    protected <T> Response executeStreamRequest(T request) throws Exception {
        int statusCode = execute(request);

        if (RestUtils.isHttpError(statusCode)) {
            return new Response(httpResponse.getStatusLine().getReasonPhrase(), statusCode);
        }

        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            return new Response(entity.getContent(), statusCode);
        }

        return new Response(null, statusCode);
    }

    /**
     * Execute a Zowe rest call request
     *
     * @param request http verb request
     * @return status code of the http request
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    private <T> int execute(T request) throws Exception {
        ValidateUtils.checkNullParameter(request == null, "request is null");
        ValidateUtils.checkNullParameter(client == null, "client is null");

        httpResponse = client.execute((HttpUriRequest) request, localContext);

        int statusCode = httpResponse.getStatusLine().getStatusCode();

        LOG.debug("ZoweRequest::execute - Response statusCode {}, Response {}",
                httpResponse.getStatusLine().getStatusCode(), httpResponse.toString());

        return statusCode;
    }

    /**
     * Retrieve current request http type
     *
     * @return http request type
     * @author Frank Giordano
     */
    public ZoweRequestType requestType() {
        return requestType;
    }

}
