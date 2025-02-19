/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfauth.methods;

import kong.unirest.core.Cookies;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfauth.ZosmfAuthConstants;

/**
 * Provides z/OSMF authentication logout service
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=services-log-out-zosmf-server">z/OSMF REST API </a>
 *
 * @author Esteban Sandoval
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfLogout {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /**
     * Logout constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Esteban Sandoval
     */
    public ZosmfLogout(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative Logout constructor with ZosmfRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Esteban Sandoval
     */
    public ZosmfLogout(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof DeleteJsonZosmfRequest)) {
            throw new IllegalStateException("DELETE_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Request to log out of server and delete authentication token
     *
     * @param cookies Cookies Object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Esteban Sandoval
     * @author Frank Giordano
     */
    public Response logout(Cookies cookies) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(cookies == null, "cookies is null");
        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosmfAuthConstants.RESOURCE;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.DELETE_JSON);
        }
        request.setUrl(url);
        request.setCookie(cookies.get(0));

        return request.executeRequest();
    }

}
