/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.response;

import java.util.Optional;

/**
 * The z/OSMF info API response.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosmfInfoResponse {

    /**
     * z/OS version
     */
    private final String zosVersion;

    /**
     * Zosmf port number
     */
    private final String zosmfPort;

    /**
     * Zosmf version
     */
    private final String zosmfVersion;

    /**
     * Zosmf host name
     */
    private final String zosmfHostName;

    /**
     * Zosmf saf realm
     */
    private final String zosmfSafRealm;

    /**
     * Zosmf full version
     */
    private final String zosmfFullVersion;

    /**
     * Zosmf api version
     */
    private final String apiVersion;

    /**
     * Zosmf plugin information
     */
    private final ZosmfPluginInfo[] zosmfPluginsInfo;

    /**
     * ZosmfInfoResponse constructor
     *
     * @param builder Builder Object
     * @author Frank Giordano
     */
    private ZosmfInfoResponse(final Builder builder) {
        this.zosVersion = builder.zosVersion;
        this.zosmfPort = builder.zosmfPort;
        this.zosmfVersion = builder.zosmfVersion;
        this.zosmfHostName = builder.zosmfHostName;
        this.zosmfSafRealm = builder.zosmfSafRealm;
        this.zosmfFullVersion = builder.zosmfFullVersion;
        this.apiVersion = builder.apiVersion;
        this.zosmfPluginsInfo = builder.zosmfPluginsInfo;
    }

    /**
     * Retrieve apiVersion specified
     *
     * @return apiVersion value
     */
    public Optional<String> getApiVersion() {
        return Optional.ofNullable(apiVersion);
    }

    /**
     * Retrieve zosVersion specified
     *
     * @return zosVersion value
     */
    public Optional<String> getZosVersion() {
        return Optional.ofNullable(zosVersion);
    }

    /**
     * Retrieve zosmfFullVersion specified
     *
     * @return zosmfFullVersion value
     */
    public Optional<String> getZosmfFullVersion() {
        return Optional.ofNullable(zosmfFullVersion);
    }

    /**
     * Retrieve zosmfHostName specified
     *
     * @return zosmfHostName value
     */
    public Optional<String> getZosmfHostName() {
        return Optional.ofNullable(zosmfHostName);
    }

    /**
     * Retrieve zosmfPluginsInfo specified
     *
     * @return zosmfPluginsInfo value
     */
    public Optional<ZosmfPluginInfo[]> getZosmfPluginsInfo() {
        return Optional.ofNullable(zosmfPluginsInfo);
    }

    /**
     * Retrieve zosmfPort specified
     *
     * @return zosmfPort value
     */
    public Optional<String> getZosmfPort() {
        return Optional.ofNullable(zosmfPort);
    }

    /**
     * Retrieve zosmfSafRealm specified
     *
     * @return zosmfSafRealm value
     */
    public Optional<String> getZosmfSafRealm() {
        return Optional.ofNullable(zosmfSafRealm);
    }

    /**
     * Retrieve zosmfVersion specified
     *
     * @return zosmfVersion value
     */
    public Optional<String> getZosmfVersion() {
        return Optional.ofNullable(zosmfVersion);
    }

    /**
     * Return string value representing ZosmfInfoResponse object
     *
     * @return string representation of ZosmfInfoResponse
     */
    @Override
    public String toString() {
        return "ZosmfInfoResponse{" +
                "zosVersion=" + zosVersion +
                ", zosmfPort=" + zosmfPort +
                ", zosmfVersion=" + zosmfVersion +
                ", zosmfHostName=" + zosmfHostName +
                ", zosmfSafRealm=" + zosmfSafRealm +
                ", zosmfFullVersion=" + zosmfFullVersion +
                ", apiVersion=" + apiVersion +
                ", zosmfPluginsInfo=" + zosmfPluginsInfo +
                '}';
    }

    /**
     * Builder class for ZosmfInfoResponse
     */
    public static class Builder {

        /**
         * z/OS version
         */
        private String zosVersion;

        /**
         * Zosmf port number
         */
        private String zosmfPort;

        /**
         * Zosmf version
         */
        private String zosmfVersion;

        /**
         * Zosmf host name
         */
        private String zosmfHostName;

        /**
         * Zosmf saf realm
         */
        private String zosmfSafRealm;

        /**
         * Zosmf full version
         */
        private String zosmfFullVersion;

        /**
         * Zosmf api version
         */
        private String apiVersion;

        /**
         * Zosmf plugin information
         */
        private ZosmfPluginInfo[] zosmfPluginsInfo;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set apiVersion string value
         *
         * @param apiVersion string value
         * @return Builder this object
         */
        public Builder apiVersion(final String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        /**
         * Set zosVersion string value
         *
         * @param zosVersion string value
         * @return Builder this object
         */
        public Builder zosVersion(final String zosVersion) {
            this.zosVersion = zosVersion;
            return this;
        }

        /**
         * Set zosmfFullVersion string value
         *
         * @param zosmfFullVersion string value
         * @return Builder this object
         */
        public Builder zosmfFullVersion(final String zosmfFullVersion) {
            this.zosmfFullVersion = zosmfFullVersion;
            return this;
        }

        /**
         * Set zosmfHostName string value
         *
         * @param zosmfHostName string value
         * @return Builder this object
         */
        public Builder zosmfHostName(final String zosmfHostName) {
            this.zosmfHostName = zosmfHostName;
            return this;
        }

        /**
         * Set zosmfPluginsInfo string value
         *
         * @param zosmfPluginsInfo string value
         * @return Builder this object
         */
        public Builder zosmfPluginsInfo(final ZosmfPluginInfo[] zosmfPluginsInfo) {
            this.zosmfPluginsInfo = zosmfPluginsInfo;
            return this;
        }

        /**
         * Set zosmfPort string value
         *
         * @param zosmfPort string value
         * @return Builder this object
         */
        public Builder zosmfPort(final String zosmfPort) {
            this.zosmfPort = zosmfPort;
            return this;
        }

        /**
         * Set zosmfSafRealm string value
         *
         * @param zosmfSafRealm string value
         * @return Builder this object
         */
        public Builder zosmfSafRealm(final String zosmfSafRealm) {
            this.zosmfSafRealm = zosmfSafRealm;
            return this;
        }

        /**
         * Set zosmfVersion string value
         *
         * @param zosmfVersion string value
         * @return Builder this object
         */
        public Builder zosmfVersion(final String zosmfVersion) {
            this.zosmfVersion = zosmfVersion;
            return this;
        }

        /**
         * Return ZosmfInfoResponse object based on Builder this object
         *
         * @return ZosmfInfoResponse this object
         */
        public ZosmfInfoResponse build() {
            return new ZosmfInfoResponse(this);
        }

    }

}
