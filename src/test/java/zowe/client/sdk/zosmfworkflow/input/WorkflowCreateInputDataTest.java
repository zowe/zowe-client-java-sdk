/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.input;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.zosmfworkflow.model.WorkflowVariable;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class containing unit tests for WorkflowCreateInputData.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowCreateInputDataTest {

    @Test
    public void tstWorkflowCreateInputDataBuilderSuccess() {
        final WorkflowCreateInputData inputData = WorkflowCreateInputData.builder()
                .workflowName("AutomationExample")
                .workflowDefinitionFile("/usr/lpp/zosmf/samples/workflow_sample_automation.xml")
                .workflowDefinitionFileSystem("SY1")
                .variableInputFile("/tmp/workflow.properties")
                .variables(Arrays.asList(
                        new WorkflowVariable("user_name", "IBMUSER"),
                        new WorkflowVariable("file_name", "textfile.txt")
                ))
                .resolveGlobalConflictByUsing("global")
                .system("SY1")
                .owner("zosmfad")
                .workflowArchiveSAFID("ZOSMFAD")
                .comments("created for testing")
                .assignToOwner(Boolean.TRUE)
                .accessType("Public")
                .accountInfo("ACCT123")
                .jobStatement(Arrays.asList("//TEST JOB (ACCT123)", "//*"))
                .deleteCompletedJobs(Boolean.FALSE)
                .jobsOutputDirectory("/u/IBMUSER/jobFiles")
                .autoDeleteOnCompletion(Boolean.FALSE)
                .targetSystemuid("remoteuser")
                .targetSystempwd("remotepwd")
                .build();

        assertNotNull(inputData);
        assertEquals("AutomationExample", inputData.getWorkflowName());
        assertEquals("/usr/lpp/zosmf/samples/workflow_sample_automation.xml", inputData.getWorkflowDefinitionFile());
        assertEquals("SY1", inputData.getWorkflowDefinitionFileSystem());
        assertEquals("/tmp/workflow.properties", inputData.getVariableInputFile());
        assertEquals(2, inputData.getVariables().size());
        assertEquals("global", inputData.getResolveGlobalConflictByUsing());
        assertEquals("SY1", inputData.getSystem());
        assertEquals("zosmfad", inputData.getOwner());
        assertEquals("ZOSMFAD", inputData.getWorkflowArchiveSAFID());
        assertEquals("created for testing", inputData.getComments());
        assertEquals(Boolean.TRUE, inputData.getAssignToOwner());
        assertEquals("Public", inputData.getAccessType());
        assertEquals("ACCT123", inputData.getAccountInfo());
        assertEquals(2, inputData.getJobStatement().size());
        assertEquals(Boolean.FALSE, inputData.getDeleteCompletedJobs());
        assertEquals("/u/IBMUSER/jobFiles", inputData.getJobsOutputDirectory());
        assertEquals(Boolean.FALSE, inputData.getAutoDeleteOnCompletion());
        assertEquals("remoteuser", inputData.getTargetSystemuid());
        assertEquals("remotepwd", inputData.getTargetSystempwd());
    }

    @Test
    public void tstWorkflowCreateInputDataToBuilderCopiesAndOverridesSuccess() {
        final WorkflowCreateInputData original = WorkflowCreateInputData.builder()
                .workflowName("AutomationExample")
                .workflowDefinitionFile("/local/workflow.xml")
                .variableInputFile("/local/vars.properties")
                .system("SY1")
                .owner("zosmfad")
                .accessType("Public")
                .build();

        final WorkflowCreateInputData copy = original.toBuilder()
                .workflowDefinitionFile("/tmp/workflow.xml")
                .variableInputFile("/tmp/vars.properties")
                .build();

        // overridden values
        assertEquals("/tmp/workflow.xml", copy.getWorkflowDefinitionFile());
        assertEquals("/tmp/vars.properties", copy.getVariableInputFile());
        // copied values
        assertEquals("AutomationExample", copy.getWorkflowName());
        assertEquals("SY1", copy.getSystem());
        assertEquals("zosmfad", copy.getOwner());
        assertEquals("Public", copy.getAccessType());
        // original is unchanged
        assertEquals("/local/workflow.xml", original.getWorkflowDefinitionFile());
    }

}
