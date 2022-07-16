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

import zowe.client.sdk.core.ZOSConnection;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * Global Utility Class with static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class Util {

    /**
     * Wait by time specified.
     *
     * @param time in milliseconds
     * @author Frank Giordano
     */
    public static void wait(int time) {
        Timer timer = new Timer(time).initialize();
        while (!timer.isEnded()) {
        }
    }

    /**
     * Encodes the passed connection String as UTF-8 for usage of the AUTHORIZATION http header.
     *
     * @param connection connection information, see ZOSConnection object
     * @return encoded String
     * @author Frank Giordano
     */
    public static String getAuthEncoding(ZOSConnection connection) {
        Util.checkConnection(connection);
        return Base64.getEncoder().encodeToString((connection.getUser() + ":" + connection.getPassword())
                .getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Check connection for validity.
     *
     * @param connection connection information, see ZOSConnection object
     * @throws IllegalStateException with message "Connection data not setup properly"
     * @author Frank Giordano
     */
    public static void checkConnection(ZOSConnection connection) {
        if (connection == null || connection.getZosmfPort() == null || connection.getHost() == null ||
                connection.getPassword() == null || connection.getUser() == null || connection.getZosmfPort().isEmpty() ||
                connection.getHost().isEmpty() || connection.getPassword().isEmpty() || connection.getUser().isEmpty()) {
            throw new IllegalStateException("Connection data not setup properly");
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
     * Encodes the passed String as UTF-8 using an algorithm that's compatible
     * with JavaScript's encodeURIComponent function. Returns incoming string un-encoded if exception occurs.
     *
     * @param str string to be encoded
     * @return encoded String or original string
     * @author Frank Giordano
     */
    public static String encodeURIComponent(String str) {
        Util.checkNullParameter(str == null, "str is null");
        Util.checkIllegalParameter(str.isEmpty(), "str not specified");
        String result;
        result = URLEncoder.encode(str, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20")
                .replaceAll("\\%21", "!")
                .replaceAll("\\%27", "'")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%7E", "~");
        return result;
    }

}
