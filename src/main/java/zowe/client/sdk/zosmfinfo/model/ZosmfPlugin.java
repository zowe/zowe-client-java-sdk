/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfinfo.model;

import com.fasterxml.jackson.annotation.*;

/**
 * The plugin information structure for the plugin property of the z/OSMF info response.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ZosmfPlugin {

    /**
     * Plugin version.
     */
    private final String pluginVersion;

    /**
     * Plugin default name.
     */
    private final String pluginDefaultName;

    /**
     * Plugin status.
     */
    private final String pluginStatus;

    /**
     * ZosmfPlugin constructor.
     *
     * @param pluginVersion     version of the plugin
     * @param pluginDefaultName default name of the plugin
     * @param pluginStatus      status of the plugin
     */
    @JsonCreator
    public ZosmfPlugin(
            @JsonProperty("pluginVersion") final String pluginVersion,
            @JsonProperty("pluginDefaultName") final String pluginDefaultName,
            @JsonProperty("pluginStatus") final String pluginStatus) {
        this.pluginVersion = pluginVersion == null ? "" : pluginVersion;
        this.pluginDefaultName = pluginDefaultName == null ? "" : pluginDefaultName;
        this.pluginStatus = pluginStatus == null ? "" : pluginStatus;
    }

    /**
     * Retrieve the plugin version specified.
     *
     * @return plugin version value
     */
    public String getPluginVersion() {
        return pluginVersion;
    }

    /**
     * Retrieve plugin default name specified.
     *
     * @return plugin default name value
     */
    public String getPluginDefaultName() {
        return pluginDefaultName;
    }

    /**
     * Retrieve plugin status specified.
     *
     * @return plugin status value
     */
    public String getPluginStatus() {
        return pluginStatus;
    }

    /**
     * Return string value representing ZosmfPlugin object.
     *
     * @return string representation of ZosmfPlugin
     */
    @Override
    public String toString() {
        return "ZosmfPlugin{" +
                "pluginVersion='" + pluginVersion + '\'' +
                ", pluginDefaultName='" + pluginDefaultName + '\'' +
                ", pluginStatus='" + pluginStatus + '\'' +
                '}';
    }

}