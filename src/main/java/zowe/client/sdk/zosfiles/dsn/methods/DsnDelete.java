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
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.DeleteMigratedInputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides delete dataset and member functionality
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnDelete {

    private final ZosConnection connection;
    private ZosmfRequest request;
    private ZosmfRequest putRequest;

    /**
     * DsnDelete Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Leonid Baranov
     */
    public DsnDelete(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative DsnDelete constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    DsnDelete(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Alternative DsnDelete constructor with ZoweRequest objects. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object for delete
     * @param putRequest any compatible ZoweRequest Interface object for put
     * @author Frank Giordano
     */
    DsnDelete(final ZosConnection connection, final ZosmfRequest request, final ZosmfRequest putRequest) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        ValidateUtils.checkNullParameter(putRequest, "putRequest");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        if (!(putRequest instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
        this.putRequest = putRequest;
    }

    /**
     * Delete a dataset member
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @param memberName  name of member to delete
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response delete(final String dataSetName, final String memberName) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(dataSetName, "dataSetName");
        ValidateUtils.checkIllegalParameter(memberName, "memberName");

        return delete(String.format("%s(%s)", dataSetName, memberName));
    }

    /**
     * Delete a dataset
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Leonid Baranov
     */
    public Response delete(final String dataSetName) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(dataSetName, "dataSetName");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" +
                EncodeUtils.encodeURIComponent(dataSetName);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }
        request.setUrl(url);

        return request.executeRequest();
    }

    /**
     * Delete a migrated dataset
     *
     * @param dataSetName name of a dataset (e.g. 'DATASET.LIB')
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response deleteMigrated(final String dataSetName) throws ZosmfRequestException {
        return deleteMigrated(dataSetName, null);
    }

    /**
     * Delete a migrated dataset with optional parameters
     *
     * @param dataSetName             name of a dataset (e.g. 'DATASET.LIB')
     * @param deleteMigratedInputData delete migrated input parameters, see DeleteMigratedInputData object
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    public Response deleteMigrated(final String dataSetName, final DeleteMigratedInputData deleteMigratedInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(dataSetName, "dataSetName");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" +
                EncodeUtils.encodeURIComponent(dataSetName);

        final Map<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("request", "hdelete");
        if (deleteMigratedInputData != null) {
            deleteMigratedInputData.getWait().ifPresent(v -> deleteMap.put("wait", v));
            deleteMigratedInputData.getPurge().ifPresent(v -> deleteMap.put("purge", v));
        }

        if (putRequest == null) {
            putRequest = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        putRequest.setUrl(url);
        putRequest.setBody(new JSONObject(deleteMap).toString());

        return putRequest.executeRequest();
    }

}
