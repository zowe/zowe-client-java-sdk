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

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Profile POJO to act as a container for a parsed Zowe Global Team Configuration file representing a profile section.
 *
 * @author Frank Giordano
 * @version 7.0
 */

public class Profile {

    /**
     * Profile name
     */
    private final String name;

    /**
     * Profile type
     */
    private final String type;

    /**
     * Profile property values
     */
    private final Map<String, String> properties;

    /**
     * Profile secure List object
     */
    private final List<String> secure;

    /**
     * Profile constructor
     *
     * @param name       profile name
     * @param type       profile type
     * @param properties property values
     * @param secure     secure List object
     */
    public Profile(final String name,
                   final String type,
                   final Map<String, String> properties,
                   final List<String> secure) {
        this.name = name;
        this.type = type;
        this.properties = properties != null ? properties : Collections.emptyMap();
        this.secure = secure != null ? secure : Collections.emptyList();
    }

    /**
     * Return profile name
     *
     * @return profile name string value
     */
    public String getName() {
        return name;
    }

    /**
     * Return profile type
     *
     * @return profile type string value
     */
    public String getType() {
        return type;
    }

    /**
     * Return hashmap of property values
     *
     * @return profile property key/value pairs
     */
    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Return secure value
     *
     * @return secure List object
     */
    public List<String> getSecure() {
        return secure;
    }

    /**
     * Return string value representing a Profile object
     *
     * @return string representation of Profile
     */
    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", secure=" + secure +
                ", properties=" + properties +
                '}';
    }

}
