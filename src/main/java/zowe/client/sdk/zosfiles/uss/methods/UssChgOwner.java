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

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.ChgOwnerParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameter container class for Unix System Services (USS) chown operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssChgOwner {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssChgOwner constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssChgOwner(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssChgOwner constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     */
    public UssChgOwner(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Performs the chown operation
     *
     * @param path  path to the file or directory to be changed
     * @param owner new owner of the file or directory
     * @return Response object
     * @throws Exception processing error
     */
    public Response changeOwner(String path, String owner) throws Exception {
        return changeOwner(path, new ChgOwnerParams.Builder().owner(owner).build());
    }

    /**
     * Performs the chown operation
     *
     * @param path   path to the file or directory to be changed
     * @param params parameter object
     * @return Response object
     * @throws Exception processing error
     */
    public Response changeOwner(String path, ChgOwnerParams params) throws Exception {
        ValidateUtils.checkNullParameter(path == null, "path is null");
        ValidateUtils.checkIllegalParameter(path.isEmpty(), "path not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + path;

        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "chown");
        jsonMap.put("owner", params.getOwner());
        params.getGroup().ifPresent(group -> jsonMap.put("group", group));
        jsonMap.put("recursive", params.isRecursive());

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        return RestUtils.getResponse(request);
    }

}
