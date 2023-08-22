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
         * @param params GetAclParams object to drive the request
        * @return Response object
        * @throws Exception processing error
        */
        public Response getAcl(String fileNamePath, GetAclParams params) throws Exception {
            ValidateUtils.checkNullParameter(fileNamePath == null, "path is null");
            ValidateUtils.checkIllegalParameter(fileNamePath.trim().isBlank(), "path is empty");
            ValidateUtils.checkNullParameter(params == null, "params is null");

            final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                    ZosFilesConstants.RESOURCE + ZosFilesConstants.RES_USS_FILES +
                    EncodeUtils.encodeURIComponent(FileUtils.validatePath(fileNamePath));

            final Map<String, Object> getFaclMap = new HashMap<>();
            params.getType().ifPresent(type -> getFaclMap.put("type", type));
            params.getUser().ifPresent(user -> getFaclMap.put("user", user));
            if (params.getUseCommas())
                getFaclMap.put("useCommas", params.getUseCommas());
            if (params.getSuppressHeader())
                getFaclMap.put("suppressHeader", params.getSuppressHeader());
            if (params.getSuppressBaseAcl())
                getFaclMap.put("suppressBaseAcl", params.getSuppressBaseAcl());

            if (request == null) {
                request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
            }
            request.setUrl(url);
            request.setBody(new JSONObject(getFaclMap).toString());

            return request.executeRequest();
        }
}
