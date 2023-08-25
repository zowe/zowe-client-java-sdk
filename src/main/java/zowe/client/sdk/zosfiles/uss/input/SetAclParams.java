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

import zowe.client.sdk.zosfiles.uss.types.DeleteType;
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) setfacl operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class SetAclParams {

    /**
     * The default is false. When true, aborts processing if an error or warning occurs.
     * See the setfacl command documentation for complete documentation on the errors and warnings (setfacl -a).
     */
    private boolean abort;

    /**
     * The default is 'follow'. 'suppress' does not follow symbolic links.
     * Because ACLs are not associated with symbolic links,
     * nothing happens if a symbolic link is encountered (setfacl -h).
     * <p>
     * Note:  At least one of the following four keywords must be specified.
     * 'modify' and 'delete' may both be specified, but not with 'delete-type' and 'set'.
     */
    private Optional<LinkType> links;

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
    private Optional<DeleteType> deleteType;

    /**
     * sets (replaces) all ACLs with 'entries'.
     * 'entries' represents a string of ACL entries.
     * Refer to the setfacl command reference for the string format (setfacl -s entries).
     * <p>
     * Note: The 'set' keyword cannot be specified with 'delete-type', 'modify' or 'delete'.
     */
    private Optional<String> set;

    /**
     * Modifies the ACL entries. 'entries' represents a string of ACL entries.
     * Refer to the setfacl command reference for the string format.
     * If an ACL entry does not exist for a user or group that is specified in 'entries', it is created.
     * If an ACL entry exists for a user or group that was specified in 'entries', it is replaced.
     * <p>
     * Note: The 'modify' keyword cannot be specified with 'delete-type' or 'set'.
     */
    private Optional<String> modify;

    /**
     * Deletes the extended ACL entries that are specified by 'entries'. 'entries' is a string of ACL entries.
     * Refer to the setfacl command reference for the string format.
     * If an ACL entry does not exist for the user or group specified, no error is issued.
     * <p>
     * Note: The 'delete' keyword cannot be specified with 'delete-type' or 'set'.
     */
    private Optional<String> delete;

    /**
     * SetAclParams constructor
     *
     * @param builder SetAclParams.Builder builder
     */
    public SetAclParams(final SetAclParams.Builder builder) {
        this.abort = builder.abort;
        this.links = Optional.ofNullable(builder.links);
        this.deleteType = Optional.ofNullable(builder.deleteType);
        this.set = Optional.ofNullable(builder.set);
        this.modify = Optional.ofNullable(builder.modify);
        this.delete = Optional.ofNullable(builder.delete);
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
     */
    public Optional<LinkType> getLinks() {
        return links;
    }

    /**
     * Retrieve deleteType value
     */
    public Optional<DeleteType> getDeleteType() {
        return deleteType;
    }

    /**
     * Retrieve set value
     */
    public Optional<String> getSet() {
        return set;
    }

    /**
     * Retrieve modify value
     */
    public Optional<String> getModify() {
        return modify;
    }

    /**
     * Retrieve delete value
     */
    public Optional<String> getDelete() {
        return delete;
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
        private DeleteType deleteType;

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
         * If an ACL entry does not exist for a user or group that is specified in 'entries', it is created.
         * If an ACL entry exists for a user or group that was specified in 'entries', it is replaced.
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
        public SetAclParams.Builder setAbort(final boolean abort) {
            this.abort = abort;
            return this;
        }

        /**
         * Set links value
         *
         * @param links links value
         * @return SetAclParams.Builder
         */
        public SetAclParams.Builder setLinks(final LinkType links) {
            this.links = links;
            return this;
        }

        /**
         * Set deleteType value
         *
         * @param deleteType deleteType value
         * @return SetAclParams.Builder
         */
        public SetAclParams.Builder setDeleteType(final DeleteType deleteType) {
            this.deleteType = deleteType;
            return this;
        }

        /**
         * Set set value
         *
         * @param set set value
         * @return SetAclParams.Builder
         */
        public SetAclParams.Builder setSet(final String set) {
            this.set = set;
            return this;
        }

        /**
         * Set modify value
         *
         * @param modify modify value
         * @return SetAclParams.Builder
         */
        public SetAclParams.Builder setModify(final String modify) {
            this.modify = modify;
            return this;
        }

        /**
         * Set delete value
         *
         * @param delete delete value
         * @return SetAclParams.Builder
         */
        public SetAclParams.Builder setDelete(final String delete) {
            this.delete = delete;
            return this;
        }

        /**
         * Build SetAclParams object
         *
         * @return SetAclParams
         */
        public SetAclParams build() {
            return new SetAclParams(this);
        }

    }

}
