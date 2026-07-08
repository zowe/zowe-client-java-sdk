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

/**
 * Class containing unit tests for WorkflowVariableReference.
 */
public class WorkflowVariableReferenceTest {

    @Test
    public void tstWorkflowVariableReferenceConstructorSuccess() {
        final WorkflowVariableReference reference = new WorkflowVariableReference("st_user", "instance");

        assertEquals("st_user", reference.getName());
        assertEquals("instance", reference.getScope());
        assertEquals("WorkflowVariableReference{name='st_user', scope='instance'}", reference.toString());
    }

    @Test
    public void tstWorkflowVariableReferenceNullDefaultsSuccess() {
        final WorkflowVariableReference reference = new WorkflowVariableReference(null, null);

        assertEquals("", reference.getName());
        assertEquals("", reference.getScope());
    }

    @Test
    public void tstWorkflowVariableReferenceParseSuccess() throws Exception {
        final WorkflowVariableReference reference = JsonUtils.parseResponse(
                "{\"scope\":\"global\",\"name\":\"procNameVariable\"}",
                WorkflowVariableReference.class, "test");

        assertEquals("procNameVariable", reference.getName());
        assertEquals("global", reference.getScope());
    }

}
