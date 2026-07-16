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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for StatusNameType.
 */
public class StatusNameTypeTest {

    @Test
    public void tstInProgressValueSuccess() {
        assertEquals("in-progress", StatusNameType.IN_PROGRESS.getValue());
    }

    @Test
    public void tstCompleteValueSuccess() {
        assertEquals("complete", StatusNameType.COMPLETE.getValue());
    }

    @Test
    public void tstAutomationInProgressValueSuccess() {
        assertEquals("automation-in-progress", StatusNameType.AUTOMATION_IN_PROGRESS.getValue());
    }

    @Test
    public void tstCanceledValueSuccess() {
        assertEquals("canceled", StatusNameType.CANCELED.getValue());
    }

    @Test
    public void tstEnumNamesSuccess() {
        assertEquals("IN_PROGRESS", StatusNameType.IN_PROGRESS.name());
        assertEquals("COMPLETE", StatusNameType.COMPLETE.name());
        assertEquals("AUTOMATION_IN_PROGRESS", StatusNameType.AUTOMATION_IN_PROGRESS.name());
        assertEquals("CANCELED", StatusNameType.CANCELED.name());
    }

    @Test
    public void tstValuesContainsAllStatusNameTypesSuccess() {
        final StatusNameType[] values = StatusNameType.values();
        assertEquals(4, values.length);
        assertTrue(Arrays.asList(values).contains(StatusNameType.IN_PROGRESS));
        assertTrue(Arrays.asList(values).contains(StatusNameType.COMPLETE));
        assertTrue(Arrays.asList(values).contains(StatusNameType.AUTOMATION_IN_PROGRESS));
        assertTrue(Arrays.asList(values).contains(StatusNameType.CANCELED));
    }

    @Test
    public void tstValueOfEnumSuccess() {
        assertEquals(StatusNameType.IN_PROGRESS, StatusNameType.valueOf("IN_PROGRESS"));
        assertEquals(StatusNameType.CANCELED, StatusNameType.valueOf("CANCELED"));
    }

}
