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
     * list of partitions - partition can contain a separate embedded complete Global Team Configuration based on an
     * individual LPAR instance.
     */
    private final List<Partition> partitions;
    /**
     * schema section
     */
    private final String schema;
    /**
     * list of profiles - profile section
     */
    private final List<Profile> profiles;
    /**
     * defaults section
     */
    private final Map<String, String> defaults;
    /**
     * autoStore section
     */
    private final Boolean autoStore;

    public ConfigContainer(List<Partition> partitions, String schema, List<Profile> profiles,
                           Map<String, String> defaults, Boolean autoStore) {
        this.partitions = partitions;
        this.schema = schema;
        this.profiles = profiles;
        this.defaults = defaults;
        this.autoStore = autoStore;
    }

    public List<Partition> getPartitions() {
        return partitions;
    }

    public String getSchema() {
        return schema;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public Map<String, String> getDefaults() {
        return defaults;
    }

    public Boolean getAutoStore() {
        return autoStore;
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
