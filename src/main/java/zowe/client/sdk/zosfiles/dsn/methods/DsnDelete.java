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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.dsn.input.DsnDeleteInputData;
import zowe.client.sdk.zosfiles.dsn.types.DeleteType;

/**
 * Provides delete sequential and partitioned data set, member and uncataloged dataset functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=interface-delete-sequential-partitioned-data-set">z/OSMF REST API</a>
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 7.0
 */
public class DsnDelete {

    private static final String BASE_RESOURCE =
            ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + UrlConstants.URL_PATH_DELIM;
    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * DsnDelete Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Leonid Baranov
     */
    public DsnDelete(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
    }

    /**
     * Alternative DsnDelete constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link DeleteJsonZosmfRequest} implementation object
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
     * Delete a sequential and partitioned data set (PDS), a member of a PDS, or an uncataloged dataset on a specific volume.
     *
     * @param deleteInputData delete parameters, see DsnDeleteInputData object
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Jorge Samaniego
     * @author Frank Giordano
     */
    public Response delete(final DsnDeleteInputData deleteInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(deleteInputData, "deleteInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl() + BASE_RESOURCE);
        final String datasetName = EncodeUtils.encodeURIComponent(deleteInputData.getDatasetName());

        switch (deleteInputData.getType()) {
            case DATASET:
                url.append(datasetName);
                break;
            case MEMBER:
                final String memberName = EncodeUtils.encodeURIComponent(deleteInputData.getMemberName());
                url.append(datasetName).append("(").append(memberName).append(")");
                break;
            case UNCATALOGED:
                final String volume = EncodeUtils.encodeURIComponent(deleteInputData.getVolume());
                url.append("-(").append(volume).append(")").append(UrlConstants.URL_PATH_DELIM).append(datasetName);
                break;
            default:
                throw new IllegalArgumentException("unsupported delete type: " + deleteInputData.getType());
        }

        request.setUrl(url.toString());
        return request.executeRequest();
    }

}
