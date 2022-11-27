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
package zowe.client.sdk.zoslogs;

import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.JsonGetRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zoslogs.input.DirectionType;
import zowe.client.sdk.zoslogs.input.ZosLogParams;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class GetZosLog {

    private static final Logger LOG = LoggerFactory.getLogger(GetZosLog.class);

    private ZOSConnection connection;
    private ZoweRequest request;

    public GetZosLog(ZOSConnection connection) {
        this.connection = connection;
    }

    public GetZosLog(ZOSConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonGetRequest)) {
            throw new Exception("GET_JSON request type required");
        }
        this.request = request;
    }

    public String getZosLog(ZosLogParams params) throws Exception {

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosLogsConstants.RESOURCE;

        if (params.getStartTime() != null) {
            url += "time=" + params.getStartTime();
        }

        if (params.getTimeRange() != null) {
            url += "&timeRange=" + params.getTimeRange();
        }

        if (params.getDirection() != null) {
            url += "&direction=" + params.getDirection().getValue();
        }

        LOG.debug(url);

        request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        request.setRequest(url);

        final Response response = request.executeRequest();
        if (response.isEmpty()) {
            return null;
        }
        RestUtils.checkHttpErrors(response);
        final JSONArray results = (JSONArray) response.getResponsePhrase().orElse(null);
        if (results == null) {
            return null;
        }

        return null;
    }

    public static void main(String[] args) throws Exception {
        LocalDateTime now =  LocalDateTime.now();     //Current Date and Time
        LocalDateTime sameDayNextMonth = now.plusMonths(1);       //2018-08-14
        LocalDateTime sameDayLastMonth = now.minusMonths(1);
        sameDayLastMonth.toString();

        ZOSConnection zosConnection = new ZOSConnection("47.19.64.77", "443", "FGIORD", "NEW4@DAY");
        GetZosLog getZosLog = new GetZosLog(zosConnection);
        ZosLogParams zosLogParams = new ZosLogParams("2021-05-25T07:00Z", null, "24h", false);
        getZosLog.getZosLog(zosLogParams);
    }

}
