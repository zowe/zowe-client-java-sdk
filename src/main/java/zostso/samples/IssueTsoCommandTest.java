/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso.samples;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zostso.IssueResponse;
import zostso.IssueTso;
import zostso.StartStopResponses;
import zostso.zosmf.ZosmfTsoResponse;

import java.util.List;

public class IssueTsoCommandTest {

    private static final Logger LOG = LogManager.getLogger(IssueTsoCommandTest.class);

    public static void main(String[] args) {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String command = "XXX";
        String accountNumber = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        IssueResponse response = null;
        try {
            response = IssueTsoCommandTest.tstTsoConsoleCmdByIssue(connection, accountNumber, command);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        if (response != null && response.getStartResponse().isPresent()) {
            LOG.info(response.getStartResponse().get().isSuccess());
            LOG.info(response.getStartResponse().get().getZosmfTsoResponse().get().getVer());

            StartStopResponses startResponses = response.getStartResponse().get();
            startResponses.getCollectedResponses().get().forEach(LOG::info);
            List<ZosmfTsoResponse> zosmfTsoResponses = startResponses.getCollectedResponses().get();

            zosmfTsoResponses.forEach(tso -> {
                tso.getTsoData().get().forEach(msg -> {
                    if (msg.getTsoPrompt().isEmpty()) {
                        LOG.info(msg.getTsoMessage().get().getVersion() + " " + msg.getTsoMessage().get().getData());
                    }
                });
            });
        }
    }

    public static IssueResponse tstTsoConsoleCmdByIssue(ZOSConnection connection, String accountNumber, String cmd)
            throws Exception {
        IssueResponse response;
        IssueTso issueTso = new IssueTso(connection);
        try {
            response = issueTso.issueTsoCommand(accountNumber, cmd);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return response;
    }

}
