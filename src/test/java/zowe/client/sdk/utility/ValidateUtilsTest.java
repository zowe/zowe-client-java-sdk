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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for ValidateUtils.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class ValidateUtilsTest {

    /**
     * Validate class structure
     */
    @Test
    public void tstValidateUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        UtilsTestHelper.validateClass(ValidateUtils.class, privateConstructorExceptionMsg);
    }

    @Test
    public void tstValidateUtilsCheckIllegalParameterFalseSuccess() {
        boolean isIllegalArgumentException = false;
        try {
            ValidateUtils.checkIllegalParameter(false, "error msg");
        } catch (Exception e) {
            isIllegalArgumentException = true;
        }
        assertFalse(isIllegalArgumentException);
    }

    @Test
    public void tstValidateUtilsCheckIllegalParameterTrueSuccess() {
        try {
            ValidateUtils.checkIllegalParameter(true, "error msg");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void tstValidateUtilsCheckNullParameterFalseSuccess() {
        boolean isNullException = false;
        try {
            ValidateUtils.checkNullParameter(false, "error msg");
        } catch (Exception e) {
            isNullException = true;
        }
        assertFalse(isNullException);
    }

    @Test
    public void tstValidateUtilsCheckNullParameterTrueSuccess() {
        try {
            ValidateUtils.checkNullParameter(true, "error msg");
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    public void tstCheckIllegalParameterStringValidSuccess() {
        assertDoesNotThrow(() ->
                ValidateUtils.checkIllegalParameter("valid", "param"));
    }

    @Test
    public void tstCheckIllegalParameterStringNullFailure() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ValidateUtils.checkIllegalParameter(null, "param")
        );
        assertEquals("param is either null or empty", ex.getMessage());
    }

    @Test
    public void tstCheckIllegalParameterStringBlankFailure() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ValidateUtils.checkIllegalParameter("   ", "param")
        );
        assertEquals("param is either null or empty", ex.getMessage());
    }

    @Test
    public void tstCheckIllegalParameterBooleanTrueWithNullMessageFailure() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ValidateUtils.checkIllegalParameter(true, null)
        );
        assertEquals("empty message specified", ex.getMessage());
    }

    @Test
    public void tstCheckNullParameterObjectNotNullSuccess() {
        assertDoesNotThrow(() ->
                ValidateUtils.checkNullParameter(new Object(), "obj"));
    }

    @Test
    public void tstCheckNullParameterObjectNullFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> ValidateUtils.checkNullParameter(null, "obj")
        );
        assertEquals("obj is null", ex.getMessage());
    }

    @Test
    public void tstCheckNullParameterWithMessageNullFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> ValidateUtils.checkNullParameter(null, "obj", "extra info")
        );
        assertEquals("obj is null, extra info", ex.getMessage());
    }

}
