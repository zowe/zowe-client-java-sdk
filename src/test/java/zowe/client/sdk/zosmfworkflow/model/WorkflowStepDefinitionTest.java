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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for WorkflowStepDefinition.
 */
public class WorkflowStepDefinitionTest {

    @Test
    public void tstWorkflowStepDefinitionConstructorSuccess() {
        final WorkflowStepVariableSpecification spec =
                new WorkflowStepVariableSpecification("st_user", "instance", Boolean.TRUE);
        final WorkflowStepDefinition step = new WorkflowStepDefinition(
                "step1", "Step One", "first step", List.of("step0"), Boolean.FALSE,
                Collections.emptyList(), Collections.singletonList(spec));

        assertEquals("step1", step.getName());
        assertEquals("Step One", step.getTitle());
        assertEquals("first step", step.getDescription());
        assertEquals(1, step.getPrereqStep().size());
        assertEquals("step0", step.getPrereqStep().get(0));
        assertEquals(Boolean.FALSE, step.getOptional());
        assertTrue(step.getSteps().isEmpty());
        assertEquals(1, step.getVariableSpecifications().size());
        assertEquals("st_user", step.getVariableSpecifications().get(0).getName());
    }

    @Test
    public void tstWorkflowStepDefinitionNullDefaultsSuccess() {
        final WorkflowStepDefinition step =
                new WorkflowStepDefinition(null, null, null, null, null, null, null);

        assertEquals("", step.getName());
        assertEquals("", step.getTitle());
        assertEquals("", step.getDescription());
        assertEquals(Collections.emptyList(), step.getPrereqStep());
        assertNull(step.getOptional());
        assertEquals(Collections.emptyList(), step.getSteps());
        assertEquals(Collections.emptyList(), step.getVariableSpecifications());
    }

    @Test
    public void tstWorkflowStepDefinitionParseSuccess() throws Exception {
        final WorkflowStepDefinition step = JsonUtils.parseResponse(
                "{\"name\":\"TSO-UNIX-shell_Execution\",\"title\":\"A step that runs a UNIX shell script.\"," +
                        "\"description\":\"a description\",\"optional\":false,\"isRestStep\":false," +
                        "\"variable-specifications\":[{\"scope\":\"instance\",\"name\":\"st_group\",\"required\":true}]}",
                WorkflowStepDefinition.class,
                "test");

        assertEquals("TSO-UNIX-shell_Execution", step.getName());
        assertEquals("A step that runs a UNIX shell script.", step.getTitle());
        assertEquals("a description", step.getDescription());
        assertEquals(Boolean.FALSE, step.getOptional());
        assertEquals(1, step.getVariableSpecifications().size());
        assertEquals("st_group", step.getVariableSpecifications().get(0).getName());
        assertEquals("instance", step.getVariableSpecifications().get(0).getScope());
        assertEquals(Boolean.TRUE, step.getVariableSpecifications().get(0).getRequired());
    }

}
