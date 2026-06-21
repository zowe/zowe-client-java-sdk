/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.methods;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.UrlConstants;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosmfworkflow.WorkflowConstants;
import zowe.client.sdk.zosmfworkflow.input.WorkflowGetDefinitionInputData;
import zowe.client.sdk.zosmfworkflow.input.WorkflowGetPropertiesInputData;
import zowe.client.sdk.zosmfworkflow.response.WorkflowGetDefinitionResponse;
import zowe.client.sdk.zosmfworkflow.response.WorkflowGetPropertiesResponse;

/**
 * Provides retrieve workflow definition functionality through the z/OSMF workflow REST API.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-retrieve-workflow-definition">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowGet {

    private static final String DEFINITION_CONTEXT = "getDefinitionCommon";
    private static final String PROPERTIES_CONTEXT = "getPropertiesCommon";
    private final ZosConnection connection;
    private ZosmfRequest request;

    /**
     * WorkflowGet constructor.
     *
     * @param connection for connection information, see ZosConnection object
     */
    public WorkflowGet(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
    }

    /**
     * Alternative WorkflowGet constructor with ZosmfRequest object.
     * This is mainly used for internal code unit testing with Mockito,
     * and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private.
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     */
    WorkflowGet(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkNullParameter(request, "request");
        this.connection = connection;
        if (!(request instanceof GetJsonZosmfRequest)) {
            throw new IllegalStateException("GET_JSON request type required");
        }
        this.request = request;
    }

    /**
     * Retrieve a z/OSMF workflow definition-by-definition file path.
     *
     * @param definitionFilePath specifies the location of the workflow definition file, which is either a UNIX
     *                           path name (including the file name) or a fully qualified z/OS data set name
     * @return workflow definition details returned by z/OSMF
     * @throws ZosmfRequestException request error state
     */
    public WorkflowGetDefinitionResponse getDefinition(final String definitionFilePath) throws ZosmfRequestException {
        return getDefinitionCommon(WorkflowGetDefinitionInputData.builder().definitionFilePath(definitionFilePath).build());
    }

    /**
     * Retrieve a z/OSMF workflow definition-by-definition file path on a specific system.
     *
     * @param definitionFilePath specifies the location of the workflow definition file, which is either a UNIX
     *                           path name (including the file name) or a fully qualified z/OS data set name
     * @param workflowDefinitionFileSystem nickname of the system on which the workflow definition file resides
     * @return workflow definition details returned by z/OSMF
     * @throws ZosmfRequestException request error state
     */
    public WorkflowGetDefinitionResponse getDefinition(final String definitionFilePath,
                                                       final String workflowDefinitionFileSystem)
            throws ZosmfRequestException {
        return getDefinitionCommon(
                WorkflowGetDefinitionInputData.builder()
                        .definitionFilePath(definitionFilePath)
                        .workflowDefinitionFileSystem(workflowDefinitionFileSystem)
                        .build()
        );
    }

    /**
     * Retrieve a z/OSMF workflow definition.
     *
     * @param definitionInputData workflow definition retrieval parameters
     * @return workflow definition details returned by z/OSMF
     * @throws ZosmfRequestException request error state
     */
    public WorkflowGetDefinitionResponse getDefinitionCommon(final WorkflowGetDefinitionInputData definitionInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(definitionInputData, "definitionInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl());
        url.append(WorkflowConstants.WORKFLOW_DEFINITION_RESOURCE);

        // definitionFilePath is always present; WorkflowGetInputData enforces this invariant
        definitionInputData.getDefinitionFilePath()
                .ifPresent(target -> url.append("?definitionFilePath=").append(getEncodeURIComponent(target)));

        definitionInputData.getWorkflowDefinitionFileSystem()
                .ifPresent(sys -> url.append("&workflowDefinitionFileSystem=").append(getEncodeURIComponent(sys)));

        final String returnData = buildReturnData(definitionInputData);
        if (!returnData.isEmpty()) {
            url.append("&returnData=").append(returnData);
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }

        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no workflow get response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, WorkflowGetDefinitionResponse.class, DEFINITION_CONTEXT);
    }

    /**
     * Get the properties of a z/OSMF workflow by workflow key.
     *
     * @param workflowKey workflow key that uniquely identifies the workflow instance
     * @return workflow properties returned by z/OSMF
     * @throws ZosmfRequestException request error state
     */
    public WorkflowGetPropertiesResponse getProperties(final String workflowKey) throws ZosmfRequestException {
        return getPropertiesCommon(WorkflowGetPropertiesInputData.builder().workflowKey(workflowKey).build());
    }

    /**
     * Get the properties of a z/OSMF workflow by workflow key, optionally including step and variable information.
     *
     * @param workflowKey     workflow key that uniquely identifies the workflow instance
     * @param returnSteps     whether the response includes the workflow step information
     * @param returnVariables whether the response includes the workflow variable information
     * @return workflow properties returned by z/OSMF
     * @throws ZosmfRequestException request error state
     */
    public WorkflowGetPropertiesResponse getProperties(final String workflowKey,
                                                       final boolean returnSteps,
                                                       final boolean returnVariables) throws ZosmfRequestException {
        return getPropertiesCommon(
                WorkflowGetPropertiesInputData.builder()
                        .workflowKey(workflowKey)
                        .returnSteps(returnSteps)
                        .returnVariables(returnVariables)
                        .build()
        );
    }

    /**
     * Get the properties of a z/OSMF workflow.
     *
     * @param propertiesInputData workflow properties retrieval parameters
     * @return workflow properties returned by z/OSMF
     * @throws ZosmfRequestException request error state
     */
    public WorkflowGetPropertiesResponse getPropertiesCommon(final WorkflowGetPropertiesInputData propertiesInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(propertiesInputData, "propertiesInputData");

        final StringBuilder url = new StringBuilder(connection.getZosmfUrl());
        url.append(WorkflowConstants.WORKFLOWS_RESOURCE);

        // workflowKey is always present; WorkflowGetPropertiesInputData enforces this invariant
        propertiesInputData.getWorkflowKey()
                .ifPresent(key -> url.append(UrlConstants.URL_PATH_DELIM).append(getEncodeURIComponent(key)));

        final String returnData = buildReturnData(
                propertiesInputData.isReturnSteps(),
                propertiesInputData.isReturnVariables()
        );
        if (!returnData.isEmpty()) {
            url.append("?returnData=").append(returnData);
        }

        if (request == null) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }

        request.setUrl(url.toString());

        final String responsePhrase = request.executeRequest()
                .getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no workflow get response phrase"))
                .toString();

        return JsonUtils.parseResponse(responsePhrase, WorkflowGetPropertiesResponse.class, PROPERTIES_CONTEXT);
    }

    /**
     * Helper wrapper method.
     *
     * @param str String value
     * @return string value
     */
    private static String getEncodeURIComponent(final String str) {
        return EncodeUtils.encodeURIComponent(str);
    }

    /**
     * Build the returnData query parameter value from the requested attributes.
     *
     * @param definitionInputData workflow definition retrieval parameters
     * @return returnData value or an empty string when no attributes are requested
     */
    private static String buildReturnData(final WorkflowGetDefinitionInputData definitionInputData) {
        return buildReturnData(definitionInputData.isReturnSteps(), definitionInputData.isReturnVariables());
    }

    /**
     * Build the returnData query parameter value from the requested attributes.
     *
     * @param returnSteps     whether to request the workflow step information
     * @param returnVariables whether to request the workflow variable information
     * @return returnData value or an empty string when no attributes are requested
     */
    private static String buildReturnData(final boolean returnSteps, final boolean returnVariables) {
        final StringBuilder returnData = new StringBuilder();
        if (returnSteps) {
            returnData.append("steps");
        }
        if (returnVariables) {
            if (returnData.length() > 0) {
                returnData.append(',');
            }
            returnData.append("variables");
        }
        return returnData.toString();
    }

}
