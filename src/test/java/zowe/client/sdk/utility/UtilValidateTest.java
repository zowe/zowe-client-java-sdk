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

import static org.junit.Assert.assertEquals;

public class UtilValidateTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstValidateUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        Utils.validateClass(ValidateUtils.class, privateConstructorExceptionMsg);
    }

    /**
     * Test validate utility method checkIllegalParameter validate false value correctly.
     */
    @Test
    public void tstCheckIllegalParameterFalseSuccess() {
        final int input = 5;
        boolean isIllegalArgumentException = false;
        try {
            ValidateUtils.checkIllegalParameter(input == 15, "error msg");
        } catch (Exception e) {
            isIllegalArgumentException = true;
        }
        assertEquals(false, isIllegalArgumentException);
    }

    /**
     * Test validate utility method checkIllegalParameter validate true value correctly.
     */
    @Test
    public void tstCheckIllegalParameterTrueSuccess() {
        final int input = 5;
        boolean isIllegalArgumentException = false;
        try {
            ValidateUtils.checkIllegalParameter(input == 5, "error msg");
        } catch (Exception e) {
            isIllegalArgumentException = true;
        }
        assertEquals(true, isIllegalArgumentException);
    }

    /**
     * Test validate utility method checkNullParameter validate false value correctly.
     */
    @Test
    public void tstCheckNullParameterFalseSuccess() {
        String str = "";
        boolean isNullException = false;
        try {
            ValidateUtils.checkNullParameter(str == null, "error msg");
        } catch (Exception e) {
            isNullException = true;
        }
        assertEquals(false, isNullException);
    }

    /**
     * Test validate utility method checkNullParameter validate true value correctly.
     */
    @Test
    public void tstCheckNullParameterTrueSuccess() {
        String str = null;
        boolean isNullException = false;
        try {
            ValidateUtils.checkNullParameter(str == null, "error msg");
        } catch (Exception e) {
            isNullException = true;
        }
        assertEquals(true, isNullException);
    }

}
