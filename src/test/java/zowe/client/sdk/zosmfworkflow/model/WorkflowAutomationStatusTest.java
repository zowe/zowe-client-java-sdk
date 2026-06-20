/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.model;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.JsonUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowAutomationStatus.
 */
public class WorkflowAutomationStatusTest {

    @Test
    public void tstWorkflowAutomationStatusConstructorSuccess() {
        final WorkflowAutomationStatus status = new WorkflowAutomationStatus(
                "zosmfad", 1584038400000L, 1584038460000L, "step1", "1", "Step One", "IZUWF0001I", "stopped");

        assertEquals("zosmfad", status.getStartUser());
        assertEquals(1584038400000L, status.getStartedTime());
        assertEquals(1584038460000L, status.getStoppedTime());
        assertEquals("step1", status.getCurrentStepName());
        assertEquals("1", status.getCurrentStepNumber());
        assertEquals("Step One", status.getCurrentStepTitle());
        assertEquals("IZUWF0001I", status.getMessageID());
        assertEquals("stopped", status.getMessageText());
    }

    @Test
    public void tstWorkflowAutomationStatusNullDefaultsSuccess() {
        final WorkflowAutomationStatus status =
                new WorkflowAutomationStatus(null, null, null, null, null, null, null, null);

        assertEquals("", status.getStartUser());
        assertNull(status.getStartedTime());
        assertNull(status.getStoppedTime());
        assertEquals("", status.getCurrentStepName());
        assertEquals("", status.getMessageText());
    }

    @Test
    public void tstWorkflowAutomationStatusParseSuccess() throws Exception {
        final WorkflowAutomationStatus status = JsonUtils.parseResponse(
                "{\"startUser\":\"zosmfad\",\"startedTime\":1584038400000,\"stoppedTime\":null," +
                        "\"currentStepName\":\"step1\",\"currentStepNumber\":\"1\",\"currentStepTitle\":\"Step One\"}",
                WorkflowAutomationStatus.class, "test");

        assertEquals("zosmfad", status.getStartUser());
        assertEquals(1584038400000L, status.getStartedTime());
        assertNull(status.getStoppedTime());
        assertEquals("step1", status.getCurrentStepName());
    }

}
