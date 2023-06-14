/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.response;

import java.util.Optional;

/**
 * The Console API response.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ConsoleResponse {

    /**
     * True if the command was issued and the responses were collected.
     */
    private Optional<Boolean> success = Optional.empty();

    /**
     * The list of zOSMF console API responses. May issue multiple requests (because of user request) or
     * to ensure that all messages are collected. Each individual response is placed here.
     */
    private Optional<ZosmfIssueResponse> zosmfResponse = Optional.empty();

    /**
     * If an error occurs, returns the ImperativeError, which contains case error.
     */
    private Optional<String> failureResponse = Optional.empty();

    /**
     * The command response text.
     */
    private Optional<String> commandResponse = Optional.empty();

    /**
     * The final command response key - used to "follow-up" and check for additional response messages for the command.
     */
    private Optional<String> lastResponseKey = Optional.empty();

    /**
     * If the solicited keyword is specified, indicates that the keyword was detected.
     */
    private Optional<Boolean> keywordDetected = Optional.empty();

    /**
     * The "follow-up" command response URL - you can paste this in the browser to do a "GET" using the command
     * response key provided in the URI route.
     */
    private Optional<String> cmdResponseUrl = Optional.empty();

    /**
     * Retrieve cmdResponseUrl specified
     *
     * @return cmdResponseUrl value
     * @author Frank Giordano
     */
    public Optional<String> getCmdResponseUrl() {
        return cmdResponseUrl;
    }

    /**
     * Assign cmdResponseUrl value
     *
     * @param cmdResponseUrl value
     * @author Frank Giordano
     */
    public void setCmdResponseUrl(String cmdResponseUrl) {
        this.cmdResponseUrl = Optional.ofNullable(cmdResponseUrl);
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

    /**
     * Assign commandResponse value
     *
     * @param commandResponse value
     * @author Frank Giordano
     */
    public void setCommandResponse(String commandResponse) {
        this.commandResponse = Optional.ofNullable(commandResponse);
    }

    /**
     * Retrieve failureResponse specified
     *
     * @return failureResponse value
     * @author Frank Giordano
     */
    public Optional<String> getFailureResponse() {
        return failureResponse;
    }

    /**
     * Assign failureResponse value
     *
     * @param failureResponse value
     * @author Frank Giordano
     */
    public void setFailureResponse(String failureResponse) {
        this.failureResponse = Optional.ofNullable(failureResponse);
    }

    /**
     * Retrieve keywordDetected specified
     *
     * @return keywordDetected true or false is keywordDetected seen
     * @author Frank Giordano
     */
    public Optional<Boolean> getKeywordDetected() {
        return keywordDetected;
    }

    /**
     * Assign keywordDetected value
     *
     * @param keywordDetected value
     * @author Frank Giordano
     */
    public void setKeywordDetected(Boolean keywordDetected) {
        this.keywordDetected = Optional.ofNullable(keywordDetected);
    }

    /**
     * Retrieve lastResponseKey specified
     *
     * @return lastResponseKey value
     * @author Frank Giordano
     */
    public Optional<String> getLastResponseKey() {
        return lastResponseKey;
    }

    /**
     * Assign lastResponseKey value
     *
     * @param lastResponseKey value
     * @author Frank Giordano
     */
    public void setLastResponseKey(String lastResponseKey) {
        this.lastResponseKey = Optional.ofNullable(lastResponseKey);
    }

    /**
     * Retrieve success specified
     *
     * @return boolean value
     * @author Frank Giordano
     */
    public Optional<Boolean> getSuccess() {
        return success;
    }

    /**
     * Assign success value
     *
     * @param success true or false is response seen
     * @author Frank Giordano
     */
    public void setSuccess(Boolean success) {
        this.success = Optional.ofNullable(success);
    }

    /**
     * Retrieve zosmfResponse specified
     *
     * @return zosmfResponse value
     * @author Frank Giordano
     */
    public Optional<ZosmfIssueResponse> getZosmfResponse() {
        return zosmfResponse;
    }

    /**
     * Assign zosmfResponse value
     *
     * @param zosmfResponse value
     * @author Frank Giordano
     */
    public void setZosmfResponse(ZosmfIssueResponse zosmfResponse) {
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
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
