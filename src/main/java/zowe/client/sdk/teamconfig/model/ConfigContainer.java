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

import java.util.List;
import java.util.Map;

/**
 * ConfigContainer POJO to act as a container for a parsed Zowe Global Team Configuration file representation
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ConfigContainer {

    /**
     * List of partitions - partition can contain a separate embedded complete Global Team Configuration based on an
     * individual LPAR instance.
     */
    private final List<Partition> partitions;
    /**
     * Schema section value from Zowe Global Team Configuration
     */
    private final String schema;
    /**
     * List of profiles - profile section from Zowe Global Team Configuration
     */
    private final List<Profile> profiles;
    /**
     * Defaults section property values from Zowe Global Team Configuration
     */
    private final Map<String, String> defaults;
    /**
     * AutoStore section value from Zowe Global Team Configuration
     */
    private final Boolean autoStore;

    /**
     * ConfigContainer constructor.
     *
     * @param partitions list of parsed partitions from Zowe Global Team Configuration
     * @param schema     schema value from Zowe Global Team Configuration
     * @param profiles   list of parsed profiles from Zowe Global Team Configuration
     * @param defaults   hashmap of defaults values parsed from Zowe Global Team Configuration
     * @param autoStore  autoStore value from Zowe Global Team Configuration
     * @author Frank Giordano
     */
    public ConfigContainer(List<Partition> partitions, String schema, List<Profile> profiles,
                           Map<String, String> defaults, Boolean autoStore) {
        this.partitions = partitions;
        this.schema = schema;
        this.profiles = profiles;
        this.defaults = defaults;
        this.autoStore = autoStore;
    }

    /**
     * Return autoStore
     *
     * @return autoStore string value from reading and parsing Zowe Global Team Configuration
     * @author Frank Giordano
     */
    public Boolean getAutoStore() {
        return autoStore;
    }

    /**
     * Return hashmap of default values
     *
     * @return default property key/value pairs gathered from reading and parsing Zowe Global Team Configuration
     * @author Frank Giordano
     */
    public Map<String, String> getDefaults() {
        return defaults;
    }

    /**
     * Return list of partitions
     *
     * @return list of partitions gathered from reading and parsing Zowe Global Team Configuration
     * @author Frank Giordano
     */
    public List<Partition> getPartitions() {
        return partitions;
    }

    /**
     * Return list of profiles
     *
     * @return list of profiles gathered from reading and parsing Zowe Global Team Configuration
     * @author Frank Giordano
     */
    public List<Profile> getProfiles() {
        return profiles;
    }

    /**
     * Return schema
     *
     * @return schema string value from reading and parsing Zowe Global Team Configuration
     * @author Frank Giordano
     */
    public String getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "ZoweTeamConfig{" +
                "partitions=" + partitions +
                ", schema=" + schema +
                ", profiles=" + profiles +
                ", defaults=" + defaults +
                ", autoStore=" + autoStore +
                '}';
    }

}
