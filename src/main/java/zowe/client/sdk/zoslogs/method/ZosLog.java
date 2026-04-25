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

import java.util.ArrayList;
import java.util.List;

/**
 * Get z/OS log via z/OSMF restful api
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class ZosLog {

    private static final String RESOURCE = "/restconsoles/v1/log";
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * ZosLog constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosLog(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative ZosLog constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    ZosLog(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Retrieve z/OS log data from the z/OSMF REST API.
     * <p>
     * If the API fails, you may be missing APAR see PH35930 required for log operations.
     *
     * @param logInputData ZosLogInputData object
     * @return ZosLogResponse object with log messages/items
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosLogResponse issueCommand(final ZosLogInputData logInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(logInputData, "logInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl())
                .append(RESOURCE);

        final List<String> queryParams = new ArrayList<>();

        // IBM documents the time parameter as optional. When it is omitted, z/OSMF defaults to the
        // current UNIX timestamp on the server. This avoids sending a client-side "now" value that
        // could be a few seconds ahead of the z/OSMF server’s current time.
        logInputData.getStartTime()
                .ifPresent(startTime -> queryParams.add("time=" + startTime));

        logInputData.getTimeRange()
                .ifPresent(timeRange -> queryParams.add("timeRange=" + timeRange));

        logInputData.getDirection()
                .ifPresent(direction -> queryParams.add("direction=" + direction.getValue()));

        logInputData.getHardCopy()
                .ifPresent(hardCopy -> queryParams.add("hardcopy=" + hardCopy.getValue()));

        if (!queryParams.isEmpty()) {
            url.append("?").append(String.join("&", queryParams));
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no zos log response phrase"))
                .toString();

        final String context = "issueCommand";
        return JsonUtils.parseResponse(responsePhrase, ZosLogResponse.class, context);
    }

}
