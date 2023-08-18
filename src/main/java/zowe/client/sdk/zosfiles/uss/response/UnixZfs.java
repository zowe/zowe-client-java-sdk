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
 * UssZfsItem object representing a zfs item from Unix System Services (USS) list operation
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class UnixZfs {

    /**
     * zfs name
     */
    private final Optional<String> name;

    /**
     * Mount point name
     */
    private final Optional<String> mountpoint;

    /**
     * FST name
     */
    private final Optional<String> fstname;

    /**
     * Status value
     */
    private final Optional<String> status;

    /**
     * Mode values
     */
    private final Optional<String> mode;

    private final OptionalLong dev;

    /**
     * File system type
     */
    private final OptionalLong fstype;

    /**
     * Block size
     */
    private final OptionalLong bsize;

    /**
     * Blocks available
     */
    private final OptionalLong bavail;

    /**
     * Count of blocks in I/O operations
     */
    private final OptionalLong blocks;

    /**
     * Target system name
     */
    private final Optional<String> sysname;

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
    private final OptionalLong diribc;

    /**
     * The number of filesystem items returned
     */
    private final OptionalLong returnedRows;

    /**
     * The total number of filesystems
     */
    private final OptionalLong totalRows;

    /**
     * If more items than specified by X-IBM-Max-Items (or the default of 1000) match the request,
     * then moreRows will be true
     */
    private final boolean moreRows;

    /**
     * UnixZfs constructor
     *
     * @param builder UnixZfs.Builder object
     * @author Frank Giordano
     */
    public UnixZfs(final UnixZfs.Builder builder) {
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

    /**
     * Return string value representing UnixZfs object
     *
     * @return string representation of UnixZfs
     */
    @Override
    public String toString() {
        return "UnixZfs{" +
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

    /**
     * Builder class for UnixZfs
     */
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

        public UnixZfs build() {
            return new UnixZfs(this);
        }

        public UnixZfs.Builder name(final String name) {
            this.name = name;
            return this;
        }

        public UnixZfs.Builder mountpoint(final String mountpoint) {
            this.mountpoint = mountpoint;
            return this;
        }

        public UnixZfs.Builder fstname(final String fstname) {
            this.fstname = fstname;
            return this;
        }

        public UnixZfs.Builder status(final String status) {
            this.status = status;
            return this;
        }

        public UnixZfs.Builder mode(final String mode) {
            this.mode = mode;
            return this;
        }

        public UnixZfs.Builder dev(final Long dev) {
            this.dev = dev;
            return this;
        }

        public UnixZfs.Builder fstype(final Long fstype) {
            this.fstype = fstype;
            return this;
        }

        public UnixZfs.Builder bsize(final Long bsize) {
            this.bsize = bsize;
            return this;
        }

        public UnixZfs.Builder bavail(final Long bavail) {
            this.bavail = bavail;
            return this;
        }

        public UnixZfs.Builder blocks(final Long blocks) {
            this.blocks = blocks;
            return this;
        }

        public UnixZfs.Builder sysname(final String sysname) {
            this.sysname = sysname;
            return this;
        }

        public UnixZfs.Builder readibc(final Long readibc) {
            this.readibc = readibc;
            return this;
        }

        public UnixZfs.Builder writeibc(final Long writeibc) {
            this.writeibc = writeibc;
            return this;
        }

        public UnixZfs.Builder diribc(final Long diribc) {
            this.diribc = diribc;
            return this;
        }

        public UnixZfs.Builder returnedRows(final Long returnedRows) {
            this.returnedRows = returnedRows;
            return this;
        }

        public UnixZfs.Builder totalRows(final Long totalRows) {
            this.totalRows = totalRows;
            return this;
        }

        public UnixZfs.Builder moreRows(final boolean moreRows) {
            this.moreRows = moreRows;
            return this;
        }
    }

}
