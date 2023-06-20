/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zowe.client.sdk.zosfiles.dsn.methods;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;
import zowe.client.sdk.rest.ZoweRequestFactory;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.unirest.UniRestUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides rename dataset and member functionality
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class DsnRename {

    private static final Logger LOG = LoggerFactory.getLogger(DsnRename.class);
    private final ZOSConnection connection;
    private ZoweRequest request;
    private String url;

    /**
     * DsnRename Constructor
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public DsnRename(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative DsnRename constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public DsnRename(ZOSConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    public Response dataSetName(String source, String destination) throws Exception {
        setUrl(destination);
        return executeRequest(buildBody(source));
    }

    public Response memberName(String dsName, String source, String destination) throws Exception {
        setUrl(destination);
        return executeRequest(buildBody(dsName, source));
    }

    private void setUrl(String source) {
        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_DS_FILES + "/" +
                EncodeUtils.encodeURIComponent(source);
    }

    private String buildBody(String... args) {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("request", "rename");

        final Map<String, Object> fromDataSetReq = new HashMap<>();
        fromDataSetReq.put("dsn", args[0]);
        if (args.length > 1) {
            fromDataSetReq.put("member", args[1]);
        }

        final JSONObject fromDataSetObj = new JSONObject(fromDataSetReq);
        jsonMap.put("from-dataset", fromDataSetObj);

        final JSONObject jsonRequestBody = new JSONObject(jsonMap);
        LOG.debug(String.valueOf(jsonRequestBody));
        return jsonRequestBody.toString();
    }

    private Response executeRequest(String body) throws Exception {
        if (request == null) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(body);

        final Response response = UniRestUtils.getResponse(request);
        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return response;
    }

}
