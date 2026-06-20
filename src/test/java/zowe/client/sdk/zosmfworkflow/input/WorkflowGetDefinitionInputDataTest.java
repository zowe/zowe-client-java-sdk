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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowGetInputData.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class WorkflowGetDefinitionInputDataTest {

    @Test
    public void tstWorkflowGetInputDataBuilderSuccess() {
        final WorkflowGetDefinitionInputData inputData = WorkflowGetDefinitionInputData.builder()
                .definitionFilePath("/usr/lpp/zosmf/samples/workflow_sample_program_execution.xml")
                .workflowDefinitionFileSystem("SY1")
                .returnSteps(true)
                .returnVariables(true)
                .build();

        assertNotNull(inputData);
        assertTrue(inputData.getDefinitionFilePath().isPresent());
        assertEquals("/usr/lpp/zosmf/samples/workflow_sample_program_execution.xml",
                inputData.getDefinitionFilePath().get());
        assertTrue(inputData.getWorkflowDefinitionFileSystem().isPresent());
        assertEquals("SY1", inputData.getWorkflowDefinitionFileSystem().get());
        assertTrue(inputData.isReturnSteps());
        assertTrue(inputData.isReturnVariables());
    }

    @Test
    public void tstWorkflowGetInputDataBuilderDefaultsSuccess() {
        final WorkflowGetDefinitionInputData inputData = WorkflowGetDefinitionInputData.builder()
                .definitionFilePath("/tmp/workflow.xml")
                .build();

        assertEquals("/tmp/workflow.xml", inputData.getDefinitionFilePath().get());
        assertFalse(inputData.getWorkflowDefinitionFileSystem().isPresent());
        assertFalse(inputData.isReturnSteps());
        assertFalse(inputData.isReturnVariables());
    }

    @Test
    public void tstWorkflowGetInputDataBlankFileSystemReturnsEmptyOptional() {
        final WorkflowGetDefinitionInputData inputData = WorkflowGetDefinitionInputData.builder()
                .definitionFilePath("/tmp/workflow.xml")
                .workflowDefinitionFileSystem("  ")
                .build();

        assertTrue(inputData.getDefinitionFilePath().isPresent());
        assertFalse(inputData.getWorkflowDefinitionFileSystem().isPresent());
    }

    @Test
    public void tstWorkflowGetInputDataNullDefinitionFilePathThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> WorkflowGetDefinitionInputData.builder().build());
        assertEquals("definitionFilePath is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetInputDataEmptyDefinitionFilePathThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> WorkflowGetDefinitionInputData.builder().definitionFilePath("").build());
        assertEquals("definitionFilePath is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetInputDataToStringSuccess() {
        final WorkflowGetDefinitionInputData inputData = WorkflowGetDefinitionInputData.builder()
                .definitionFilePath("/tmp/workflow.xml")
                .workflowDefinitionFileSystem("SY1")
                .returnSteps(true)
                .returnVariables(false)
                .build();

        assertEquals("WorkflowGetInputData{definitionFilePath='/tmp/workflow.xml', " +
                        "workflowDefinitionFileSystem='SY1', returnSteps=true, returnVariables=false}",
                inputData.toString());
    }

}
