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

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.JsonUtils;
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
    private static final int DEFAULT_IDLE_TIMEOUT = 5;
    private static final int DEFAULT_POLL_INTERVAL = 100;
    private final List<String> msgLst = new ArrayList<>();
    private final List<String> promptLst = new ArrayList<>();
    private final ZosConnection connection;
    private final String accountNumber;
    private TsoStart tsoStart;
    private TsoStop tsoStop;
    private TsoSend tsoSend;
    private TsoReply tsoReply;
    private StartTsoInputData inputData;
    private final long promptTimeoutNanos;
    private final long idleTimeoutNanos;

    /**
     * TsoCmd constructor
     *
     * @param connection    for connection information, see ZosConnection object
     * @param accountNumber account number for tso processing
     */
    public TsoCmd(final ZosConnection connection, final String accountNumber) {
        ValidateUtils.checkNullParameter(connection, "connection");
        ValidateUtils.checkIllegalParameter(accountNumber, "accountNumber");
        this.connection = connection;
        this.accountNumber = accountNumber;
        this.promptTimeoutNanos = TimeUnit.MINUTES.toNanos(DEFAULT_PROMPT_TIMEOUT);
        this.idleTimeoutNanos = TimeUnit.MINUTES.toNanos(DEFAULT_IDLE_TIMEOUT);
    }

    /**
     * Alternative TsoCmd constructor intended primarily for internal unit testing with Mockito.
     * This constructor allows mocked TSO components and custom timeout values to be supplied
     * so command and timeout behavior can be tested without waiting for the production timeout durations.
     * <p>
     * This constructor has package-private visibility and is not intended for general SDK usage.
     *
     * @param connection         connection information, see ZosConnection
     * @param accountNumber      account number for TSO processing
     * @param tsoStart           TsoStart instance for mocking
     * @param tsoStop            TsoStop instance for mocking
     * @param tsoSend            TsoSend instance for mocking
     * @param tsoReply           TsoReply instance for mocking
     * @param promptTimeoutNanos maximum time in nanoseconds to wait for the TSO command completion prompt
     * @param idleTimeoutNanos   maximum time in nanoseconds to wait without receiving TSO output
     */
    TsoCmd(final ZosConnection connection,
           final String accountNumber,
           final TsoStart tsoStart,
           final TsoStop tsoStop,
           final TsoSend tsoSend,
           final TsoReply tsoReply,
           final long promptTimeoutNanos,
           final long idleTimeoutNanos) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.connection = connection;
        this.accountNumber = accountNumber;
        this.tsoStart = tsoStart;
        this.tsoStop = tsoStop;
        this.tsoSend = tsoSend;
        this.tsoReply = tsoReply;
        this.promptTimeoutNanos = promptTimeoutNanos;
        this.idleTimeoutNanos = idleTimeoutNanos;
    }

    /**
     * Issue TSO command API call to process the given command via z/OSMF restful api
     *
     * @param command tso command string
     * @return list of all tso returned messages
     * @throws ZosmfRequestException request error state
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
     */
    public List<String> issueCommand(final String command, final StartTsoInputData inputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(command, "command");
        this.msgLst.clear();
        this.promptLst.clear();

        // send tso start call and return the session id
        final TsoStartResponse tsoStartResponse = this.startTso(inputData);
        if (!tsoStartResponse.isSuccess()) {
            final JsonNode rootNode = JsonUtils.parse(tsoStartResponse.getResponse());
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
     */
    public List<String> issueCommandByTsoSessionId(final String sessionId, final String command)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(sessionId, "sessionId");
        ValidateUtils.checkIllegalParameter(command, "command");

        this.msgLst.clear();
        this.promptLst.clear();

        // initialize the components if they don't exist yet
        if (this.tsoSend == null) {
            this.tsoSend = new TsoSend(this.connection);
        }
        if (this.tsoReply == null) {
            this.tsoReply = new TsoReply(this.connection);
        }

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
     */
    private void executeCommand(final String sessionId, final String command) throws ZosmfRequestException {
        // send tso command to execute with session id
        LOG.debug("Executing TSO command '{}' for session ID {}", command, sessionId);
        String responseStr = this.sendTsoCommand(sessionId, command);
        LOG.debug("sendCommand response: {}", responseStr);
        JsonNode rootNode = JsonUtils.parse(responseStr);
        this.processTsoResponse(this.getTsoDataNode(rootNode));

        // check if sendTsoCommand already returned a completion prompt
        boolean tsoMessagesReceived = !this.promptLst.isEmpty();
        LOG.debug("After sendCommand: promptLst empty? {}, msgLst size = {}",
                this.promptLst.isEmpty(), this.msgLst.size());

        // preparation variables for reply loop
        long startTime = System.nanoTime();
        long idleStartTime = startTime;
        int pollCount = 0;

        while (!tsoMessagesReceived && System.nanoTime() - startTime < this.promptTimeoutNanos) {
            pollCount++;
            LOG.debug("Entering reply poll iteration #{} for session ID {}", pollCount, sessionId);
            // retrieve additional tso messages for the command
            responseStr = this.sendTsoForReply(sessionId);
            LOG.debug("sendTsoForReply response #{}: {}", pollCount, responseStr);
            rootNode = JsonUtils.parse(responseStr);
            JsonNode tsoDataNode = this.getTsoDataNode(rootNode);
            this.processTsoResponse(tsoDataNode);
            final boolean hasData = tsoDataNode != null && !tsoDataNode.isEmpty();
            if (hasData) {
                idleStartTime = System.nanoTime();
            }

            // a z/OSMF timeout flag only means this single reply request waited its server-side limit without
            // receiving output; the command may still be running, so keep polling until the TSO prompt is
            // seen, DEFAULT_IDLE_TIMEOUT passes with no output, or DEFAULT_PROMPT_TIMEOUT is reached
            final boolean isTimeout = rootNode != null &&
                    rootNode.has("timeout") &&
                    rootNode.get("timeout").asBoolean();
            if (isTimeout) {
                LOG.debug("z/OSMF reply request timeout flag detected in response #{}, continuing to poll", pollCount);
            }

            // a reply without data (z/OSMF timeout flagged or empty) is silence, check if it has lasted too long
            if (!hasData && this.promptLst.isEmpty() && System.nanoTime() - idleStartTime >= this.idleTimeoutNanos) {
                LOG.error("No TSO output for {} minutes for command '{}' on session ID {}",
                        DEFAULT_IDLE_TIMEOUT, command, sessionId);
                throw new ZosmfRequestException("No TSO output received for " + DEFAULT_IDLE_TIMEOUT +
                        " minutes while waiting for TSO command to complete");
            }

            if (!this.promptLst.isEmpty()) {
                tsoMessagesReceived = true;
                LOG.debug("Command execution completed after poll #{}", pollCount);
            } else if (!isTimeout && !hasData) {
                LOG.debug("No TSO data returned after poll #{}", pollCount);
                // Debounce only when no data was returned in this poll (matches Zowe CLI SendTso.ts)
                WaitUtil.wait(DEFAULT_POLL_INTERVAL);
            }
        }

        // is DEFAULT_PROMPT_TIMEOUT (in minutes) reached?
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
     */
    public void drainLogonPrompt(final TsoStartResponse startResponse) throws ZosmfRequestException {
        if (startResponse == null ||
                startResponse.getResponse() == null ||
                startResponse.getResponse().trim().isEmpty()) {
            return;
        }

        LOG.debug("Processing initial startTso response for session ID {}: {}",
                startResponse.getSessionId(), startResponse.getResponse());

        final JsonNode rootNode = JsonUtils.parse(startResponse.getResponse());
        this.processTsoResponse(this.getTsoDataNode(rootNode));

        // preparation variables for reply loop
        long startTime = System.nanoTime();
        int drainCount = 0;

        LOG.debug("Draining startup TSO logon prompt");
        while (this.promptLst.isEmpty() && System.nanoTime() - startTime < this.promptTimeoutNanos) {
            drainCount++;
            LOG.debug("Drain reply poll iteration #{} for session ID {}", drainCount, startResponse.getSessionId());
            final String responseStr = this.sendTsoForReply(startResponse.getSessionId());
            LOG.debug("sendTsoForReply response during logon drain iteration #{}: {}", drainCount, responseStr);
            final JsonNode replyNode = JsonUtils.parse(responseStr);
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
