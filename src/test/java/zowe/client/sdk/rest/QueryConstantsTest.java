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

import org.junit.Test;
import zowe.client.sdk.utility.Utils;

/**
 * Class containing unit test for QueryConstantsTest.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class QueryConstantsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstQueryConstantsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Constants class";
        Utils.validateClass(QueryConstants.class, privateConstructorExceptionMsg);
    }

}
