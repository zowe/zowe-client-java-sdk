/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.methods;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosconsole.ConsoleCmdProvider;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests validating RemoteConsoleCmd and ConsoleCmdProvider implementations.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class RemoteConsoleCmdTest {

    private SshConnection connection;

    @BeforeEach
    public void setup() {
        connection = new SshConnection("localhost", 22, "testuser", "testpass");
    }

    @Test
    public void tstConsoleCmdProviderPolymorphicCompatibility() {
        final ZosConnection zosConnection = ZosConnectionFactory
                .createBasicConnection("localhost", 443, "user", "pass");
        final ConsoleCmdProvider restProvider = new ConsoleCmd(zosConnection);
        final ConsoleCmdProvider sshProvider = new RemoteConsoleCmd(connection);

        assertNotNull(restProvider);
        assertNotNull(sshProvider);
        assertTrue(restProvider instanceof ConsoleCmd);
        assertTrue(sshProvider instanceof RemoteConsoleCmd);
    }

    @Test
    public void tstIssueCommandSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        final String serverReady = "{\"status\":\"ready\"}\n";
        final String rpcResponse = "{\"jsonrpc\":\"2.0\",\"result\":{\"data\":\"IEE114I 11.22.33 SYSTEM STATUS\"},\"id\":1}\n";
        final BufferedReader reader = new BufferedReader(new StringReader(serverReady + rpcResponse));

        try (MockedConstruction<RemoteConsoleCmd.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteConsoleCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteConsoleCmd.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteConsoleCmd.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(connection);
            final ConsoleCmdResponse response = remoteConsoleCmd.issueCommand("D IPLINFO");

            assertNotNull(response);
            assertEquals("IEE114I 11.22.33 SYSTEM STATUS", response.getCmdResponse());

            final String sentPayload = capturedOutput.toString(StandardCharsets.UTF_8);
            assertTrue(sentPayload.contains("\"method\":\"consoleCommand\""));
            assertTrue(sentPayload.contains("\"commandText\":\"D IPLINFO\""));
            assertTrue(sentPayload.contains("\"consoleName\":\"defcn\""));
        }
    }

    @Test
    public void tstIssueCommandWithSpecificConsoleNameSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        final String serverReady = "{\"status\":\"ready\"}\n";
        final String rpcResponse = "{\"jsonrpc\":\"2.0\",\"result\":{\"data\":\"IEE114I SYSTEM STATUS ON MYCON\"},\"id\":1}\n";
        final BufferedReader reader = new BufferedReader(new StringReader(serverReady + rpcResponse));

        try (MockedConstruction<RemoteConsoleCmd.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteConsoleCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteConsoleCmd.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteConsoleCmd.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(connection);
            final ConsoleCmdResponse response = remoteConsoleCmd.issueCommand("D A,L", "MYCON");

            assertNotNull(response);
            assertEquals("IEE114I SYSTEM STATUS ON MYCON", response.getCmdResponse());

            final String sentPayload = capturedOutput.toString(StandardCharsets.UTF_8);
            assertTrue(sentPayload.contains("\"consoleName\":\"MYCON\""));
            assertTrue(sentPayload.contains("\"commandText\":\"D A,L\""));
        }
    }

    @Test
    public void tstIssueCommandServerClosedBeforeReadyThrowsException() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        final BufferedReader emptyReader = new BufferedReader(new StringReader(""));

        try (MockedConstruction<RemoteConsoleCmd.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteConsoleCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteConsoleCmd.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteConsoleCmd.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(emptyReader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(connection);
            final IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> remoteConsoleCmd.issueCommand("D IPLINFO"));
            assertTrue(exception.getMessage().contains("server closed before becoming ready"));
        }
    }

    @Test
    public void tstIssueCommandServerUnexpectedStartupResponseThrowsException() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        final BufferedReader reader = new BufferedReader(new StringReader("{\"status\":\"error\"}\n"));

        try (MockedConstruction<RemoteConsoleCmd.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteConsoleCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteConsoleCmd.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteConsoleCmd.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(connection);
            final IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> remoteConsoleCmd.issueCommand("D IPLINFO"));
            assertTrue(exception.getMessage().contains("Unexpected Zowe Remote SSH startup response"));
        }
    }

    @Test
    public void tstIssueCommandRpcErrorResponseThrowsException() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        final String serverReady = "{\"status\":\"ready\"}\n";
        final String rpcResponse = "{\"jsonrpc\":\"2.0\",\"error\":{\"message\":\"command not recognized\"},\"id\":1}\n";
        final BufferedReader reader = new BufferedReader(new StringReader(serverReady + rpcResponse));

        try (MockedConstruction<RemoteConsoleCmd.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteConsoleCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteConsoleCmd.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteConsoleCmd.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(connection);
            final ZosmfRequestException exception = assertThrows(ZosmfRequestException.class,
                    () -> remoteConsoleCmd.issueCommand("BAD COMMAND"));
            assertTrue(exception.getMessage().contains("command not recognized"));
        }
    }

    @Test
    public void tstIssueCommandJSchExceptionThrowsZosmfRequestException() throws Exception {
        final Session mockSession = mock(Session.class);
        when(mockSession.openChannel("exec")).thenThrow(new JSchException("Connection refused"));

        try (MockedConstruction<RemoteConsoleCmd.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteConsoleCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession))) {

            final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(connection);
            final ZosmfRequestException exception = assertThrows(ZosmfRequestException.class,
                    () -> remoteConsoleCmd.issueCommand("D IPLINFO"));
            assertTrue(exception.getMessage().contains("Connection refused"));
        }
    }

    @Test
    public void tstParameterValidations() {
        assertThrows(IllegalStateException.class, () -> new RemoteConsoleCmd(null));
        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(connection);
        assertThrows(IllegalArgumentException.class, () -> remoteConsoleCmd.issueCommand(null));
        assertThrows(IllegalArgumentException.class, () -> remoteConsoleCmd.issueCommand(""));
        assertThrows(IllegalArgumentException.class, () -> remoteConsoleCmd.issueCommand("cmd", null));
        assertThrows(NullPointerException.class, () -> remoteConsoleCmd.issueCommandCommon("defcn", (ConsoleCmdInputData) null));
    }

}
