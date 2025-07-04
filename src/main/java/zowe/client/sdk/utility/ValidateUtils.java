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

import zowe.client.sdk.core.AuthType;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.core.ZosConnection;

import java.util.Optional;

/**
 * Utility class contains helper methods for validation processing
 *
 * @author Frank Giordano
 * @version 3.0
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
     * @throws IllegalStateException with invalid connection object message
     * @author Frank Giordano
     */
    public static void checkConnection(final ZosConnection connection) {
        if (connection == null) {
            throw new IllegalStateException("connection is null");
        }
        var errMsg = "required connection attribute(s) missing for " + connection.getAuthType() + " authentication";
        if (connection.getAuthType().equals(AuthType.CLASSIC)) {
            if (connection.getZosmfPort() == null || connection.getHost() == null ||
                    connection.getPassword() == null || connection.getUser() == null ||
                    connection.getZosmfPort().isBlank() || connection.getHost().isBlank() ||
                    connection.getPassword().isBlank() || connection.getUser().isBlank()) {
                throw new IllegalStateException(errMsg);
            }
        } else if (connection.getAuthType().equals(AuthType.TOKEN)) {
            if (connection.getZosmfPort() == null || connection.getHost() == null ||
                    connection.getCookie() == null || connection.getZosmfPort().isBlank() ||
                    connection.getHost().isBlank()) {
                throw new IllegalStateException(errMsg);
            }
        } else if (connection.getAuthType().equals(AuthType.SSL)) {
            if (connection.getZosmfPort() == null || connection.getHost() == null ||
                    connection.getCookie() == null || connection.getCertFilePath() == null ||
                    connection.getZosmfPort().isBlank() || connection.getHost().isBlank() ||
                    connection.getCertFilePath().isBlank()) {
                throw new IllegalStateException(errMsg);
            } else {
                if (!FileUtils.doesPathExistAndIsFile(connection.getCertFilePath())) {
                    throw new IllegalStateException("certificate file does not exist");
                }
            }
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
     * @param msg   message to display if check is true
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
     * @param connection connection information, see SshConnection object
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
