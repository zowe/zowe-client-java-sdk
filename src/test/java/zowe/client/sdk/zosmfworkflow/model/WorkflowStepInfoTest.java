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
 * Class containing unit tests for the WorkflowStepInfo deduction hierarchy.
 */
public class WorkflowStepInfoTest {

    @Test
    public void tstWorkflowStepInfoDeducesTemplateStepSuccess() throws Exception {
        final WorkflowStepInfo step = JsonUtils.parseResponse(
                "{\"name\":\"step1\",\"title\":\"Template step\",\"stepNumber\":\"1\",\"state\":\"Ready\"," +
                        "\"isRestStep\":false,\"submitAs\":\"JCL\",\"template\":\"//JOB\",\"maxLrecl\":1024," +
                        "\"procName\":\"IZUFPROC\"}",
                WorkflowStepInfo.class, "test");

        assertInstanceOf(WorkflowTemplateStepInfo.class, step);
        assertEquals("step1", step.getName());
        assertEquals("JCL", ((WorkflowTemplateStepInfo) step).getSubmitAs());
        assertEquals("//JOB", ((WorkflowTemplateStepInfo) step).getTemplate());
    }

    @Test
    public void tstWorkflowStepInfoDeducesRestStepSuccess() throws Exception {
        final WorkflowStepInfo step = JsonUtils.parseResponse(
                "{\"name\":\"restStep\",\"title\":\"REST step\",\"stepNumber\":\"2\",\"state\":\"Ready\"," +
                        "\"isRestStep\":true,\"httpMethod\":\"GET\",\"hostname\":\"www.ibm.com\"," +
                        "\"schemeName\":\"https\",\"uriPath\":\"/zosmf/info\",\"actualStatusCode\":\"200\"," +
                        "\"expectedStatusCode\":\"200\",\"port\":\"443\"}",
                WorkflowStepInfo.class, "test");

        assertInstanceOf(WorkflowRestStepInfo.class, step);
        assertEquals("restStep", step.getName());
        assertEquals(Boolean.TRUE, step.getIsRestStep());
        final WorkflowRestStepInfo restStep = (WorkflowRestStepInfo) step;
        assertEquals("GET", restStep.getHttpMethod());
        assertEquals("www.ibm.com", restStep.getHostname());
        assertEquals("https", restStep.getSchemeName());
        assertEquals("/zosmf/info", restStep.getUriPath());
        assertEquals("200", restStep.getActualStatusCode());
    }

    @Test
    public void tstWorkflowStepInfoDeducesCallingStepSuccess() throws Exception {
        final WorkflowStepInfo step = JsonUtils.parseResponse(
                "{\"name\":\"callStep\",\"title\":\"Calling step\",\"stepNumber\":\"3\",\"state\":\"Ready\"," +
                        "\"isRestStep\":false,\"hasCalledWorkflow\":true,\"calledWorkflowID\":\"childWf\"," +
                        "\"calledWorkflowVersion\":\"1.0\",\"calledInstanceURI\":\"/zosmf/workflow/rest/1.0/workflows/abc\"," +
                        "\"calledWorkflowDescription\":\"child workflow\"}",
                WorkflowStepInfo.class, "test");

        assertInstanceOf(WorkflowCallingStepInfo.class, step);
        assertEquals("callStep", step.getName());
        final WorkflowCallingStepInfo callingStep = (WorkflowCallingStepInfo) step;
        assertEquals("childWf", callingStep.getCalledWorkflowID());
        assertEquals("1.0", callingStep.getCalledWorkflowVersion());
        assertEquals("/zosmf/workflow/rest/1.0/workflows/abc", callingStep.getCalledInstanceURI());
        assertEquals("child workflow", callingStep.getCalledWorkflowDescription());
    }

}
