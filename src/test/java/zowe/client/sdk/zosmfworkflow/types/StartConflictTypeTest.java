/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for StartConflictType.
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class StartConflictTypeTest {

    @Test
    public void testOutputFileValueSuccess() {
        assertEquals("outputFileValue", StartConflictType.OUTPUT_FILE_VALUE.getValue());
    }

    @Test
    public void testExistingValueSuccess() {
        assertEquals("existingValue", StartConflictType.EXISTING_VALUE.getValue());
    }

    @Test
    public void testLeaveConflictSuccess() {
        assertEquals("leaveConflict", StartConflictType.LEAVE_CONFLICT.getValue());
    }

    @Test
    public void testEnumNamesSuccess() {
        assertEquals("OUTPUT_FILE_VALUE", StartConflictType.OUTPUT_FILE_VALUE.name());
        assertEquals("EXISTING_VALUE", StartConflictType.EXISTING_VALUE.name());
        assertEquals("LEAVE_CONFLICT", StartConflictType.LEAVE_CONFLICT.name());
    }

    @Test
    public void testValuesContainsAllTypesSuccess() {
        final StartConflictType[] values = StartConflictType.values();
        assertEquals(3, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(StartConflictType.OUTPUT_FILE_VALUE));
        assertTrue(java.util.Arrays.asList(values).contains(StartConflictType.EXISTING_VALUE));
        assertTrue(java.util.Arrays.asList(values).contains(StartConflictType.LEAVE_CONFLICT));
    }

    @Test
    public void testValueOfEnumSuccess() {
        assertEquals(StartConflictType.OUTPUT_FILE_VALUE, StartConflictType.valueOf("OUTPUT_FILE_VALUE"));
        assertEquals(StartConflictType.EXISTING_VALUE, StartConflictType.valueOf("EXISTING_VALUE"));
        assertEquals(StartConflictType.LEAVE_CONFLICT, StartConflictType.valueOf("LEAVE_CONFLICT"));
    }

}
