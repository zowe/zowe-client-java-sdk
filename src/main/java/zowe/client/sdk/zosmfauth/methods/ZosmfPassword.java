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
import zowe.client.sdk.zosmfauth.ZosmfAuthConstants;
import zowe.client.sdk.zosmfauth.input.PasswordInputData;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides z/OSMF change user password or passphrase service
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.1.0?topic=services-change-user-password-passphrase">z/OSMF REST API </a>
 *
 * @author Esteban Sandoval
 * @author Frank Giordano
 * @version 6.0
 */
public class ZosmfPassword {

    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * ZosmfPassword constructor
     *
     * @param connection for connection information, see ZosConnection object
     * @author Esteban Sandoval
     */
    public ZosmfPassword(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative ZosmfPassword constructor with ZosmfRequest object. This is mainly used for internal code
     * unit testing with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Esteban Sandoval
     */
    ZosmfPassword(ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        if (!(request instanceof PutJsonZosmfRequest)) {
            throw new IllegalArgumentException("PUT_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Change the password or passphrase for a specified User ID driven by PasswordInputData object settings
     *
     * @param pwdInputData Password response parameters, see PasswordInputData object
     * @return Response object
     * @throws ZosmfRequestException request error state
     * @author Esteban Sandoval
     * @author Frank Giordano
     */
    public Response changePassword(final PasswordInputData pwdInputData) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(pwdInputData == null, "pwdInputData is null");

        final String url = connection.getZosmfUrl() +
                ZosmfAuthConstants.RESOURCE;

        final Map<String, Object> passwordMap = new HashMap<>();
        passwordMap.put("userID", pwdInputData.getUserId());
        passwordMap.put("oldPwd", pwdInputData.getOldPwd());
        passwordMap.put("newPwd", pwdInputData.getNewPwd());

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        }

        request.setUrl(url);
        request.setBody(new JSONObject(passwordMap).toJSONString());

        return request.executeRequest();
    }

}
