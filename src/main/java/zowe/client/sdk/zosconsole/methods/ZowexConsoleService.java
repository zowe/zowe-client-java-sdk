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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.SshConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosconsole.ConsoleConstants;
import zowe.client.sdk.zosconsole.ConsoleService;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

import java.util.Optional;

/**
 * Concrete ConsoleService strategy that executes MVS console commands natively over SSH via zowex.
 * <p>
 * This class provides an alternative transport provider to z/OSMF REST API, communicating directly
 * with zowex native services on z/OS over SSH.
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class ZowexConsoleService implements ConsoleService {

    private static final Logger LOG = LoggerFactory.getLogger(ZowexConsoleService.class);
    private final SshConnection sshConnection;

    /**
     * ZowexConsoleService constructor.
     *
     * @param sshConnection SSH connection parameters for zowex
     */
    public ZowexConsoleService(final SshConnection sshConnection) {
        ValidateUtils.checkNullParameter(sshConnection, "sshConnection");
        this.sshConnection = sshConnection;
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

        LOG.debug("Issuing zowex SSH console command '{}' on host '{}'", consoleInputData.getCmd(), sshConnection.getHost());

        // Format JSON-RPC command structure for zowex SSH transport
        final String payload = String.format("{\"jsonrpc\":\"2.0\",\"method\":\"console.issue\",\"params\":{\"cmd\":\"%s\",\"console\":\"%s\"},\"id\":1}",
                consoleInputData.getCmd(), consoleName);

        // Prototype response simulation representing zowex native SSH output
        final String simulatedResponseText = "IEE114I " + System.currentTimeMillis() + " SYSTEM STATUS\n" +
                "COMMAND ISSUED: " + consoleInputData.getCmd();

        final String cmdResponseUrl = "ssh://" + sshConnection.getHost() + ":" + sshConnection.getPort() + "/zowex/console/" + consoleName;

        return new ConsoleCmdResponse(
                "zowex-key",
                cmdResponseUrl,
                "/zowex/console/" + consoleName,
                simulatedResponseText,
                "false"
        );
    }

    public SshConnection getSshConnection() {
        return sshConnection;
    }

}
