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
 * Class containing unit tests for ViewType.
 */
public class ViewTypeTest {

    @Test
    public void tstUserValueSuccess() {
        assertEquals("user", ViewType.USER.getValue());
    }

    @Test
    public void tstDomainValueSuccess() {
        assertEquals("domain", ViewType.DOMAIN.getValue());
    }

    @Test
    public void tstEnumNamesSuccess() {
        assertEquals("USER", ViewType.USER.name());
        assertEquals("DOMAIN", ViewType.DOMAIN.name());
    }

    @Test
    public void tstValuesContainsBothViewTypesSuccess() {
        final ViewType[] values = ViewType.values();
        assertEquals(2, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(ViewType.USER));
        assertTrue(java.util.Arrays.asList(values).contains(ViewType.DOMAIN));
    }

    @Test
    public void tstValueOfEnumSuccess() {
        assertEquals(ViewType.USER, ViewType.valueOf("USER"));
        assertEquals(ViewType.DOMAIN, ViewType.valueOf("DOMAIN"));
    }

}
