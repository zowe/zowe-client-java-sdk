/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.method;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.input.StartTsoParams;
import zowe.client.sdk.zostso.lifecycle.SendTso;
import zowe.client.sdk.zostso.lifecycle.StartTso;
import zowe.client.sdk.zostso.lifecycle.StopTso;
import zowe.client.sdk.zostso.message.ZosmfTsoResponse;
import zowe.client.sdk.zostso.response.IssueResponse;
import zowe.client.sdk.zostso.response.SendResponse;
import zowe.client.sdk.zostso.response.StartStopResponse;
import zowe.client.sdk.zostso.response.StartStopResponses;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle issue command to TSO
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class IssueTso {

    private final ZosConnection connection;

    /**
     * IssueTso constructor
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public IssueTso(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
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
    public IssueResponse issueCommand(final String accountNumber, final String command) throws Exception {
        return issueCommand(accountNumber, command, null);
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
    public IssueResponse issueCommand(final String accountNumber, final String command,
                                      final StartTsoParams startParams) throws Exception {
        ValidateUtils.checkNullParameter(accountNumber == null, "accountNumber is null");
        ValidateUtils.checkNullParameter(command == null, "command is null");
        ValidateUtils.checkIllegalParameter(accountNumber.isBlank(), "accountNumber not specified");
        ValidateUtils.checkIllegalParameter(command.isBlank(), "command not specified");
        // first stage open tso servlet session to use for our tso command processing
        final StartTso startTso = new StartTso(connection);
        final StartStopResponses startResponse = startTso.start(accountNumber, startParams);

        if (startResponse == null) {
            throw new IllegalStateException("Severe failure getting started TSO address space.");
        }

        if (!startResponse.isSuccess()) {
            throw new IllegalStateException("TSO address space failed to start. Error: " +
                    (startResponse.getFailureResponse().orElse("Unknown error")));
        }

        final IssueResponse issueResponse = new IssueResponse();
        issueResponse.setStartResponse(startResponse);

        final List<ZosmfTsoResponse> zosmfTsoResponses = new ArrayList<>();
        zosmfTsoResponses.add(startResponse.getZosmfTsoResponse()
                .orElseThrow(() -> new IllegalStateException("no zosmf start tso response")));

        final String servletKey = startResponse.getServletKey()
                .orElseThrow(() -> new IllegalStateException("no servletKey key value returned from startTso"));

        // second stage send command to tso servlet session created in first stage and collect all tso responses
        final SendTso sendTso = new SendTso(connection);
        final SendResponse sendResponse = sendTso.sendDataToTsoCollect(servletKey, command);
        issueResponse.setSuccess(sendResponse.isSuccess());
        zosmfTsoResponses.addAll(sendResponse.getZosmfResponses());

        issueResponse.setZosmfResponses(zosmfTsoResponses);

        // lastly save the command response to our issueResponse reference
        issueResponse.setCommandResponses(sendResponse.getCommandResponse()
                .orElseThrow(() -> new IllegalStateException("error getting tso command response")));

        // third stage here where the tso end session operation is performed
        final StopTso stopTso = new StopTso(connection);
        final StartStopResponse stopResponse = stopTso.stop(servletKey);
        issueResponse.setStopResponse(stopResponse);

        return issueResponse;
    }

}
