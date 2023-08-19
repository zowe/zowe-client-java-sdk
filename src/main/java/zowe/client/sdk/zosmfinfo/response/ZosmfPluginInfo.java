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
 * The plugin information structure for plugin property of the z/OSMF info response
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZosmfPluginInfo {

    /**
     * Plugin version
     */
    private final Optional<String> pluginVersion;

    /**
     * Plugin default name
     */
    private final Optional<String> pluginDefaultName;

    /**
     * Plugin status.
     */
    private final Optional<String> pluginStatus;

    /**
     * ZosmfPluginInfo constructor
     *
     * @param builder ZosmfPluginInfo.Builder Object
     * @author Frank Giordano
     */
    private ZosmfPluginInfo(final ZosmfPluginInfo.Builder builder) {
        this.pluginVersion = Optional.ofNullable(builder.pluginVersion);
        this.pluginDefaultName = Optional.ofNullable(builder.pluginDefaultName);
        this.pluginStatus = Optional.ofNullable(builder.pluginStatus);
    }

    /**
     * Retrieve pluginDefaultName specified
     *
     * @return pluginDefaultName value
     */
    public Optional<String> getPluginDefaultName() {
        return pluginDefaultName;
    }

    /**
     * Retrieve pluginStatus specified
     *
     * @return pluginStatus value
     */
    public Optional<String> getPluginStatus() {
        return pluginStatus;
    }

    /**
     * Retrieve pluginVersion specified
     *
     * @return pluginVersion value
     */
    public Optional<String> getPluginVersion() {
        return pluginVersion;
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

        private String pluginVersion;
        private String pluginDefaultName;
        private String pluginStatus;

        public ZosmfPluginInfo.Builder pluginDefaultName(final String pluginDefaultName) {
            this.pluginDefaultName = pluginDefaultName;
            return this;
        }

        public ZosmfPluginInfo.Builder pluginStatus(final String pluginStatus) {
            this.pluginStatus = pluginStatus;
            return this;
        }

        public ZosmfPluginInfo.Builder pluginVersion(final String pluginVersion) {
            this.pluginVersion = pluginVersion;
            return this;
        }

        public ZosmfPluginInfo build() {
            return new ZosmfPluginInfo(this);
        }

    }

}
