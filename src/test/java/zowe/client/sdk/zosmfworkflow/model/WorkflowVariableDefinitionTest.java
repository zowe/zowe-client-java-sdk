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

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Class containing unit tests for WorkflowVariableDefinition.
 */
public class WorkflowVariableDefinitionTest {

    @Test
    public void tstWorkflowVariableDefinitionConstructorSuccess() {
        final WorkflowVariableDefinition variable = new WorkflowVariableDefinition(
                "st_user", "instance", "User ID for the started task.", "Started",
                Arrays.asList("A", "B"), 0, "MYSTUSER", "The user ID under whose authority the task runs.",
                Boolean.FALSE, 8, "", 1, "", Boolean.FALSE, "^[0-9A-Z$#@]{1,8}$",
                Boolean.FALSE, "string", "USERID", Boolean.FALSE, "private");

        assertEquals("st_user", variable.getName());
        assertEquals("instance", variable.getScope());
        assertEquals("User ID for the started task.", variable.getAbstractInfo());
        assertEquals("Started", variable.getCategory());
        assertEquals(2, variable.getChoice().size());
        assertEquals(0, variable.getDecimalPlaces().intValue());
        assertEquals("MYSTUSER", variable.getDefaultValue());
        assertEquals("The user ID under whose authority the task runs.", variable.getDescription());
        assertEquals(Boolean.FALSE, variable.getExposeToUser());
        assertEquals(8, variable.getMaxLength().intValue());
        assertEquals(1, variable.getMinLength().intValue());
        assertEquals(Boolean.FALSE, variable.getPromptAtCreate());
        assertEquals("^[0-9A-Z$#@]{1,8}$", variable.getRegularExpression());
        assertEquals(Boolean.FALSE, variable.getRequiredAtCreate());
        assertEquals("string", variable.getType());
        assertEquals("USERID", variable.getValidationType());
        assertEquals(Boolean.FALSE, variable.getValueMustBeChoice());
        assertEquals("private", variable.getVisibility());
    }

    @Test
    public void tstWorkflowVariableDefinitionNullDefaultsSuccess() {
        final WorkflowVariableDefinition variable = new WorkflowVariableDefinition(
                null, null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null, null, null);

        assertEquals("", variable.getName());
        assertEquals("", variable.getScope());
        assertEquals("", variable.getAbstractInfo());
        assertEquals("", variable.getDefaultValue());
        assertEquals(Collections.emptyList(), variable.getChoice());
        assertNull(variable.getDecimalPlaces());
        assertNull(variable.getExposeToUser());
    }

    @Test
    public void tstWorkflowVariableDefinitionParseSuccess() throws Exception {
        final WorkflowVariableDefinition variable = JsonUtils.parseResponse(
                "{\"name\":\"st_user\",\"scope\":\"instance\",\"abstract\":\"User ID for the started task.\"," +
                        "\"type\":\"string\",\"default\":\"MYSTUSER\",\"validationType\":\"USERID\"," +
                        "\"visibility\":\"private\",\"exposeToUser\":false,\"requiredAtCreate\":false}",
                WorkflowVariableDefinition.class,
                "test");

        assertEquals("st_user", variable.getName());
        assertEquals("instance", variable.getScope());
        assertEquals("User ID for the started task.", variable.getAbstractInfo());
        assertEquals("string", variable.getType());
        assertEquals("MYSTUSER", variable.getDefaultValue());
        assertEquals("USERID", variable.getValidationType());
        assertEquals("private", variable.getVisibility());
        assertEquals(Boolean.FALSE, variable.getExposeToUser());
        assertEquals(Boolean.FALSE, variable.getRequiredAtCreate());
    }

}
