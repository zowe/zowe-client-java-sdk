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

import java.util.Optional;

/**
 * Custom exception to represent z/OSMF request error state
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfRequestException extends Exception {

    private Optional<Response> response = Optional.empty();

    /**
     * ZosmfRequestException constructor for message value
     *
     * @param message  error message
     */
    public ZosmfRequestException(final String message) {
        super(message);
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
        this.response = Optional.ofNullable(response);
    }

    /**
     * Getter for response object
     *
     * @return Response optional object
     */
    public Optional<Response> getResponse() {
        return response;
    }

}
