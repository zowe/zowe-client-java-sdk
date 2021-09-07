/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso;

import zostso.zosmf.ZosmfTsoResponse;

import java.util.List;
import java.util.Optional;

/**
 * The TsoSend API response
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SendResponse {

    /**
     * True if the command was issued and the responses were collected.
     */
    private final Optional<Boolean> success;

    /**
     * The list of zOSMF send API responses. May issue multiple requests or
     * to ensure that all messages are collected. Each individual response is placed here.
     */
    private final Optional<List<ZosmfTsoResponse>> zosmfResponse;

    /**
     * The command response text.
     */
    private final Optional<String> commandResponse;

    /**
     * SendResponse constructor
     *
     * @param success         boolean value
     * @param zosmfResponse   list of ZosmfTsoResponse objects
     * @param commandResponse tso command response
     * @author Frank Giordano
     */
    public SendResponse(boolean success, List<ZosmfTsoResponse> zosmfResponse, String commandResponse) {
        this.success = Optional.ofNullable(success);
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
        this.commandResponse = Optional.ofNullable(commandResponse);
    }

    /**
     * Retrieve success specified
     *
     * @return success value
     * @author Frank Giordano
     */
    public Optional<Boolean> getSuccess() {
        return success;
    }

    /**
     * Retrieve zosmfResponse specified
     *
     * @return zosmfResponse value
     * @author Frank Giordano
     */
    public Optional<List<ZosmfTsoResponse>> getZosmfResponse() {
        return zosmfResponse;
    }

    /**
     * Retrieve commandResponse specified
     *
     * @return commandResponse value
     * @author Frank Giordano
     */
    public Optional<String> getCommandResponse() {
        return commandResponse;
    }

    @Override
    public String toString() {
        return "SendResponse{" +
                "success=" + success +
                ", zosmfResponse=" + zosmfResponse +
                ", commandResponse=" + commandResponse +
                '}';
    }

}
