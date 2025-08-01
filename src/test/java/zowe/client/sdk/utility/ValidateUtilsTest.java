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

import static org.junit.Assert.*;

/**
 * Class containing unit tests for ValidateUtils.
 *
 * @author Frank Giordano
 * @version 4.0
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
        boolean isIllegalArgumentException = false;
        try {
            ValidateUtils.checkIllegalParameter(false, "error msg");
        } catch (Exception e) {
            isIllegalArgumentException = true;
        }
        assertFalse(isIllegalArgumentException);
    }

    @Test
    public void tstCheckIllegalParameterTrueSuccess() {
        try {
            ValidateUtils.checkIllegalParameter(true, "error msg");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void tstCheckNullParameterFalseSuccess() {
        boolean isNullException = false;
        try {
            ValidateUtils.checkNullParameter(false, "error msg");
        } catch (Exception e) {
            isNullException = true;
        }
        assertFalse(isNullException);
    }

    @Test
    public void tstCheckNullParameterTrueSuccess() {
        try {
            ValidateUtils.checkNullParameter(true, "error msg");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

}
