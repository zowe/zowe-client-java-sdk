/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.keytar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class containing unit test for KeyTarConfig.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class KeyTarConfigTest {

    @Test
    void tstCreateKeyTarConfigSuccess() {
        KeyTarConfig config = new KeyTarConfig(
                "/tmp/team_config.json",
                "USER1",
                "PASSWORD",
                "keytar-store");

        assertEquals("/tmp/team_config.json", config.getLocation());
        assertEquals("USER1", config.getUser());
        assertEquals("USER1", config.getUserName());
        assertEquals("PASSWORD", config.getPassword());
        assertEquals("keytar-store", config.getStoreName());
    }

    @Test
    void tstReturnToStringWithMaskedPasswordSuccess() {
        KeyTarConfig config = new KeyTarConfig(
                "/tmp/team_config.json",
                "USER1",
                "PASSWORD",
                "keytar-store");

        assertEquals(
                "KeyTarConfig{location='/tmp/team_config.json', userName='USER1', password='*****'}",
                config.toString());
    }

    @Test
    void tstReturnToStringWithEmptyPasswordSuccess() {
        KeyTarConfig config = new KeyTarConfig(
                "/tmp/team_config.json",
                "USER1",
                "",
                "keytar-store");

        assertEquals(
                "KeyTarConfig{location='/tmp/team_config.json', userName='USER1', password=''}",
                config.toString());
    }

    @Test
    void tstReturnToStringWithNullPasswordSuccess() {
        KeyTarConfig config = new KeyTarConfig(
                "/tmp/team_config.json",
                "USER1",
                null,
                "keytar-store");

        assertEquals(
                "KeyTarConfig{location='/tmp/team_config.json', userName='USER1', password=''}",
                config.toString());
    }

    @Test
    void tstReturnToStringWithNullUserNameSuccess() {
        KeyTarConfig config = new KeyTarConfig(
                "/tmp/team_config.json",
                null,
                "PASSWORD",
                "keytar-store");

        assertEquals(
                "KeyTarConfig{location='/tmp/team_config.json', userName='', password='*****'}",
                config.toString());
    }

}
