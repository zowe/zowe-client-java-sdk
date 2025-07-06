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

import kong.unirest.core.Cookies;
import zowe.client.sdk.rest.Response;

/**
 * Holds Login response information
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosmfLoginResponse {

    /**
     * Holds Response object
     */
    private final Response response;

    /**
     * Holds tokens information
     */
    private final Cookies tokens;

    /**
     * ZosmfLoginResponse constructor
     *
     * @param response Response object
     * @param tokens  Cookies object representing TOKENS
     * @author Frank Giordano
     */
    public ZosmfLoginResponse(final Response response, final Cookies tokens) {
        this.response = response;
        this.tokens = tokens;
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
     * Retrieve cookies representing TOKENS
     *
     * @return cookies object
     */
    public Cookies getTokens() {
        return tokens;
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
                ", tokens=" + tokens +
                '}';
    }

}
