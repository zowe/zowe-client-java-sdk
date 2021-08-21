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
 * @version 1.0
 */
public class ZosDsnList {

    private static final Logger LOG = LogManager.getLogger(ZosDsnList.class);

    private final ZOSConnection connection;

    /**
     * ZosDsnList constructor
     *
     * @param connection connection object, see ZOSConnection object
     * @author Frank Giordano
     */
    public ZosDsnList(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Get a list of members from a Dataset
     *
     * @param dataSetName name of a dataset (i.e. 'DATASET.LIB')
     * @param options     list options, see ListParams object
     * @return A String list of member names
     */
    @SuppressWarnings("unchecked")
    public List<String> listMembers(String dataSetName, ListParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        Map<String, String> headers = new HashMap<>();
        List<String> members = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/"
                + dataSetName + ZosFilesConstants.RES_DS_MEMBERS;
        try {
            if (options.getPattern().isPresent()) {
                url += QueryConstants.QUERY_ID + ZosFilesConstants.QUERY_PATTERN + options.getPattern().get();
            }

            Response response = getResponse(options, headers, url);
            if (response.isEmpty())
                return members;

            try {
                UtilRest.checkHttpErrors(response);
            } catch (Exception e) {
                UtilDataset.checkHttpErrors(e.getMessage(), dataSetName);
            }

            JSONObject results = (JSONObject) response.getResponsePhrase().orElse(new JSONObject());
            if (results.isEmpty())
                return members;
            JSONArray items = (JSONArray) results.get("items");
            items.forEach(item -> {
                JSONObject datasetObj = (JSONObject) item;
                members.add(datasetObj.get("member").toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return members;
    }

    /**
     * Get a list of Dataset names
     *
     * @param dataSetName name of a dataset (i.e. 'DATASET.LIB')
     * @param options     list options parameters, see ListParams object
     * @return A String list of Dataset names
     */
    @SuppressWarnings("unchecked")
    public List<Dataset> listDsn(String dataSetName, ListParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        Map<String, String> headers = new HashMap<>();
        List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + QueryConstants.QUERY_ID;

        try {
            url += ZosFilesConstants.QUERY_DS_LEVEL + dataSetName;

            if (options.getVolume().isPresent()) {
                url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_VOLUME + options.getVolume().get();
            }
            if (options.getStart().isPresent()) {
                url += QueryConstants.COMBO_ID + ZosFilesConstants.QUERY_START + options.getStart().get();
            }

            Response response = getResponse(options, headers, url);
            if (response.isEmpty())
                return datasets;

            try {
                UtilRest.checkHttpErrors(response);
            } catch (Exception e) {
                UtilDataset.checkHttpErrors(e.getMessage(), dataSetName);
            }

            JSONObject results = (JSONObject) response.getResponsePhrase().orElse(new JSONObject());
            if (results.isEmpty())
                return datasets;
            JSONArray items = (JSONArray) results.get(ZosFilesConstants.RESPONSE_ITEMS);
            items.forEach(item -> {
                JSONObject datasetObj = (JSONObject) item;
                datasets.add(UtilDataset.createDatasetObjFromJson(datasetObj));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datasets;
    }

    private Response getResponse(ListParams options, Map<String, String> headers, String url) throws Exception {
        LOG.debug(url);
        setHeaders(options, headers);
        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                ZoweRequestType.VerbType.GET_JSON);
        request.setAdditionalHeaders(headers);
        return request.executeHttpRequest();
    }

    private void setHeaders(ListParams options, Map<String, String> headers) {
        String key, value;
        key = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(0);
        value = ZosmfHeaders.HEADERS.get("ACCEPT_ENCODING").get(1);
        headers.put(key, value);

        if (options.getAttributes().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
            headers.put(key, value);
        }
        if (options.getMaxLength().isPresent()) {
            key = "X-IBM-Max-Items";
            value = options.getMaxLength().get();
        } else {
            key = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(0);
            value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
        }
        headers.put(key, value);
        if (options.getResponseTimeout().isPresent()) {
            key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
            value = options.getResponseTimeout().get();
            headers.put(key, value);
        }
        if (options.getRecall().isPresent()) {
            switch (options.getRecall().get().toLowerCase(Locale.ROOT)) {
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
