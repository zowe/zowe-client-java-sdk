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
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class contains helper methods for response processing
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoUtil {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private TsoUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Helper method calls to perform the request http request and returns the response object
     * as a string value.
     *
     * @param request request object for performing http call
     * @return response object as a string value of the http call
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public static String getResponseStr(final ZosmfRequest request) throws ZosmfRequestException {
        final Response response = request.executeRequest();
        final String responseStr = response.getResponsePhrase()
                .orElseThrow(() -> new ZosmfRequestException("response phrase is either null or empty"))
                .toString();

        AtomicInteger statusCode = new AtomicInteger();
        response.getStatusCode().ifPresent(statusCode::set);

        if (!(statusCode.get() >= 100 && statusCode.get() <= 299)) {
            throw new ZosmfRequestException("Response: " + responseStr);
        }

        return responseStr;
    }

}
