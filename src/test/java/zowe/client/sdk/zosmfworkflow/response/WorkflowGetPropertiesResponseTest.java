/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.response;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.zosmfworkflow.model.WorkflowStepInfo;
import zowe.client.sdk.zosmfworkflow.model.WorkflowTemplateStepInfo;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowGetPropertiesResponse.
 */
public class WorkflowGetPropertiesResponseTest {

    /**
     * Full workflow properties response document from the example in the IBM z/OSMF REST API
     * documentation for the get properties of a workflow operation (returnData=steps,variables).
     */
    private static String getWorkflowPropertiesJson() {
        return "{\n" +
                "  \"access\": \"Public\",\n" +
                "  \"productID\": \"ABC123\",\n" +
                "  \"jobStatement\": null,\n" +
                "  \"deleteCompletedJobs\": false,\n" +
                "  \"productName\": \"Product ABC\",\n" +
                "  \"globalVariableGroup\": null,\n" +
                "  \"productVersion\": \"Version 1\",\n" +
                "  \"jobsOutputDirectory\": null,\n" +
                "  \"vendor\": \"IBM\",\n" +
                "  \"scope\": \"none\",\n" +
                "  \"statusName\": \"in-progress\",\n" +
                "  \"workflowID\": \"programExecutionSample\",\n" +
                "  \"owner\": \"zosmfad\",\n" +
                "  \"accountInfo\": null,\n" +
                "  \"isInstanceVariableWithoutPrefix\": false,\n" +
                "  \"variables\": [\n" +
                "    {\"visibility\": \"private\", \"scope\": \"instance\", \"name\": \"procNameVariable\", \"type\": \"string\", \"value\": null},\n" +
                "    {\"visibility\": \"private\", \"scope\": \"instance\", \"name\": \"st_group\", \"type\": \"string\", \"value\": null},\n" +
                "    {\"visibility\": \"private\", \"scope\": \"instance\", \"name\": \"st_user\", \"type\": \"string\", \"value\": null}\n" +
                "  ],\n" +
                "  \"workflowName\": \"testProgramExecutionSample\",\n" +
                "  \"automationStatus\": null,\n" +
                "  \"autoDeleteOnCompletion\": false,\n" +
                "  \"percentComplete\": 0,\n" +
                "  \"workflowDescription\": \"Sample that demonstrates how to run an executable program from a step.\",\n" +
                "  \"steps\": [\n" +
                buildTemplateStep("TSO-UNIX-shell_Execution", "A step that runs a UNIX shell script.",
                        "TSO-UNIX-shell", "1") + ",\n" +
                buildTemplateStep("TSO-UNIX-REXX_Execution", "A step that runs a UNIX REXX exec program.",
                        "TSO-UNIX-REXX", "2") + ",\n" +
                buildTemplateStep("TSO-TSO-REXX_Execution", "A step that runs a REXX exec program.",
                        "TSO-REXX", "3") + "\n" +
                "  ],\n" +
                "  \"containsParallelSteps\": false,\n" +
                "  \"workflowDefinitionFileMD5Value\": \"5c5dd66eb3ca3cd1c578ccf323d57cc0\",\n" +
                "  \"isCallable\": null,\n" +
                "  \"system\": \"PLEX1.SY1\",\n" +
                "  \"workflowKey\": \"7a2263a7-7c91-40b4-8892-2a4342a222c3\",\n" +
                "  \"workflowVersion\": \"1.0\",\n" +
                "  \"category\": \"configuration\"\n" +
                "}";
    }

    private static String buildTemplateStep(final String name, final String title, final String submitAs,
                                            final String stepNumber) {
        return "    {\n" +
                "      \"template\": \"inline program sample\",\n" +
                "      \"instructions\": \"This step outputs some variables and prints a few words.\",\n" +
                "      \"maxLrecl\": 1024,\n" +
                "      \"failedPattern\": [\"failed.*\"],\n" +
                "      \"assignees\": \"zosmfad\",\n" +
                "      \"description\": \"In this step, you submit an inline program for immediate processing.\",\n" +
                "      \"outputVariablesPrefix\": \"prefix:\",\n" +
                "      \"variable-references\": [\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_group\"},\n" +
                "        {\"scope\": \"instance\", \"name\": \"st_user\"},\n" +
                "        {\"scope\": \"instance\", \"name\": \"procNameVariable\"}\n" +
                "      ],\n" +
                "      \"saveAsUnixFileSub\": true,\n" +
                "      \"procName\": \"${instance-procNameVariable}\",\n" +
                "      \"title\": \"" + title + "\",\n" +
                "      \"jobInfo\": null,\n" +
                "      \"timeout\": 60,\n" +
                "      \"regionSize\": 50000,\n" +
                "      \"skills\": \"System Programmer\",\n" +
                "      \"isRestStep\": false,\n" +
                "      \"output\": null,\n" +
                "      \"outputSub\": false,\n" +
                "      \"returnCode\": null,\n" +
                "      \"outputSysoutDD\": false,\n" +
                "      \"successPattern\": \"success.*\",\n" +
                "      \"state\": \"Ready\",\n" +
                "      \"templateSub\": true,\n" +
                "      \"owner\": \"zosmfad\",\n" +
                "      \"autoEnable\": false,\n" +
                "      \"submitAs\": \"" + submitAs + "\",\n" +
                "      \"userDefined\": false,\n" +
                "      \"weight\": \"1\",\n" +
                "      \"optional\": false,\n" +
                "      \"steps\": null,\n" +
                "      \"scriptParameters\": \"para1\",\n" +
                "      \"saveAsUnixFile\": \"/u/${instance-st_user}/savedStuff/myScript.sh\",\n" +
                "      \"instructionsSub\": false,\n" +
                "      \"saveAsDatasetSub\": false,\n" +
                "      \"isConditionStep\": false,\n" +
                "      \"prereqStep\": null,\n" +
                "      \"hasCalledWorkflow\": false,\n" +
                "      \"name\": \"" + name + "\",\n" +
                "      \"stepNumber\": \"" + stepNumber + "\",\n" +
                "      \"saveAsDataset\": null\n" +
                "    }";
    }

    @Test
    public void tstWorkflowGetPropertiesResponseParseSuccess() throws Exception {
        final WorkflowGetPropertiesResponse response = JsonUtils.parseResponse(
                getWorkflowPropertiesJson(), WorkflowGetPropertiesResponse.class, "test");

        assertEquals("testProgramExecutionSample", response.getWorkflowName());
        assertEquals("7a2263a7-7c91-40b4-8892-2a4342a222c3", response.getWorkflowKey());
        assertEquals("programExecutionSample", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
        assertEquals("zosmfad", response.getOwner());
        assertEquals("PLEX1.SY1", response.getSystem());
        assertEquals("Public", response.getAccess());
        assertEquals("in-progress", response.getStatusName());
        assertEquals("none", response.getScope());
        assertEquals("configuration", response.getCategory());
        assertEquals("Product ABC", response.getProductName());
        assertEquals("ABC123", response.getProductID());
        assertEquals("Version 1", response.getProductVersion());
        assertEquals("5c5dd66eb3ca3cd1c578ccf323d57cc0", response.getWorkflowDefinitionFileMD5Value());
        assertEquals(0, response.getPercentComplete().intValue());
        assertEquals(Boolean.FALSE, response.getContainsParallelSteps());
        assertEquals(Boolean.FALSE, response.getDeleteCompletedJobs());
        assertEquals(Boolean.FALSE, response.getAutoDeleteOnCompletion());
        assertEquals("false", response.getIsInstanceVariableWithoutPrefix());
        assertNull(response.getIsCallable());
        assertNull(response.getAutomationStatus());
        // null string fields default to empty string
        assertEquals("", response.getJobStatement());
        assertEquals("", response.getAccountInfo());
        assertEquals("", response.getGlobalVariableGroup());

        assertEquals(3, response.getVariables().size());
        assertEquals("procNameVariable", response.getVariables().get(0).getName());
        assertEquals("instance", response.getVariables().get(0).getScope());
        assertEquals("string", response.getVariables().get(0).getType());
        assertEquals("private", response.getVariables().get(0).getVisibility());
        assertEquals("st_user", response.getVariables().get(2).getName());
    }

    @Test
    public void tstWorkflowGetPropertiesResponseStepsDeducedAsTemplateSuccess() throws Exception {
        final WorkflowGetPropertiesResponse response = JsonUtils.parseResponse(
                getWorkflowPropertiesJson(), WorkflowGetPropertiesResponse.class, "test");

        assertEquals(3, response.getSteps().size());

        final WorkflowStepInfo first = response.getSteps().get(0);
        assertInstanceOf(WorkflowTemplateStepInfo.class, first);
        assertEquals("TSO-UNIX-shell_Execution", first.getName());
        assertEquals("A step that runs a UNIX shell script.", first.getTitle());
        assertEquals("1", first.getStepNumber());
        assertEquals("Ready", first.getState());
        assertEquals(Boolean.FALSE, first.getOptional());
        assertEquals(Boolean.FALSE, first.getIsRestStep());
        assertEquals("zosmfad", first.getOwner());
        assertEquals("1", first.getWeight());

        final WorkflowTemplateStepInfo templateStep = (WorkflowTemplateStepInfo) first;
        assertEquals("TSO-UNIX-shell", templateStep.getSubmitAs());
        assertEquals("${instance-procNameVariable}", templateStep.getProcName());
        assertEquals(1024, templateStep.getMaxLrecl().intValue());
        assertEquals("60", templateStep.getTimeout());
        assertEquals("50000", templateStep.getRegionSize());
        assertEquals("success.*", templateStep.getSuccessPattern());
        assertEquals(1, templateStep.getFailedPattern().size());
        assertEquals("failed.*", templateStep.getFailedPattern().get(0));
        assertNull(templateStep.getJobInfo());
        assertEquals(3, templateStep.getVariableReferences().size());
        assertEquals("st_group", templateStep.getVariableReferences().get(0).getName());
        assertEquals("instance", templateStep.getVariableReferences().get(0).getScope());

        assertInstanceOf(WorkflowTemplateStepInfo.class, response.getSteps().get(1));
        assertEquals("TSO-UNIX-REXX_Execution", response.getSteps().get(1).getName());
        assertInstanceOf(WorkflowTemplateStepInfo.class, response.getSteps().get(2));
        assertEquals("TSO-TSO-REXX_Execution", response.getSteps().get(2).getName());
    }

}
