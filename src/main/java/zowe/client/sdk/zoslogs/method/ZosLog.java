/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zowe.client.sdk.zoslogs.method;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonGetRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zoslogs.input.ZosLogParams;
import zowe.client.sdk.zoslogs.response.ZosLogItem;
import zowe.client.sdk.zoslogs.response.ZosLogReply;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Get z/OS log via z/OSMF restful api
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosLog {

    private static final String RESOURCE = "/zosmf/restconsoles/v1/log?";
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * GetZosLog constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosLog(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative GetZosLog constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ZosLog(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonGetRequest)) {
            throw new Exception("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Issue a z/OSMF log command, returns "raw" z/OSMF response.
     * <p>
     * If API fails you may be missing APAR see PH35930 required for log operations.
     *
     * @param params ZosLogParams object
     * @return ZosLogReply object with log messages/items
     * @throws Exception processing error
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    public ZosLogReply issueCommand(ZosLogParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String defaultUrl = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + RESOURCE;
        final StringBuilder url = new StringBuilder(defaultUrl);

        params.getStartTime().ifPresent(time -> {
            if (isNotValidDate(time)) {
                throw new IllegalArgumentException("startTime date format is invalid");
            }
            final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
            final ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, formatter);
            url.append("time=").append(zonedDateTime);
        });
        params.getTimeRange().ifPresent(timeRange -> url.append("&timeRange=").append(timeRange));
        params.getDirection().ifPresent(direction -> url.append("&direction=").append(direction.getValue()));
        params.getHardCopy().ifPresent(hardCopy -> url.append("&hardcopy=").append(hardCopy.getValue()));

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }
        request.setUrl(url.toString());

        final Response response = RestUtils.getResponse(request);
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(response.getResponsePhrase().get().toString());
        JSONArray jsonArray = new JSONArray();
        if (jsonObject.get("items") != null) {
            jsonArray = (JSONArray) jsonObject.get("items");
        }

        final List<ZosLogItem> zosLogItems = new ArrayList<>();
        final boolean isProcessResponse = params.isProcessResponses();
        jsonArray.forEach(item -> {
            final JSONObject itemObj = (JSONObject) item;
            final String message = processMessage(itemObj, isProcessResponse);
            final ZosLogItem.Builder zosLogItemBuilder = new ZosLogItem.Builder()
                    .cart(itemObj.get("cart") != null ? (String) itemObj.get("cart") : null)
                    .color(itemObj.get("color") != null ? (String) itemObj.get("color") : null)
                    .jobName(itemObj.get("jobName") != null ? (String) itemObj.get("jobName") : null)
                    .message(message)
                    .messageId(itemObj.get("messageId") != null ? (String) itemObj.get("messageId") : null)
                    .replyId(itemObj.get("replyId") != null ? (String) itemObj.get("replyId") : null)
                    .system(itemObj.get("system") != null ? (String) itemObj.get("system") : null)
                    .type(itemObj.get("type") != null ? (String) itemObj.get("type") : null)
                    .subType(itemObj.get("subType") != null ? (String) itemObj.get("subType") : null)
                    .time(itemObj.get("time") != null ? (String) itemObj.get("time") : null)
                    .timeStamp(itemObj.get("timestamp") != null ? (Long) itemObj.get("timestamp") : 0);
            zosLogItems.add(zosLogItemBuilder.build());
        });

        return new ZosLogReply(jsonObject.get("timezone") != null ? (Long) jsonObject.get("timezone") : 0,
                jsonObject.get("nextTimestamp") != null ? (Long) jsonObject.get("nextTimestamp") : 0,
                jsonObject.get("source") != null ? (String) jsonObject.get("source") : null,
                jsonObject.get("totalitems") != null ? (Long) jsonObject.get("totalitems") : null,
                zosLogItems);
    }

    /**
     * Process response message; message contains a log line statement.
     * Perform special newline replacement if applicable.
     *
     * @param jsonObj JSONObject object
     * @return string value of the message processed
     * @author Frank Giordano
     */
    private static String processMessage(JSONObject jsonObj, boolean isProcessResponse) {
        try {
            String message = (String) jsonObj.get("message");
            if (isProcessResponse) {
                if (message.contains("\r")) {
                    message = message.replace('\r', '\n');
                }
                if (message.contains("\n\n")) {
                    message = message.replaceAll("\n\n", "\n");
                }
            }
            return message;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validate given string in expected date/time string format.
     *
     * @param str string representing a date/time
     * @return boolean value
     * @author Frank Giordano
     */
    private static boolean isNotValidDate(String str) {
        //  pattern to match example: 2022-11-27T05:06:20Z
        final String patternStr = ".*[0-9]-.*[0-9]-.*[0-9][T].*[0-9][:]*[0-9][:]*[0-9][Z]";
        final Pattern pattern = Pattern.compile(patternStr);
        final Matcher matcher = pattern.matcher(str);
        return !matcher.matches();
    }

}
