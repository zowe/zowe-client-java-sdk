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

/**
 * Utility class contains helper methods for console processing.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class ConsoleUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ConsoleUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Process response from console command. Add a line break if needed.
     *
     * @param response string value of response from console command
     * @return string value of response with line break added if needed
     */
    public static String processCmdResponse(String response) {
        response = response.replace('\r', '\n');
        if (!response.isBlank() && response.charAt(response.length() - 1) != '\n') {
            response = response + "\n";
        }
        return response;
    }

}
