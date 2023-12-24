/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest.exception;

import zowe.client.sdk.rest.Response;

/**
 * Custom exception to represent z/OSMF request error state
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfRequestException extends Exception {

    private Response response;

    /**
     * ZosmfRequestException constructor for message value
     *
     * @param message error message
     * @author Frank Giordano
     */
    public ZosmfRequestException(final String message) {
        super(message);
    }

    /**
     * ZosmfRequestException constructor for message and throwable values
     *
     * @param message error message
     * @param err     original throwable exception
     * @author Frank Giordano
     */
    public ZosmfRequestException(final String message, Throwable err) {
        super(message, err);
    }

    /**
     * ZosmfRequestException constructor for message and response values
     *
     * @param message  error message
     * @param response Response object
     * @author Frank Giordano
     */
    public ZosmfRequestException(final String message, final Response response) {
        super(message);
        this.response = response;
    }

    /**
     * ZosmfRequestException constructor for message, response and throwable values
     *
     * @param message  error message
     * @param response Response object
     * @param err      original throwable exception
     * @author Frank Giordano
     */
    public ZosmfRequestException(final String message, final Response response, Throwable err) {
        super(message, err);
        this.response = response;
    }

    /**
     * Getter for response object
     *
     * @return Response object
     */
    public Response getResponse() {
        return response;
    }

}
