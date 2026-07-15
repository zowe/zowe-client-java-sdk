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

import com.fasterxml.jackson.databind.JsonNode;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.FileUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosfiles.uss.input.UssGetAclInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides Unix System Services (USS) getfacl functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 7.0
 */
public class UssGetAcl {

    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * UssGetAcl Constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author James Kostrewski
     */
    public UssGetAcl(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
    }

    /**
     * Alternative UssGetAcl constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link PutJsonZosmfRequest} implementation object
     * @author James Kostrewski
     */
    UssGetAcl(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalStateException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Get the ACL for a USS file or directory
     *
     * @param targetPath UNIX path to the target file or directory
     * @param useCommas  true if commas are to be used in the output
     * @return list of attribute strings
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    @SuppressWarnings("DuplicatedCode")
    public List<String> get(final String targetPath, final boolean useCommas) throws ZosmfRequestException {
        final Response response = useCommas ?
                getAclCommon(targetPath, new UssGetAclInputData.Builder().usecommas(true).build()) :
                getAclCommon(targetPath, new UssGetAclInputData.Builder().build());
        final JsonNode json = JsonUtils.parse(response.getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(ZosFilesConstants.RESPONSE_PHRASE_ERROR)).toString());
        final List<String> attributes = new ArrayList<>();
        final JsonNode stdout = json.get("stdout");
        if (stdout != null && stdout.isArray()) {
            stdout.forEach(item -> attributes.add(item.asText()));
        }
        return attributes;
    }

    /**
     * Get the ACL for a USS file or directory
     *
     * @param targetPath      UNIX path to the target file or directory
     * @param getAclInputData UssGetAclInputData object to drive the request
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author James Kostrewski
     */
    public Response getAclCommon(final String targetPath, final UssGetAclInputData getAclInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(targetPath, "targetPath");
        ValidateUtils.checkNullParameter(getAclInputData, "getAclInputData");

        final String url = connection.getZosmfUrl() +
                ZosFilesConstants.RESOURCE +
                ZosFilesConstants.RES_USS_FILES +
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

        request.setUrl(url);
        request.setBody(JsonUtils.asRequestBodyJson(getAclMap));

        return request.executeRequest();
    }

}
