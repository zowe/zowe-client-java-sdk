/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package examples.zostso;

import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zostso.IssueResponse;
import zostso.IssueTso;

import java.util.Arrays;

/**
 * Class example to test tso command functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueTsoCommand extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(IssueTsoCommand.class);

    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection, and tso command parameters used for the example test.
     *
     * @param args for main not used
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String command = "XXX";
        String accountNumber = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        IssueResponse response = IssueTsoCommand.tsoConsoleCmdByIssue(accountNumber, command);
        String[] results = response.getCommandResponses().orElse("").split("\n");
        Arrays.stream(results).sequential().forEach(LOG::info);
    }

    /**
     * Issue issueTsoCommand method from IssueTso class which will execute the given tso command
     *
     * @param accountNumber user's z/OSMF permission account number
     * @param cmd           tso command to execute
     * @return issue response object
     * @throws Exception error processing request
     * @author Frank Giordano
     */
    public static IssueResponse tsoConsoleCmdByIssue(String accountNumber, String cmd) throws Exception {
        IssueTso issueTso = new IssueTso(connection);
        return issueTso.issueTsoCommand(accountNumber, cmd);
    }

}
