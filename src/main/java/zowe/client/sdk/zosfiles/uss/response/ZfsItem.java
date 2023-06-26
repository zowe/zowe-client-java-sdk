/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.response;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * ListZfsItem object representing a zfs item from unix system services list operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZfsItem {

    public Optional<String> name;
    public Optional<String> mountpoint;
    public Optional<String> fstname;
    public Optional<String> status;
    public List<String> mode;
    public OptionalLong dev;
    public OptionalLong fstype;
    public OptionalLong bsize;
    public OptionalLong bavail;
    public OptionalLong blocks;
    public Optional<String> sysname;
    public OptionalLong readibc;
    public OptionalLong writeibc;
    public OptionalLong diribc;
    public OptionalLong returnedRows;
    public OptionalLong totalRows;
    public boolean moreRows;

    public ZfsItem(ZfsItem.Builder builder) {
        this.name = Optional.ofNullable(builder.name);
        this.mountpoint = Optional.ofNullable(builder.mountpoint);
        this.fstname = Optional.ofNullable(builder.fstname);
        this.status = Optional.ofNullable(builder.status);
        this.mode = builder.mode;
        if (builder.dev == null) {
            this.dev = OptionalLong.empty();
        } else {
            this.dev = OptionalLong.of(builder.dev);
        }
        if (builder.fstype == null) {
            this.fstype = OptionalLong.empty();
        } else {
            this.fstype = OptionalLong.of(builder.fstype);
        }
        if (builder.bsize == null) {
            this.bsize = OptionalLong.empty();
        } else {
            this.bsize = OptionalLong.of(builder.bsize);
        }
        if (builder.bavail == null) {
            this.bavail = OptionalLong.empty();
        } else {
            this.bavail = OptionalLong.of(builder.bavail);
        }
        if (builder.blocks == null) {
            this.blocks = OptionalLong.empty();
        } else {
            this.blocks = OptionalLong.of(builder.blocks);
        }
        this.sysname = Optional.ofNullable(builder.sysname);
        if (builder.readibc == null) {
            this.readibc = OptionalLong.empty();
        } else {
            this.readibc = OptionalLong.of(builder.readibc);
        }
        if (builder.writeibc == null) {
            this.writeibc = OptionalLong.empty();
        } else {
            this.writeibc = OptionalLong.of(builder.writeibc);
        }
        if (builder.diribc == null) {
            this.diribc = OptionalLong.empty();
        } else {
            this.diribc = OptionalLong.of(builder.diribc);
        }
        if (builder.returnedRows == null) {
            this.returnedRows = OptionalLong.empty();
        } else {
            this.returnedRows = OptionalLong.of(builder.returnedRows);
        }
        if (builder.totalRows == null) {
            this.totalRows = OptionalLong.empty();
        } else {
            this.totalRows = OptionalLong.of(builder.totalRows);
        }
        this.moreRows = moreRows;
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getMountpoint() {
        return mountpoint;
    }

    public Optional<String> getFstname() {
        return fstname;
    }

    public Optional<String> getStatus() {
        return status;
    }

    public List<String> getMode() {
        return mode;
    }

    public OptionalLong getDev() {
        return dev;
    }

    public OptionalLong getFstype() {
        return fstype;
    }

    public OptionalLong getBsize() {
        return bsize;
    }

    public OptionalLong getBavail() {
        return bavail;
    }

    public OptionalLong getBlocks() {
        return blocks;
    }

    public Optional<String> getSysname() {
        return sysname;
    }

    public OptionalLong getReadibc() {
        return readibc;
    }

    public OptionalLong getWriteibc() {
        return writeibc;
    }

    public OptionalLong getDiribc() {
        return diribc;
    }

    public OptionalLong getReturnedRows() {
        return returnedRows;
    }

    public OptionalLong getTotalRows() {
        return totalRows;
    }

    public boolean isMoreRows() {
        return moreRows;
    }

    public static class Builder {

        private String name;
        private String mountpoint;
        private String fstname;
        private String status;

        private List<String> mode;
        private Long dev;
        private Long fstype;
        private Long bsize;
        private Long bavail;
        private Long blocks;
        private String sysname;
        private Long readibc;
        private Long writeibc;
        private Long diribc;
        private Long returnedRows;
        private Long totalRows;
        private boolean moreRows = false;

        public ZfsItem build() {
            return new ZfsItem(this);
        }

        public ZfsItem.Builder name(String name) {
            this.name = name;
            return this;
        }

        public ZfsItem.Builder mountpoint(String mountpoint) {
            this.mountpoint = mountpoint;
            return this;
        }

        public ZfsItem.Builder fstname(String fstname) {
            this.fstname = fstname;
            return this;
        }

        public ZfsItem.Builder status(String status) {
            this.status = status;
            return this;
        }

        public ZfsItem.Builder mode(List<String> mode) {
            this.mode = mode;
            return this;
        }

        public ZfsItem.Builder dev(Long dev) {
            this.dev = dev;
            return this;
        }

        public ZfsItem.Builder fstype(Long fstype) {
            this.fstype = fstype;
            return this;
        }

        public ZfsItem.Builder bsize(Long bsize) {
            this.bsize = bsize;
            return this;
        }

        public ZfsItem.Builder bavail(Long bavail) {
            this.bavail = bavail;
            return this;
        }

        public ZfsItem.Builder blocks(Long blocks) {
            this.blocks = blocks;
            return this;
        }

        public ZfsItem.Builder sysname(String sysname) {
            this.sysname = sysname;
            return this;
        }

        public ZfsItem.Builder readibc(Long readibc) {
            this.readibc = readibc;
            return this;
        }

        public ZfsItem.Builder writeibc(Long writeibc) {
            this.writeibc = writeibc;
            return this;
        }

        public ZfsItem.Builder diribc(Long diribc) {
            this.diribc = diribc;
            return this;
        }

        public ZfsItem.Builder returnedRows(Long returnedRows) {
            this.returnedRows  = returnedRows;
            return this;
        }

        public ZfsItem.Builder moreRows(boolean moreRows) {
            this.moreRows  = moreRows;
            return this;
        }

    }

}
