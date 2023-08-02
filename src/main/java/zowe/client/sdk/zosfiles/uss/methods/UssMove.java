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

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) move functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssMove {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssMove Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssMove(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssMove constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author James Kostrewski
     */
    public UssMove(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Move a UNIX file or directory with overwrite set as true
     *
     * @param fromPath   the source path of the file or directory to move
     * @param targetPath the target path of where the file or directory will be moved too
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response move(String fromPath, String targetPath) throws Exception {
        return moveCommon(fromPath, targetPath, true);
    }

    /**
     * Move a UNIX file or directory with overwrite value specified
     *
     * @param fromPath   the source path of the file or directory to move
     * @param targetPath the target path of where the file or directory will be moved too
     * @param overwrite  true if you want to override existing data at target path or false to not override
     * @return Response object
     * @throws Exception processing error
     * @author Frank Giordano
     */
    public Response move(String fromPath, String targetPath, boolean overwrite) throws Exception {
        return moveCommon(fromPath, targetPath, overwrite);
    }

    /**
     * Move a UNIX file or directory
     *
     * @param fromPath   the source path of the file or directory to move
     * @param targetPath the target path of where the file or directory will be moved too
     * @param overwrite  true if you want to override existing data at target path or false to not override
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewski
     * @author Frank Giordano
     */
    private Response moveCommon(String fromPath, String targetPath, boolean overwrite) throws Exception {
        ValidateUtils.checkNullParameter(fromPath == null, "fromPath is null");
        ValidateUtils.checkIllegalParameter(fromPath.trim().isEmpty(), "fromPath not specified");
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.trim().isEmpty(), "targetPath not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + targetPath;

        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "move");
        jsonMap.put("from", fromPath);
        jsonMap.put("overwrite", overwrite);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        return RestUtils.getResponse(request);
    }

}
