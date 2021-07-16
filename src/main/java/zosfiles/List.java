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

import java.io.IOException;
import java.util.*;

import core.ZOSConnection;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.IZoweRequest;
import rest.JsonRequest;
import utility.Util;
import utility.UtilDataset;
import utility.UtilJobs;
import zosfiles.constants.ZosFilesConstants;
import zosfiles.doc.ListOptions;
import zosfiles.response.Dataset;
import zosjobs.JobsConstants;


import java.util.ArrayList;

public class List {

    private static final Logger LOG = LogManager.getLogger(List.class);

    public static  void listDsn(ZOSConnection connection, String dataSetName, ListOptions options ) throws IOException {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
        Util.checkConnection(connection);

        java.util.List<Dataset> datasets = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + ZosFilesConstants.RES_DS_MEMBERS;
        try {
            if (options.getVolume().isPresent()) {
                url += "&volser" + options.getPattern().get();
            }
            if (options.getStart().isPresent()) {
                url += "&volser" + options.getStart().get();
            }
            if (options.getAttributes().isPresent()) {
                if (parms.getPrefix().get() != JobsConstants.DEFAULT_PREFIX) {
                    if (url.contains(JobsConstants.QUERY_ID)) {
                        url += JobsConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_PREFIX + parms.getPrefix().get();
                }
            }
            if (options.getMaxLength().isPresent()) {
                if (parms.getMaxJobs().get() != JobsConstants.DEFAULT_MAX_JOBS) {
                    if (url.contains(JobsConstants.QUERY_ID)) {
                        url += JobsConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_MAX_JOBS + parms.getMaxJobs().get();
                }
            }
            if (options.getResponseTimeout().isPresent()) {
                if (url.contains(JobsConstants.QUERY_ID)) {
                    url += JobsConstants.COMBO_ID;
                }
                url += JobsConstants.QUERY_JOBID + parms.getJobId().get();
            }
        }


        LOG.debug(url);

        IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
        JSONArray results = request.httpGet();
        results.forEach(item -> {
            JSONObject datasetObj = (JSONObject) item;
            datasets.add(UtilDataset.createDatasetObjFromJson(datasetObj));
        });

        return ;
    }
}
