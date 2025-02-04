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
 * @version 2.0
 */
public class Response {

    /**
     * Holds http response information
     */
    private final Optional<Object> responsePhrase;

    /**
     * Holds http response status code
     */
    private final OptionalInt statusCode;

    /**
     * Holds http response status text
     */
    private final Optional<String> statusText;

    /**
     * Store raw reply
     */
    private final Optional<Cookies> cookies;

    /**
     * Response constructor
     *
     * @param responsePhrase http response information
     * @param statusCode     http response status code
     * @param statusText     http response status text
     * @author Frank Giordano
     */
    public Response(final Object responsePhrase, final Integer statusCode, final String statusText) {
        this.responsePhrase = Optional.ofNullable(responsePhrase);
        if (statusCode == null) {
            this.statusCode = OptionalInt.empty();
        } else {
            this.statusCode = OptionalInt.of(statusCode);
        }
        this.statusText = Optional.ofNullable(statusText);
        this.cookies = Optional.empty();
    }

    /**
     * Response constructor
     *
     * @param responsePhrase http response information
     * @param statusCode     http response status code
     * @param statusText     http response status text
     * @param cookies        http response cookies
     * @author Frank Giordano
     */
    public Response(final Object responsePhrase, final Integer statusCode, final String statusText,
                    final Cookies cookies) {
        this.responsePhrase = Optional.ofNullable(responsePhrase);
        if (statusCode == null) {
            this.statusCode = OptionalInt.empty();
        } else {
            this.statusCode = OptionalInt.of(statusCode);
        }
        this.statusText = Optional.ofNullable(statusText);
        this.cookies = Optional.ofNullable(cookies);
    }

    /**
     * Retrieve responsePhrase value
     *
     * @return responsePhrase Optional object value
     */
    public Optional<Object> getResponsePhrase() {
        return responsePhrase;
    }

    /**
     * Retrieve statusCode value
     *
     * @return statusCode Optional int value
     */
    public OptionalInt getStatusCode() {
        return statusCode;
    }

    /**
     * Retrieve statusText value
     *
     * @return statusText Optional String value
     */
    public Optional<String> getStatusText() {
        return statusText;
    }

    /**
     * Retrieve cookies value
     *
     * @return cookies Cookies object
     */
    public Cookies getCookies() {
        return cookies.orElse(null);
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
