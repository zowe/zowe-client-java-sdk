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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class contains helper methods for TSO response processing.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public final class TsoUtils {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private TsoUtils() {
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

    /**
     * Retrieve error message text from response string value.
     *
     * @param responseStr response string value
     * @return error message text
     * @author Frank Giordano
     */
    public static String getMsgDataText(String responseStr) {
        String errMsg = "";
        JsonNode rootNode;
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            rootNode = objectMapper.readTree(responseStr);
            rootNode = rootNode.get("msgData");
            if (rootNode.isArray() && rootNode.size() == 1) {
                errMsg = rootNode.get(0).get("messageText").asText();
            } else if (rootNode.isArray() && rootNode.size() > 1) {
                errMsg = rootNode.toPrettyString();
            } else if (rootNode.isObject()) {
                errMsg = rootNode.toPrettyString();
            }
        } catch (JsonProcessingException ignored) {
        }
        return errMsg;
    }

}
