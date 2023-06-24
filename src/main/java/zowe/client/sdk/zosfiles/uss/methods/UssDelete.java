/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonDeleteRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.CreateParams;

/**
 * Provides unix system service delete object functionality
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssDelete {

    private static final Logger LOG = LoggerFactory.getLogger(UssDelete.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssDelete Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author James Kostrewski
     */
    public UssDelete(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssDelete constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public UssDelete(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Perform a delete UNIX object request
     *
     * @param destName name of file or directory with path
     * @param params   delete response parameters, see DeleteParams object
     * @return http response object
     * @throws Exception error processing request
     * @author James Kostrewski
     */
    public Response delete(String destName, CreateParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkNullParameter(destName == null, "destName is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + destName;
        LOG.debug(url);

        if (request == null || !(request instanceof JsonDeleteRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.DELETE_JSON);
        }
        request.setUrl(url);

        return RestUtils.getResponse(request);
    }

}
