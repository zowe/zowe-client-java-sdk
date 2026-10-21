/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosuss.response;

/**
 * UssCmdResponse class represents the response returned from executing a USS command via SSH.
 * <p>
 * The response contains the standard output, standard error, and exit code returned by the
 * remote command. Standard output and standard error are captured separately and therefore
 * their relative execution order is not preserved.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public final class UssCmdResponse {

    private final String stdout;

    private final String stderr;

    private final int exitCode;

    /**
     * UssCmdResponse constructor.
     *
     * @param stdout string value containing standard output from the USS command
     * @param stderr string value containing standard error from the USS command
     * @param exitCode int value containing the remote command exit code
     */
    public UssCmdResponse(final String stdout, final String stderr, final int exitCode) {
        this.stdout = stdout;
        this.stderr = stderr;
        this.exitCode = exitCode;
    }

    /**
     * Retrieve standard output from the USS command.
     *
     * @return string value containing standard output
     */
    public String getStdout() {
        return stdout;
    }

    /**
     * Retrieve standard error from the USS command.
     *
     * @return string value containing standard error
     */
    public String getStderr() {
        return stderr;
    }

    /**
     * Retrieve the exit code returned by the USS command.
     *
     * @return int value containing the remote command exit code
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Indicates whether the USS command completed successfully.
     *
     * @return true if the command exit code is zero; false otherwise
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    /**
     * Returns a string representation of this USS command response.
     *
     * @return string representation containing standard output, standard error,
     *         and the remote command exit code
     */
    @Override
    public String toString() {
        return "UssCmdResponse{" +
                "stdout='" + stdout + '\'' +
                ", stderr='" + stderr + '\'' +
                ", exitCode=" + exitCode +
                '}';
    }

}
