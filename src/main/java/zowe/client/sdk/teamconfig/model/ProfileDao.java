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

public class ProfileDao {

    private final Profile profile;
    private final String user;
    private final String password;
    private final String host;
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
