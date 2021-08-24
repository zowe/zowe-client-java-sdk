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
import rest.Response;
import rest.ZoweRequest;
import rest.ZoweRequestFactory;
import rest.ZoweRequestType;
import utility.Util;
import utility.UtilDataset;
import utility.UtilRest;
import zosfiles.input.CopyParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides copy dataset and dataset member functionality
 *
 * @version 1.0
 */
public class ZosDsnCopy {

    private static final Logger LOG = LogManager.getLogger(ZosDsnCopy.class);

    private final ZOSConnection connection;

    /**
     * ZosDsnCopy constructor
     *
     * @param connection is a connection, see ZOSConnection object
     */
    public ZosDsnCopy(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Copy dataset or dataset member
     *
     * @param params contains copy dataset parameters
     */
    public void copy(CopyParams params) {
        Util.checkNullParameter(params.getFromDataSet().isEmpty(), "fromDataSetName is null");
        Util.checkNullParameter(params.getToDataSet().isEmpty(), "toDataSetName is null");

        Util.checkConnection(connection);

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getToVolser().isPresent()) {
            url += "-(" + params.getToVolser().get() + ")/";
        }

        url += params.getToDataSet().get();

        try {
            LOG.info(url);

            String body = buildBody(params);

            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, body,
                    ZoweRequestType.VerbType.PUT_JSON);

            Response response = request.executeHttpRequest();

            try {
                UtilRest.checkHttpErrors(response);
            } catch (Exception e) {
                UtilDataset.checkHttpErrors(e.getMessage(), params.getFromDataSet().get());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Copy dataset or dataset member
     *
     * @param fromDataSetName is a name of source dataset (i.e. SOURCE.DATASET(MEMBER))
     * @param toDataSetName   is a name of target dataset (i.e. TARGET.DATASET(MEMBER))
     * @param replace         if true, members in the target dataset are replaced. if false,
     *                        like named members are not copied and an error is returned.
     */
    public void copy(String fromDataSetName, String toDataSetName, boolean replace) {
        copy(new CopyParams.Builder()
                .fromDataSet(fromDataSetName)
                .toDataSet(toDataSetName)
                .replace(replace)
                .build());
    }

    private String buildBody(CopyParams params) {
        String fromDataSetName = params.getFromDataSet().get();

        Map<String, Object> req = new HashMap<>();
        req.put("request", "copy");

        String member = "*";
        int startMemberIndex = fromDataSetName.indexOf("(");
        if (startMemberIndex > 0) {
            member = fromDataSetName.substring(startMemberIndex + 1, fromDataSetName.length() - 1);
            fromDataSetName = fromDataSetName.substring(0, startMemberIndex);
        }

        Map<String, Object> fromDataSetReq = new HashMap<>();
        fromDataSetReq.put("dsn", fromDataSetName);
        fromDataSetReq.put("member", member);
        JSONObject fromDataSetObj = new JSONObject(fromDataSetReq);

        req.put("from-dataset", fromDataSetObj);
        req.put("replace", params.isReplace());

        if (params.getFromVolser().isPresent()) {
            req.put("volser", params.getFromVolser());
        }

        JSONObject reqBody = new JSONObject(req);
        LOG.debug(reqBody);
        return reqBody.toString();
    }

}
