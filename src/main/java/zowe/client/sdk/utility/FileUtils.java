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

import zowe.client.sdk.rest.ZosmfHeaders;
import zowe.client.sdk.zosfiles.input.DownloadParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility Class for zosFiles related static helper methods.
 *
 * @author Nikunj Goyal
 * @version 2.0
 */
public final class FileUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Common method to build headers given input options object
     *
     * @param options various options parameters, see DownloadParams object
     * @return HeaderContent
     * @author Nikunj Goyal
     */
    public static Map<String, String> generateHeadersBasedOnOptions(DownloadParams options) {
        ValidateUtils.checkNullParameter(options == null, "options is null");
        String key, value;
        final Map<String, String> headers = new HashMap<>();

        if (options.getBinary().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_BINARY").get(1);
            headers.put(key, value);
        } else if (options.getEncoding().isPresent()) {
            key = ZosmfHeaders.X_IBM_TEXT;
            value = ZosmfHeaders.X_IBM_TEXT + ZosmfHeaders.X_IBM_TEXT_ENCODING + options.getEncoding();
            headers.put(key, value);
        }

        key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
        value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
        headers.put(key, value);

        if (options.getResponseTimeout().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
            value = ZosmfHeaders.HEADERS.get(options.getResponseTimeout().toString()).get(1);
            headers.put(key, value);
        }

        return headers;
    }

}
