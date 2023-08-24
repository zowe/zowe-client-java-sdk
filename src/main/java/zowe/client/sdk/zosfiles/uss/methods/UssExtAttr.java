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

import kong.unirest.json.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) extattr functionality
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssExtAttr {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssCopy Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @throws Exception processing error
     */
    public UssExtAttr(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssCopy constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     */
    public UssExtAttr(ZosConnection connection, ZosmfRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Returns a response JSON documenting listing attributes
     *
     * @param targetPath path to the file or directory
     */
    public Response display(String targetPath) throws Exception {
        return executeRequest(targetPath, null, null);
    }

    /**
     * Extends the attributes of a file or directory using set
     *
     * @param targetPath path to the file or directory
     * @param set   one or more of the following: alps
     */
    public Response set(String targetPath, String set) throws Exception {
        return executeRequest(targetPath, set, null);
    }

    /**
     * Extends the attributes of a file or directory using reset
     *
     * @param targetPath path to the file or directory
     * @param reset one or more of the following: alps
     */
    public Response reset(String targetPath, String reset) throws Exception {
        return executeRequest(targetPath, null, reset);
    }


    /**
     * Extends the attributes of a file or directory
     *
     * @param targetPath path to the file or directory
     * @param set   one of more of the following: alps
     * @param reset one or more of the following: alps
     */
    private Response executeRequest(String targetPath, String set, String reset) throws Exception {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + FileUtils.validatePath(targetPath);

        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("request", "extattr");
        if (set != null)
            jsonMap.put("set", set);
        if (reset != null)
            jsonMap.put("reset", reset);

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        return request.executeRequest();
    }

}
