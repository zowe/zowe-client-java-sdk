/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zowe.client.sdk.zosfiles.dsn.methods;

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides rename dataset and member functionality
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class DsnRename {

    private final ZosConnection connection;

    private ZosmfRequest request;

    private String url;

    /**
     * DsnRename Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public DsnRename(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnRename constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public DsnRename(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
    }

    /**
     * Change the existing dataset name (source) to a new dataset name (destination)
     *
     * @param dataSetName    existing dataset name
     * @param newDataSetName new dataset name
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response dataSetName(final String dataSetName, final String newDataSetName) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isBlank(), "dataSetName not specified");
        ValidateUtils.checkNullParameter(newDataSetName == null, "newDataSetName is null");
        ValidateUtils.checkIllegalParameter(newDataSetName.isBlank(), "newDataSetName not specified");

        setUrl(newDataSetName);
        return executeCommon(dataSetName);
    }

    /**
     * Change the existing member name (source) to a new member name (destination) within a partition dataset
     *
     * @param fromDataSetName from dataset name
     * @param memberName      existing member name
     * @param newMemberName   new member name
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response memberName(final String fromDataSetName, final String memberName, final String newMemberName)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(fromDataSetName == null, "fromDataSetName is null");
        ValidateUtils.checkIllegalParameter(fromDataSetName.isBlank(), "fromDataSetName not specified");
        ValidateUtils.checkNullParameter(memberName == null, "memberName is null");
        ValidateUtils.checkIllegalParameter(memberName.isBlank(), "memberName not specified");
        ValidateUtils.checkNullParameter(newMemberName == null, "newMemberName is null");
        ValidateUtils.checkIllegalParameter(newMemberName.isBlank(), "newMemberName not specified");

        setUrl(fromDataSetName, newMemberName);
        return executeCommon(fromDataSetName, memberName);
    }

    /**
     * Set the global url value
     *
     * @param args new or current dataset name and/or new member name
     * @author Frank Giordano
     */
    private void setUrl(final String... args) {
        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + EncodeUtils.encodeURIComponent(args[0]);
        if (args.length > 1) {
            url += "(" + EncodeUtils.encodeURIComponent(args[1]) + ")";
        }
    }

    /**
     * Build and execute the request and return the response
     *
     * @param args at most two string arguments:
     *             one given source dataSet name to be renamed or
     *             one given source dataSet name where the member to be renamed exists, second member name to rename
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private Response executeCommon(final String... args) throws ZosmfRequestException {
        final Map<String, Object> renameMap = new HashMap<>();
        renameMap.put("request", "rename");

        final Map<String, Object> fromDataSetReq = new HashMap<>();
        fromDataSetReq.put("dsn", args[0]);
        if (args.length > 1) {
            fromDataSetReq.put("member", args[1]);
        }

        final JSONObject fromDataSetObj = new JSONObject(fromDataSetReq);
        renameMap.put("from-dataset", fromDataSetObj);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(renameMap).toString());

        return request.executeRequest();
    }

}
