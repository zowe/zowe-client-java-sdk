/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso;

import core.ZOSConnection;
import utility.Util;
import zostso.input.StartTsoParams;

import java.util.Optional;

public class IssueTso {

    public static IssueResponse issueTsoCommand(ZOSConnection connection, String accountNumber,
                                                String command) throws Exception {
        return issueTsoCommand(connection, accountNumber, command, null);
    }

    public static IssueResponse issueTsoCommand(ZOSConnection connection, String accountNumber,
                                                String command, StartTsoParams startParams) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(accountNumber == null, "accountNumber not specified");
        Util.checkNullParameter(command == null, "command not specified");
        Util.checkStateParameter(accountNumber.isEmpty(), "accountNumber is empty");
        Util.checkStateParameter(command.isEmpty(), "command is empty");

        IssueResponse response = new IssueResponse(false, null, false, null,
                null, null);
        response.setStartResponse(StartTso.start(connection, accountNumber, startParams));

        if (response.startResponse.isPresent() && !response.startResponse.get().success) {
            throw new Exception("TSO address space failed to start.");
        }

        SendResponse sendResponse = SendTso.sendDataToTSOCollect(connection,
                response.startResponse.get().servletKey, command);
        response.success = sendResponse.getSuccess();
        response.zosmfResponse = Optional.of(sendResponse.getZosmfResponse().get().get(0));  // TODO
        response.commandResponses = sendResponse.getCommandResponse();
        response.stopResponses = Optional.ofNullable(StopTso.stop(connection, response.startResponse.get().servletKey));

        return response;
    }

}
