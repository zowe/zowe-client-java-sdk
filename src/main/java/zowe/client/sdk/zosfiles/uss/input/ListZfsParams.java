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

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameter container class for Unix System Services (USS) list operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-list-files-directories-unix-file-path">z/OSMF REST API</a>
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ListZfsParams {

    /**
     * The indicator that we want to show less files
     */
    public final OptionalInt maxLength;
    /**
     * This parameter identifies the UNIX directory that contains the files and directories to be listed.
     * This parameter may not be specified if the 'fsname' parameter is specified. It can consist a directory or
     * fully qualified path name in the UNIX file system structure. A fully qualified file name can be up to 1023
     * bytes long. You cannot use wildcard characters for this parameter.
     */
    public final Optional<String> path;
    /**
     * This parameter identifies the fully qualified filesystem name to be listed. For zFS filesystems, this is
     * the data set name of the aggregate. This parameter may not be specified if the 'path' parameter is specified.
     */
    public final Optional<String> fsname;

    public ListZfsParams(ListZfsParams.Builder builder) {
        if (builder.maxLength == null) {
            this.maxLength = OptionalInt.empty();
        } else {
            this.maxLength = OptionalInt.of(builder.maxLength);
        }
        this.path = Optional.ofNullable(builder.path);
        this.fsname = Optional.ofNullable(builder.fsname);
    }

    public OptionalInt getMaxLength() {
        return maxLength;
    }

    public Optional<String> getPath() {
        return path;
    }

    public Optional<String> getFsname() {
        return fsname;
    }

    @Override
    public String toString() {
        return "ListZfsParams{" +
                "maxLength=" + maxLength +
                ", path=" + path +
                ", fsname=" + fsname +
                '}';
    }

    public static class Builder {

        private Integer maxLength;
        private String path;
        private String fsname;

        public ListZfsParams build() {
            return new ListZfsParams(this);
        }

        public ListZfsParams.Builder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public ListZfsParams.Builder path(String path) {
            this.path = path;
            return this;
        }

        public ListZfsParams.Builder user(String fsname) {
            this.fsname = fsname;
            return this;
        }

    }

}
