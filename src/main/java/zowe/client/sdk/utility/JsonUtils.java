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
import java.util.Map;

/**
 * Utility class contains helper methods for JSON parse processing.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public final class JsonUtils {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String PARSE_ERROR_MSG = "json response parse error";

    /**
     * Defines the target JSON representation for parsing a z/OSMF response.
     * <p>
     * This enum is used internally to determine which JSON parsing implementation
     * should be used when converting a JSON string into a concrete representation.
     * </p>
     */
    private enum ParseTarget {

        /**
         * Parse the JSON content into a Jackson {@link JsonNode}.
         */
        NODE,

        /**
         * Parse the JSON content into a json-simple {@link JSONArray}.
         */
        ARRAY
    }

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parses a JSON string returned from a z/OSMF request into a Jackson
     * {@link JsonNode}.
     * <p>
     * This method is intended for general JSON parsing and supports both JSON
     * objects and arrays. Callers can inspect the returned {@link JsonNode}
     * to determine its structure.
     * </p>
     *
     * @param item JSON string representation returned from a z/OSMF request
     * @return {@link JsonNode} representing the parsed JSON content
     * @throws ZosmfRequestException indicates the JSON item from the z/OSMF request
     *                               is invalid or cannot be parsed
     */
    public static JsonNode parse(final String item) throws ZosmfRequestException {
        return (JsonNode) parseInternal(item, ParseTarget.NODE);
    }

    /**
     * Parses a JSON array string returned from a z/OSMF request into a
     * json-simple {@link JSONArray}.
     * <p>
     * This method exists primarily for backward compatibility with existing
     * code that depends on json-simple types. New implementations should
     * prefer {@link #parse(String)} and work with {@link JsonNode} instead.
     * </p>
     *
     * @param item JSON array string representation returned from a z/OSMF request
     * @return {@link JSONArray} representing the parsed JSON array
     * @throws ZosmfRequestException indicates the JSON item from the z/OSMF request
     *                               is invalid or cannot be parsed
     */
    public static JSONArray parseArray(final String item) throws ZosmfRequestException {
        return (JSONArray) parseInternal(item, ParseTarget.ARRAY);
    }

    /**
     * Internal helper method that performs JSON parsing for z/OSMF responses.
     * <p>
     * This method centralizes parsing logic and exception handling for all JSON
     * parsing operations, ensuring consistent logging and error translation into
     * {@link ZosmfRequestException}.
     * </p>
     *
     * @param item   JSON string representation returned from a z/OSMF request
     * @param target the desired target representation for the parsed JSON
     * @return parsed JSON object in the requested representation
     * @throws ZosmfRequestException indicates the JSON item from the z/OSMF request
     *                               is invalid or cannot be parsed
     */
    private static Object parseInternal(final String item, final ParseTarget target)
            throws ZosmfRequestException {
        try {
            return target == ParseTarget.ARRAY
                    ? new JSONParser().parse(item)
                    : objectMapper.readTree(item);
        } catch (Exception e) {
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

        for (Map.Entry<String, JsonNode> entry : root.properties()) {
            // Convert any type to string
            map.put(entry.getKey(), entry.getValue().asText());
        }

        return map;
    }

}
