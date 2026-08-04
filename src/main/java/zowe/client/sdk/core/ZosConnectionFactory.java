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
 * @version 7.0
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
        ValidateUtils.checkNullParameter(token, "token");

        ZosConnection zosConnection = new ZosConnection(host, port, basePath, AuthType.TOKEN);
        zosConnection.setToken(token);
        return zosConnection;
    }

    /**
     * Creates a ZosConnection with SSL/TLS (mTLS) certificate authentication using a PKCS12 file (.p12).
     * <p>
     * The specified PKCS12 file (.p12) houses the client certificate and private key used to authenticate
     * the client application with z/OSMF.
     * <p>
     * Server certificate validation during HTTPS requests supports three modes:
     * <ul>
     *   <li><b>Default Mode (Standard CA Validation):</b> When no system properties are set (`zowe.sdk.allow.insecure.connection = false`),
     *       the client .p12 certificate file is used solely for client authentication (mTLS). Server certificate
     *       verification relies on the JVM's standard default truststore ({@code cacerts}) with standard
     *       hostname verification. When the z/OSMF server presents a certificate signed by a CA trusted by Java,
     *       server verification succeeds automatically. Recommended for production.</li>
     *   <li><b>Option 1 (Custom TrustStore):</b> To support self-signed z/OSMF servers securely without using
     *       {@code TRUST_ALL_CERTS}, set system property {@code "zowe.sdk.truststore.path"} (and optional
     *       {@code "zowe.sdk.truststore.password"}) to load a separate TrustStore (.p12 or .jks) containing the
     *       server's certificate/CA, rather than reusing the client's mTLS .p12 file. Disables hostname verification.</li>
     *   <li><b>Option 2 (Insecure Mode):</b> Set system property {@code "zowe.sdk.allow.insecure.connection"}
     *       to {@code "true"} as an explicit, optional developer opt-in (disabled by default) designed specifically
     *       to bypass server TLS certificate validation using {@code TRUST_ALL_CERTS} (similar to {@code curl -k})
     *       and disable hostname verification when users do not have the server certificate in a truststore.
     *       Accompanied by prominent log warnings. Intended for isolated test/sandbox environments only.</li>
     * </ul>
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the PKCS12 certificate file (.p12) containing client key and cert
     * @param certPassword Password for the PKCS12 certificate file (.p12)
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
     * Creates a ZosConnection with SSL/TLS (mTLS) certificate authentication using a PKCS12 file (.p12).
     * <p>
     * The specified PKCS12 file (.p12) houses the client certificate and private key used to authenticate
     * the client application with z/OSMF.
     * <p>
     * Server certificate validation during HTTPS requests supports three modes:
     * <ul>
     *   <li><b>Default Mode (Standard CA Validation):</b> When no system properties are set (`zowe.sdk.allow.insecure.connection = false`),
     *       the client .p12 certificate file is used solely for client authentication (mTLS). Server certificate
     *       verification relies on the JVM's standard default truststore ({@code cacerts}) with standard
     *       hostname verification. When the z/OSMF server presents a certificate signed by a CA trusted by Java,
     *       server verification succeeds automatically. Recommended for production.</li>
     *   <li><b>Option 1 (Custom TrustStore):</b> To support self-signed z/OSMF servers securely without using
     *       {@code TRUST_ALL_CERTS}, set system property {@code "zowe.sdk.truststore.path"} (and optional
     *       {@code "zowe.sdk.truststore.password"}) to load a separate TrustStore (.p12 or .jks) containing the
     *       server's certificate/CA, rather than reusing the client's mTLS .p12 file. Disables hostname verification.</li>
     *   <li><b>Option 2 (Insecure Mode):</b> Set system property {@code "zowe.sdk.allow.insecure.connection"}
     *       to {@code "true"} as an explicit, optional developer opt-in (disabled by default) designed specifically
     *       to bypass server TLS certificate validation using {@code TRUST_ALL_CERTS} (similar to {@code curl -k})
     *       and disable hostname verification when users do not have the server certificate in a truststore.
     *       Accompanied by prominent log warnings. Intended for isolated test/sandbox environments only.</li>
     * </ul>
     *
     * @param host         Host address of the z/OSMF server
     * @param port         Port number of the z/OSMF server
     * @param certFilePath Path to the PKCS12 certificate file (.p12) containing client key and cert
     * @param certPassword Password for the PKCS12 certificate file (.p12)
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
