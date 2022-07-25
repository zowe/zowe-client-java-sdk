/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso;

import zowe.client.sdk.zostso.zosmf.ZosmfTsoResponse;

import java.util.Optional;

/**
 * The TsoStartStop API response
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class StartStopResponse {

    /**
     * Response from z/OSMF to start rest call
     */
    public final Optional<ZosmfTsoResponse> zosmfTsoResponse;
    /**
     * Servlet key from ZosmfTsoResponse
     */
    public final Optional<String> servletKey;
    /**
     * If an error occurs, returns error which contains cause error.
     */
    public Optional<String> failureResponse = Optional.empty();
    /**
     * True if the command was issued and the responses were collected.
     */
    public Optional<Boolean> success;

    /**
     * StartStopResponse constructor
     *
     * @param success          true or false if response seen
     * @param zosmfTsoResponse tso response
     * @param servletKey       key value for tso address space
     * @author Frank Giordano
     */
    public StartStopResponse(boolean success, ZosmfTsoResponse zosmfTsoResponse, String servletKey) {
        this.success = Optional.of(success);
        this.zosmfTsoResponse = Optional.ofNullable(zosmfTsoResponse);
        this.servletKey = Optional.ofNullable(servletKey);
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
     * @param failureResponse failure response string
     * @author Frank Giordano
     */
    public void setFailureResponse(String failureResponse) {
        this.failureResponse = Optional.ofNullable(failureResponse);
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey value
     * @author Frank Giordano
     */
    public Optional<String> getServletKey() {
        return servletKey;
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
    public void setSuccess(boolean success) {
        this.success = Optional.of(success);
    }

    /**
     * Retrieve zosmfTsoResponse specified
     *
     * @return ZosmfTsoResponse value
     * @author Frank Giordano
     */
    public Optional<ZosmfTsoResponse> getZosmfTsoResponse() {
        return zosmfTsoResponse;
    }

    @Override
    public String toString() {
        return "StartStopResponse{" +
                "success=" + success +
                ", zosmfTsoResponse=" + zosmfTsoResponse +
                ", failureResponse=" + failureResponse +
                ", servletKey=" + servletKey +
                '}';
    }

}
