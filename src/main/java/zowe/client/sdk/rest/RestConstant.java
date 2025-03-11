package zowe.client.sdk.rest;

import java.util.AbstractMap;
import java.util.Map;

public final class RestConstant {

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private RestConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final Map<Integer, String> HTTP_STATUS = Map.<Integer, String>ofEntries(
            new AbstractMap.SimpleEntry<>(200, "ok"),
            new AbstractMap.SimpleEntry<>(201, "created"),
            new AbstractMap.SimpleEntry<>(202, "accepted"),
            new AbstractMap.SimpleEntry<>(203, "non authoritative information"),
            new AbstractMap.SimpleEntry<>(204, "no content"),
            new AbstractMap.SimpleEntry<>(205, "reset content"),
            new AbstractMap.SimpleEntry<>(206, "partial content"),
            new AbstractMap.SimpleEntry<>(207, "multi status"),
            new AbstractMap.SimpleEntry<>(208, "already reported"),
            new AbstractMap.SimpleEntry<>(226, "im used"),
            new AbstractMap.SimpleEntry<>(300, "multiple choice"),
            new AbstractMap.SimpleEntry<>(301, "moved permanently"),
            new AbstractMap.SimpleEntry<>(302, "found"),
            new AbstractMap.SimpleEntry<>(303, "see other"),
            new AbstractMap.SimpleEntry<>(304, "not modified"),
            new AbstractMap.SimpleEntry<>(305, "use proxy"),
            new AbstractMap.SimpleEntry<>(306, "unused"),
            new AbstractMap.SimpleEntry<>(307, "temporary redirect"),
            new AbstractMap.SimpleEntry<>(308, "permanent redirect"),
            new AbstractMap.SimpleEntry<>(400, "bad request"),
            new AbstractMap.SimpleEntry<>(401, "unauthorized"),
            new AbstractMap.SimpleEntry<>(402, "payment required"),
            new AbstractMap.SimpleEntry<>(403, "forbidden"),
            new AbstractMap.SimpleEntry<>(404, "not found"),
            new AbstractMap.SimpleEntry<>(405, "method not allowed"),
            new AbstractMap.SimpleEntry<>(406, "not acceptable"),
            new AbstractMap.SimpleEntry<>(407, "proxy authentication required"),
            new AbstractMap.SimpleEntry<>(408, "request timeout"),
            new AbstractMap.SimpleEntry<>(409, "conflict"),
            new AbstractMap.SimpleEntry<>(410, "gone"),
            new AbstractMap.SimpleEntry<>(411, "length required"),
            new AbstractMap.SimpleEntry<>(412, "precondition failed"),
            new AbstractMap.SimpleEntry<>(413, "payload too long"),
            new AbstractMap.SimpleEntry<>(414, "uri too long"),
            new AbstractMap.SimpleEntry<>(415, "unsupported media type"),
            new AbstractMap.SimpleEntry<>(416, "range not satisfiable"),
            new AbstractMap.SimpleEntry<>(417, "expectation failed"),
            new AbstractMap.SimpleEntry<>(418, "in a teapot"),
            new AbstractMap.SimpleEntry<>(421, "misdirected request"),
            new AbstractMap.SimpleEntry<>(422, "unprocessable entity"),
            new AbstractMap.SimpleEntry<>(423, "locked"),
            new AbstractMap.SimpleEntry<>(424, "failed dependency"),
            new AbstractMap.SimpleEntry<>(425, "to early"),
            new AbstractMap.SimpleEntry<>(426, "upgrade required"),
            new AbstractMap.SimpleEntry<>(428, "precondition required"),
            new AbstractMap.SimpleEntry<>(429, "too many requests"),
            new AbstractMap.SimpleEntry<>(430, "request header fields too large"),
            new AbstractMap.SimpleEntry<>(451, "unavailable for legal reasons"),
            new AbstractMap.SimpleEntry<>(500, "internal server error"),
            new AbstractMap.SimpleEntry<>(501, "not implemented"),
            new AbstractMap.SimpleEntry<>(502, "bad gateway"),
            new AbstractMap.SimpleEntry<>(503, "service unavailable"),
            new AbstractMap.SimpleEntry<>(504, "gateway timeout"),
            new AbstractMap.SimpleEntry<>(505, "version not supported"),
            new AbstractMap.SimpleEntry<>(506, "variant also negotiates"),
            new AbstractMap.SimpleEntry<>(507, "insufficient storage"),
            new AbstractMap.SimpleEntry<>(508, "loop detected"),
            new AbstractMap.SimpleEntry<>(510, "not expended"),
            new AbstractMap.SimpleEntry<>(511, "network authentication required"));

}
