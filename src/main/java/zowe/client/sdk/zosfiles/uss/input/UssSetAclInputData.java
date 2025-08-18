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

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosfiles.uss.types.DeleteAclType;
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) setfacl operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public class UssSetAclInputData {

    /**
     * The default is false. When true, aborts processing if an error or warning occurs.
     * See the setfacl command documentation for complete documentation on the errors and warnings (setfacl -a).
     */
    private final boolean abort;

    /**
     * The default is 'follow'. 'suppress' does not follow symbolic links.
     * Because ACLs are not associated with symbolic links,
     * nothing happens if a symbolic link is encountered (setfacl -h).
     * <p>
     * Note: At least one of the following four keywords must be specified.
     * 'modify' and 'delete' may both be specified, but not with 'delete-type' and 'set'.
     */
    private final LinkType links;

    /**
     * Delete all extended ACL entries by type (setfacl -D type):
     * <p>
     * access - Access ACL
     * dir    - Directory default ACL
     * file   - File default ACL
     * every  - Every extended ACL for all ACL types that are applicable for the current path name.
     * <p>
     * Note: The 'delete-type' keyword cannot be specified with 'set', 'modify' or 'delete'.
     */
    private final DeleteAclType deleteType;

    /**
     * sets (replaces) all ACLs with 'entries'.
     * 'entries' represents a string of ACL entries.
     * Refer to the setfacl command reference for the string format (setfacl -s entries).
     * <p>
     * Note: The 'set' keyword cannot be specified with 'delete-type', 'modify' or 'delete'.
     */
    private final String set;

    /**
     * Modifies the ACL entries. 'entries' represents a string of ACL entries.
     * Refer to the setfacl command reference for the string format.
     * If an ACL entry does not exist for a user or group specified in 'entries', it is created.
     * If an ACL entry exists for a user or group specified in 'entries', it is replaced.
     * <p>
     * Note: The 'modify' keyword cannot be specified with 'delete-type' or 'set'.
     */
    private final String modify;

    /**
     * Deletes the extended ACL entries that are specified by 'entries'. 'entries' is a string of ACL entries.
     * Refer to the setfacl command reference for the string format.
     * If an ACL entry does not exist for the user or group specified, no error is issued.
     * <p>
     * Note: The 'delete' keyword cannot be specified with 'delete-type' or 'set'.
     */
    private final String delete;

    /**
     * SetAclParams constructor
     *
     * @param builder SetAclParams.Builder builder
     */
    public UssSetAclInputData(final UssSetAclInputData.Builder builder) {
        this.abort = builder.abort;
        this.links = builder.links;
        this.deleteType = builder.deleteType;
        this.set = builder.set;
        this.modify = builder.modify;
        this.delete = builder.delete;
    }

    /**
     * Retrieve abort value
     *
     * @return abort value
     */
    public boolean isAbort() {
        return abort;
    }

    /**
     * Retrieve links value
     *
     * @return links value
     */
    public Optional<LinkType> getLinks() {
        return Optional.ofNullable(links);
    }

    /**
     * Retrieve deleteType value
     *
     * @return deleteType value
     */
    public Optional<DeleteAclType> getDeleteType() {
        return Optional.ofNullable(deleteType);
    }

    /**
     * Retrieve set value
     *
     * @return set value
     */
    public Optional<String> getSet() {
        return Optional.ofNullable(set);
    }

    /**
     * Retrieve modify value
     *
     * @return modify value
     */
    public Optional<String> getModify() {
        return Optional.ofNullable(modify);
    }

    /**
     * Retrieve delete value
     *
     * @return delete value
     */
    public Optional<String> getDelete() {
        return Optional.ofNullable(delete);
    }

    /**
     * Return string value representing SetAclParams object
     *
     * @return string representation of SetAclParams
     */
    @Override
    public String toString() {
        return "SetAclParams{" +
                "abort=" + abort +
                ", links=" + links +
                ", deleteType=" + deleteType +
                ", set=" + set +
                ", modify=" + modify +
                ", delete=" + delete +
                '}';
    }

    /**
     * Builder class for SetAclParams
     */
    public static class Builder {

        /**
         * The default is false. When true, aborts processing if an error or warning occurs.
         * See the setfacl command documentation for complete documentation on the errors and warnings (setfacl -a).
         */
        private boolean abort = false;

        /**
         * The default is 'follow'. 'suppress' does not follow symbolic links.
         * Because ACLs are not associated with symbolic links,
         * nothing happens if a symbolic link is encountered (setfacl -h).
         * <p>
         * Note:  At least one of the following four keywords must be specified.
         * 'modify' and 'delete' may both be specified, but not with 'delete-type' and 'set'.
         */
        private LinkType links = LinkType.FOLLOW;

        /**
         * Delete all extended ACL entries by type (setfacl -D type):
         * <p>
         * access - Access ACL
         * dir    - Directory default ACL
         * file   - File default ACL
         * every  - Every extended ACL for all ACL types that are applicable for the current path name.
         * <p>
         * Note: The 'delete-type' keyword cannot be specified with 'set', 'modify' or 'delete'.
         */
        private DeleteAclType deleteType;

        /**
         * sets (replaces) all ACLs with 'entries'.
         * 'entries' represents a string of ACL entries.
         * Refer to the setfacl command reference for the string format (setfacl -s entries).
         * <p>
         * Note: The 'set' keyword cannot be specified with 'delete-type', 'modify' or 'delete'.
         */
        private String set;

        /**
         * Modifies the ACL entries. 'entries' represents a string of ACL entries.
         * Refer to the setfacl command reference for the string format.
         * If an ACL entry does not exist for a user or group specified in 'entries', it is created.
         * If an ACL entry exists for a user or group specified in 'entries', it is replaced.
         * <p>
         * Note: The 'modify' keyword cannot be specified with 'delete-type' or 'set'.
         */
        private String modify;

        /**
         * Deletes the extended ACL entries that are specified by 'entries'. 'entries' is a string of ACL entries.
         * Refer to the setfacl command reference for the string format.
         * If an ACL entry does not exist for the user or group specified, no error is issued.
         * <p>
         * Note: The 'delete' keyword cannot be specified with 'delete-type' or 'set'.
         */
        private String delete;

        /**
         * Set abort value
         *
         * @param abort abort value
         * @return SetAclParams.Builder
         */
        public UssSetAclInputData.Builder setAbort(final boolean abort) {
            this.abort = abort;
            return this;
        }

        /**
         * Set links value
         *
         * @param links links value
         * @return SetAclParams.Builder
         */
        public UssSetAclInputData.Builder setLinks(final LinkType links) {
            this.links = links;
            return this;
        }

        /**
         * Set deleteType value
         *
         * @param deleteType deleteType value
         * @return SetAclParams.Builder
         */
        public UssSetAclInputData.Builder setDeleteType(final DeleteAclType deleteType) {
            ValidateUtils.checkNullParameter(deleteType == null, "deleteType is null");
            this.deleteType = deleteType;
            return this;
        }

        /**
         * Set the SET value
         *
         * @param set set value
         * @return SetAclParams.Builder
         */
        public UssSetAclInputData.Builder setSet(final String set) {
            ValidateUtils.checkNullParameter(set == null, "set is null");
            ValidateUtils.checkIllegalParameter(set.isBlank(), "set not specified");
            this.set = set;
            return this;
        }

        /**
         * Set modify value
         *
         * @param modify modify value
         * @return SetAclParams.Builder
         */
        public UssSetAclInputData.Builder setModify(final String modify) {
            ValidateUtils.checkNullParameter(modify == null, "modify is null");
            ValidateUtils.checkIllegalParameter(modify.isBlank(), "modify not specified");
            this.modify = modify;
            return this;
        }

        /**
         * Set delete value
         *
         * @param delete delete value
         * @return SetAclParams.Builder
         */
        public UssSetAclInputData.Builder setDelete(final String delete) {
            ValidateUtils.checkNullParameter(delete == null, "delete is null");
            ValidateUtils.checkIllegalParameter(delete.isBlank(), "delete not specified");
            this.delete = delete;
            return this;
        }

        /**
         * Build SetAclParams object
         *
         * @return SetAclParams
         */
        public UssSetAclInputData build() {
            return new UssSetAclInputData(this);
        }

    }

}
