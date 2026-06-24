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
 * Class containing unit tests for ConflictStartType.
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class ConflictStartTypeTest {

    @Test
    public void testOutputFileValueSuccess() {
        assertEquals("outputFileValue", ConflictStartType.OUTPUT_FILE_VALUE.getValue());
    }

    @Test
    public void testExistingValueSuccess() {
        assertEquals("existingValue", ConflictStartType.EXISTING_VALUE.getValue());
    }

    @Test
    public void testLeaveConflictSuccess() {
        assertEquals("leaveConflict", ConflictStartType.LEAVE_CONFLICT.getValue());
    }

    @Test
    public void testEnumNamesSuccess() {
        assertEquals("OUTPUT_FILE_VALUE", ConflictStartType.OUTPUT_FILE_VALUE.name());
        assertEquals("EXISTING_VALUE", ConflictStartType.EXISTING_VALUE.name());
        assertEquals("LEAVE_CONFLICT", ConflictStartType.LEAVE_CONFLICT.name());
    }

    @Test
    public void testValuesContainsAllTypesSuccess() {
        final ConflictStartType[] values = ConflictStartType.values();
        assertEquals(3, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(ConflictStartType.OUTPUT_FILE_VALUE));
        assertTrue(java.util.Arrays.asList(values).contains(ConflictStartType.EXISTING_VALUE));
        assertTrue(java.util.Arrays.asList(values).contains(ConflictStartType.LEAVE_CONFLICT));
    }

    @Test
    public void testValueOfEnumSuccess() {
        assertEquals(ConflictStartType.OUTPUT_FILE_VALUE, ConflictStartType.valueOf("OUTPUT_FILE_VALUE"));
        assertEquals(ConflictStartType.EXISTING_VALUE, ConflictStartType.valueOf("EXISTING_VALUE"));
        assertEquals(ConflictStartType.LEAVE_CONFLICT, ConflictStartType.valueOf("LEAVE_CONFLICT"));
    }

}
