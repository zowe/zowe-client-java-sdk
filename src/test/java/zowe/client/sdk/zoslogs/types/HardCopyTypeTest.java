/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zoslogs.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for HardCopyType class.
 *
 * @author Frank Giordano
 * @version 6.0
 */
class HardCopyTypeTest {

    @Test
    void tstOperlogValueSuccess() {
        assertEquals("operlog", HardCopyType.OPERLOG.getValue());
    }

    @Test
    void tstSyslogValueSuccess() {
        assertEquals("syslog", HardCopyType.SYSLOG.getValue());
    }

    @Test
    void tstEnumNamesSuccess() {
        assertEquals("OPERLOG", HardCopyType.OPERLOG.name());
        assertEquals("SYSLOG", HardCopyType.SYSLOG.name());
    }

    @Test
    void tstValuesContainsBothTypesSuccess() {
        HardCopyType[] values = HardCopyType.values();
        assertEquals(2, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(HardCopyType.OPERLOG));
        assertTrue(java.util.Arrays.asList(values).contains(HardCopyType.SYSLOG));
    }

    @Test
    void tstValueOfEnumSuccess() {
        assertEquals(HardCopyType.OPERLOG, HardCopyType.valueOf("OPERLOG"));
        assertEquals(HardCopyType.SYSLOG, HardCopyType.valueOf("SYSLOG"));
    }

}

