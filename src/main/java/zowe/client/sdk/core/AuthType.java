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
 * Represents the authentication method used for HTTP/HTTPS requests to z/OSMF.
 * <p>
 * <b>Transport Encryption (HTTPS/TLS) vs. Client Authentication:</b>
 * <br>
 * All requests to z/OSMF are transmitted over HTTPS, which requires transport-layer TLS encryption.
 * The {@link AuthType} enum defines how the <i>client's identity</i> is proven over that encrypted channel:
 * <ul>
 *   <li>{@link #BASIC}: Authenticates client identity using a username and password in the HTTP
 *       {@code Authorization: Basic} header.</li>
 *   <li>{@link #TOKEN}: Authenticates client identity using a token or cookie (e.g. JWT or LTPA)
 *       retrieved from a z/OSMF login request.</li>
 *   <li>{@link #SSL}: Authenticates client identity using Mutual TLS (mTLS) via a PKCS12 ({@code .p12})
 *       keystore containing a client certificate and private key. No username or password is required.</li>
 * </ul>
 * <p>
 * Server certificate validation across all authentication types can be configured using system properties
 * such as {@value zowe.client.sdk.rest.RestConstant#TRUSTSTORE_PATH_PROPERTY_NAME} ("zowe.sdk.truststore.path")
 * or {@value zowe.client.sdk.rest.RestConstant#INSECURE_PROPERTY_NAME} ("zowe.sdk.allow.insecure.connection").
 *
 * @author Frank Giordano
 * @version 7.0
 */
public enum AuthType {

    /**
     * Classic Basic Authentication type.
     * <p>
     * Authenticates the client's identity by sending an HTTP {@code Authorization: Basic <base64>} header
     * containing the username and password specified in the {@link ZosConnection} object over an HTTPS encrypted channel.
     */
    BASIC,
    /**
     * Web Token Authentication type.
     * <p>
     * Authenticates the client's identity using a cookie/token value (e.g. JSON Web Token or LTPA token)
     * retrieved from a z/OSMF login response payload over an HTTPS encrypted channel.
     */
    TOKEN,
    /**
     * Client Certificate Authentication (Mutual TLS / mTLS) type.
     * <p>
     * Authenticates the client's identity during the TLS handshake using a PKCS12 ({@code .p12}) key store
     * containing a client certificate and private key. Unlike {@link #BASIC}, no username or password is required
     * because the client's identity is verified directly by z/OSMF using the certificate.
     */
    SSL

}
