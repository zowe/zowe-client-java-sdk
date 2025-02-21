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

public class PasswordParams {
    private final String userId;
    private final String oldPwd;
    private final String newPwd;

    public PasswordParams(String userId, String oldPwd, String newPwd) {
        this.userId = userId;
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
    }

    public String getUserId() {
        return userId;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }
}
