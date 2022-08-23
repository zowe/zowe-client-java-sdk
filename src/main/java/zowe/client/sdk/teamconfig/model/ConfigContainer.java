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

public class ConfigContainer {

    private final List<Partition> partitions;
    private final String schema;
    private final List<Profile> profiles;
    private final Map<String, String> defaults;
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
