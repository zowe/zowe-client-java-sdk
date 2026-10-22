/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for Profile.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class ProfileTest {

    @Test
    void tstProfileToStringMasksSensitivePropertiesSuccess() {
        Map<String, String> properties = Map.of(
                "host", "zos.example.com",
                "port", "443",
                "password", "secretPassword123",
                "tokenValue", "secretToken456",
                "user", "USER1"
        );
        Profile profile = new Profile("zosmf", "zosmf", properties, List.of("password"));

        String str = profile.toString();
        assertFalse(str.contains("secretPassword123"));
        assertFalse(str.contains("secretToken456"));
        assertTrue(str.contains("password=*****"));
        assertTrue(str.contains("tokenValue=*****"));
        assertTrue(str.contains("host=zos.example.com"));
    }

    @Test
    void tstPartitionToStringMasksSensitivePropertiesSuccess() {
        Map<String, String> properties = Map.of(
                "host", "lpar1.example.com",
                "password", "partitionSecret"
        );
        Profile profile = new Profile("zosmf", "zosmf", Map.of("port", "443", "token", "myToken"), List.of());
        Partition partition = new Partition("lpar1", properties, List.of(profile));

        String str = partition.toString();
        assertFalse(str.contains("partitionSecret"));
        assertFalse(str.contains("myToken"));
        assertTrue(str.contains("password=*****"));
        assertTrue(str.contains("token=*****"));
    }

}
