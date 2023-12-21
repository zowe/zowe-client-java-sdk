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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.ZosLogItemJsonParse;
import zowe.client.sdk.parse.ZosLogReplyJsonParse;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonParserUtil;
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

    private static final String RESOURCE = "/zosmf/restconsoles/v1/log";

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * GetZosLog constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosLog(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative GetZosLog constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public ZosLog(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Issue a z/OSMF log command and return log data.
     * <p>
     * If API fails you may be missing APAR see PH35930 required for log operations.
     *
     * @param params ZosLogParams object
     * @return ZosLogReply object with log messages/items
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosLogReply issueCommand(final ZosLogParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String defaultUrl = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + RESOURCE;
        final StringBuilder url = new StringBuilder(defaultUrl);

        params.getStartTime().ifPresent(time -> {
            if (isNotValidDate(time)) {
                throw new IllegalArgumentException("startTime date format is invalid");
            }
            final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.systemDefault());
            final ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, formatter);
            url.append("?time=").append(zonedDateTime);
        });
        params.getTimeRange().ifPresent(timeRange -> {
            if (params.getQueryCount() > 1) {
                url.append("&timeRange=").append(timeRange);
            } else {
                url.append("?timeRange=").append(timeRange);
            }
        });
        params.getDirection().ifPresent(direction -> {
            if (params.getQueryCount() > 1) {
                url.append("&direction=").append(direction.getValue());
            } else {
                url.append("?direction=").append(direction.getValue());
            }
        });
        params.getHardCopy().ifPresent(hardCopy -> {
            if (params.getQueryCount() > 1) {
                url.append("&hardcopy=").append(hardCopy.getValue());
            } else {
                url.append("?hardcopy=").append(hardCopy.getValue());
            }
        });

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url.toString().replace("?&", "?"));

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no zos log response phrase")).toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        JSONArray jsonArray = new JSONArray();
        if (jsonObject.get("items") != null) {
            jsonArray = (JSONArray) jsonObject.get("items");
        }

        final List<ZosLogItem> zosLogItems = new ArrayList<>();
        final boolean isProcessResponse = params.isProcessResponses();

        for (Object itemJsonObj : jsonArray) {
            final ZosLogItemJsonParse parser = (ZosLogItemJsonParse) JsonParseFactory.buildParser(ParseType.ZOS_LOG_ITEM);
            zosLogItems.add(parser.parseResponse(itemJsonObj, isProcessResponse));
        }

        final ZosLogReplyJsonParse parser = (ZosLogReplyJsonParse) JsonParseFactory.buildParser(ParseType.ZOS_LOG_REPLY);
        return parser.parseResponse(jsonObject, zosLogItems);
    }

    /**
     * Validate given string in expected date/time string format.
     *
     * @param value string representing a date/time
     * @return boolean true or false
     * @author Frank Giordano
     */
    private static boolean isNotValidDate(final String value) {
        //  pattern to match example: 2022-11-27T05:06:20Z
        final String patternStr = ".*[0-9]-.*[0-9]-.*[0-9][T].*[0-9][:]*[0-9][:]*[0-9][Z]";
        final Pattern pattern = Pattern.compile(patternStr);
        final Matcher matcher = pattern.matcher(value);
        return !matcher.matches();
    }

}
