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
 * Utility class contains helper methods for encoding processing.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public final class EncodeUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private EncodeUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Encodes the given String using UTF-8 in a manner compatible with JavaScript's
     * {@code encodeURIComponent} function.
     *
     * <p>Note: the input value must be non-null and non-empty. Validation is enforced
     * to match SDK defensive programming standards.</p>
     *
     * @param value the string to encode
     * @return the encoded string
     * @throws IllegalArgumentException if {@code value} is null or empty
     */
    public static String encodeURIComponent(final String value) {
        ValidateUtils.checkIllegalParameter(value, "value");

        return URLEncoder.encode(value, StandardCharsets.UTF_8)
                .replace("+", "%20")
                .replace("%21", "!")
                .replace("%27", "'")
                .replace("%28", "(")
                .replace("%29", ")")
                .replace("%7E", "~");
    }

    /**
     * Encodes the passed connection String as UTF-8 for usage of the AUTHORIZATION http header.
     *
     * @param connection for connection information, see ZosConnection object
     * @return encoded String
     * @author Frank Giordano
     */
    public static String encodeBasicAuthCredentials(final ZosConnection connection) {
        final String user = connection.getUser() != null && !connection.getUser().isEmpty() ? connection.getUser() : "";
        final String pwd = connection.getPassword() != null && !connection.getPassword().isEmpty() ? connection.getPassword() : "";
        return Base64.getEncoder().encodeToString((user + ":" + pwd).getBytes(StandardCharsets.UTF_8));
    }

}
