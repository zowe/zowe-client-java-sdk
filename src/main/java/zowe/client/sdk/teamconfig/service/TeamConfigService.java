/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.model.ConfigContainer;
import zowe.client.sdk.teamconfig.model.Partition;
import zowe.client.sdk.teamconfig.model.Profile;
import zowe.client.sdk.utility.ValidateUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TeamConfigService class that provides a service layer to perform Zowe Global Team Configuration processing.
 *
 * @author Frank Giordano
 * @version 7.0
 */
public class TeamConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamConfigService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Create a new TeamConfigService instance.
     */
    public TeamConfigService() {
        // intentionally empty
    }

    /**
     * Return ConfigContainer object container of a parsed Zowe Global Team Configuration file representation.
     *
     * @param config KeyTarConfig object
     * @return ConfigContainer object
     * @throws TeamConfigException error processing team configuration
     * @author Frank Giordano
     */
    public ConfigContainer getTeamConfig(final KeyTarConfig config) throws TeamConfigException {
        ValidateUtils.checkNullParameter(config, "config");
        final JsonNode root;
        try {
            root = MAPPER.readTree(new File(config.getLocation()));
        } catch (IOException e) {
            throw new TeamConfigException("Error reading zowe global team configuration file", e);
        }
        return parseJson(root);
    }

    /**
     * Update a profile's properties in the Zowe Global Team Configuration file on disk.
     * Only the properties present in {@code updatedProperties} are written; existing properties
     * not included in the map are left unchanged.
     *
     * @param config            KeyTarConfig object providing the file location
     * @param profileName       name of the profile to update
     * @param updatedProperties map of property key/value pairs to set on the profile
     * @throws TeamConfigException error reading or writing the team configuration file
     * @author Frank Giordano
     */
    public void updateTeamConfig(final KeyTarConfig config, final String profileName,
                                 final Map<String, String> updatedProperties) throws TeamConfigException {
        ValidateUtils.checkNullParameter(config, "config");
        ValidateUtils.checkIllegalParameter(profileName, "profileName");
        ValidateUtils.checkNullParameter(updatedProperties, "updatedProperties");
        final File file = new File(config.getLocation());
        final JsonNode root;
        try {
            root = MAPPER.readTree(file);
        } catch (IOException e) {
            throw new TeamConfigException("Error reading zowe global team configuration file", e);
        }
        final JsonNode profilesNode = root.path("profiles");
        final JsonNode profileNode = profilesNode.path(profileName);
        if (profileNode.isMissingNode()) {
            throw new TeamConfigException("Profile '" + profileName + "' not found in configuration file");
        }
        final ObjectNode propertiesNode = (ObjectNode) profileNode.path("properties");
        updatedProperties.forEach(propertiesNode::put);
        try {
            MAPPER.writerWithDefaultPrettyPrinter().writeValue(file, root);
        } catch (IOException e) {
            throw new TeamConfigException("Error writing zowe global team configuration file", e);
        }
        LOG.debug("updateTeamConfig profile {} updated with {}", profileName, updatedProperties);
    }

    /**
     * Parse the root JSON node of a Zowe Global Team Configuration file into a ConfigContainer.
     *
     * @param root root JsonNode of the configuration file
     * @return ConfigContainer object
     * @author Frank Giordano
     */
    private ConfigContainer parseJson(final JsonNode root) {
        final String schema = root.path("$schema").asText(null);
        final Boolean autoStore = root.has("autoStore") ? root.get("autoStore").asBoolean() : null;
        final List<Profile> profiles = new ArrayList<>();
        final List<Partition> partitions = new ArrayList<>();
        final Map<String, String> defaults = new HashMap<>();

        final JsonNode defaultsNode = root.path("defaults");
        if (defaultsNode.isObject()) {
            defaultsNode.properties().forEach(e -> defaults.put(e.getKey(), e.getValue().asText()));
        }

        final JsonNode profilesNode = root.path("profiles");
        if (profilesNode.isObject()) {
            profilesNode.properties().forEach(e -> {
                final String name = e.getKey();
                final JsonNode node = e.getValue();
                if (node.has("profiles")) {
                    partitions.add(getPartition(name, node));
                } else {
                    profiles.add(buildProfile(name, node));
                }
            });
        }

        return new ConfigContainer(partitions, schema, profiles, defaults, autoStore);
    }

    /**
     * Build a Partition object from a named JSON node that contains a nested profiles section.
     *
     * @param name partition name
     * @param node JsonNode representing the partition
     * @return Partition object
     * @author Frank Giordano
     */
    private Partition getPartition(final String name, final JsonNode node) {
        LOG.debug("partition found name {} containing: {}", name, node);
        final Map<String, String> properties = parseProperties(node.path("properties"));
        final List<Profile> nestedProfiles = new ArrayList<>();
        final JsonNode nestedProfilesNode = node.path("profiles");
        if (nestedProfilesNode.isObject()) {
            nestedProfilesNode.properties().forEach(e ->
                    nestedProfiles.add(buildProfile(e.getKey(), e.getValue())));
        }
        return new Partition(name, properties, nestedProfiles);
    }

    /**
     * Build a Profile object from a named JSON node.
     *
     * @param name profile name
     * @param node JsonNode representing the profile
     * @return Profile object
     * @author Frank Giordano
     */
    private Profile buildProfile(final String name, final JsonNode node) {
        final String type = node.path("type").asText(null);
        final Map<String, String> properties = parseProperties(node.path("properties"));
        final List<String> secure = parseSecure(node.path("secure"));
        return new Profile(name, type, properties, secure);
    }

    /**
     * Parse a properties JSON node into a key/value map.
     *
     * @param propertiesNode JsonNode representing the properties section
     * @return map of property key/value pairs
     * @author Frank Giordano
     */
    private Map<String, String> parseProperties(final JsonNode propertiesNode) {
        final Map<String, String> map = new HashMap<>();
        if (propertiesNode.isObject()) {
            propertiesNode.properties().forEach(e -> map.put(e.getKey(), e.getValue().asText()));
        }
        return map;
    }

    /**
     * Parse a secure JSON array node into a list of field names.
     *
     * @param secureNode JsonNode representing the secure array
     * @return list of secure field names
     * @author Frank Giordano
     */
    private List<String> parseSecure(final JsonNode secureNode) {
        final List<String> list = new ArrayList<>();
        if (secureNode.isArray()) {
            secureNode.forEach(item -> list.add(item.asText()));
        }
        return list;
    }

}
