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
 * The z/OSMF object returned for every defined system.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class DefinedSystem {

    /**
     * Unique name assigned to the system definition.
     */
    private final Optional<String> systemNickName;

    /**
     * Comma-separated list of the groups to which the system is assigned.
     */
    private final Optional<String> groupNames;

    /**
     * Serial number of the CPC.
     */
    private final Optional<String> cpcSerial;

    /**
     * Version of z/OS
     */
    private final Optional<String> zosVR;

    /**
     * Name specified for the system on the SYSNAME parameter in the IEASYSxx parmlib member.
     */
    private final Optional<String> systemName;

    /**
     * Type for the primary job entry subsystem running on the system. The type is either JES2 or JES3.
     */
    private final Optional<String> jesType;

    /**
     * Name of the sysplex where the z/OS system is a member.
     */
    private final Optional<String> sysplexName;

    /**
     * JES2 multi-access spool (MAS) member name or JES3 complex member name
     */
    private final Optional<String> jesMemberName;

    /**
     * Name of the HTTP proxy definition that specifies the settings required to access the system through
     * an HTTP or SOCKS proxy server.
     */
    private final Optional<String> httpProxyName;

    /**
     * Name of the server definition that specifies the settings required to access the FTP or SFTP server
     * that is running on the system.
     */
    private final Optional<String> ftpDestinationName;

    /**
     * URL used to access the z/OSMF instance that resides in the same sysplex as the system identified by
     * the systemName attribute.
     */
    private final Optional<String> url;

    /**
     * Name specified for the central processor complex (CPC) at the support element (SE) of that processor complex.
     */
    private final Optional<String> cpcName;

    /**
     * DefinedSystem constructor
     *
     * @param builder Builder Object
     * @author Frank Giordano
     */
    private DefinedSystem(final Builder builder) {
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

    /**
     * Retrieve cpcName specified
     *
     * @return cpcName value
     */
    public Optional<String> getCpcName() {
        return cpcName;
    }

    /**
     * Retrieve cpcSerial specified
     *
     * @return cpcSerial value
     */
    public Optional<String> getCpcSerial() {
        return cpcSerial;
    }

    /**
     * Retrieve ftpDestinationName specified
     *
     * @return ftpDestinationName value
     */
    public Optional<String> getFtpDestinationName() {
        return ftpDestinationName;
    }

    /**
     * Retrieve groupNames specified
     *
     * @return groupNames value
     */
    public Optional<String> getGroupNames() {
        return groupNames;
    }

    /**
     * Retrieve httpProxyName specified
     *
     * @return httpProxyName value
     */
    public Optional<String> getHttpProxyName() {
        return httpProxyName;
    }

    /**
     * Retrieve jesMemberName specified
     *
     * @return jesMemberName value
     */
    public Optional<String> getJesMemberName() {
        return jesMemberName;
    }

    /**
     * Retrieve jesType specified
     *
     * @return jesType value
     */
    public Optional<String> getJesType() {
        return jesType;
    }

    /**
     * Retrieve sysplexName specified
     *
     * @return sysplexName value
     */
    public Optional<String> getSysplexName() {
        return sysplexName;
    }

    /**
     * Retrieve systemName specified
     *
     * @return systemName value
     */
    public Optional<String> getSystemName() {
        return systemName;
    }

    /**
     * Retrieve systemNickName specified
     *
     * @return systemNickName value
     */
    public Optional<String> getSystemNickName() {
        return systemNickName;
    }

    /**
     * Retrieve url specified
     *
     * @return url value
     */
    public Optional<String> getUrl() {
        return url;
    }

    /**
     * Retrieve zosVR specified
     *
     * @return zosVR value
     */
    public Optional<String> getZosVR() {
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

    /**
     * Builder class for DefinedSystem
     */
    public static class Builder {

        /**
         * Unique name assigned to the system definition.
         */
        private String systemNickName;

        /**
         * Comma-separated list of the groups to which the system is assigned.
         */
        private String groupNames;

        /**
         * Serial number of the CPC.
         */
        private String cpcSerial;

        /**
         * Version of z/OS
         */
        private String zosVR;

        /**
         * Name specified for the system on the SYSNAME parameter in the IEASYSxx parmlib member.
         */
        private String systemName;

        /**
         * Type for the primary job entry subsystem running on the system. The type is either JES2 or JES3.
         */
        private String jesType;

        /**
         * Name of the sysplex where the z/OS system is a member.
         */
        private String sysplexName;

        /**
         * JES2 multi-access spool (MAS) member name or JES3 complex member name
         */
        private String jesMemberName;

        /**
         * Name of the HTTP proxy definition that specifies the settings required to access the system through
         * an HTTP or SOCKS proxy server.
         */
        private String httpProxyName;

        /**
         * Name of the server definition that specifies the settings required to access the FTP or SFTP server
         * that is running on the system.
         */
        private String ftpDestinationName;

        /**
         * URL used to access the z/OSMF instance that resides in the same sysplex as the system identified by
         * the systemName attribute.
         */
        private String url;

        /**
         * Name specified for the central processor complex (CPC) at the support element (SE) of that processor complex.
         */
        private String cpcName;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set cpcName string value
         *
         * @param cpcName string value
         * @return Builder this object
         */
        public Builder cpcName(final String cpcName) {
            this.cpcName = cpcName;
            return this;
        }

        /**
         * Set cpcSerial string value
         *
         * @param cpcSerial string value
         * @return Builder this object
         */
        public Builder cpcSerial(final String cpcSerial) {
            this.cpcSerial = cpcSerial;
            return this;
        }

        /**
         * Set ftpDestinationName string value
         *
         * @param ftpDestinationName string value
         * @return Builder this object
         */
        public Builder ftpDestinationName(final String ftpDestinationName) {
            this.ftpDestinationName = ftpDestinationName;
            return this;
        }

        /**
         * Set groupNames string value
         *
         * @param groupNames string value
         * @return Builder this object
         */
        public Builder groupNames(final String groupNames) {
            this.groupNames = groupNames;
            return this;
        }

        /**
         * Set httpProxyName string value
         *
         * @param httpProxyName string value
         * @return Builder this object
         */
        public Builder httpProxyName(final String httpProxyName) {
            this.httpProxyName = httpProxyName;
            return this;
        }

        /**
         * Set jesMemberName string value
         *
         * @param jesMemberName string value
         * @return Builder this object
         */
        public Builder jesMemberName(final String jesMemberName) {
            this.jesMemberName = jesMemberName;
            return this;
        }

        /**
         * Set jesType string value
         *
         * @param jesType string value
         * @return Builder this object
         */
        public Builder jesType(final String jesType) {
            this.jesType = jesType;
            return this;
        }

        /**
         * Set sysplexName string value
         *
         * @param sysplexName string value
         * @return Builder this object
         */
        public Builder sysplexName(final String sysplexName) {
            this.sysplexName = sysplexName;
            return this;
        }

        /**
         * Set systemName string value
         *
         * @param systemName string value
         * @return Builder this object
         */
        public Builder systemName(final String systemName) {
            this.systemName = systemName;
            return this;
        }

        /**
         * Set systemNickName string value
         *
         * @param systemNickName string value
         * @return Builder this object
         */
        public Builder systemNickName(final String systemNickName) {
            this.systemNickName = systemNickName;
            return this;
        }

        /**
         * Set url string value
         *
         * @param url string value
         * @return Builder this object
         */
        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        /**
         * Set zosVR string value
         *
         * @param zosVR string value
         * @return Builder this object
         */
        public Builder zosVR(final String zosVR) {
            this.zosVR = zosVR;
            return this;
        }

        /**
         * Return DefinedSystem object based on Builder this object
         *
         * @return DefinedSystem this object
         */
        public DefinedSystem build() {
            return new DefinedSystem(this);
        }

    }

}
