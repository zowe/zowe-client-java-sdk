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
import zowe.client.sdk.zosmfworkflow.types.ConflictResolutionType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowStartInputData.
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class WorkflowStartInputDataTest {

    @Test
    public void testWorkflowStartInputDataBuilderSuccess() {
        final WorkflowStartInputData inputData = new WorkflowStartInputData.Builder("workflow-key-123")
                .resolveConflictByUsing(ConflictResolutionType.OUTPUT_FILE_VALUE)
                .stepName("Step1")
                .performSubsequent(Boolean.TRUE)
                .notificationUrl("https://example.com/notification")
                .targetSystemuid("remoteuser")
                .targetSystempwd("remotepwd")
                .build();

        assertNotNull(inputData);
        assertEquals("workflow-key-123", inputData.getWorkflowKey());
        assertEquals("outputFileValue", inputData.getResolveConflictByUsing());
        assertEquals("Step1", inputData.getStepName());
        assertEquals(Boolean.TRUE, inputData.getPerformSubsequent());
        assertEquals("https://example.com/notification", inputData.getNotificationUrl());
        assertEquals("remoteuser", inputData.getTargetSystemuid());
        assertEquals("remotepwd", inputData.getTargetSystempwd());
    }

    @Test
    public void testWorkflowStartInputDataBuilderWithMinimalParameters() {
        final WorkflowStartInputData inputData = new WorkflowStartInputData.Builder("workflow-key-123").build();

        assertNotNull(inputData);
        assertEquals("workflow-key-123", inputData.getWorkflowKey());
    }

    @Test
    public void testWorkflowStartInputDataWithNullWorkflowKey() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WorkflowStartInputData.Builder(null).build()
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void testWorkflowStartInputDataWithEmptyWorkflowKey() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new WorkflowStartInputData.Builder("").build()
        );
        assertEquals("workflowKey is either null or empty", exception.getMessage());
    }

    @Test
    public void testAllConflictResolutionTypes() {
        final WorkflowStartInputData outputFileData = new WorkflowStartInputData.Builder("key1")
                .resolveConflictByUsing(ConflictResolutionType.OUTPUT_FILE_VALUE)
                .build();
        assertEquals("outputFileValue", outputFileData.getResolveConflictByUsing());

        final WorkflowStartInputData existingValueData = new WorkflowStartInputData.Builder("key2")
                .resolveConflictByUsing(ConflictResolutionType.EXISTING_VALUE)
                .build();
        assertEquals("existingValue", existingValueData.getResolveConflictByUsing());

        final WorkflowStartInputData leaveConflictData = new WorkflowStartInputData.Builder("key3")
                .resolveConflictByUsing(ConflictResolutionType.LEAVE_CONFLICT)
                .build();
        assertEquals("leaveConflict", leaveConflictData.getResolveConflictByUsing());
    }

}
