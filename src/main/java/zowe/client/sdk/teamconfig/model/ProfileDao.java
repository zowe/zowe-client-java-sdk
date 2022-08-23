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

/**
 * ProfileDao POJO to act as a container for a parsed Zowe Global Team Configuration file representing a profile section
 * with merged properties from other profiles i.e. base, and KeyTarConfig details (containing credential information).
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class ProfileDao {

    /**
     * profile object
     */
    private final Profile profile;
    /**
     * username from OS credential store
     */
    private final String user;
    /**
     * password from OS credential store
     */
    private final String password;
    /**
     * host value from properties section
     */
    private final String host;
    /**
     * port value from properties section
     */
    private final String port;

    public ProfileDao(Profile profile, String user, String password, String host, String port) {
        this.profile = profile;
        this.user = user;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return "ProfileDao{" +
                "profile=" + profile +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                '}';
    }

}
