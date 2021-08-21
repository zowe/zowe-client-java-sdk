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
     * @param connection is a connection object, see ZOSConnection object
     */
    public ZosDsn(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Replaces a content of a dataset or a dataset member with a new content
     * The new dataset member will be created if a dataset member is not exists
     *
     * @param dataSetName is the name of a dataset or a dataset member (f.e. DATASET.LIB(MEMBER))
     * @param content     is a new content of the dataset or a dataset member
     */
    public void writeDsn(String dataSetName, String content) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
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
     * @param dataSetName is the name of a dataset or a dataset member (f.e. 'DATASET.LIB(MEMBER)')
     */
    public void deleteDsn(String dataSetName) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
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
     * @param dataSetName is the name of a dataset to create (f.e. 'DATASET.LIB')
     * @param params      is a dataset parameters
     */
    public void createDsn(String dataSetName, CreateParams params) {
        Util.checkNullParameter(dataSetName == null, "dataSetName is null");
        Util.checkStateParameter(dataSetName.isEmpty(), "dataSetName is empty");
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
        Map<String, Object> reqBody = new HashMap<>();

        params.getVolser().ifPresent(v -> reqBody.put("volser", v));
        params.getUnit().ifPresent(v -> reqBody.put("unit", v));
        params.getDsorg().ifPresent(v -> reqBody.put("dsorg", v));
        params.getAlcunit().ifPresent(v -> reqBody.put("alcunit", v));
        params.getPrimary().ifPresent(v -> reqBody.put("primary", v));
        params.getSecondary().ifPresent(v -> reqBody.put("secondary", v));
        params.getDirblk().ifPresent(v -> reqBody.put("dirblk", v));
        params.getAvgblk().ifPresent(v -> reqBody.put("avgblk", v));
        params.getRecfm().ifPresent(v -> reqBody.put("recfm", v));
        params.getBlksize().ifPresent(v -> reqBody.put("blksize", v));
        params.getLrecl().ifPresent(v -> reqBody.put("lrecl", v));
        params.getStorclass().ifPresent(v -> reqBody.put("storclass", v));
        params.getStorclass().ifPresent(v -> reqBody.put("mgntclass", v));
        params.getMgntclass().ifPresent(v -> reqBody.put("mgntclass", v));
        params.getDataclass().ifPresent(v -> reqBody.put("dataclass", v));
        params.getDsntype().ifPresent(v -> reqBody.put("dsntype", v));

        JSONObject req = new JSONObject(reqBody);
        LOG.debug(req);
        return req.toString();
    }

}
