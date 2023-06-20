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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.utility.ValidateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides rename dataset and member functionality
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class DsnRename {

    private static final Logger LOG = LoggerFactory.getLogger(DsnRename.class);
    private final ZOSConnection connection;
    private ZoweRequest request;

    /**
     * DsnRename Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public DsnRename(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnRename constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public DsnRename(ZOSConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    public Response member(String dsName, String memberName, String newMemberName) {

        return null;
    }

    public Response dataSet(String dsName, String newDsName) {

        return null;
    }

    private Response rename(String dsName) {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "rename");

        return null;
    }

}
