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
import zowe.client.sdk.zosfiles.uss.input.CopyParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) copy functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssCopy {

    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssCopy Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssCopy(ZosConnection connection) {
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
     * @author James Kostrewski
     */
    public UssCopy(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Copy a Unix file or directory to another location
     *
     * @param fromPath   the source path of the file or directory to copy
     * @param targetPath target path of where the file or directory will be copied too
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response copy(String fromPath, String targetPath) throws Exception {
        return copy(targetPath, new CopyParams.Builder().from(fromPath).build());
    }

    /**
     * Copy a Unix file or directory to another location request driven by CopyParams object settings
     *
     * @param targetPath target path of where the file or directory will be copied too
     * @param params     CopyParams parameters that specifies copy action request
     * @return Response object
     * @throws Exception processing error
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public Response copy(String targetPath, CopyParams params) throws Exception {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "copy");
        jsonMap.put("from", FileUtils.validatePath(
                params.getFrom().orElseThrow(() -> new Exception("from not specified"))));
        if (!params.isOverwrite()) {
            jsonMap.put("overwrite", "false");
        }
        if (params.isRecursive()) {
            jsonMap.put("recursive", "true");
        }

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        return RestUtils.getResponse(request);
    }

}
