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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class containing unit tests for WorkflowStartInputData.
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class WorkflowStartInputDataTest {

    @Test
    public void testWorkflowStartInputDataBuilderSuccess() {
        final WorkflowStartInputData inputData = WorkflowStartInputData.builder()
                .resolveConflictByUsing("outputFileValue")
                .stepName("Step1")
                .performSubsequent(Boolean.TRUE)
                .notificationUrl("https://example.com/notification")
                .targetSystemuid("remoteuser")
                .targetSystempwd("remotepwd")
                .build();

        assertNotNull(inputData);
        assertEquals("outputFileValue", inputData.getResolveConflictByUsing());
        assertEquals("Step1", inputData.getStepName());
        assertEquals(Boolean.TRUE, inputData.getPerformSubsequent());
        assertEquals("https://example.com/notification", inputData.getNotificationUrl());
        assertEquals("remoteuser", inputData.getTargetSystemuid());
        assertEquals("remotepwd", inputData.getTargetSystempwd());
    }

    @Test
    public void testWorkflowStartInputDataBuilderWithMinimalParameters() {
        final WorkflowStartInputData inputData = WorkflowStartInputData.builder().build();

        assertNotNull(inputData);
    }

}
