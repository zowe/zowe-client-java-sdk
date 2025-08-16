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

import zowe.client.sdk.utility.ValidateUtils;

/**
 * TSO issue command z/OSMF parameters
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class SendTsoInputData {

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
    public SendTsoInputData(final String servletKey, final String data) {
        ValidateUtils.checkIllegalParameter(servletKey, "servletKey");
        ValidateUtils.checkIllegalParameter(data, "data");
        this.servletKey = servletKey;
        this.data = data;
    }

    /**
     * Retrieve data specified
     *
     * @return data value being used to send it to active address space
     */
    public String getData() {
        return data;
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey key value of an active address space
     */
    public String getServletKey() {
        return servletKey;
    }

    /**
     * Return string value representing SendTsoParams object
     *
     * @return string representation of SendTsoParams
     */
    @Override
    public String toString() {
        return "SendTsoParams{" +
                "servletKey='" + servletKey + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

}
