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

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * Holds connection parameters required to connect to z/OS native services over SSH (e.g. zowex).
 *
 * @author Chaitanya Katore
 * @version 7.0
 */
public class SshConnection {

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String privateKeyPath;

    /**
     * SshConnection constructor using password authentication.
     *
     * @param host     target hostname or IP address
     * @param port     SSH port (e.g., 22)
     * @param user     SSH username
     * @param password SSH password
     */
    public SshConnection(final String host, final int port, final String user, final String password) {
        ValidateUtils.checkIllegalParameter(host, "host");
        ValidateUtils.checkIllegalParameter(user, "user");
        ValidateUtils.checkIllegalParameter(password, "password");
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("invalid port number: " + port);
        }
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.privateKeyPath = null;
    }

    /**
     * SshConnection constructor using SSH key authentication.
     *
     * @param host           target hostname or IP address
     * @param port           SSH port (e.g., 22)
     * @param user           SSH username
     * @param password       SSH password/passphrase or null if unencrypted key
     * @param privateKeyPath path to SSH private key file
     */
    public SshConnection(final String host, final int port, final String user,
                         final String password, final String privateKeyPath) {
        ValidateUtils.checkIllegalParameter(host, "host");
        ValidateUtils.checkIllegalParameter(user, "user");
        ValidateUtils.checkIllegalParameter(privateKeyPath, "privateKeyPath");
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("invalid port number: " + port);
        }
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.privateKeyPath = privateKeyPath;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Optional<String> getPrivateKeyPath() {
        return Optional.ofNullable(privateKeyPath);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SshConnection that = (SshConnection) o;
        return port == that.port &&
                Objects.equals(host, that.host) &&
                Objects.equals(user, that.user) &&
                Objects.equals(password, that.password) &&
                Objects.equals(privateKeyPath, that.privateKeyPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port, user, password, privateKeyPath);
    }

    @Override
    public String toString() {
        return "SshConnection{" +
                "host='" + ((host == null) ? "" : host) + '\'' +
                ", port=" + port +
                ", user='" + ((user == null) ? "" : user) + '\'' +
                ", password='" + ((password == null || password.isEmpty()) ? "" : "*****") + '\'' +
                ", privateKeyPath='" + ((privateKeyPath == null) ? "" : privateKeyPath) + '\'' +
                '}';
    }

}
