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

import kong.unirest.HttpResponse;

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
    private final HttpResponse rawReply;

    /**
     * Response constructor
     *
     * @param responsePhrase http response information
     * @param statusCode     http response status code
     * @param statusText     http response status text
     * @param rawReply       raw http response
     * @author Frank Giordano
     */
    public Response(final Object responsePhrase, final Integer statusCode, final String statusText,
                    final HttpResponse rawReply) {
        this.responsePhrase = Optional.ofNullable(responsePhrase);
        if (statusCode == null) {
            this.statusCode = OptionalInt.empty();
        } else {
            this.statusCode = OptionalInt.of(statusCode);
        }
        this.statusText = Optional.ofNullable(statusText);
        this.rawReply = rawReply;
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
     * Retrieve rawReply value
     *
     * @return rawReply HttpResponse
     */
    public HttpResponse getRawReply() {
        return rawReply;
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
