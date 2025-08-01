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

import kong.unirest.core.Cookie;

/**
 * Factory class for creating ZosConnection objects with different authentication types
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosConnectionFactory {

    /**
     * Private constructor defined to avoid instantiation of static factory class
     */
    private ZosConnectionFactory() {
        throw new IllegalStateException("static factory class");
    }

    /**
     * Creates a ZosConnection with basic authentication
     *
     * @param host     Host address of the z/OSMF server
     * @param port     Port number of the z/OSMF server
     * @param user     Username for authentication
     * @param password Password for authentication
     * @return ZosConnection configured for basic authentication
     * @author Frank Giordano
     */
    public static ZosConnection createBasicConnection(final String host,
                                                      final String port,
                                                      final String user,
                                                      final String password) {

        if (host == null || host.isBlank())
            throw new IllegalStateException("Host is either null or empty");

        if (port == null || port.isBlank())
            throw new IllegalStateException("Port is either null or empty");

        if (user == null || user.isBlank())
            throw new IllegalStateException("User is either null or empty");

        if (password == null || password.isBlank())
            throw new IllegalStateException("Password is either null or empty");

        return new ZosConnection.Builder()
                .host(host)
                .zosmfPort(port)
                .user(user)
                .password(password)
                .authType(AuthType.BASIC)
                .build();
    }

    /**
     * Creates a ZosConnection with token authentication
     *
     * @param host  Host address of the z/OSMF server
     * @param port  Port number of the z/OSMF server
     * @param token Authentication token cookie
     * @return ZosConnection configured for token authentication
     * @author Frank Giordano
     */
    public static ZosConnection createTokenConnection(final String host,
                                                      final String port,
                                                      final Cookie token) {
        if (host == null || host.isBlank())
            throw new IllegalStateException("Host is either null or empty");

        if (port == null || port.isBlank())
            throw new IllegalStateException("Port is either null or empty");

        if (token == null)
            throw new IllegalStateException("Token is null");

        return new ZosConnection.Builder()
                .host(host)
                .zosmfPort(port)
                .token(token)
                .authType(AuthType.TOKEN)
                .build();
    }

    /**
     * Creates a ZosConnection with SSL certificate authentication with isSecure
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the certificate file (.p12)
     * @param certPassword Password for the certificate
     * @return ZosConnection configured for SSL authentication
     * @author Frank Giordano
     */
    public static ZosConnection createSslConnection(final String host,
                                                    final String port,
                                                    final String certFilePath,
                                                    final String certPassword) {
        if (host == null || host.isBlank())
            throw new IllegalStateException("Host is either null or empty");

        if (port == null || port.isBlank())
            throw new IllegalStateException("Port is either null or empty");

        if (certFilePath == null || certFilePath.isBlank())
            throw new IllegalStateException("Certificate file path (.p12) is either null or empty");

        if (certPassword == null || certPassword.isBlank())
            throw new IllegalStateException("Certificate password is either null or empty");

        return new ZosConnection.Builder()
                .host(host)
                .zosmfPort(port)
                .certFilePath(certFilePath)
                .certPassword(certPassword)
                .authType(AuthType.SSL)
                .build();
    }

}

