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

/**
 * Provides copy dataset and dataset member functionality
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class ZosDsnCopy {

    private static final Logger LOG = LogManager.getLogger(ZosDsnCopy.class);

    private final ZOSConnection connection;

    /**
     * ZosDsnCopy constructor
     *
     * @param connection is a connection, see ZOSConnection object
     * @author Leonid Baranov
     */
    public ZosDsnCopy(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Copy dataset or dataset member
     *
     * @param params contains copy dataset parameters
     * @return http response object
     * @author Leonid Baranov
     */
    public Response copy(CopyParams params) {
        Util.checkConnection(connection);
        Util.checkNullParameter(params == null, "params is null");
        Util.checkIllegalParameter(params.getFromDataSet().isEmpty(), "fromDataSetName not specified");
        Util.checkIllegalParameter(params.getToDataSet().isEmpty(), "toDataSetName not specified");
        UtilDataset.checkDatasetName(params.getFromDataSet().get(), true);
        UtilDataset.checkDatasetName(params.getToDataSet().get(), true);

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getToVolser().isPresent()) {
            url += "-(" + params.getToVolser().get() + ")/";
        }

        String toDataSet = params.getToDataSet().get();

        url += Util.encodeURIComponent(toDataSet);

        Response response = new Response(null, null);
        try {
            LOG.debug(url);

            String body = buildBody(params);
            ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, body,
                    ZoweRequestType.VerbType.PUT_JSON);
            response = request.executeHttpRequest();

            try {
                UtilRest.checkHttpErrors(response);
            } catch (Exception e) {
                UtilDataset.checkHttpErrors(e.getMessage(), toDataSet, "copy");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Copy dataset or dataset member
     *
     * @param fromDataSetName is a name of source dataset (e.g. SOURCE.DATASET(MEMBER))
     * @param toDataSetName   is a name of target dataset (e.g. TARGET.DATASET(MEMBER))
     * @param replace         if true, members in the target dataset are replaced. if false,
     * @return http response object
     * @author Leonid Baranov
     */
    public Response copy(String fromDataSetName, String toDataSetName, boolean replace) {
        return copy(new CopyParams.Builder()
                .fromDataSet(fromDataSetName)
                .toDataSet(toDataSetName)
                .replace(replace)
                .build());
    }

    /**
     * Build the Json body for the copy request
     *
     * @param params CopyParams object
     * @return json string
     * @author Leonid Baranov
     */
    private String buildBody(CopyParams params) {
        String fromDataSetName = Util.encodeURIComponent(params.getFromDataSet().get());

        var jsonMap = new HashMap<String, Object>();
        jsonMap.put("request", "copy");

        String member = "*";
        int startMemberIndex = fromDataSetName.indexOf("(");
        if (startMemberIndex > 0) {
            member = fromDataSetName.substring(startMemberIndex + 1, fromDataSetName.length() - 1);
            fromDataSetName = fromDataSetName.substring(0, startMemberIndex);
        }

        var fromDataSetReq = new HashMap<String, Object>();
        fromDataSetReq.put("dsn", fromDataSetName);
        fromDataSetReq.put("member", member);
        JSONObject fromDataSetObj = new JSONObject(fromDataSetReq);

        jsonMap.put("from-dataset", fromDataSetObj);
        jsonMap.put("replace", params.isReplace());

        if (params.getFromVolser().isPresent()) {
            jsonMap.put("volser", params.getFromVolser());
        }

        var jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(jsonRequestBody);
        return jsonRequestBody.toString();
    }

}
