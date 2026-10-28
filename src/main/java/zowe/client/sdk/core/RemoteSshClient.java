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
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ValidateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Generic SSH client for communicating with Zowe Remote SSH native services (zo server)
 * over JSON-RPC.
 *
 * @author Chaitanya Katore
 * @author Frank Giordano
 * @version 7.0
 */
public class RemoteSshClient {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteSshClient.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final String DEFAULT_ZO_SERVER_COMMAND = "~/.zowe-server/zo server --num-workers 1";
    public static final int DEFAULT_TIMEOUT = 10000;

    private final SshConnection connection;
    private final String zoServerCommand;
    private final int timeout;

    /**
     * RemoteSshClient constructor with default zo server command and timeout.
     *
     * @param connection SSH connection parameters
     */
    public RemoteSshClient(final SshConnection connection) {
        this(connection, DEFAULT_ZO_SERVER_COMMAND, DEFAULT_TIMEOUT);
    }

    /**
     * RemoteSshClient constructor with custom timeout.
     *
     * @param connection SSH connection parameters
     * @param timeout    timeout in milliseconds
     */
    public RemoteSshClient(final SshConnection connection, final int timeout) {
        this(connection, DEFAULT_ZO_SERVER_COMMAND, timeout);
    }

    /**
     * RemoteSshClient constructor with custom zo server command and timeout.
     *
     * @param connection      SSH connection parameters
     * @param zoServerCommand path or command to launch the zo server
     * @param timeout         timeout in milliseconds
     */
    public RemoteSshClient(final SshConnection connection, final String zoServerCommand, final int timeout) {
        ValidateUtils.checkSshConnection(connection);
        ValidateUtils.checkIllegalParameter(zoServerCommand, "zoServerCommand");
        this.connection = connection;
        this.zoServerCommand = zoServerCommand;
        this.timeout = timeout <= 0 ? DEFAULT_TIMEOUT : timeout;
    }

    /**
     * Executes a JSON-RPC request string over Zowe Remote SSH and returns the parsed response as a JsonNode.
     *
     * @param payload JSON-RPC payload string
     * @return JsonNode containing parsed response
     * @throws ZosmfRequestException on transport or server failure
     */
    public JsonNode executeRequest(final String payload) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(payload, "payload");

        LOG.debug("Sending Zowe Remote SSH request to host '{}': {}", connection.getHost(), payload);

        try (final ManagedSession session = new ManagedSession(connection, timeout);
             final ManagedRemoteChannel channel = new ManagedRemoteChannel(session.get(), zoServerCommand, timeout)) {

            final BufferedReader reader = channel.getReader();
            final OutputStream writer = channel.getWriter();

            // 1. Wait for and validate zo server ready banner
            final String readyLine = reader.readLine();
            if (readyLine == null) {
                throw new IllegalStateException("Zowe Remote SSH server closed before becoming ready");
            }

            final JsonNode ready = OBJECT_MAPPER.readTree(readyLine);
            if (!"ready".equals(ready.path("status").asText())) {
                throw new IllegalStateException("Unexpected Zowe Remote SSH startup response: " + readyLine);
            }

            // 2. Send JSON-RPC request followed by newline
            writer.write((payload + "\n").getBytes(StandardCharsets.UTF_8));
            writer.flush();

            // 3. Read matching JSON-RPC response
            final String responseLine = reader.readLine();
            if (responseLine == null) {
                throw new IllegalStateException("Zowe Remote SSH server closed before sending response");
            }

            // 4. Return parsed JsonNode response
            return OBJECT_MAPPER.readTree(responseLine);

        } catch (JSchException | IOException e) {
            throw new ZosmfRequestException(e.getMessage(), e);
        }
    }

    /**
     * Executes a JSON-RPC request node over Zowe Remote SSH and returns the parsed response as a JsonNode.
     *
     * @param requestNode JsonNode containing the JSON-RPC request
     * @return JsonNode containing parsed response
     * @throws ZosmfRequestException on transport or serialization failure
     */
    public JsonNode executeRequest(final JsonNode requestNode) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(requestNode, "requestNode");
        try {
            return executeRequest(OBJECT_MAPPER.writeValueAsString(requestNode));
        } catch (IOException e) {
            throw new ZosmfRequestException("Failed to serialize JSON-RPC request: " + e.getMessage(), e);
        }
    }

    public SshConnection getConnection() {
        return connection;
    }

    public String getZoServerCommand() {
        return zoServerCommand;
    }

    public int getTimeout() {
        return timeout;
    }

    /**
     * AutoCloseable wrapper for JSch Session
     */
    static class ManagedSession implements AutoCloseable {
        private final Session session;

        ManagedSession(final SshConnection connection, final int timeout) throws JSchException {
            final JSch jsch = new JSch();
            this.session = jsch.getSession(connection.getUser(), connection.getHost(), connection.getPort());
            if (connection.getPassword() != null && !connection.getPassword().isEmpty()) {
                session.setPassword(connection.getPassword());
            }
            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password,keyboard-interactive");
            session.setConfig(config);
            session.connect(timeout);
        }

        Session get() {
            return session;
        }

        @Override
        public void close() {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
        }
    }

    /**
     * AutoCloseable wrapper for JSch ChannelExec running zo server process
     */
    static class ManagedRemoteChannel implements AutoCloseable {
        private final ChannelExec channel;
        private final BufferedReader reader;
        private final OutputStream writer;

        ManagedRemoteChannel(final Session session, final String command, final int timeout)
                throws JSchException, IOException {
            this.channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            this.reader = new BufferedReader(new InputStreamReader(channel.getInputStream(), StandardCharsets.UTF_8));
            this.writer = channel.getOutputStream();
            channel.connect(timeout);
        }

        BufferedReader getReader() {
            return reader;
        }

        OutputStream getWriter() {
            return writer;
        }

        ChannelExec get() {
            return channel;
        }

        @Override
        public void close() {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {
            }
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ignored) {
            }
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

}
