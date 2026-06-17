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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowGetResponse.
 */
public class WorkflowGetResponseTest {

    @Test
    public void tstWorkflowGetResponseConstructorSuccess() {
        final WorkflowGetResponse response = new WorkflowGetResponse(
                "sample", "desc", "programExecutionSample", "1.0", "IBM",
                "5c5dd66eb3ca3cd1c578ccf323d57cc0", null, Boolean.FALSE, "none", null,
                "configuration", "ABC123", "Product ABC", "Version 1", null, Boolean.FALSE,
                null, null);

        assertNotNull(response);
        assertEquals("sample", response.getWorkflowDefaultName());
        assertEquals("desc", response.getWorkflowDescription());
        assertEquals("programExecutionSample", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
        assertEquals("5c5dd66eb3ca3cd1c578ccf323d57cc0", response.getWorkflowDefinitionFileMD5Value());
        assertEquals("", response.getIsCallable());
        assertEquals(Boolean.FALSE, response.getContainsParallelSteps());
        assertEquals("none", response.getScope());
        assertEquals("configuration", response.getCategory());
        assertEquals("ABC123", response.getProductID());
        assertEquals("Product ABC", response.getProductName());
        assertEquals("Version 1", response.getProductVersion());
        assertEquals(Boolean.FALSE, response.getIsInstanceVariableWithoutPrefix());
        assertTrue(response.getSteps().isEmpty());
        assertTrue(response.getVariables().isEmpty());
    }

    @Test
    public void tstWorkflowGetResponseParseWithStepsAndVariablesSuccess() throws Exception {
        final WorkflowGetResponse response = JsonUtils.parseResponse(
                "{\"workflowID\":\"programExecutionSample\",\"workflowVersion\":\"1.0\",\"vendor\":\"IBM\"," +
                        "\"category\":\"configuration\",\"scope\":\"none\",\"containsParallelSteps\":false," +
                        "\"steps\":[{\"name\":\"step1\",\"title\":\"Step One\",\"optional\":false," +
                        "\"variable-specifications\":[{\"name\":\"st_user\",\"scope\":\"instance\",\"required\":true}]}]," +
                        "\"variables\":[{\"name\":\"st_user\",\"scope\":\"instance\",\"type\":\"string\"," +
                        "\"default\":\"MYSTUSER\",\"abstract\":\"User ID.\",\"visibility\":\"private\"}]}",
                WorkflowGetResponse.class,
                "test");

        assertEquals("programExecutionSample", response.getWorkflowID());
        assertEquals("1.0", response.getWorkflowVersion());
        assertEquals("IBM", response.getVendor());
        assertEquals("configuration", response.getCategory());
        assertEquals("none", response.getScope());
        assertEquals(Boolean.FALSE, response.getContainsParallelSteps());

        assertEquals(1, response.getSteps().size());
        assertEquals("step1", response.getSteps().get(0).getName());
        assertEquals("Step One", response.getSteps().get(0).getTitle());
        assertEquals(1, response.getSteps().get(0).getVariableSpecifications().size());
        assertEquals("st_user", response.getSteps().get(0).getVariableSpecifications().get(0).getName());

        assertEquals(1, response.getVariables().size());
        assertEquals("st_user", response.getVariables().get(0).getName());
        assertEquals("string", response.getVariables().get(0).getType());
        assertEquals("MYSTUSER", response.getVariables().get(0).getDefaultValue());
        assertEquals("User ID.", response.getVariables().get(0).getAbstractInfo());
        assertEquals("private", response.getVariables().get(0).getVisibility());
    }

    @Test
    public void tstWorkflowGetResponseParseNullDefaultsSuccess() throws Exception {
        final WorkflowGetResponse response = JsonUtils.parseResponse(
                "{\"workflowID\":\"programExecutionSample\"}",
                WorkflowGetResponse.class,
                "test");

        assertEquals("programExecutionSample", response.getWorkflowID());
        assertEquals("", response.getVendor());
        assertEquals("", response.getWorkflowDefaultName());
        assertTrue(response.getSteps().isEmpty());
        assertTrue(response.getVariables().isEmpty());
    }

}
