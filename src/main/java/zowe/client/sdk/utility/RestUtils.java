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

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility Class for Rest related static helper methods.
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

        final int statusCode = response.getStatusCode()
                .orElseThrow(() -> new Exception("no response status code returned"));

        final String responsePhrase = String.valueOf((response.getResponsePhrase()
                .orElse("n/a")));

        if (RestUtils.isHttpError(statusCode)) {
            final String statusText = response.getStatusText()
                    .orElseThrow(() -> new Exception("no response status text returned"));
            String msg = "http status error code: " + statusCode + ", status text: " + statusText;
            if (!statusText.equalsIgnoreCase(responsePhrase)) {
                msg += ", response phrase: " + responsePhrase;
            }
            throw new Exception(msg);
        }

        return response;
    }

    /**
     * Checks if statusCode is a valid http code or not
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
     * @param url value
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
