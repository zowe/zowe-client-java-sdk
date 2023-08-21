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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;

import java.util.Map;

/**
 * Profile POJO to act as a container for a parsed Zowe Global Team Configuration file representing a profile section.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class Profile {

    /**
     * Profile name
     */
    private final String name;

    /**
     * Profile secure json object
     */
    private final JSONArray secure;

    /**
     * Profile property values
     */
    private final Map<String, String> properties;

    /**
     * Partition constructor.
     *
     * @param name   profile name
     * @param obj    json object of property values within profile section from Zowe Global Team Configuration
     * @param secure jsonarray value of secure section
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    public Profile(final String name, final JSONObject obj, final JSONArray secure) {
        this.name = name;
        this.secure = secure;
        properties = (Map<String, String>) JsonParseFactory.buildParser(ParseType.PROPS)
                .setJsonObject(obj).parseResponse();
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
     * @return secure Json object
     */
    public JSONArray getSecure() {
        return secure;
    }

    /**
     * Return string value representing Profile object
     *
     * @return string representation of Profile
     */
    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", secure=" + secure +
                ", properties=" + properties +
                '}';
    }

}
