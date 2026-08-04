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

import kong.unirest.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base abstract class that conforms to http CRUD operations
 *
 * @author Frank Giordano
 * @version 7.0
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
     * Per-connection Unirest client instances, keyed by ZosConnection (host/port/authType/credentials).
     * <p>
     * A dedicated UnirestInstance is spawned and configured (TLS/auth) once per unique connection so that
     * concurrent requests against different connections never race on JVM-global Unirest configuration.
     */
    private static final Map<ZosConnection, UnirestInstance> UNIREST_INSTANCES = new ConcurrentHashMap<>();
    /**
     * ZosConnection object
     */
    protected final ZosConnection connection;
    /**
     * Unirest client instance dedicated to this request's connection
     */
    protected final UnirestInstance unirest;
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
        this.unirest = (connection == null || connection.getAuthType() == null) ?
                null : UNIREST_INSTANCES.computeIfAbsent(connection, ZosmfRequest::buildUnirestInstance);
        this.initialize();
    }

    /**
     * Spawn and configure (TLS/auth) a new Unirest client instance dedicated to the given connection.
     * <p>
     * Invoked at most once per unique ZosConnection via {@link #UNIREST_INSTANCES}, so this mutates
     * only the newly spawned instance's own config, never the JVM-global Unirest.config() singleton.
     *
     * @param connection for connection information, see ZosConnection object
     * @return configured UnirestInstance for this connection
     * @author Frank Giordano
     */
    private static UnirestInstance buildUnirestInstance(final ZosConnection connection) {
        final UnirestInstance instance = Unirest.spawnInstance();
        instance.config().enableCookieManagement(false);
        switch (connection.getAuthType()) {
            case BASIC:
                LOG.debug("basic authentication type");
                break;
            case TOKEN:
                LOG.debug("token authentication type");
                break;
            case SSL:
                setupSsl(instance, connection);
                break;
            default:
                throw new IllegalStateException("no authentication type found");
        }
        return instance;
    }

    /**
     * Initialize the http request object's per-request state (headers/token) based on an authentication type
     *
     * @author Frank Giordano
     */
    private void initialize() {
        if (connection == null || connection.getAuthType() == null) {
            return;
        }
        this.headers.clear();
        this.setStandardHeaders();
        this.token = null;
        switch (connection.getAuthType()) {
            case BASIC:
                headers.put("Authorization", "Basic " + EncodeUtils.encodeBasicAuthCredentials(connection));
                break;
            case TOKEN:
                this.token = connection.getToken();
                break;
            case SSL:
                break;
            default:
                throw new IllegalStateException("no authentication type found");
        }
    }

    /**
     * Setup authentication SSL type
     * <p>
     * When system property "zowe.sdk.allow.insecure.connection" is set to "true", hostname
     * verification is disabled and self-signed certificate processing is used.
     * Otherwise, standard SSL client certificate store configuration is applied with default
     * JVM CA trust and hostname verification.
     *
     * @param instance   UnirestInstance to configure
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    private static void setupSsl(final UnirestInstance instance, final ZosConnection connection) {
        LOG.debug("ssl authentication type");
        boolean inSecure = Boolean.parseBoolean(System.getProperty(RestConstant.INSECURE_PROPERTY_NAME, "false"));
        if (inSecure) {
            LOG.warn(RestConstant.INSECURE_ENABLE_WARNING);
            setupSelfSignedCertificate(instance, connection.getCertFilePath(), connection.getCertPassword());
        } else {
            instance.config().clientCertificateStore(connection.getCertFilePath(), connection.getCertPassword());
        }
    }

    /**
     * Set up authentication SSL type for a self-signed certificate.
     * Disables hostname verification for connections where self-signed certificates or test hostnames are used.
     *
     * @param instance     UnirestInstance to configure
     * @param certFilePath certificate file (.p12) location
     * @param certPassword certificate password for certificate file (.p12)
     * @author Frank Giordano
     */
    private static void setupSelfSignedCertificate(final UnirestInstance instance,
                                                   final String certFilePath,
                                                   final String certPassword) {
        try {
            instance.config().disableHostNameVerification(true);

            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (FileInputStream fileInputStream = new FileInputStream(certFilePath)) {
                keyStore.load(fileInputStream, certPassword.toCharArray());
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }

            KeyManagerFactory keyManagerFactory =
                    KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, certPassword.toCharArray());

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(),
                    trustManagerFactory.getTrustManagers(), new java.security.SecureRandom());
            instance.config().sslContext(sslContext);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Build a Response object from a given HttpResponse reply
     *
     * @param reply HttpResponse object
     * @param <T>   either JsonNode, String, or byte[] type
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

        final String responsePhraseStr = Objects.toString(responsePhrase.get(), "");
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void setUrl(final String url) {
        ValidateUtils.checkIllegalParameter(url, "url");

        try {
            new URI(url).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new IllegalArgumentException("invalid url: " + url, e);
        }

        this.url = url;
        LOG.debug("url set to {}", this.url);
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
     * Get current http header value(s) for request
     *
     * @return map of header value(s)
     * @author Frank Giordano
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

}
