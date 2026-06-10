/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflow.response;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.JsonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class containing unit tests for WorkflowCreateResponse.
 */
public class WorkflowCreateResponseTest {

    @Test
    public void tstWorkflowCreateResponseConstructorSuccess() {
        final WorkflowCreateResponse response = new WorkflowCreateResponse(
                "workflow-key",
                "workflow-description",
                "workflow-id",
                "1.0",
                "IBM");

        assertNotNull(response);
        assertEquals("workflow-key", response.getWorkflowKey());
        assertEquals("workflow-description", response.getWorkflowDescription());
        assertEquals("workflow-id", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
        assertEquals("WorkflowCreateResponse{workflowKey='workflow-key', workflowDescription='workflow-description', workflowID='workflow-id', workflowVersion='1.0', vendor='IBM'}",
                response.toString());
    }

    @Test
    public void tstWorkflowCreateResponseParseSuccess() throws Exception {
        final WorkflowCreateResponse response = JsonUtils.parseResponse(
                "{\"workflowKey\":\"workflow-key\",\"workflowDescription\":\"workflow-description\",\"workflowID\":\"workflow-id\",\"workflowVersion\":\"1.0\",\"vendor\":\"IBM\"}",
                WorkflowCreateResponse.class,
                "test");

        assertEquals("workflow-key", response.getWorkflowKey());
        assertEquals("workflow-description", response.getWorkflowDescription());
        assertEquals("workflow-id", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
    }

}