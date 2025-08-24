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
 * The plugin information structure for the plugin property of the z/OSMF info response
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosmfPluginInfo {

    /**
     * Plugin version
     */
    private final String pluginVersion;

    /**
     * Plugin default name
     */
    private final String pluginDefaultName;

    /**
     * Plugin status.
     */
    private final String pluginStatus;

    /**
     * ZosmfPluginInfo constructor
     *
     * @param builder Builder Object
     * @author Frank Giordano
     */
    private ZosmfPluginInfo(final Builder builder) {
        this.pluginVersion = builder.pluginVersion;
        this.pluginDefaultName = builder.pluginDefaultName;
        this.pluginStatus = builder.pluginStatus;
    }

    /**
     * Retrieve pluginDefaultName specified
     *
     * @return pluginDefaultName value
     */
    public Optional<String> getPluginDefaultName() {
        return Optional.ofNullable(pluginDefaultName);
    }

    /**
     * Retrieve pluginStatus specified
     *
     * @return pluginStatus value
     */
    public Optional<String> getPluginStatus() {
        return Optional.ofNullable(pluginStatus);
    }

    /**
     * Retrieve pluginVersion specified
     *
     * @return pluginVersion value
     */
    public Optional<String> getPluginVersion() {
        return Optional.ofNullable(pluginVersion);
    }

    /**
     * Return string value representing ZosmfPluginInfo object
     *
     * @return string representation of ZosmfPluginInfo
     */
    @Override
    public String toString() {
        return "ZosmfPluginInfo{" +
                "pluginVersion=" + pluginVersion +
                ", pluginDefaultName=" + pluginDefaultName +
                ", pluginStatus=" + pluginStatus +
                '}';
    }

    /**
     * Builder class for ZosmfPluginInfo
     */
    public static class Builder {

        /**
         * Plugin version
         */
        private String pluginVersion;

        /**
         * Plugin default name
         */
        private String pluginDefaultName;

        /**
         * Plugin status.
         */
        private String pluginStatus;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set pluginDefaultName string value
         *
         * @param pluginDefaultName string value
         * @return Builder this object
         */
        public Builder pluginDefaultName(final String pluginDefaultName) {
            this.pluginDefaultName = pluginDefaultName;
            return this;
        }

        /**
         * Set pluginStatus string value
         *
         * @param pluginStatus string value
         * @return Builder this object
         */
        public Builder pluginStatus(final String pluginStatus) {
            this.pluginStatus = pluginStatus;
            return this;
        }

        /**
         * Set pluginVersion string value
         *
         * @param pluginVersion string value
         * @return Builder this object
         */
        public Builder pluginVersion(final String pluginVersion) {
            this.pluginVersion = pluginVersion;
            return this;
        }

        /**
         * Return ZosmfPluginInfo object based on Builder this object
         *
         * @return ZosmfPluginInfo this object
         */
        public ZosmfPluginInfo build() {
            return new ZosmfPluginInfo(this);
        }

    }

}
