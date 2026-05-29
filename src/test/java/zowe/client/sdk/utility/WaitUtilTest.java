/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for WaitUtil.
 *
 * @author Chaitanya Katore
 * @version 6.0
 */
public class WaitUtilTest {

    @Test
    public void tstWaitUtilClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        UtilsTestHelper.validateClass(WaitUtil.class, privateConstructorExceptionMsg);
    }

    @Test
    public void tstWaitBlocksForTime() {
        final long start = System.currentTimeMillis();
        WaitUtil.wait(100);
        final long elapsed = System.currentTimeMillis() - start;
        assertTrue(elapsed >= 90, "Should wait for at least 100 milliseconds");
    }

}
