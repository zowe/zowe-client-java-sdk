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
 * The z/OSMF tso common response.
 * <p>
 * This class is used to parse the response from the z/OSMF tso apis using jackson library.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoCommonResponse {

    private String servletKey;
    private String ver;
    private Boolean timeout;
    private Boolean reused;

    /**
     * TsoStopResponse No-argument constructor (required by Jackson)
     */
    public TsoCommonResponse() {
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
     * Set tso session id
     *
     * @param servletKey string value representing tso session id
     */
    public void setServletKey(String servletKey) {
        this.servletKey = servletKey;
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
     * Set tso version value
     *
     * @param ver string value
     */
    public void setVer(String ver) {
        this.ver = ver;
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
     * Set tso timeout value
     *
     * @param timeout Boolean value
     */
    public void setTimeout(Boolean timeout) {
        this.timeout = timeout;
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
     * Set tso reuse value
     *
     * @param reused boolean value
     */
    public void setReused(Boolean reused) {
        this.reused = reused;
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
