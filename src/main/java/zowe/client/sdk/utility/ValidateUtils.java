/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import zowe.client.sdk.core.ZosConnection;

import java.util.Optional;

/**
 * Utility Class contains helper methods for validation type processing
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class ValidateUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ValidateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check connection for validity.
     *
     * @param connection connection information, see ZosConnection object
     * @throws IllegalStateException with message "Connection data not setup properly"
     * @author Frank Giordano
     */
    public static void checkConnection(ZosConnection connection) {
        if (connection == null || connection.getZosmfPort() == null || connection.getHost() == null ||
                connection.getPassword() == null || connection.getUser() == null ||
                connection.getZosmfPort().trim().isEmpty() || connection.getHost().trim().isEmpty() ||
                connection.getPassword().trim().isEmpty() || connection.getUser().trim().isEmpty()) {
            throw new IllegalStateException("Connection data not setup properly");
        }
    }

    /**
     * Check for state of parameter
     *
     * @param check Check for true or false value
     * @param msg   message to display if check is true
     * @throws IllegalArgumentException with message
     * @author Frank Giordano
     */
    public static void checkIllegalParameter(boolean check, String msg) {
        Optional<String> message = Optional.ofNullable(msg);
        if (check) {
            throw new IllegalArgumentException(message.orElse("empty message specified"));
        }
    }

    /**
     * Check for null parameter
     *
     * @param check check for true or false value
     * @param msg   message to display if check is true
     * @throws IllegalArgumentException with message
     * @author Frank Giordano
     */
    public static void checkNullParameter(boolean check, String msg) {
        Optional<String> message = Optional.ofNullable(msg);
        if (check) {
            throw new NullPointerException(message.orElse("empty message specified"));
        }
    }

}
