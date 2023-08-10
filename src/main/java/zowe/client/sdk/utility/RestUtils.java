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
import zowe.client.sdk.rest.ZoweRequest;

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
     * Perform zowe rest request and retrieve its response
     *
     * @param request zowe request object
     * @return Response object
     * @throws Exception http error code
     * @author Frank Giordano
     */
    public static Response getResponse(ZoweRequest request) throws Exception {
        final Response response = request.executeRequest();

        final String statusCodeErrMsg = "no response status code returned";
        final int statusCode = response.getStatusCode().orElseThrow(() -> new Exception(statusCodeErrMsg));

        if (RestUtils.isHttpError(statusCode)) {
            final AtomicReference<Object> responsePhase = new AtomicReference<>();
            response.getResponsePhrase().ifPresent(phrase -> responsePhase.set(phrase));
            if (responsePhase.get() instanceof byte[]) {
                final InputStreamReader inputStreamReader = new InputStreamReader(
                        new ByteArrayInputStream((byte[]) responsePhase.get()), StandardCharsets.UTF_8);
                final BufferedReader br = new BufferedReader(inputStreamReader);
                String content = "";
                String line;
                while ((line = br.readLine()) != null) {
                    content += line + "\n";
                }
                responsePhase.set(content.substring(0, content.length() - 1));
            }

            final Optional<String> responsePhaseStr = Optional.ofNullable(responsePhase.get().toString());
            final String responsePhraseErrMsg = "no response phrase returned";
            final String responsePhrase = String.valueOf((
                    !responsePhaseStr.isEmpty() && !responsePhaseStr.get().trim().isEmpty() ?
                            responsePhase.get() : responsePhraseErrMsg));
            final String statusTextErrMsg = "no response status text returned";
            final String statusText =
                    !response.getStatusText().isEmpty() && !response.getStatusText().get().trim().isEmpty()
                    ? response.getStatusText().get() : statusTextErrMsg;
            String errMsg = "http status error code: " + statusCode + ", status text: " + statusText;
            if (!statusText.equalsIgnoreCase(responsePhrase)) {
                errMsg += ", response phrase: " + responsePhrase;
            }
            throw new Exception(errMsg);
        }

        return response;
    }

    /**
     * Checks if statusCode is an error http code or not
     *
     * @param statusCode http code value
     * @return boolean value
     * @author Frank Giordano
     */
    public static boolean isHttpError(int statusCode) {
        return !((statusCode >= 200 && statusCode <= 299) || (statusCode >= 100 && statusCode <= 199));
    }

    /**
     * Checks if url is a valid http or https url.
     *
     * @param url string value
     * @return boolean value
     * @author Frank Giordano
     */
    public static boolean isUrlNotValid(String url) {
        try {
            new URL(url).toURI();
            return false;
        } catch (URISyntaxException | MalformedURLException exception) {
            return true;
        }
    }

}
