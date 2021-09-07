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
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;

import java.util.Map;

/**
 * Base abstract class that conforms to Http CRUD operations
 *
 * @author Frank Giordano
 * @version 1.0
 */
public abstract class ZoweRequest {

    public static final String X_CSRF_ZOSMF_HEADER_KEY = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
    public static final String X_CSRF_ZOSMF_HEADER_VALUE = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);

    private final ZoweRequestType.VerbType requestType;
    protected final ZOSConnection connection;
    protected HttpClient client;
    protected HttpContext localContext = new BasicHttpContext();
    protected HttpResponse httpResponse;

    /**
     * ZoweRequest constructor.
     *
     * @param connection  connection information, see ZOSConnection object
     * @param requestType request type, see ZoweRequestType.VerbType object
     * @author Frank Giordano
     */
    public ZoweRequest(ZOSConnection connection, ZoweRequestType.VerbType requestType) {
        this.connection = connection;
        this.requestType = requestType;
    }

    /**
     * Execute the formulated http request
     *
     * @return Response value
     * @throws Exception error executing request
     * @author Frank Giordano
     */
    public abstract Response executeHttpRequest() throws Exception;

    /**
     * Set the standard headers for the http request
     *
     * @author Frank Giordano
     */
    public abstract void setStandardHeaders();

    /**
     * Set additional headers needed for the http request
     *
     * @param additionalHeaders additional headers to add to the request
     * @author Frank Giordano
     */
    public abstract void setAdditionalHeaders(Map<String, String> additionalHeaders);

    /**
     * Set the following incoming url with a new http request
     *
     * @param url rest url end point
     * @throws Exception error setting the http request
     * @author Frank Giordano
     */
    public abstract void setRequest(String url) throws Exception;

    /**
     * Setup to be used first in setting up the http request
     *
     * @author Frank Giordano
     */
    protected void setup() {
        setStandardHeaders();
        try {
            client = HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve current request type for http request
     *
     * @return http request type
     * @author Frank Giordano
     */
    public ZoweRequestType.VerbType requestType() {
        return requestType;
    }

}
