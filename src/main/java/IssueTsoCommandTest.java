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
import zostso.zosmf.TsoMessages;

import java.util.List;

public class IssueTsoCommandTest {

    private static final Logger LOG = LogManager.getLogger(IssueTsoCommandTest.class);

    public static void main(String[] args) {
        String hostName = "usilCA31.lvn.broadcom.net";
        String port = "1443";
        String userName = "FG892105";
        String password = "dell101D";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        String command = "Status";
        String accountNumber = "105200000";

        IssueResponse response = null;
        try {
            response = IssueTsoCommandTest.tstTsoConsoleCmdByIssue(connection, accountNumber, command);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        if (response != null && response.getStartResponse().isPresent()) {
            System.out.println(response.getStartResponse().get().isSuccess());
            System.out.println(response.getStartResponse().get().getZosmfTsoResponse().get().getVer());

            List<TsoMessages> tsoMessages = response.getZosmfResponse().get().getTsoData().get();

            for (int i = 0; i < tsoMessages.size() - 1; i++) {
                System.out.println(tsoMessages.get(i).getTsoMessage().getVersion() + " " +
                        tsoMessages.get(i).getTsoMessage().getData());
            }
        }
    }

    public static IssueResponse tstTsoConsoleCmdByIssue(ZOSConnection connection, String accountNumber, String cmd) throws Exception {
        IssueResponse response;
        try {
            response = IssueTso.issueTsoCommand(connection, accountNumber, cmd);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return response;
    }

}
