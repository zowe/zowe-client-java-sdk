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

import kong.unirest.core.Cookie;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base abstract class that conforms to http CRUD operations
 *
 * @author Frank Giordano
 * @version 4.0
 */
public abstract class ZosmfRequest {

    private static final Logger LOG = LoggerFactory.getLogger(ZosmfRequest.class);
    /**
     * X_CSRF_ZOSMF_HEADER_KEY header info
     */
    public static final String X_CSRF_ZOSMF_HEADER_KEY = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
    /**
     * X_CSRF_ZOSMF_HEADER_VALUE header info
     */
    public static final String X_CSRF_ZOSMF_HEADER_VALUE = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);
    /**
     * ZosConnection object
     */
    protected final ZosConnection connection;
    /**
     * Map of HTTP headers
     */
    protected final Map<String, String> headers = new HashMap<>();
    /**
     * URL string
     */
    protected String url;
    /**
     * Cookie object representing a TOKEN
     */
    protected Cookie token;

    /**
     * ZosmfRequest constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosmfRequest(final ZosConnection connection) {
        this.connection = connection;
        this.initialize();
    }

    /**
     * Initialize the unirest http request object based on an authentication type
     *
     * @author Frank Giordano
     */
    private void initialize() {
        Unirest.config().reset();
        Unirest.config().enableCookieManagement(false);
        this.setStandardHeaders();
        this.token = null;
        switch (connection.getAuthType()) {
            case BASIC:
                setupBasic();
                break;
            case TOKEN:
                setupToken();
                break;
            case SSL:
                setupSsl();
                break;
            default:
                throw new IllegalStateException("no authentication type found");
        }
    }

    /**
     * Setup authentication BASIC type
     *
     * @author Frank Giordano
     */
    private void setupBasic() {
        LOG.debug("basic authentication type");
        Unirest.config().verifySsl(false);
        headers.put("Authorization", "Basic " + EncodeUtils.encodeAuthComponent(connection));
    }

    /**
     * Setup authentication TOKEN type
     *
     * @author Frank Giordano
     */
    private void setupToken() {
        LOG.debug("token authentication type");
        Unirest.config().verifySsl(false);
        this.token = connection.getToken();
    }

    /**
     * Setup authentication SSL type
     * <p>
     * With the following system property set "zowe.sdk.allow.insecure.connection",
     * insecure type for self-signed certificate processing is enabled.
     *
     * @author Frank Giordano
     */
    private void setupSsl() {
        LOG.debug("ssl authentication type");
        boolean inSecure = Boolean.parseBoolean(System.getProperty(RestConstant.INSECURE_PROPERTY_NAME, "false"));
        if (inSecure) {
            LOG.debug("insecure enabled");
            setupSelfSignedCertificate(connection.getCertFilePath(), connection.getCertPassword());
        } else {
            Unirest.config().clientCertificateStore(connection.getCertFilePath(), connection.getCertPassword());
        }
    }

    /**
     * Setup authentication SSL type for self-signed certificate
     *
     * @param certFilePath certificate file (.p12) location
     * @param certPassword certificate password for certificate file (.p12)
     * @author Frank Giordano
     */
    private void setupSelfSignedCertificate(String certFilePath, String certPassword) {
        try {
            System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fileInputStream = new FileInputStream(certFilePath)) {
                keyStore.load(fileInputStream, certPassword.toCharArray());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }

            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, certPassword.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(),
                    RestConstant.TRUST_ALL_CERTS, new java.security.SecureRandom());
            Unirest.config().sslContext(sslContext);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Build a Response object from a given HttpResponse reply
     *
     * @param reply HttpResponse object
     * @param <T>   either JsonNode, String or byte[] type
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    public <T> Response buildResponse(HttpResponse<T> reply) throws ZosmfRequestException {
        final int statusCode = reply.getStatus();
        if (statusCode == 0) {
            throw new IllegalStateException("zero number status code return");
        }

        String statusText;
        if (reply.getStatusText() == null || reply.getStatusText().isBlank()) {
            statusText = RestConstant.HTTP_STATUS.get(reply.getStatus());
        } else {
            statusText = reply.getStatusText();
        }
        if (statusText == null || statusText.isBlank()) {
            statusText = "n\\a";
        }

        Response response;
        if (statusText.toLowerCase().contains("no content")) {
            response = reply.getCookies() != null ?
                    new Response(statusText, statusCode, statusText, reply.getCookies()) :
                    new Response(statusText, statusCode, statusText);
        } else if (reply.getBody() instanceof JsonNode) {
            final HttpResponse<JsonNode> jsonReply = (HttpResponse<JsonNode>) reply;
            if (reply.getCookies() != null) {
                response = jsonReply.getBody().isArray() ?
                        new Response(jsonReply.getBody().getArray(), statusCode, statusText, reply.getCookies()) :
                        new Response(jsonReply.getBody().getObject(), statusCode, statusText, reply.getCookies());
            } else {
                response = jsonReply.getBody().isArray() ?
                        new Response(jsonReply.getBody().getArray(), statusCode, statusText) :
                        new Response(jsonReply.getBody().getObject(), statusCode, statusText);
            }
        } else if (reply.getBody() instanceof String) {
            final HttpResponse<String> stringReply = (HttpResponse<String>) reply;
            response = reply.getCookies() != null ?
                    new Response(stringReply.getBody(), statusCode, statusText, reply.getCookies()) :
                    new Response(stringReply.getBody(), statusCode, statusText);
        } else if (reply.getBody() instanceof byte[]) {
            final HttpResponse<byte[]> byteReply = (HttpResponse<byte[]>) reply;
            response = reply.getCookies() != null ?
                    new Response(byteReply.getBody(), statusCode, statusText, reply.getCookies()) :
                    new Response(byteReply.getBody(), statusCode, statusText);
        } else if (reply.getParsingError().isPresent()) {
            final HttpResponse<JsonNode> jsonReply = (HttpResponse<JsonNode>) reply;
            final String errMsg = jsonReply.getParsingError().get().getMessage();
            final String originalBody = jsonReply.getParsingError().get().getOriginalBody();
            LOG.debug("Unirest parsing error: {} {}", errMsg, originalBody);
            response = reply.getCookies() != null ?
                    new Response(originalBody, statusCode, statusText, reply.getCookies()) :
                    new Response(originalBody, statusCode, statusText);
        } else {
            LOG.debug("no reply instanceof found");
            response = reply.getCookies() != null ?
                    new Response(null, statusCode, statusText, reply.getCookies()) :
                    new Response(null, statusCode, statusText);
        }

        if (!(statusCode >= 100 && statusCode <= 299)) {
            throw new ZosmfRequestException(httpErrorMsg(response, statusCode), response);
        }

        return response;
    }

    /**
     * Return custom http error message
     *
     * @param response   Response object
     * @param statusCode http status code
     * @return String error message
     * @author Frank Giordano
     */
    private String httpErrorMsg(final Response response, final int statusCode) {
        final AtomicReference<Object> responsePhrase = new AtomicReference<>();
        response.getResponsePhrase().ifPresent(responsePhrase::set);
        if (responsePhrase.get() instanceof byte[] && ((byte[]) responsePhrase.get()).length > 0) {
            try (final InputStreamReader inputStreamReader = new InputStreamReader(
                    new ByteArrayInputStream((byte[]) responsePhrase.get()), StandardCharsets.UTF_8)) {
                try (final BufferedReader br = new BufferedReader(inputStreamReader)) {
                    final StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    responsePhrase.set(content.substring(0, content.length() - 1));
                }
            } catch (IOException e) {
                return e.getMessage();
            }
        }

        final String responsePhraseStr = String.valueOf((responsePhrase.get()));
        final String statusText = response.getStatusText().orElse("n\\a");
        String httpErrMsg = "http status error code: " + statusCode + ", status text: " + statusText;
        if (!statusText.equalsIgnoreCase(responsePhraseStr)) {
            httpErrMsg += ", response phrase: " + responsePhraseStr;
        }
        return httpErrMsg;
    }

    /**
     * Perform the http rest request
     *
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public abstract Response executeRequest() throws ZosmfRequestException;

    /**
     * Set the body information for the http request
     *
     * @param body object value
     * @author Frank Giordano
     */
    public abstract void setBody(final Object body);

    /**
     * Set any headers needed for the http request
     *
     * @param headers headers to add to the request
     * @author Frank Giordano
     */
    public void setHeaders(final Map<String, String> headers) {
        this.headers.clear();
        this.initialize();
        this.headers.putAll(headers);
    }

    /**
     * Set the standard headers for the http request
     *
     * @author Frank Giordano
     */
    public abstract void setStandardHeaders();

    /**
     * Set the url needed for the http request
     *
     * @param url rest url end point
     * @throws IllegalArgumentException error setting valid url string
     * @author Frank Giordano
     */
    public void setUrl(final String url) throws IllegalArgumentException {
        ValidateUtils.checkIllegalParameter(url, "url");
        if (isUrlNotValid(url)) {
            throw new IllegalArgumentException("url is invalid");
        }
        this.url = url;
        LOG.debug(this.url);
    }

    /**
     * Retrieve the url string value
     *
     * @return string value
     */
    public String getUrl() {
        return url;
    }

    /**
     * Check if the url is a valid http(s) url
     *
     * @param url string value
     * @return boolean true or false
     * @author Frank Giordano
     */
    private static boolean isUrlNotValid(final String url) {
        try {
            new URL(url).toURI();
            return false;
        } catch (URISyntaxException | MalformedURLException exception) {
            return true;
        }
    }

    /**
     * Get current http header value(s) for request
     *
     * @return map of header value(s)
     * @author Frank Giordano
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

}
