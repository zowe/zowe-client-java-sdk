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

public class Partition {

    private final String name;
    private final Map<String, String> properties;
    private final List<Profile> profiles;

    public Partition(String name, Map<String, String> properties, List<Profile> profiles) {
        this.name = name;
        this.properties = properties;
        this.profiles = profiles;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

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
