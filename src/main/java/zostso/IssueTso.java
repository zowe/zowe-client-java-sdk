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

/**
 * Class to handle issue command to TSO
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueTso {

    private ZOSConnection connection;

    /**
     * IssueTso constructor
     *
     * @param connection connection object, see ZOSConnection object
     * @author Frank Giordano
     */
    public IssueTso(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * API method to start a TSO address space, issue a command, collect responses until prompt is reached, and
     * terminate the address space.
     *
     * @param accountNumber accounting info for Jobs
     * @param command       command text to issue to the TSO address space.
     * @return issue tso response, see IssueResponse object
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    public IssueResponse issueTsoCommand(String accountNumber, String command) throws Exception {
        return issueTsoCommand(accountNumber, command, null);
    }

    /**
     * API method to start a TSO address space with provided parameters, issue a command,
     * collect responses until prompt is reached, and terminate the address space.
     *
     * @param accountNumber accounting info for Jobs
     * @param command       command text to issue to the TSO address space.
     * @param startParams   start tso parameters, see startParams object
     * @return issue tso response, see IssueResponse object
     * @throws Exception error executing command
     * @author Frank Giordano
     */
    public IssueResponse issueTsoCommand(String accountNumber, String command, StartTsoParams startParams)
            throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(accountNumber == null, "accountNumber is null");
        Util.checkNullParameter(command == null, "command is null");
        Util.checkStateParameter(accountNumber.isEmpty(), "accountNumber not specified");
        Util.checkStateParameter(command.isEmpty(), "command not specified");

        IssueResponse response = new IssueResponse(false, null, false, null,
                null, null);
        StartTso startTso = new StartTso(connection);
        StartStopResponses startResponse = startTso.start(accountNumber, startParams);
        response.setStartResponse(Optional.ofNullable(startResponse));

        if (response.getStartResponse().isPresent() && !response.getStartResponse().get().isSuccess()) {
            throw new Exception("TSO address space failed to start. Error: " +
                    (response.getStartResponse().isPresent() ? response.getStartResponse().get().getFailureResponse() :
                            "Unknown error"));
        }

        response.setZosmfResponse(Optional.ofNullable(startResponse.getZosmfTsoResponse().get()));

        SendTso sendTso = new SendTso(connection);
        SendResponse sendResponse = sendTso.sendDataToTSOCollect(
                response.getStartResponse().get().getServletKey().get(), command);
        response.setSuccess(sendResponse.getSuccess());
        response.setZosmfResponse(Optional.of(sendResponse.getZosmfResponse().get().get(0)));
        startResponse.setCollectedResponses(sendResponse.getZosmfResponse().get());
        response.setCommandResponses(sendResponse.getCommandResponse());
        StopTso stopTso = new StopTso(connection);
        response.setStopResponse(Optional.ofNullable(
                stopTso.stop(response.getStartResponse().get().getServletKey().get())));

        return response;
    }

}
