/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfauth.methods;

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.ZosFilesConstants;
import zowe.client.sdk.zosmfauth.input.PasswordParams;

import java.util.HashMap;
import java.util.Map;
// define class userid old pass new pass


public class ZosmfPassword {

    private final ZosConnection connection;

    private ZosmfRequest request;

    /*
    basic constructor
     */
    public ZosmfPassword(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;

    }

    /*
    alternative constructor
     */
    public ZosmfPassword(ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkConnection(connection);
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if(!(request instanceof PutJsonZosmfRequest)){
            throw new IllegalArgumentException("PUT_JSON request type required");
        }
        this.request = request;
    }
    /*
    method to change password
     */
    public Response changePassword(final PasswordParams params) throws ZosmfRequestException {

        //talking point

//        ValidateUtils.checkNullParameter(params.getNewPwd()==null, "newPwd is null");
//        ValidateUtils.checkNullParameter(params.getOldPwd()==null, "oldPwd is null");
//        ValidateUtils.checkNullParameter(params.getNewPwd()==null, "newPwd is null");

        ValidateUtils.checkNullParameter(params == null, "params is null");

        final String url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() +
            ZosFilesConstants.RESOURCE;

        final Map<String, Object> passwordMap = new HashMap<>();
        passwordMap.put("userId", params.getUserId());
        passwordMap.put("oldPassword", params.getOldPwd());
        passwordMap.put("newPassword", params.getNewPwd());

        if(request == null){
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }

        request.setUrl(url);
        request.setBody(new JSONObject(passwordMap).toJSONString());


        final Response response = request.executeRequest();
        return response;



    }

}
