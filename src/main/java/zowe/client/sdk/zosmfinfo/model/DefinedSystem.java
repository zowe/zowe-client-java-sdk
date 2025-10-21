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
 * The z/OSMF object returned for every defined system.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class DefinedSystem {

    /**
     * Unique name assigned to the system definition.
     */
    private final String systemNickName;

    /**
     * Comma-separated list of the groups to which the system is assigned.
     */
    private final String groupNames;

    /**
     * Serial number of the CPC.
     */
    private final String cpcSerial;

    /**
     * Version of z/OS
     */
    private final String zosVR;

    /**
     * Name specified for the system on the SYSNAME parameter in the IEASYSxx parmlib member.
     */
    private final String systemName;

    /**
     * Type for the primary job entry subsystem running on the system. The type is either JES2 or JES3.
     */
    private final String jesType;

    /**
     * Name of the sysplex where the z/OS system is a member.
     */
    private final String sysplexName;

    /**
     * JES2 multi-access spool (MAS) member name or JES3 complex member name
     */
    private final String jesMemberName;

    /**
     * Name of the HTTP proxy definition that specifies the settings required to
     * access the system through an HTTP or SOCKS proxy server.
     */
    private final String httpProxyName;

    /**
     * Name of the server definition that specifies the settings required to
     * access the FTP or SFTP server that is running on the system.
     */
    private final String ftpDestinationName;

    /**
     * URL used to access the z/OSMF instance that resides in the same sysplex as
     * the system identified by the systemName attribute.
     */
    private final String url;

    /**
     * Name specified for the central processor complex (CPC) at the support element (SE)
     * of that processor complex.
     */
    private final String cpcName;

    /**
     * Jackson constructor for DefinedSystem
     *
     * @param systemNickName     unique name assigned to the system definition
     * @param groupNames         comma-separated list of the groups to which the system is assigned
     * @param cpcSerial          serial number of the CPC
     * @param zosVR              version of z/OS
     * @param systemName         name specified for the system on the SYSNAME parameter
     * @param jesType            type of JES subsystem (JES2 or JES3)
     * @param sysplexName        name of the sysplex where the system is a member
     * @param jesMemberName      JES2 MAS member name or JES3 complex member name
     * @param httpProxyName      HTTP proxy definition name
     * @param ftpDestinationName FTP/SFTP server definition name
     * @param url                URL used to access the z/OSMF instance
     * @param cpcName            name of the CPC at the support element
     */
    @JsonCreator
    public DefinedSystem(
            @JsonProperty("systemNickName") String systemNickName,
            @JsonProperty("groupNames") String groupNames,
            @JsonProperty("cpcSerial") String cpcSerial,
            @JsonProperty("zosVR") String zosVR,
            @JsonProperty("systemName") String systemName,
            @JsonProperty("jesType") String jesType,
            @JsonProperty("sysplexName") String sysplexName,
            @JsonProperty("jesMemberName") String jesMemberName,
            @JsonProperty("httpProxyName") String httpProxyName,
            @JsonProperty("ftpDestinationName") String ftpDestinationName,
            @JsonProperty("url") String url,
            @JsonProperty("cpcName") String cpcName
    ) {
        this.systemNickName = systemNickName == null ? "" : systemNickName;
        this.groupNames = groupNames == null ? "" : groupNames;
        this.cpcSerial = cpcSerial == null ? "" : cpcSerial;
        this.zosVR = zosVR == null ? "" : zosVR;
        this.systemName = systemName == null ? "" : systemName;
        this.jesType = jesType == null ? "" : jesType;
        this.sysplexName = sysplexName == null ? "" : sysplexName;
        this.jesMemberName = jesMemberName == null ? "" : jesMemberName;
        this.httpProxyName = httpProxyName == null ? "" : httpProxyName;
        this.ftpDestinationName = ftpDestinationName == null ? "" : ftpDestinationName;
        this.url = url == null ? "" : url;
        this.cpcName = cpcName == null ? "" : cpcName;
    }

    /**
     * Retrieve cpcName specified
     *
     * @return cpcName value
     */
    public String getCpcName() {
        return cpcName;
    }

    /**
     * Retrieve cpcSerial specified
     *
     * @return cpcSerial value
     */
    public String getCpcSerial() {
        return cpcSerial;
    }

    /**
     * Retrieve ftpDestinationName specified
     *
     * @return ftpDestinationName value
     */
    public String getFtpDestinationName() {
        return ftpDestinationName;
    }

    /**
     * Retrieve groupNames specified
     *
     * @return groupNames value
     */
    public String getGroupNames() {
        return groupNames;
    }

    /**
     * Retrieve httpProxyName specified
     *
     * @return httpProxyName value
     */
    public String getHttpProxyName() {
        return httpProxyName;
    }

    /**
     * Retrieve jesMemberName specified
     *
     * @return jesMemberName value
     */
    public String getJesMemberName() {
        return jesMemberName;
    }

    /**
     * Retrieve jesType specified
     *
     * @return jesType value
     */
    public String getJesType() {
        return jesType;
    }

    /**
     * Retrieve sysplexName specified
     *
     * @return sysplexName value
     */
    public String getSysplexName() {
        return sysplexName;
    }

    /**
     * Retrieve systemName specified
     *
     * @return systemName value
     */
    public String getSystemName() {
        return systemName;
    }

    /**
     * Retrieve systemNickName specified
     *
     * @return systemNickName value
     */
    public String getSystemNickName() {
        return systemNickName;
    }

    /**
     * Retrieve url specified
     *
     * @return url value
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retrieve zosVR specified
     *
     * @return zosVR value
     */
    public String getZosVR() {
        return zosVR;
    }

    /**
     * Return string value representing DefinedSystem object
     *
     * @return string representation of DefinedSystem
     */
    @Override
    public String toString() {
        return "DefinedSystem{" +
                "systemNickName=" + systemNickName +
                ", groupNames=" + groupNames +
                ", cpcSerial=" + cpcSerial +
                ", zosVR=" + zosVR +
                ", systemName=" + systemName +
                ", jesType=" + jesType +
                ", sysplexName=" + sysplexName +
                ", jesMemberName=" + jesMemberName +
                ", httpProxyName=" + httpProxyName +
                ", ftpDestinationName=" + ftpDestinationName +
                ", url=" + url +
                ", cpcName=" + cpcName +
                '}';
    }

}
