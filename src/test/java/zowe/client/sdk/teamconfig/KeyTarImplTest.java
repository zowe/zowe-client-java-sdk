/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig;

import org.junit.jupiter.api.Test;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit test for KeyTarImpl.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class KeyTarImplTest {

    /**
     * Simple test double to inject decoded JSON directly
     */
    static class TestKeyTarImpl extends KeyTarImpl {

        private final String testKeyString;

        TestKeyTarImpl(String testKeyString) {
            this.testKeyString = testKeyString;
        }

        @Override
        public void processKey() {
            try {
                java.lang.reflect.Field field =
                        KeyTarImpl.class.getDeclaredField("keyString");
                field.setAccessible(true);
                field.set(this, testKeyString);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    void tstShouldParseValidKeyTarJsonSuccess() throws Exception {
        String json =
                "{"
                        + "\"store1\": {"
                        + "\"profiles.base.properties.user\": \"fred\","
                        + "\"profiles.base.properties.password\": \"secret\""
                        + "}"
                        + "}";

        TestKeyTarImpl keyTar = new TestKeyTarImpl(json);
        keyTar.processKey();

        List<KeyTarConfig> configs = keyTar.getKeyConfigs();

        assertEquals(1, configs.size());
        assertEquals("store1", configs.get(0).getStoreName());
        assertEquals("fred", configs.get(0).getUser());
        assertEquals("secret", configs.get(0).getPassword());
    }

    @SuppressWarnings("ExtractMethodRecommender")
    @Test
    void tstShouldHandleMultipleStoresSuccess() throws Exception {
        String json =
                "{"
                        + "\"store1\": {"
                        + "\"profiles.base.properties.user\": \"user1\","
                        + "\"profiles.base.properties.password\": \"pass1\""
                        + "},"
                        + "\"store2\": {"
                        + "\"profiles.base.properties.user\": \"user2\","
                        + "\"profiles.base.properties.password\": \"pass2\""
                        + "}"
                        + "}";

        TestKeyTarImpl keyTar = new TestKeyTarImpl(json);
        keyTar.processKey();

        List<KeyTarConfig> configs = keyTar.getKeyConfigs();

        assertEquals(2, configs.size());
        assertEquals("store1", configs.get(0).getStoreName());
        assertEquals("user1", configs.get(0).getUser());
        assertEquals("pass1", configs.get(0).getPassword());
        assertEquals("store2", configs.get(1).getStoreName());
        assertEquals("user2", configs.get(1).getUser());
        assertEquals("pass2", configs.get(1).getPassword());
    }

    @Test
    void tstShouldSkipNonObjectStoreNodesSuccess() throws Exception {
        String json =
                "{"
                        + "\"store1\": \"unexpected\","
                        + "\"store2\": {"
                        + "\"profiles.base.properties.user\": \"user\","
                        + "\"profiles.base.properties.password\": \"pass\""
                        + "}"
                        + "}";

        TestKeyTarImpl keyTar = new TestKeyTarImpl(json);
        keyTar.processKey();

        List<KeyTarConfig> configs = keyTar.getKeyConfigs();

        assertEquals(1, configs.size());
        assertEquals("store2", configs.get(0).getStoreName());
        assertEquals("user", configs.get(0).getUser());
        assertEquals("pass", configs.get(0).getPassword());
    }

    @Test
    void tstShouldThrowWhenRequiredFieldMissingFailure() {
        String json =
                "{"
                        + "\"store1\": {"
                        + "\"profiles.base.properties.user\": \"user\""
                        + "}"
                        + "}";

        TestKeyTarImpl keyTar = new TestKeyTarImpl(json);
        keyTar.processKey();

        TeamConfigException ex = assertThrows(
                TeamConfigException.class,
                keyTar::getKeyConfigs
        );

        assertTrue(ex.getMessage().contains("Missing or invalid KeyTar field"));
    }

    @Test
    void tstShouldFailIfGettersCalledBeforeProcessKeyFailure() {
        KeyTarImpl keyTar = new KeyTarImpl();

        assertThrows(
                NullPointerException.class,
                keyTar::getKeyTarValue
        );

        assertThrows(
                NullPointerException.class,
                keyTar::getKeyConfigs
        );
    }

}
