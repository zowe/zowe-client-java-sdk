/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.response;

import zowe.client.sdk.zostso.message.ZosmfTsoResponse;

import java.util.Optional;

/**
 * The TsoStartStop API response
 *
 * @author Frank Giordano
 * @version 2.0
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
    public boolean success;

    /**
     * StartStopResponse constructor
     *
     * @param success          true or false if response seen
     * @param zosmfTsoResponse tso response
     * @param servletKey       key value for tso address space
     * @author Frank Giordano
     */
    public StartStopResponse(final boolean success, final ZosmfTsoResponse zosmfTsoResponse, final String servletKey) {
        this.success = success;
        this.zosmfTsoResponse = Optional.ofNullable(zosmfTsoResponse);
        this.servletKey = Optional.ofNullable(servletKey);
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
     * @param failureResponse failure response string
     */
    public void setFailureResponse(final String failureResponse) {
        this.failureResponse = Optional.ofNullable(failureResponse);
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey value
     */
    public Optional<String> getServletKey() {
        return servletKey;
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
     * @param success true or false is response seen
     */
    public void setSuccess(final boolean success) {
        this.success = success;
    }

    /**
     * Retrieve zosmfTsoResponse specified
     *
     * @return ZosmfTsoResponse value=
     */
    public Optional<ZosmfTsoResponse> getZosmfTsoResponse() {
        return zosmfTsoResponse;
    }

    /**
     * Return string value representing StartStopResponse object
     *
     * @return string representation of StartStopResponse
     */
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
