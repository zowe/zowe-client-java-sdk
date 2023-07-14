/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.methods;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.CopyParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides copy dataset and dataset member functionality
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class DsnCopy {

    private static final Logger LOG = LoggerFactory.getLogger(DsnCopy.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * DsnCopy constructor
     *
     * @param connection is a connection, see ZOSConnection object
     * @author Leonid Baranov
     */
    public DsnCopy(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnCopy constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public DsnCopy(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
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
        final boolean isFullPartitionCopy = params.isCopyAllMembers();

        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "copy");

        String member = "";
        // does fromDataSetName contain a member if so extract it and include member field and value in the json body
        final int startMemberIndex = fromDataSetName.indexOf("(");
        if (startMemberIndex > 0) {
            member = fromDataSetName.substring(startMemberIndex + 1, fromDataSetName.length() - 1);
            fromDataSetName = fromDataSetName.substring(0, startMemberIndex);
        }

        final Map<String, Object> fromDataSetReq = new HashMap<>();
        fromDataSetReq.put("dsn", fromDataSetName);
        if (member.length() > 0) { // include a member if it was specified in fromDataSetName
            fromDataSetReq.put("member", member);
        } else if (isFullPartitionCopy) {  // if true indicates a copy of all members in partition dataset to another
            fromDataSetReq.put("member", "*");
        }

        final JSONObject fromDataSetObj = new JSONObject(fromDataSetReq);

        jsonMap.put("from-dataset", fromDataSetObj);
        jsonMap.put("replace", params.isReplace());

        if (params.getFromVolser().isPresent()) {
            jsonMap.put("volser", params.getFromVolser().get());
        }

        final JSONObject jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));
        return jsonRequestBody.toString();
    }

    /**
     * This copy method allows the following copy operations:
     * <p>
     * - sequential dataset to sequential dataset
     * - sequential dataset to partition dataset member
     * - partition dataset member to partition dataset member
     * - partition dataset member to partition dataset non-existing member
     * - partition dataset member to sequential dataset
     * <p>
     * If copyAllMembers parameter value sent as true it will perform a copy of all
     * members in source partition dataset to another partition dataset.
     *
     * @param fromDataSetName is a name of source dataset (e.g. 'SOURCE.DATASET' or 'SOURCE.DATASET(MEMBER)')
     * @param toDataSetName   is a name of target dataset (e.g. 'TARGET.DATASET' or 'TARGET.DATASET(MEMBER)')
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
     * Copy dataset or dataset member
     *
     * @param params contains copy dataset parameters
     * @return http response object
     * @throws Exception error processing copy request
     * @author Leonid Baranov
     */
    public Response copy(CopyParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getFromDataSet().isEmpty(), "fromDataSetName not specified");
        ValidateUtils.checkIllegalParameter(params.getToDataSet().isEmpty(), "toDataSetName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";

        if (params.getToVolser().isPresent()) {
            url += "-(" + params.getToVolser().get() + ")/";
        }

        final String toDataSet = params.getToDataSet().get();

        url += EncodeUtils.encodeURIComponent(toDataSet);

        LOG.debug(url);

        final String body = buildBody(params);
        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        return RestUtils.getResponse(request);
    }

}
