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

public class SendResponse {

    private Optional<Boolean> success;
    private Optional<List<ZosmfTsoResponse>> zosmfResponse;
    private Optional<String> commandResponse;

    public SendResponse(boolean success, List<ZosmfTsoResponse> zosmfResponse, String commandResponse) {
        this.success = Optional.ofNullable(success);
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
        this.commandResponse = Optional.ofNullable(commandResponse);
    }

    public Optional<Boolean> getSuccess() {
        return success;
    }

    public Optional<List<ZosmfTsoResponse>> getZosmfResponse() {
        return zosmfResponse;
    }

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
