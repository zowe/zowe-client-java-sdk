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
        Util.checkNullParameter(accountNumber == null, "accountNumber is null");
        Util.checkNullParameter(command == null, "command is null");
        Util.checkStateParameter(accountNumber.isEmpty(), "accountNumber not specified");
        Util.checkStateParameter(command.isEmpty(), "command not specified");

        IssueResponse response = new IssueResponse(false, null, false, null,
                null, null);
        StartStopResponses StartResponse = StartTso.start(connection, accountNumber, startParams);
        response.setStartResponse(Optional.ofNullable(StartResponse));

        if (response.getStartResponse().isPresent() && !response.getStartResponse().get().isSuccess()) {
            throw new Exception("TSO address space failed to start. Error: " +
                    (response.getStartResponse().isPresent() ? response.getStartResponse().get().getFailureResponse() :
                    "Unknown error"));
        }

        response.setZosmfResponse(Optional.ofNullable(StartResponse.getZosmfTsoResponse().get()));

        SendResponse sendResponse = SendTso.sendDataToTSOCollect(connection,
                response.getStartResponse().get().getServletKey().get(), command);
        response.setSuccess(sendResponse.getSuccess());
        response.setZosmfResponse(Optional.of(sendResponse.getZosmfResponse().get().get(0)));  // TODO
        response.setCommandResponses(sendResponse.getCommandResponse());
        response.setStopResponse(Optional.ofNullable(
                StopTso.stop(connection, response.getStartResponse().get().getServletKey().get())));

        return response;
    }

}
