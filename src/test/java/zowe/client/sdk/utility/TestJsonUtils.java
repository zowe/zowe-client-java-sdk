package zowe.client.sdk.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.TreeMap;

/**
 * Common JSON utility methods for tests.
 */
public class TestJsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Serialize a {@code Map<String, String>} to a JSON string using Jackson.
     * <p>
     * Keys are sorted alphabetically to ensure deterministic output, making
     * this a safe replacement for {@code new JSONObject(map).toString()}.
     * </p>
     *
     * @param map the map to serialize
     * @return JSON string representation
     * @throws JsonProcessingException if serialization fails
     */
    public static String toJsonString(final Map<String, String> map) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new TreeMap<>(map));
    }

}
