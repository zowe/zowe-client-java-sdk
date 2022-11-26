/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.DataSetUtils;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.input.ListParams;
import zowe.client.sdk.zosfiles.response.Dataset;
import zowe.client.sdk.zosfiles.types.AttributeType;
import zowe.client.sdk.zosfiles.types.OperationType;

import java.util.*;

/**
 * ZosDsnList class that provides Dataset member list function
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class ZosDsnList {

    private static final Logger LOG = LoggerFactory.getLogger(ZosDsnList.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * ZosDsnList constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public ZosDsnList(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative ZosDsnList constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public ZosDsnList(ZOSConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonGetRequest)) {
            throw new Exception("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform the http request
     *
     * @param params  list parameters
     * @param headers list of headers for http request
     * @param url     url for http request
     * @return response object with http response info
     * @author Frank Giordano
     */
    private Response getResponse(ListParams params, Map<String, String> headers, String url) throws Exception {
        LOG.debug(url);
        setHeaders(params, headers);
        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }
        request.setRequest(url);
        request.setHeaders(headers);
        return request.executeRequest();
    }

    /**
     * Get a list of Dataset names
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @param params      list parameters, see ListParams object
     * @return A String list of Dataset names
     * @throws Exception error processing request
     * @author Nikunj Goyal
     */
    @SuppressWarnings("unchecked")
    public List<Dataset> listDsn(String dataSetName, ListParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        final Map<String, String> headers = new HashMap<>();
        final List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + QueryConstants.QUERY_ID;

        url += ZosFilesConstants.QUERY_DS_LEVEL + EncodeUtils.encodeURIComponent(dataSetName);

        if (params.getVolume().isPresent()) {
            url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_VOLUME +
                    EncodeUtils.encodeURIComponent(params.getVolume().get());
        }
        if (params.getStart().isPresent()) {
            url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_START + params.getStart().get();
        }

        final Response response = getResponse(params, headers, url);
        if (response.isEmpty()) {
            return datasets;
        }

        try {
            RestUtils.checkHttpErrors(response);
        } catch (Exception e) {
            DataSetUtils.checkHttpErrors(e.getMessage(), List.of(dataSetName), OperationType.READ);
        }

        final JSONObject results = (JSONObject) response.getResponsePhrase().orElse(new JSONObject());
        if (results.isEmpty()) {
            return datasets;
        }
        final JSONArray items = (JSONArray) results.get(ZosFilesConstants.RESPONSE_ITEMS);
        items.forEach(item -> {
            JSONObject datasetObj = (JSONObject) item;
            datasets.add(DataSetUtils.parseJsonDSResponse(datasetObj));
        });

        return datasets;
    }

    /**
     * Get a list of members from a Dataset
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @param params      list parameters, see ListParams object
     * @return list of member names
     * @throws Exception error processing request
     * @author Nikunj Goyal
     */
    @SuppressWarnings("unchecked")
    public List<String> listDsnMembers(String dataSetName, ListParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        final Map<String, String> headers = new HashMap<>();
        final List<String> members = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" +
                EncodeUtils.encodeURIComponent(dataSetName) + ZosFilesConstants.RES_DS_MEMBERS;

        if (params.getPattern().isPresent()) {
            url += QueryConstants.QUERY_ID + ZosFilesConstants.QUERY_PATTERN +
                    EncodeUtils.encodeURIComponent(params.getPattern().get());
        }

        final Response response = getResponse(params, headers, url);
        if (response.isEmpty()) {
            return members;
        }

        try {
            RestUtils.checkHttpErrors(response);
        } catch (Exception e) {
            DataSetUtils.checkHttpErrors(e.getMessage(), List.of(dataSetName), OperationType.READ);
        }

        final JSONObject results = (JSONObject) response.getResponsePhrase().orElse(new JSONObject());
        if (results.isEmpty()) {
            return members;
        }
        final JSONArray items = (JSONArray) results.get("items");
        items.forEach(item -> {
            JSONObject datasetObj = (JSONObject) item;
            members.add(datasetObj.get("member").toString());
        });

        return members;
    }

    /**
     * Generate the http headers for the request
     *
     * @param params  list parameters
     * @param headers list of headers for http request
     * @author Nikunj Goyal
     */
    private void setHeaders(ListParams params, Map<String, String> headers) {
        String key, value;
        key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
        value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
        headers.put(key, value);

        if (params.getAttribute().isPresent()) {
            AttributeType attribute = params.getAttribute().get();
            if (attribute == AttributeType.BASE) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
            } else if (attribute == AttributeType.VOL) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_VOL").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_VOL").get(1);
            }
            headers.put(key, value);
        }
        if (params.getMaxLength().isPresent()) {
            key = "X-IBM-Max-Items";
            value = params.getMaxLength().get();
        } else {
            key = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
        }
        headers.put(key, value);
        if (params.getResponseTimeout().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
            value = params.getResponseTimeout().get();
            headers.put(key, value);
        }
        if (params.getRecall().isPresent()) {
            switch (params.getRecall().get().toLowerCase(Locale.ROOT)) {
                case "wait":
                    key = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_WAIT").get(0);
                    value = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_WAIT").get(1);
                    headers.put(key, value);
                    break;
                case "nowait":
                    key = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_NO_WAIT").get(0);
                    value = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_NO_WAIT").get(1);
                    headers.put(key, value);
                    break;
                case "error":
                    key = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_ERROR").get(0);
                    value = ZosmfHeaders.HEADERS.get("X_IBM_MIGRATED_RECALL_ERROR").get(1);
                    headers.put(key, value);
                    break;
            }
        }
    }

}
