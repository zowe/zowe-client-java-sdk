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
import zostso.zosmf.TsoMessage;
import zostso.zosmf.TsoMessages;

import java.util.List;

public class IssueTsoCommandTest {

    private static final Logger LOG = LogManager.getLogger(IssueTsoCommandTest.class);

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String accountNumber = "XXX";
        String command = "status";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

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
                TsoMessages tsoMsgs = tsoMessages.get(i);
                TsoMessage tsoMsg = tsoMsgs.getTsoMessage().orElseThrow(Exception::new);
                System.out.println(tsoMsg.getVersion().orElse("unknown version") + " " +
                        tsoMsg.getData().orElse("unknown data"));
            }
        }
    }

    public static IssueResponse tstTsoConsoleCmdByIssue(ZOSConnection connection, String accountNumber, String cmd) throws Exception {
        IssueResponse response;
        try {
            response = IssueTso.issueTsoCommand(connection, accountNumber, cmd);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
        return response;
    }

}
