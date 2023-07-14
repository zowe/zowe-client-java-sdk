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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.TextPutRequest;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

/**
 * Provides write dataset and member functionality
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class DsnWrite {

    private static final Logger LOG = LoggerFactory.getLogger(DsnWrite.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * DsnWrite Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Leonid Baranov
     */
    public DsnWrite(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnWrite constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public DsnWrite(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
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
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public Response write(String dataSetName, String memberName, String content) throws Exception {
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");
        ValidateUtils.checkNullParameter(memberName == null, "memberName is null");
        ValidateUtils.checkIllegalParameter(memberName.isEmpty(), "memberName not specified");
        return write(String.format("%s(%s)", dataSetName, memberName), content);
    }

    /**
     * Replaces the content of an existing sequential data set with new content.
     *
     * @param dataSetName sequential dataset (e.g. 'DATASET.LIB')
     * @param content     new content
     * @return http response object
     * @throws Exception error processing request
     * @author Leonid Baranov
     */
    public Response write(String dataSetName, String content) throws Exception {
        ValidateUtils.checkNullParameter(content == null, "content is null");
        ValidateUtils.checkNullParameter(dataSetName == null, "dataSetName is null");
        ValidateUtils.checkIllegalParameter(dataSetName.isEmpty(), "dataSetName not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_DS_FILES + "/" + EncodeUtils.encodeURIComponent(dataSetName);

        LOG.debug(url);

        if (request == null || !(request instanceof TextPutRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_TEXT);
        }
        request.setUrl(url);
        request.setBody(content);

        return RestUtils.getResponse(request);
    }

}
