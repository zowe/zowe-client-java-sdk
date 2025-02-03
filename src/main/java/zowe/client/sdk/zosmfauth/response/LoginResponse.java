/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfauth.response;

import kong.unirest.Cookies;
import zowe.client.sdk.rest.Response;

import java.util.Optional;

/**
 * Holds Login response information
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class LoginResponse {

    /**
     * Holds Response object
     */
    private final Response response;

    /**
     * Holds cookie information
     */
    private final Optional<String> cookie;

    /**
     * LoginResponse constructor
     *
     * @param response Response object
     * @param cookie   Cookies object
     * @author Frank Giordano
     */
    public LoginResponse(final Response response, final Cookies cookie) {
        this.response = response;
        this.cookie = Optional.ofNullable(cookie.toString());
    }

    /**
     * Retrieve response
     *
     * @return response object
     */
    public Response getResponse() {
        return response;
    }

    /**
     * Retrieve cookie
     *
     * @return cookie Optional String value
     */
    public Optional<String> getCookie() {
        return cookie;
    }

    /**
     * Return string value representing LoginResponse object
     *
     * @return string representation of LoginResponse
     */
    @Override
    public String toString() {
        return "LoginResponse{" +
                "response=" + response +
                ", cookie=" + cookie +
                '}';
    }

}
