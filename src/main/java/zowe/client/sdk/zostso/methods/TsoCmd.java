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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.WaitUtil;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StartTsoInputData;
import zowe.client.sdk.zostso.response.TsoStartResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Issue tso command via z/OSMF restful api
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-issue-tsoe-command-zosmf-rest-api">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class TsoCmd {

    private static final Logger LOG = LoggerFactory.getLogger(TsoCmd.class);

    private static final int DEFAULT_PROMPT_TIMEOUT = 30;
    private static final int DEFAULT_POLL_INTERVAL = 100;
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
     * @param connection    for connection information, see ZosConnection object
     * @param accountNumber account number for tso processing
     * @author Frank Giordano
     */
    public TsoCmd(final ZosConnection connection, final String accountNumber) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkIllegalParameter(accountNumber, "accountNumber");
        this.connection = connection;
        this.accountNumber = accountNumber;
    }

    /**
     * Alternative TsoCmd constructor with ZoweRequest object. This is mainly used for internal code unit
     * testing with Mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private visibility.
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
        ValidateUtils.checkNullParameter(connection, "connection");
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
        final TsoStartResponse tsoStartResponse = this.startTso(inputData);
        if (!tsoStartResponse.isSuccess()) {
            final JsonNode rootNode = this.getJsonNode(tsoStartResponse.getResponse());
            this.processTsoResponse(this.getTsoDataNode(rootNode));
            return this.msgLst;
        }
        try {
            this.drainLogonPrompt(tsoStartResponse);
            this.executeCommand(tsoStartResponse.getSessionId(), command);
        } finally {
            // stop the tso session
            this.stopTso(tsoStartResponse.getSessionId());
        }

        return msgLst;
    }

    /**
     * Reuses a persistent, long-running TSO session ID across rapid loops.
     *
     * @param sessionId existing TSO session ID
     * @param command   tso command string
     * @return list of all tso returned messages
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<String> issueCommandByTsoSessionId(final String sessionId, final String command)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sessionId, "sessionId");
        ValidateUtils.checkIllegalParameter(command, "command");

        this.msgLst.clear();
        this.promptLst.clear();

        // initialize the components if they don't exist yet
        if (this.tsoSend == null) this.tsoSend = new TsoSend(this.connection);
        if (this.tsoReply == null) this.tsoReply = new TsoReply(this.connection);

        this.executeCommand(sessionId, command);

        // NOTICE: We omit stopTso() completely so the host context stays alive for the next command!
        return this.msgLst;
    }

    /**
     * Helper method to send a TSO command to an active TSO session ID and poll for response messages.
     *
     * @param sessionId active TSO session ID
     * @param command   tso command string
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private void executeCommand(final String sessionId, final String command) throws ZosmfRequestException {
        // send tso command to execute with session id
        LOG.debug("Executing TSO command '{}' for session ID {}", command, sessionId);
        String responseStr = this.sendTsoCommand(sessionId, command);
        LOG.debug("sendCommand response: {}", responseStr);
        JsonNode rootNode = this.getJsonNode(responseStr);
        this.processTsoResponse(this.getTsoDataNode(rootNode));

        // check if sendTsoCommand already returned a completion prompt
        boolean tsoMessagesReceived = !this.promptLst.isEmpty();
        LOG.debug("After sendCommand: promptLst empty? {}, msgLst size = {}",
                this.promptLst.isEmpty(), this.msgLst.size());

        // setup variables for reply loop
        long startTime = System.nanoTime();
        long timeoutNanos = TimeUnit.MINUTES.toNanos(DEFAULT_PROMPT_TIMEOUT);
        int pollCount = 0;

        while (!tsoMessagesReceived && System.nanoTime() - startTime < timeoutNanos) {
            pollCount++;
            LOG.debug("Entering reply poll iteration #{} for session ID {}", pollCount, sessionId);
            // retrieve additional tso messages for the command
            responseStr = this.sendTsoForReply(sessionId);
            LOG.debug("sendTsoForReply response #{}: {}", pollCount, responseStr);
            rootNode = this.getJsonNode(responseStr);
            JsonNode tsoDataNode = this.getTsoDataNode(rootNode);
            this.processTsoResponse(tsoDataNode);

            // check for zosmf request timeout if any
            // acts as a safeguard against future code changes or z/OSMF prompt variations
            final boolean isTimeout = rootNode != null && rootNode.has("timeout") && rootNode.get("timeout").asBoolean();
            if (isTimeout) {
                LOG.debug("z/OSMF session timeout flag detected in response #{}", pollCount);
                this.msgLst.add("z/OSMF session timeout flag detected");
            }

            if (!this.promptLst.isEmpty() || isTimeout) {
                tsoMessagesReceived = true;
                LOG.debug("Command execution completed after poll #{}", pollCount);
            } else if (tsoDataNode == null || tsoDataNode.isEmpty()) {
                LOG.debug("No TSO data returned after poll #{}", pollCount);
                // Debounce only when no data was returned in this poll (matches Zowe CLI SendTso.ts)
                WaitUtil.wait(DEFAULT_POLL_INTERVAL);
            }
        }

        // is DEFAULT_PROMPT_TIMEOUT (in minutes) reached
        if (!tsoMessagesReceived) {
            LOG.error("Timeout waiting for TSO command '{}' to complete on session ID {}", command, sessionId);
            throw new ZosmfRequestException("Timeout waiting for TSO command to complete");
        }
    }

    /**
     * Extracts the tsoData array node from a JSON root node payload.
     *
     * @param rootNode root JsonNode
     * @return tsoData JsonNode or null
     */
    private JsonNode getTsoDataNode(final JsonNode rootNode) {
        if (rootNode != null && rootNode.has("tsoData")) {
            return rootNode.get("tsoData");
        }
        return null;
    }

    /**
     * Make the first TSO request to start the TSO session and retrieve its session id (servletKey).
     *
     * @param inputData start TSO request inputs parameters, see StartTsoInputData
     * @return TsoStartResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private TsoStartResponse startTso(final StartTsoInputData inputData) throws ZosmfRequestException {
        if (this.tsoStart == null) {
            this.tsoStart = new TsoStart(this.connection);
        }
        this.inputData = inputData;
        if (this.inputData == null) {
            this.inputData = new StartTsoInputData();
        }
        this.inputData.setAccount(accountNumber);
        final TsoStartResponse response = tsoStart.start(this.inputData);
        LOG.debug("startTso response: {}", response.getResponse());
        return response;
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
        if (this.tsoSend == null) {
            this.tsoSend = new TsoSend(this.connection);
        }
        return this.tsoSend.sendCommand(sessionId, command);
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
        if (this.tsoReply == null) {
            this.tsoReply = new TsoReply(this.connection);
        }
        return this.tsoReply.reply(sessionId);
    }

    /**
     * Stop the TSO session by session id (servletKey)
     *
     * @param sessionId servletKey id retrieve from start TSO request
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private void stopTso(final String sessionId) throws ZosmfRequestException {
        if (this.tsoStop == null) {
            this.tsoStop = new TsoStop(this.connection);
        }
        this.tsoStop.stop(sessionId);
    }

    /**
     * Drains any remaining TSO logon messages and prompt from a newly started TSO session.
     * Wipes startup noise so the session is clean before executing the command.
     *
     * @param startResponse TsoStartResponse object
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public void drainLogonPrompt(final TsoStartResponse startResponse) throws ZosmfRequestException {
        if (startResponse == null ||
                startResponse.getResponse() == null ||
                startResponse.getResponse().trim().isEmpty()) {
            return;
        }

        LOG.debug("Processing initial startTso response for session ID {}: {}",
                startResponse.getSessionId(), startResponse.getResponse());

        final JsonNode rootNode = this.getJsonNode(startResponse.getResponse());
        this.processTsoResponse(this.getTsoDataNode(rootNode));

        // setup variables for reply loop
        long startTime = System.nanoTime();
        long timeoutNanos = TimeUnit.MINUTES.toNanos(DEFAULT_PROMPT_TIMEOUT);
        int drainCount = 0;

        LOG.debug("Draining startup TSO logon prompt");
        while (this.promptLst.isEmpty() && System.nanoTime() - startTime < timeoutNanos) {
            drainCount++;
            LOG.debug("Drain reply poll iteration #{} for session ID {}", drainCount, startResponse.getSessionId());
            final String responseStr = this.sendTsoForReply(startResponse.getSessionId());
            LOG.debug("sendTsoForReply response during logon drain iteration #{}: {}", drainCount, responseStr);
            final JsonNode replyNode = this.getJsonNode(responseStr);
            final JsonNode tsoDataNode = this.getTsoDataNode(replyNode);
            this.processTsoResponse(tsoDataNode);
            if (this.promptLst.isEmpty() && (tsoDataNode == null || tsoDataNode.isEmpty())) {
                // Debounce only when no data was returned in this poll (matches Zowe CLI SendTso.ts)
                WaitUtil.wait(DEFAULT_POLL_INTERVAL);
            }
        }

        if (!this.promptLst.isEmpty()) {
            LOG.debug("TSO logon prompt drained for session ID {}. Clearing startup msgLst (size {}) and promptLst",
                    startResponse.getSessionId(), this.msgLst.size());
            this.msgLst.clear();
            this.promptLst.clear();
            return;
        }

        throw new ZosmfRequestException("Timeout waiting for TSO Start Logon prompt on session ID " +
                startResponse.getSessionId());
    }

    /**
     * Processes a tsoData JsonNode array returned from a z/OSMF TSO request.
     * Extracts TSO message text into msgLst and TSO prompt entries into promptLst.
     *
     * @param tsoData tsoData JsonNode array object containing TSO_MESSAGE and TSO_PROMPT items
     * @author Frank Giordano
     */
    private void processTsoResponse(final JsonNode tsoData) {
        if (tsoData == null || !tsoData.isArray()) {
            return;
        }
        tsoData.forEach(tsoDataItem -> {
            // extract message text if present
            final JsonNode messageNode = tsoDataItem.get(TsoConstants.TSO_MESSAGE);
            if (messageNode != null && messageNode.hasNonNull("DATA")) {
                this.msgLst.add(messageNode.get("DATA").asText());
                LOG.debug("TSO message received: {}", messageNode);
            }
            final JsonNode promptNode = tsoDataItem.get(TsoConstants.TSO_PROMPT);
            if (promptNode != null) {
                this.promptLst.add(promptNode.toString());
                LOG.debug("Valid completion TSO prompt received: {}", promptNode);
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
            rootNode = this.objectMapper.readTree(responseStr);
        } catch (JsonProcessingException e) {
            throw new ZosmfRequestException("Response: " + e.getMessage());
        }
        return rootNode;
    }

    /**
     * Returns the input data for the start TSO session call
     * <p>
     * This is a private-package
     *
     * @return StartTsoInputData object
     */
    StartTsoInputData getInputData() {
        return this.inputData;
    }

}
