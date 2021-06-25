/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosconsole;

import zosconsole.zosmf.ZosmfIssueResponse;

import java.util.Optional;

public class ConsoleResponse {

    private Optional<Boolean> success = Optional.empty();
    private Optional<ZosmfIssueResponse> zosmfResponse = Optional.empty();
    private Optional<String> failureResponse = Optional.empty();
    private Optional<String> commandResponse = Optional.empty();
    private Optional<String> lastResponseKey = Optional.empty();
    private Optional<Boolean> keywordDetected = Optional.empty();
    private Optional<String> cmdResponseUrl = Optional.empty();

    public Optional<Boolean> getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = Optional.ofNullable(success);
    }

    public Optional<ZosmfIssueResponse> getZosmfResponse() {
        return zosmfResponse;
    }

    public void setZosmfResponse(ZosmfIssueResponse zosmfResponse) {
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
    }

    public Optional<String> getFailureResponse() {
        return failureResponse;
    }

    public void setFailureResponse(String failureResponse) {
        this.failureResponse = Optional.ofNullable(failureResponse);
    }

    public Optional<String> getCommandResponse() {
        return commandResponse;
    }

    public void setCommandResponse(String commandResponse) {
        this.commandResponse = Optional.ofNullable(commandResponse);
    }

    public Optional<String> getLastResponseKey() {
        return lastResponseKey;
    }

    public void setLastResponseKey(String lastResponseKey) {
        this.lastResponseKey = Optional.ofNullable(lastResponseKey);
    }

    public Optional<Boolean> getKeywordDetected() {
        return keywordDetected;
    }

    public void setKeywordDetected(Boolean keywordDetected) {
        this.keywordDetected = Optional.ofNullable(keywordDetected);
    }

    public Optional<String> getCmdResponseUrl() {
        return cmdResponseUrl;
    }

    public void setCmdResponseUrl(String cmdResponseUrl) {
        this.cmdResponseUrl = Optional.ofNullable(cmdResponseUrl);
    }

    @Override
    public String toString() {
        return "ConsoleResponse{" +
                "success=" + success +
                ", zosmfResponse=" + zosmfResponse +
                ", failureResponse=" + failureResponse +
                ", commandResponse=" + commandResponse +
                ", lastResponseKey=" + lastResponseKey +
                ", keywordDetected=" + keywordDetected +
                ", cmdResponseUrl=" + cmdResponseUrl +
                '}';
    }

}
