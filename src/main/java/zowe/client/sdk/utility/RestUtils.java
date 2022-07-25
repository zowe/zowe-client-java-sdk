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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.rest.Response;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility Class for Rest related static helper methods.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public final class RestUtils {

    private RestUtils() {}

    private static final Logger LOG = LoggerFactory.getLogger(RestUtils.class);

    /**
     * Return specialized http error message
     *
     * @param response Response object
     * @throws Exception containing specialized http error message
     * @author Frank Giordano
     */
    public static void checkHttpErrors(Response response) throws Exception {
        ValidateUtils.checkNullParameter(response == null, "response is null");
        int httpCode;
        if (response.getStatusCode().isPresent()) {
            httpCode = response.getStatusCode().get();
        } else {
            throw new Exception("no http code value returned");
        }
        if (isHttpError(httpCode)) {
            String responsePhrase = "";
            if (response.getResponsePhrase().isPresent()) {
                responsePhrase = (String) response.getResponsePhrase().get();
            }
            String errorMsg = "Http error code ";
            if (!responsePhrase.isEmpty()) {
                errorMsg += httpCode + " " + responsePhrase + ".";
            } else {
                errorMsg += httpCode + ".";
            }
            throw new Exception(errorMsg);
        }
    }

    /**
     * Retrieve response JSON entity content from httpResponse object
     *
     * @param httpResponse HttpResponse object
     * @return response Json entity content
     * @throws Exception due to extracting entity or parsing entity problem
     * @author Frank Giordano
     */
    public static Object getJsonResponseEntity(HttpResponse httpResponse) throws Exception {
        ValidateUtils.checkNullParameter(httpResponse == null, "httpResponse is null");
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("UtilRest::getJsonResponseEntity - result = {}", result);
            JSONParser parser = new JSONParser();
            try {
                if (result.isEmpty()) {
                    return null;
                } else {
                    return parser.parse(result);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Retrieve response text entity content from httpResponse object
     *
     * @param httpResponse HttpResponse object
     * @return response text entity content
     * @throws Exception due to extracting entity or parsing entity problem
     * @author Frank Giordano
     */
    public static Object getTextResponseEntity(HttpResponse httpResponse) throws Exception {
        ValidateUtils.checkNullParameter(httpResponse == null, "httpResponse is null");
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("UtilRest::getTextResponseEntity - result = {}", result);
            return result;
        }
        return null;
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
        ValidateUtils.checkNullParameter(url == null, "url is null");
        try {
            new URL(url).toURI();
            return false;
        } catch (URISyntaxException | MalformedURLException exception) {
            return true;
        }
    }

}
