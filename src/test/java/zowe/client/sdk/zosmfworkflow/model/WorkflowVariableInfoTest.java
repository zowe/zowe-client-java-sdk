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
 * Class containing unit tests for WorkflowVariableInfo.
 */
public class WorkflowVariableInfoTest {

    @Test
    public void tstWorkflowVariableInfoConstructorSuccess() {
        final WorkflowVariableInfo variable =
                new WorkflowVariableInfo("st_user", "instance", "string", "IBMUSER", "private");

        assertEquals("st_user", variable.getName());
        assertEquals("instance", variable.getScope());
        assertEquals("string", variable.getType());
        assertEquals("IBMUSER", variable.getValue());
        assertEquals("private", variable.getVisibility());
        assertEquals("WorkflowVariableInfo{name='st_user', scope='instance', type='string', " +
                "value='IBMUSER', visibility='private'}", variable.toString());
    }

    @Test
    public void tstWorkflowVariableInfoNullDefaultsSuccess() {
        final WorkflowVariableInfo variable = new WorkflowVariableInfo(null, null, null, null, null);

        assertEquals("", variable.getName());
        assertEquals("", variable.getScope());
        assertEquals("", variable.getType());
        assertEquals("", variable.getValue());
        assertEquals("", variable.getVisibility());
    }

    @Test
    public void tstWorkflowVariableInfoParseSuccess() throws Exception {
        final WorkflowVariableInfo variable = JsonUtils.parseResponse(
                "{\"visibility\":\"private\",\"scope\":\"instance\",\"name\":\"st_group\",\"type\":\"string\",\"value\":null}",
                WorkflowVariableInfo.class, "test");

        assertEquals("st_group", variable.getName());
        assertEquals("instance", variable.getScope());
        assertEquals("string", variable.getType());
        assertEquals("private", variable.getVisibility());
        assertEquals("", variable.getValue());
    }

}
