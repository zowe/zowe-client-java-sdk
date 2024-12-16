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
 * Class containing unit tests for SshConnection.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class SshConnectionTest {

    @Test
    public void tstReferenceNotEqualsSuccess() {
        SshConnection sc1 = new SshConnection("test", 1, "user", "password");
        SshConnection sc2 = new SshConnection("test", 1, "user", "password");
        assertNotSame(sc1, sc2);
    }

    @Test
    public void tstReferenceEqualsSuccess() {
        SshConnection sc1 = new SshConnection("test", 1, "user", "password");
        SshConnection sc2 = sc1;
        assertEquals(sc1, sc2);
    }

    @Test
    public void tstEqualsSuccess() {
        SshConnection sc1 = new SshConnection("test", 1, "user", "password");
        SshConnection sc2 = new SshConnection("test", 1, "user", "password");
        assertEquals(sc1, sc2);
    }

    @Test
    public void tstNotEqualsSuccess() {
        SshConnection sc1 = new SshConnection("test", 1, "user", "password");
        SshConnection sc2 = new SshConnection("test2", 1, "user", "password");
        assertNotEquals(sc1, sc2);
    }

}
