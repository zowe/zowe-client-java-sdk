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
 * @version 1.0
 */
public class ZosmfPluginInfo {

    /**
     * Plugin version
     */
    public final Optional<String> pluginVersion;

    /**
     * Plugin default name
     */
    public final Optional<String> pluginDefaultName;

    /**
     * Plugin status.
     */
    public final Optional<String> pluginStatus;


    /**
     * ZosmfPluginInfo Constructor.
     *
     * @param builder ZosmfPluginInfo.Builder Object
     * @author Frank Giordano
     */
    private ZosmfPluginInfo(ZosmfPluginInfo.Builder builder) {
        this.pluginVersion = Optional.ofNullable(builder.pluginVersion);
        this.pluginDefaultName = Optional.ofNullable(builder.pluginDefaultName);
        this.pluginStatus = Optional.ofNullable(builder.pluginStatus);
    }

    @Override
    public String toString() {
        return "ZosmfPluginInfo{" +
                "pluginVersion=" + pluginVersion +
                ", pluginDefaultName=" + pluginDefaultName +
                ", pluginStatus=" + pluginStatus +
                '}';
    }

    public static class Builder {

        private String pluginVersion;
        private String pluginDefaultName;
        private String pluginStatus;

        public ZosmfPluginInfo.Builder pluginVersion(String pluginVersion) {
            this.pluginVersion = pluginVersion;
            return this;
        }

        public ZosmfPluginInfo.Builder pluginDefaultName(String pluginDefaultName) {
            this.pluginDefaultName = pluginDefaultName;
            return this;
        }

        public ZosmfPluginInfo.Builder pluginStatus(String pluginStatus) {
            this.pluginStatus = pluginStatus;
            return this;
        }

        public ZosmfPluginInfo build() {
            return new ZosmfPluginInfo(this);
        }

    }

}
