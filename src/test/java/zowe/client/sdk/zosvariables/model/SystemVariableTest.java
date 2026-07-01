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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void tstSystemVariableNullNameFailure() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new SystemVariable(null, "value1", "desc1"));
        assertEquals("name is either null or empty", e.getMessage());
    }

    @Test
    public void tstSystemVariableEmptyNameFailure() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new SystemVariable("", "value1", "desc1"));
        assertEquals("name is either null or empty", e.getMessage());
    }

    @Test
    public void tstSystemVariableNullValueFailure() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new SystemVariable("var1", null, "desc1"));
        assertEquals("value is either null or empty", e.getMessage());
    }

    @Test
    public void tstSystemVariableEmptyValueFailure() {
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new SystemVariable("var1", "", "desc1"));
        assertEquals("value is either null or empty", e.getMessage());
    }

    @Test
    public void tstSystemVariableEqualsSuccess() {
        final SystemVariable systemVariable1 = new SystemVariable("var1", "value1", "desc1");
        final SystemVariable systemVariable2 = new SystemVariable("var1", "value1", "desc1");
        final SystemVariable systemVariable3 = new SystemVariable("var2", "value2", "desc2");

        assertEquals(systemVariable1, systemVariable2);
        assertNotEquals(systemVariable1, systemVariable3);
        assertNotEquals(systemVariable1, null);
        assertNotEquals(systemVariable1, "not a SystemVariable");
        assertEquals(systemVariable1, systemVariable1);
    }

    @Test
    public void tstSystemVariableHashCodeSuccess() {
        final SystemVariable systemVariable1 = new SystemVariable("var1", "value1", "desc1");
        final SystemVariable systemVariable2 = new SystemVariable("var1", "value1", "desc1");

        assertEquals(systemVariable1.hashCode(), systemVariable2.hashCode());
    }

}
