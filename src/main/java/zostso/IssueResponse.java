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

import java.util.Optional;

public class IssueResponse {

    public Optional<Boolean> success;
    public Optional<StartStopResponses> startResponse;
    public Optional<Boolean> startReady;
    public Optional<StartStopResponse> stopResponses;
    public Optional<ZosmfTsoResponse> zosmfResponse;
    public Optional<String> commandResponses;

    public IssueResponse(boolean success, StartStopResponses startResponse, boolean startReady,
                         StartStopResponse stopResponses, ZosmfTsoResponse zosmfResponse,
                         String commandResponses) {
        this.success = Optional.ofNullable(success);
        this.startResponse = Optional.ofNullable(startResponse);
        this.startReady = Optional.ofNullable(startReady);
        this.stopResponses = Optional.ofNullable(stopResponses);
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
        this.commandResponses = Optional.ofNullable(commandResponses);
    }

    public void setStartResponse(StartStopResponses startResponse) {
        this.startResponse = Optional.ofNullable(startResponse);
    }

    @Override
    public String toString() {
        return "IssueResponse{" +
                "success=" + success +
                ", startResponse=" + startResponse +
                ", startReady=" + startReady +
                ", stopResponses=" + stopResponses +
                ", zosmfResponse=" + zosmfResponse +
                ", commandResponses=" + commandResponses +
                '}';
    }

}
