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
 * Partition POJO to act as a container for a parsed Zowe Global Team Configuration file representing a partition section.
 * A partition can contain a separate complete Global Team Configuration based on an individual LPAR instance.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class Partition {

    /**
     * Partition name
     */
    private final String name;

    /**
     * Partition property values
     */
    private final Map<String, String> properties;

    /**
     * Partition profiles
     */
    private final List<Profile> profiles;

    /**
     * Partition constructor.
     *
     * @param name       partition name
     * @param properties hashmap of property values
     * @param profiles   list of Profiles
     * @author Frank Giordano
     */
    public Partition(final String name, final Map<String, String> properties, final List<Profile> profiles) {
        this.name = name;
        this.properties = properties;
        this.profiles = profiles;
    }

    /**
     * Return name
     *
     * @return partition string value
     */
    public String getName() {
        return name;
    }

    /**
     * Return list of Profiles
     *
     * @return list of profiles
     */
    public List<Profile> getProfiles() {
        return profiles;
    }

    /**
     * Return hashmap of property values
     *
     * @return partition property key/values pairs
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Return string value representing Partition object
     *
     * @return string representation of Partition
     */
    @Override
    public String toString() {
        return "Partition{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
                ", profiles=" + profiles +
                '}';
    }

}
