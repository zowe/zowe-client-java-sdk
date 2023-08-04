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
 *
 * @author James Kostrewski
 * @version 2.0
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
     * belong to the specified owner (and group, if :group is specified). (chown -R)
     */
    private final boolean recursive;

    /**
     * The default is 'follow'. 'change' does not follow the link, but instead changes the link itself (chown -h).
     */
    private final Optional<LinkType> linkType;

    /**
     * ChangeOwnerParams constructor
     *
     * @params ChangeOwnerParams.Builder builder
     * @author James Kostrewski
     */
    public ChangeOwnerParams(ChangeOwnerParams.Builder builder) {
        this.owner = Optional.ofNullable(builder.owner);
        this.group = Optional.ofNullable(builder.group);
        this.recursive = builder.recursive;
        this.linkType = Optional.ofNullable(builder.linkType);
    }

    /**
     * Retrieve owner value
     *
     * @return owner value
     * @author James Kostrewski
     */
    public Optional<String> getOwner() {
        return owner;
    }

    /**
     * Retrieve group value
     *
     * @return group value
     * @author James Kostrewski
     */
    public Optional<String> getGroup() {
        return group;
    }

    /**
     * Is recursive value true or false
     *
     * @return recursive value
     * @author James Kostrewski
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * Retrieve linkType value
     *
     * @return linkType value
     * @author James Kostrewski
     */
    public Optional<LinkType> getLinkType() {
        return linkType;
    }

    @Override
    public String toString() {
        return "ChangeOwnerParams{" +
                "owner='" + owner + '\'' +
                ", group=" + group +
                ", recursive=" + recursive +
                ", linkType=" + linkType +
                '}';
    }

    public static class Builder {

        private String owner;
        private String group;
        private boolean recursive = false;
        private LinkType linkType;

        public ChangeOwnerParams build() {
            return new ChangeOwnerParams(this);
        }

        public ChangeOwnerParams.Builder owner(String owner) {
            ValidateUtils.checkNullParameter(owner == null, "owner is null");
            ValidateUtils.checkIllegalParameter(owner.trim().isEmpty(), "owner not specified");
            this.owner = owner;
            return this;
        }

        public ChangeOwnerParams.Builder group(String group) {
            this.group = group;
            return this;
        }

        public ChangeOwnerParams.Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        public ChangeOwnerParams.Builder linktype(LinkType type) {
            this.linkType = type;
            return this;
        }

    }

}
