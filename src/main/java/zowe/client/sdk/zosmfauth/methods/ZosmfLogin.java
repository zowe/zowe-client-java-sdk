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

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfauth.ZosmfAuthConstants;
import zowe.client.sdk.zosmfauth.response.ZosmfLoginResponse;

/**
 * Provides z/OSMF authentication login and token service
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-log-in-zosmf-server">z/OSMF REST API </a>
 *
 * @author Esteban Sandoval
 * @author Frank Giordano
 * @version 7.0
 */
public class ZosmfLogin {

    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * ZosmfLogin constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Esteban Sandoval
     */
    public ZosmfLogin(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
    }

    /**
     * Alternative Login constructor with ZosmfRequest object. This is mainly used for internal code
     * unit testing with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link PostJsonZosmfRequest} implementation object
     * @author Esteban Sandoval
     */
    ZosmfLogin(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Request to log into the server and retrieve authentication tokens
     *
     * @return ZosmfLoginResponse object
     * @throws ZosmfRequestException request error state
     * @author Esteban Sandoval
     * @author Frank Giordano
     */
    public ZosmfLoginResponse login() throws ZosmfRequestException {
        final String url = connection.getZosmfUrl() + ZosmfAuthConstants.RESOURCE;

        request.setUrl(url);
        request.setBody("");

        final Response response = request.executeRequest();
        return new ZosmfLoginResponse(response, response.getTokens());
    }

}
