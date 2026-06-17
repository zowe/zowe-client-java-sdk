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
 * Class containing unit tests for OrderByType.
 */
public class OrderByTypeTest {

    @Test
    public void tstDescValueSuccess() {
        assertEquals("desc", OrderByType.DESC.getValue());
    }

    @Test
    public void tstAscValueSuccess() {
        assertEquals("asc", OrderByType.ASC.getValue());
    }

    @Test
    public void tstEnumNamesSuccess() {
        assertEquals("DESC", OrderByType.DESC.name());
        assertEquals("ASC", OrderByType.ASC.name());
    }

    @Test
    public void tstValuesContainsBothOrderTypesSuccess() {
        final OrderByType[] values = OrderByType.values();
        assertEquals(2, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(OrderByType.DESC));
        assertTrue(java.util.Arrays.asList(values).contains(OrderByType.ASC));
    }

    @Test
    public void tstValueOfEnumSuccess() {
        assertEquals(OrderByType.DESC, OrderByType.valueOf("DESC"));
        assertEquals(OrderByType.ASC, OrderByType.valueOf("ASC"));
    }

}
