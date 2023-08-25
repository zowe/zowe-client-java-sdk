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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
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
     * @author James Kostrewski
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
     * @author James Kostrewski
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
     * Returns a response string documenting listing attributes
     *
     * @param targetPath path to the file or directory
     * @author James Kostrewski
     */
    public String display(String targetPath) throws Exception {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("request", "extattr");
        final Response response = executeRequest(targetPath, jsonMap);
        final JSONObject json = (JSONObject) new JSONParser().parse(response.getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR)).toString());
        final JSONArray jsonArray = (JSONArray) json.get("stdout");
        final StringBuilder sb = new StringBuilder();
        jsonArray.forEach(item -> sb.append(item.toString()).append("\n"));
        return sb.toString();
    }

    /**
     * Extends the attributes of a file or directory
     *
     * @param targetPath path to the file or directory
     * @param value      one or more of the following: alps
     * @author James Kostrewski
     */
    public Response set(String targetPath, String value) throws Exception {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("request", "extattr");
        jsonMap.put("set", value);
        return executeRequest(targetPath, jsonMap);
    }

    /**
     * Resets the attributes of a file or directory
     *
     * @param targetPath path to the file or directory
     * @param value      one or more of the following: alps
     * @author James Kostrewski
     */
    public Response reset(String targetPath, String value) throws Exception {
        final Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("request", "extattr");
        jsonMap.put("reset", value);
        return executeRequest(targetPath, jsonMap);
    }


    /**
     * Execute request for given path and json map
     *
     * @param targetPath path to the file or directory
     * @param jsonMap    map representing request body
     * @author James Kostrewski
     */
    private Response executeRequest(String targetPath, Map<String, String> jsonMap) {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        return request.executeRequest();
    }

}
