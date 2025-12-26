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
 * @version 5.0
 */
public final class ZosConnection {

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
     * This constructor is package-private.
     * <p>
     * Use ZosConnectionFactory to initiate a ZosConnection Object.
     *
     * @param host      string value
     * @param zosmfPort string value
     * @param authType  AuthType enum value
     * @author Frank Giordano
     */
    ZosConnection(final String host, final String zosmfPort, final AuthType authType) {
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
     * <p>
     * This method's access level is private-package
     *
     * @param user string value
     */
    void setUser(String user) {
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
        if (basePath == null || basePath.isBlank())
            this.basePath = basePath;
        else {
            this.basePath = getNormalizedPath(basePath);
        }
    }

    private String getNormalizedPath(String basePath) {
        String normalizedPath = basePath.replace('\\', '/');
        normalizedPath = normalizedPath.startsWith("/") ? normalizedPath : "/" + normalizedPath;
        return normalizedPath.endsWith("/") ?
                normalizedPath.substring(0, normalizedPath.length() - 1) : normalizedPath;
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
     * Retrieve the z/OSMF url
     *
     * @return string value
     * @author Shabaz Kowthalam
     */
    public String getZosmfUrl() {
        return "https://" + host + ":" + zosmfPort + (basePath != null ? basePath : "") + "/zosmf";
    }

    /**
     * Equals method comparing fields based on the authentication type.
     * <p>
     * For BASIC authentication: compares host, port, authType, basePath, user, and password.
     * For TOKEN authentication: compares host, port, authType, basePath, and token value.
     * For SSL authentication: compares host, port, authType, basePath, certFilePath, and certPassword.
     *
     * @param obj object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ZosConnection that = (ZosConnection) obj;

        // Common fields compared for all auth types
        if (!Objects.equals(host, that.host)
                || !Objects.equals(zosmfPort, that.zosmfPort)
                || authType != that.authType
                || !Objects.equals(basePath, that.basePath)) {
            return false;
        }

        // Compare based on authType
        switch (authType) {
            case BASIC:
                return Objects.equals(user, that.user)
                        && Objects.equals(password, that.password);
            case TOKEN:
                String thisTokenValue = token != null ? token.getValue() : null;
                String thatTokenValue = that.token != null ? that.token.getValue() : null;
                return Objects.equals(thisTokenValue, thatTokenValue);
            case SSL:
                return Objects.equals(certFilePath, that.certFilePath)
                        && Objects.equals(certPassword, that.certPassword);
            default:
                return false;
        }
    }

    /**
     * Hashcode method consistent with equals().
     *
     * @return int value representing the hash code
     */
    @Override
    public int hashCode() {
        switch (authType) {
            case BASIC:
                return Objects.hash(host, zosmfPort, authType, user, password, basePath);
            case TOKEN:
                String tokenValue = token != null ? token.getValue() : null;
                return Objects.hash(host, zosmfPort, authType, tokenValue, basePath);
            case SSL:
                return Objects.hash(host, zosmfPort, authType, certFilePath, certPassword, basePath);
            default:
                return Objects.hash(host, zosmfPort, authType, basePath);
        }
    }

    /**
     * Return string value representing ZosConnection object
     *
     * @return string representation of ZosConnection
     */
    @Override
    public String toString() {
        return "ZosConnection{" +
                "host='" + ((host == null) ? "" : host) + '\'' +
                ", zosmfPort='" + ((zosmfPort == null) ? "" : zosmfPort) + '\'' +
                ", authType=" + authType +
                ", user='" + ((user == null) ? "" : user) + '\'' +
                ", password='" + ((password == null || password.isEmpty()) ? "" : "*****") + '\'' +
                ", token='" + ((token == null) ? "" : "*****") + '\'' +
                ", certFilePath='" + ((certFilePath == null) ? "" : certFilePath) + '\'' +
                ", certPassword='" + ((certPassword == null || certPassword.isEmpty()) ? "" : "*****") + '\'' +
                ", basePath='" + ((basePath == null) ? "" : basePath) + '\'' +
                '}';
    }

}
