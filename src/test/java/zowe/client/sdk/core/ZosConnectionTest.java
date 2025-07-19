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
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for ZosConnection.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosConnectionTest {

    private static final String HOST = "test.host";
    private static final String PORT = "443";
    private static final String USER = "testuser";
    private static final String PASSWORD = "testpass";
    private static final String CERT_PATH = "/path/to/cert.p12";
    private static final String CERT_PASSWORD = "certpass";
    private static final String CUSTOM_BASE_PATH = "/custom/path";

    @Test
    public void tstBasicConnectionBuilderSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        assertEquals(HOST, connection.getHost());
        assertEquals(PORT, connection.getZosmfPort());
        assertEquals(USER, connection.getUser());
        assertEquals(PASSWORD, connection.getPassword());
        assertEquals(AuthType.BASIC, connection.getAuthType());
        assertTrue(connection.isSecure());
    }

    @Test
    public void tstTokenConnectionBuilderSuccess() {
        Cookie token = new Cookie("LtpaToken2", "token123");
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .token(token)
                .authType(AuthType.TOKEN)
                .build();

        assertEquals(HOST, connection.getHost());
        assertEquals(PORT, connection.getZosmfPort());
        assertEquals(token, connection.getToken());
        assertEquals(AuthType.TOKEN, connection.getAuthType());
        assertTrue(connection.isSecure());
    }

    @Test
    public void tstSslConnectionBuilderSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .certFilePath(CERT_PATH)
                .certPassword(CERT_PASSWORD)
                .authType(AuthType.SSL)
                .build();

        assertEquals(HOST, connection.getHost());
        assertEquals(PORT, connection.getZosmfPort());
        assertEquals(CERT_PATH, connection.getCertFilePath());
        assertEquals(CERT_PASSWORD, connection.getCertPassword());
        assertEquals(AuthType.SSL, connection.getAuthType());
        assertTrue(connection.isSecure());
    }

    @Test
    public void tstSetAndGetUserSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .authType(AuthType.BASIC)
                .build();

        connection.setUser("newuser");
        assertEquals("newuser", connection.getUser());
    }

    @Test
    public void tstSecureFlagSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .secure(false)
                .authType(AuthType.BASIC)
                .build();

        assertFalse(connection.isSecure());
    }

    @Test
    public void tstEqualsSameInstanceSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        assertEquals(connection, connection);
    }

    @Test
    public void tstEqualsNullSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        assertNotEquals(null, connection);
    }

    @Test
    public void tstEqualsDifferentClassSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        assertNotEquals(new Object(), connection);
    }

    @Test
    public void tstHashCodeConsistencySuccess() {
        ZosConnection connection1 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        ZosConnection connection2 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        assertEquals(connection1.hashCode(), connection2.hashCode());
    }

    @Test
    public void tstTokenAuthenticationWithUserSuccess() {
        Cookie token = new Cookie("LtpaToken2", "token123");
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .token(token)
                .authType(AuthType.TOKEN)
                .build();

        assertEquals(USER, connection.getUser());
        assertEquals(token, connection.getToken());
        assertEquals(AuthType.TOKEN, connection.getAuthType());
    }

    @Test
    public void tstSslAuthenticationWithUserSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .certFilePath(CERT_PATH)
                .certPassword(CERT_PASSWORD)
                .authType(AuthType.SSL)
                .build();

        assertEquals(USER, connection.getUser());
        assertEquals(CERT_PATH, connection.getCertFilePath());
        assertEquals(CERT_PASSWORD, connection.getCertPassword());
        assertEquals(AuthType.SSL, connection.getAuthType());
    }

    @Test
    public void tstEqualsWithDifferentAuthTypesSuccess() {
        ZosConnection basicConnection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        ZosConnection sslConnection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .certFilePath(CERT_PATH)
                .certPassword(CERT_PASSWORD)
                .authType(AuthType.SSL)
                .build();

        assertNotEquals(basicConnection, sslConnection);
    }

    @Test
    public void tstDefaultBasePathIsEmptySuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        assertTrue(connection.getBasePath().isEmpty(), "Default basePath should be empty");
    }

    @Test
    public void tstSetAndGetBasePathSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        connection.setBasePath(CUSTOM_BASE_PATH);
        assertEquals(CUSTOM_BASE_PATH,
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : ""),
                "BasePath should match the set value");
    }

    @Test
    public void tstSetNullBasePathSuccess() {
        ZosConnection connection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        connection.setBasePath(null);
        assertTrue(connection.getBasePath().isEmpty(),
                "BasePath should be empty when set to null");
    }

    @Test
    public void tstEqualsWithBasePathSuccess() {
        ZosConnection connection1 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        ZosConnection connection2 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        connection1.setBasePath(CUSTOM_BASE_PATH);
        connection2.setBasePath(CUSTOM_BASE_PATH);

        assertEquals(connection1, connection2,
                "Connections with same basePath should be equal");
    }

    @Test
    public void tstEqualsWithDifferentBasePathSuccess() {
        ZosConnection connection1 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        ZosConnection connection2 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        connection1.setBasePath(CUSTOM_BASE_PATH);
        connection2.setBasePath("/different/path");

        assertNotEquals(connection1, connection2,
                "Connections with different basePath should not be equal");
    }

    @Test
    public void tstEqualsWithBasePathAndDifferentAuthTypesSuccess() {
        Cookie token = new Cookie("LtpaToken2", "token123");

        ZosConnection basicConnection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        ZosConnection tokenConnection = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .token(token)
                .authType(AuthType.TOKEN)
                .build();

        basicConnection.setBasePath(CUSTOM_BASE_PATH);
        tokenConnection.setBasePath(CUSTOM_BASE_PATH);

        assertNotEquals(basicConnection, tokenConnection,
                "Connections with same basePath but different auth types should not be equal");
    }

    @Test
    public void tstHashCodeWithBasePathSuccess() {
        ZosConnection connection1 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        ZosConnection connection2 = new ZosConnection.Builder()
                .host(HOST)
                .zosmfPort(PORT)
                .user(USER)
                .password(PASSWORD)
                .authType(AuthType.BASIC)
                .build();

        connection1.setBasePath(CUSTOM_BASE_PATH);
        connection2.setBasePath(CUSTOM_BASE_PATH);

        assertEquals(connection1.hashCode(), connection2.hashCode(),
                "Hash codes should be equal for equal connections");
    }

}

