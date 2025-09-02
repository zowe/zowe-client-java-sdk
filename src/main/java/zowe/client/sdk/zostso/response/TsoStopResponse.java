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
 * The z/OSMF tso stop response.
 * <p>
 * This class is used to parse the response from the z/OSMF tso ping command using jackson library.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoStopResponse {

    private String servletKey;
    private String ver;
    private Boolean timeout;
    private Boolean reuse;

    /**
     * TsoStopResponse No-argument constructor (required by Jackson)
     */
    public TsoStopResponse() {
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
    public Boolean getReuse() {
        return reuse;
    }

    /**
     * Set tso reuse value
     *
     * @param reuse boolean value
     */
    public void setReuse(Boolean reuse) {
        this.reuse = reuse;
    }

    /**
     * Return string value representing TsoStopResponse object
     *
     * @return string representation of TsoStopResponse
     */
    @Override
    public String toString() {
        return "TsoStopResponse{" +
                "servletKey='" + servletKey + '\'' +
                ", ver='" + ver + '\'' +
                ", timeout='" + timeout + '\'' +
                ", reuse='" + reuse + '\'' +
                '}';
    }

}
