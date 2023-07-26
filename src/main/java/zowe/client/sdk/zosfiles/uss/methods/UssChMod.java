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
import zowe.client.sdk.rest.JsonPutRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.ChModParams;

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
public class UssChMod {
    private static final Logger LOG = LoggerFactory.getLogger(UssChMod.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssChMod Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssChMod(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssChMod constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @throws Exception processing error
     * @author James Kostrewski
     */
    public UssChMod(ZosConnection connection, ZoweRequest request) throws Exception {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        if(!(request instanceof JsonPutRequest)) {
            throw new Exception("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Change the mode of a file or directory
     *
     * @param
     */
    public Response chMod(String path, ChModParams params) throws Exception {
        ValidateUtils.checkNullParameter(path == null, "path is null");
        ValidateUtils.checkIllegalParameter(path.isEmpty(), "path not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");


        String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + path;
        LOG.debug(url);

        final String body = buildBody(params);
        LOG.debug(body);

        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        return RestUtils.getResponse(request);
    }

    public static String buildBody(ChModParams params) throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "chmod");
        jsonMap.put("mode", params.getMode());
        if (params.isRecursive()) {
            jsonMap.put("recursive", "true");
        }
        return new JSONObject(jsonMap).toString();
    }

}
