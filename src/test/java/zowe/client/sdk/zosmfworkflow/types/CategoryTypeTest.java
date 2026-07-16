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
 * Class containing unit tests for CategoryType.
 */
public class CategoryTypeTest {

    @Test
    public void tstGeneralValueSuccess() {
        assertEquals("general", CategoryType.GENERAL.getValue());
    }

    @Test
    public void tstConfigurationValueSuccess() {
        assertEquals("configuration", CategoryType.CONFIGURATION.getValue());
    }

    @Test
    public void tstEnumNamesSuccess() {
        assertEquals("GENERAL", CategoryType.GENERAL.name());
        assertEquals("CONFIGURATION", CategoryType.CONFIGURATION.name());
    }

    @Test
    public void tstValuesContainsBothCategoryTypesSuccess() {
        final CategoryType[] values = CategoryType.values();
        assertEquals(2, values.length);
        assertTrue(Arrays.asList(values).contains(CategoryType.GENERAL));
        assertTrue(Arrays.asList(values).contains(CategoryType.CONFIGURATION));
    }

    @Test
    public void tstValueOfEnumSuccess() {
        assertEquals(CategoryType.GENERAL, CategoryType.valueOf("GENERAL"));
        assertEquals(CategoryType.CONFIGURATION, CategoryType.valueOf("CONFIGURATION"));
    }

}
