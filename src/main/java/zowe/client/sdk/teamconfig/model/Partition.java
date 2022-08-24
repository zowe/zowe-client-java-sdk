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
 * @version 1.0
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
     * @param profiles   list oof Profiles
     * @author Frank Giordano
     */
    public Partition(String name, Map<String, String> properties, List<Profile> profiles) {
        this.name = name;
        this.properties = properties;
        this.profiles = profiles;
    }

    /**
     * Return name
     *
     * @author Frank Giordano
     */
    public String getName() {
        return name;
    }

    /**
     * Return hashmap of property values
     *
     * @author Frank Giordano
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Return list of Profiles
     *
     * @author Frank Giordano
     */
    public List<Profile> getProfiles() {
        return profiles;
    }

    @Override
    public String toString() {
        return "Partition{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
                ", profiles=" + profiles +
                '}';
    }

}
