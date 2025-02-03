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
     * Holds cookies information
     */
    private final Optional<String> cookies;

    /**
     * LoginResponse constructor
     *
     * @param response Response object
     * @param cookies   Cookies object
     * @author Frank Giordano
     */
    public LoginResponse(final Response response, final Cookies cookies) {
        this.response = response;
        this.cookies = Optional.ofNullable(cookies.toString());
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
     * Retrieve cookies
     *
     * @return cookies Optional String value
     */
    public Optional<String> getCookies() {
        return cookies;
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
                ", cookies=" + cookies +
                '}';
    }

}
