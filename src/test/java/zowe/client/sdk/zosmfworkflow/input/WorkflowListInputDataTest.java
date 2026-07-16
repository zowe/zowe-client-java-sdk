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
import zowe.client.sdk.zosmfworkflow.types.CategoryType;
import zowe.client.sdk.zosmfworkflow.types.StatusNameType;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class containing unit tests for WorkflowListInputData.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowListInputDataTest {

    @Test
    public void tstWorkflowListInputDataBuilderSuccess() {
        final WorkflowListInputData inputData = WorkflowListInputData.builder()
                .workflowName("AutomationExample.*")
                .category(CategoryType.GENERAL)
                .system("SY1")
                .statusName(StatusNameType.COMPLETE)
                .owner("zosmfad")
                .vendor("IBM")
                .build();

        assertNotNull(inputData);
        assertEquals(Optional.of("AutomationExample.*"), inputData.getWorkflowName());
        assertEquals(Optional.of(CategoryType.GENERAL), inputData.getCategory());
        assertEquals(Optional.of("SY1"), inputData.getSystem());
        assertEquals(Optional.of(StatusNameType.COMPLETE), inputData.getStatusName());
        assertEquals(Optional.of("zosmfad"), inputData.getOwner());
        assertEquals(Optional.of("IBM"), inputData.getVendor());
    }

    @Test
    public void tstWorkflowListInputDataBuilderEmptySuccess() {
        final WorkflowListInputData inputData = WorkflowListInputData.builder().build();

        assertNotNull(inputData);
        assertEquals(Optional.empty(), inputData.getWorkflowName());
        assertEquals(Optional.empty(), inputData.getCategory());
        assertEquals(Optional.empty(), inputData.getSystem());
        assertEquals(Optional.empty(), inputData.getStatusName());
        assertEquals(Optional.empty(), inputData.getOwner());
        assertEquals(Optional.empty(), inputData.getVendor());
    }

    @Test
    public void tstWorkflowListInputDataToStringSuccess() {
        final WorkflowListInputData inputData = WorkflowListInputData.builder()
                .workflowName("test")
                .category(CategoryType.CONFIGURATION)
                .system("SY1")
                .statusName(StatusNameType.IN_PROGRESS)
                .owner("zosmfad")
                .vendor("IBM")
                .build();

        assertEquals("WorkflowListInputData{workflowName='test', category=CONFIGURATION, system='SY1'," +
                " statusName=IN_PROGRESS, owner='zosmfad', vendor='IBM'}", inputData.toString());
    }

}
