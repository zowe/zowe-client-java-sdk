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

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Class containing unit tests for ZosConnectionFactory.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosConnectionCreateTest {

    @Test
    public void tstReferenceNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstReferenceNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        final ZosConnection zc2 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello1", "world"));
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstReferenceEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstReferenceEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        final ZosConnection zc2 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test2", "zosmfPort", "user", "password");
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world1"));
        final ZosConnection zc2 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstHashCodeMapWithSecondHostDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test2", "zosmfPort", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstHashCodeMapWithSecondZosmfPortDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort2", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstHashCodeMapWithSecondUserDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user2", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstHashCodeMapWithSecondPasswordDifferentSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password2");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstHashCodeMapNoDuplicateSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(1, zcs.size());
    }

    @Test
    public void tstBasePathSetGetSuccess() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        connection.setBasePath("/custom/path");
        assertEquals("/custom/path",
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : ""));
    }

    @Test
    public void tstBasePathDefaultEmptySuccess() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        assertTrue(connection.getBasePath().isEmpty());
    }

    @Test
    public void tstBasePathEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path");
        zc2.setBasePath("/custom/path");
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstBasePathNotEqualsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path1");
        zc2.setBasePath("/custom/path2");
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstBasePathHashCodeMapWithDifferentPathsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path1");
        zc2.setBasePath("/custom/path2");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(2, zcs.size());
    }

    @Test
    public void tstBasePathHashCodeMapWithSamePathsSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = ZosConnectionFactory
                .createBasicConnection("test", "zosmfPort", "user", "password");
        zc1.setBasePath("/custom/path");
        zc2.setBasePath("/custom/path");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(1, zcs.size());
    }

    @Test
    public void tstBasePathTokenConnectionSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createTokenConnection("test", "zosmfPort", new Cookie("hello", "world"));
        zc1.setBasePath("/custom/path");
        assertEquals("/custom/path", (zc1.getBasePath().isPresent() ? zc1.getBasePath().get() : ""));
    }

    @Test
    public void tstBasePathSslConnectionSuccess() {
        final ZosConnection zc1 = ZosConnectionFactory
                .createSslConnection("test", "zosmfPort", "certPath", "certPassword");
        zc1.setBasePath("/custom/path");
        assertEquals("/custom/path", (zc1.getBasePath().isPresent() ? zc1.getBasePath().get() : ""));
    }

}