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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.JsonPostRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.CopyParams;


import java.util.HashMap;
import java.util.Map;



/**
 * Provides Unix System Services (USS) copy functionality
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssCopy {
    private static final Logger LOG = LoggerFactory.getLogger(UssCopy.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssCopy Constructor
     *
     * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
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
     * @author James Kostrewski
     */
    public UssCopy(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    private static String buildBody(CopyParams params) {
        final Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("request", "copy");
        if (params.getFrom().isPresent()) {
            jsonMap.put("from", params.getFrom().get());
        }
        if (params.getFrom_dataset().isPresent()) {
            jsonMap.put("from-dataset", params.getFrom_dataset().get());
        }
        if (params.isOverwrite() == false) {
            jsonMap.put("overwrite", "false");
        }
        if (params.isRecursive()) {
            jsonMap.put("recursive", "true");
        }

        final JSONObject jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));
        return jsonRequestBody.toString();
    }

    /**
     * Copy a USS file or directory to another location
     *
     * @param destinationPath the destination path of the file or directory to copy
     * @param sourcePath      the source path of the file or directory to copy
     * @return ZoweRequest object with the response from the server
     * @throws Exception if the request fails
     */
    public Response copy(String destinationPath, String sourcePath) throws Exception {
        CopyParams.Builder builder = new CopyParams.Builder();
        builder.from(sourcePath);
        return copy(destinationPath, new CopyParams(builder));
    }

    /**
     * Copy a USS file or directory to another location
     *
     * @param destinationPath the destination path of the file or directory to copy
     * @param copyParams       if true, overwrite the destination file if it exists
     * @return ZoweRequest object with the response from the server
     * @throws Exception if the request fails
     */
    public Response copy(String destinationPath, CopyParams copyParams) throws Exception {
        ValidateUtils.checkNullParameter(destinationPath == null, "destinationPath is null");
        ValidateUtils.checkIllegalParameter(destinationPath.isEmpty(), "destinationPath not specified");
        ValidateUtils.checkNullParameter(copyParams == null, "copyParams is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + destinationPath;
        LOG.debug(url);

        final String body = buildBody(copyParams);

        if (request == null || !(request instanceof JsonPostRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);
        return RestUtils.getResponse(request);
    }

}
