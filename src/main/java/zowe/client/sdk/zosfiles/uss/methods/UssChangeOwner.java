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
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.ChangeOwnerParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Parameter container class for Unix System Services (USS) chown operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssChangeOwner {

    private final ZosConnection connection;

    private ZoweRequest request;

    /**
     * UssChangeOwner constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssChangeOwner(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssChangeOwner constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public UssChangeOwner(final ZosConnection connection, final ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Perform chown operation on UNIX file or directory
     *
     * @param targetPath identifies the UNIX file or directory to be the target of the operation
     * @param owner      new owner of the file or directory
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewski
     */
    public Response change(final String targetPath, final String owner) throws Exception {
        return change(targetPath, new ChangeOwnerParams.Builder().owner(owner).build());
    }

    /**
     * Perform chown operation on UNIX file or directory request driven by ChangeOwnerParams object settings
     *
     * @param targetPath identifies the UNIX file or directory to be the target of the operation
     * @param params     change owner response parameters, see ChangeOwnerParams object
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response change(final String targetPath, final ChangeOwnerParams params) throws Exception {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> changeOnerMap = new HashMap<>();
        changeOnerMap.put("request", "chown");
        params.getGroup().ifPresent(group -> changeOnerMap.put("group", group));
        if (params.isRecursive()) {
            changeOnerMap.put("recursive", "true");
        }
        params.getLinks().ifPresent(type -> changeOnerMap.put("links", type.getValue()));
        final String errMsg = "owner not specified";
        changeOnerMap.put("owner", params.getOwner().orElseThrow(() -> new IllegalStateException(errMsg)));

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(changeOnerMap).toString());

        return RestUtils.getResponse(request);
    }

}
