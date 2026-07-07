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
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.util.Map;
import java.util.TreeMap;

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
     * Private constructor defined to avoid instantiation of class
     */
    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Centralized JSON parsing for z/OSMF responses using Jackson.
     * <p>
     * The returned JsonNode may represent either a JSON object or a JSON array.
     * Callers can inspect the node type as needed.
     * </p>
     *
     * @param item JSON string representation
     * @return JsonNode (ObjectNode, ArrayNode, etc.)
     * @throws ZosmfRequestException if parsing fails
     */
    public static JsonNode parse(final String item) throws ZosmfRequestException {
        try {
            return objectMapper.readTree(item);
        } catch (JsonProcessingException e) {
            LOG.debug(PARSE_ERROR_MSG, e);
            throw new ZosmfRequestException(e.getMessage(), e);
        }
    }

    /**
     * Convenience method for array responses.
     * <p>
     * This method validates that the parsed JSON content is an array and throws
     * a {@link ZosmfRequestException} if the content is not a JSON array.
     * </p>
     *
     * @param item JSON array string representation returned from a z/OSMF request
     * @return ArrayNode representing the parsed JSON array
     * @throws ZosmfRequestException if parsing fails or root is not an array
     */
    public static ArrayNode parseArray(final String item) throws ZosmfRequestException {
        JsonNode node = parse(item);
        if (!node.isArray()) {
            throw new ZosmfRequestException("Expected JSON array but got: " + node.getNodeType());
        }
        return (ArrayNode) node;
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
     * Serialize the given object as a JSON request body.
     *
     * @param body object to serialize (Map or POJO)
     * @return JSON string
     * @throws ZosmfRequestException if serialization fails
     */
    public static String asRequestBodyJson(final Object body) throws ZosmfRequestException {
        if (body == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException(
                    "Failed to serialize request body to JSON", e);
        }
    }

    /**
     * Serialize a {@code Map<String, String>} to a JSON string using Jackson.
     * <p>
     * Keys are sorted alphabetically to ensure deterministic output, making
     * this a safe replacement for {@code new JSONObject(map).toString()}.
     * </p>
     *
     * @param map the map to serialize
     * @return JSON string representation
     * @throws ZosmfRequestException if serialization fails
     */
    public static String toJsonString(final Map<String, String> map) throws ZosmfRequestException {
        try {
            return objectMapper.writeValueAsString(new TreeMap<>(map));
        } catch (JsonProcessingException e) {
            LOG.debug("json serialization error", e);
            throw new ZosmfRequestException("json serialization error: " + e.getMessage(), e);
        }
    }

}
