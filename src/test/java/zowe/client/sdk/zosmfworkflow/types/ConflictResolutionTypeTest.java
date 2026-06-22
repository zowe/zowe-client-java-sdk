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
 * Class containing unit tests for ConflictResolutionType.
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
public class ConflictResolutionTypeTest {

    @Test
    public void testOutputFileValueSuccess() {
        assertEquals("outputFileValue", ConflictResolutionType.OUTPUT_FILE_VALUE.getValue());
    }

    @Test
    public void testExistingValueSuccess() {
        assertEquals("existingValue", ConflictResolutionType.EXISTING_VALUE.getValue());
    }

    @Test
    public void testLeaveConflictSuccess() {
        assertEquals("leaveConflict", ConflictResolutionType.LEAVE_CONFLICT.getValue());
    }

    @Test
    public void testEnumNamesSuccess() {
        assertEquals("OUTPUT_FILE_VALUE", ConflictResolutionType.OUTPUT_FILE_VALUE.name());
        assertEquals("EXISTING_VALUE", ConflictResolutionType.EXISTING_VALUE.name());
        assertEquals("LEAVE_CONFLICT", ConflictResolutionType.LEAVE_CONFLICT.name());
    }

    @Test
    public void testValuesContainsAllTypesSuccess() {
        final ConflictResolutionType[] values = ConflictResolutionType.values();
        assertEquals(3, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(ConflictResolutionType.OUTPUT_FILE_VALUE));
        assertTrue(java.util.Arrays.asList(values).contains(ConflictResolutionType.EXISTING_VALUE));
        assertTrue(java.util.Arrays.asList(values).contains(ConflictResolutionType.LEAVE_CONFLICT));
    }

    @Test
    public void testValueOfEnumSuccess() {
        assertEquals(ConflictResolutionType.OUTPUT_FILE_VALUE, ConflictResolutionType.valueOf("OUTPUT_FILE_VALUE"));
        assertEquals(ConflictResolutionType.EXISTING_VALUE, ConflictResolutionType.valueOf("EXISTING_VALUE"));
        assertEquals(ConflictResolutionType.LEAVE_CONFLICT, ConflictResolutionType.valueOf("LEAVE_CONFLICT"));
    }

}
