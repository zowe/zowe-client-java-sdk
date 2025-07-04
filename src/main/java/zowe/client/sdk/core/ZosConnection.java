/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.core;

import kong.unirest.core.Cookie;

import java.util.Objects;

/**
 * Z/OSMF Connection information placeholder
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosConnection {

    /**
     * Host name pointing to the backend z/OS instance
     */
    private final String host;
    /**
     * Host z/OSMF port number pointing to the backend z/OS instance
     */
    private final String zosmfPort;
    /**
     * Host username with access to a backend z/OS instance
     */
    private final String user;
    /**
     * Host username's password with access to backend z/OS instance
     */
    private final String password;
    /**
     * The certificate password for SSL authentication
     */
    private final String certPassword;
    /**
     * Cookie value set to use as an authentication token for http call
     */
    private final Cookie cookie;
    /**
     * Path with filename denoting the certificate for SSL authentication usage
     */
    private final String certFilePath;
    /**
     * AuthType: CLASSIC, TOKEN or SSL
     */
    private final AuthType authType;

    /**
     * Private constructor used by the Builder
     *
     * @param builder Builder instance containing configuration
     */
    private ZosConnection(ZosConnection.Builder builder) {
        this.host = builder.host;
        this.zosmfPort = builder.zosmfPort;
        this.user = builder.user;
        this.password = builder.password;
        this.certPassword = builder.certPassword;
        this.cookie = builder.cookie;
        this.certFilePath = builder.certFilePath;
        this.authType = builder.authType;
    }

    /**
     * Retrieve authType
     *
     * @return AuthType enum
     */
    public AuthType getAuthType() {
        return authType;
    }

    /**
     * Retrieve certFilePath
     *
     * @return string value
     */
    public String getCertFilePath() {
        return certFilePath;
    }

    /**
     * Retrieve cookie
     *
     * @return Cookie object
     */
    public Cookie getCookie() {
        return cookie;
    }

    /**
     * Retrieve password
     *
     * @return string value
     */
    public String getPassword() {
        return password;
    }

    /**
     * Retrieve certPassword
     *
     * @return string value
     */
    public String getCertPassword() {
        return certPassword;
    }

    /**
     * Retrieve user
     *
     * @return string value
     */
    public String getUser() {
        return user;
    }

    /**
     * Retrieve zosmfPort
     *
     * @return string value
     */
    public String getZosmfPort() {
        return zosmfPort;
    }

    /**
     * Retrieve
     *
     * @return string value
     */
    public String getHost() {
        return host;
    }

    /**
     * Equals method. Use all members for equality.
     *
     * @param obj object
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZosConnection other = (ZosConnection) obj;
        if (cookie != null) {
            return Objects.equals(host, other.host) &&
                    Objects.equals(zosmfPort, other.zosmfPort) &&
                    Objects.equals(user, other.user) &&
                    Objects.equals(password, other.password) &&
                    Objects.equals(certPassword, other.certPassword) &&
                    Objects.equals(cookie.getName(), other.cookie.getName()) &&
                    Objects.equals(cookie.getValue(), other.cookie.getValue()) &&
                    Objects.equals(certFilePath, other.certFilePath) &&
                    Objects.equals(authType, other.authType);
        } else {
            return Objects.equals(host, other.host) &&
                    Objects.equals(zosmfPort, other.zosmfPort) &&
                    Objects.equals(user, other.user) &&
                    Objects.equals(password, other.password) &&
                    Objects.equals(certPassword, other.certPassword) &&
                    Objects.equals(cookie, other.cookie) &&
                    Objects.equals(certFilePath, other.certFilePath) &&
                    Objects.equals(authType, other.authType);
        }
    }

    /**
     * Hashcode method. Use all members for hashing.
     *
     * @return int value
     */
    @Override
    public int hashCode() {
        return Objects.hash(host, zosmfPort, user, password, certPassword, cookie, certFilePath, authType);
    }

    /**
     * Builder class for ZosConnection
     */
    public static class Builder {

        /**
         * Host name pointing to the backend z/OS instance
         */
        private String host;
        /**
         * Host z/OSMF port number pointing to the backend z/OS instance
         */
        private String zosmfPort;
        /**
         * Host username with access to a backend z/OS instance
         */
        private String user;
        /**
         * Host username's password with access to backend z/OS instance
         */
        private String password;
        /**
         * The certificate password for SSL authentication
         */
        private String certPassword;
        /**
         * Cookie value set to use as an authentication token for http call
         */
        private Cookie cookie;
        /**
         * Path with filename denoting the certificate for SSL authentication usage
         */
        private String certFilePath;
        /**
         * AuthType: CLASSIC, TOKEN or SSL
         */
        private final AuthType authType;

        /**
         * Constructor with setting the required authentication type
         *
         * @param authType AuthType
         */
        public Builder(AuthType authType) {
            this.authType = authType;
        }

        /**
         * Set the host
         *
         * @param host machine host pointing to backend z/OS instance
         * @return Builder instance
         */
        public ZosConnection.Builder host(String host) {
            this.host = host;
            return this;
        }

        /**
         * Set the z/OSMF port
         *
         * @param zosmfPort machine host z/OSMF port number pointing to backend z/OS instance
         * @return Builder instance
         */
        public ZosConnection.Builder zosmfPort(String zosmfPort) {
            this.zosmfPort = zosmfPort;
            return this;
        }

        /**
         * Set the user
         *
         * @param user machine host username with access to backend z/OS instance
         * @return Builder instance
         */
        public ZosConnection.Builder user(String user) {
            this.user = user;
            return this;
        }

        /**
         * Set the password
         *
         * @param password machine host username's password with access to backend z/OS instance
         * @return Builder instance
         */
        public ZosConnection.Builder password(String password) {
            this.password = password;
            return this;
        }

        /**
         * Set the certificate password for SSL authentication
         *
         * @param certPassword certificate password
         * @return Builder instance
         */
        public ZosConnection.Builder certPassword(String certPassword) {
            this.certPassword = certPassword;
            return this;
        }

        /**
         * Set the cookie
         *
         * @param cookie Cookie object containing a token value
         * @return Builder instance
         */
        public ZosConnection.Builder cookie(Cookie cookie) {
            this.cookie = cookie;
            return this;
        }

        /**
         * Set the certificate file path
         *
         * @param certFilePath path to the certificate file
         * @return Builder instance
         */
        public ZosConnection.Builder certFilePath(String certFilePath) {
            this.certFilePath = certFilePath;
            return this;
        }

        /**
         * Build the ZosConnection instance
         *
         * @return new ZosConnection instance
         */
        public ZosConnection build() {
            return new ZosConnection(this);
        }
    }

}
