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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class contains helper methods for JSON parse processing.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public final class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PARSE_ERROR_MSG = "json response parse error";

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JsonUtils() {
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

    /**
     * Parse a JSON string into a specified POJO type.
     *
     * @param json    the JSON string to parse
     * @param clazz   the target class type
     * @param context the context for logging purposes
     * @param <T>     the type parameter
     * @return deserialized object of type T
     * @throws ZosmfRequestException if parsing fails
     */
    public static <T> T parseResponse(final String json, final Class<T> clazz, final String context)
            throws ZosmfRequestException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException(
                    "Failed to parse JSON response for [" + context +
                            "] into " + clazz.getSimpleName(), e);

        }
    }

    /**
     * Convert a JSONObject to a {@code Map<String, String>}, converting all values to String.
     * <p>
     * This method supports JSON values of any type (string, number, boolean, null, etc.)
     * and ensures that all map values are safely represented as strings.
     *
     * @param jsonObject the JSONObject to convert (must not be null)
     * @return a {@code Map<String, String>} with all keys its values as all String values
     * @throws JsonProcessingException if JSON parsing fails
     */
    public static Map<String, String> parseMap(JSONObject jsonObject) throws JsonProcessingException {
        // Convert the org.json.JSONObject to Jackson JsonNode for traversal
        final JsonNode root = objectMapper.readTree(jsonObject.toString());

        final Map<String, String> map = new HashMap<>();
        final Iterator<Map.Entry<String, JsonNode>> fields = root.fields();

        while (fields.hasNext()) {
            final Map.Entry<String, JsonNode> entry = fields.next();
            // Convert any type to string
            map.put(entry.getKey(), entry.getValue().asText());
        }

        return map;
    }

}
