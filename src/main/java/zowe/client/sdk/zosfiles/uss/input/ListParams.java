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

import zowe.client.sdk.zosfiles.uss.type.ListFilterType;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameter container class for unix system services list operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-files-directories-unix-file-path">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ListParams {

    /**
     * The indicator that we want to show less files
     */
    public OptionalInt maxLength;

    /**
     * The group owner or GID to filter
     */
    private final Optional<String> group;

    /**
     * The username or UID to filter
     */
    private final Optional<String> user;

    /**
     * The modification time to filter, in days
     * Valid values are either an integer, or an integer with leading plus (+) or minus (-)
     */
    private final Optional<String> mtime;

    /**
     * The size to filter
     * Valid values are either an integer, and integer with a suffix (K, M, G),
     * or an integer with leading plus (+) or minus (-)
     */
    public OptionalInt size;

    /**
     * The name of the file or directory to filter
     */
    private final Optional<String> name;

    /**
     * The permission octal mask to use
     * The type is a string because valid values are either an integer, or an integer with a leading minus (-)
     */
    private final Optional<String> perm;

    /**
     * The type of file to filter for, see ListFilterType enum object
     */
    private final Optional<ListFilterType> type;

    /**
     * The depth of the directory structure to list files and directories for
     */
    private final Optional<String> depth;

    /**
     * Whether to search all filesystems under the path, or just the same filesystem as the path
     * <p>
     * True means search all
     * False means search same
     */
    public boolean filesys;

    /**
     * Whether to follow symlinks, or report them
     * <p>
     * True means to report
     * False means to follow
     */
    public boolean symlinks;

    public ListParams(ListParams.Builder builder) {
        if (builder.maxLength == null) {
            this.maxLength = OptionalInt.empty();
        } else {
            this.maxLength = OptionalInt.of(builder.maxLength);
        }
        this.group = Optional.ofNullable(builder.group);
        this.user = Optional.ofNullable(builder.user);
        this.mtime = Optional.ofNullable(builder.mtime);
        if (builder.size == null) {
            this.size = OptionalInt.empty();
        } else {
            this.size = OptionalInt.of(builder.size);
        }
        this.name = Optional.ofNullable(builder.name);
        this.perm = Optional.ofNullable(builder.perm);
        this.type = Optional.ofNullable(builder.type);
        this.depth = Optional.ofNullable(builder.depth);
        this.filesys = builder.filesys;
        this.symlinks = builder.symlinks;
    }

    public OptionalInt getMaxLength() {
        return maxLength;
    }

    public Optional<String> getGroup() {
        return group;
    }

    public Optional<String> getUser() {
        return user;
    }

    public Optional<String> getMtime() {
        return mtime;
    }

    public OptionalInt getSize() {
        return size;
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getPerm() {
        return perm;
    }

    public Optional<ListFilterType> getType() {
        return type;
    }

    public Optional<String> getDepth() {
        return depth;
    }

    public boolean isFilesys() {
        return filesys;
    }

    public boolean isSymlinks() {
        return symlinks;
    }

    @Override
    public String toString() {
        return "ListParams{" +
                "maxLength=" + maxLength +
                ", group=" + group +
                ", user=" + user +
                ", mtime=" + mtime +
                ", size=" + size +
                ", name=" + name +
                ", perm=" + perm +
                ", type=" + type +
                ", depth=" + depth +
                ", filesys=" + filesys +
                ", symlinks=" + symlinks +
                '}';
    }

    public static class Builder {

        private Integer maxLength;
        private String group;
        private String user;
        private String mtime;
        private Integer size;
        private String name;
        private String perm;
        private ListFilterType type;
        private String depth;
        private boolean filesys = false;
        private boolean symlinks = false;

        public ListParams build() {
            return new ListParams(this);
        }

        public ListParams.Builder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public ListParams.Builder group(String group) {
            this.group = group;
            return this;
        }

        public ListParams.Builder user(String user) {
            this.user = user;
            return this;
        }

        public ListParams.Builder mtime(String mtime) {
            this.mtime = mtime;
            return this;
        }

        public ListParams.Builder size(int size) {
            this.size = size;
            return this;
        }

        public ListParams.Builder name(String name) {
            this.name = name;
            return this;
        }

        public ListParams.Builder perm(String perm) {
            this.perm = perm;
            return this;
        }

        public ListParams.Builder type(ListFilterType type) {
            this.type = type;
            return this;
        }

        public ListParams.Builder depth(String depth) {
            this.depth = depth;
            return this;
        }

        public ListParams.Builder filesys(boolean filesys) {
            this.filesys = filesys;
            return this;
        }

        public ListParams.Builder symlinks(boolean symlinks) {
            this.symlinks = symlinks;
            return this;
        }

    }

}
