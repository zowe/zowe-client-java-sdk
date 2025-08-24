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

import zowe.client.sdk.core.SshConnection;

import java.util.Optional;

/**
 * Utility class contains helper methods for validation processing
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class ValidateUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ValidateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check for the state of parameter by String value
     *
     * @param value string value
     * @param name  name of attribute
     * @author Frank Giordano
     */
    public static void checkIllegalParameter(String value, String name) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(name + " is either null or empty");
    }

    /**
     * Check for state of parameter
     *
     * @param check Check for true or false value
     * @param msg   message to display if the check is true
     * @throws IllegalArgumentException with message
     * @author Frank Giordano
     */
    public static void checkIllegalParameter(final boolean check, final String msg) {
        Optional<String> message = Optional.ofNullable(msg);
        if (check) {
            throw new IllegalArgumentException(message.orElse("empty message specified"));
        }
    }

    /**
     * Check for null parameter
     *
     * @param check check for true or false value
     * @param msg   message to display if the check is true
     * @throws IllegalArgumentException with message
     * @author Frank Giordano
     */
    public static void checkNullParameter(final boolean check, final String msg) {
        Optional<String> message = Optional.ofNullable(msg);
        if (check) {
            throw new NullPointerException(message.orElse("empty message specified"));
        }
    }

    /**
     * Check SSH connection for validity.
     *
     * @param connection for connection information, see SshConnection object
     * @throws IllegalStateException with the message "Connection data not setup properly"
     * @author Frank Giordano
     */
    public static void checkSshConnection(final SshConnection connection) {
        if (connection == null || connection.getHost() == null || connection.getPassword() == null ||
                connection.getUser() == null || connection.getHost().isBlank() ||
                connection.getPassword().isBlank() || connection.getUser().isBlank()) {
            throw new IllegalStateException("SSH connection data not setup properly");
        }
    }

}
