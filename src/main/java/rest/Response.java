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

import java.util.Optional;

/**
 * Holds Http response information
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class Response {

    /**
     * Holds Http response information
     */
    private Optional<Object> responsePhrase;

    /**
     * Holds Http response status code
     */
    private Optional<Integer> statusCode;

    public Response(Object responsePhrase, Integer statusCode) {
        this.responsePhrase = Optional.ofNullable(responsePhrase);
        this.statusCode = Optional.ofNullable(statusCode);
    }

    /**
     * Retrieve responsePhrase value
     *
     * @return responsePhrase value
     * @author Frank Giordano
     */
    public Optional<Object> getResponsePhrase() {
        return responsePhrase;
    }

    /**
     * Retrieve statusCode value
     *
     * @return status code value
     * @author Frank Giordano
     */
    public Optional<Integer> getStatusCode() {
        return statusCode;
    }

    /**
     * Does object contain all empty values
     *
     * @return boolean true if all values in object are empty
     * @author Frank Giordano
     */
    public boolean isEmpty() {
        return (responsePhrase.isEmpty() && statusCode.isEmpty());
    }

    @Override
    public String toString() {
        return "Response{" +
                "responsePhrase=" + responsePhrase +
                ", statusCode=" + statusCode +
                '}';
    }

}
