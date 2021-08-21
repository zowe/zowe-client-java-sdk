/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package utility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import rest.Response;

import java.util.Optional;

/**
 * Utility Class for Rest related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class UtilRest {

    private static final Logger LOG = LogManager.getLogger(UtilRest.class);

    /**
     * Retrieve response JSON entity content from httpResponse object
     *
     * @param httpResponse HttpResponse object
     * @return The response Json entity content
     * @throws Exception due to extracting entity or parsing entity problem
     * @author Frank Giordano
     */
    public static Optional<Object> getJsonResponseEntity(HttpResponse httpResponse) throws Exception {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("UtilRest::getJsonResponseEntity - result = {}", result);
            try {
                if (result.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.ofNullable(new JSONObject(result));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    /**
     * Retrieve response text entity content from httpResponse object
     *
     * @param httpResponse HttpResponse object
     * @return The response text entity content
     * @throws Exception due to extracting entity or parsing entity problem
     * @author Frank Giordano
     */
    public static Optional<Object> getTextResponseEntity(HttpResponse httpResponse) throws Exception {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("UtilRest::getTextResponseEntity - result = {}", result);
            return Optional.ofNullable(result);
        }
        return Optional.empty();
    }

    /**
     * Return specialized http error message if any
     *
     * @param response Response object
     * @throws Exception containing specialized http error message
     * @author Frank Giordano
     */
    public static void checkHttpErrors(Response response) throws Exception {
        int httpCode;
        if (response.getStatusCode().isPresent())
            httpCode = response.getStatusCode().get();
        else throw new Exception("no http code value returned");
        if (isHttpError(httpCode)) {
            String responsePhrase = "";
            if (response.getResponsePhrase().isPresent())
                responsePhrase = (String) response.getResponsePhrase().get();
            String errorMsg = "http error code ";
            if (!responsePhrase.isEmpty())
                errorMsg += httpCode + " " + responsePhrase + ".";
            else errorMsg += httpCode + ".";
            throw new Exception(errorMsg);
        }
    }

    /**
     * Checks if statusCode is a valid http code or not
     *
     * @param statusCode The http code value
     * @return A boolean value
     * @author Frank Giordano
     */
    public static boolean isHttpError(int statusCode) {
        return !((statusCode >= 200 && statusCode <= 299) || (statusCode >= 100 && statusCode <= 199));
    }

}
