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
 * @version 3.0
 */
public class ConsoleResponse {

    /**
     * True if the command was issued and the responses were collected.
     */
    private boolean success = false;

    /**
     * The list of zOSMF console API responses. May issue multiple requests (because of a user request) or
     * to ensure that all messages are collected. Each response is placed here.
     */
    private Optional<ZosmfIssueResponse> zosmfResponse = Optional.empty();

    /**
     * If an error occurs, returns the ImperativeError, which contains a case error.
     */
    private Optional<String> failureResponse = Optional.empty();

    /**
     * The command response text.
     */
    private Optional<String> commandResponse = Optional.empty();

    /**
     * The final command response key - used to "follow up" and check for additional response messages for the command.
     */
    private Optional<String> lastResponseKey = Optional.empty();

    /**
     * If the solicited keyword is specified, indicates that the keyword was detected.
     */
    private boolean keywordDetected = false;

    /**
     * The "follow-up" command response URL - you can paste this in the browser to do a "GET" using the command
     * response key provided in the URI route.
     */
    private Optional<String> cmdResponseUrl = Optional.empty();

    /**
     * Retrieve cmdResponseUrl specified
     *
     * @return cmdResponseUrl value
     */
    public Optional<String> getCmdResponseUrl() {
        return cmdResponseUrl;
    }

    /**
     * Assign cmdResponseUrl value
     *
     * @param cmdResponseUrl value
     */
    public void setCmdResponseUrl(final String cmdResponseUrl) {
        this.cmdResponseUrl = Optional.ofNullable(cmdResponseUrl);
    }

    /**
     * Retrieve commandResponse specified
     *
     * @return commandResponse value
     */
    public Optional<String> getCommandResponse() {
        return commandResponse;
    }

    /**
     * Assign commandResponse value
     *
     * @param commandResponse value
     */
    public void setCommandResponse(final String commandResponse) {
        this.commandResponse = Optional.ofNullable(commandResponse);
    }

    /**
     * Retrieve failureResponse specified
     *
     * @return failureResponse value
     */
    public Optional<String> getFailureResponse() {
        return failureResponse;
    }

    /**
     * Assign failureResponse value
     *
     * @param failureResponse value
     */
    public void setFailureResponse(final String failureResponse) {
        this.failureResponse = Optional.ofNullable(failureResponse);
    }

    /**
     * Retrieve the keywordDetected specified
     *
     * @return keywordDetected true or false is keywordDetected seen
     */
    public boolean isKeywordDetected() {
        return keywordDetected;
    }

    /**
     * Assign keywordDetected value
     *
     * @param keywordDetected value
     */
    public void setKeywordDetected(boolean keywordDetected) {
        this.keywordDetected = keywordDetected;
    }

    /**
     * Retrieve lastResponseKey specified
     *
     * @return lastResponseKey value
     */
    public Optional<String> getLastResponseKey() {
        return lastResponseKey;
    }

    /**
     * Assign lastResponseKey value
     *
     * @param lastResponseKey value
     */
    public void setLastResponseKey(final String lastResponseKey) {
        this.lastResponseKey = Optional.ofNullable(lastResponseKey);
    }

    /**
     * Retrieve is success
     *
     * @return boolean true or false
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Assign success value
     *
     * @param success true or false is the response seen
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Retrieve zosmfResponse specified
     *
     * @return zosmfResponse value
     */
    public Optional<ZosmfIssueResponse> getZosmfResponse() {
        return zosmfResponse;
    }

    /**
     * Assign zosmfResponse value
     *
     * @param zosmfResponse value
     */
    public void setZosmfResponse(final ZosmfIssueResponse zosmfResponse) {
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
    }

    /**
     * Return string value representing ConsoleResponse object
     *
     * @return string representation of ConsoleResponse
     */
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
