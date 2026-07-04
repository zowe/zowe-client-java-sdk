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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import zowe.client.sdk.teamconfig.model.ConfigContainer;
import zowe.client.sdk.teamconfig.model.Profile;
import zowe.client.sdk.teamconfig.service.TeamConfigService;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class containing unit tests for TeamConfigService.parseJson method.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class TeamConfigServiceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Strip single-line comments (# ...) from a JSON-like string so it can be parsed as valid JSON.
     */
    private static String stripComments(final String input) {
        return input.lines()
                .map(line -> {
                    final int idx = line.indexOf('#');
                    return idx >= 0 ? line.substring(0, idx) : line;
                })
                .collect(java.util.stream.Collectors.joining("\n"));
    }

    /**
     * Invoke the private parseJson method on TeamConfigService via reflection.
     */
    private ConfigContainer invokeParseJson(final JsonNode root) throws Exception {
        final TeamConfigService service = new TeamConfigService();
        final Method method = TeamConfigService.class.getDeclaredMethod("parseJson", JsonNode.class);
        method.setAccessible(true);
        return (ConfigContainer) method.invoke(service, root);
    }

    @Test
    public void tstParseJsonFlatProfilesConfig() throws Exception {
        final String json = stripComments(
                "{\n" +
                        "  \"$schema\": \"./zowe.schema.json\",\n" +
                        "  \"profiles\": {\n" +
                        "    \"zosmf\": {\n" +
                        "      \"type\": \"zosmf\",\n" +
                        "      \"properties\": {\n" +
                        "        \"port\": 443\n" +
                        "      },\n" +
                        "      \"secure\": []\n" +
                        "    },\n" +
                        "    \"tso\": {\n" +
                        "      \"type\": \"tso\",\n" +
                        "      \"properties\": {\n" +
                        "        \"account\": \"\",\n" +
                        "        \"codePage\": \"1047\",\n" +
                        "        \"logonProcedure\": \"IZUFPROC\"\n" +
                        "      },\n" +
                        "      \"secure\": []\n" +
                        "    },\n" +
                        "    \"ssh\": {\n" +
                        "      \"type\": \"ssh\",\n" +
                        "      \"properties\": {\n" +
                        "        \"port\": 22\n" +
                        "      },\n" +
                        "      \"secure\": []\n" +
                        "    },\n" +
                        "    \"rse\": {\n" +
                        "      \"type\": \"rse\",\n" +
                        "      \"properties\": {\n" +
                        "        \"port\": 6800,\n" +
                        "        \"basePath\": \"rseapi\",\n" +
                        "        \"protocol\": \"https\"\n" +
                        "      },\n" +
                        "      \"secure\": []\n" +
                        "    },\n" +
                        "    # common properties can be defined in the base profile instead of defining them in every profile\n" +
                        "    \"base\": {\n" +
                        "      \"type\": \"base\",\n" +
                        "      \"properties\": {\n" +
                        "        \"host\": \"myzos.ibm.com\",\n" +
                        "        # reject connection if self-signed certificate is used\n" +
                        "        \"rejectUnauthorized\": true\n" +
                        "      },\n" +
                        "      # these secure fields will determine the values written to and stored in the OS credential manager.\n" +
                        "      \"secure\": [\"user\", \"password\"]\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"defaults\": {\n" +
                        "    \"zosmf\": \"zosmf\",\n" +
                        "    \"tso\": \"tso\",\n" +
                        "    \"ssh\": \"ssh\",\n" +
                        "    \"rse\": \"rse\",\n" +
                        "    \"base\": \"base\"\n" +
                        "  },\n" +
                        "  # setting determines if information is written to the file or credential manager\n" +
                        "  \"autoStore\": true\n" +
                        "}"
        );

        final JsonNode root = MAPPER.readTree(json);
        final ConfigContainer container = invokeParseJson(root);

        // validate schema
        assertEquals("./zowe.schema.json", container.getSchema());

        // validate autoStore
        assertTrue(container.isAutoStore());

        // validate defaults
        final Map<String, String> defaults = container.getDefaults();
        assertEquals(5, defaults.size());
        assertEquals("zosmf", defaults.get("zosmf"));
        assertEquals("tso", defaults.get("tso"));
        assertEquals("ssh", defaults.get("ssh"));
        assertEquals("rse", defaults.get("rse"));
        assertEquals("base", defaults.get("base"));

        // validate no partitions
        assertTrue(container.getPartitions().isEmpty());

        // validate profiles
        final List<Profile> profiles = container.getProfiles();
        assertEquals(5, profiles.size());

        // zosmf profile
        final Profile zosmf = profiles.stream().filter(p -> "zosmf".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("zosmf", zosmf.getType());
        assertEquals("443", zosmf.getProperties().get("port"));
        assertTrue(zosmf.getSecure().isEmpty());

        // tso profile
        final Profile tso = profiles.stream().filter(p -> "tso".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("tso", tso.getType());
        assertEquals("", tso.getProperties().get("account"));
        assertEquals("1047", tso.getProperties().get("codePage"));
        assertEquals("IZUFPROC", tso.getProperties().get("logonProcedure"));
        assertTrue(tso.getSecure().isEmpty());

        // ssh profile
        final Profile ssh = profiles.stream().filter(p -> "ssh".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("ssh", ssh.getType());
        assertEquals("22", ssh.getProperties().get("port"));
        assertTrue(ssh.getSecure().isEmpty());

        // rse profile
        final Profile rse = profiles.stream().filter(p -> "rse".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("rse", rse.getType());
        assertEquals("6800", rse.getProperties().get("port"));
        assertEquals("rseapi", rse.getProperties().get("basePath"));
        assertEquals("https", rse.getProperties().get("protocol"));
        assertTrue(rse.getSecure().isEmpty());

        // base profile
        final Profile base = profiles.stream().filter(p -> "base".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("base", base.getType());
        assertEquals("myzos.ibm.com", base.getProperties().get("host"));
        assertEquals("true", base.getProperties().get("rejectUnauthorized"));
        assertEquals(List.of("user", "password"), base.getSecure());
    }

    @Test
    public void tstParseJsonNestedLparConfig() throws Exception {
        final String json = stripComments(
                "{\n" +
                        "  \"$schema\": \"./zowe.schema.json\",\n" +
                        "  \"profiles\": {\n" +
                        "    \"lpar1\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"host\": \"lpar1.com\",\n" +
                        "        \"rejectUnauthorized\": false\n" +
                        "      },\n" +
                        "      \"secure\": [\"user\", \"password\"],\n" +
                        "      # Example of shared credentials across LPAR profiles\n" +
                        "      \"profiles\": {\n" +
                        "        \"zosmf\": {\n" +
                        "          \"type\": \"zosmf\",\n" +
                        "          \"properties\": {\n" +
                        "            \"port\": 443\n" +
                        "          }\n" +
                        "        },\n" +
                        "        \"rse\": {\n" +
                        "          \"type\": \"rse\",\n" +
                        "          \"properties\": {\n" +
                        "            \"port\": 6800,\n" +
                        "            \"basePath\": \"rseapi\",\n" +
                        "            \"protocol\": \"https\"\n" +
                        "          }\n" +
                        "        },\n" +
                        "        \"ssh\": {\n" +
                        "          \"type\": \"ssh\",\n" +
                        "          \"properties\": {\n" +
                        "            \"port\": 22\n" +
                        "          }\n" +
                        "        }\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"lpar2\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"host\": \"lpar2.com\",\n" +
                        "        \"rejectUnauthorized\": false\n" +
                        "      },\n" +
                        "      \"profiles\": {\n" +
                        "        \"zosmf\": {\n" +
                        "          \"type\": \"zosmf\",\n" +
                        "          \"properties\": {\n" +
                        "            \"port\": 443\n" +
                        "          },\n" +
                        "          \"secure\": [\"user\", \"password\"]\n" +
                        "          # Example of credentials that are profile specific\n" +
                        "        },\n" +
                        "        \"ssh\": {\n" +
                        "          \"type\": \"ssh\",\n" +
                        "          \"properties\": {\n" +
                        "            \"port\": 22\n" +
                        "          },\n" +
                        "          \"secure\": [\"user\", \"password\"]\n" +
                        "        }\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"lpar3\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"host\": \"lpar3.com\",\n" +
                        "        \"rejectUnauthorized\": false\n" +
                        "      },\n" +
                        "      \"secure\": [\"user\", \"password\"],\n" +
                        "      \"profiles\": {\n" +
                        "        \"zosmf\": {\n" +
                        "          \"type\": \"zosmf\",\n" +
                        "          \"properties\": {\n" +
                        "            \"port\": 3128,\n" +
                        "            \"protocol\": \"http\"\n" +
                        "          }\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"defaults\": {\n" +
                        "    \"zosmf\": \"lpar1.zosmf\",\n" +
                        "    \"ssh\": \"lpar1.ssh\",\n" +
                        "    \"rse\": \"lpar1.rse\"\n" +
                        "  },\n" +
                        "  \"autoStore\": true\n" +
                        "}"
        );

        final JsonNode root = MAPPER.readTree(json);
        final ConfigContainer container = invokeParseJson(root);

        // validate schema
        assertEquals("./zowe.schema.json", container.getSchema());

        // validate autoStore
        assertTrue(container.isAutoStore());

        // validate defaults
        final Map<String, String> defaults = container.getDefaults();
        assertEquals(3, defaults.size());
        assertEquals("lpar1.zosmf", defaults.get("zosmf"));
        assertEquals("lpar1.ssh", defaults.get("ssh"));
        assertEquals("lpar1.rse", defaults.get("rse"));

        // validate no flat profiles (all are partitions)
        assertTrue(container.getProfiles().isEmpty());

        // validate 3 partitions
        final List<zowe.client.sdk.teamconfig.model.Partition> partitions = container.getPartitions();
        assertEquals(3, partitions.size());

        // --- lpar1 ---
        final zowe.client.sdk.teamconfig.model.Partition lpar1 =
                partitions.stream().filter(p -> "lpar1".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("lpar1.com", lpar1.getProperties().get("host"));
        assertEquals("false", lpar1.getProperties().get("rejectUnauthorized"));

        final List<Profile> lpar1Profiles = lpar1.getProfiles();
        assertEquals(3, lpar1Profiles.size());

        final Profile lpar1Zosmf = lpar1Profiles.stream().filter(p -> "zosmf".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("zosmf", lpar1Zosmf.getType());
        assertEquals("443", lpar1Zosmf.getProperties().get("port"));

        final Profile lpar1Rse = lpar1Profiles.stream().filter(p -> "rse".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("rse", lpar1Rse.getType());
        assertEquals("6800", lpar1Rse.getProperties().get("port"));
        assertEquals("rseapi", lpar1Rse.getProperties().get("basePath"));
        assertEquals("https", lpar1Rse.getProperties().get("protocol"));

        final Profile lpar1Ssh = lpar1Profiles.stream().filter(p -> "ssh".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("ssh", lpar1Ssh.getType());
        assertEquals("22", lpar1Ssh.getProperties().get("port"));

        // --- lpar2 ---
        final zowe.client.sdk.teamconfig.model.Partition lpar2 =
                partitions.stream().filter(p -> "lpar2".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("lpar2.com", lpar2.getProperties().get("host"));
        assertEquals("false", lpar2.getProperties().get("rejectUnauthorized"));

        final List<Profile> lpar2Profiles = lpar2.getProfiles();
        assertEquals(2, lpar2Profiles.size());

        final Profile lpar2Zosmf = lpar2Profiles.stream().filter(p -> "zosmf".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("zosmf", lpar2Zosmf.getType());
        assertEquals("443", lpar2Zosmf.getProperties().get("port"));
        assertEquals(List.of("user", "password"), lpar2Zosmf.getSecure());

        final Profile lpar2Ssh = lpar2Profiles.stream().filter(p -> "ssh".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("ssh", lpar2Ssh.getType());
        assertEquals("22", lpar2Ssh.getProperties().get("port"));
        assertEquals(List.of("user", "password"), lpar2Ssh.getSecure());

        // --- lpar3 ---
        final zowe.client.sdk.teamconfig.model.Partition lpar3 =
                partitions.stream().filter(p -> "lpar3".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("lpar3.com", lpar3.getProperties().get("host"));
        assertEquals("false", lpar3.getProperties().get("rejectUnauthorized"));

        final List<Profile> lpar3Profiles = lpar3.getProfiles();
        assertEquals(1, lpar3Profiles.size());

        final Profile lpar3Zosmf = lpar3Profiles.stream().filter(p -> "zosmf".equals(p.getName())).findFirst().orElseThrow();
        assertEquals("zosmf", lpar3Zosmf.getType());
        assertEquals("3128", lpar3Zosmf.getProperties().get("port"));
        assertEquals("http", lpar3Zosmf.getProperties().get("protocol"));
    }

}
