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
import zowe.client.sdk.zosfiles.uss.types.ListFilterType;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameter container class for Unix System Services (USS) list operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-files-directories-unix-file-path">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ListParams {

    /**
     * This parameter identifies the UNIX directory that contains the files and directories to be listed.
     * This parameter is required and can consist of one or more directories in the hierarchical file system structure,
     * or a fully qualified file name. A fully qualified file name, which consists of the name of each directory in
     * the path to a file plus the file name itself, can be up to 1023 bytes long. You cannot use wildcard characters
     * for this parameter.
     * <p>
     * The following list contains sample file path names:
     * /
     * /bin
     * /usr/lib/libSM.a
     */
    private final Optional<String> path;

    /**
     * The indicator that we want to show fewer files
     */
    private final OptionalInt maxLength;

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
     * Valid values is either an integer, and integer with a suffix (K, M, G),
     * or an integer with leading plus (+) or minus (-)
     */
    private final OptionalInt size;

    /**
     * Select entries that match pattern according to the rules of fnmatch().
     * The supplied pattern is matched against the absolute path of the entry,
     * with behavior similar to the find -name option.
     */
    private final Optional<String> name;

    /**
     * The permission octal mask to use
     * The type is a string because valid values are either an integer or an integer with a leading minus (-)
     */
    private final Optional<String> perm;

    /**
     * The type of file to filter for, see ListFilterType enum object
     */
    private final Optional<ListFilterType> type;

    /**
     * The default value for this parameter is 0, which means that all subdirectories under a path are listed,
     * regardless of depth. When the depth is greater than 1, subdirectories up to the specified depth are listed.
     * depth is 1, only the files in the path are listed.
     * <p>
     * The name field in the returned JSON document contains the path of the entry,
     * relative to the path query parameter.
     */
    private final OptionalInt depth;

    /**
     * Whether to search all filesystems under the path, or just the same filesystem as the path
     * <p>
     * True means search all
     * False means search same
     */
    public final boolean filesys;

    /**
     * Whether to follow symlinks, or report them
     * <p>
     * True means to report
     * False means to follow
     */
    public final boolean symlinks;

    /**
     * ListParams constructor
     *
     * @param builder ListParams.Builder builder
     * @author Frank Giordano
     */
    public ListParams(final ListParams.Builder builder) {
        this.path = Optional.of(builder.path);
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
        if (builder.depth == null) {
            this.depth = OptionalInt.empty();
        } else {
            this.depth = OptionalInt.of(builder.depth);
        }
        this.filesys = builder.filesys;
        this.symlinks = builder.symlinks;
    }

    /**
     * Retrieve path value
     *
     * @return path value
     */
    public Optional<String> getPath() {
        return path;
    }

    /**
     * Retrieve maxLength value
     *
     * @return maxLength value
     */
    public OptionalInt getMaxLength() {
        return maxLength;
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
     * Retrieve user value
     *
     * @return user value
     */
    public Optional<String> getUser() {
        return user;
    }

    /**
     * Retrieve mtime value
     *
     * @return mtime value
     */
    public Optional<String> getMtime() {
        return mtime;
    }

    /**
     * Retrieve size value
     *
     * @return size value
     */
    public OptionalInt getSize() {
        return size;
    }

    /**
     * Retrieve name value
     *
     * @return name value
     */
    public Optional<String> getName() {
        return name;
    }

    /**
     * Retrieve perm value
     *
     * @return perm value
     */
    public Optional<String> getPerm() {
        return perm;
    }

    /**
     * Retrieve type value
     *
     * @return type value
     */
    public Optional<ListFilterType> getType() {
        return type;
    }

    /**
     * Retrieve depth value
     *
     * @return depth value
     */
    public OptionalInt getDepth() {
        return depth;
    }

    /**
     * Retrieve is filesys specified
     *
     * @return boolean true or false
     */
    public boolean isFilesys() {
        return filesys;
    }

    /**
     * Retrieve is symlinks specified
     *
     * @return boolean true or false
     */
    public boolean isSymlinks() {
        return symlinks;
    }

    /**
     * Return string value representing ListParams object
     *
     * @return string representation of ListParams
     */
    @Override
    public String toString() {
        return "ListParams{" +
                "path=" + path +
                ", maxLength=" + maxLength +
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

    /**
     * Builder class for ListParams
     */
    public static class Builder {

        /**
         * This parameter identifies the UNIX directory that contains the files and directories to be listed.
         * This parameter is required and can consist of one or more directories in the hierarchical file system structure,
         * or a fully qualified file name. A fully qualified file name, which consists of the name of each directory in
         * the path to a file plus the file name itself, can be up to 1023 bytes long. You cannot use wildcard characters
         * for this parameter.
         * <p>
         * The following list contains sample file path names:
         * /
         * /bin
         * /usr/lib/libSM.a
         */
        private String path;

        /**
         * The indicator that we want to show fewer files
         */
        private Integer maxLength;

        /**
         * The group owner or GID to filter
         */
        private String group;


        /**
         * The username or UID to filter
         */
        private String user;

        /**
         * The modification time to filter, in days
         * Valid values aren't either an integer or an integer with leading plus (+) or minus (-)
         */
        private String mtime;

        /**
         * The size to filter
         * Valid values is either an integer, and integer with a suffix (K, M, G),
         * or an integer with leading plus (+) or minus (-)
         */
        private Integer size;

        /**
         * Select entries that match the pattern according to the rules of fnmatch().
         * The supplied pattern is matched against the absolute path of the entry,
         * with behavior similar to the find -name option.
         */
        private String name;

        /**
         * The permission octal mask to use
         * The type is a string because valid values are either an integer or an integer with a leading minus (-)
         */
        private String perm;

        /**
         * The type of file to filter for, see ListFilterType enum object
         */
        private ListFilterType type;

        /**
         * The default value for this parameter is 0, which means that all subdirectories under a path are listed,
         * regardless of depth. When the depth is greater than 1, subdirectories up to the specified depth are listed.
         * depth is 1, only the files in the path are listed.
         * <p>
         * The name field in the returned JSON document contains the path of the entry,
         * relative to the path query parameter.
         */
        private Integer depth;

        /**
         * Whether to search all filesystems under the path, or just the same filesystem as the path
         * <p>
         * True means search all
         * False means search same
         */
        private boolean filesys = false;

        /**
         * Whether to follow symlinks, or report them
         * <p>
         * True means to report
         * False means to follow
         */
        private boolean symlinks = false;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set path string value
         *
         * @param path string value
         * @return Builder this object
         */
        public Builder path(final String path) {
            ValidateUtils.checkNullParameter(path == null, "path is null");
            ValidateUtils.checkIllegalParameter(path.isBlank(), "path not specified");
            this.path = path;
            return this;
        }

        /**
         * Set maxLength int value
         *
         * @param maxLength int value
         * @return Builder this object
         */
        public Builder maxLength(final int maxLength) {
            this.maxLength = maxLength;
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
         * Set user string value
         *
         * @param user string value
         * @return Builder this object
         */
        public ListParams.Builder user(final String user) {
            this.user = user;
            return this;
        }

        /**
         * Specify mtime string value
         *
         * @param mtime string value
         * @return Builder this object
         */
        public Builder mtime(final String mtime) {
            this.mtime = mtime;
            return this;
        }

        /**
         * Set size int value
         *
         * @param size int value
         * @return Builder this object
         */
        public Builder size(final int size) {
            this.size = size;
            return this;
        }

        /**
         * Set name string value
         *
         * @param name string value
         * @return Builder this object
         */
        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        /**
         * Set perm string value
         *
         * @param perm string value
         * @return Builder this object
         */
        public Builder perm(final String perm) {
            this.perm = perm;
            return this;
        }

        /**
         * Set ListFilterType value
         *
         * @param type ListFilterType type object
         * @return Builder this object
         */
        public Builder type(final ListFilterType type) {
            this.type = type;
            return this;
        }

        /**
         * Set depth int value
         *
         * @param depth int value
         * @return Builder this object
         */
        public Builder depth(final int depth) {
            this.depth = depth;
            return this;
        }

        /**
         * Set filesys boolean value
         *
         * @param filesys boolean true or false value
         * @return Builder this object
         */
        public Builder filesys(final boolean filesys) {
            this.filesys = filesys;
            return this;
        }

        /**
         * Set symlinks boolean value
         *
         * @param symlinks boolean true or false value
         * @return Builder this object
         */
        public Builder symlinks(final boolean symlinks) {
            this.symlinks = symlinks;
            return this;
        }

        /**
         * Return ListParams object based on Builder this object
         *
         * @return ListParams object
         */
        public ListParams build() {
            return new ListParams(this);
        }

    }

}

