/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input.list;

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameter container class for Unix System Services (USS) list operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-zos-unix-filesystems">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ListZfsInput {

    /**
     * The indicator that we want to show fewer files
     */
    private final OptionalInt maxLength;

    /**
     * This parameter identifies the UNIX directory that contains the files and directories to be listed.
     * This parameter may not be specified if the 'fsname' parameter is specified. It can consist of a directory or
     * fully qualified path name in the UNIX file system structure. A fully qualified file name can be up to 1023
     * bytes long. You cannot use wildcard characters for this parameter.
     */
    private final Optional<String> path;

    /**
     * This parameter identifies the fully qualified filesystem name to be listed. For zFS filesystems, this is
     * the data set name of the aggregate. This parameter may not be specified if the 'path' parameter is specified.
     */
    private final Optional<String> fsname;

    /**
     * ListZfsParams constructor
     *
     * @param builder ListZfsParams.Builder builder
     * @author Frank Giordano
     */
    public ListZfsInput(final ListZfsInput.Builder builder) {
        if (builder.maxLength == null) {
            this.maxLength = OptionalInt.empty();
        } else {
            this.maxLength = OptionalInt.of(builder.maxLength);
        }
        this.path = Optional.ofNullable(builder.path);
        this.fsname = Optional.ofNullable(builder.fsname);
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
     * Retrieve path value
     *
     * @return path value
     */
    public Optional<String> getPath() {
        return path;
    }

    /**
     * Retrieve fsname value
     *
     * @return fsname value
     */
    public Optional<String> getFsname() {
        return fsname;
    }

    /**
     * Return string value representing ListZfsParams object
     *
     * @return string representation of ListZfsParams
     */
    @Override
    public String toString() {
        return "ListZfsParams{" +
                "maxLength=" + maxLength +
                ", path=" + path +
                ", fsname=" + fsname +
                '}';
    }

    /**
     * Builder class for ListZfsParams
     */
    public static class Builder {

        /**
         * The indicator that we want to show fewer files
         */
        private Integer maxLength;

        /**
         * This parameter identifies the UNIX directory that contains the files and directories to be listed.
         * This parameter may not be specified if the 'fsname' parameter is specified. It can consist of a directory or
         * fully qualified path name in the UNIX file system structure. A fully qualified file name can be up to 1023
         * bytes long. You cannot use wildcard characters for this parameter.
         */
        private String path;

        /**
         * This parameter identifies the fully qualified filesystem name to be listed. For zFS filesystems, this is
         * the data set name of the aggregate. This parameter may not be specified if the 'path' parameter is specified.
         */
        private String fsname;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set maxLength int value
         *
         * @param maxLength the maxLength int value
         * @return Builder this object
         */
        public Builder maxLength(final int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        /**
         * Set path value
         *
         * @param path string value
         * @return Builder this object
         */
        public Builder path(final String path) {
            ValidateUtils.checkNullParameter(path == null, "path is null");
            ValidateUtils.checkIllegalParameter(path.isBlank(), "path not specified");
            if (this.fsname != null) {
                throw new IllegalStateException("cannot specify both path and fsname parameters");
            }
            this.path = path;
            return this;
        }

        /**
         * Set fsname value
         *
         * @param fsname string value
         * @return Builder this object
         */
        public Builder fsname(final String fsname) {
            ValidateUtils.checkNullParameter(fsname == null, "fsname is null");
            ValidateUtils.checkIllegalParameter(fsname.isBlank(), "fsname not specified");
            if (this.path != null) {
                throw new IllegalStateException("cannot specify both path and fsname parameters");
            }
            this.fsname = fsname;
            return this;
        }

        /**
         * Return ListZfsParams object based on Builder this object
         *
         * @return ListZfsParams object
         */
        public ListZfsInput build() {
            return new ListZfsInput(this);
        }

    }

}
