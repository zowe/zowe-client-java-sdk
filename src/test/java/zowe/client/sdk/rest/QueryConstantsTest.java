/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.utility.UtilsTestHelper;

/**
 * Class containing unit test for QueryConstantsTest.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class QueryConstantsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstQueryConstantsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Constants class";
        UtilsTestHelper.validateClass(QueryConstants.class, privateConstructorExceptionMsg);
    }

}
