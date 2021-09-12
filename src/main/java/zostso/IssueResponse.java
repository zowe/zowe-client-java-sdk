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
    private boolean success = false;

    /**
     * zOSMF start TSO API response.
     */
    private Optional<StartStopResponses> startResponse;

    /**
     * Indicates if started TSO contains "READY " message
     */
    private boolean startReady = false;

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
     * Retrieve success specified
     *
     * @return boolean value
     * @author Frank Giordano
     */
    public boolean getSuccess() {
        return success;
    }

    /**
     * Assign success value
     *
     * @param success true or false is response seen
     * @author Frank Giordano
     */
    public void setSuccess(boolean success) {
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
    public void setStartResponse(StartStopResponses startResponse) {
        this.startResponse = Optional.ofNullable(startResponse);
    }

    /**
     * Retrieve startReady specified
     *
     * @return startReady value
     * @author Frank Giordano
     */
    public boolean getStartReady() {
        return startReady;
    }

    /**
     * Assign startReady value
     *
     * @param startReady true or false is start ready prompt seen or not
     * @author Frank Giordano
     */
    public void setStartReady(boolean startReady) {
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
    public void setStopResponse(StartStopResponse stopResponse) {
        this.stopResponse = Optional.ofNullable(stopResponse);
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
    public void setZosmfResponse(ZosmfTsoResponse zosmfResponse) {
        this.zosmfResponse = Optional.ofNullable(zosmfResponse);
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
