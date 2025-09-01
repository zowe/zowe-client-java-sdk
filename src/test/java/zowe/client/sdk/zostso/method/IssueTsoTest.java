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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zostso.input.StartTsoInputData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the IssueTso class.
 * <p>
 * These tests validate constructor checks, private method behavior,
 * request execution, and the full TSO command flow with mocked dependencies.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class IssueTsoTest {

    private ZosConnection mockConnection;
    private StartTso mockStartTso;
    private SendTso mockSendTso;
    private ReplyTso mockReplyTso;
    private StopTso mockStopTso;
    private String account = "ACCT123";
    private String sessionId = "SESSION123";
    private String command = "LISTDS";

    /**
     * Initializes mocked service dependencies before each test. This ensures that
     * each test runs with a fresh mock setup for connection and TSO service classes.
     */
    @BeforeEach
    public void setUp() {
        account = "ACCT123";
        sessionId = "SESSION123";
        command = "LISTDS";

        mockConnection = mock(ZosConnection.class);
        mockStartTso = mock(StartTso.class);
        mockSendTso = mock(SendTso.class);
        mockReplyTso = mock(ReplyTso.class);
        mockStopTso = mock(StopTso.class);
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
        String secondResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"HIDDEN\":\"READY\"}}]}";

        when(mockStartTso.start(any(StartTsoInputData.class))).thenReturn(sessionId);
        when(mockSendTso.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockReplyTso.reply(sessionId)).thenReturn(secondResponse);

        IssueTso issueTso = new IssueTso(
                mockConnection,
                account,
                mockStartTso,
                mockStopTso,
                mockSendTso,
                mockReplyTso
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(1, result.size());
        assertEquals("JOB STARTED", result.get(0));
        assertEquals(account, issueTso.getInputData().getAccount().orElse(null));

        verify(mockStartTso, times(1)).start(any(StartTsoInputData.class));
        verify(mockSendTso, times(1)).sendCommand(sessionId, command);
        verify(mockReplyTso, atLeastOnce()).reply(sessionId);
        verify(mockStopTso, times(1)).stop(sessionId);
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
        String secondResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"HIDDEN\":\"READY\"}}]}";

        when(mockStartTso.start(any(StartTsoInputData.class))).thenReturn(sessionId);
        when(mockSendTso.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockReplyTso.reply(sessionId)).thenReturn(secondResponse);

        IssueTso issueTso = new IssueTso(
                mockConnection,
                account,
                mockStartTso,
                mockStopTso,
                mockSendTso,
                mockReplyTso
        );
        StartTsoInputData inputData = new StartTsoInputData();
        inputData.setAccount("ACCT456");
        List<String> result = issueTso.issueCommand(command, inputData);

        assertEquals(1, result.size());
        assertEquals("JOB STARTED", result.get(0));
        assertEquals(account, issueTso.getInputData().getAccount().orElse(null));

        verify(mockStartTso, times(1)).start(any(StartTsoInputData.class));
        verify(mockSendTso, times(1)).sendCommand(sessionId, command);
        verify(mockReplyTso, atLeastOnce()).reply(sessionId);
        verify(mockStopTso, times(1)).stop(sessionId);
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
        String secondReplyResponse = "{\"tsoData\":[{\"TSO PROMPT\":{\"HIDDEN\":\"READY\"}}]}";

        when(mockStartTso.start(any(StartTsoInputData.class))).thenReturn(sessionId);
        when(mockSendTso.sendCommand(sessionId, command)).thenReturn(firstResponse);
        when(mockReplyTso.reply(sessionId)).thenReturn(firstReplyResponse, secondReplyResponse);

        IssueTso issueTso = new IssueTso(
                mockConnection,
                account,
                mockStartTso,
                mockStopTso,
                mockSendTso,
                mockReplyTso
        );
        List<String> result = issueTso.issueCommand(command);

        assertEquals(List.of("RUNNING", "RUNNING2"), result);
        verify(mockStopTso).stop(sessionId);
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
        when(mockStartTso.start(any())).thenThrow(new ZosmfRequestException("start failed"));

        IssueTso issueTso = new IssueTso(
                mockConnection,
                "ACCTFAIL",
                mockStartTso,
                mockStopTso,
                mockSendTso,
                mockReplyTso
        );

        try {
            issueTso.issueCommand("TIME");
        } catch (ZosmfRequestException e) {
            assertEquals("start failed", e.getMessage());
        }

        verify(mockStopTso, never()).stop(anyString());
    }

    /**
     * Verifies that the constructor throws a NullPointerException when the connection is null.
     */
    @Test
    public void tstIssueTsoConnectionNullFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new IssueTso(null, "1")
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
                () -> new IssueTso(mockConnection, null)
        );
        assertEquals("accountNumber is either null or empty", ex.getMessage());
    }

    /**
     * Verifies that issueCommand throws an IllegalArgumentException when the command string is null.
     */
    @Test
    public void tstIssueCommandNullFailure() {
        IssueTso issueTso = new IssueTso(mockConnection, "ACCT123");
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
                () -> new IssueTso(null, "1", null,
                        null, null, null)
        );
        assertEquals("connection is null", ex.getMessage());
    }

}
