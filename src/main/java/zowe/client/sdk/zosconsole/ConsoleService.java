/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole;

import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosconsole.input.ConsoleCmdInputData;
import zowe.client.sdk.zosconsole.response.ConsoleCmdResponse;

/**
 * Strategy interface defining z/OS Console operations independent of transport protocol
 * (e.g., z/OSMF REST API or zowex SSH native services).
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public interface ConsoleService {

    /**
     * Issue an MVS console command on the default console.
     *
     * @param command string value representing command to issue
     * @return ConsoleCmdResponse object
     * @throws ZosmfRequestException request error state
     */
    ConsoleCmdResponse issueCommand(final String command) throws ZosmfRequestException;

    /**
     * Issue an MVS console command on a specific console name.
     *
     * @param command     string value representing console command to issue
     * @param consoleName name of the console that is used to issue the command
     * @return ConsoleCmdResponse object
     * @throws ZosmfRequestException request error state
     */
    ConsoleCmdResponse issueCommand(final String command, final String consoleName) throws ZosmfRequestException;

    /**
     * Issue an MVS console command driven by ConsoleCmdInputData settings.
     *
     * @param consoleName      name of the console that is used to issue the command
     * @param consoleInputData ConsoleCmdInputData options
     * @return ConsoleCmdResponse object
     * @throws ZosmfRequestException request error state
     */
    ConsoleCmdResponse issueCommandCommon(final String consoleName, final ConsoleCmdInputData consoleInputData)
            throws ZosmfRequestException;

}
