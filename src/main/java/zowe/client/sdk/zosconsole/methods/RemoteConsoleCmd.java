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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.RemoteSshClient;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ConsoleUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleCmdProvider;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

/**
 * Concrete ConsoleCmdProvider implementation that executes MVS console commands natively
 * over SSH via Zowe Remote SSH (zo server), delegating transport to RemoteSshClient.
 *
 * @author Chaitanya Katore
 * @author Frank Giordano
 * @version 7.0
 */
public class RemoteConsoleCmd implements ConsoleCmdProvider {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteConsoleCmd.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final RemoteSshClient remoteSshClient;

    /**
     * RemoteConsoleCmd constructor with SshConnection.
     *
     * @param connection SSH connection parameters
     */
    public RemoteConsoleCmd(final SshConnection connection) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.remoteSshClient = new RemoteSshClient(connection);
    }

    /**
     * RemoteConsoleCmd constructor with SshConnection and timeout.
     *
     * @param connection SSH connection parameters
     * @param timeout    timeout in milliseconds
     */
    public RemoteConsoleCmd(final SshConnection connection, final int timeout) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.remoteSshClient = new RemoteSshClient(connection, timeout);
    }

    /**
     * RemoteConsoleCmd constructor with SshConnection, custom zo server command, and timeout.
     *
     * @param connection      SSH connection parameters
     * @param zoServerCommand path or command to launch the zo server
     * @param timeout         timeout in milliseconds
     */
    public RemoteConsoleCmd(final SshConnection connection, final String zoServerCommand, final int timeout) {
        ValidateUtils.checkNullParameter(connection, "connection");
        this.remoteSshClient = new RemoteSshClient(connection, zoServerCommand, timeout);
    }

    /**
     * RemoteConsoleCmd constructor with RemoteSshClient.
     *
     * @param remoteSshClient RemoteSshClient instance
     */
    public RemoteConsoleCmd(final RemoteSshClient remoteSshClient) {
        ValidateUtils.checkNullParameter(remoteSshClient, "remoteSshClient");
        this.remoteSshClient = remoteSshClient;
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

        LOG.debug("Issuing Zowe Remote SSH console command '{}'", consoleInputData.getCmd());

        final ObjectNode requestNode = OBJECT_MAPPER.createObjectNode();
        requestNode.put("jsonrpc", "2.0");
        requestNode.put("method", "consoleCommand");
        final ObjectNode paramsNode = requestNode.putObject("params");
        paramsNode.put("commandText", consoleInputData.getCmd());
        if (!consoleName.isBlank()) {
            paramsNode.put("consoleName", consoleName);
        }
        requestNode.put("id", 1);

        final JsonNode responseJson = remoteSshClient.executeRequest(requestNode);

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
            cmdResponse = responseJson.toString();
        }

        ConsoleCmdResponse response = new ConsoleCmdResponse(null, null, null, cmdResponse, null);
        if (consoleInputData.isProcessResponse()) {
            response = response.withCmdResponse(ConsoleUtils.processCmdResponse(response.getCmdResponse()));
        }
        return response;
    }

    public RemoteSshClient getRemoteSshClient() {
        return remoteSshClient;
    }

}
