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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zoslogs.input.ZosLogInputData;
import zowe.client.sdk.zoslogs.response.ZosLogResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Get z/OS log via z/OSMF restful api
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosLog {

    private static final String RESOURCE = "/restconsoles/v1/log";

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * GetZosLog constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosLog(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative GetZosLog constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    ZosLog(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Issue a z/OSMF log command and return log data.
     * <p>
     * If the API fails, you may be missing APAR see PH35930 required for log operations.
     *
     * @param logInputData ZosLogInputData object
     * @return ZosLogReply object with log messages/items
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosLogResponse issueCommand(final ZosLogInputData logInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(logInputData == null, "logInputData is null");

        final String defaultUrl = connection.getZosmfUrl() + RESOURCE;
        final StringBuilder url = new StringBuilder(defaultUrl);
        final String customPattern = "yyyy-MM-dd'T'HH:mm'Z'";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(customPattern);

        logInputData.getStartTime().ifPresentOrElse(time -> url.append("?time=").append(time),
                () -> url.append("?time=").append(LocalDateTime.now().format(formatter)));
        logInputData.getTimeRange().ifPresent(timeRange -> {
            if (logInputData.getQueryCount() > 1) {
                url.append("&timeRange=").append(timeRange);
            } else {
                url.append("?timeRange=").append(timeRange);
            }
        });
        logInputData.getDirection().ifPresent(direction -> {
            if (logInputData.getQueryCount() > 1) {
                url.append("&direction=").append(direction.getValue());
            } else {
                url.append("?direction=").append(direction.getValue());
            }
        });
        logInputData.getHardCopy().ifPresent(hardCopy -> {
            if (logInputData.getQueryCount() > 1) {
                url.append("&hardcopy=").append(hardCopy.getValue());
            } else {
                url.append("?hardcopy=").append(hardCopy.getValue());
            }
        });

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url.toString().replace("?&", "?"));

        final String responsePhrase = String.valueOf(request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no zos log response phrase")));

        String context = "issueCommand";
        return JsonUtils.parseResponse(responsePhrase, ZosLogResponse.class, context);
    }

}
