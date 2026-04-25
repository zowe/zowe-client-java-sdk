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

import org.checkerframework.checker.nullness.qual.NonNull;
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
 * Retrieves z/OS log data through the z/OSMF REST API.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class ZosLog {

    private static final String RESOURCE = "/restconsoles/v1/log";
    private static final String CONTEXT = "issueCommand";
    private static final int ZOSMF_MAX_LOG_ITEMS = 10_000;
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
     * with Mockito, and it is not recommended to be used by the larger community.
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
     * If the API fails, APAR PH35930 may be required for log operations.
     *
     * @param logInputData ZosLogInputData object containing optional log query parameters
     * @return ZosLogResponse object with log messages/items
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosLogResponse issueCommand(final ZosLogInputData logInputData) throws ZosmfRequestException {
        final String url = getUrl(logInputData, 0);
        setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no zos log response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, ZosLogResponse.class, CONTEXT);
    }

    /**
     * Retrieve z/OS log data from the z/OSMF REST API and continue requesting additional log data
     * when z/OSMF returns the maximum number of log items.
     * <p>
     * z/OSMF returns a maximum of 10,000 log items per request. When that limit is reached, the
     * returned next timestamp is used as the {@code timestamp} parameter for the next request.
     * <p>
     * The {@code totalMaxLimit} value is used as a pagination stop threshold. The returned number
     * of log items may exceed this value because z/OSMF returns results in response-sized batches.
     *
     * @param logInputData       ZosLogInputData object containing optional log query parameters
     * @param totalItemThreshold pagination stop threshold across requests
     * @return list of ZosLogResponse objects returned by each z/OSMF request
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<ZosLogResponse> issueCommand(final ZosLogInputData logInputData,
                                             final int totalItemThreshold) throws ZosmfRequestException {
        if (totalItemThreshold <= 0) {
            throw new IllegalArgumentException("totalItemThreshold must be greater than 0");
        }

        final List<ZosLogResponse> zosLogResponses = new ArrayList<>();
        String url = getUrl(logInputData, 0);
        this.setUrl(url);

        String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no zos log response phrase"))
                .toString();

        ZosLogResponse zosLogResponse = JsonUtils.parseResponse(responsePhrase, ZosLogResponse.class, CONTEXT);
        zosLogResponses.add(zosLogResponse);
        long totalItems = zosLogResponse.getTotalItems();

        // Prevent repeated paging if z/OSMF returns the same nextTimestamp more than once.
        long previousNextTimestamp = -1;
        while (totalItems < totalItemThreshold
                && zosLogResponse.getTotalItems() == ZOSMF_MAX_LOG_ITEMS
                && zosLogResponse.getNextTimeStamp() > 0) {

            final long nextTimestamp = zosLogResponse.getNextTimeStamp();
            if (previousNextTimestamp == nextTimestamp) {
                break;
            }
            previousNextTimestamp = nextTimestamp;

            url = getUrl(logInputData, nextTimestamp);
            setUrl(url);

            responsePhrase = request.executeRequest()
                    .getResponsePhrase()
                    .orElseThrow(() -> new IllegalStateException("no zos log response phrase"))
                    .toString();

            zosLogResponse = JsonUtils.parseResponse(responsePhrase, ZosLogResponse.class, CONTEXT);
            zosLogResponses.add(zosLogResponse);
            totalItems += zosLogResponse.getTotalItems();
        }

        return zosLogResponses;
    }

    /**
     * Builds the z/OSMF log REST API request URL from the provided input data.
     * <p>
     * When {@code nextTimestamp} is greater than zero, the value is added as the z/OSMF {@code timestamp}
     * query parameter and the {@code time} parameter is not included. This is used for follow-up requests
     * when paging through log responses.
     * <p>
     * When {@code nextTimestamp} is zero or less, the {@code time} query parameter is only included when
     * {@code logInputData} contains an explicit start time.
     * <p>
     * If no start time is provided, the {@code time} parameter is omitted and z/OSMF uses its own
     * server-side current time.
     *
     * @param logInputData  ZosLogInputData object containing optional log query parameters
     * @param nextTimestamp timestamp value from a previous z/OSMF log response, or zero for the initial request
     * @return z/OSMF log REST API request URL
     * @author Frank Giordano
     */
    private @NonNull String getUrl(final ZosLogInputData logInputData, final long nextTimestamp) {
        ValidateUtils.checkNullParameter(logInputData, "logInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl())
                .append(RESOURCE);

        final List<String> queryParams = new ArrayList<>();

        if (nextTimestamp > 0) {
            queryParams.add("timestamp=" + nextTimestamp);
        } else {
            logInputData.getStartTime()
                    .ifPresent(startTime -> queryParams.add("time=" + startTime));
        }

        logInputData.getTimeRange()
                .ifPresent(timeRange -> queryParams.add("timeRange=" + timeRange));

        logInputData.getDirection()
                .ifPresent(direction -> queryParams.add("direction=" + direction.getValue()));

        logInputData.getHardCopy()
                .ifPresent(hardCopy -> queryParams.add("hardcopy=" + hardCopy.getValue()));

        if (!queryParams.isEmpty()) {
            url.append("?").append(String.join("&", queryParams));
        }
        return url.toString();
    }

    /**
     * Sets the request URL on the z/OSMF request object.
     *
     * @param url z/OSMF log REST API request URL
     * @author Frank Giordano
     */
    private void setUrl(final String url) {
        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);
    }

}
