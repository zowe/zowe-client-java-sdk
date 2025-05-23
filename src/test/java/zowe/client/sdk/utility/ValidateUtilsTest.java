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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Class containing unit tests for ValidateUtils.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class ValidateUtilsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstValidateUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        Utils.validateClass(ValidateUtils.class, privateConstructorExceptionMsg);
    }

    @Test
    public void tstCheckIllegalParameterFalseSuccess() {
        final int input = 5;
        boolean isIllegalArgumentException = false;
        try {
            ValidateUtils.checkIllegalParameter(input == 15, "error msg");
        } catch (Exception e) {
            isIllegalArgumentException = true;
        }
        assertFalse(isIllegalArgumentException);
    }

    @Test
    public void tstCheckIllegalParameterTrueSuccess() {
        final int input = 5;
        boolean isIllegalArgumentException = false;
        try {
            ValidateUtils.checkIllegalParameter(input == 5, "error msg");
        } catch (Exception e) {
            isIllegalArgumentException = true;
        }
        assertTrue(isIllegalArgumentException);
    }

    @Test
    public void tstCheckNullParameterFalseSuccess() {
        final String str = "";
        boolean isNullException = false;
        try {
            ValidateUtils.checkNullParameter(str == null, "error msg");
        } catch (Exception e) {
            isNullException = true;
        }
        assertFalse(isNullException);
    }

    @Test
    public void tstCheckNullParameterTrueSuccess() {
        final String str = null;
        boolean isNullException = false;
        try {
            ValidateUtils.checkNullParameter(str == null, "error msg");
        } catch (Exception e) {
            isNullException = true;
        }
        assertTrue(isNullException);
    }

}
