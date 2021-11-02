/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.*;
import utility.Util;
import utility.UtilDataset;
import utility.UtilRest;
import zosfiles.input.ListParams;
import zosfiles.response.Dataset;

import java.util.*;

/**
 * ZosDsnList class that provides Dataset member list function
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class ZosDsnList {

    private static final Logger LOG = LogManager.getLogger(ZosDsnList.class);

    private final ZOSConnection connection;

    /**
     * ZosDsnList constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public ZosDsnList(ZOSConnection connection) {
        Util.checkConnection(connection);
        this.connection = connection;
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
        Util.checkNullParameter(params == null, "params is null");
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        Map<String, String> headers = new HashMap<>();
        List<String> members = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" +
                Util.encodeURIComponent(dataSetName) + ZosFilesConstants.RES_DS_MEMBERS;

        if (params.getPattern().isPresent()) {
            url += QueryConstants.QUERY_ID + ZosFilesConstants.QUERY_PATTERN +
                    Util.encodeURIComponent(params.getPattern().get());
        }

        Response response = getResponse(params, headers, url);
        if (response.isEmpty())
            return members;

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), Arrays.asList(dataSetName), UtilDataset.Operation.read);
        }

        JSONObject results = (JSONObject) response.getResponsePhrase().orElse(new JSONObject());
        if (results.isEmpty())
            return members;
        JSONArray items = (JSONArray) results.get("items");
        items.forEach(item -> {
            JSONObject datasetObj = (JSONObject) item;
            members.add(datasetObj.get("member").toString());
        });

        return members;
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
        Util.checkNullParameter(params == null, "params is null");
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        Map<String, String> headers = new HashMap<>();
        List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + QueryConstants.QUERY_ID;

        url += ZosFilesConstants.QUERY_DS_LEVEL + Util.encodeURIComponent(dataSetName);

        if (params.getVolume().isPresent()) {
            url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_VOLUME +
                    Util.encodeURIComponent(params.getVolume().get());
        }
        if (params.getStart().isPresent()) {
            url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_START + params.getStart().get();
        }

        Response response = getResponse(params, headers, url);
        if (response.isEmpty())
            return datasets;

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), Arrays.asList(dataSetName), UtilDataset.Operation.read);
        }

        JSONObject results = (JSONObject) response.getResponsePhrase().orElse(new JSONObject());
        if (results.isEmpty())
            return datasets;
        JSONArray items = (JSONArray) results.get(ZosFilesConstants.RESPONSE_ITEMS);
        items.forEach(item -> {
            JSONObject datasetObj = (JSONObject) item;
            datasets.add(UtilDataset.createDatasetObjFromJson(datasetObj));
        });

        return datasets;
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
        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.GET_JSON);
        request.setHeaders(headers);
        return request.executeRequest();
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
            UtilDataset.Attribute attribute = params.getAttribute().get();
            if (attribute == UtilDataset.Attribute.BASE) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
            } else if (attribute == UtilDataset.Attribute.VOL) {
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
