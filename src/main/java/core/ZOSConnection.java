/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package core;

/**
 * z/OS Connection information placeholder
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ZOSConnection {

    /**
     * machine host pointing to backend z/OS instance
     */
    private String host;

    /**
     * machine host port number pointing to backend z/OS instance
     */
    private String port;

    /**
     * machine host username with access to backend z/OS instance
     */
    private String user;

    /**
     * machine host username's password with access to backend z/OS instance
     */
    private String password;

    /**
     * ZOSConnection constructor
     *
     * @param host     machine host pointing to backend z/OS instance
     * @param port     machine host port number pointing to backend z/OS instance
     * @param user     machine host username with access to backend z/OS instance
     * @param password machine host username's password with access to backend z/OS instance
     * @author Frank Giordano
     */
    public ZOSConnection(String host, String port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * Retrieve host specified
     *
     * @author Frank Giordano
     */
    public String getHost() {
        return host;
    }

    /**
     * Retrieve port number specified
     *
     * @author Frank Giordano
     */
    public String getPort() {
        return port;
    }

    /**
     * Retrieve username specified
     *
     * @author Frank Giordano
     */
    public String getUser() {
        return user;
    }

    /**
     * Retrieve password specified
     *
     * @author Frank Giordano
     */
    public String getPassword() {
        return password;
    }

}
