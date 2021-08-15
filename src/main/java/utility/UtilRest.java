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
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import rest.Response;

import java.io.IOException;
import java.util.Optional;

public class UtilRest {

    private static final Logger LOG = LogManager.getLogger(UtilRest.class);

    public static Optional<Object> getJsonResponseEntity(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("UtilRest::getJsonResponseEntity - result = {}", result);
            JSONParser parser = new JSONParser();
            try {
                if (result.isEmpty()) {
                    return Optional.empty();
                } else {
                    return Optional.ofNullable(parser.parse(result));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    public static Optional<Object> getTextResponseEntity(HttpResponse httpResponse) throws IOException {
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            String result = EntityUtils.toString(entity);
            LOG.debug("UtilRest::getTextResponseEntity - result = {}", result);
            return Optional.ofNullable(result);
        }
        return Optional.empty();
    }

    public static void checkHttpErrors(Response response) throws Exception {
        int httpCode;
        if (response.getStatusCode().isPresent())
            httpCode = response.getStatusCode().get();
        else throw new Exception("no http code value returned");
        if (Util.isHttpError(httpCode)) {
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

}
