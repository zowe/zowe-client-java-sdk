/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.type.ZosmfRequestType;

/**
 * Zowe request factory that generates the desire CRUD operation
 *
 * @author Frank Giordano
 * @version 4.0
 */
public final class ZosmfRequestFactory {

    private static final Logger LOG = LoggerFactory.getLogger(ZosmfRequestFactory.class);

    /**
     * Private constructor defined to avoid instantiation of class
     */
    private ZosmfRequestFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Assign the request to the Http verb type request object
     *
     * @param connection connection information, see ZosConnection object
     * @param type       request http type, see ZosmfRequestType object
     * @return ZosmfRequest abstract object of ZosmfRequestType value
     * @author Frank Giordano
     */
    public static ZosmfRequest buildRequest(final ZosConnection connection, final ZosmfRequestType type) {
        LOG.debug(type.name());
        ZosmfRequest request;
        switch (type) {
            case GET_JSON:
                request = new GetJsonZosmfRequest(connection);
                break;
            case PUT_JSON:
                request = new PutJsonZosmfRequest(connection);
                break;
            case POST_JSON:
                request = new PostJsonZosmfRequest(connection);
                break;
            case DELETE_JSON:
                request = new DeleteJsonZosmfRequest(connection);
                break;
            case GET_TEXT:
                request = new GetTextZosmfRequest(connection);
                break;
            case PUT_TEXT:
                request = new PutTextZosmfRequest(connection);
                break;
            case GET_STREAM:
                request = new GetStreamZosmfRequest(connection);
                break;
            case PUT_STREAM:
                request = new PutStreamZosmfRequest(connection);
                break;
            default:
                throw new IllegalStateException("no valid ZoweRequestType type specified");
        }
        return request;
    }

}
