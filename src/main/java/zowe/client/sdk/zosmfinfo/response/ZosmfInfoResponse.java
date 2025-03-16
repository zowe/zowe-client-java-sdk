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
 * The Z/OSMF info API response.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class ZosmfInfoResponse {

    /**
     * z/OS version
     */
    private final Optional<String> zosVersion;

    /**
     * Zosmf port number
     */
    private final Optional<String> zosmfPort;

    /**
     * Zosmf version
     */
    private final Optional<String> zosmfVersion;

    /**
     * Zosmf host name
     */
    private final Optional<String> zosmfHostName;

    /**
     * Zosmf saf realm
     */
    private final Optional<String> zosmfSafRealm;

    /**
     * Zosmf full version
     */
    private final Optional<String> zosmfFullVersion;

    /**
     * Zosmf api version
     */
    private final Optional<String> apiVersion;

    /**
     * Zosmf plugin information
     */
    private final Optional<ZosmfPluginInfo[]> zosmfPluginsInfo;

    /**
     * ZosmfInfoResponse constructor
     *
     * @param builder Builder Object
     * @author Frank Giordano
     */
    private ZosmfInfoResponse(final Builder builder) {
        this.zosVersion = Optional.ofNullable(builder.zosVersion);
        this.zosmfPort = Optional.ofNullable(builder.zosmfPort);
        this.zosmfVersion = Optional.ofNullable(builder.zosmfVersion);
        this.zosmfHostName = Optional.ofNullable(builder.zosmfHostName);
        this.zosmfSafRealm = Optional.ofNullable(builder.zosmfSafRealm);
        this.zosmfFullVersion = Optional.ofNullable(builder.zosmfFullVersion);
        this.apiVersion = Optional.ofNullable(builder.apiVersion);
        this.zosmfPluginsInfo = Optional.ofNullable(builder.zosmfPluginsInfo);
    }

    /**
     * Retrieve apiVersion specified
     *
     * @return apiVersion value
     */
    public Optional<String> getApiVersion() {
        return apiVersion;
    }

    /**
     * Retrieve zosVersion specified
     *
     * @return zosVersion value
     */
    public Optional<String> getZosVersion() {
        return zosVersion;
    }

    /**
     * Retrieve zosmfFullVersion specified
     *
     * @return zosmfFullVersion value
     */
    public Optional<String> getZosmfFullVersion() {
        return zosmfFullVersion;
    }

    /**
     * Retrieve zosmfHostName specified
     *
     * @return zosmfHostName value
     */
    public Optional<String> getZosmfHostName() {
        return zosmfHostName;
    }

    /**
     * Retrieve zosmfPluginsInfo specified
     *
     * @return zosmfPluginsInfo value
     */
    public Optional<ZosmfPluginInfo[]> getZosmfPluginsInfo() {
        return zosmfPluginsInfo;
    }

    /**
     * Retrieve zosmfPort specified
     *
     * @return zosmfPort value
     */
    public Optional<String> getZosmfPort() {
        return zosmfPort;
    }

    /**
     * Retrieve zosmfSafRealm specified
     *
     * @return zosmfSafRealm value
     */
    public Optional<String> getZosmfSafRealm() {
        return zosmfSafRealm;
    }

    /**
     * Retrieve zosmfVersion specified
     *
     * @return zosmfVersion value
     */
    public Optional<String> getZosmfVersion() {
        return zosmfVersion;
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
         * Return ChangeTagParams object based on Builder this object
         *
         * @return ChangeTagParams this object
         */
        public ZosmfInfoResponse build() {
            return new ZosmfInfoResponse(this);
        }

    }

}
