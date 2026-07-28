/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfauth.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Class containing unit tests for PasswordInputData.
 *
 * @author Frank Giordano
 * @version 7.0
 */
class PasswordInputDataTest {

    @Test
    void tstCreatePasswordInputDataSuccess() {
        PasswordInputData input = new PasswordInputData(
                "USER1",
                "OLDPWD",
                "NEWPWD");

        assertEquals("USER1", input.getUserId());
        assertEquals("OLDPWD", input.getOldPwd());
        assertEquals("NEWPWD", input.getNewPwd());
    }

    @Test
    void tstReturnToStringWithMaskedPasswordsSuccess() {
        PasswordInputData input = new PasswordInputData(
                "USER1",
                "OLDPWD",
                "NEWPWD");

        assertEquals(
                "PasswordInputData{userId='USER1', oldPwd='*****', newPwd='*****'}",
                input.toString());
    }

    @Test
    void tstThrowExceptionWhenUserIdIsNullFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordInputData(null, "OLDPWD", "NEWPWD"));
    }

    @Test
    void tstThrowExceptionWhenOldPasswordIsNullFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordInputData("USER1", null, "NEWPWD"));
    }

    @Test
    void tstThrowExceptionWhenNewPasswordIsNullFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordInputData("USER1", "OLDPWD", null));
    }

    @Test
    void tstThrowExceptionWhenUserIdIsEmptyFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordInputData("", "OLDPWD", "NEWPWD"));
    }

    @Test
    void tstThrowExceptionWhenOldPasswordIsEmptyFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordInputData("USER1", "", "NEWPWD"));
    }

    @Test
    void tstThrowExceptionWhenNewPasswordIsEmptyFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> new PasswordInputData("USER1", "OLDPWD", ""));
    }

}
