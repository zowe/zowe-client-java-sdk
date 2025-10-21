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
 * The z/OSMF tso common response.
 * <p>
 * This class is used to parse the response from the z/OSMF tso apis using jackson library.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TsoCommonResponse {

    /**
     * TSO session identifier
     */
    private final String servletKey;

    /**
     * TSO version
     */
    private final String ver;

    /**
     * Indicates if timeout occurred
     */
    private final Boolean timeout;

    /**
     * Indicates if a session was reused
     */
    private final Boolean reused;

    /**
     * TsoCommonResponse constructor for JSON deserialization
     *
     * @param servletKey TSO session identifier
     * @param ver        TSO version
     * @param timeout    Indicates if timeout occurred
     * @param reused     Indicates if a session was reused
     */
    @JsonCreator
    public TsoCommonResponse(
            @JsonProperty("servletKey") final String servletKey,
            @JsonProperty("ver") final String ver,
            @JsonProperty("timeout") final Boolean timeout,
            @JsonProperty("reused") final Boolean reused) {
        this.servletKey = servletKey == null ? "" : servletKey;
        this.ver = ver == null ? "" : ver;
        this.timeout = timeout;
        this.reused = reused;
    }

    /**
     * Retrieve tso session id
     *
     * @return string value representing tso session id
     */
    public String getServletKey() {
        return servletKey;
    }

    /**
     * Retrieve tso version value
     *
     * @return string value
     */
    public String getVer() {
        return ver;
    }

    /**
     * Retrieve tso timeout value
     *
     * @return Boolean value
     */
    public Boolean getTimeout() {
        return timeout;
    }

    /**
     * Retrieve tso reuse value
     *
     * @return boolean value
     */
    public Boolean getReused() {
        return reused;
    }

    /**
     * Return string value representing TsoCommonResponse object
     *
     * @return string representation of TsoCommonResponse
     */
    @Override
    public String toString() {
        return "TsoCommonResponse{" +
                "servletKey='" + servletKey + '\'' +
                ", ver='" + ver + '\'' +
                ", timeout='" + timeout + '\'' +
                ", reused='" + reused + '\'' +
                '}';
    }

}
