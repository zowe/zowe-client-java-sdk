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
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.IZoweRequest;
import rest.JsonRequest;
import rest.QueryConstants;
import rest.ZosmfHeaders;
import utility.Util;
import utility.UtilDataset;
import zosfiles.input.ListParams;
import zosfiles.response.Dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class List {

    private static final Logger LOG = LogManager.getLogger(List.class);

    public static java.util.List<Dataset> listMembers(ZOSConnection connection, String dataSetName, ListParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        java.util.List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES  + "/" + dataSetName + ZosFilesConstants.RES_DS_MEMBERS;
        try {
            if (options.getPattern().isPresent()) {
                url += QueryConstants.QUERY_ID + ZosFilesConstants.QUERY_PATTERN + options.getPattern().get();
            }
            String key, value;
            Map<String, String> headers = new HashMap<>();
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
                headers.put(key, value);
            } else {
                key = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
                headers.put(key, value);
            }
            if (options.getResponseTimeout().isPresent()) {
                key = ZosmfHeaders.HEADERS.get("X_IBM_RESPONSE_TIMEOUT").get(0);
                value = options.getResponseTimeout().get();
                headers.put(key, value);
            }
            LOG.info(url);

            IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
            request.setHeaders(headers);
            JSONObject results = request.httpGet();
            JSONArray items = (JSONArray) results.get("items");
            items.forEach(item -> {
                JSONObject datasetObj = (JSONObject) item;
                datasets.add(UtilDataset.createDatasetObjFromJson(datasetObj));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datasets;
    }

    public static java.util.List<Dataset> listDsn(ZOSConnection connection, String dataSetName, ListParams options) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        java.util.List<Dataset> datasets = new ArrayList<>();
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
            String key, value;
            Map<String, String> headers = new HashMap<>();
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
                headers.put(key, value);
            } else {
                key = ZosmfHeaders.HEADERS.get("X_IBM_MAX_ITEMS").get(0);
                value = ZosmfHeaders.HEADERS.get("X_IBM_ATTRIBUTES_BASE").get(1);
                headers.put(key, value);
            }
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
            LOG.info(url);

            IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
            request.setHeaders(headers);
            JSONObject results = request.httpGet();
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
}
