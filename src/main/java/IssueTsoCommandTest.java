/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zostso.IssueResponse;
import zostso.IssueTso;

public class IssueTsoCommandTest {

    private static final Logger LOG = LogManager.getLogger(IssueTsoCommandTest.class);

    public static void main(String[] args) {
        String hostName = "xxxxxxxx";
        String port = "xxxxxxxx";
        String userName = "xxxxxxxx";
        String password = "xxxxxxxx";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        String command = "status";
        String accountNumber = "accountNumber";

        IssueTsoCommandTest.tstTsoConsoleCmdByIssue(connection, accountNumber, command);
    }

    public static IssueResponse tstTsoConsoleCmdByIssue(ZOSConnection connection, String accountNumber, String cmd) {
        IssueResponse response = null;
        try {
            response = IssueTso.issueTsoCommand(connection, accountNumber, cmd);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        return response;
    }

}
