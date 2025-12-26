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
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for ZosConnectionFactory.
 *
 * @version 5.0
 */
public class ZosConnectionFactoryTest {

    @Test
    public void tstZosConnectionFactoryReferenceNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryReferenceNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createTokenConnection(
                "test", "443", new Cookie("hello", "world"));
        final ZosConnection zc2 = ZosConnectionFactory.createTokenConnection(
                "test", "443", new Cookie("hello1", "world"));
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryReferenceEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryReferenceEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createTokenConnection(
                "test", "443", new Cookie("hello", "world"));
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createTokenConnection(
                "test", "1", new Cookie("hello", "world"));
        final ZosConnection zc2 = ZosConnectionFactory.createTokenConnection(
                "test", "1", new Cookie("hello", "world"));
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "1", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test2", "1", "user", "password");
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createTokenConnection(
                "test", "443", new Cookie("hello", "world1"));
        final ZosConnection zc2 = ZosConnectionFactory.createTokenConnection(
                "test", "443", new Cookie("hello", "world"));
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryHashCodeMapWithSecondHostDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test2", "443", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstZosConnectionFactoryHashCodeMapWithSecondZosmfPortDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "4432", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstZosConnectionFactoryHashCodeMapWithSecondUserDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user2", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstZosConnectionFactoryHashCodeMapWithSecondPasswordDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password2");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstZosConnectionFactoryHashCodeMapNoDuplicateSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(1, zcs.size());
    }

    @Test
    public void tstZosConnectionFactoryBasePathSetGetSuccess() {
        final ZosConnection connection = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path");
        assertEquals("/custom/path",
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : ""));
    }

    @Test
    public void tstZosConnectionFactoryBasePathDefaultEmptySuccess() {
        final ZosConnection connection = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password");
        assertTrue(connection.getBasePath().isEmpty());
    }

    @Test
    public void tstZosConnectionFactoryBasePathEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path");
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryBasePathNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path1");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path2");
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstZosConnectionFactoryBasePathHashCodeMapWithDifferentPathsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path1");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path2");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstZosConnectionFactoryBasePathHashCodeMapWithSamePathsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path");
        final ZosConnection zc2 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "/custom/path");
        final ZosConnection zc3 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "custom/path/");
        final ZosConnection zc4 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "custom/path");
        final ZosConnection zc5 = ZosConnectionFactory.createBasicConnection(
                "test", "443", "user", "password", "\\custom\\path");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        zcs.put(zc3, 3);
        zcs.put(zc4, 4);
        zcs.put(zc5, 5);
        assertEquals(1, zcs.size());
    }

    @Test
    public void tstZosConnectionFactoryBasePathTokenSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createTokenConnection(
                "test", "443", new Cookie("hello", "world"), "/custom/path");
        assertEquals("/custom/path", (zc1.getBasePath().isPresent() ? zc1.getBasePath().get() : ""));
    }

    @Test
    public void tstZosConnectionFactoryBasePathSslSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory.createSslConnection(
                "test", "4431", "certPath", "certPassword", "/custom/path");
        assertEquals("/custom/path", (zc1.getBasePath().isPresent() ? zc1.getBasePath().get() : ""));
    }

    @Test
    public void tstZosConnectionFactoryBasicInvalidStatesFailure() {
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                null, "443", "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "", "443", "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", null, "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", " ", "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", "443", null, "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", "443", "", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", "443", "user", null));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", "443", "user", ""));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", "443", "user", "password", null));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createBasicConnection(
                "host", "443", "user", "password", ""));
    }

    @Test
    public void tstZosConnectionFactorySslInvalidStatesFailure() {
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                null, "443", "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "", "443", "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", null, "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", " ", "user", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", "443", null, "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", "443", "", "password"));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", "443", "user", null));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", "443", "user", ""));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", "443", "user", "password", null));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createSslConnection(
                "host", "443", "user", "password", ""));
    }

    @Test
    public void tstZosConnectionFactoryTokenInvalidStatesFailure() {
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createTokenConnection(
                null, "443", new Cookie("foo", "bar")));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createTokenConnection(
                "", "443", new Cookie("foo", "bar")));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createTokenConnection(
                "host", null, new Cookie("foo", "bar")));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createTokenConnection(
                "host", " ", new Cookie("foo", "bar")));
        assertThrows(NullPointerException.class, () -> ZosConnectionFactory.createTokenConnection(
                "host", "443", null));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createTokenConnection(
                "host", "443", new Cookie("foo", "bar"), null));
        assertThrows(IllegalArgumentException.class, () -> ZosConnectionFactory.createTokenConnection(
                "host", "443", new Cookie("foo", "bar"), ""));
    }

}
