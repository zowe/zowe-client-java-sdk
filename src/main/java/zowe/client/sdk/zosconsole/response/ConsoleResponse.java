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
 * @version 5.0
 */
public class ConsoleResponse {

    /**
     * True if the command was issued and the responses were collected.
     */
    private boolean success = false;

    /**
     * If an error occurs, returns the ImperativeError, which contains a case error.
     */
    private String failureResponse;

    /**
     * The command response text.
     */
    private String commandResponse;

    /**
     * The final command response key - used to "follow up" and check for additional response messages for the command.
     */
    private String lastResponseKey;

    /**
     * If the solicited keyword is specified, indicates that the keyword was detected.
     */
    private boolean keywordDetected = false;

    /**
     * The "follow-up" command response URL - you can paste this in the browser to do a "GET" using the command
     * response key provided in the URI route.
     */
    private String cmdResponseUrl;

    /**
     * Retrieve cmdResponseUrl specified
     *
     * @return cmdResponseUrl value
     */
    public Optional<String> getCmdResponseUrl() {
        return Optional.ofNullable(cmdResponseUrl);
    }

    /**
     * Assign cmdResponseUrl value
     *
     * @param cmdResponseUrl value
     */
    public void setCmdResponseUrl(final String cmdResponseUrl) {
        this.cmdResponseUrl = cmdResponseUrl;
    }

    /**
     * Retrieve commandResponse specified
     *
     * @return commandResponse value
     */
    public Optional<String> getCommandResponse() {
        return Optional.ofNullable(commandResponse);
    }

    /**
     * Assign commandResponse value
     *
     * @param commandResponse value
     */
    public void setCommandResponse(final String commandResponse) {
        this.commandResponse = commandResponse;
    }

    /**
     * Retrieve failureResponse specified
     *
     * @return failureResponse value
     */
    public Optional<String> getFailureResponse() {
        return Optional.ofNullable(failureResponse);
    }

    /**
     * Assign failureResponse value
     *
     * @param failureResponse value
     */
    public void setFailureResponse(final String failureResponse) {
        this.failureResponse = failureResponse;
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
        return Optional.ofNullable(lastResponseKey);
    }

    /**
     * Assign lastResponseKey value
     *
     * @param lastResponseKey value
     */
    public void setLastResponseKey(final String lastResponseKey) {
        this.lastResponseKey = lastResponseKey;
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
     * Return string value representing ConsoleResponse object
     *
     * @return string representation of ConsoleResponse
     */
    @Override
    public String toString() {
        return "ConsoleResponse{" +
                "success=" + success +
                ", failureResponse=" + failureResponse +
                ", commandResponse=" + commandResponse +
                ", lastResponseKey=" + lastResponseKey +
                ", keywordDetected=" + keywordDetected +
                ", cmdResponseUrl=" + cmdResponseUrl +
                '}';
    }

}
