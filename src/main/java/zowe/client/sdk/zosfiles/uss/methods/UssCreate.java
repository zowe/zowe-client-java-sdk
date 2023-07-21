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
import zowe.client.sdk.zosfiles.uss.input.CreateParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) create object functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-create-unix-file-directory">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssCreate {

    private static final Logger LOG = LoggerFactory.getLogger(UssCreate.class);
    private final ZosConnection connection;
    private ZoweRequest request;

    /**
     * UssCreate Constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssCreate(ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative UssCreate constructor with ZoweRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     * @author Frank Giordano
     */
    public UssCreate(ZosConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Perform UNIX create file or directory name request driven by CreateParams object settings
     *
     * @param name   name of object to create
     * @param params create response parameters, see CreateParams object
     * @return Response object
     * @throws Exception error processing request
     * @author James Kostrewski
     */
    public Response create(String name, CreateParams params) throws Exception {
        ValidateUtils.checkNullParameter(name == null, "name is null");
        ValidateUtils.checkIllegalParameter(name.isEmpty(), "name not specified");
        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES + name;
        LOG.debug(url);

        final String body = buildBody(params);

        if (request == null || !(request instanceof JsonPostRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.POST_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        return RestUtils.getResponse(request);
    }

    /**
     * Create the http body request
     *
     * @param params CreateParams parameters
     * @return body string value for http request
     * @author James Kostrewski
     */
    private static String buildBody(CreateParams params) {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("type", params.getType().getValue());
        jsonMap.put("mode", params.getMode());

        final JSONObject jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));
        return jsonRequestBody.toString();
    }

}
