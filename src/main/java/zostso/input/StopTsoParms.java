/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso.input;

import java.util.Optional;

/**
 * TSO stop command z/OSMF parameters
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class StopTsoParms {

    /**
     * Servlet key of an active address space
     */
    private Optional<String> servletKey;

    /**
     * SendTsoParms constructor
     *
     * @param servletKey key of an active tso address space
     * @author Frank Giordano
     */
    public StopTsoParms(String servletKey) {
        this.servletKey = Optional.ofNullable(servletKey);
    }

    /**
     * Retrieve servletKey specified
     *
     * @return servletKey key value of an active address space
     * @author Frank Giordano
     */
    public Optional<String> getServletKey() {
        return servletKey;
    }

    @Override
    public String toString() {
        return "StopTsoParms{" +
                "servletKey=" + servletKey +
                '}';
    }

}
