/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.model;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.JsonUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for SystemVariable.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class SystemVariableTest {

    @Test
    public void tstSystemVariableSuccess() {
        final SystemVariable systemVariable = new SystemVariable("var1", "value1", "desc1");

        assertNotNull(systemVariable);
        assertEquals("var1", systemVariable.getName());
        assertEquals("value1", systemVariable.getValue());
        assertEquals("desc1", systemVariable.getDescription());
        assertEquals("SystemVariable{name='var1', value='value1', description='desc1'}", systemVariable.toString());
    }

    @Test
    public void tstSystemVariableWithoutDescriptionSuccess() {
        final SystemVariable systemVariable = new SystemVariable("var1", "value1");

        assertNotNull(systemVariable);
        assertEquals("var1", systemVariable.getName());
        assertEquals("value1", systemVariable.getValue());
        assertNull(systemVariable.getDescription());
    }

    @Test
    public void tstSystemVariableNullDescriptionSuccess() {
        final SystemVariable systemVariable = new SystemVariable("var1", "value1", null);

        assertNotNull(systemVariable);
        assertEquals("var1", systemVariable.getName());
        assertEquals("value1", systemVariable.getValue());
        assertNull(systemVariable.getDescription());
    }

    @Test
    public void tstSystemVariableParseSuccess() throws Exception {
        final SystemVariable systemVariable = JsonUtils.parseResponse(
                "{\"name\":\"var1\",\"value\":\"value1\",\"description\":\"desc1\"}",
                SystemVariable.class,
                "test");

        assertEquals("var1", systemVariable.getName());
        assertEquals("value1", systemVariable.getValue());
        assertEquals("desc1", systemVariable.getDescription());
    }

    @Test
    void tstSystemVariableNullNameFailure() {
        assertThrows(IllegalArgumentException.class, () ->
                new SystemVariable(
                        null,
                        "123",
                        "desc"
                )
        );
    }

    @Test
    void tstSystemVariableNullValueSuccess() {
        assertThrows(IllegalArgumentException.class, () ->
                new SystemVariable(
                        "MY_VAR",
                        null,
                        "desc"
                )
        );
    }

}
