/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosconsole.ConsoleResponse;
import zosconsole.IssueCommand;
import zosconsole.zosmf.IssueParms;
import core.ZOSConnection;
import zosconsole.zosmf.ZosmfIssueParms;
import zosconsole.zosmf.ZosmfIssueResponse;

public class IssueCommandTest {

    private static final Logger LOG = LogManager.getLogger(IssueCommandTest.class);

    public static void main(String[] args) {
        String hostName = "xxxxxxxx";
        String port = "xxxxxxxx";
        String userName = "xxxxxxxx";
        String password = "xxxxxxxx";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        String command = "D IPLINFO";

        IssueCommandTest.tstConsoleCmdByIssue(connection, command);
        IssueCommandTest.tstConsoleCmdByIssueSimple(connection, command);
        IssueCommandTest.tstConsoleCmdByIssueDefConsoleCommon(connection, command);
    }

    public static void tstConsoleCmdByIssue(ZOSConnection connection, String cmd) {
        IssueParms parms = new IssueParms();
        parms.setCommand(cmd);
        ConsoleResponse response;
        try {
            response = IssueCommand.issue(connection, parms);
            LOG.info(response.getCommandResponse().get());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public static void tstConsoleCmdByIssueSimple(ZOSConnection connection, String cmd) {
        ConsoleResponse response;
        try {
            response = IssueCommand.issueSimple(connection, cmd);
            LOG.info(response.getCommandResponse().get());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    public static void tstConsoleCmdByIssueDefConsoleCommon(ZOSConnection connection, String cmd) {
        ZosmfIssueParms zparms = new ZosmfIssueParms();
        zparms.setCmd(cmd);
        ZosmfIssueResponse zResponse;
        try {
            zResponse = IssueCommand.issueDefConsoleCommon(connection, zparms);
            LOG.info(zResponse.getCmdResponse().get());
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

}
