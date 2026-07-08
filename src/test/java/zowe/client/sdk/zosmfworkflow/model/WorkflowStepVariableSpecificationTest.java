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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Class containing unit tests for WorkflowStepVariableSpecification.
 */
public class WorkflowStepVariableSpecificationTest {

    @Test
    public void tstWorkflowStepVariableSpecificationConstructorSuccess() {
        final WorkflowStepVariableSpecification spec =
                new WorkflowStepVariableSpecification("st_user", "instance", Boolean.TRUE);

        assertEquals("st_user", spec.getName());
        assertEquals("instance", spec.getScope());
        assertEquals(Boolean.TRUE, spec.getRequired());
        assertEquals("WorkflowStepVariableSpecification{name='st_user', scope='instance', required=true}",
                spec.toString());
    }

    @Test
    public void tstWorkflowStepVariableSpecificationNullDefaultsSuccess() {
        final WorkflowStepVariableSpecification spec =
                new WorkflowStepVariableSpecification(null, null, null);

        assertEquals("", spec.getName());
        assertEquals("", spec.getScope());
        assertNull(spec.getRequired());
    }

    @Test
    public void tstWorkflowStepVariableSpecificationParseSuccess() throws Exception {
        final WorkflowStepVariableSpecification spec = JsonUtils.parseResponse(
                "{\"name\":\"st_group\",\"scope\":\"instance\",\"required\":true}",
                WorkflowStepVariableSpecification.class,
                "test");

        assertEquals("st_group", spec.getName());
        assertEquals("instance", spec.getScope());
        assertEquals(Boolean.TRUE, spec.getRequired());
    }

}
