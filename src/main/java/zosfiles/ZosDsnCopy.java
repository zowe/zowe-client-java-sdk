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
import utility.Crud;
import utility.Util;
import utility.UtilDataset;
import utility.UtilRest;
import zosfiles.input.CopyParams;

import java.util.Arrays;
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
        Util.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Copy dataset or dataset member
     *
     * @param params contains copy dataset parameters
     * @return http response object
     * @throws Exception error processing copy request
     * @author Leonid Baranov
     */
    public Response copy(CopyParams params) throws Exception {
        Util.checkNullParameter(params == null, "params is null");
        Util.checkIllegalParameter(params.getFromDataSet().isEmpty(), "fromDataSetName not specified");
        Util.checkIllegalParameter(params.getToDataSet().isEmpty(), "toDataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getToVolser().isPresent()) {
            url += "-(" + params.getToVolser().get() + ")/";
        }

        String toDataSet = params.getToDataSet().get();
        String fromDataSet = params.getFromDataSet().get();

        url += Util.encodeURIComponent(toDataSet);

        LOG.debug(url);

        String body = buildBody(params);
        ZoweRequest request = ZoweRequestFactory.buildRequest(connection, url, body, ZoweRequestType.VerbType.PUT_JSON);
        Response response = request.executeHttpRequest();

        try {
            UtilRest.checkHttpErrors(response);
        } catch (Exception e) {
            UtilDataset.checkHttpErrors(e.getMessage(), Arrays.asList(toDataSet, fromDataSet), Crud.type.copy);
        }

        return response;
    }

    /**
     * This copy method allows the following copy operations:
     * <p>
     * - sequential dataset to sequential dataset
     * - sequential dataset to partition dataset member
     * - partition dataset member to partition dataset member
     * - partition dataset member to partition dataset non-existing member
     * <p>
     * If copyAllMembers parameter value sent as true it will perform a copy of all
     * members in source partition dataset to another partition dataset.
     *
     * @param fromDataSetName is a name of source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   is a name of target dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param replace         if true members in the target dataset are replaced
     * @param copyAllMembers  if true copy all members in source partition dataset specified
     * @return http response object
     * @throws Exception error processing copy request
     * @author Leonid Baranov
     */
    public Response copy(String fromDataSetName, String toDataSetName, boolean replace, boolean copyAllMembers)
            throws Exception {
        return copy(new CopyParams.Builder()
                .fromDataSet(fromDataSetName)
                .toDataSet(toDataSetName)
                .replace(replace)
                .copyAllMembers(copyAllMembers)
                .build());
    }

    /**
     * Build the Json body for the copy request
     *
     * @param params CopyParams object
     * @return json string
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    private String buildBody(CopyParams params) throws Exception {
        String fromDataSetName = params.getFromDataSet().orElseThrow(() -> new Exception("dataset not specified"));
        boolean isFullPartitionCopy = params.isCopyAllMembers();

        var jsonMap = new HashMap<String, Object>();
        jsonMap.put("request", "copy");

        String member = "";
        // does fromDataSetName contain a member if so extract it and include member field and value in the json body
        int startMemberIndex = fromDataSetName.indexOf("(");
        if (startMemberIndex > 0) {
            member = fromDataSetName.substring(startMemberIndex + 1, fromDataSetName.length() - 1);
            fromDataSetName = fromDataSetName.substring(0, startMemberIndex);
        }

        var fromDataSetReq = new HashMap<String, Object>();
        fromDataSetReq.put("dsn", fromDataSetName);
        if (member.length() > 0) // include a member if it was specified in fromDataSetName
            fromDataSetReq.put("member", member);
        else if (isFullPartitionCopy) // if true indicates a copy of all members in partition dataset to another
            fromDataSetReq.put("member", "*");

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
