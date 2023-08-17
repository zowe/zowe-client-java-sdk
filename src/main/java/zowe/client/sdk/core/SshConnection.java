/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.core;

/**
 * SSH Connection information placeholder
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class SshConnection {

    /**
     * machine host pointing to backend z/OS instance
     */
    private final String host;

    /**
     * machine host port number pointing to backend z/OS instance
     */
    private final int port;

    /**
     * machine host username with access to backend z/OS instance
     */
    private final String user;

    /**
     * machine host username's password with access to backend z/OS instance
     */
    private final String password;

    /**
     * ZOSConnection constructor
     *
     * @param host     machine host pointing to backend z/OS instance
     * @param port     machine host port number pointing to backend z/OS instance
     * @param user     machine host username with access to backend z/OS instance
     * @param password machine host username's password with access to backend z/OS instance
     * @author Frank Giordano
     */
    public SshConnection(final String host, final int port, final String user, final String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    /**
     * Retrieve host specified
     *
     * @return host value
     * @author Frank Giordano
     */
    public String getHost() {
        return host;
    }

    /**
     * Retrieve port number specified
     *
     * @return port value
     * @author Frank Giordano
     */
    public int getPort() {
        return port;
    }

    /**
     * Retrieve username specified
     *
     * @return user value
     * @author Frank Giordano
     */
    public String getUser() {
        return user;
    }

    /**
     * Retrieve password specified
     *
     * @return password value
     * @author Frank Giordano
     */
    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "SSHConnection{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
