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
 * Class containing unit tests for ZosConnection.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosConnectionTest {

    @Test
    public void tstReferenceNotEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstReferenceNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.TOKEN)
                .host("test").password("password").user("user").zosmfPort("zosmfPort")
                .token(new Cookie("hello", "world")).build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.TOKEN)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstReferenceEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstReferenceEqualsWithCookieSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.TOKEN)
                .host("test").password("password").user("user").zosmfPort("zosmfPort")
                .token(new Cookie("hello", "world")).build();
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstEqualsWithCookieSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.TOKEN)
                .host("test").password("password").user("user").zosmfPort("zosmfPort")
                .token(new Cookie("hello", "world")).build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.TOKEN)
                .host("test").password("password").user("user").zosmfPort("zosmfPort")
                .token(new Cookie("hello", "world")).build();
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstNotEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test2").password("password").user("user").zosmfPort("zosmfPort").build();
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstNotEqualsWithCookieSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.TOKEN)
                .host("test").password("password").user("user").zosmfPort("zosmfPort")
                .token(new Cookie("hello", "world1")).build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.TOKEN)
                .host("test").password("password").user("user").zosmfPort("zosmfPort")
                .token(new Cookie("hello", "world")).build();
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstHashCodeMapWithSecondHostDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test2").password("password").user("user").zosmfPort("zosmfPort").build();
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapWithSecondZosmfPortDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort2").build();
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapWithSecondUserDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user2").zosmfPort("zosmfPort").build();
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapWithSecondPasswordDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password2").user("user").zosmfPort("zosmfPort").build();
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapNoDuplicateSuccess() {
        final ZosConnection zc1 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final ZosConnection zc2 = new ZosConnection.Builder(AuthType.BASIC)
                .host("test").password("password").user("user").zosmfPort("zosmfPort").build();
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 1);
    }

}
