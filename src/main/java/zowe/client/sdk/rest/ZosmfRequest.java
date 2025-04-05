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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Base abstract class that conforms to http CRUD operations
 *
 * @author Frank Giordano
 * @version 3.0
 */
public abstract class ZosmfRequest {

    private static final Logger LOG = LoggerFactory.getLogger(ZosmfRequest.class);
    public static final String X_CSRF_ZOSMF_HEADER_KEY = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(0);
    public static final String X_CSRF_ZOSMF_HEADER_VALUE = ZosmfHeaders.HEADERS.get(ZosmfHeaders.X_CSRF_ZOSMF_HEADER).get(1);
    protected final ZosConnection connection;
    protected final Map<String, String> headers = new HashMap<>();
    protected String url;
    protected Cookie cookie;

    /**
     * ZosmfRequest constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosmfRequest(final ZosConnection connection) {
        this.connection = connection;
        this.setup();
    }

    /**
     * Setup to be used first in setting up the http request
     *
     * @author Frank Giordano
     */
    private void setup() {
        Unirest.config().reset();
        Unirest.config().enableCookieManagement(false);
        Unirest.config().verifySsl(false);
        this.setStandardHeaders();
    }

    /**
     * Build Response object from given HttpResponse reply
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
        this.setStandardHeaders();
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
        ValidateUtils.checkNullParameter(url == null, "url is null");
        ValidateUtils.checkIllegalParameter(url.isBlank(), "url not specified");
        if (isUrlNotValid(url)) {
            throw new IllegalArgumentException("url is invalid");
        }
        this.url = url;
        LOG.debug(this.url);
    }

    /**
     * Check if url is a valid http(s) url.
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
     * Set a cookie token for this request. This is optional for most requests and not needed.
     * Setting the cookie with a non-null value will remove the HTTP Authorization request header.
     * <p>
     * Setting the cookie value as null afterward will revert/enable HTTP Authorization request header
     * for future requests.
     *
     * @param cookie object
     * @author Frank Giordano
     */
    public void setCookie(final Cookie cookie) {
        this.cookie = cookie;
        this.toggleAuthenticationType();
    }

    /**
     * Determine authentication type (basic or token authorization) and switch accordingly.
     *
     * @author Frank Giordano
     */
    private void toggleAuthenticationType() {
        if (this.cookie == null) {
            LOG.debug("enable basic authorization");
            headers.put("Authorization", "Basic " +
                    EncodeUtils.encodeAuthComponent(connection));
        } else {
            LOG.debug("disable basic authorization");
            headers.remove("Authorization");
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
