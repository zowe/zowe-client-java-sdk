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

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Class containing unit tests for ZosConnection.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosConnectionTest {

    @Test
    public void tstReferenceNotEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test", "zosmfPort", "user", "password");
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstReferenceEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test", "zosmfPort", "user", "password");
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstNotEqualsSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test2", "zosmfPort", "user", "password");
        assertNotEquals(zc1, zc2);
    }

    @Test
    public void tstHashCodeMapWithSecondHostDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test2", "zosmfPort", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapWithSecondZosmfPortDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test", "zosmfPort2", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapWithSecondUserDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test", "zosmfPort", "user2", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapWithSecondPasswordDifferentSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test", "zosmfPort", "user", "password2");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 2);
    }

    @Test
    public void tstHashCodeMapNoDuplicateSuccess() {
        final ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        final ZosConnection zc2 = new ZosConnection("test", "zosmfPort", "user", "password");
        final var zcs = new HashMap<ZosConnection, Integer>();
        zcs.put(zc1, 1);
        zcs.put(zc2, 2);
        assertEquals(zcs.size(), 1);
    }

}
