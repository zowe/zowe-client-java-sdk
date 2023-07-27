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
    private final String owner;

    /**
     * The group ID or GID
     */
    private final Optional<String> group;

    /**
     * The default is false. When 'true', changes all the files and subdirectories in that directory to
     * belong to the specified owner (and group, if :group is specified). (chown -R)
     */
    private boolean recursive;

    public ChangeOwnerParams(ChangeOwnerParams.Builder builder) {
        this.owner = builder.owner;
        this.group = Optional.ofNullable(builder.group);
        this.recursive = builder.recursive;
    }

    public String getOwner() {
        return owner;
    }

    public Optional<String> getGroup() {
        return group;
    }

    public boolean isRecursive() {
        return recursive;
    }

    @Override
    public String toString() {
        return "ChangeOwnerParams{" +
                "owner=" + this.getOwner() +
                ", group=" + this.getGroup().get() +
                ", recursive=" + this.isRecursive() +
                '}';
    }

    public static class Builder {

        private String owner;
        private String group;
        private boolean recursive = false;

        public ChangeOwnerParams build() {
            return new ChangeOwnerParams(this);
        }

        public ChangeOwnerParams.Builder owner(String owner) {
            ValidateUtils.checkNullParameter(owner == null, "owner is null");
            ValidateUtils.checkIllegalParameter(owner.isEmpty(), "owner is empty");
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
    }

}
