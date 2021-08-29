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

/**
 * The TsoSend API response
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class IssueResponse {

    /**
     * True if the command was issued and the responses were collected.
     */
    private Optional<Boolean> success;

    /**
     * zOSMF start TSO API response.
     */
    private Optional<StartStopResponses> startResponse;

    /**
     * Indicates if started TSO contains "READY " message
     */
    private Optional<Boolean> startReady;

    /**
     * zOSMF stop TSO API response.
     */
    private Optional<StartStopResponse> stopResponse;

    /**
     * The list of zOSMF send API responses. May issue multiple requests or
     * to ensure that all messages are collected. Each individual response is placed here.
     */
    private Optional<ZosmfTsoResponse> zosmfResponse;

    /**
     * The command response text.
     */
    private Optional<String> commandResponses;

    /**
     * IssueResponse constructor
     *
     * @param success          true or false if response seen
     * @param startResponse    tso response
     * @param startReady       true or false if ready tso starting prompt is seen
     * @param stopResponses    tso response
     * @param zosmfResponse    z/OSMF response
     * @param commandResponses tso command responses
     * @author Frank Giordano
     */
    public IssueResponse(boolean success, StartStopResponses startResponse, boolean startReady,
                         StartStopResponse stopResponses, ZosmfTsoResponse zosmfResponse, String commandResponses) {
        this.success = Optional.ofNullable(success);
        this.startResponse = Optional.ofNullable(startResponse);
        this.startReady = Optional.ofNullable(startReady);
        this.stopResponse = Optional.ofNullable(stopResponses);
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
        this.commandResponses = Optional.ofNullable(commandResponses);
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
    public void setSuccess(Optional<Boolean> success) {
        this.success = success;
    }

    /**
     * Retrieve startResponse specified
     *
     * @return startResponse value
     * @author Frank Giordano
     */
    public Optional<StartStopResponses> getStartResponse() {
        return startResponse;
    }

    /**
     * Assign startResponse value
     *
     * @param startResponse tso response
     * @author Frank Giordano
     */
    public void setStartResponse(Optional<StartStopResponses> startResponse) {
        this.startResponse = startResponse;
    }

    /**
     * Retrieve startReady specified
     *
     * @return startReady value
     * @author Frank Giordano
     */
    public Optional<Boolean> getStartReady() {
        return startReady;
    }

    /**
     * Assign startReady value
     *
     * @param startReady true or false is start ready prompt seen or not
     * @author Frank Giordano
     */
    public void setStartReady(Optional<Boolean> startReady) {
        this.startReady = startReady;
    }

    /**
     * Retrieve stopResponse specified
     *
     * @return stopResponse tso response
     * @author Frank Giordano
     */
    public Optional<StartStopResponse> getStopResponse() {
        return stopResponse;
    }

    /**
     * Assign stopResponse value
     *
     * @param stopResponse tso response
     * @author Frank Giordano
     */
    public void setStopResponse(Optional<StartStopResponse> stopResponse) {
        this.stopResponse = stopResponse;
    }

    /**
     * Retrieve zosmfResponse specified
     *
     * @return zosmfResponse z/OSMF response
     * @author Frank Giordano
     */
    public Optional<ZosmfTsoResponse> getZosmfResponse() {
        return zosmfResponse;
    }

    /**
     * Assign zosmfResponse value
     *
     * @param zosmfResponse z/OSMF response
     * @author Frank Giordano
     */
    public void setZosmfResponse(Optional<ZosmfTsoResponse> zosmfResponse) {
        this.zosmfResponse = zosmfResponse;
    }

    /**
     * Retrieve zosmfResponse specified
     *
     * @return zosmfResponse z/OSMF response
     * @author Frank Giordano
     */
    public Optional<String> getCommandResponses() {
        return commandResponses;
    }

    /**
     * Assign commandResponses value
     *
     * @param commandResponses command string responses
     * @author Frank Giordano
     */
    public void setCommandResponses(Optional<String> commandResponses) {
        this.commandResponses = commandResponses;
    }

    @Override
    public String toString() {
        return "IssueResponse{" +
                "success=" + success +
                ", startResponse=" + startResponse +
                ", startReady=" + startReady +
                ", stopResponse=" + stopResponse +
                ", zosmfResponse=" + zosmfResponse +
                ", commandResponses=" + commandResponses +
                '}';
    }

}
