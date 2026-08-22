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
 * Factory class for creating {@link ZosConnection} objects with different authentication types.
 * <p>
 * Supported authentication types are:
 * <ul>
 *   <li>
 *     <b>Basic Authentication:</b> Uses a username and password to authenticate
 *     requests to z/OSMF.
 *   </li>
 *   <li>
 *     <b>Token Authentication:</b> Uses an authentication token or cookie to
 *     authenticate requests to z/OSMF.
 *   </li>
 *   <li>
 *     <b>SSL/TLS Client Certificate Authentication:</b> Uses a PKCS12 ({@code .p12})
 *     client certificate and private key to authenticate the client using Mutual TLS (mTLS).
 *   </li>
 * </ul>
 * <p>
 * SSL/TLS server certificate validation is configured independently of the authentication
 * type. All connections use HTTPS/TLS and can validate the z/OSMF server certificate using
 * Java's default JVM CA truststore, a custom TrustStore, or the explicitly enabled insecure mode.
 * <p>
 * A custom TrustStore can therefore be used with Basic Authentication, Token Authentication,
 * or SSL/TLS client certificate authentication. The custom TrustStore is used to validate the
 * z/OSMF server certificate and is separate from the client PKCS12 file used for mTLS.
 * <p>
 * The {@link #createSslConnection(String, int, String, String)} methods specifically create
 * {@link AuthType#SSL} connections and therefore require a PKCS12 client certificate file.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class ZosConnectionFactory {

    /**
     * Private constructor defined to avoid instantiation of a static factory class.
     */
    private ZosConnectionFactory() {
        throw new IllegalStateException("Factory class");
    }

    /**
     * Creates a ZosConnection with basic authentication.
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
     * Creates a ZosConnection with basic authentication.
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
     * Creates a ZosConnection with token authentication.
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
     * Creates a ZosConnection with token authentication.
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

    private static ZosConnection createTokenZosConnection(final String host,
                                                          final int port,
                                                          final Cookie token,
                                                          final String basePath) {
        ValidateUtils.checkIllegalParameter(host, "host");
        ValidateUtils.checkNullParameter(token, "token");

        ZosConnection zosConnection = new ZosConnection(host, port, basePath, AuthType.TOKEN);
        zosConnection.setToken(token);
        return zosConnection;
    }

    /**
     * Creates a {@link ZosConnection} with SSL/TLS (mTLS) client certificate authentication
     * using a PKCS12 file ({@code .p12}).
     * <p>
     * The specified PKCS12 file contains the client certificate and private key used to
     * authenticate the client application with z/OSMF using Mutual TLS (mTLS).
     * <p>
     * Server certificate validation is configured independently of client certificate
     * authentication and supports the following modes:
     * <ul>
     *   <li>
     *     <b>Default:</b> The z/OSMF server certificate is validated against Java's
     *     default JVM CA truststore ({@code cacerts}) with standard hostname verification.
     *   </li>
     *   <li>
     *     <b>Custom TrustStore:</b> A separate {@code .p12} or {@code .jks} TrustStore
     *     can be configured using
     *     {@code zowe.sdk.truststore.path} and the optional
     *     {@code zowe.sdk.truststore.password} system properties. The TrustStore is used
     *     to validate the z/OSMF server certificate and is separate from the client
     *     certificate PKCS12 file.
     *   </li>
     *   <li>
     *     <b>Insecure Mode:</b> Setting
     *     {@code zowe.sdk.allow.insecure.connection} to {@code true} explicitly disables
     *     server certificate validation and hostname verification. This mode is intended
     *     only for isolated test or sandbox environments.
     *   </li>
     * </ul>
     * <p>
     * The client PKCS12 file is required for this method because the connection uses
     * {@link AuthType#SSL} client certificate authentication.
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the PKCS12 certificate file ({@code .p12}) containing
     *                     the client certificate and private key
     * @param certPassword Password for the PKCS12 certificate file ({@code .p12})
     * @return ZosConnection configured for SSL/TLS client certificate authentication
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
     * Creates a {@link ZosConnection} with SSL/TLS (mTLS) client certificate authentication
     * using a PKCS12 file ({@code .p12}), with a specified base path for z/OSMF REST endpoints.
     * <p>
     * The SSL/TLS server certificate validation behavior is the same as the
     * {@link #createSslConnection(String, int, String, String)} overload.
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the PKCS12 certificate file ({@code .p12}) containing
     *                     the client certificate and private key
     * @param certPassword Password for the PKCS12 certificate file ({@code .p12})
     * @param basePath     Base path for z/OSMF REST endpoints
     * @return ZosConnection configured for SSL/TLS client certificate authentication
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
