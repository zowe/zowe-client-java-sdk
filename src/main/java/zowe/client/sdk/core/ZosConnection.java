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
    private String user;
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
    private final Cookie token;
    /**
     * Path with filename denoting the certificate for SSL authentication usage
     */
    private final String certFilePath;
    /**
     * AuthType: BASIC, TOKEN or SSL enum value for http request processing
     */
    private final AuthType authType;

    /**
     * Private constructor used by the Builder
     *
     * @param builder Builder instance containing configuration
     * @author Frank Giordano
     */
    private ZosConnection(ZosConnection.Builder builder) {
        this.host = builder.host;
        this.zosmfPort = builder.zosmfPort;
        this.user = builder.user;
        this.password = builder.password;
        this.certPassword = builder.certPassword;
        this.token = builder.token;
        this.certFilePath = builder.certFilePath;
        this.authType = builder.authType;
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
     * Retrieve a cookie object representing a TOKEN
     *
     * @return Cookie object
     */
    public Cookie getToken() {
        return token;
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
     * Set user value
     *
     * @param user string value
     */
    public void setUser(String user) {
        this.user = user;
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
     * Retrieve host
     *
     * @return string value
     */
    public String getHost() {
        return host;
    }

    /**
     * Retrieve AuthType: BASIC, TOKEN or SSL enum value for http request processing
     *
     * @return AuthType enum value
     */
    public AuthType getAuthType() {
        return authType;
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
        if (this.authType == AuthType.BASIC) {
            return Objects.equals(host, other.host) &&
                    Objects.equals(zosmfPort, other.zosmfPort) &&
                    Objects.equals(user, other.user) &&
                    Objects.equals(password, other.password);
        } else if (this.authType == AuthType.TOKEN) {
            if (this.user != null && !this.user.isBlank()) {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(user, other.user) &&
                        Objects.equals(token.getValue(), other.token.getValue());
            } else {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(token.getValue(), other.token.getValue());
            }
        } else if (this.authType == AuthType.SSL) {
            if (this.user != null && !this.user.isBlank()) {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(user, other.user) &&
                        Objects.equals(certPassword, other.certPassword) &&
                        Objects.equals(certFilePath, other.certFilePath);
            } else {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(certPassword, other.certPassword) &&
                        Objects.equals(certFilePath, other.certFilePath);
            }
        }
        return false;
    }

    /**
     * Hashcode method. Use all members for hashing.
     *
     * @return int value
     */
    @Override
    public int hashCode() {
        return Objects.hash(host, zosmfPort, user, password, certPassword, token, certFilePath);
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
         * Token value set to use as an authentication token for http call
         */
        private Cookie token;
        /**
         * Path with filename denoting the certificate for SSL authentication usage
         */
        private String certFilePath;
        /**
         * Authentication enum type for the http request
         */
        private AuthType authType;

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
         * Set the token
         *
         * @param token Cookie object containing a token value
         * @return Builder instance
         */
        public ZosConnection.Builder token(Cookie token) {
            this.token = token;
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
         * Set the AuthType: BASIC, TOKEN or SSL enum value for http request processing
         *
         * @param authType AuthType enum value
         * @return Builder instance
         */
        public ZosConnection.Builder authType(AuthType authType) {
            this.authType = authType;
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
