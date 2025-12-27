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
import zowe.client.sdk.utility.ValidateUtils;

/**
 * Factory class for creating ZosConnection objects with different authentication types
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosConnectionFactory {

    /**
     * Private constructor defined to avoid instantiation of a static factory class
     */
    private ZosConnectionFactory() {
        throw new IllegalStateException("Factory class");
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
     * @author Shabaz Kowthalam
     */
    public static ZosConnection createBasicConnection(final String host,
                                                      final int port,
                                                      final String user,
                                                      final String password) {
        return createBasicZosConnection(host, port, user, password, null);
    }

    /**
     * Creates a ZosConnection with basic authentication
     *
     * @param host     Host address of the z/OSMF server
     * @param port     Port number of the z/OSMF server
     * @param user     Username for authentication
     * @param password Password for authentication
     * @param basePath base path for z/OSMF REST endpoints
     * @return ZosConnection configured for basic authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    public static ZosConnection createBasicConnection(final String host,
                                                      final int port,
                                                      final String user,
                                                      final String password,
                                                      final String basePath) {
        ValidateUtils.checkIllegalParameter(basePath, "basePath");

        return createBasicZosConnection(host, port, user, password, basePath);
    }

    /**
     * Private wrapper method for createBasicZosConnection
     *
     * @param host     Host address of the z/OSMF server
     * @param port     Port number of the z/OSMF server
     * @param user     Username for authentication
     * @param password Password for authentication
     * @param basePath base path for z/OSMF REST endpoints
     * @return ZosConnection configured for token authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    private static ZosConnection createBasicZosConnection(final String host,
                                                          final int port,
                                                          final String user,
                                                          final String password,
                                                          final String basePath) {
        ValidateUtils.checkIllegalParameter(host, "host");
        ValidateUtils.checkIllegalParameter(user, "user");
        ValidateUtils.checkIllegalParameter(password, "password");

        ZosConnection zosConnection = new ZosConnection(host, port, basePath, AuthType.BASIC);
        zosConnection.setUser(user);
        zosConnection.setPassword(password);
        return zosConnection;
    }

    /**
     * Creates a ZosConnection with token authentication
     *
     * @param host  Host address of the z/OSMF server
     * @param port  Port number of the z/OSMF server
     * @param token Authentication token cookie
     * @return ZosConnection configured for token authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    public static ZosConnection createTokenConnection(final String host,
                                                      final int port,
                                                      final Cookie token) {
        return createTokenZosConnection(host, port, token, null);
    }

    /**
     * Creates a ZosConnection with token authentication
     *
     * @param host     Host address of the z/OSMF server
     * @param port     Port number of the z/OSMF server
     * @param token    Authentication token cookie
     * @param basePath base path for z/OSMF REST endpoints
     * @return ZosConnection configured for token authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    public static ZosConnection createTokenConnection(final String host,
                                                      final int port,
                                                      final Cookie token,
                                                      final String basePath) {
        ValidateUtils.checkIllegalParameter(basePath, "basePath");

        return createTokenZosConnection(host, port, token, basePath);
    }

    /**
     * Private wrapper method for createTokenZosConnection
     *
     * @param host     Host address of the z/OSMF server
     * @param port     Port number of the z/OSMF server
     * @param token    Authentication token cookie
     * @param basePath base path for z/OSMF REST endpoints
     * @return ZosConnection configured for token authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    private static ZosConnection createTokenZosConnection(final String host,
                                                          final int port,
                                                          final Cookie token,
                                                          final String basePath) {
        ValidateUtils.checkIllegalParameter(host, "host");
        ValidateUtils.checkNullParameter(token == null, "token is null");

        ZosConnection zosConnection = new ZosConnection(host, port, basePath, AuthType.TOKEN);
        zosConnection.setToken(token);
        return zosConnection;
    }

    /**
     * Creates a ZosConnection with SSL certificate authentication
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the certificate file (.p12)
     * @param certPassword Password for the certificate
     * @return ZosConnection configured for SSL authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    public static ZosConnection createSslConnection(final String host,
                                                    final int port,
                                                    final String certFilePath,
                                                    final String certPassword) {
        return createSslZosConnection(host, port, certFilePath, certPassword, null);
    }

    /**
     * Creates a ZosConnection with SSL certificate authentication
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the certificate file (.p12)
     * @param certPassword Password for the certificate
     * @param basePath     base path for z/OSMF REST endpoints
     * @return ZosConnection configured for SSL authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    public static ZosConnection createSslConnection(final String host,
                                                    final int port,
                                                    final String certFilePath,
                                                    final String certPassword,
                                                    final String basePath) {
        ValidateUtils.checkIllegalParameter(basePath, "basePath");

        return createSslZosConnection(host, port, certFilePath, certPassword, basePath);
    }

    /**
     * Private wrapper method for createSslConnection
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the certificate file (.p12)
     * @param certPassword Password for the certificate
     * @param basePath     base path for z/OSMF REST endpoints
     * @return ZosConnection configured for SSL authentication
     * @author Frank Giordano
     * @author Shabaz Kowthalam
     */
    private static ZosConnection createSslZosConnection(final String host,
                                                        final int port,
                                                        final String certFilePath,
                                                        final String certPassword,
                                                        final String basePath) {
        ValidateUtils.checkIllegalParameter(host, "host");
        ValidateUtils.checkIllegalParameter(certFilePath, "certificate file path (.p12)");
        ValidateUtils.checkIllegalParameter(certPassword, "certPassword");

        ZosConnection zosConnection = new ZosConnection(host, port, basePath, AuthType.SSL);
        zosConnection.setCertFilePath(certFilePath);
        zosConnection.setCertPassword(certPassword);
        return zosConnection;
    }

}
