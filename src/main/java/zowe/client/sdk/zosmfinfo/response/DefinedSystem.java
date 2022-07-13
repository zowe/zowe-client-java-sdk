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
 * The Z/OSMF object returned for every defined system.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class DefinedSystem {

    /**
     * Unique name assigned to the system definition.
     */
    public final Optional<String> systemNickName;

    /**
     * Comma-separated list of the groups to which the system is assigned.
     */
    public final Optional<String> groupNames;

    /**
     * Serial number of the CPC.
     */
    public final Optional<String> cpcSerial;

    /**
     * Version of z/OS
     */
    public final Optional<String> zosVR;

    /**
     * Name specified for the system on the SYSNAME parameter in the IEASYSxx parmlib member.
     */
    public final Optional<String> systemName;

    /**
     * Type for the primary job entry subsystem running on the system. The type is either JES2 or JES3.
     */
    public final Optional<String> jesType;

    /**
     * Name of the sysplex where the z/OS system is a member.
     */
    public final Optional<String> sysplexName;

    /**
     * JES2 multi-access spool (MAS) member name or JES3 complex member name
     */
    public final Optional<String> jesMemberName;

    /**
     * Name of the HTTP proxy definition that specifies the settings required to access the system through
     * an HTTP or SOCKS proxy server.
     */
    public final Optional<String> httpProxyName;

    /**
     * Name of the server definition that specifies the settings required to access the FTP or SFTP server
     * that is running on the system.
     */
    public final Optional<String> ftpDestinationName;

    /**
     * URL used to access the z/OSMF instance that resides in the same sysplex as the system identified by
     * the systemName attribute.
     */
    public final Optional<String> url;

    /**
     * Name specified for the central processor complex (CPC) at the support element (SE) of that processor complex.
     */
    public final Optional<String> cpcName;

    /**
     * DefinedSystem Constructor.
     *
     * @param builder DefinedSystem.Builder Object
     * @author Frank Giordano
     */
    private DefinedSystem(DefinedSystem.Builder builder) {
        this.systemNickName = Optional.ofNullable(builder.systemNickName);
        this.groupNames = Optional.ofNullable(builder.groupNames);
        this.cpcSerial = Optional.ofNullable(builder.cpcSerial);
        this.zosVR = Optional.ofNullable(builder.zosVR);
        this.systemName = Optional.ofNullable(builder.systemName);
        this.jesType = Optional.ofNullable(builder.jesType);
        this.sysplexName = Optional.ofNullable(builder.sysplexName);
        this.jesMemberName = Optional.ofNullable(builder.jesMemberName);
        this.httpProxyName = Optional.ofNullable(builder.httpProxyName);
        this.ftpDestinationName = Optional.ofNullable(builder.ftpDestinationName);
        this.url = Optional.ofNullable(builder.url);
        this.cpcName = Optional.ofNullable(builder.cpcName);
    }

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

    public static class Builder {

        private String systemNickName;
        private String groupNames;
        private String cpcSerial;
        private String zosVR;
        private String systemName;
        private String jesType;
        private String sysplexName;
        private String jesMemberName;
        private String httpProxyName;
        private String ftpDestinationName;
        private String url;
        private String cpcName;

        public DefinedSystem.Builder systemNickName(String systemNickName) {
            this.systemNickName = systemNickName;
            return this;
        }

        public DefinedSystem.Builder groupNames(String groupNames) {
            this.groupNames = groupNames;
            return this;
        }

        public DefinedSystem.Builder cpcSerial(String cpcSerial) {
            this.cpcSerial = cpcSerial;
            return this;
        }

        public DefinedSystem.Builder zosVR(String zosVR) {
            this.zosVR = zosVR;
            return this;
        }

        public DefinedSystem.Builder systemName(String systemName) {
            this.systemName = systemName;
            return this;
        }

        public DefinedSystem.Builder jesType(String jesType) {
            this.jesType = jesType;
            return this;
        }

        public DefinedSystem.Builder sysplexName(String sysplexName) {
            this.sysplexName = sysplexName;
            return this;
        }

        public DefinedSystem.Builder jesMemberName(String jesMemberName) {
            this.jesMemberName = jesMemberName;
            return this;
        }

        public DefinedSystem.Builder httpProxyName(String httpProxyName) {
            this.httpProxyName = httpProxyName;
            return this;
        }

        public DefinedSystem.Builder ftpDestinationName(String ftpDestinationName) {
            this.ftpDestinationName = ftpDestinationName;
            return this;
        }

        public DefinedSystem.Builder url(String url) {
            this.url = url;
            return this;
        }

        public DefinedSystem.Builder cpcName(String cpcName) {
            this.cpcName = cpcName;
            return this;
        }

        public DefinedSystem build() {
            return new DefinedSystem(this);
        }

    }

}
