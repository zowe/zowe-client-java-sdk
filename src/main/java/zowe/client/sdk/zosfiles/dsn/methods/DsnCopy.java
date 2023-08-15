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
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
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
     * If copyAllMembers parameter value sent as true it will perform a copy of all members in
     * source partition dataset to the target partition dataset if no member name included in the
     * source partition dataset value (fromDataSetName).
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
        return copyCommon(new CopyParams.Builder()
                .fromDataSet(fromDataSetName)
                .toDataSet(toDataSetName)
                .replace(replace)
                .copyAllMembers(copyAllMembers)
                .build());
    }

    /**
     * Copy dataset or dataset member request driven by CopyParams object settings
     *
     * @param params contains copy dataset parameters
     * @return http response object
     * @throws Exception error processing copy request
     * @author Leonid Baranov
     * @author Frank Giordano
     */
    public Response copyCommon(CopyParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = setUrl(params);
        // build the body key-value pairs needed for the request
        final Map<String, Object> fromDataSetMap = setFromDataSetMapValues(params);
        final Map<String, Object> copyMap = new HashMap<>();
        copyMap.put("request", "copy");
        copyMap.put("from-dataset", new JSONObject(fromDataSetMap));
        copyMap.put("replace", params.isReplace());

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(copyMap).toString());

        return RestUtils.getResponse(request);
    }

    /**
     * Return url string value formulated by the given CopyParams object
     *
     * @param params CopyParams object
     * @return url string value
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private String setUrl(CopyParams params) throws Exception {
        final String toDataSetNameErrMsg = "toDataSetName not specified";
        final String toDataSet = params.getToDataSet()
                .orElseThrow(() -> new IllegalArgumentException(toDataSetNameErrMsg));

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/";
        if (params.getToVolser().isPresent()) {
            url += "-(" + params.getToVolser().get() + ")/";
        }
        url += EncodeUtils.encodeURIComponent(toDataSet);
        return url;
    }

    /**
     * Return a hashmap contains key-value pairs for from-dataset parent json value
     * <p>
     * Keys to be considered: dsn, member, and volser
     *
     * @param params CopyParams object
     * @return map containing key-value pairs for the from-dataset parent json value
     * @throws Exception processing error
     * @author Frank Giordano
     */
    private Map<String, Object> setFromDataSetMapValues(CopyParams params) throws Exception {
        final String fromDataSetNameErrMsg = "fromDataSetName not specified";
        String fromDataSetName = params.getFromDataSet()
                .orElseThrow(() -> new IllegalStateException(fromDataSetNameErrMsg));

        final Map<String, Object> fromDataSetReq = new HashMap<>();
        // is member name specified in DataSet value
        if (isMemberNameIncluded(fromDataSetName)) {
            // member exist extract it
            final int startMemberIndex = fromDataSetName.indexOf("(");
            String member = fromDataSetName.substring(startMemberIndex + 1, fromDataSetName.length() - 1);
            fromDataSetReq.put("member", member);
            // reassign fromDataSetName to dataSet value without member
            fromDataSetName = fromDataSetName.substring(0, startMemberIndex);
        } else if (params.isCopyAllMembers()) {
            // no member specified copy all members as such
            fromDataSetReq.put("member", "*");
        }
        fromDataSetReq.put("dsn", fromDataSetName);
        if (params.getFromVolser().isPresent()) {
            fromDataSetReq.put("volser", params.getFromVolser().get());
        }
        return fromDataSetReq;
    }

    /**
     * Is a member name included in fromDataSetName string value
     *
     * @param fromDataSetName string value representing a data set notation
     * @return true or false boolean value
     * @author Frank Giordano
     */
    private boolean isMemberNameIncluded(String fromDataSetName) {
        return fromDataSetName.indexOf("(") > 0;
    }

}
