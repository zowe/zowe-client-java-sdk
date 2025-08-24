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

import kong.unirest.core.Cookies;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Holds http response information
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class Response {

    /**
     * Holds http response information
     */
    private final Object responsePhrase;

    /**
     * Holds http response status code
     */
    private final Integer statusCode;

    /**
     * Holds http response status text
     */
    private final String statusText;

    /**
     * Store raw reply
     */
    private final Cookies tokens;

    /**
     * Response constructor
     *
     * @param responsePhrase http response information
     * @param statusCode     http response status code
     * @param statusText     http response status text
     * @author Frank Giordano
     */
    public Response(final Object responsePhrase, final Integer statusCode, final String statusText) {
        this.responsePhrase = responsePhrase;
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.tokens = null;
    }

    /**
     * Response constructor
     *
     * @param responsePhrase http response information
     * @param statusCode     http response status code
     * @param statusText     http response status text
     * @param tokens         http response Cookies object representing TOKENS
     * @author Frank Giordano
     */
    public Response(final Object responsePhrase, final Integer statusCode, final String statusText,
                    final Cookies tokens) {
        this.responsePhrase = responsePhrase;
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.tokens = tokens;
    }

    /**
     * Retrieve responsePhrase value
     *
     * @return responsePhrase Optional object value
     */
    public Optional<Object> getResponsePhrase() {
        return Optional.ofNullable(responsePhrase);
    }

    /**
     * Retrieve statusCode value
     *
     * @return statusCode Optional int value
     */
    public OptionalInt getStatusCode() {
        return (statusCode == null) ? OptionalInt.empty() :
                OptionalInt.of(statusCode);
    }

    /**
     * Retrieve statusText value
     *
     * @return statusText Optional String value
     */
    public Optional<String> getStatusText() {
        return Optional.ofNullable(statusText);
    }

    /**
     * Retrieve tokens value
     *
     * @return tokens Cookies object representing TOKENS
     */
    public Cookies getTokens() {
        return tokens;
    }

    /**
     * Return string value representing Response object
     *
     * @return string representation of Response
     */
    @Override
    public String toString() {
        return "Response{" +
                "responsePhrase=" + responsePhrase +
                ", statusCode=" + statusCode +
                ", statusText=" + statusText +
                '}';
    }

}
