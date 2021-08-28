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
 * The TsoStartStop API response
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class StartStopResponse {

    /**
     * True if the command was issued and the responses were collected.
     */
    public Optional<Boolean> success;

    /**
     * Response from z/OSMF to start rest call
     */
    public Optional<ZosmfTsoResponse> zosmfTsoResponse;

    /**
     * If an error occurs, returns error which contains cause error.
     */
    public Optional<String> failureResponse = Optional.empty();

    /**
     * Servlet key from ZosmfTsoResponse
     */
    public Optional<String> servletKey;

    public StartStopResponse(boolean success, ZosmfTsoResponse zosmfTsoResponse, String servletKey) {
        this.success = Optional.ofNullable(success);
        this.zosmfTsoResponse = Optional.ofNullable(zosmfTsoResponse);
        this.servletKey = Optional.ofNullable(servletKey);
    }

    public Optional<Boolean> getSuccess() {
        return success;
    }

    public Optional<ZosmfTsoResponse> getZosmfTsoResponse() {
        return zosmfTsoResponse;
    }

    public Optional<String> getFailureResponse() {
        return failureResponse;
    }

    public Optional<String> getServletKey() {
        return servletKey;
    }

    public void setSuccess(boolean success) {
        this.success = Optional.ofNullable(success);
    }

    public void setFailureResponse(String failureResponse) {
        this.failureResponse = Optional.ofNullable(failureResponse);
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
