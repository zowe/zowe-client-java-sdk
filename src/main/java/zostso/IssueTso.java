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
import zostso.zosmf.ZosmfTsoResponse;

import java.util.ArrayList;

/**
 * Class to handle issue command to TSO
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueTso {

    private final ZOSConnection connection;

    /**
     * IssueTso constructor
     *
     * @param connection connection information, see ZOSConnection object
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
        Util.checkIllegalParameter(accountNumber.isEmpty(), "accountNumber not specified");
        Util.checkIllegalParameter(command.isEmpty(), "command not specified");

        // first stage open tso servlet session to use for our tso command processing
        StartTso startTso = new StartTso(connection);
        StartStopResponses startResponse = startTso.start(accountNumber, startParams);

        if (startResponse == null)
            throw new Exception("Severe failure getting started TSO address space.");

        if (!startResponse.isSuccess()) {
            throw new Exception("TSO address space failed to start. Error: " +
                    (startResponse.getFailureResponse().orElse("Unknown error")));
        }

        IssueResponse issueResponse = new IssueResponse();
        issueResponse.setStartResponse(startResponse);

        var zosmfTsoResponses = new ArrayList<ZosmfTsoResponse>();
        zosmfTsoResponses.add(startResponse.getZosmfTsoResponse()
                .orElseThrow(() -> new Exception("no zosmf start tso response")));

        var servletKey = startResponse.getServletKey()
                .orElseThrow(() -> new Exception("no servletKey key value returned from startTso"));

        // second stage send command to tso servlet session created in first stage and collect all tso responses
        SendTso sendTso = new SendTso(connection);
        SendResponse sendResponse = sendTso.sendDataToTSOCollect(servletKey, command);
        issueResponse.setSuccess(sendResponse.getSuccess());
        zosmfTsoResponses.addAll(sendResponse.getZosmfResponses()
                .orElseThrow(() -> new Exception("no zosmf send tso response")));

        issueResponse.setZosmfResponses(zosmfTsoResponses);

        // lastly save the command response to our issueResponse reference
        issueResponse.setCommandResponses(sendResponse.getCommandResponse());

        // third stage here where the tso end session operation is performed
        StopTso stopTso = new StopTso(connection);
        StartStopResponse stopResponse = stopTso.stop(servletKey);
        issueResponse.setStopResponse(stopResponse);

        return issueResponse;
    }

}
