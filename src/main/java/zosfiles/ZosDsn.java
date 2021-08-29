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
import org.json.simple.JSONObject;
import rest.*;
import utility.Util;
import utility.UtilDataset;
import utility.UtilRest;
import zosfiles.input.CreateParams;

import java.util.HashMap;
import java.util.Map;

/**
 * ZosDsn class that provides CRUD operations on Datasets
 *
 * @version 1.0
 */
public class ZosDsn {

    private static final Logger LOG = LogManager.getLogger(ZosDsn.class);

    private final ZOSConnection connection;

    /**
     * ZosDsn Constructor
     *
     * @param connection connection information, see ZOSConnection object
     */
    public ZosDsn(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Replaces a content of a dataset or a dataset member with a new content
     * The new dataset member will be created if a dataset member is not exists
     *
     * @param dataSetName dataset or a dataset member (f.e. DATASET.LIB(MEMBER))
     * @param content     new content of the dataset or a dataset member
     */
    public void writeDsn(String dataSetName, String content) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.debug(url);

            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, content,
                    ZoweRequestType.VerbType.PUT_TEXT);
            Response response = request.executeHttpRequest();
            if (response.isEmpty())
                return;

            try {
                UtilRest.checkHttpErrors(response);
            } catch (Exception e) {
                UtilDataset.checkHttpErrors(e.getMessage(), dataSetName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete dataset or a dataset member
     *
     * @param dataSetName name of a dataset or a dataset member (f.e. 'DATASET.LIB(MEMBER)')
     */
    public void deleteDsn(String dataSetName) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.debug(url);

            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, null,
                    ZoweRequestType.VerbType.DELETE_JSON);
            Response response = request.executeHttpRequest();
            if (response.isEmpty())
                return;

            try {
                UtilRest.checkHttpErrors(response);
            } catch (Exception e) {
                UtilDataset.checkHttpErrors(e.getMessage(), dataSetName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new dataset with specified parameters
     *
     * @param dataSetName name of a dataset to create (f.e. 'DATASET.LIB')
     * @param params      create dataset parameters, see CreateParams object
     */
    public void createDsn(String dataSetName, CreateParams params) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName not specified");
        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" + dataSetName;

        try {
            LOG.debug(url);

            String body = buildBody(params);

            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, body,
                    ZoweRequestType.VerbType.POST_JSON);

            Response response = request.executeHttpRequest();
            if (response.isEmpty())
                return;

            try {
                UtilRest.checkHttpErrors(response);
            } catch (Exception e) {
                UtilDataset.checkHttpErrors(e.getMessage(), dataSetName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildBody(CreateParams params) {
        var jsonMap = new HashMap<String, Object>();
        params.getVolser().ifPresent(v -> jsonMap.put("volser", v));
        params.getUnit().ifPresent(v -> jsonMap.put("unit", v));
        params.getDsorg().ifPresent(v -> jsonMap.put("dsorg", v));
        params.getAlcunit().ifPresent(v -> jsonMap.put("alcunit", v));
        params.getPrimary().ifPresent(v -> jsonMap.put("primary", v));
        params.getSecondary().ifPresent(v -> jsonMap.put("secondary", v));
        params.getDirblk().ifPresent(v -> jsonMap.put("dirblk", v));
        params.getAvgblk().ifPresent(v -> jsonMap.put("avgblk", v));
        params.getRecfm().ifPresent(v -> jsonMap.put("recfm", v));
        params.getBlksize().ifPresent(v -> jsonMap.put("blksize", v));
        params.getLrecl().ifPresent(v -> jsonMap.put("lrecl", v));
        params.getStorclass().ifPresent(v -> jsonMap.put("storclass", v));
        params.getStorclass().ifPresent(v -> jsonMap.put("mgntclass", v));
        params.getMgntclass().ifPresent(v -> jsonMap.put("mgntclass", v));
        params.getDataclass().ifPresent(v -> jsonMap.put("dataclass", v));
        params.getDsntype().ifPresent(v -> jsonMap.put("dsntype", v));

        var jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(jsonRequestBody);
        return jsonRequestBody.toString();
    }

}
