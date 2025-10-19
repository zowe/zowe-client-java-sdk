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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The z/OSMF tso start response.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TsoStartResponse {

    /**
     * Indicates if TSO start was successful
     */
    @JsonProperty("success")
    private final boolean success;

    /**
     * TSO session identifier
     */
    @JsonProperty("sessionId")
    private final String sessionId;

    /**
     * TSO start response message
     */
    @JsonProperty("response")
    private final String response;

    /**
     * TsoStartResponse constructor for JSON deserialization
     *
     * @param success   indicates if TSO start was successful
     * @param sessionId TSO session identifier
     * @param response  TSO start response message
     */
    @JsonCreator
    public TsoStartResponse(
            @JsonProperty("success") final boolean success,
            @JsonProperty("sessionId") final String sessionId,
            @JsonProperty("response") final String response) {
        this.success = success;
        this.sessionId = sessionId;
        this.response = response;
    }

    /**
     * Did TSO start succeed?
     *
     * @return boolean value
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Retrieve TSO start response string
     *
     * @return string value
     */
    public String getResponse() {
        return response;
    }

    /**
     * Retrieve started TSO session id
     *
     * @return string value
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Return string value representing TsoStartResponse object
     *
     * @return string representation of TsoStartResponse
     */
    @Override
    public String toString() {
        return "TsoStartResponse{" +
                "success=" + success +
                ", sessionId='" + sessionId + '\'' +
                ", response='" + response + '\'' +
                '}';
    }

}
