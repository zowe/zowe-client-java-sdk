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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

/**
 * Utility class contains helper methods for JSON parse processing
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class JsonParserUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonParserUtil.class);

    private static final String PARSE_ERROR_MSG = "json response parse error";

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JsonParserUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * This method is a wrapper for JSONParser().parse() call to parse z/OSMF response
     * which may return ZosmfRequestException.
     *
     * @param item JSON string representation
     * @return JSONObject object
     * @throws ZosmfRequestException indicates the JSON item from z/OSMF request is invalid for parsing
     */
    public static JSONObject parse(final String item) throws ZosmfRequestException {
        try {
            return (JSONObject) new JSONParser().parse(item);
        } catch (ParseException e) {
            LOG.debug(PARSE_ERROR_MSG, e);
            throw new ZosmfRequestException(e.getMessage(), e);
        }
    }

    /**
     * This method is a wrapper for JSONParser().parse() call to parse z/OSMF response
     * which may return ZosmfRequestException.
     *
     * @param item JSON array representation
     * @return JSONArray object
     * @throws ZosmfRequestException indicates the JSON item from z/OSMF request is invalid for parsing
     */
    public static JSONArray parseArray(final String item) throws ZosmfRequestException {
        try {
            return (JSONArray) new JSONParser().parse(item);
        } catch (ParseException e) {
            LOG.debug(PARSE_ERROR_MSG, e);
            throw new ZosmfRequestException(e.getMessage(), e);
        }
    }

}
