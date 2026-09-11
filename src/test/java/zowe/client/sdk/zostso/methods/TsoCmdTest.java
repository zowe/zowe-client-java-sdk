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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zostso.input.StartTsoInputData;
import zowe.client.sdk.zostso.response.TsoStartResponse;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TsoCmd class.
 * <p>
 * These tests validate constructor checks, private method behavior,
 * request execution, and the full TSO command flow with mocked dependencies.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class TsoCmdTest {

    private ZosConnection mockConnection;
    private TsoStart mockTsoStart;
    private TsoSend mockTsoSend;
    private TsoReply mockTsoReply;
    private TsoStop mockTsoStop;
    private String account = "ACCT123";
    private String sessionId = "SESSION123";
    private String command = "LISTDS";

    /**
     * Initializes mocked dependencies before each test. This ensures that
     * each test runs with a fresh mock setup for connection and TSO classes.
     */
    @BeforeEach
    public void setUp() {
        account = "ACCT123";
        sessionId = "SESSION123";
        command = "LISTDS";

        mockConnection = mock(ZosConnection.class);
        mockTsoStart = mock(TsoStart.class);
        mockTsoSend = mock(TsoSend.class);
        mockTsoReply = mock(TsoReply.class);
        mockTsoStop = mock(TsoStop.class);
    }

    /**
     * Tests issuing a TSO command when the final terminal milestone returns a standard
     * unmasked text prompt (DATA: READY) completely missing the HIDDEN key.
     * <p>
     * Verifies that the updated parsing logic accurately catches the TSO PROMPT block
     * based on its object container existence, successfully exiting the reply loop.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandWithStandardTextPromptMissingHiddenSuccess() throws Exception {
        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"LISTDS PROCESSING COMPLETE\"}}]}";

        // This payload mimics a standard z/OSMF text milestone completely lacking a "HIDDEN" key.
        // The old code would hang indefinitely here; the new code must exit the loop cleanly.
        String secondResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"DATA\":\"READY\"}}]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, ""));
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockTsoReply.reply(sessionId)).thenReturn(secondResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(1, result.size());
        assertEquals("LISTDS PROCESSING COMPLETE", result.get(0));

        verify(mockTsoStart, times(1)).start(any(StartTsoInputData.class));
        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoReply, times(1)).reply(sessionId); // Loops exactly once because the text prompt is caught
        verify(mockTsoStop, times(1)).stop(sessionId);
    }

    /**
     * Tests that when a fast command returns both the message data AND the closing TSO prompt
     * inside the very first response payload, the class short-circuits and skips the loop entirely.
     * <p>
     * Verifies that tsoReply.reply() is never invoked, preventing redundant commands on an idle session.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandShortCircuitsWhenPromptInFirstPayloadSuccess() throws Exception {
        // Single response containing everything
        String instantResponse = "{\"tsoData\":["
                + "{\"TSO MESSAGE\":{\"DATA\":\"FAST OUTPUT\"}},"
                + "{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"DATA\":\"READY\"}}"
                + "]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, ""));
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(instantResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(1, result.size());
        assertEquals("FAST OUTPUT", result.get(0));

        verify(mockTsoStart, times(1)).start(any(StartTsoInputData.class));
        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoReply, never()).reply(anyString());
        verify(mockTsoStop, times(1)).stop(sessionId);
    }

    /**
     * Tests that when sendCommand returns a prompt-only payload (0 messages, e.g. early startup prompt),
     * the reply loop executes and polls sendTsoForReply until the final prompt is retrieved.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    /**
     * Tests that when sendCommand returns a prompt-only payload, the command completes immediately
     * without unnecessary GET polling.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandCompletesWhenSendCommandReturnsPromptOnlySuccess() throws Exception {
        String promptOnlyResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":\"FALSE\"}}]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, ""));
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(promptOnlyResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        List<String> result = issueTso.issueCommand(command);

        verify(mockTsoStart, times(1)).start(any(StartTsoInputData.class));
        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoReply, never()).reply(anyString());
        verify(mockTsoStop, times(1)).stop(sessionId);
    }

    /**
     * Tests that when startTso returns a startup payload missing the logon prompt,
     * drainLogonPrompt polls reply until the logon prompt is drained, clearing startup noise,
     * before sendCommand executes cleanly.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandDrainsLogonPromptOnStartSuccess() throws Exception {
        String startResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"IKJ56455I LOGON IN PROGRESS\"}}]}";
        String logonPromptResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"READY \"}},{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":\"FALSE\"}}]}";
        String commandResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"COMMAND OUTPUT\"}},{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":\"FALSE\"}}]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, startResponse));
        when(mockTsoReply.reply(sessionId)).thenReturn(logonPromptResponse);
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(commandResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(1, result.size());
        assertEquals("COMMAND OUTPUT", result.get(0));

        verify(mockTsoStart, times(1)).start(any(StartTsoInputData.class));
        verify(mockTsoReply, times(1)).reply(sessionId); // Drained logon prompt
        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoStop, times(1)).stop(sessionId);
    }

    /**
     * Tests that a payload containing a READY message alongside a TSO prompt
     * (e.g. silent commands like ALLOCATE, FREE, or DELETE) polls reply and returns READY cleanly.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandCompletesOnFirstPayloadWithReadyMessageAndPromptSuccess() throws Exception {
        String response = "{\"tsoData\":["
                + "{\"TSO MESSAGE\":{\"DATA\":\"READY \"}},"
                + "{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":\"FALSE\"}}"
                + "]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, ""));
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(response);
        when(mockTsoReply.reply(sessionId)).thenReturn(response);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(1, result.size());
        assertEquals("READY ", result.get(0));

        verify(mockTsoStart, times(1)).start(any(StartTsoInputData.class));
        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoReply, never()).reply(anyString());
        verify(mockTsoStop, times(1)).stop(sessionId);
    }

    /**
     * Tests issuing a TSO command when the initial response contains a TSO message
     * and later responses contain a TSO prompt.
     * <p>
     * Verifies that the command response is collected, the reply loop continues
     * until a prompt is returned, and that the session is properly started and stopped.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandWithTsoMessageAndPromptSuccess() throws Exception {
        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"JOB STARTED\"}}]}";
        String secondResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":\"FALSE\"}}]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, ""));
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockTsoReply.reply(sessionId)).thenReturn(secondResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(1, result.size());
        assertEquals("JOB STARTED", result.get(0));
        assertEquals(account, issueTso.getInputData().getAccount().orElse(null));

        verify(mockTsoStart, times(1)).start(any(StartTsoInputData.class));
        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoReply, atLeastOnce()).reply(sessionId);
        verify(mockTsoStop, times(1)).stop(sessionId);
    }

    /**
     * Tests issuing a TSO command when the initial response contains a TSO message
     * and later responses contain a TSO prompt with a different account number than
     * the initial request.
     * <p>
     * Verifies that the command response is collected, the reply loop continues
     * until a prompt is returned, and that the session is properly started and stopped.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandWithTsoMessageAndPromptWithDifferentAccountNumberSuccess() throws Exception {
        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"JOB STARTED\"}}]}";
        String secondResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":\"FALSE\"}}]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, ""));
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockTsoReply.reply(sessionId)).thenReturn(secondResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        StartTsoInputData inputData = new StartTsoInputData();
        inputData.setAccount("ACCT456");
        List<String> result = issueTso.issueCommand(command, inputData);

        assertEquals(1, result.size());
        assertEquals("JOB STARTED", result.get(0));
        assertEquals(account, issueTso.getInputData().getAccount().orElse(null));

        verify(mockTsoStart, times(1)).start(any(StartTsoInputData.class));
        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoReply, atLeastOnce()).reply(sessionId);
        verify(mockTsoStop, times(1)).stop(sessionId);
    }

    /**
     * Tests issuing a TSO command when multiple messages are returned, and the
     * loop exits on the first reply containing a prompt.
     * <p>
     * Verifies that the message is captured correctly and that the TSO session
     * is stopped after execution.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandWithTsoMultipleMessagesAndPromptSuccess() throws Exception {
        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"RUNNING\"}}]}";
        String firstReplyResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"RUNNING2\"}}]}";
        String secondReplyResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"HIDDEN\":\"FALSE\"}}]}";

        when(mockTsoStart.start(any(StartTsoInputData.class))).thenReturn(
                new TsoStartResponse(true, sessionId, ""));
        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockTsoReply.reply(sessionId)).thenReturn(firstReplyResponse, secondReplyResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(List.of("RUNNING", "RUNNING2"), result);
        verify(mockTsoStop).stop(sessionId);
    }

    /**
     * Tests that an exception is thrown if starting the TSO session fails.
     * <p>
     * Verifies that the exception message matches the failure reason and that the
     * TSO session is not stopped since it was never started.
     *
     * @throws ZosmfRequestException if the test setup fails
     */
    @Test
    public void tstIssueCommandThrowsZosmfRequestExceptionFailure() throws ZosmfRequestException {
        when(mockTsoStart.start(any())).thenThrow(new ZosmfRequestException("start failed"));

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                "ACCTFAIL",
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );

        try {
            issueTso.issueCommand("TIME");
        } catch (ZosmfRequestException e) {
            assertEquals("start failed", e.getMessage());
        }

        verify(mockTsoStop, never()).stop(anyString());
    }

    /**
     * Tests issuing a TSO command by reusing an existing session ID.
     * <p>
     * Verifies that the command response is collected cleanly, the reply loop handles
     * the payload milestones, and crucially, neither startTso nor stopTso are invoked.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandByTsoSessionIdSuccess() throws Exception {
        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"DATA CHUNK 1\"}}]}";
        String secondResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"DATA\":\"READY\"}}]}";

        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockTsoReply.reply(sessionId)).thenReturn(secondResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );

        List<String> result = issueTso.issueCommandByTsoSessionId(sessionId, command);

        assertEquals(1, result.size());
        assertEquals("DATA CHUNK 1", result.get(0));

        // CRITICAL CONTEXT VALIDATION:
        // Lifecycle management must be completely bypassed to allow continuous state reuse
        verify(mockTsoStart, never()).start(any());
        verify(mockTsoStop, never()).stop(anyString());

        verify(mockTsoSend, times(1)).sendCommand(sessionId, command);
        verify(mockTsoReply, times(1)).reply(sessionId);
    }

    /**
     * Tests that the "Connection: close" isolation header is successfully wiped from
     * both underlying execution components during the finally block cleanup step.
     * <p>
     * This ensures that subsequent standard operations are not starved of connection pooling.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandByTsoSessionIdClearsHeadersOnFinally() throws Exception {
        String firstResponse = "{\"tsoData\":[{\"TSO MESSAGE\":{\"DATA\":\"FAST LINE\"}},"
                + "{\"TSO PROMPT\":{\"VERSION\":\"0100\",\"DATA\":\"READY\"}}]}";

        when(mockTsoSend.sendCommand(sessionId, command)).thenReturn(firstResponse);

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );

        issueTso.issueCommandByTsoSessionId(sessionId, command);

        // Verify that the isolation header was injected before execution
        Map<String, String> expectedIsolationHeader = Map.of("Connection", "close");
        verify(mockTsoSend).setHeaders(expectedIsolationHeader);
        verify(mockTsoReply).setHeaders(expectedIsolationHeader);

        // CRITICAL POOL CLEANUP CHECK:
        // Verify that an empty map was passed to scrub the headers clean in the finally block
        Map<String, String> expectedCleanupHeader = Map.of();
        verify(mockTsoSend).setHeaders(expectedCleanupHeader);
        verify(mockTsoReply).setHeaders(expectedCleanupHeader);
    }

    /**
     * Tests that the "Connection: close" header cleanup occurs even if the transaction
     * throws a ZosmfRequestException mid-execution.
     * <p>
     * Verifies that network isolation logic does not break the instance states on failures.
     *
     * @throws Exception if a mocked service call fails unexpectedly
     */
    @Test
    public void tstIssueCommandByTsoSessionIdClearsHeadersOnExceptionFailure() throws Exception {
        when(mockTsoSend.sendCommand(sessionId, command)).thenThrow(new ZosmfRequestException("Network dropped"));

        TsoCmd issueTso = new TsoCmd(
                mockConnection,
                account,
                mockTsoStart,
                mockTsoStop,
                mockTsoSend,
                mockTsoReply
        );

        assertThrows(ZosmfRequestException.class, () ->
                issueTso.issueCommandByTsoSessionId(sessionId, command)
        );

        // Verify that despite the exception breaking the try block, the finally block still wiped the map
        Map<String, String> expectedCleanupHeader = Map.of();
        verify(mockTsoSend).setHeaders(expectedCleanupHeader);
        verify(mockTsoReply).setHeaders(expectedCleanupHeader);
    }

    /**
     * Verifies that issueCommandByTsoSessionId throws an IllegalArgumentException when
     * the passed sessionId parameter is empty or null.
     */
    @Test
    public void tstIssueCommandByTsoSessionIdNullIdFailure() {
        TsoCmd issueTso = new TsoCmd(mockConnection, account);

        IllegalArgumentException exNull = assertThrows(
                IllegalArgumentException.class,
                () -> issueTso.issueCommandByTsoSessionId(null, command)
        );
        assertEquals("sessionId is either null or empty", exNull.getMessage());

        IllegalArgumentException exEmpty = assertThrows(
                IllegalArgumentException.class,
                () -> issueTso.issueCommandByTsoSessionId("", command)
        );
        assertEquals("sessionId is either null or empty", exEmpty.getMessage());
    }

    /**
     * Verifies that the constructor throws a NullPointerException when the connection is null.
     */
    @Test
    public void tstIssueTsoConnectionNullFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoCmd(null, "1")
        );
        assertEquals("connection is null", ex.getMessage());
    }

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the account number is null.
     */
    @Test
    public void tstIssueTsoAccountNumberNullFailure() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new TsoCmd(mockConnection, null)
        );
        assertEquals("accountNumber is either null or empty", ex.getMessage());
    }

    /**
     * Verifies that issueCommand throws an IllegalArgumentException when the command string is null.
     */
    @Test
    public void tstIssueCommandNullFailure() {
        TsoCmd issueTso = new TsoCmd(mockConnection, "ACCT123");
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> issueTso.issueCommand(null)
        );
        assertEquals("command is either null or empty", ex.getMessage());
    }

    /**
     * Verifies that the alternative constructor throws a NullPointerException when the connection is null.
     */
    @Test
    public void tstAlternativeIssueTsoConnectionNullFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoCmd(null, "1", null,
                        null, null, null)
        );
        assertEquals("connection is null", ex.getMessage());
    }

}
