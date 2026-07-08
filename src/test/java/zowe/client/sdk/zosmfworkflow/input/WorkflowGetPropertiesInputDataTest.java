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
 * Class containing unit tests for WorkflowGetPropertiesInputData.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class WorkflowGetPropertiesInputDataTest {

    @Test
    public void tstWorkflowGetPropertiesInputDataBuilderSuccess() {
        final WorkflowGetPropertiesInputData inputData = WorkflowGetPropertiesInputData.builder()
                .workflowKey("7a2263a7-7c91-40b4-8892-2a4342a222c3")
                .returnSteps(true)
                .returnVariables(true)
                .build();

        assertNotNull(inputData);
        assertTrue(inputData.getWorkflowKey().isPresent());
        assertEquals("7a2263a7-7c91-40b4-8892-2a4342a222c3", inputData.getWorkflowKey().get());
        assertTrue(inputData.isReturnSteps());
        assertTrue(inputData.isReturnVariables());
    }

    @Test
    public void tstWorkflowGetPropertiesInputDataBuilderDefaultsSuccess() {
        final WorkflowGetPropertiesInputData inputData = WorkflowGetPropertiesInputData.builder()
                .workflowKey("KEY1")
                .build();

        assertEquals("KEY1", inputData.getWorkflowKey().get());
        assertFalse(inputData.isReturnSteps());
        assertFalse(inputData.isReturnVariables());
    }

    @Test
    public void tstWorkflowGetPropertiesInputDataNullWorkflowKeyThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> WorkflowGetPropertiesInputData.builder().build());
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetPropertiesInputDataEmptyWorkflowKeyThrows() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> WorkflowGetPropertiesInputData.builder().workflowKey("").build());
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void tstWorkflowGetPropertiesInputDataToStringSuccess() {
        final WorkflowGetPropertiesInputData inputData = WorkflowGetPropertiesInputData.builder()
                .workflowKey("KEY1")
                .returnSteps(true)
                .returnVariables(false)
                .build();

        assertEquals("WorkflowGetPropertiesInputData{workflowKey='KEY1', returnSteps=true, returnVariables=false}",
                inputData.toString());
    }

}
