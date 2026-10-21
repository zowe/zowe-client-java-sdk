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
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ConsoleUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleCmdProvider;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Concrete ConsoleCmdProvider implementation that executes MVS console commands natively
 * over SSH via Zowe Remote SSH (zo server).
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class RemoteConsoleCmd implements ConsoleCmdProvider {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteConsoleCmd.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static final String DEFAULT_ZO_SERVER_COMMAND = "~/.zowe-server/zo server --num-workers 1";
    public static final int DEFAULT_TIMEOUT = 10000;

    private final SshConnection connection;
    private final String zoServerCommand;
    private final int timeout;

    /**
     * RemoteConsoleCmd constructor with default zo server command and timeout.
     *
     * @param connection SSH connection parameters
     */
    public RemoteConsoleCmd(final SshConnection connection) {
        this(connection, DEFAULT_ZO_SERVER_COMMAND, DEFAULT_TIMEOUT);
    }

    /**
     * RemoteConsoleCmd constructor with custom timeout.
     *
     * @param connection SSH connection parameters
     * @param timeout    timeout in milliseconds
     */
    public RemoteConsoleCmd(final SshConnection connection, final int timeout) {
        this(connection, DEFAULT_ZO_SERVER_COMMAND, timeout);
    }

    /**
     * RemoteConsoleCmd constructor with custom zo server command and timeout.
     *
     * @param connection      SSH connection parameters
     * @param zoServerCommand path/command to launch the zo server
     * @param timeout         timeout in milliseconds
     */
    public RemoteConsoleCmd(final SshConnection connection, final String zoServerCommand, final int timeout) {
        ValidateUtils.checkSshConnection(connection);
        ValidateUtils.checkIllegalParameter(zoServerCommand, "zoServerCommand");
        this.connection = connection;
        this.zoServerCommand = zoServerCommand;
        this.timeout = timeout <= 0 ? DEFAULT_TIMEOUT : timeout;
    }

    @Override
    public ConsoleCmdResponse issueCommand(final String command) throws ZosmfRequestException {
        return issueCommandCommon(ConsoleConstants.RES_DEF_CN, new ConsoleCmdInputData(command));
    }

    @Override
    public ConsoleCmdResponse issueCommand(final String command, final String consoleName) throws ZosmfRequestException {
        return issueCommandCommon(consoleName, new ConsoleCmdInputData(command));
    }

    @Override
    public ConsoleCmdResponse issueCommandCommon(final String consoleName, final ConsoleCmdInputData consoleInputData)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(consoleName, "consoleName");
        ValidateUtils.checkNullParameter(consoleInputData, "consoleInputData");

        LOG.debug("Issuing Zowe Remote SSH console command '{}' on host '{}'", consoleInputData.getCmd(), connection.getHost());

        final String payload;
        try {
            final ObjectNode requestNode = OBJECT_MAPPER.createObjectNode();
            requestNode.put("jsonrpc", "2.0");
            requestNode.put("method", "consoleCommand");
            final ObjectNode paramsNode = requestNode.putObject("params");
            paramsNode.put("commandText", consoleInputData.getCmd());
            if (!consoleName.isBlank()) {
                paramsNode.put("consoleName", consoleName);
            }
            requestNode.put("id", 1);
            payload = OBJECT_MAPPER.writeValueAsString(requestNode);
        } catch (IOException e) {
            throw new ZosmfRequestException("Failed to serialize JSON-RPC request: " + e.getMessage(), e);
        }

        try (final ManagedSession session = new ManagedSession(connection, timeout);
             final ManagedRemoteChannel channel = new ManagedRemoteChannel(session.get(), zoServerCommand, timeout)) {

            final BufferedReader reader = channel.getReader();
            final OutputStream writer = channel.getWriter();

            // 1. Wait for zo server ready banner
            final String readyLine = reader.readLine();
            if (readyLine == null) {
                throw new IllegalStateException("Zowe Remote SSH server closed before becoming ready");
            }

            final JsonNode ready = OBJECT_MAPPER.readTree(readyLine);
            if (!"ready".equals(ready.path("status").asText())) {
                throw new IllegalStateException("Unexpected Zowe Remote SSH startup response: " + readyLine);
            }

            // 2. Send JSON-RPC request
            writer.write((payload + "\n").getBytes(StandardCharsets.UTF_8));
            writer.flush();

            // 3. Read matching response
            final String responseLine = reader.readLine();
            if (responseLine == null) {
                throw new IllegalStateException("Zowe Remote SSH server closed before sending response");
            }

            // 4. Parse result.data
            final JsonNode responseJson = OBJECT_MAPPER.readTree(responseLine);
            if (responseJson.has("error") && !responseJson.get("error").isNull()) {
                final JsonNode errorNode = responseJson.get("error");
                final String errorMessage = errorNode.has("message")
                        ? errorNode.get("message").asText()
                        : errorNode.toString();
                throw new ZosmfRequestException("Zowe Remote SSH error: " + errorMessage);
            }

            final JsonNode resultNode = responseJson.path("result");
            final String cmdResponse;
            if (resultNode.has("data")) {
                cmdResponse = resultNode.path("data").asText();
            } else if (!resultNode.isMissingNode() && !resultNode.isNull()) {
                cmdResponse = resultNode.asText();
            } else if (responseJson.has("data")) {
                cmdResponse = responseJson.path("data").asText();
            } else {
                cmdResponse = responseLine;
            }

            ConsoleCmdResponse response = new ConsoleCmdResponse(null, null, null, cmdResponse, null);
            if (consoleInputData.isProcessResponse()) {
                response = response.withCmdResponse(ConsoleUtils.processCmdResponse(response.getCmdResponse()));
            }
            return response;

        } catch (JSchException | IOException e) {
            throw new ZosmfRequestException(e.getMessage(), e);
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
            if (connection.getPrivateKeyPath().isPresent()) {
                jsch.addIdentity(connection.getPrivateKeyPath().get());
            }
            this.session = jsch.getSession(connection.getUser(), connection.getHost(), connection.getPort());
            if (connection.getPassword() != null && !connection.getPassword().isEmpty()) {
                session.setPassword(connection.getPassword());
            }
            final Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            config.put("PreferredAuthentications", "password,publickey,keyboard-interactive");
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
