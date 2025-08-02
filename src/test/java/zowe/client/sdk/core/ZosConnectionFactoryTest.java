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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit tests for ZosConnectionFactory.
 *
 * @author Frank Giordano
 */
class ZosConnectionFactoryTest {

    @Test
    public void tstBasicZosConnectionInvalidStates() {
        assertThrows(IllegalStateException.class, () ->
            ZosConnectionFactory.createBasicConnection(null, "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("", "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", null, "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", " ", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", null, "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", "", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", "user", null)
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createBasicConnection("host", "port", "user", "")
        );
    }

    @Test
    public void tstSslZosConnectionInvalidStates() {
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection(null, "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("", "port", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", null, "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", " ", "user", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", null, "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", "", "password")
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", "user", null)
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createSslConnection("host", "port", "user", "")
        );
    }

    @Test
    public void tstTokenZosConnectionInvalidStates() {
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection(null, "port", new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("", "port", new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("host", null, new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("host", " ", new Cookie("foo", "bar"))
        );
        assertThrows(IllegalStateException.class, () ->
                ZosConnectionFactory.createTokenConnection("host", "port", null)
        );
    }
}