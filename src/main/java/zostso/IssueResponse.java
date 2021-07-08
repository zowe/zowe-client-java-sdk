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

    private Optional<Boolean> success;
    private Optional<StartStopResponses> startResponse;
    private Optional<Boolean> startReady;
    private Optional<StartStopResponse> stopResponses;
    private Optional<ZosmfTsoResponse> zosmfResponse;
    private Optional<String> commandResponses;

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

    public void setSuccess(boolean success) {
        this.success = Optional.ofNullable(success);
    }

    public Optional<Boolean> getSuccess() {
        return success;
    }

    public void setStartResponse(StartStopResponses startResponse) {
        this.startResponse = Optional.ofNullable(startResponse);
    }

    public Optional<StartStopResponses> getStartResponse() {
        return startResponse;
    }

    public void setZosmfResponse(Optional<ZosmfTsoResponse> zosmfResponse) {
        this.zosmfResponse = zosmfResponse;
    }

    public Optional<ZosmfTsoResponse> getZosmfResponse() {
        return zosmfResponse;
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
