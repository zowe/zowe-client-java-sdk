/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zoslogs.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class containing unit test for ZosLogInputData class.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class ZosLogInputDataTest {

    @Test
    public void tstTimeRangeValidValuesSuccess() {
        assertEquals("1s", new ZosLogInputData.Builder()
                .timeRange("1s")
                .build()
                .getTimeRange()
                .orElse(null));

        assertEquals("10m", new ZosLogInputData.Builder()
                .timeRange("10m")
                .build()
                .getTimeRange()
                .orElse(null));

        assertEquals("999h", new ZosLogInputData.Builder()
                .timeRange("999h")
                .build()
                .getTimeRange()
                .orElse(null));
    }

    @Test
    public void tstTimeRangeInvalidValuesFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange(null));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange(""));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("0s"));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("00m"));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("1000s"));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("10"));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("10d"));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("10M"));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("-10m"));

        assertThrows(IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("10 m"));
    }

    @Test
    public void tstTimeRangeInvalidValueMessageFailure() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ZosLogInputData.Builder().timeRange("1000m"));

        assertEquals(
                "timeRange must be a value from 1 to 999 followed by s, m or h "
                        + "(for example 999s, 123m, 10h), but was: 1000m",
                exception.getMessage());
    }

}
