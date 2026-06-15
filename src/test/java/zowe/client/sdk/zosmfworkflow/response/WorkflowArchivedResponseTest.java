/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.response;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.JsonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class containing unit tests for WorkflowArchivedResponse.
 */
public class WorkflowArchivedResponseTest {

    @Test
    public void tstWorkflowArchivedResponseConstructorSuccess() {
        final WorkflowArchivedResponse response = new WorkflowArchivedResponse(
                "my-workflow",
                "abc-key-123",
                "/zosmf/workflow/rest/1.0/archivedworkflows/abc-key-123");

        assertNotNull(response);
        assertEquals("my-workflow", response.getName());
        assertEquals("abc-key-123", response.getKey());
        assertEquals("/zosmf/workflow/rest/1.0/archivedworkflows/abc-key-123",
                response.getArchivedInstanceURI());
        assertEquals("WorkflowArchivedResponse{name='my-workflow', key='abc-key-123'," +
                        " archivedInstanceURI='/zosmf/workflow/rest/1.0/archivedworkflows/abc-key-123'}",
                response.toString());
    }

    @Test
    public void tstWorkflowArchivedResponseParseSuccess() throws Exception {
        final WorkflowArchivedResponse response = JsonUtils.parseResponse(
                "{\"workflowName\":\"my-workflow\",\"workflowKey\":\"abc-key-123\"," +
                        "\"archivedInstanceURI\":\"/zosmf/workflow/rest/1.0/archivedworkflows/abc-key-123\"}",
                WorkflowArchivedResponse.class,
                "test");

        assertEquals("my-workflow", response.getName());
        assertEquals("abc-key-123", response.getKey());
        assertEquals("/zosmf/workflow/rest/1.0/archivedworkflows/abc-key-123",
                response.getArchivedInstanceURI());
    }

}