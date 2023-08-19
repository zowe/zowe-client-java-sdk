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

import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Utility class for REST related static helper methods.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public final class RestUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private RestUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Perform z/OSMF rest api request and retrieve its response
     *
     * @param request zowe request object
     * @return Response object
     * @throws Exception http error code
     * @author Frank Giordano
     */
    public static Response getResponse(final ZosmfRequest request) throws Exception {
        final Response response = request.executeRequest();

        final String errMsg = "no response status code returned";
        final int statusCode = response.getStatusCode().orElseThrow(() -> new IllegalStateException(errMsg));

        if (!(statusCode >= 100 && statusCode <= 299)) {
            final AtomicReference<Object> responsePhrase = new AtomicReference<>();
            response.getResponsePhrase().ifPresent(responsePhrase::set);
            if (responsePhrase.get() instanceof byte[]) {
                try (final InputStreamReader inputStreamReader = new InputStreamReader(
                        new ByteArrayInputStream((byte[]) responsePhrase.get()), StandardCharsets.UTF_8)) {
                    final BufferedReader br = new BufferedReader(inputStreamReader);
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    responsePhrase.set(content.substring(0, content.length() - 1));
                    br.close();
                }
            }

            final String responsePhraseStr = String.valueOf((responsePhrase.get()));
            final String statusText = response.getStatusText().orElse("n\\a");
            String httpErrMsg = "http status error code: " + statusCode + ", status text: " + statusText;
            if (!statusText.equalsIgnoreCase(responsePhraseStr)) {
                httpErrMsg += ", response phrase: " + responsePhraseStr;
            }
            throw new Exception(httpErrMsg);
        }

        return response;
    }

}
