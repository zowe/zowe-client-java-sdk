/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.methods;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfinfo.ZosmfConstants;
import zowe.client.sdk.zosmfinfo.response.ZosmfSystemsResponse;

/**
 * This class is used to list the systems defined to z/OSMF through the z/OSMF APIs.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class ZosmfSystems {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * ListDefinedSystems Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public ZosmfSystems(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative ListDefinedSystems constructor with ZoweRequest object.
     * This is mainly used for internal code unit testing with mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    ZosmfSystems(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * List systems defined to z/OSMF
     *
     * @return ZosmfListDefinedSystemsResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public ZosmfSystemsResponse get() throws ZosmfRequestException {
        final String url = connection.getZosmfUrl() +
                ZosmfConstants.TOPOLOGY +
                ZosmfConstants.SYSTEMS;

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no z/osmf info response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, ZosmfSystemsResponse.class, "get");
    }

}
