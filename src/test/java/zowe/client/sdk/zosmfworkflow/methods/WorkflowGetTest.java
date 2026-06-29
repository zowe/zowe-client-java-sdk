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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.zosmfworkflow.input.WorkflowGetDefinitionInputData;
import zowe.client.sdk.zosmfworkflow.input.WorkflowGetPropertiesInputData;
import zowe.client.sdk.zosmfworkflow.response.WorkflowGetDefinitionResponse;
import zowe.client.sdk.zosmfworkflow.response.WorkflowGetPropertiesResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * Class containing unit tests for WorkflowGet.
 */
public class WorkflowGetTest {

    private static final String DEFINITION_FILE_PATH =
            "/usr/lpp/zosmf/samples/workflow_sample_program_execution.xml";

    /**
     * Full workflow definition response document from the example in the IBM z/OSMF REST API
     * documentation for the retrieve workflow definition operation (returnData=steps,variables).
     */
    private static String getWorkflowDefinitionJson() {
        return "{\n" +
                "  \"workflowDefaultName\": null,\n" +
                "  \"isInstanceVariableWithoutPrefix\": false,\n" +
                "  \"variables\": [\n" +
                "    {\n" +
                "      \"visibility\": \"private\",\n" +
                "      \"exposeToUser\": false,\n" +
                "      \"promptAtCreate\": false,\n" +
                "      \"description\": \"This value is used to specify a procedure name for the TSO/E address space.\",\n" +
                "      \"abstract\": \"Enter a procedure name for running the program.\",\n" +
                "      \"type\": \"string\",\n" +
                "      \"requiredAtCreate\": false,\n" +
                "      \"default\": null,\n" +
                "      \"decimalPlaces\": null,\n" +
                "      \"valueMustBeChoice\": false,\n" +
                "      \"scope\": \"instance\",\n" +
                "      \"name\": \"procNameVariable\",\n" +
                "      \"category\": \"TSO procName\",\n" +
                "      \"choice\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"visibility\": \"private\",\n" +
                "      \"exposeToUser\": false,\n" +
                "      \"regularExpression\": \"^[A-Z$#@]{1}[0-9A-Z$#@]{0,7}$\",\n" +
                "      \"promptAtCreate\": false,\n" +
                "      \"validationType\": \"GROUP\",\n" +
                "      \"description\": \"The group name under whose authority the started task will run.\",\n" +
                "      \"abstract\": \"Group name for the started task.\",\n" +
                "      \"type\": \"string\",\n" +
                "      \"requiredAtCreate\": false,\n" +
                "      \"default\": \"SYS1\",\n" +
                "      \"decimalPlaces\": null,\n" +
                "      \"valueMustBeChoice\": false,\n" +
                "      \"scope\": \"instance\",\n" +
                "      \"name\": \"st_group\",\n" +
                "      \"category\": \"Started\",\n" +
                "      \"choice\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"visibility\": \"private\",\n" +
                "      \"exposeToUser\": false,\n" +
                "      \"regularExpression\": \"^[0-9A-Z$#@]{1,8}$\",\n" +
                "      \"promptAtCreate\": false,\n" +
                "      \"validationType\": \"USERID\",\n" +
                "      \"description\": \"The user ID under whose authority the new started task will run.\",\n" +
                "      \"abstract\": \"User ID for the started task.\",\n" +
                "      \"type\": \"string\",\n" +
                "      \"requiredAtCreate\": false,\n" +
                "      \"default\": \"MYSTUSER\",\n" +
                "      \"decimalPlaces\": null,\n" +
                "      \"valueMustBeChoice\": false,\n" +
                "      \"scope\": \"instance\",\n" +
                "      \"name\": \"st_user\",\n" +
                "      \"category\": \"Started\",\n" +
                "      \"choice\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"productID\": \"ABC123\",\n" +
                "  \"workflowDescription\": \"Sample that demonstrates how to run an executable program from a step.\",\n" +
                "  \"steps\": [\n" +
                "    {\n" +
                "      \"template\": \"#!/bin/sh echo sample\",\n" +
                "      \"instructions\": \"This step outputs some variables and prints a few words.\",\n" +
                "      \"autoEnable\": false,\n" +
                "      \"maxLrecl\": 1024,\n" +
                "      \"submitAs\": \"TSO-UNIX-shell\",\n" +
                "      \"failedPattern\": [\"failed.*\"],\n" +
                "      \"description\": \"In this step, you submit an inline UNIX shell script for immediate processing.\",\n" +
                "      \"weight\": 1,\n" +
                "      \"outputVariablesPrefix\": \"prefix:\",\n" +
                "      \"optional\": false,\n" +
                "      \"procName\": \"${instance-procNameVariable}\",\n" +
                "      \"title\": \"A step that runs a UNIX shell script.\",\n" +
                "      \"timeout\": 60,\n" +
                "      \"regionSize\": 50000,\n" +
                "      \"skills\": \"System Programmer\",\n" +
                "      \"output\": null,\n" +
                "      \"scriptParameters\": \"para1\",\n" +
                "      \"isRestStep\": false,\n" +
                "      \"saveAsUnixFile\": \"/u/${instance-st_user}/savedStuff/myScript.sh\",\n" +
                "      \"outputSysoutDD\": null,\n" +
                "      \"variable-specifications\": [\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_group\", \"required\": true},\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_user\", \"required\": true},\n" +
                "        {\"scope\": \"instance\", \"name\": \"procNameVariable\", \"required\": true}\n" +
                "      ],\n" +
                "      \"name\": \"TSO-UNIX-shell_Execution\",\n" +
                "      \"successPattern\": \"success.*\",\n" +
                "      \"saveAsDataset\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"template\": \"/* rexx */ say sample\",\n" +
                "      \"instructions\": \"This step outputs some variables and prints a few words.\",\n" +
                "      \"autoEnable\": false,\n" +
                "      \"maxLrecl\": 1024,\n" +
                "      \"submitAs\": \"TSO-UNIX-REXX\",\n" +
                "      \"failedPattern\": [\"failed.*\"],\n" +
                "      \"description\": \"In this step, you submit an inline UNIX REXX exec for immediate processing.\",\n" +
                "      \"weight\": 1,\n" +
                "      \"outputVariablesPrefix\": \"prefix:\",\n" +
                "      \"optional\": false,\n" +
                "      \"procName\": \"${instance-procNameVariable}\",\n" +
                "      \"title\": \"A step that runs a UNIX REXX exec program.\",\n" +
                "      \"timeout\": 60,\n" +
                "      \"regionSize\": 50000,\n" +
                "      \"skills\": \"System Programmer\",\n" +
                "      \"output\": null,\n" +
                "      \"scriptParameters\": \"para1\",\n" +
                "      \"isRestStep\": false,\n" +
                "      \"saveAsUnixFile\": \"/u/${instance-st_user}/savedStuff/myScript.sh\",\n" +
                "      \"outputSysoutDD\": null,\n" +
                "      \"variable-specifications\": [\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_group\", \"required\": true},\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_user\", \"required\": true},\n" +
                "        {\"scope\": \"instance\", \"name\": \"procNameVariable\", \"required\": true}\n" +
                "      ],\n" +
                "      \"name\": \"TSO-UNIX-REXX_Execution\",\n" +
                "      \"successPattern\": \"success.*\",\n" +
                "      \"saveAsDataset\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"template\": \"/* rexx */ say sample\",\n" +
                "      \"instructions\": \"This step outputs some variables and prints a few words.\",\n" +
                "      \"autoEnable\": false,\n" +
                "      \"maxLrecl\": 1024,\n" +
                "      \"submitAs\": \"TSO-REXX\",\n" +
                "      \"failedPattern\": [\"failed.*\"],\n" +
                "      \"description\": \"In this step, you submit an inline REXX exec for immediate processing.\",\n" +
                "      \"weight\": 1,\n" +
                "      \"outputVariablesPrefix\": \"prefix:\",\n" +
                "      \"optional\": false,\n" +
                "      \"procName\": \"${instance-procNameVariable}\",\n" +
                "      \"title\": \"A step that runs a REXX exec program.\",\n" +
                "      \"timeout\": 60,\n" +
                "      \"regionSize\": 50000,\n" +
                "      \"skills\": \"System Programmer\",\n" +
                "      \"output\": null,\n" +
                "      \"scriptParameters\": \"para1\",\n" +
                "      \"isRestStep\": false,\n" +
                "      \"saveAsUnixFile\": \"/u/${instance-st_user}/savedStuff/myScript.sh\",\n" +
                "      \"outputSysoutDD\": null,\n" +
                "      \"variable-specifications\": [\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_group\", \"required\": true},\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_user\", \"required\": true},\n" +
                "        {\"scope\": \"instance\", \"name\": \"procNameVariable\", \"required\": true}\n" +
                "      ],\n" +
                "      \"name\": \"TSO-TSO-REXX_Execution\",\n" +
                "      \"successPattern\": \"success.*\",\n" +
                "      \"saveAsDataset\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"productName\": \"Product ABC\",\n" +
                "  \"globalVariableGroup\": null,\n" +
                "  \"containsParallelSteps\": false,\n" +
                "  \"workflowDefinitionFileMD5Value\": \"5c5dd66eb3ca3cd1c578ccf323d57cc0\",\n" +
                "  \"isCallable\": null,\n" +
                "  \"productVersion\": \"Version 1\",\n" +
                "  \"jobsOutputDirectory\": null,\n" +
                "  \"vendor\": \"IBM\",\n" +
                "  \"scope\": \"none\",\n" +
                "  \"workflowVersion\": \"1.0\",\n" +
                "  \"category\": \"configuration\",\n" +
                "  \"workflowID\": \"programExecutionSample\"\n" +
                "}";
    }

    @Test
    public void tstWorkflowGetSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        WorkflowGet workflowGet = new WorkflowGet(connection, request);
        assertNotNull(workflowGet);
    }

    @Test
    public void tstWorkflowGetSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new WorkflowGet(null, request));
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new WorkflowGet(connection, null));
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new WorkflowGet(connection, request));
        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        WorkflowGet workflowGet = new WorkflowGet(connection);
        assertNotNull(workflowGet);
    }

    @Test
    public void tstWorkflowGetPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new WorkflowGet(null));
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetCommonSuccess() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        GetJsonZosmfRequest mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(getWorkflowDefinitionJson(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        WorkflowGet workflowGet = new WorkflowGet(connection, mockGetRequest);
        WorkflowGetDefinitionInputData inputData = WorkflowGetDefinitionInputData.builder()
                .definitionFilePath(DEFINITION_FILE_PATH)
                .workflowDefinitionFileSystem("SY1")
                .returnSteps(true)
                .returnVariables(true)
                .build();
        WorkflowGetDefinitionResponse response = workflowGet.getDefinitionCommon(inputData);

        String expectedUrl = "https://1:443/workflow/rest/1.0/workflowDefinition" +
                "?definitionFilePath=" + EncodeUtils.encodeURIComponent(DEFINITION_FILE_PATH) +
                "&workflowDefinitionFileSystem=" + EncodeUtils.encodeURIComponent("SY1") +
                "&returnData=steps,variables";
        assertEquals(expectedUrl, mockGetRequest.getUrl());

        assertEquals("programExecutionSample", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
        assertEquals("configuration", response.getCategory());
        assertEquals("none", response.getScope());
        assertEquals("Product ABC", response.getProductName());
        assertEquals("ABC123", response.getProductID());
        assertEquals("Version 1", response.getProductVersion());
        assertEquals("5c5dd66eb3ca3cd1c578ccf323d57cc0", response.getWorkflowDefinitionFileMD5Value());
        assertEquals(Boolean.FALSE, response.getContainsParallelSteps());
        assertEquals(Boolean.FALSE, response.getIsInstanceVariableWithoutPrefix());
        assertEquals("", response.getWorkflowDefaultName());
        assertEquals("", response.getIsCallable());

        assertEquals(3, response.getSteps().size());
        assertEquals("TSO-UNIX-shell_Execution", response.getSteps().get(0).getName());
        assertEquals("A step that runs a UNIX shell script.", response.getSteps().get(0).getTitle());
        assertEquals(Boolean.FALSE, response.getSteps().get(0).getOptional());
        assertEquals(3, response.getSteps().get(0).getVariableSpecifications().size());
        assertEquals("st_group", response.getSteps().get(0).getVariableSpecifications().get(0).getName());
        assertEquals(Boolean.TRUE, response.getSteps().get(0).getVariableSpecifications().get(0).getRequired());

        assertEquals("TSO-UNIX-REXX_Execution", response.getSteps().get(1).getName());
        assertEquals("A step that runs a UNIX REXX exec program.", response.getSteps().get(1).getTitle());
        assertEquals(Boolean.FALSE, response.getSteps().get(1).getOptional());
        assertEquals(3, response.getSteps().get(1).getVariableSpecifications().size());
        assertEquals("st_user", response.getSteps().get(1).getVariableSpecifications().get(1).getName());

        assertEquals("TSO-TSO-REXX_Execution", response.getSteps().get(2).getName());
        assertEquals("A step that runs a REXX exec program.", response.getSteps().get(2).getTitle());
        assertEquals(Boolean.FALSE, response.getSteps().get(2).getOptional());
        assertEquals("procNameVariable", response.getSteps().get(2).getVariableSpecifications().get(2).getName());

        assertEquals(3, response.getVariables().size());
        assertEquals("procNameVariable", response.getVariables().get(0).getName());
        assertEquals("instance", response.getVariables().get(0).getScope());
        assertEquals("TSO procName", response.getVariables().get(0).getCategory());

        assertEquals("st_group", response.getVariables().get(1).getName());
        assertEquals("instance", response.getVariables().get(1).getScope());
        assertEquals("Started", response.getVariables().get(1).getCategory());
        assertEquals("SYS1", response.getVariables().get(1).getDefaultValue());
        assertEquals("GROUP", response.getVariables().get(1).getValidationType());
        assertEquals("Group name for the started task.", response.getVariables().get(1).getAbstractInfo());

        assertEquals("st_user", response.getVariables().get(2).getName());
        assertEquals("MYSTUSER", response.getVariables().get(2).getDefaultValue());
        assertEquals("USERID", response.getVariables().get(2).getValidationType());
        assertEquals("User ID for the started task.", response.getVariables().get(2).getAbstractInfo());
    }

    @Test
    public void tstWorkflowGetByDefinitionFilePathSuccess() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        GetJsonZosmfRequest mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(getWorkflowDefinitionJson(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        WorkflowGet workflowGet = new WorkflowGet(connection, mockGetRequest);
        WorkflowGetDefinitionResponse response = workflowGet.getDefinition(DEFINITION_FILE_PATH);

        String expectedUrl = "https://1:443/workflow/rest/1.0/workflowDefinition" +
                "?definitionFilePath=" + EncodeUtils.encodeURIComponent(DEFINITION_FILE_PATH);
        assertEquals(expectedUrl, mockGetRequest.getUrl());
        assertEquals("programExecutionSample", response.getWorkflowID());
    }

    @Test
    public void tstWorkflowGetByDefinitionFilePathAndSystemSuccess() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        GetJsonZosmfRequest mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(getWorkflowDefinitionJson(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        WorkflowGet workflowGet = new WorkflowGet(connection, mockGetRequest);
        workflowGet.getDefinition(DEFINITION_FILE_PATH, "SY1");

        String expectedUrl = "https://1:443/workflow/rest/1.0/workflowDefinition" +
                "?definitionFilePath=" + EncodeUtils.encodeURIComponent(DEFINITION_FILE_PATH) +
                "&workflowDefinitionFileSystem=" + EncodeUtils.encodeURIComponent("SY1");
        assertEquals(expectedUrl, mockGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowGetUrlGeneration() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        GetJsonZosmfRequest mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(getWorkflowDefinitionJson(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        WorkflowGet workflowGet = new WorkflowGet(connection, mockGetRequest);
        workflowGet.getDefinitionCommon(WorkflowGetDefinitionInputData.builder()
                .definitionFilePath(DEFINITION_FILE_PATH)
                .returnSteps(true)
                .build());

        String expectedUrl = "https://1:443/zosmf/workflow/rest/1.0/workflowDefinition" +
                "?definitionFilePath=" + EncodeUtils.encodeURIComponent(DEFINITION_FILE_PATH) +
                "&returnData=steps";
        assertEquals(expectedUrl, mockGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowGetWithNullInputData() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        WorkflowGet workflowGet = new WorkflowGet(connection);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> workflowGet.getDefinitionCommon(null));
        assertEquals("definitionInputData is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetWithNullDefinitionFilePath() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        WorkflowGet workflowGet = new WorkflowGet(connection);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workflowGet.getDefinition(null));
        assertEquals("definitionFilePath is either null or empty", exception.getMessage());
    }

    private static final String WORKFLOW_KEY = "7a2263a7-7c91-40b4-8892-2a4342a222c3";

    private static String getWorkflowPropertiesJson() {
        return "{\n" +
                "  \"workflowName\": \"testProgramExecutionSample\",\n" +
                "  \"workflowKey\": \"7a2263a7-7c91-40b4-8892-2a4342a222c3\",\n" +
                "  \"workflowID\": \"programExecutionSample\",\n" +
                "  \"owner\": \"zosmfad\",\n" +
                "  \"system\": \"PLEX1.SY1\",\n" +
                "  \"statusName\": \"in-progress\",\n" +
                "  \"access\": \"Public\",\n" +
                "  \"percentComplete\": 0\n" +
                "}";
    }

    @Test
    public void tstWorkflowGetPropertiesByKeySuccess() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        GetJsonZosmfRequest mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(getWorkflowPropertiesJson(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        WorkflowGet workflowGet = new WorkflowGet(connection, mockGetRequest);
        WorkflowGetPropertiesResponse response = workflowGet.getProperties(WORKFLOW_KEY);

        String expectedUrl = "https://1:443/workflow/rest/1.0/workflows/" +
                EncodeUtils.encodeURIComponent(WORKFLOW_KEY);
        assertEquals(expectedUrl, mockGetRequest.getUrl());
        assertEquals("testProgramExecutionSample", response.getWorkflowName());
        assertEquals(WORKFLOW_KEY, response.getWorkflowKey());
        assertEquals("programExecutionSample", response.getWorkflowID());
        assertEquals("zosmfad", response.getOwner());
        assertEquals("PLEX1.SY1", response.getSystem());
        assertEquals("Public", response.getAccess());
        assertEquals("in-progress", response.getStatusName());
        assertEquals(0, response.getPercentComplete().intValue());
    }

    @Test
    public void tstWorkflowGetPropertiesCommonReturnStepsAndVariablesUrlGeneration() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443/zosmf");
        GetJsonZosmfRequest mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(getWorkflowPropertiesJson(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        WorkflowGet workflowGet = new WorkflowGet(connection, mockGetRequest);
        workflowGet.getPropertiesCommon(WorkflowGetPropertiesInputData.builder()
                .workflowKey(WORKFLOW_KEY)
                .returnSteps(true)
                .returnVariables(true)
                .build());

        String expectedUrl = "https://1:443/zosmf/workflow/rest/1.0/workflows/" +
                EncodeUtils.encodeURIComponent(WORKFLOW_KEY) + "?returnData=steps,variables";
        assertEquals(expectedUrl, mockGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowGetPropertiesCommonReturnStepsOnlyUrlGeneration() throws ZosmfRequestException {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        Mockito.when(connection.getZosmfUrl()).thenReturn("https://1:443");
        GetJsonZosmfRequest mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(getWorkflowPropertiesJson(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        WorkflowGet workflowGet = new WorkflowGet(connection, mockGetRequest);
        workflowGet.getPropertiesCommon(WorkflowGetPropertiesInputData.builder()
                .workflowKey(WORKFLOW_KEY)
                .returnSteps(true)
                .build());

        String expectedUrl = "https://1:443/workflow/rest/1.0/workflows/" +
                EncodeUtils.encodeURIComponent(WORKFLOW_KEY) + "?returnData=steps";
        assertEquals(expectedUrl, mockGetRequest.getUrl());
    }

    @Test
    public void tstWorkflowGetPropertiesWithNullInputData() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        WorkflowGet workflowGet = new WorkflowGet(connection);
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> workflowGet.getPropertiesCommon(null));
        assertEquals("propertiesInputData is null", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetPropertiesWithNullWorkflowKey() {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
        WorkflowGet workflowGet = new WorkflowGet(connection);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> workflowGet.getProperties(null));
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

}
