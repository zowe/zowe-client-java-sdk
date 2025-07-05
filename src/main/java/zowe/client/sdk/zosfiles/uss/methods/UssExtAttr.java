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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Provides Unix System Services (USS) extattr functionality
 *
 * @author James Kostrewski
 * @version 4.0
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
    public UssExtAttr(final ZosConnection connection) {
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
    public UssExtAttr(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Returns a response string documenting listing attributes
     *
     * @param targetPath path to the file or directory
     * @return string output
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    @SuppressWarnings("unchecked")
    public String display(final String targetPath) throws ZosmfRequestException {
        final Map<String, String> requestMap = new HashMap<>();
        requestMap.put("request", "extattr");
        final Response response = executeRequest(targetPath, requestMap);
        final JSONObject json = JsonParserUtil.parse(response.getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR)).toString());
        final StringBuilder str = new StringBuilder();
        ((JSONArray) json.get("stdout")).forEach(item -> str.append(item.toString()).append("\n"));
        return str.toString();
    }

    /**
     * Extends the attributes of a file or directory
     *
     * @param targetPath path to the file or directory
     * @param value      one or more of the following characters: a,l,p,s
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response set(final String targetPath, final String value) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(isNotValidAttributes(value),
                "specified valid value character sequence");
        final Map<String, String> requestMap = new HashMap<>();
        requestMap.put("request", "extattr");
        requestMap.put("set", value);
        return executeRequest(targetPath, requestMap);
    }

    /**
     * Resets the attributes of a file or directory
     *
     * @param targetPath path to the file or directory
     * @param value      one or more of the following characters: a,l,p,s
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response reset(final String targetPath, final String value) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(isNotValidAttributes(value),
                "specified valid value character sequence");
        final Map<String, String> requestMap = new HashMap<>();
        requestMap.put("request", "extattr");
        requestMap.put("reset", value);
        return executeRequest(targetPath, requestMap);
    }

    /**
     * Execute request for given path and JSON map
     *
     * @param targetPath path to the file or directory
     * @param jsonMap    map representing request body
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    private Response executeRequest(final String targetPath, final Map<String, String> jsonMap)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(targetPath == null, "targetPath is null");
        ValidateUtils.checkIllegalParameter(targetPath.isBlank(), "targetPath not specified");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(jsonMap).toString());

        return request.executeRequest();
    }

    /**
     * Validate that the given value is one or more of the following characters: a,l,p,s
     *
     * @param value string
     * @return boolean value
     * @author Frank Giordano
     */
    private boolean isNotValidAttributes(final String value) {
        return !Pattern.compile("^(?!.*(.).*\\1)[apls]+$").matcher(value).matches();
    }

}
