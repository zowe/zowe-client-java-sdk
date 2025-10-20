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

import com.fasterxml.jackson.annotation.*;
import zowe.client.sdk.zosmfinfo.model.ZosmfPlugin;

import java.util.Arrays;

/**
 * The z/OSMF info API response.
 * Immutable class using Jackson for JSON parsing.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ZosmfInfoResponse {

    /**
     * z/OS version
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private final String zosVersion;

    /**
     * Zosmf port number
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private final String zosmfPort;

    /**
     * Zosmf version
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private final String zosmfVersion;

    /**
     * Zosmf host name
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private final String zosmfHostName;

    /**
     * Zosmf saf realm
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private final String zosmfSafRealm;

    /**
     * Zosmf full version
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private final String zosmfFullVersion;

    /**
     * Zosmf api version
     */
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private final String apiVersion;

    /**
     * Zosmf plugin information
     */
    private final ZosmfPlugin[] zosmfPluginsInfo;

    /**
     * Jackson constructor for ZosmfInfoResponse
     *
     * @param zosVersion       z/OS version
     * @param zosmfPort        Zosmf port number
     * @param zosmfVersion     Zosmf version
     * @param zosmfHostName    Zosmf host name
     * @param zosmfSafRealm    Zosmf saf realm
     * @param zosmfFullVersion Zosmf full version
     * @param apiVersion       Zosmf api version
     * @param zosmfPluginsInfo Zosmf plugin information
     */
    @JsonCreator
    public ZosmfInfoResponse(
            @JsonProperty("zos_version") String zosVersion,
            @JsonProperty("zosmf_port") String zosmfPort,
            @JsonProperty("zosmf_version") String zosmfVersion,
            @JsonProperty("zosmf_hostname") String zosmfHostName,
            @JsonProperty("zosmf_saf_realm") String zosmfSafRealm,
            @JsonProperty("zosmf_full_version") String zosmfFullVersion,
            @JsonProperty("api_version") String apiVersion,
            @JsonProperty("plugins") ZosmfPlugin[] zosmfPluginsInfo
    ) {
        this.zosVersion = zosVersion;
        this.zosmfPort = zosmfPort;
        this.zosmfVersion = zosmfVersion;
        this.zosmfHostName = zosmfHostName;
        this.zosmfSafRealm = zosmfSafRealm;
        this.zosmfFullVersion = zosmfFullVersion;
        this.apiVersion = apiVersion;
        this.zosmfPluginsInfo = zosmfPluginsInfo;
    }

    /**
     * Retrieve apiVersion specified
     *
     * @return apiVersion value
     */
    public String getApiVersion() {
        return apiVersion;
    }

    /**
     * Retrieve zosVersion specified
     *
     * @return zosVersion value
     */
    public String getZosVersion() {
        return zosVersion;
    }

    /**
     * Retrieve zosmfFullVersion specified
     *
     * @return zosmfFullVersion value
     */
    public String getZosmfFullVersion() {
        return zosmfFullVersion;
    }

    /**
     * Retrieve zosmfHostName specified
     *
     * @return zosmfHostName value
     */
    public String getZosmfHostName() {
        return zosmfHostName;
    }

    /**
     * Retrieve zosmfPluginsInfo specified
     *
     * @return zosmfPluginsInfo value
     */
    public ZosmfPlugin[] getZosmfPluginsInfo() {
        return zosmfPluginsInfo;
    }

    /**
     * Retrieve zosmfPort specified
     *
     * @return zosmfPort value
     */
    public String getZosmfPort() {
        return zosmfPort;
    }

    /**
     * Retrieve zosmfSafRealm specified
     *
     * @return zosmfSafRealm value
     */
    public String getZosmfSafRealm() {
        return zosmfSafRealm;
    }

    /**
     * Retrieve zosmfVersion specified
     *
     * @return zosmfVersion value
     */
    public String getZosmfVersion() {
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
                ", zosmfPluginsInfo=" + Arrays.toString(zosmfPluginsInfo) +
                '}';
    }

}
