/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */

package zowe.client.sdk.zosuss.method;

/**
 * Holds the standard output and standard error produced by a USS command.
 */
public class UssCmdResponse {

    private final String stdout;
    private final String stderr;

    public UssCmdResponse(final String stdout, final String stderr) {
        this.stdout = stdout;
        this.stderr = stderr;
    }

    public String getStdout() {
        return stdout;
    }

    public String getStderr() {
        return stderr;
    }
}
