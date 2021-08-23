/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import core.ZOSConnection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

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
     * @param connection connection object, see ZOSConnection object
     * @return encoded String
     * @author Frank Giordano
     */
    public static String getAuthEncoding(ZOSConnection connection) {
        return Base64.getEncoder().encodeToString((connection.getUser() + ":" + connection.getPassword())
                .getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Check connection for validity.
     *
     * @param connection connection object, see ZOSConnection object
     * @throws IllegalStateException with message "Connection data not setup properly"
     * @author Frank Giordano
     */
    public static void checkConnection(ZOSConnection connection) {
        if (connection == null || connection.getPort() == null || connection.getHost() == null ||
                connection.getPassword() == null || connection.getUser() == null || connection.getPort().isEmpty() ||
                connection.getHost().isEmpty() || connection.getPassword().isEmpty() || connection.getUser().isEmpty())
            throw new IllegalStateException("Connection data not setup properly");
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
        if (check)
            throw new IllegalArgumentException(msg);
    }

    /**
     * Check for state of parameter
     *
     * @param check Check for true or false value
     * @param msg   message to display if check is true
     * @throws IllegalStateException with message
     * @author Frank Giordano
     */
    public static void checkStateParameter(boolean check, String msg) {
        if (check)
            throw new IllegalStateException(msg);
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
        String result;
        try {
            result = URLEncoder.encode(str, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = str;
        }
        return result;
    }

}
