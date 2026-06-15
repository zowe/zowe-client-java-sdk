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
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Class containing unit tests for WorkflowListArchivedInputData.
 */
public class WorkflowListArchivedInputDataTest {

    @Test
    public void tstWorkflowListArchivedInputDataBuilderSuccess() {
        final WorkflowListArchivedInputData inputData = WorkflowListArchivedInputData.builder()
                .orderBy("desc")
                .view("user")
                .build();

        assertNotNull(inputData);
        assertEquals("desc", inputData.getOrderBy());
        assertEquals("user", inputData.getView());
    }

    @Test
    public void tstWorkflowListArchivedInputDataBuilderEmptySuccess() {
        final WorkflowListArchivedInputData inputData = WorkflowListArchivedInputData.builder()
                .build();

        assertNotNull(inputData);
        assertNull(inputData.getOrderBy());
        assertNull(inputData.getView());
    }

}