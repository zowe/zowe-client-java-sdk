/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs;

import org.junit.Test;
import zowe.client.sdk.utility.Utils;

/**
 * Class containing unit test for JobsConstantsTest.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobsConstantsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstJobsConstantsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Constants class";
        Utils.validateClass(JobsConstants.class, privateConstructorExceptionMsg);
    }

}
