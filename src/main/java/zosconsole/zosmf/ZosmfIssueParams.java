/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosconsole.zosmf;

import java.util.Optional;

/**
 * The z/OSMF console API parameters. See the z/OSMF REST API documentation for full details.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZosmfIssueParams {

    /**
     * The z/OS console command to issue.
     */
    private Optional<String> cmd = Optional.empty();

    /**
     * The solicited keyword to look for.

     */
    private Optional<String> solKey = Optional.empty();

    /**
     * The system in the sysplex to route the command.
     */
    private Optional<String> system = Optional.empty();

    /**
     * The method of issuing the command.
     */
    private Optional<String> async = Optional.empty();

    /**
     * Retrieve cmd value
     *
     * @return cmd value
     * @author Frank Giordano
     */
    public Optional<String> getCmd() {
        return cmd;
    }

    /**
     * Assign cmd value
     *
     * @param cmd value
     * @author Frank Giordano
     */
    public void setCmd(String cmd) {
        this.cmd = Optional.ofNullable(cmd);
    }

    /**
     * Retrieve solKey value
     *
     * @return solKey value
     * @author Frank Giordano
     */
    public Optional<String> getSolKey() {
        return solKey;
    }

    /**
     * Assign solKey value
     *
     * @param solKey value
     * @author Frank Giordano
     */
    public void setSolKey(String solKey) {
        this.solKey = Optional.ofNullable(solKey);
    }

    /**
     * Retrieve system value
     *
     * @return system value
     * @author Frank Giordano
     */
    public Optional<String> getSystem() {
        return system;
    }

    /**
     * Assign system value
     *
     * @param system value
     * @author Frank Giordano
     */
    public void setSystem(String system) {
        this.system = Optional.ofNullable(system);
    }

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

    @Override
    public String toString() {
        return "ZosmfIssueParams{" +
                "cmd=" + cmd +
                ", solKey=" + solKey +
                ", system=" + system +
                ", async=" + async +
                '}';
    }

}
