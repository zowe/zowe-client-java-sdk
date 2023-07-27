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
import zowe.client.sdk.zosfiles.uss.input.ChangeModeParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) chmod functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssChangeMode {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssChangeMode Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssChangeMode(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssChangeMode constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author James Kostrewski
     */
    public UssChangeMode(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Change the mode of a UNIX file or directory request driven by ChangeModeParams object settings
     *
     * @param targetPath identifies the UNIX file or directory to be the target of the operation
     * @param params     change mode response parameters, see ChangeModeParams object
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewsk
     * @author Frank Giordano
     */
    public Response change(String targetPath, ChangeModeParams params) throws Exception {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isEmpty(), "targetPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + targetPath;

        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "chmod");
        if (params.isRecursive()) {
            jsonMap.put("recursive", "true");
        }
        params.getLinkType().ifPresent(type -> jsonMap.put("links", type.getValue()));
        jsonMap.put("mode", params.getMode().orElseThrow(() -> new Exception("mode not specified")));

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        return RestUtils.getResponse(request);
    }

    public static void main(String[] args) throws Exception {
        final String hostName = "10.175.84.31";
        final String zosmfPort = "1443";
        final String userName = "FG892105";
        final String password = "again1";
        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);

        UssChangeMode chmod = new UssChangeMode(connection);
        ChangeModeParams params = new ChangeModeParams.Builder().mode("rwxrwxrwx").build();
        Response response = chmod.change("/u/fgiorda/test3", params);

    }

}
