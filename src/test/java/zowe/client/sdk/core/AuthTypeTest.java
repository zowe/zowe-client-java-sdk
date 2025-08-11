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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for AuthType.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class AuthTypeTest {

    @Test
    public void tstAuthTypeEnumValuesSuccess() {
        AuthType[] types = AuthType.values();
        assertEquals(3, types.length, "There should be exactly 3 auth types");
        assertArrayEquals(
                new AuthType[]{AuthType.BASIC, AuthType.TOKEN, AuthType.SSL},
                types,
                "Auth types should match expected values in correct order"
        );
    }

    @Test
    public void tstAuthTypeEnumValuesExistSuccess() {
        assertNotNull(AuthType.BASIC, "BASIC auth type should exist");
        assertNotNull(AuthType.TOKEN, "TOKEN auth type should exist");
        assertNotNull(AuthType.SSL, "SSL auth type should exist");
    }

    @Test
    public void tstAuthTypeEnumValueOfSuccess() {
        assertEquals(AuthType.BASIC, AuthType.valueOf("BASIC"), "Should get BASIC auth type");
        assertEquals(AuthType.TOKEN, AuthType.valueOf("TOKEN"), "Should get TOKEN auth type");
        assertEquals(AuthType.SSL, AuthType.valueOf("SSL"), "Should get SSL auth type");
    }

    @Test
    public void tstAuthTypeEnumToStringSuccess() {
        assertEquals("BASIC", AuthType.BASIC.toString(), "BASIC toString should match");
        assertEquals("TOKEN", AuthType.TOKEN.toString(), "TOKEN toString should match");
        assertEquals("SSL", AuthType.SSL.toString(), "SSL toString should match");
    }

    @Test
    public void tstAuthTypeInvalidValueOfFailure() {
        assertThrows(IllegalArgumentException.class,
                () -> AuthType.valueOf("INVALID"),
                "Should throw IllegalArgumentException for invalid auth type"
        );
    }

}

