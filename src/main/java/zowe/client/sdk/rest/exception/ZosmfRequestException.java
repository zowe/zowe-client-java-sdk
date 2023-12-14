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

/**
 * Custom exception to represent z/OSMF http error state
 *
 * @author Frank Giordano
 */
public class ZosmfRequestException extends Exception {

    /**
     * ZosmfRequestException constructor
     *
     * @param message error message
     */
    public ZosmfRequestException(String message) {
        super(message);
    }

}
