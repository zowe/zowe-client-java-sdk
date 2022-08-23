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
import zowe.client.sdk.teamconfig.utility.TeamConfigUtils;

import java.util.Map;

/**
 * Profile POJO to act as a container for a parsed Zowe Global Team Configuration file representing a profile section.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class Profile {

    /**
     * profile name
     */
    private final String name;
    /**
     * profile secure json object
     */
    private final JSONArray secure;
    /**
     * profile properties
     */
    private Map<String, String> properties;

    public Profile(String name, JSONObject obj, JSONArray secure) {
        this.name = name;
        this.secure = secure;
        properties = TeamConfigUtils.parseJsonPropsObj(obj);
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public JSONArray getSecure() {
        return secure;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", secure=" + secure +
                ", properties=" + properties +
                '}';
    }

}
