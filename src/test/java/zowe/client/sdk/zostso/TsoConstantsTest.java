/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso;

import org.junit.Test;
import zowe.client.sdk.altzostso.TsoConstants;
import zowe.client.sdk.utility.Utils;

/**
 * Class containing unit test for TsoConstantsTest.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class TsoConstantsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstTsoConstantsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Constants class";
        Utils.validateClass(TsoConstants.class, privateConstructorExceptionMsg);
    }

}
