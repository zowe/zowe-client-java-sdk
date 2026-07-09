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
 * KeyTarConfig POJO to act as a container for a parsed Keytar JSON object
 *
 * @author Frank Giordano
 * @version 7.0
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
     * Represents a string value of the OS credential store name
     */
    private final String storeName;

    /**
     * KeyTarConfig constructor
     *
     * @param location  location of the Zowe Global Team Configuration filename and path
     * @param userName  userName specified from parsed KeyTar keyValue
     * @param password  password specified from parsed KeyTar keyValue
     * @param storeName store name specified in the OS credential store
     * @author Frank Giordano
     */
    public KeyTarConfig(final String location,
                        final String userName,
                        final String password,
                        final String storeName) {
        this.location = location;
        this.userName = userName;
        this.password = password;
        this.storeName = storeName;
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
     * Return username
     *
     * @return username string value from OS credential store
     */
    public String getUser() {
        return userName;
    }

    /**
     * Return userName
     *
     * @return userName string value from OS credential store
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Return storeName
     *
     * @return storeName string value from OS credential store
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * Return string value representing KeyTarConfig object
     *
     * @return string representation of KeyTarConfig
     */
    @Override
    public String toString() {
        return "KeyTarConfig{" +
                "location='" + location + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
