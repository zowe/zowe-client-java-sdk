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

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class contains helper methods for encoding processing
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class EncodeUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private EncodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Encodes the passed String as UTF-8 using an algorithm that's compatible
     * with JavaScript's encodeURIComponent function. Returns incoming string un-encoded if exception occurs.
     *
     * @param value string to be encoded
     * @return encoded String or original string
     * @author Frank Giordano
     */
    public static String encodeURIComponent(final String value) {
        ValidateUtils.checkIllegalParameter(value, "value");
        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20")
                .replaceAll("%21", "!")
                .replaceAll("%27", "'")
                .replaceAll("%28", "(")
                .replaceAll("%29", ")")
                .replaceAll("%7E", "~");
    }

    /**
     * Encodes the passed connection String as UTF-8 for usage of the AUTHORIZATION http header.
     *
     * @param connection for connection information, see ZosConnection object
     * @return encoded String
     * @author Frank Giordano
     */
    public static String encodeAuthComponent(final ZosConnection connection) {
        final String user = connection.getUser() != null && !connection.getUser().isEmpty() ? connection.getUser() : "";
        final String pwd = connection.getPassword() != null && !connection.getPassword().isEmpty() ? connection.getPassword() : "";
        return Base64.getEncoder().encodeToString((user + ":" + pwd).getBytes(StandardCharsets.UTF_8));
    }

}
