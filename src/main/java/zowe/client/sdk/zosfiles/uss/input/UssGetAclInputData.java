/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input;

import zowe.client.sdk.zosfiles.uss.types.GetAclType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) getfacl operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 5.0
 */
public class UssGetAclInputData {

    /**
     * The default is 'access', displays the access ACL entries for a file or directory (getfacl -a).
     * 'dir' displays the directory default ACL entries (getfacl -d).
     * If the target is not a directory, a warning is issued.
     */
    private final GetAclType type;

    /**
     * The user ID or UID (as a JSON string),
     * displays only the ACL entries for the specified types of access control lists (getfacl -a, -d, -f)
     * which affects the specified user's access (getfacl -e user).
     */
    private final String user;

    /**
     * The default is 'false'.
     * When true, displays each ACL entry, using commas to separate the ACL entries instead of newlines.
     */
    private final boolean useCommas;

    /**
     * The default is 'false'.
     * When true, the comment header (the first three lines of each file's output) is not to be displayed (getfacl -m)
     */
    private final boolean suppressHeader;

    /**
     * The default is 'false'.
     * When true, displays only the extended ACL entries. Does not display the base ACL entries (getfacl -o).
     */
    private final boolean suppressBaseAcl;

    /**
     * UssGetAclInputData constructor
     *
     * @param builder UssGetAclInputData.Builder builder
     * @author James Kostrewski
     */
    public UssGetAclInputData(final UssGetAclInputData.Builder builder) {
        this.type = builder.type;
        this.user = builder.user;
        this.useCommas = builder.usecommas;
        this.suppressHeader = builder.suppressheader;
        this.suppressBaseAcl = builder.suppressbaseacl;
    }

    /**
     * Retrieve type value
     *
     * @return type value
     */
    public Optional<GetAclType> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Retrieve user value
     *
     * @return user value
     */
    public Optional<String> getUser() {
        return Optional.ofNullable(user);
    }

    /**
     * Retrieve useCommas value
     *
     * @return useCommas value
     */
    public boolean getUseCommas() {
        return useCommas;
    }

    /**
     * Retrieve suppressHeader value
     *
     * @return suppressHeader value
     */
    public boolean getSuppressHeader() {
        return suppressHeader;
    }

    /**
     * Retrieve suppressBaseAcl value
     *
     * @return suppressBaseAcl value
     */
    public boolean getSuppressBaseAcl() {
        return suppressBaseAcl;
    }

    /**
     * Return string value representing UssGetAclInputData object
     *
     * @return string representation of UssGetAclInputData
     */
    @Override
    public String toString() {
        return "UssGetAclInputData{" +
                "type=" + type +
                ", user=" + user +
                ", useCommas=" + useCommas +
                ", suppressHeader=" + suppressHeader +
                ", suppressBaseAcl=" + suppressBaseAcl +
                '}';
    }

    /**
     * Builder class for UssGetAclInputData
     */
    public static class Builder {

        /**
         * The default is 'access', displays the access ACL entries for a file or directory (getfacl -a).
         * 'dir' displays the directory default ACL entries (getfacl -d).
         * If the target is not a directory, a warning is issued.
         */
        private GetAclType type;

        /**
         * The user ID or UID (as a JSON string),
         * displays only the ACL entries for the specified types of access control lists (getfacl -a, -d, -f)
         * which affect the specified user's access (getfacl -e user).
         */
        private String user;

        /**
         * The default is 'false'.
         * When true, displays each ACL entry, using commas to separate the ACL entries instead of newlines.
         */
        private boolean usecommas = false;

        /**
         * The default is 'false'.
         * When true, the comment header (the first three lines of each file's output) is not to be displayed
         * (getfacl -m)
         */
        private boolean suppressheader = false;

        /**
         * The default is 'false'.
         * When true, displays only the extended ACL entries. Does not display the base ACL entries (getfacl -o).
         */
        private boolean suppressbaseacl = false;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set type value
         *
         * @param type String value representing type
         * @return Builder this object
         */
        public Builder type(final GetAclType type) {
            this.type = type;
            return this;
        }

        /**
         * Set user value
         *
         * @param user String value representing user
         * @return Builder this object
         */
        public Builder user(final String user) {
            this.user = user;
            return this;
        }

        /**
         * Set usecommas value
         *
         * @param usecommas boolean value representing usecommas
         * @return Builder this object
         */
        public Builder usecommas(final boolean usecommas) {
            this.usecommas = usecommas;
            return this;
        }

        /**
         * Set suppressheader value
         *
         * @param suppressheader boolean value representing suppressheader
         * @return Builder this object
         */
        public Builder suppressheader(final boolean suppressheader) {
            this.suppressheader = suppressheader;
            return this;
        }

        /**
         * Set suppressbaseacl value
         *
         * @param suppressbaseacl boolean value representing suppressbaseacl
         * @return Builder this object
         */
        public Builder suppressbaseacl(final boolean suppressbaseacl) {
            this.suppressbaseacl = suppressbaseacl;
            return this;
        }

        /**
         * Return UssGetAclInputData object based on Builder this object
         *
         * @return UssGetAclInputData object
         */
        public UssGetAclInputData build() {
            return new UssGetAclInputData(this);
        }

    }

}
