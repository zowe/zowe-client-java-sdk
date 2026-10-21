/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosuss.method;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.zosuss.exception.UssCmdException;
import zowe.client.sdk.zosuss.response.UssCmdResponse;

import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class containing unit tests for UssCmd.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class UssCmdTest {

    private SshConnection mockConnection;

    @BeforeEach
    public void setup() {
        mockConnection = mock(SshConnection.class);
        when(mockConnection.getUser()).thenReturn("testuser");
        when(mockConnection.getHost()).thenReturn("localhost");
        when(mockConnection.getPort()).thenReturn(22);
        when(mockConnection.getPassword()).thenReturn("testpass");
    }

    @Test
    public void tstIssueCommandReturnsOutputSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);

        when(mockChannel.isClosed()).thenReturn(false, true);
        when(mockChannel.isConnected()).thenReturn(true);
        when(mockChannel.getExitStatus()).thenReturn(0);

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 =
                     Mockito.mockConstruction(UssCmd.ManagedSession.class,
                             (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<UssCmd.ManagedChannel> ignored2 =
                     Mockito.mockConstruction(UssCmd.ManagedChannel.class,
                             (mock, context) -> {
                                 final OutputStream stdout =
                                         (OutputStream) context.arguments().get(2);
                                 stdout.write("mock output".getBytes());
                                 when(mock.get()).thenReturn(mockChannel);
                             })) {

            final UssCmd cmd = new UssCmd(mockConnection);
            final String result = cmd.issueCommand("echo test", 1000);

            assertEquals("mock output", result);
        }
    }

    @Test
    public void tstIssueCommandUsesOrderedOutputRedirectionSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final AtomicReference<String> capturedCommand = new AtomicReference<>();

        when(mockChannel.isClosed()).thenReturn(true);
        when(mockChannel.getExitStatus()).thenReturn(0);

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 =
                     Mockito.mockConstruction(UssCmd.ManagedSession.class,
                             (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<UssCmd.ManagedChannel> ignored2 =
                     Mockito.mockConstruction(UssCmd.ManagedChannel.class,
                             (mock, context) -> {
                                 capturedCommand.set(
                                         (String) context.arguments().get(1));
                                 when(mock.get()).thenReturn(mockChannel);
                             })) {

            final UssCmd cmd = new UssCmd(mockConnection);
            cmd.issueCommand("echo test", 1000);

            assertEquals(
                    "{ echo test\n} 2>&1; ",
                    capturedCommand.get()
            );
        }
    }

    @Test
    public void tstIssueCommandTrimsCommandBeforeWrappingSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final AtomicReference<String> capturedCommand = new AtomicReference<>();

        when(mockChannel.isClosed()).thenReturn(true);
        when(mockChannel.getExitStatus()).thenReturn(0);

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 =
                     Mockito.mockConstruction(UssCmd.ManagedSession.class,
                             (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<UssCmd.ManagedChannel> ignored2 =
                     Mockito.mockConstruction(UssCmd.ManagedChannel.class,
                             (mock, context) -> {
                                 capturedCommand.set(
                                         (String) context.arguments().get(1));
                                 when(mock.get()).thenReturn(mockChannel);
                             })) {

            final UssCmd cmd = new UssCmd(mockConnection);
            cmd.issueCommand(
                    "   cd test; touch frank; ls   ",
                    1000
            );

            assertEquals(
                    "{ cd test; touch frank; ls\n} 2>&1; ",
                    capturedCommand.get()
            );
        }
    }

    @Test
    public void tstIssueCommandResponseReturnsStdoutStderrAndExitCodeFailure() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);

        when(mockChannel.isClosed()).thenReturn(false, true);
        when(mockChannel.getExitStatus()).thenReturn(1);

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 =
                     Mockito.mockConstruction(UssCmd.ManagedSession.class,
                             (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<UssCmd.ManagedChannel> ignored2 =
                     Mockito.mockConstruction(UssCmd.ManagedChannel.class,
                             (mock, context) -> {
                                 final OutputStream stdout =
                                         (OutputStream) context.arguments().get(2);
                                 final OutputStream stderr =
                                         (OutputStream) context.arguments().get(3);

                                 stdout.write("standard output".getBytes());
                                 stderr.write("standard error".getBytes());

                                 when(mock.get()).thenReturn(mockChannel);
                             })) {

            final UssCmd cmd = new UssCmd(mockConnection);

            final UssCmdResponse response =
                    cmd.issueCommandResponse("ls /invalid", 1000);

            assertEquals("standard output", response.getStdout());
            assertEquals("standard error", response.getStderr());
            assertEquals(1, response.getExitCode());
            assertFalse(response.isSuccess());
        }
    }

    @Test
    public void tstIssueCommandResponseReturnsSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);

        when(mockChannel.isClosed()).thenReturn(true);
        when(mockChannel.getExitStatus()).thenReturn(0);

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 =
                     Mockito.mockConstruction(UssCmd.ManagedSession.class,
                             (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<UssCmd.ManagedChannel> ignored2 =
                     Mockito.mockConstruction(UssCmd.ManagedChannel.class,
                             (mock, context) -> when(mock.get()).thenReturn(mockChannel))) {

            final UssCmd cmd = new UssCmd(mockConnection);

            final UssCmdResponse response =
                    cmd.issueCommandResponse("echo test", 1000);

            assertEquals("", response.getStdout());
            assertEquals("", response.getStderr());
            assertEquals(0, response.getExitCode());
            assertTrue(response.isSuccess());
        }
    }

    @Test
    public void tstIssueCommandResponseDoesNotModifyCommandSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final AtomicReference<String> capturedCommand = new AtomicReference<>();

        when(mockChannel.isClosed()).thenReturn(true);
        when(mockChannel.getExitStatus()).thenReturn(0);

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 =
                     Mockito.mockConstruction(UssCmd.ManagedSession.class,
                             (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<UssCmd.ManagedChannel> ignored2 =
                     Mockito.mockConstruction(UssCmd.ManagedChannel.class,
                             (mock, context) -> {
                                 capturedCommand.set(
                                         (String) context.arguments().get(1));
                                 when(mock.get()).thenReturn(mockChannel);
                             })) {

            final UssCmd cmd = new UssCmd(mockConnection);

            cmd.issueCommandResponse(
                    "cd test; touch frank; ls",
                    1000
            );

            assertEquals(
                    "cd test; touch frank; ls",
                    capturedCommand.get()
            );
        }
    }

    @Test
    public void tstIssueCommandThrowsExceptionOnZeroTimeoutFailure() {
        final UssCmd cmd = new UssCmd(mockConnection);

        assertThrows(
                IllegalArgumentException.class,
                () -> cmd.issueCommand("echo test", 0)
        );
    }

    @Test
    public void tstIssueCommandThrowsExceptionOnNegativeTimeoutFailure() {
        final UssCmd cmd = new UssCmd(mockConnection);

        assertThrows(
                IllegalArgumentException.class,
                () -> cmd.issueCommand("echo test", -1)
        );
    }

    @Test
    public void tstIssueCommandResponseThrowsExceptionOnZeroTimeoutFailure() {
        final UssCmd cmd = new UssCmd(mockConnection);

        assertThrows(
                IllegalArgumentException.class,
                () -> cmd.issueCommandResponse("echo test", 0)
        );
    }

    @Test
    public void tstIssueCommandResponseThrowsExceptionOnNegativeTimeoutFailure() {
        final UssCmd cmd = new UssCmd(mockConnection);

        assertThrows(
                IllegalArgumentException.class,
                () -> cmd.issueCommandResponse("echo test", -1)
        );
    }

    @Test
    public void tstIssueCommandThrowsExceptionOnJSchErrorFailure() throws Exception {
        final Session mockSession = mock(Session.class);

        when(mockSession.openChannel("exec"))
                .thenThrow(new JSchException("SSH error"));

        try (MockedConstruction<UssCmd.ManagedSession> ignored =
                     Mockito.mockConstruction(
                             UssCmd.ManagedSession.class,
                             (mock, context) ->
                                     when(mock.get()).thenReturn(mockSession))) {

            final UssCmd cmd = new UssCmd(mockConnection);

            final UssCmdException exception = assertThrows(
                    UssCmdException.class,
                    () -> cmd.issueCommand("bad cmd", 1000)
            );

            assertEquals("SSH error", exception.getMessage());
        }
    }

    @Test
    public void tstIssueCommandResponseThrowsExceptionOnJSchErrorFailure() throws Exception {
        final Session mockSession = mock(Session.class);

        when(mockSession.openChannel("exec"))
                .thenThrow(new JSchException("SSH error"));

        try (MockedConstruction<UssCmd.ManagedSession> ignored =
                     Mockito.mockConstruction(
                             UssCmd.ManagedSession.class,
                             (mock, context) ->
                                     when(mock.get()).thenReturn(mockSession))) {

            final UssCmd cmd = new UssCmd(mockConnection);

            final UssCmdException exception = assertThrows(
                    UssCmdException.class,
                    () -> cmd.issueCommandResponse("bad cmd", 1000)
            );

            assertEquals("SSH error", exception.getMessage());
        }
    }

}
