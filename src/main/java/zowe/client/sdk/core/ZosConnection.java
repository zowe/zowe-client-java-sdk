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
import java.util.Optional;

/**
 * z/OSMF Connection information placeholder
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
     * AuthType: BASIC, TOKEN or SSL enum value for http request processing
     */
    private final AuthType authType;
    /**
     * Host username with access to a backend z/OS instance
     */
    private String user;
    /**
     * Host username's password with access to backend z/OS instance
     */
    private String password;
    /**
     * Cookie value set to use as an authentication token for http call
     */
    private Cookie token;
    /**
     * Path with filename denoting the certificate for SSL authentication usage
     */
    private String certFilePath;
    /**
     * The certificate password for SSL authentication
     */
    private String certPassword;
    /**
     * Base path for z/OSMF REST endpoints
     */
    private Optional<String> basePath = Optional.empty();
    /**
     * Flag is not used at this time.
     */
    private final boolean isSecure = true;

    /**
     * ZosConnection constructor
     *
     * @param host      string value
     * @param zosmfPort string value
     * @param authType  AuthType enum value
     */
    ZosConnection(String host, String zosmfPort, AuthType authType) {
        this.host = host;
        this.zosmfPort = zosmfPort;
        this.authType = authType;
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
     * Retrieve zosmfPort
     *
     * @return string value
     */
    public String getZosmfPort() {
        return zosmfPort;
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
     * Retrieve password
     *
     * @return string value
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password value
     * <p>
     * This method is access level is private-package
     *
     * @param password string value
     */
    void setPassword(String password) {
        this.password = password;
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
     * Set token value
     * <p>
     * This method is access level is private-package
     *
     * @param token Cookie object
     */
    void setToken(Cookie token) {
        this.token = token;
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
     * Set certificate path value
     * <p>
     * This method is access level is private-package
     *
     * @param certFilePath string value
     */
    void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
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
     * Set the certificate password value
     * <p>
     * This method is access level is private-package
     *
     * @param certPassword string value
     */
    void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    /**
     * Set the base path for z/OSMF REST endpoints
     *
     * @param basePath string value
     */
    public void setBasePath(String basePath) {
        this.basePath = Optional.ofNullable(basePath);
    }

    /**
     * Retrieve the base path for z/OSMF REST endpoints
     *
     * @return string value
     */
    public Optional<String> getBasePath() {
        return basePath;
    }

    /**
     * Flag is not used at this time.
     *
     * @return boolean value
     */
    public boolean isSecure() {
        return isSecure;
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
                    Objects.equals(password, other.password) &&
                    Objects.equals(basePath, other.basePath) &&
                    isSecure == other.isSecure;
        } else if (this.authType == AuthType.TOKEN) {
            if (this.user != null && !this.user.isBlank()) {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(user, other.user) &&
                        Objects.equals(token.getValue(), other.token.getValue()) &&
                        Objects.equals(basePath, other.basePath) &&
                        isSecure == other.isSecure;
            } else {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(token.getValue(), other.token.getValue()) &&
                        Objects.equals(basePath, other.basePath) &&
                        isSecure == other.isSecure;
            }
        } else if (this.authType == AuthType.SSL) {
            if (this.user != null && !this.user.isBlank()) {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(user, other.user) &&
                        Objects.equals(certPassword, other.certPassword) &&
                        Objects.equals(certFilePath, other.certFilePath) &&
                        Objects.equals(basePath, other.basePath) &&
                        isSecure == other.isSecure;
            } else {
                return Objects.equals(host, other.host) &&
                        Objects.equals(zosmfPort, other.zosmfPort) &&
                        Objects.equals(certPassword, other.certPassword) &&
                        Objects.equals(certFilePath, other.certFilePath) &&
                        Objects.equals(basePath, other.basePath) &&
                        isSecure == other.isSecure;
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
        return Objects.hash(host, zosmfPort, user, password, certPassword, token, certFilePath, isSecure, basePath);
    }

}
