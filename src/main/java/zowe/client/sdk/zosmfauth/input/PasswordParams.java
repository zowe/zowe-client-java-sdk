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

import zowe.client.sdk.utility.ValidateUtils;

/**
 * Parameter container for the z/OSMF authentication service changes user password or passphrase
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.1.0?topic=services-change-user-password-passphrase">z/OSMF REST API </a>
 *
 * @author Esteban Sandoval
 * @version 4.0
 */
public class PasswordParams {

    /**
     * The user ID value for the password or passphrase change
     */
    private final String userId;

    /**
     * The old password that will be updated
     */
    private final String oldPwd;

    /**
     * The new password that will replace the old password
     */
    private final String newPwd;

    /**
     * PasswordParams constructor
     *
     * @param userId user ID value
     * @param oldPwd old password value
     * @param newPwd new password value
     * @author Esteban Sandoval
     */
    public PasswordParams(final String userId, final String oldPwd, final String newPwd) {
        ValidateUtils.checkNullParameter(userId == null, "userId is null");
        ValidateUtils.checkIllegalParameter(userId.isBlank(), "userId is empty");
        ValidateUtils.checkNullParameter(oldPwd == null, "oldPwd is null");
        ValidateUtils.checkIllegalParameter(oldPwd.isBlank(), "oldPwd is empty");
        ValidateUtils.checkNullParameter(newPwd == null, "newPwd is null");
        ValidateUtils.checkIllegalParameter(newPwd.isBlank(), "newPwd is empty");
        this.userId = userId;
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
    }

    /**
     * Retrieve userID specified
     *
     * @return userID value
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Retrieve oldPwd specified
     *
     * @return oldPwd value
     */
    public String getOldPwd() {
        return oldPwd;
    }

    /**
     * Retrieve newPwd specified
     *
     * @return newPwd value
     */
    public String getNewPwd() {
        return newPwd;
    }

    /**
     * Return string value representing PasswordParams object
     *
     * @return string representation of PasswordParams
     */
    @Override
    public String toString() {
        return "PasswordParams{" +
                "userId='" + userId + '\'' +
                ", oldPwd='" + oldPwd + '\'' +
                ", newPwd='" + newPwd + '\'' +
                '}';
    }

}
