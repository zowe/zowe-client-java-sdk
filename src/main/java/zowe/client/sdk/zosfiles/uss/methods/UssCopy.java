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
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
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

    /**
     * Copy a Unix file or directory to another location
     *
     * @param destinationPath the destination path of the file or directory to copy
     * @param sourcePath      the source path of the file or directory to copy
     * @return Response object
     * @throws Exception processing error
     */
    public Response copy(String destinationPath, String sourcePath) throws Exception {
        return copyCommon(destinationPath, new CopyParams.Builder().from(sourcePath).build());
    }

    /**
     * Copy a Unix file or directory to another location
     *
     * @param destinationPath the destination path of the file or directory to copy
     * @param params          CopyParams parameters that specifies copy action request
     * @return Response object
     * @throws Exception processing error
     */
    public Response copyCommon(String destinationPath, CopyParams params) throws Exception {
        ValidateUtils.checkNullParameter(destinationPath == null, "destinationPath is null");
        ValidateUtils.checkIllegalParameter(destinationPath.isEmpty(), "destinationPath not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + destinationPath;
        LOG.debug(url);

        final String body = buildBody(params);

        if (request == null || !(request instanceof JsonPostRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        return RestUtils.getResponse(request);
    }

    /**
     * Build request body to handle the incoming request
     *
     * @param params CopyParams object
     * @return json string value
     * @author James Kostrewski
     * @author Frank Giordano
     */
    private static String buildBody(CopyParams params) {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "copy");
        params.getFrom().ifPresent(str -> jsonMap.put("from", str));
        if (!params.isOverwrite()) {
            jsonMap.put("overwrite", "false");
        }
        if (params.isRecursive()) {
            jsonMap.put("recursive", "true");
        }

        final JSONObject jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));

        return jsonRequestBody.toString();
    }

}
