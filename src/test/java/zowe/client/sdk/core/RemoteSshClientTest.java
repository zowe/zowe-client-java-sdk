/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests validating RemoteSshClient execution and lifecycle management.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class RemoteSshClientTest {

    private SshConnection connection;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        connection = new SshConnection("localhost", 22, "testuser", "testpass");
    }

    @Test
    public void tstExecuteRequestStringSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        final String serverReady = "{\"status\":\"ready\"}\n";
        final String rpcResponse = "{\"jsonrpc\":\"2.0\",\"result\":{\"data\":\"OUTPUT\"},\"id\":1}\n";
        final BufferedReader reader = new BufferedReader(new StringReader(serverReady + rpcResponse));

        try (MockedConstruction<RemoteSshClient.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteSshClient.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteSshClient.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteSshClient.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteSshClient client = new RemoteSshClient(connection);
            final JsonNode response = client.executeRequest("{\"test\":1}");

            assertNotNull(response);
            assertEquals("2.0", response.path("jsonrpc").asText());
            assertEquals("OUTPUT", response.path("result").path("data").asText());

            final String sentPayload = capturedOutput.toString(StandardCharsets.UTF_8);
            assertEquals("{\"test\":1}\n", sentPayload);
        }
    }

    @Test
    public void tstExecuteRequestJsonNodeSuccess() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        final String serverReady = "{\"status\":\"ready\"}\n";
        final String rpcResponse = "{\"jsonrpc\":\"2.0\",\"result\":\"OK\"}\n";
        final BufferedReader reader = new BufferedReader(new StringReader(serverReady + rpcResponse));

        try (MockedConstruction<RemoteSshClient.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteSshClient.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteSshClient.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteSshClient.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteSshClient client = new RemoteSshClient(connection, 5000);
            final JsonNode requestNode = mapper.createObjectNode().put("action", "query");
            final JsonNode response = client.executeRequest(requestNode);

            assertNotNull(response);
            assertEquals("OK", response.path("result").asText());

            final String sentPayload = capturedOutput.toString(StandardCharsets.UTF_8);
            assertTrue(sentPayload.contains("\"action\":\"query\""));
        }
    }

    @Test
    public void tstServerClosedBeforeReadyThrowsException() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        final BufferedReader emptyReader = new BufferedReader(new StringReader(""));

        try (MockedConstruction<RemoteSshClient.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteSshClient.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteSshClient.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteSshClient.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(emptyReader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteSshClient client = new RemoteSshClient(connection);
            final IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> client.executeRequest("{}"));
            assertTrue(exception.getMessage().contains("server closed before becoming ready"));
        }
    }

    @Test
    public void tstUnexpectedStartupResponseThrowsException() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        final BufferedReader reader = new BufferedReader(new StringReader("{\"status\":\"error\"}\n"));

        try (MockedConstruction<RemoteSshClient.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteSshClient.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteSshClient.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteSshClient.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteSshClient client = new RemoteSshClient(connection);
            final IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> client.executeRequest("{}"));
            assertTrue(exception.getMessage().contains("Unexpected Zowe Remote SSH startup response"));
        }
    }

    @Test
    public void tstServerClosedBeforeResponseThrowsException() throws Exception {
        final Session mockSession = mock(Session.class);
        final ChannelExec mockChannel = mock(ChannelExec.class);
        final ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        final BufferedReader reader = new BufferedReader(new StringReader("{\"status\":\"ready\"}\n"));

        try (MockedConstruction<RemoteSshClient.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteSshClient.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession));
             MockedConstruction<RemoteSshClient.ManagedRemoteChannel> ignoredChannel = Mockito.mockConstruction(
                     RemoteSshClient.ManagedRemoteChannel.class,
                     (mock, context) -> {
                         when(mock.getReader()).thenReturn(reader);
                         when(mock.getWriter()).thenReturn(capturedOutput);
                         when(mock.get()).thenReturn(mockChannel);
                     })) {

            final RemoteSshClient client = new RemoteSshClient(connection);
            final IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> client.executeRequest("{}"));
            assertTrue(exception.getMessage().contains("server closed before sending response"));
        }
    }

    @Test
    public void tstJSchExceptionThrowsZosmfRequestException() throws Exception {
        final Session mockSession = mock(Session.class);
        when(mockSession.openChannel("exec")).thenThrow(new JSchException("Channel error"));

        try (MockedConstruction<RemoteSshClient.ManagedSession> ignoredSession = Mockito.mockConstruction(
                RemoteSshClient.ManagedSession.class,
                (mock, context) -> when(mock.get()).thenReturn(mockSession))) {

            final RemoteSshClient client = new RemoteSshClient(connection);
            final ZosmfRequestException exception = assertThrows(ZosmfRequestException.class,
                    () -> client.executeRequest("{}"));
            assertTrue(exception.getMessage().contains("Channel error"));
        }
    }

    @Test
    public void tstParameterValidations() {
        assertThrows(IllegalStateException.class, () -> new RemoteSshClient(null));
        final RemoteSshClient client = new RemoteSshClient(connection);
        assertThrows(IllegalArgumentException.class, () -> client.executeRequest((String) null));
        assertThrows(IllegalArgumentException.class, () -> client.executeRequest(""));
        assertThrows(NullPointerException.class, () -> client.executeRequest((JsonNode) null));

        assertEquals(connection, client.getConnection());
        assertEquals(RemoteSshClient.DEFAULT_ZO_SERVER_COMMAND, client.getZoServerCommand());
        assertEquals(RemoteSshClient.DEFAULT_TIMEOUT, client.getTimeout());
    }

}
