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

import java.util.Optional;

/**
 * TSO stop command z/OSMF parameters
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class StopTsoInputData {

    /**
     * Servlet key of an active address space
     */
    private final Optional<String> servletKey;

    /**
     * StopTsoParams constructor
     *
     * @param servletKey key of an active tso address space
     * @author Frank Giordano
     */
    public StopTsoInputData(final String servletKey) {
        ValidateUtils.checkIllegalParameter(servletKey, "servletKey");
        this.servletKey = Optional.of(servletKey);
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey key value of an active address space
     */
    public Optional<String> getServletKey() {
        return servletKey;
    }

    /**
     * Return string value representing StopTsoParams object
     *
     * @return string representation of StopTsoParams
     */
    @Override
    public String toString() {
        return "StopTsoParams{" +
                "servletKey=" + servletKey +
                '}';
    }

}
