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
 * @version 2.0
 */
public class ZosmfLoginResponse {

    /**
     * Holds Response object
     */
    private final Response response;

    /**
     * Holds cookies information
     */
    private final Cookies cookies;

    /**
     * LoginResponse constructor
     *
     * @param response Response object
     * @param cookies  Cookies object
     * @author Frank Giordano
     */
    public ZosmfLoginResponse(final Response response, final Cookies cookies) {
        this.response = response;
        this.cookies = cookies;
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
     * @return cookies object
     */
    public Cookies getCookies() {
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
