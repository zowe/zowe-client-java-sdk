/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.teamconfig.keytar;

/**
 * KeyTarConfig POJO to act as a container for a parsed Keytar json object
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class KeyTarConfig {

    /**
     * represents a string value of Zowe Global Team Configuration file name and path location
     */
    private final String location;
    /**
     * represents a string value of a username contained with the OS credential store
     */
    private final String userName;
    /**
     * represents a string value of a password contained with the OS credential store
     */
    private final String password;

    public KeyTarConfig(String location, String userName, String password) {
        this.location = location;
        this.userName = userName;
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "KeyTarConfig{" +
                "location='" + location + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
