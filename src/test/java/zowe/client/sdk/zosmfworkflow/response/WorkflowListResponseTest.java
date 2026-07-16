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
 * Class containing unit tests for WorkflowListResponse.
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
public class WorkflowListResponseTest {

    @Test
    public void tstWorkflowListResponseConstructorSuccess() {
        final WorkflowListResponse response = new WorkflowListResponse(
                "my-workflow",
                "abc-key-123",
                "my workflow description",
                "automationSample",
                "1.0",
                "a8825b7497793bc620b0edffa8b97cd9",
                "/zosmf/workflow/rest/1.0/workflows/abc-key-123",
                "zosmfad",
                "IBM",
                "Public");

        assertNotNull(response);
        assertEquals("my-workflow", response.getWorkflowName());
        assertEquals("abc-key-123", response.getWorkflowKey());
        assertEquals("my workflow description", response.getWorkflowDescription());
        assertEquals("automationSample", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("a8825b7497793bc620b0edffa8b97cd9", response.getWorkflowDefinitionFileMD5Value());
        assertEquals("/zosmf/workflow/rest/1.0/workflows/abc-key-123", response.getInstanceURI());
        assertEquals("zosmfad", response.getOwner());
        assertEquals("IBM", response.getVendor());
        assertEquals("Public", response.getAccess());
        assertEquals("WorkflowListResponse{workflowName='my-workflow', workflowKey='abc-key-123'," +
                        " workflowDescription='my workflow description', workflowID='automationSample'," +
                        " workflowVersion='1.0', workflowDefinitionFileMD5Value='a8825b7497793bc620b0edffa8b97cd9'," +
                        " instanceURI='/zosmf/workflow/rest/1.0/workflows/abc-key-123', owner='zosmfad'," +
                        " vendor='IBM', access='Public'}",
                response.toString());
    }

    @Test
    public void tstWorkflowListResponseNullValuesDefaultToEmptySuccess() {
        final WorkflowListResponse response = new WorkflowListResponse(
                null, null, null, null, null, null, null, null, null, null);

        assertEquals("", response.getWorkflowName());
        assertEquals("", response.getWorkflowKey());
        assertEquals("", response.getWorkflowDescription());
        assertEquals("", response.getWorkflowID());
        assertEquals("", response.getWorkflowVersion());
        assertEquals("", response.getWorkflowDefinitionFileMD5Value());
        assertEquals("", response.getInstanceURI());
        assertEquals("", response.getOwner());
        assertEquals("", response.getVendor());
        assertEquals("", response.getAccess());
    }

    @Test
    public void tstWorkflowListResponseParseSuccess() throws Exception {
        final WorkflowListResponse response = JsonUtils.parseResponse(
                "{\"instanceURI\":\"/zosmf/workflow/rest/1.0/workflows/d043b5f1-adab-48e7-b7c3-d41cd95fa4b0\"," +
                        "\"owner\":\"zosmfad\",\"vendor\":\"IBM\"," +
                        "\"workflowDefinitionFileMD5Value\":\"a8825b7497793bc620b0edffa8b97cd9\"," +
                        "\"workflowDescription\":\"Sample demonstrating the use of automated steps in workflow.\"," +
                        "\"workflowID\":\"automationSample\"," +
                        "\"workflowKey\":\"d043b5f1-adab-48e7-b7c3-d41cd95fa4b0\"," +
                        "\"workflowName\":\"AutomationExample|Canceled|1423679433714\"," +
                        "\"workflowVersion\":\"1.0\",\"access\":\"Public\"}",
                WorkflowListResponse.class,
                "test");

        assertNotNull(response);
        assertEquals("AutomationExample|Canceled|1423679433714", response.getWorkflowName());
        assertEquals("d043b5f1-adab-48e7-b7c3-d41cd95fa4b0", response.getWorkflowKey());
        assertEquals("Sample demonstrating the use of automated steps in workflow.",
                response.getWorkflowDescription());
        assertEquals("automationSample", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("a8825b7497793bc620b0edffa8b97cd9", response.getWorkflowDefinitionFileMD5Value());
        assertEquals("/zosmf/workflow/rest/1.0/workflows/d043b5f1-adab-48e7-b7c3-d41cd95fa4b0",
                response.getInstanceURI());
        assertEquals("zosmfad", response.getOwner());
        assertEquals("IBM", response.getVendor());
        assertEquals("Public", response.getAccess());
    }

    @Test
    public void tstWorkflowListResponseParseUnknownFieldIgnoredSuccess() throws Exception {
        final WorkflowListResponse response = JsonUtils.parseResponse(
                "{\"workflowName\":\"my-workflow\",\"someNewField\":\"value\"}",
                WorkflowListResponse.class,
                "test");

        assertNotNull(response);
        assertEquals("my-workflow", response.getWorkflowName());
        assertEquals("", response.getWorkflowKey());
    }

}
