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

import org.json.simple.JSONObject;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosvariables.VariableConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle importing of z/OS system variables.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-import-system-variables">z/OSMF REST API</a>
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class VariableImport {

    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * VariableImport Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Chaitanya Katore
     */
    public VariableImport(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
    }

    /**
     * Alternative VariableImport constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZosmfRequest Interface object
     * @author Chaitanya Katore
     */
    VariableImport(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Import variables from a CSV data file on USS.
     *
     * @param sysplexName         name of the sysplex (e.g. 'PLEX1')
     * @param systemName          name of the system (e.g. 'SYS1')
     * @param variablesImportFile absolute path of the variables import file on USS (e.g. '/u/user1/vars.csv')
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Chaitanya Katore
     */
    public Response load(final String sysplexName,
                         final String systemName,
                         final String variablesImportFile) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
        ValidateUtils.checkIllegalParameter(systemName, "systemName");
        ValidateUtils.checkIllegalParameter(variablesImportFile, "variablesImportFile");

        final String url = connection.getZosmfUrl() +
                VariableConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(sysplexName) + "." +
                EncodeUtils.encodeURIComponent(systemName) +
                "/actions/import";

        final Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("variables-import-file", variablesImportFile);

        request.setUrl(url);
        request.setBody(new JSONObject(bodyMap).toString());

        return request.executeRequest();
    }

}
