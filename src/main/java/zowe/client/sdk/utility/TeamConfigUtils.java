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

import java.util.*;

/**
 * Utility class contains helper methods for team configuration processing.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public final class TeamConfigUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private TeamConfigUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Mask sensitive key-value pairs in a properties map for safe logging.
     *
     * @param properties map of property key/value pairs
     * @return map with sensitive property values masked
     */
    public static Map<String, String> getMaskedProperties(final Map<String, String> properties) {
        return getMaskedProperties(properties, null);
    }

    /**
     * Mask sensitive key-value pairs in a properties map for safe logging.
     *
     * @param properties map of property key/value pairs
     * @param secure     optional list of secure key names to mask
     * @return map with sensitive property values masked
     */
    public static Map<String, String> getMaskedProperties(final Map<String, String> properties,
                                                          final List<String> secure) {
        if (properties == null || properties.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<String, String> masked = new HashMap<>();
        for (final Map.Entry<String, String> entry : properties.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            if (isSensitiveKey(key, secure)) {
                masked.put(key, (value == null || value.isEmpty()) ? "" : "*****");
            } else {
                masked.put(key, value);
            }
        }
        return masked;
    }

    /**
     * Check if a property key is sensitive based on key name or secure list.
     *
     * @param key    property key name
     * @param secure optional list of secure key names
     * @return true if sensitive, false otherwise
     */
    public static boolean isSensitiveKey(final String key, final List<String> secure) {
        if (key == null) {
            return false;
        }
        if (secure != null) {
            for (final String sec : secure) {
                if (key.equalsIgnoreCase(sec)) {
                    return true;
                }
            }
        }
        final String lowerKey = key.toLowerCase(Locale.ROOT);
        return lowerKey.contains("pass") || lowerKey.contains("pwd") ||
                lowerKey.contains("token") || lowerKey.contains("secret") ||
                lowerKey.contains("cert") || lowerKey.contains("key") ||
                lowerKey.contains("auth") || lowerKey.contains("cred");
    }

}
