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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import zowe.client.sdk.core.RemoteSshClient;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosconsole.ConsoleCmdProvider;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests validating RemoteConsoleCmd and ConsoleCmdProvider implementations.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class RemoteConsoleCmdTest {

    private RemoteSshClient mockSshClient;
    private final ObjectMapper mapper = new ObjectMapper();
    private SshConnection connection;

    @BeforeEach
    public void setup() {
        mockSshClient = mock(RemoteSshClient.class);
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
        final JsonNode rpcResponse = mapper.readTree(
                "{\"jsonrpc\":\"2.0\",\"result\":{\"data\":\"IEE114I 11.22.33 SYSTEM STATUS\"},\"id\":1}"
        );
        when(mockSshClient.executeRequest(any(JsonNode.class))).thenReturn(rpcResponse);

        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(mockSshClient);
        final ConsoleCmdResponse response = remoteConsoleCmd.issueCommand("D IPLINFO");

        assertNotNull(response);
        assertEquals("IEE114I 11.22.33 SYSTEM STATUS", response.getCmdResponse());

        final ArgumentCaptor<JsonNode> captor = ArgumentCaptor.forClass(JsonNode.class);
        verify(mockSshClient).executeRequest(captor.capture());
        final JsonNode sentRequest = captor.getValue();

        assertEquals("2.0", sentRequest.path("jsonrpc").asText());
        assertEquals("consoleCommand", sentRequest.path("method").asText());
        assertEquals("D IPLINFO", sentRequest.path("params").path("commandText").asText());
        assertEquals("defcn", sentRequest.path("params").path("consoleName").asText());
    }

    @Test
    public void tstIssueCommandWithSpecificConsoleNameSuccess() throws Exception {
        final JsonNode rpcResponse = mapper.readTree(
                "{\"jsonrpc\":\"2.0\",\"result\":{\"data\":\"IEE114I SYSTEM STATUS ON MYCON\"},\"id\":1}"
        );
        when(mockSshClient.executeRequest(any(JsonNode.class))).thenReturn(rpcResponse);

        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(mockSshClient);
        final ConsoleCmdResponse response = remoteConsoleCmd.issueCommand("D A,L", "MYCON");

        assertNotNull(response);
        assertEquals("IEE114I SYSTEM STATUS ON MYCON", response.getCmdResponse());

        final ArgumentCaptor<JsonNode> captor = ArgumentCaptor.forClass(JsonNode.class);
        verify(mockSshClient).executeRequest(captor.capture());
        final JsonNode sentRequest = captor.getValue();

        assertEquals("D A,L", sentRequest.path("params").path("commandText").asText());
        assertEquals("MYCON", sentRequest.path("params").path("consoleName").asText());
    }

    @Test
    public void tstIssueCommandWithProcessResponseTrue() throws Exception {
        final JsonNode rpcResponse = mapper.readTree(
                "{\"jsonrpc\":\"2.0\",\"result\":{\"data\":\"LINE1\\rLINE2\"},\"id\":1}"
        );
        when(mockSshClient.executeRequest(any(JsonNode.class))).thenReturn(rpcResponse);

        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(mockSshClient);
        final ConsoleCmdInputData inputData = new ConsoleCmdInputData("D A,L");
        inputData.setProcessResponse();

        final ConsoleCmdResponse response = remoteConsoleCmd.issueCommandCommon("defcn", inputData);

        assertNotNull(response);
        assertEquals("LINE1\nLINE2\n", response.getCmdResponse());
    }

    @Test
    public void tstIssueCommandResultAsDirectStringSuccess() throws Exception {
        final JsonNode rpcResponse = mapper.readTree(
                "{\"jsonrpc\":\"2.0\",\"result\":\"DIRECT TEXT\",\"id\":1}"
        );
        when(mockSshClient.executeRequest(any(JsonNode.class))).thenReturn(rpcResponse);

        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(mockSshClient);
        final ConsoleCmdResponse response = remoteConsoleCmd.issueCommand("D IPLINFO");

        assertEquals("DIRECT TEXT", response.getCmdResponse());
    }

    @Test
    public void tstIssueCommandRootDataSuccess() throws Exception {
        final JsonNode rpcResponse = mapper.readTree(
                "{\"jsonrpc\":\"2.0\",\"data\":\"ROOT DATA\",\"id\":1}"
        );
        when(mockSshClient.executeRequest(any(JsonNode.class))).thenReturn(rpcResponse);

        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(mockSshClient);
        final ConsoleCmdResponse response = remoteConsoleCmd.issueCommand("D IPLINFO");

        assertEquals("ROOT DATA", response.getCmdResponse());
    }

    @Test
    public void tstIssueCommandRpcErrorResponseThrowsException() throws Exception {
        final JsonNode rpcResponse = mapper.readTree(
                "{\"jsonrpc\":\"2.0\",\"error\":{\"message\":\"command not recognized\"},\"id\":1}"
        );
        when(mockSshClient.executeRequest(any(JsonNode.class))).thenReturn(rpcResponse);

        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(mockSshClient);
        final ZosmfRequestException exception = assertThrows(ZosmfRequestException.class,
                () -> remoteConsoleCmd.issueCommand("BAD COMMAND"));
        assertTrue(exception.getMessage().contains("command not recognized"));
    }

    @Test
    public void tstConstructorsAndParameterValidations() {
        assertThrows(NullPointerException.class, () -> new RemoteConsoleCmd((SshConnection) null));
        assertThrows(NullPointerException.class, () -> new RemoteConsoleCmd((RemoteSshClient) null));
        assertThrows(NullPointerException.class, () -> new RemoteConsoleCmd((SshConnection) null, 5000));
        assertThrows(NullPointerException.class, () -> new RemoteConsoleCmd((SshConnection) null, "cmd", 5000));

        final RemoteConsoleCmd fromConnection = new RemoteConsoleCmd(connection);
        assertNotNull(fromConnection.getRemoteSshClient());

        final RemoteConsoleCmd fromConnectionWithTimeout = new RemoteConsoleCmd(connection, 8000);
        assertEquals(8000, fromConnectionWithTimeout.getRemoteSshClient().getTimeout());

        final RemoteConsoleCmd fromConnectionCustom = new RemoteConsoleCmd(connection, "custom zo", 8000);
        assertEquals("custom zo", fromConnectionCustom.getRemoteSshClient().getZoServerCommand());

        final RemoteConsoleCmd remoteConsoleCmd = new RemoteConsoleCmd(mockSshClient);
        assertThrows(IllegalArgumentException.class, () -> remoteConsoleCmd.issueCommand(null));
        assertThrows(IllegalArgumentException.class, () -> remoteConsoleCmd.issueCommand(""));
        assertThrows(IllegalArgumentException.class, () -> remoteConsoleCmd.issueCommand("cmd", null));
        assertThrows(NullPointerException.class, () -> remoteConsoleCmd.issueCommandCommon("defcn", null));
    }

}
