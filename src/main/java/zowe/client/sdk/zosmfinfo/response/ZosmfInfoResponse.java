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
 * @version 1.0
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
     * ZosmfInfoResponse Constructor.
     *
     * @param builder ZosmfInfoResponse.Builder Object
     * @author Frank Giordano
     */
    private ZosmfInfoResponse(ZosmfInfoResponse.Builder builder) {
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
     * Retrieve zosVersion specified
     *
     * @return zosVersion value
     * @author Frank Giordano
     */
    public Optional<String> getZosVersion() {
        return zosVersion;
    }

    /**
     * Retrieve zosmfPort specified
     *
     * @return zosmfPort value
     * @author Frank Giordano
     */
    public Optional<String> getZosmfPort() {
        return zosmfPort;
    }

    /**
     * Retrieve zosmfVersion specified
     *
     * @return zosmfVersion value
     * @author Frank Giordano
     */
    public Optional<String> getZosmfVersion() {
        return zosmfVersion;
    }

    /**
     * Retrieve zosmfHostName specified
     *
     * @return zosmfHostName value
     * @author Frank Giordano
     */
    public Optional<String> getZosmfHostName() {
        return zosmfHostName;
    }

    /**
     * Retrieve zosmfSafRealm specified
     *
     * @return zosmfSafRealm value
     * @author Frank Giordano
     */
    public Optional<String> getZosmfSafRealm() {
        return zosmfSafRealm;
    }

    /**
     * Retrieve zosmfFullVersion specified
     *
     * @return zosmfFullVersion value
     * @author Frank Giordano
     */
    public Optional<String> getZosmfFullVersion() {
        return zosmfFullVersion;
    }

    /**
     * Retrieve apiVersion specified
     *
     * @return apiVersion value
     * @author Frank Giordano
     */
    public Optional<String> getApiVersion() {
        return apiVersion;
    }

    /**
     * Retrieve zosmfPluginsInfo specified
     *
     * @return zosmfPluginsInfo value
     * @author Frank Giordano
     */
    public Optional<ZosmfPluginInfo[]> getZosmfPluginsInfo() {
        return zosmfPluginsInfo;
    }

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

    public static class Builder {

        private String zosVersion;
        private String zosmfPort;
        private String zosmfVersion;
        private String zosmfHostName;
        private String zosmfSafRealm;
        private String zosmfFullVersion;
        private String apiVersion;
        private ZosmfPluginInfo[] zosmfPluginsInfo;

        public ZosmfInfoResponse.Builder zosVersion(String zosVersion) {
            this.zosVersion = zosVersion;
            return this;
        }

        public ZosmfInfoResponse.Builder zosmfPort(String zosmfPort) {
            this.zosmfPort = zosmfPort;
            return this;
        }

        public ZosmfInfoResponse.Builder zosmfSafRealm(String zosmfSafRealm) {
            this.zosmfSafRealm = zosmfSafRealm;
            return this;
        }

        public ZosmfInfoResponse.Builder zosmfVersion(String zosmfVersion) {
            this.zosmfVersion = zosmfVersion;
            return this;
        }

        public ZosmfInfoResponse.Builder zosmfHostName(String zosmfHostName) {
            this.zosmfHostName = zosmfHostName;
            return this;
        }

        public ZosmfInfoResponse.Builder zosmfFullVersion(String zosmfFullVersion) {
            this.zosmfFullVersion = zosmfFullVersion;
            return this;
        }

        public ZosmfInfoResponse.Builder apiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        public ZosmfInfoResponse.Builder zosmfPluginsInfo(ZosmfPluginInfo[] zosmfPluginsInfo) {
            this.zosmfPluginsInfo = zosmfPluginsInfo;
            return this;
        }

        public ZosmfInfoResponse build() {
            return new ZosmfInfoResponse(this);
        }

    }

}
