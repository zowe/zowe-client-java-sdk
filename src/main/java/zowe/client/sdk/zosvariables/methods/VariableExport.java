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
 * Class to handle exporting of z/OS system variables.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-export-system-variables">z/OSMF REST API</a>
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class VariableExport {

    private final ZosConnection connection;
    private final ZosmfRequest request;

    /**
     * VariableExport Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Chaitanya Katore
     */
    public VariableExport(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.POST_JSON);
    }

    /**
     * Alternative VariableExport constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZosmfRequest Interface object
     * @author Chaitanya Katore
     */
    VariableExport(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof PostJsonZosmfRequest)) {
            throw new IllegalStateException("POST_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Export variables to a CSV data file on USS.
     *
     * @param sysplexName name of the sysplex (e.g. 'PLEX1')
     * @param systemName name of the system (e.g. 'SYS1')
     * @param targetFile UNIX path to variables export file on USS (e.g. '/u/user1/vars.csv')
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Chaitanya Katore
     */
    public Response export(final String sysplexName,
                           final String systemName,
                           final String targetFile) throws ZosmfRequestException {
        return exportCommon(sysplexName, systemName, targetFile, false);
    }

    /**
     * Export variables to a CSV data file on USS with overwrite option.
     *
     * @param sysplexName name of the sysplex (e.g. 'PLEX1')
     * @param systemName name of the system (e.g. 'SYS1')
     * @param targetFile UNIX path to variables export file on USS (e.g. '/u/user1/vars.csv')
     * @param overwrite boolean to indicate if file should be overwritten if it already exists
     * @return http response object
     * @throws ZosmfRequestException request error state
     * @author Chaitanya Katore
     */
    public Response export(final String sysplexName,
                           final String systemName,
                           final String targetFile,
                           final boolean overwrite) throws ZosmfRequestException {
        return exportCommon(sysplexName, systemName, targetFile, overwrite);
    }

    /**
     * Common method to handle exporting of variables.
     *
     * @param sysplexName name of the sysplex (e.g. 'PLEX1')
     * @param systemName name of the system (e.g. 'SYS1')
     * @param targetFile UNIX path to variables export file on USS (e.g. '/u/user1/vars.csv')
     * @param overwrite boolean value to indicate if file should be overwritten
     * @return http response object
     * @throws ZosmfRequestException request error state
     */
    private Response exportCommon(final String sysplexName,
                                  final String systemName,
                                  final String targetFile,
                                  final boolean overwrite) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sysplexName, "sysplexName");
        ValidateUtils.checkIllegalParameter(systemName, "systemName");
        ValidateUtils.checkIllegalParameter(targetFile, "targetFile");

        final String url = connection.getZosmfUrl() +
                VariableConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(sysplexName) + "." +
                EncodeUtils.encodeURIComponent(systemName) +
                "/actions/export";

        final Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("variables-export-file", targetFile);
        bodyMap.put("overwrite", overwrite);

        request.setUrl(url);
        request.setBody(new JSONObject(bodyMap).toString());

        return request.executeRequest();
    }

}
