/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosconsole.examples;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosconsole.ConsoleResponse;
import zosconsole.input.IssueParams;
import zosconsole.zosmf.ZosmfIssueParams;
import zosconsole.zosmf.ZosmfIssueResponse;

/**
 * Class example to showcase mvs console command functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueCommand {

    private static final Logger LOG = LogManager.getLogger(IssueCommand.class);

    /**
     * Main method defines z/OSMF host and user connection, and mvs command used for the example test.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String hostName = "XXX";
        String zosmfPort = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String command = "D IPLINFO";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        IssueCommand.consoleCmdByIssue(connection, command);
        IssueCommand.consoleCmdByIssueSimple(connection, command);
        IssueCommand.consoleCmdByIssueDefConsoleCommon(connection, command);
    }

    /**
     * Issue IssueCommend issue method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void consoleCmdByIssue(ZOSConnection connection, String cmd) {
        IssueParams params = new IssueParams();
        params.setCommand(cmd);
        ConsoleResponse response;
        zosconsole.IssueCommand issueCommand = new zosconsole.IssueCommand(connection);
        try {
            response = issueCommand.issue(params);
            LOG.info(response.getCommandResponse().get());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Issue IssueCommend issueSimple method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void consoleCmdByIssueSimple(ZOSConnection connection, String cmd) {
        ConsoleResponse response;
        zosconsole.IssueCommand issueCommand = new zosconsole.IssueCommand(connection);
        try {
            response = issueCommand.issueSimple(cmd);
            LOG.info(response.getCommandResponse().get());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Issue IssueCommend issueDefConsoleCommon method which will execute the given mvs console command
     *
     * @param connection connection information, see ZOSConnection object
     * @param cmd        mvs command to execute
     * @author Frank Giordano
     */
    public static void consoleCmdByIssueDefConsoleCommon(ZOSConnection connection, String cmd) {
        ZosmfIssueParams params = new ZosmfIssueParams();
        params.setCmd(cmd);
        ZosmfIssueResponse zResponse;
        zosconsole.IssueCommand issueCommand = new zosconsole.IssueCommand(connection);
        try {
            zResponse = issueCommand.issueDefConsoleCommon(params);
            LOG.info(zResponse.getCmdResponse().get());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

}
