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

import java.util.Optional;
import java.util.OptionalLong;

/**
 * UssZfsItem object representing a zfs item from unix system services list operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UssZfsItem {

    /**
     * zfs name
     */
    public final Optional<String> name;
    /**
     * Mount point name
     */
    public final Optional<String> mountpoint;
    /**
     * FST name
     */
    public final Optional<String> fstname;
    /**
     * Status value
     */
    public final Optional<String> status;
    /**
     * Mode values
     */
    public final Optional<String> mode;
    public final OptionalLong dev;
    /**
     * File system type
     */
    public final OptionalLong fstype;
    /**
     * Block size
     */
    public final OptionalLong bsize;
    /**
     * Blocks available
     */
    public final OptionalLong bavail;
    /**
     * Count of blocks in I/O operations
     */
    public final OptionalLong blocks;
    /**
     * Target system name
     */
    public final Optional<String> sysname;
    /**
     * Count of I/O operations
     */
    public final OptionalLong readibc;
    /**
     * Count of I/O operations
     */
    public final OptionalLong writeibc;
    /**
     * Count of I/O operations
     */
    public final OptionalLong diribc;
    /**
     * The number of filesystem items returned
     */
    public final OptionalLong returnedRows;
    /**
     * The total number of filesystems
     */
    public final OptionalLong totalRows;
    /**
     * If more items than specified by X-IBM-Max-Items (or the default of 1000) match the request,
     * then moreRows will be true
     */
    public final boolean moreRows;

    public UssZfsItem(UssZfsItem.Builder builder) {
        this.name = Optional.ofNullable(builder.name);
        this.mountpoint = Optional.ofNullable(builder.mountpoint);
        this.fstname = Optional.ofNullable(builder.fstname);
        this.status = Optional.ofNullable(builder.status);
        this.mode = Optional.ofNullable(builder.mode);
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
        this.moreRows = builder.moreRows;
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

    public Optional<String> getMode() {
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

    @Override
    public String toString() {
        return "UssZfsItem{" +
                "name=" + name +
                ", mountpoint=" + mountpoint +
                ", fstname=" + fstname +
                ", status=" + status +
                ", mode=" + mode +
                ", dev=" + dev +
                ", fstype=" + fstype +
                ", bsize=" + bsize +
                ", bavail=" + bavail +
                ", blocks=" + blocks +
                ", sysname=" + sysname +
                ", readibc=" + readibc +
                ", writeibc=" + writeibc +
                ", diribc=" + diribc +
                ", returnedRows=" + returnedRows +
                ", totalRows=" + totalRows +
                ", moreRows=" + moreRows +
                '}';
    }

    public static class Builder {

        private String name;
        private String mountpoint;
        private String fstname;
        private String status;

        private String mode;
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

        public UssZfsItem build() {
            return new UssZfsItem(this);
        }

        public UssZfsItem.Builder name(String name) {
            this.name = name;
            return this;
        }

        public UssZfsItem.Builder mountpoint(String mountpoint) {
            this.mountpoint = mountpoint;
            return this;
        }

        public UssZfsItem.Builder fstname(String fstname) {
            this.fstname = fstname;
            return this;
        }

        public UssZfsItem.Builder status(String status) {
            this.status = status;
            return this;
        }

        public UssZfsItem.Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public UssZfsItem.Builder dev(Long dev) {
            this.dev = dev;
            return this;
        }

        public UssZfsItem.Builder fstype(Long fstype) {
            this.fstype = fstype;
            return this;
        }

        public UssZfsItem.Builder bsize(Long bsize) {
            this.bsize = bsize;
            return this;
        }

        public UssZfsItem.Builder bavail(Long bavail) {
            this.bavail = bavail;
            return this;
        }

        public UssZfsItem.Builder blocks(Long blocks) {
            this.blocks = blocks;
            return this;
        }

        public UssZfsItem.Builder sysname(String sysname) {
            this.sysname = sysname;
            return this;
        }

        public UssZfsItem.Builder readibc(Long readibc) {
            this.readibc = readibc;
            return this;
        }

        public UssZfsItem.Builder writeibc(Long writeibc) {
            this.writeibc = writeibc;
            return this;
        }

        public UssZfsItem.Builder diribc(Long diribc) {
            this.diribc = diribc;
            return this;
        }

        public UssZfsItem.Builder returnedRows(Long returnedRows) {
            this.returnedRows = returnedRows;
            return this;
        }

        public UssZfsItem.Builder totalRows(Long totalRows) {
            this.totalRows = totalRows;
            return this;
        }

        public UssZfsItem.Builder moreRows(boolean moreRows) {
            this.moreRows = moreRows;
            return this;
        }

    }

}
