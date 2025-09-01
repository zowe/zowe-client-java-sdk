/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.methods;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StartTsoInputData;

import java.util.ArrayList;
import java.util.List;

/**
 * Issue tso command via z/OSMF restful api
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoCmd {

    private final List<String> msgLst = new ArrayList<>();
    private final List<String> promptLst = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ZosConnection connection;
    private final String accountNumber;
    private TsoStart tsoStart;
    private TsoStop tsoStop;
    private TsoSend tsoSend;
    private TsoReply tsoReply;
    private StartTsoInputData inputData;

    /**
     * TsoCmd constructor
     *
     * @param connection    ZosConnection object
     * @param accountNumber account number for tso processing
     * @author Frank Giordano
     */
    public TsoCmd(final ZosConnection connection, final String accountNumber) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkIllegalParameter(accountNumber, "accountNumber");
        this.connection = connection;
        this.accountNumber = accountNumber;
    }

    /**
     * Alternative TsoCmd constructor with ZoweRequest object. This is mainly used for internal code unit
     * testing with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection    for connection information, see ZosConnection object
     * @param accountNumber account number for tso processing
     * @param tsoStart      TsoStart for mocking
     * @param tsoStop       TsoStop for mocking
     * @param tsoSend       TsoSend for mocking
     * @param tsoReply      TsoReply for mocking
     * @author Frank Giordano
     */
    TsoCmd(final ZosConnection connection,
           final String accountNumber,
           final TsoStart tsoStart,
           final TsoStop tsoStop,
           final TsoSend tsoSend,
           final TsoReply tsoReply) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
        this.accountNumber = accountNumber;
        this.tsoStart = tsoStart;
        this.tsoStop = tsoStop;
        this.tsoSend = tsoSend;
        this.tsoReply = tsoReply;
    }

    /**
     * Issue TSO command API call to process the given command via z/OSMF restful api
     *
     * @param command tso command string
     * @return list of all tso returned messages
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<String> issueCommand(final String command) throws ZosmfRequestException {
        return this.issueCommand(command, null);
    }

    /**
     * Issue TSO command API call to process the given command via z/OSMF restful with given custom
     * parameters for the start TSO session call
     *
     * @param command   tso command string
     * @param inputData start TSO request inputs parameters, see StartTsoInputData
     * @return list of all tso returned messages
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<String> issueCommand(final String command, final StartTsoInputData inputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(command, "command");
        this.msgLst.clear();
        this.promptLst.clear();

        // send tso start call and return the session id
        final String sessionId = this.startTso(inputData);

        // send tso command to execute with session id
        String responseStr = this.sendTsoCommand(sessionId, command);
        JsonNode tsoData = this.getJsonNode(responseStr).get("tsoData");
        this.processTsoData(tsoData);

        boolean tsoMessagesReceived = false;
        while (!tsoMessagesReceived) {
            // retrieve additional tso messages for the command
            responseStr = this.sendTsoForReply(sessionId);
            tsoData = this.getJsonNode(responseStr).get("tsoData");
            this.processTsoData(tsoData);

            // check for tso prompt message - indicates the end of the command
            if (!promptLst.isEmpty()) {
                tsoMessagesReceived = true;
            }
        }

        // stop the tso session
        this.stopTso(sessionId);
        return msgLst;
    }

    /**
     * Make the first TSO request to start the TSO session and retrieve its session id (servletKey).
     *
     * @param inputData start TSO request inputs parameters, see StartTsoInputData
     * @return string value representing the session id (servletKey)
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private String startTso(final StartTsoInputData inputData) throws ZosmfRequestException {
        if (tsoStart == null) {
            tsoStart = new TsoStart(connection);
        }
        this.inputData = inputData;
        if (this.inputData == null) {
            this.inputData = new StartTsoInputData();
        }
        this.inputData.setAccount(accountNumber);
        return tsoStart.start(this.inputData);
    }

    /**
     * Make the second request to send TSO the command to perform via z/OSMF
     *
     * @param sessionId servletKey id retrieve from start TSO request
     * @param command   tso command
     * @return response string representing the returned request payload
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private String sendTsoCommand(final String sessionId, final String command) throws ZosmfRequestException {
        if (tsoSend == null) {
            tsoSend = new TsoSend(connection);
        }
        return tsoSend.sendCommand(sessionId, command);
    }

    /**
     * Send a request to z/OSMF TSO for additional TSO message data
     *
     * @param sessionId servletKey id retrieve from start TSO request
     * @return response string representing the returned request payload
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private String sendTsoForReply(final String sessionId) throws ZosmfRequestException {
        if (tsoReply == null) {
            tsoReply = new TsoReply(connection);
        }
        return tsoReply.reply(sessionId);
    }

    /**
     * Stop the TSO session by session id (servletKey)
     *
     * @param sessionId servletKey id retrieve from start TSO request
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private void stopTso(final String sessionId) throws ZosmfRequestException {
        if (tsoStop == null) {
            tsoStop = new TsoStop(connection);
        }
        tsoStop.stop(sessionId);
    }

    /**
     * Transform the JSON response payload for its tso message types
     *
     * @param tsoData JsonNode object
     * @author Frank Giordano
     */
    private void processTsoData(final JsonNode tsoData) {
        if (tsoData == null || !tsoData.isArray()) {
            return;
        }
        tsoData.forEach(tsoDataItem -> {
            // extract message text if present
            final JsonNode messageNode = tsoDataItem.get(TsoConstants.TSO_MESSAGE);
            if (messageNode != null && messageNode.hasNonNull("DATA")) {
                this.msgLst.add(messageNode.get("DATA").asText());
            }
            // extract prompt hidden text if present (signals the end of conversation)
            final JsonNode promptNode = tsoDataItem.get(TsoConstants.TSO_PROMPT);
            if (promptNode != null && promptNode.hasNonNull("HIDDEN")) {
                this.promptLst.add(promptNode.get("HIDDEN").asText());
            }
        });
    }

    /**
     * Transform a response string representing a JSON returned payload from a tso call into a JsonNode to
     * be used for parsing the response.
     *
     * @param responseStr response string
     * @return JsonNode object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private JsonNode getJsonNode(final String responseStr) throws ZosmfRequestException {
        final JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(responseStr);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException(TsoConstants.SEND_TSO_FAIL_MSG + " Response: " + e.getMessage());
        }
        return rootNode;
    }

    /**
     * Returns the input data for the start TSO session call
     * This is private-package
     *
     * @return StartTsoInputData object
     */
    StartTsoInputData getInputData() {
        return inputData;
    }

}
