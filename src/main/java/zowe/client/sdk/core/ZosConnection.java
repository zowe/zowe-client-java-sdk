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
    private String basePath;

    /**
     * ZosConnection constructor
     * <p>
     * This constructor is package-private. Use ZosConnectionFactory to initiate a ZosConnection Object.
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
     * This method's access level is private-package
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
     * This method's access level is private-package
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
     * This method's access level is private-package
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
     * This method's access level is private-package
     *
     * @param certPassword string value
     */
    void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    /**
     * Set the base path for z/OSMF REST endpoints
     * <p>
     * This method's access level is private-package
     *
     * @param basePath string value
     */
    void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    /**
     * Retrieve the base path for z/OSMF REST endpoints
     *
     * @return string value
     */
    public Optional<String> getBasePath() {
        return Optional.ofNullable(basePath);
    }

    /**
     * Equals method. Use all members for equality except for TOKEN which is a special case and
     * uses a subset of members for equality.
     *
     * @param obj object
     * @return true or false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ZosConnection that = (ZosConnection) obj;
        if (this.authType == AuthType.TOKEN) {
            return Objects.equals(host, that.host) &&
                    Objects.equals(zosmfPort, that.zosmfPort) &&
                    Objects.equals(user, that.user) &&
                    Objects.equals(token.getValue(), that.token.getValue()) &&
                    Objects.equals(basePath, that.basePath);
        } else {
            return Objects.equals(host, that.host) &&
                    Objects.equals(zosmfPort, that.zosmfPort) &&
                    authType == that.authType &&
                    Objects.equals(user, that.user) &&
                    Objects.equals(password, that.password) &&
                    Objects.equals(certFilePath, that.certFilePath) &&
                    Objects.equals(certPassword, that.certPassword) &&
                    Objects.equals(basePath, that.basePath);
        }
    }

    /**
     * Hashcode method. Use all members for hashing.
     *
     * @return int value
     */
    @Override
    public int hashCode() {
        return Objects.hash(host, zosmfPort, authType, user, password, token,
                certFilePath, certPassword, basePath);
    }

}
