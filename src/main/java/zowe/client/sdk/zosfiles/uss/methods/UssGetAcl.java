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
import zowe.client.sdk.zosfiles.uss.input.GetAclParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides Unix System Services (USS) getfacl functionality
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class UssGetAcl {

        private final ZosConnection connection;
        private ZosmfRequest request;

        /**
        * UssGetAcl Constructor
        *
        * @param connection connection information, see ZosConnection object
        */
        public UssGetAcl(ZosConnection connection) {
            ValidateUtils.checkConnection(connection);
            this.connection = connection;
        }

        /**
        * Alternative UssGetAcl constructor with ZoweRequest object. This is mainly used for internal code unit testing
        * with mockito, and it is not recommended to be used by the larger community.
        *
        * @param connection connection information, see ZosConnection object
        * @param request any compatible ZoweRequest Interface object
        * @throws Exception processing error
        */
        public UssGetAcl(ZosConnection connection, ZosmfRequest request) throws Exception {
            ValidateUtils.checkConnection(connection);
            ValidateUtils.checkNullParameter(request == null, "request is null");
            this.connection = connection;
            if (!(request instanceof PutJsonZosmfRequest)) {
                throw new Exception("PUT_JSON request type required");
            }
            this.request = request;
        }

    /**
     * Get the ACL for a USS file or directory
     *
     * @param fileNamePath file name with path
     * @param commas true if commas are to be used in the output
     * @return String representation of facl response phrase
     * @throws Exception processing error
     */
    public String getAcl(String fileNamePath, boolean commas) throws Exception {
            Response response = getAclCommon(fileNamePath, new GetAclParams.Builder().build());
            final JSONParser parser = new JSONParser();
            final JSONObject json = (JSONObject) parser.parse(response.getResponsePhrase().get().toString());
            JSONArray jsonArray = (JSONArray) json.get("stdout");
            StringBuilder sb = new StringBuilder();
            if (commas)
                jsonArray.forEach(item -> sb.append(item.toString()));
            else
                jsonArray.forEach(item -> sb.append(item.toString() + "\n"));

            return sb.toString();
        }

        /**
        * Get the ACL for a USS file or directory
        *
        * @param fileNamePath file name with path
         * @param params GetAclParams object to drive the request
        * @return Response object
        * @throws Exception processing error
        */
        public Response getAclCommon(String fileNamePath, GetAclParams params) throws Exception {
            ValidateUtils.checkNullParameter(fileNamePath == null, "path is null");
            ValidateUtils.checkIllegalParameter(fileNamePath.trim().isBlank(), "path is empty");
            ValidateUtils.checkNullParameter(params == null, "params is null");

            final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                    ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                    EncodeUtils.encodeURIComponent(FileUtils.validatePath(fileNamePath));

            final Map<String, Object> getFaclMap = new HashMap<>();
            getFaclMap.put("request", "getfacl");
            params.getType().ifPresent(type -> getFaclMap.put("type", type));
            params.getUser().ifPresent(user -> getFaclMap.put("user", user));
            if (params.getUseCommas())
                getFaclMap.put("use-commas", params.getUseCommas());
            if (params.getSuppressHeader())
                getFaclMap.put("suppress-header", params.getSuppressHeader());
            if (params.getSuppressBaseAcl())
                getFaclMap.put("suppress-baseacl", params.getSuppressBaseAcl());

            if (request == null) {
                request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
            }
            request.setUrl(url);
            request.setBody(new JSONObject(getFaclMap).toString());

            Response response = request.executeRequest();

            return response;

        }



}
