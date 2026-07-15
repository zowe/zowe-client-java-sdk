/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.methods;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosvariables.VariableConstants;
import zowe.client.sdk.zosvariables.model.SystemVariable;

import java.util.List;
import java.util.Map;

/**
 * Class to handle creating or updating of z/OS system variables.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-create-update-system-variables">z/OSMF REST API</a>
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class VariableCreate {


    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * VariableCreate constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Muhammad Imran
     */
    public VariableCreate(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
    }

    /**
     * Alternative VariableCreate constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    a {@link PostJsonZosmfRequest} implementation object
     * @author Muhammad Imran
     */
    VariableCreate(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Create or update z/OS system variables in the system variable pool.
     * <p>
     * If {@code variables} is empty, the request succeeds, but no variables are created or updated,
     * as defined by the z/OSMF REST API.
     *
     * @param sysplexName name of the sysplex (e.g. 'PLEX1')
     * @param systemName  name of the system (e.g. 'SYS1')
     * @param variables   variables to create or update
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Muhammad Imran
     */
    public Response create(final String sysplexName,
                           final String systemName,
                           final List<SystemVariable> variables) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
        ValidateUtils.checkIllegalParameter(systemName, "systemName");
        ValidateUtils.checkNullParameter(variables, "variables");

        final String url = connection.getZosmfUrl() +
                VariableConstants.RESOURCE +
                UrlConstants.URL_PATH_DELIM +
                EncodeUtils.encodeURIComponent(sysplexName) + "." +
                EncodeUtils.encodeURIComponent(systemName);

        request.setBody(JsonUtils.asRequestBodyJson(Map.of("system-variable-list", variables)));

        request.setUrl(url);
        return request.executeRequest();
    }

}
