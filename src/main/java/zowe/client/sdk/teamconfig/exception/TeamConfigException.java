/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.exception;

/**
 * Custom exception to represent Zowe team configuration error state
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class TeamConfigException extends Exception {

    /**
     * TeamConfigException constructor for message value
     *
     * @param message error message
     * @author Frank Giordano
     */
    public TeamConfigException(final String message) {
        super(message);
    }

    /**
     * TeamConfigException constructor for message and throwable values
     *
     * @param message error message
     * @param err     original throwable exception
     * @author Frank Giordano
     */
    public TeamConfigException(final String message, Throwable err) {
        super(message, err);
    }

}
