/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflow.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class containing unit tests for WorkflowVariable.
 */
public class WorkflowVariableTest {

    @Test
    public void tstWorkflowVariableSuccess() {
        final WorkflowVariable workflowVariable = new WorkflowVariable("user_name", "IBMUSER");

        assertNotNull(workflowVariable);
        assertEquals("user_name", workflowVariable.getName());
        assertEquals("IBMUSER", workflowVariable.getValue());
        assertEquals("WorkflowVariable{name='user_name', value='IBMUSER'}", workflowVariable.toString());
    }

}
