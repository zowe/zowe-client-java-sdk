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
import zowe.client.sdk.zosfiles.uss.input.UssGetAclInputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) getfacl functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 5.0
 */
public class UssGetAcl {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * UssGetAcl Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssGetAcl(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative UssGetAcl constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author James Kostrewski
     */
    UssGetAcl(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get the ACL for a USS file or directory
     *
     * @param targetPath file name with a path
     * @param useCommas  true if commas are to be used in the output
     * @return string representation of response phrase
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    @SuppressWarnings("unchecked")
    public String get(final String targetPath, final boolean useCommas) throws ZosmfRequestException {
        final Response response = useCommas ?
                getAclCommon(targetPath, new UssGetAclInputData.Builder().usecommas(true).build()) :
                getAclCommon(targetPath, new UssGetAclInputData.Builder().build());
        final JSONObject json = JsonParserUtil.parse(response.getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR)).toString());
        final StringBuilder str = new StringBuilder();
        if (useCommas) {
            ((JSONArray) json.get("stdout")).forEach(item -> str.append(item.toString()));
        } else {
            ((JSONArray) json.get("stdout")).forEach(item -> str.append(item.toString()).append("\n"));
        }
        return str.toString();
    }

    /**
     * Get the ACL for a USS file or directory
     *
     * @param targetPath      file name with a path
     * @param getAclInputData UssGetAclInputData object to drive the request
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response getAclCommon(final String targetPath, final UssGetAclInputData getAclInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(targetPath, "targetPath");
        ValidateUtils.checkNullParameter(getAclInputData == null, "getAclInputData is null");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                EncodeUtils.encodeURIComponent(FileUtils.validatePath(targetPath));

        final Map<String, Object> getAclMap = new HashMap<>();
        getAclMap.put("request", "getfacl");
        getAclInputData.getType().ifPresent(type -> getAclMap.put("type", type.getValue()));
        getAclInputData.getUser().ifPresent(user -> getAclMap.put("user", user));
        if (getAclInputData.getUseCommas()) {
            getAclMap.put("use-commas", getAclInputData.getUseCommas());
        }
        if (getAclInputData.getSuppressHeader()) {
            getAclMap.put("suppress-header", getAclInputData.getSuppressHeader());
        }
        if (getAclInputData.getSuppressBaseAcl()) {
            getAclMap.put("suppress-baseacl", getAclInputData.getSuppressBaseAcl());
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }
        request.setUrl(url);
        request.setBody(new JSONObject(getAclMap).toString());

        return request.executeRequest();
    }

}
