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
 * @version 2.0
 */
public class KeyTarConfig {

    /**
     * Represents a string value of Zowe Global Team Configuration file name and path location
     */
    private final String location;

    /**
     * Represents a string value of a username contained within the OS credential store
     */
    private final String userName;

    /**
     * Represents a string value of a password contained within the OS credential store
     */
    private final String password;

    /**
     * KeyTarConfig constructor.
     *
     * @param location location of the Zowe Global Team Configuration filename and path
     * @param userName userName specified from parsed KeyTar keyValue
     * @param password password specified from parsed KeyTar keyValue
     * @author Frank Giordano
     */
    public KeyTarConfig(final String location, final String userName, final String password) {
        this.location = location;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Return location
     *
     * @return location string value of zowe configuration filename and path
     * @author Frank Giordano
     */
    public String getLocation() {
        return location;
    }

    /**
     * Return password
     *
     * @return password string value from OS credential store
     * @author Frank Giordano
     */
    public String getPassword() {
        return password;
    }

    /**
     * Return userName
     *
     * @return userName string value from OS credential store
     * @author Frank Giordano
     */
    public String getUserName() {
        return userName;
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
