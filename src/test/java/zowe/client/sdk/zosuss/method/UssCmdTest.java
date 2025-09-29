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

import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class containing unit tests for UssCmd.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class UssCmdTest {

    private SshConnection mockConnection;

    @BeforeEach
    void setup() {
        mockConnection = mock(SshConnection.class);
        when(mockConnection.getUser()).thenReturn("testuser");
        when(mockConnection.getHost()).thenReturn("localhost");
        when(mockConnection.getPort()).thenReturn(22);
        when(mockConnection.getPassword()).thenReturn("testpass");
    }

    @Test
    public void tstIssueCommandReturnsOutputSuccess() throws Exception {
        // Mock JSch Session and ChannelExec
        Session mockSession = mock(Session.class);
        ChannelExec mockChannel = mock(ChannelExec.class);

        // Simulate channel disconnect after one loop
        when(mockChannel.isConnected()).thenReturn(true, false);
        when(mockSession.openChannel("exec")).thenReturn(mockChannel);

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 = Mockito.mockConstruction(UssCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<UssCmd.ManagedChannel> ignored2 = Mockito.mockConstruction(UssCmd.ManagedChannel.class,
                     (mock, context) -> {
                         // Grab the real OutputStream passed in constructor
                         OutputStream os = (OutputStream) context.arguments().get(2);
                         os.write("mock output".getBytes());
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            UssCmd cmd = new UssCmd(mockConnection);
            String result = cmd.issueCommand("echo test", 1000);

            assertEquals("mock output", result);
        }
    }

    @Test
    public void tstIssueCommandThrowsExceptionOnJSchError() throws Exception {
        Session mockSession = mock(Session.class);
        when(mockSession.openChannel("exec")).thenThrow(new JSchException("SSH error"));

        try (MockedConstruction<UssCmd.ManagedSession> ignored1 = Mockito.mockConstruction(UssCmd.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession))) {

            UssCmd cmd = new UssCmd(mockConnection);

            assertThrows(UssCmdException.class, () -> cmd.issueCommand("bad cmd", 1000));
        }
    }

}
