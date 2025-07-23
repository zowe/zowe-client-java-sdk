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
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) chown operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public class ChangeOwnerParams {

    /**
     * The user ID or UID
     */
    private final Optional<String> owner;

    /**
     * The group ID or GID
     */
    private final Optional<String> group;

    /**
     * The default is false. When 'true', changes all the files and subdirectories in that directory to
     * belong to the specified owner (and group if :group is specified). (chown -R)
     */
    private final boolean recursive;

    /**
     * The default is 'follow'. 'change' does not follow the link, but instead changes the link itself (chown -h).
     */
    private final Optional<LinkType> links;

    /**
     * ChangeOwnerParams constructor
     *
     * @param builder ChangeOwnerParams.Builder builder
     * @author James Kostrewski
     */
    public ChangeOwnerParams(final ChangeOwnerParams.Builder builder) {
        this.owner = Optional.ofNullable(builder.owner);
        this.group = Optional.ofNullable(builder.group);
        this.recursive = builder.recursive;
        this.links = Optional.ofNullable(builder.links);
    }

    /**
     * Retrieve owner value
     *
     * @return owner value
     */
    public Optional<String> getOwner() {
        return owner;
    }

    /**
     * Retrieve group value
     *
     * @return group value
     */
    public Optional<String> getGroup() {
        return group;
    }

    /**
     * Is recursive specified
     *
     * @return boolean true or false
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * Retrieve linkType value
     *
     * @return linkType value
     */
    public Optional<LinkType> getLinks() {
        return links;
    }

    /**
     * Return string value representing ChangeOwnerParams object
     *
     * @return string representation of ChangeOwnerParams
     */
    @Override
    public String toString() {
        return "ChangeOwnerParams{" +
                "owner='" + owner + '\'' +
                ", group=" + group +
                ", recursive=" + recursive +
                ", links=" + links +
                '}';
    }

    /**
     * Builder class for ChangeOwnerParams
     */
    public static class Builder {

        /**
         * The user ID or UID
         */
        private String owner;

        /**
         * The group ID or GID
         */
        private String group;

        /**
         * The default is false. When 'true', changes all the files and subdirectories in that directory to
         * belong to the specified owner (and group if :group is specified). (chown -R)
         */
        private boolean recursive = false;

        /**
         * The default is 'follow'. 'change' does not follow the link, but instead changes the link itself (chown -h).
         */
        private LinkType links;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set owner string value
         *
         * @param owner string value
         * @return Builder this object
         */
        public Builder owner(final String owner) {
            ValidateUtils.checkNullParameter(owner == null, "owner is null");
            ValidateUtils.checkIllegalParameter(owner.isBlank(), "owner not specified");
            this.owner = owner;
            return this;
        }

        /**
         * Set group string value
         *
         * @param group string value
         * @return Builder this object
         */
        public Builder group(final String group) {
            this.group = group;
            return this;
        }

        /**
         * Set recursive boolean value
         *
         * @param recursive boolean true or false value
         * @return Builder this object
         */
        public Builder recursive(final boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        /**
         * Set links LinkType value
         *
         * @param type LinkType type object
         * @return Builder this object
         */
        public Builder links(final LinkType type) {
            this.links = type;
            return this;
        }

        /**
         * Return ChangeOwnerParams object based on Builder this object
         *
         * @return ChangeOwnerParams object
         */
        public ChangeOwnerParams build() {
            return new ChangeOwnerParams(this);
        }

    }

}
