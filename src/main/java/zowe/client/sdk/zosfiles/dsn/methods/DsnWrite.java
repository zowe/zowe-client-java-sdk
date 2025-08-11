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
import zowe.client.sdk.rest.PutTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

/**
 * Provides write dataset and member functionality
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 4.0
 */
public class DsnWrite {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * DsnWrite Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Leonid Baranov
     */
    public DsnWrite(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative DsnWrite constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    DsnWrite(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutTextZosmfRequest)) {
            throw new IllegalStateException("PUT_TEXT request type required");
        }
        this.request = request;
    }

    /**
     * Replaces the content of a member of a partitioned data set (PDS or PDSE) with new content.
     * A new dataset member will be created if the specified dataset member does not exist.
     *
     * @param dataSetName dataset name of where the member is located (e.g. 'DATASET.LIB')
     * @param memberName  name of member to add new content
     * @param content     new content
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Response write(final String dataSetName, final String memberName, final String content)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(dataSetName, "dataSetName");
        ValidateUtils.checkIllegalParameter(memberName, "memberName");

        return write(String.format("%s(%s)", dataSetName, memberName), content);
    }

    /**
     * Replaces the content of an existing sequential data set with new content.
     *
     * @param dataSetName sequential dataset (e.g. 'DATASET.LIB')
     * @param content     new content
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Leonid Baranov
     */
    public Response write(final String dataSetName, final String content) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(dataSetName, "dataSetName");
        ValidateUtils.checkIllegalParameter(content, "content");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" +
                EncodeUtils.encodeURIComponent(dataSetName);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_TEXT);
        }
        request.setUrl(url);
        request.setBody(content);

        return request.executeRequest();
    }

}
