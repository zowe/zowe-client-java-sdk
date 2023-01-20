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

import org.junit.Test;

/**
 * Class containing unit tests for ConsoleUtils.
 *
 * @author Frank Giordano
 */
public class ConsoleUtilsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstConsoleUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        Utils.validateClass(ConsoleUtils.class, privateConstructorExceptionMsg);
    }

}
