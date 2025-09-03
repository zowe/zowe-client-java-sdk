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

/**
 * The z/OSMF tso start response.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoStartResponse {

    private final boolean success;
    private final String sessionId;
    private final String response;

    /**
     * TsoStartResponse constructor
     *
     * @param success   boolean value
     * @param sessionId string value
     * @param response  string value
     */
    public TsoStartResponse(final boolean success, final String sessionId, final String response) {
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
        return "TsoStartResponse [" +
                "success=" + success + ", " +
                "sessionId=" + sessionId + ", " +
                "response=" + response + "]";
    }

}
