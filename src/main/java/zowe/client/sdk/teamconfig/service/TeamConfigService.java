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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarConfig;
import zowe.client.sdk.teamconfig.model.ConfigContainer;
import zowe.client.sdk.teamconfig.model.Partition;
import zowe.client.sdk.teamconfig.model.Profile;
import zowe.client.sdk.teamconfig.types.SectionType;
import zowe.client.sdk.utility.JsonUtils;
import zowe.client.sdk.utility.ValidateUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * TeamConfigService class that provides a service layer to perform Zowe Global Team Configuration processing.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class TeamConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(TeamConfigService.class);

    /**
     * Create a new TeamConfigService instance.
     */
    public TeamConfigService() {
        // intentionally empty
    }

    /**
     * Parse a JSON representation of a Zowe Global Team Configuration partition section.
     *
     * @param name       partition name
     * @param jsonObject JSONObject object
     * @return Partition object
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    private Partition getPartition(final String name, final JSONObject jsonObject) throws TeamConfigException {
        final Set<String> keyObjs = jsonObject.keySet();
        final List<Profile> profiles = new ArrayList<>();
        Map<String, String> properties = new HashMap<>();
        LOG.debug("partition found name {} containing {}:", name, jsonObject);
        for (final String keyObj : keyObjs) {
            if (SectionType.PROFILES.getValue().equals(keyObj)) {
                JSONObject jsonProfileObj = (JSONObject) jsonObject.get(SectionType.PROFILES.getValue());
                final Set<String> jsonProfileKeys = jsonProfileObj.keySet();
                for (final String profileKeyVal : jsonProfileKeys) {
                    final JSONObject profileTypeJsonObj = (JSONObject) jsonProfileObj.get(profileKeyVal);
                    profiles.add(new Profile(profileKeyVal,
                            (String) profileTypeJsonObj.get("type"),
                            (JSONObject) profileTypeJsonObj.get("properties"),
                            (JSONArray) profileTypeJsonObj.get("secure")));
                }
            } else if ("properties".equalsIgnoreCase(keyObj)) {
                try {
                    properties = JsonUtils.parseMap((JSONObject) jsonObject.get(keyObj));
                } catch (JsonProcessingException e) {
                    throw new TeamConfigException("Error parsing properties", e);
                }
            }
        }
        return new Partition(name, properties, profiles);
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
        final JSONParser parser = new JSONParser();
        Object obj;
        try {
            obj = parser.parse(new FileReader(config.getLocation()));
        } catch (IOException | ParseException e) {
            throw new TeamConfigException("Error reading zowe global team configuration file", e);
        }
        return parseJson((JSONObject) obj);
    }

    /**
     * Determine if JSON contains a partition section next.
     *
     * @param profileKeyObj partition name
     * @return boolean true or false
     * @author Frank Giordano
     */
    private boolean isPartition(final Set<String> profileKeyObj) {
        final Iterator<String> itr = profileKeyObj.iterator();
        if (itr.hasNext()) {
            String keyVal = itr.next();
            return SectionType.PROFILES.getValue().equals(keyVal);
        } else {
            throw new IllegalStateException("TeamConfig profile type detail missing in profile section.");
        }
    }

    /**
     * Parse a JSON representation of a Zowe Global Team Configuration file.
     *
     * @param jsonObj JSONObject object
     * @return ConfigContainer object
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    private ConfigContainer parseJson(final JSONObject jsonObj) throws TeamConfigException {
        String schema = null;
        Boolean autoStore = null;
        final List<Profile> profiles = new ArrayList<>();
        final Map<String, String> defaults = new HashMap<>();
        final List<Partition> partitions = new ArrayList<>();

        final Set<String> jsonSectionKeys = jsonObj.keySet();
        for (final String keySectionVal : jsonSectionKeys) {
            if (SectionType.$SCHEMA.getValue().equals(keySectionVal)) {
                schema = (String) jsonObj.get(SectionType.$SCHEMA.getValue());
            } else if (SectionType.PROFILES.getValue().equals(keySectionVal)) {
                final JSONObject jsonProfileObj = (JSONObject) jsonObj.get(SectionType.PROFILES.getValue());
                final Set<String> jsonProfileKeys = jsonProfileObj.keySet();
                for (final String profileKeyVal : jsonProfileKeys) {
                    JSONObject profileTypeJsonObj = (JSONObject) jsonProfileObj.get(profileKeyVal);
                    final Set<String> isEmbeddedKeyProfile = profileTypeJsonObj.keySet();
                    if (isPartition(isEmbeddedKeyProfile)) {
                        partitions.add(getPartition(profileKeyVal, profileTypeJsonObj));
                    } else {
                        profiles.add(new Profile(profileKeyVal,
                                (String) profileTypeJsonObj.get("type"),
                                (JSONObject) profileTypeJsonObj.get("properties"),
                                (JSONArray) profileTypeJsonObj.get("secure")));
                    }
                }
            } else if (SectionType.DEFAULTS.getValue().equals(keySectionVal)) {
                final JSONObject keyValues = (JSONObject) jsonObj.get(SectionType.DEFAULTS.getValue());
                for (final Object defaultKeyVal : keyValues.keySet()) {
                    final String key = (String) defaultKeyVal;
                    final String value = (String) keyValues.get(key);
                    defaults.put(key, value);
                }
            } else if (SectionType.AUTOSTORE.getValue().equals(keySectionVal)) {
                autoStore = (boolean) jsonObj.get(SectionType.AUTOSTORE.getValue());
            }
        }

        return new ConfigContainer(partitions, schema, profiles, defaults, autoStore);
    }

}
