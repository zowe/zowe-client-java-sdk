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

import kong.unirest.core.Cookie;
import org.junit.Test;
import zowe.client.sdk.core.AuthType;
import zowe.client.sdk.core.ZosConnection;

import static org.junit.Assert.*;

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
    public void tstValidateUtilsZosConnectionNulFailure() {
        boolean isValid = true;
        var msg = "connection is null";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(null);
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionBasicSuccess() {
        boolean isValid = true;
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.BASIC).host("test")
                    .password("password").user("user").zosmfPort("zosmfPort").build());
        } catch (Exception e) {
            isValid = false;
        }
        assertTrue(isValid);
    }

    @Test
    public void tstValidateUtilsCheckConnectionTokenSuccess() {
        boolean isValid = true;
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.TOKEN).host("test")
                    .cookie(new Cookie("xxx", "xxx")).zosmfPort("zosmfPort").build());
        } catch (Exception e) {
            isValid = false;
        }
        assertTrue(isValid);
    }

    @Test
    public void tstValidateUtilsCheckConnectionSslSuccess() {
        boolean isValid = true;
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.SSL)
                    .host("test").certFilePath("xxx").zosmfPort("zosmfPort").build());
        } catch (Exception e) {
            isValid = false;
        }
        assertTrue(isValid);
    }

    @Test
    public void tstValidateUtilsCheckConnectionBasicPasswordEmptyErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for BASIC authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.BASIC).host("test")
                    .password("").user("user").zosmfPort("zosmfPort").build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionBasicPasswordNullErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for BASIC authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.BASIC).host("test")
                    .password(null).user("user").zosmfPort("zosmfPort").build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionBasicUserEmptyErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for BASIC authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.BASIC).host("test")
                    .password("password").user("").zosmfPort("zosmfPort").build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionBasicUserNullErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for BASIC authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.BASIC).host("test")
                    .password("password").user(null).zosmfPort("zosmfPort").build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionBasicZosmfPortEmptyErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for BASIC authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.BASIC).host("test")
                    .password("password").user("user").zosmfPort("").build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionBasicZosmfPortNullErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for BASIC authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.BASIC).host("test")
                    .password("password").user("user").zosmfPort(null).build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionTokenNullErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for TOKEN authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.TOKEN).host("test")
                    .cookie(null).zosmfPort(null).build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionSslEmptyErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for SSL authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.SSL).host("test")
                    .certFilePath("").zosmfPort(null).build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstValidateUtilsCheckConnectionSslNullErrMsgFailure() {
        boolean isValid = true;
        var msg = "required connection attribute(s) missing for SSL authentication";
        String errMsg = "";
        try {
            ValidateUtils.checkConnection(new ZosConnection.Builder(AuthType.SSL).host("test")
                    .certFilePath(null).zosmfPort(null).build());
        } catch (Exception e) {
            isValid = false;
            errMsg = e.getMessage();
        }
        assertFalse(isValid);
        assertEquals(msg, errMsg);
    }

    @Test
    public void tstCheckIllegalParameterFalseSuccess() {
        final int input = 5;
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
        final int input = 5;
        boolean isIllegalArgumentException = false;
        try {
            ValidateUtils.checkIllegalParameter(true, "error msg");
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
            ValidateUtils.checkNullParameter(false, "error msg");
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
            ValidateUtils.checkNullParameter(true, "error msg");
        } catch (Exception e) {
            isNullException = true;
        }
        assertTrue(isNullException);
    }

}
