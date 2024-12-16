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
        ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        ZosConnection zc2 = new ZosConnection("test", "zosmfPort", "user", "password");
        assertNotSame(zc1, zc2);
    }

    @Test
    public void tstReferenceEqualsSuccess() {
        ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        ZosConnection zc2 = zc1;
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstEqualsSuccess() {
        ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        ZosConnection zc2 = new ZosConnection("test", "zosmfPort", "user", "password");
        assertEquals(zc1, zc2);
    }

    @Test
    public void tstNotEqualsSuccess() {
        ZosConnection zc1 = new ZosConnection("test", "zosmfPort", "user", "password");
        ZosConnection zc2 = new ZosConnection("test2", "zosmfPort", "user", "password");
        assertNotEquals(zc1, zc2);
    }

}
