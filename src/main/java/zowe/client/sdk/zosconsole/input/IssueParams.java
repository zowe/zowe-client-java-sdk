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

import java.util.Optional;

/**
 * Interface for Issue command parameters
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class IssueParams extends ConsoleParams {

    /**
     * The Command to issue.
     */
    private Optional<String> command = Optional.empty();

    /**
     * The system (within the z/OSMF sysplex) to route the command.
     */
    private Optional<String> sysplexSystem = Optional.empty();

    /**
     * The solicited keyword to check for in the response. Causes the API to return immediately when the keyword
     * is found, however, it may include solicited command response messages beyond the keyword itself.
     */
    private Optional<String> solicitedKeyword = Optional.empty();

    /**
     * Indicates the method of issuing the command is synchronous or asynchronous.
     * Default value is "N" - Synchronous request.
     */
    private Optional<String> async = Optional.empty();

    /**
     * Retrieve async value
     *
     * @return async value
     * @author Frank Giordano
     */
    public Optional<String> getAsync() {
        return async;
    }

    /**
     * Assign async value
     *
     * @param async value
     * @author Frank Giordano
     */
    public void setAsync(String async) {
        this.async = Optional.ofNullable(async);
    }

    /**
     * Retrieve command value
     *
     * @return command value
     * @author Frank Giordano
     */
    public Optional<String> getCommand() {
        return command;
    }

    /**
     * Assign command value
     *
     * @param command value
     * @author Frank Giordano
     */
    public void setCommand(String command) {
        this.command = Optional.ofNullable(command);
    }

    /**
     * Retrieve solicitedKeyword value
     *
     * @return solicitedKeyword value
     * @author Frank Giordano
     */
    public Optional<String> getSolicitedKeyword() {
        return solicitedKeyword;
    }

    /**
     * Assign solicitedKeyword value
     *
     * @param solicitedKeyword value
     * @author Frank Giordano
     */
    public void setSolicitedKeyword(String solicitedKeyword) {
        this.solicitedKeyword = Optional.ofNullable(solicitedKeyword);
    }

    /**
     * Retrieve sysplexSystem value
     *
     * @return sysplexSystem value
     * @author Frank Giordano
     */
    public Optional<String> getSysplexSystem() {
        return sysplexSystem;
    }

    /**
     * Assign sysplexSystem value
     *
     * @param sysplexSystem value
     * @author Frank Giordano
     */
    public void setSysplexSystem(String sysplexSystem) {
        this.sysplexSystem = Optional.ofNullable(sysplexSystem);
    }

    @Override
    public String toString() {
        return "IssueParams{" +
                "command=" + command +
                ", sysplexSystem=" + sysplexSystem +
                ", solicitedKeyword=" + solicitedKeyword +
                ", async=" + async +
                '}';
    }

}
