/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.input;

/**
 * TSO issue command z/OSMF parameters
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SendTsoParams {

    /**
     * Servlet key of an active address space
     */
    private final String servletKey;

    /**
     * Data to be sent to the active address space
     */
    private final String data;

    /**
     * SendTsoParams constructor
     *
     * @param servletKey key of an active address space
     * @param data       to be sent to the active address space
     * @author Frank Giordano
     */
    public SendTsoParams(String servletKey, String data) {
        this.servletKey = servletKey;
        this.data = data;
    }

    /**
     * Retrieve data specified
     *
     * @return data value being used to sent to active address space
     * @author Frank Giordano
     */
    public String getData() {
        return data;
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey key value of an active address space
     * @author Frank Giordano
     */
    public String getServletKey() {
        return servletKey;
    }

    @Override
    public String toString() {
        return "SendTsoParams{" +
                "servletKey='" + servletKey + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

}
