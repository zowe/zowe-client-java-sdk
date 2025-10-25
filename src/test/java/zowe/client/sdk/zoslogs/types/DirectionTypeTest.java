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
 * Unit tests for DirectionType class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
class DirectionTypeTest {

    @Test
    void tstForwardValueSuccess() {
        assertEquals("forward", DirectionType.FORWARD.getValue());
    }

    @Test
    void tstBackwardValueSuccess() {
        assertEquals("backward", DirectionType.BACKWARD.getValue());
    }

    @Test
    void tstEnumNamesSuccess() {
        assertEquals("FORWARD", DirectionType.FORWARD.name());
        assertEquals("BACKWARD", DirectionType.BACKWARD.name());
    }

    @Test
    void tstValuesContainsBothDirectionsSuccess() {
        DirectionType[] values = DirectionType.values();
        assertEquals(2, values.length);
        assertTrue(java.util.Arrays.asList(values).contains(DirectionType.FORWARD));
        assertTrue(java.util.Arrays.asList(values).contains(DirectionType.BACKWARD));
    }

    @Test
    void tstValueOfEnumSuccess() {
        assertEquals(DirectionType.FORWARD, DirectionType.valueOf("FORWARD"));
        assertEquals(DirectionType.BACKWARD, DirectionType.valueOf("BACKWARD"));
    }

}

