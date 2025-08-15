/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.input;

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Optional;

/**
 * The z/OSMF console API parameters. See the z/OSMF REST API documentation for full details.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class IssueConsoleParams {

    /**
     * The z/OS console command to issue.
     */
    private final String cmd;

    /**
     * The solicited keyword to check for in the response. Causes the API to return immediately when the keyword
     * is found, however, it may include solicited command response messages beyond the keyword itself.
     */
    private String solKey;

    /**
     * The system in the sysplex to route the command.
     */
    private String system;

    /**
     * The z/OSMF Console API returns '\r' or '\r\n' where line-breaks. Can attempt to replace these
     * sequences with '\n', but there may be cases where that are not preferable. Specify false to prevent processing.
     */
    private boolean processResponse = false;

    /**
     * IssueConsoleParams constructor, command value is required
     *
     * @param command console command to issue
     * @author Frank Giordano
     */
    public IssueConsoleParams(final String command) {
        ValidateUtils.checkIllegalParameter(command, "command");
        this.cmd = command;
    }

    /**
     * Retrieve cmd value
     *
     * @return cmd value
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * Retrieve solKey value
     *
     * @return solKey value
     */
    public Optional<String> getSolKey() {
        return Optional.ofNullable(solKey);
    }

    /**
     * Assign solKey value
     *
     * @param solKey value
     */
    public void setSolKey(final String solKey) {
        this.solKey = solKey;
    }

    /**
     * Retrieve system value
     *
     * @return system value
     */
    public Optional<String> getSystem() {
        return Optional.ofNullable(system);
    }

    /**
     * Assign system value
     *
     * @param system value
     */
    public void setSystem(final String system) {
        this.system = system;
    }

    /**
     * Retrieve is processResponse specified
     *
     * @return boolean true or false
     */
    public boolean isProcessResponse() {
        return processResponse;
    }

    /**
     * Set process response to true
     */
    public void setProcessResponse() {
        this.processResponse = true;
    }

    /**
     * Return string value representing IssueConsoleParams object
     *
     * @return string representation of IssueConsoleParams
     */
    @Override
    public String toString() {
        return "IssueConsoleParams{" +
                "cmd=" + cmd +
                ", solKey=" + solKey +
                ", system=" + system +
                ", processResponse=" + processResponse +
                '}';
    }

}
