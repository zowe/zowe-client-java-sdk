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
 * @version 4.0
 */
public class UnixZfs {

    /**
     * zfs name
     */
    private final String name;

    /**
     * Mount point name
     */
    private final String mountpoint;

    /**
     * FST name
     */
    private final String fstname;

    /**
     * Status value
     */
    private final String status;

    /**
     * Mode values
     */
    private final String mode;

    private final Long dev;

    /**
     * File system type
     */
    private final Long fstype;

    /**
     * Block size
     */
    private final Long bsize;

    /**
     * Blocks available
     */
    private final Long bavail;

    /**
     * Count of blocks in I/O operations
     */
    private final Long blocks;

    /**
     * Target system name
     */
    private final String sysname;

    /**
     * Count of I/O operations
     */
    public final Long readibc;

    /**
     * Count of I/O operations
     */
    public final Long writeibc;

    /**
     * Count of I/O operations
     */
    private final Long diribc;

    /**
     * The number of filesystem items returned
     */
    private final Long returnedRows;

    /**
     * The total number of filesystems
     */
    private final Long totalRows;

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
        this.name = builder.name;
        this.mountpoint = builder.mountpoint;
        this.fstname = builder.fstname;
        this.status = builder.status;
        this.mode = builder.mode;
        this.dev = builder.dev;
        this.fstype = builder.fstype;
        this.bsize = builder.bsize;
        this.bavail = builder.bavail;
        this.blocks = builder.blocks;
        this.sysname = builder.sysname;
        this.readibc = builder.readibc;
        this.writeibc = builder.writeibc;
        this.diribc = builder.diribc;
        this.returnedRows = builder.returnedRows;
        this.totalRows = builder.totalRows;
        this.moreRows = builder.moreRows;
    }

    /**
     * Retrieve name value
     *
     * @return name value
     */
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    /**
     * Retrieve mountpoint value
     *
     * @return mountpoint value
     */
    public Optional<String> getMountpoint() {
        return Optional.ofNullable(mountpoint);
    }

    /**
     * Retrieve fstname value
     *
     * @return fstname value
     */
    public Optional<String> getFstname() {
        return Optional.ofNullable(fstname);
    }

    /**
     * Retrieve status value
     *
     * @return status value
     */
    public Optional<String> getStatus() {
        return Optional.ofNullable(status);
    }

    /**
     * Retrieve mode value
     *
     * @return mode value
     */
    public Optional<String> getMode() {
        return Optional.ofNullable(mode);
    }

    /**
     * Retrieve dev value
     *
     * @return dev value
     */
    public OptionalLong getDev() {
        return (dev == null) ? OptionalLong.empty() : OptionalLong.of(dev);
    }

    /**
     * Retrieve fstype value
     *
     * @return fstype value
     */
    public OptionalLong getFstype() {
        return (fstype == null) ? OptionalLong.empty() : OptionalLong.of(fstype);
    }

    /**
     * Retrieve bsize value
     *
     * @return bsize value
     */
    public OptionalLong getBsize() {
        return (bsize == null) ? OptionalLong.empty() : OptionalLong.of(bsize);
    }

    /**
     * Retrieve bavail value
     *
     * @return bavail value
     */
    public OptionalLong getBavail() {
        return (bavail == null) ? OptionalLong.empty() : OptionalLong.of(bavail);
    }

    /**
     * Retrieve blocks value
     *
     * @return blocks value
     */
    public OptionalLong getBlocks() {
        return (blocks == null) ? OptionalLong.empty() : OptionalLong.of(blocks);
    }

    /**
     * Retrieve sysname value
     *
     * @return sysname value
     */
    public Optional<String> getSysname() {
        return Optional.ofNullable(sysname);
    }

    /**
     * Retrieve readibc value
     *
     * @return readibc value
     */
    public OptionalLong getReadibc() {
        return (readibc == null) ? OptionalLong.empty() : OptionalLong.of(readibc);
    }

    /**
     * Retrieve writeibc value
     *
     * @return writeibc value
     */
    public OptionalLong getWriteibc() {
        return (writeibc == null) ? OptionalLong.empty() : OptionalLong.of(writeibc);
    }

    /**
     * Retrieve diribc value
     *
     * @return diribc value
     */
    public OptionalLong getDiribc() {
        return (diribc == null) ? OptionalLong.empty() : OptionalLong.of(diribc);
    }

    /**
     * Retrieve returnedRows value
     *
     * @return returnedRows value
     */
    public OptionalLong getReturnedRows() {
        return (returnedRows == null) ? OptionalLong.empty() : OptionalLong.of(returnedRows);
    }

    /**
     * Retrieve totalRows value
     *
     * @return totalRows value
     */
    public OptionalLong getTotalRows() {
        return (totalRows == null) ? OptionalLong.empty() : OptionalLong.of(totalRows);
    }

    /**
     * Retrieve moreRows value
     *
     * @return moreRows value
     */
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

        /**
         * zfs name
         */
        private String name;

        /**
         * Mount point name
         */
        private String mountpoint;

        /**
         * FST name
         */
        private String fstname;

        /**
         * Status value
         */
        private String status;

        /**
         * Mode values
         */
        private String mode;

        /**
         * dev value
         */
        private Long dev;

        /**
         * File system type
         */
        private Long fstype;

        /**
         * Block size
         */
        private Long bsize;

        /**
         * Blocks available
         */
        private Long bavail;

        /**
         * Count of blocks in I/O operations
         */
        private Long blocks;

        /**
         * Target system name
         */
        private String sysname;

        /**
         * Count of I/O operations
         */
        private Long readibc;

        /**
         * Count of I/O operations
         */
        private Long writeibc;

        /**
         * Count of I/O operations
         */
        private Long diribc;

        /**
         * The number of filesystem items returned
         */
        private Long returnedRows;

        /**
         * The total number of filesystems
         */
        private Long totalRows;

        /**
         * If more items than specified by X-IBM-Max-Items (or the default of 1000) match the request,
         * then moreRows will be true
         */
        private boolean moreRows = false;

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
         * Set mountpoint string value
         *
         * @param mountpoint string value
         * @return Builder this object
         */
        public Builder mountpoint(final String mountpoint) {
            this.mountpoint = mountpoint;
            return this;
        }

        /**
         * Set fstname string value
         *
         * @param fstname string value
         * @return Builder this object
         */
        public Builder fstname(final String fstname) {
            this.fstname = fstname;
            return this;
        }

        /**
         * Set status string value
         *
         * @param status string value
         * @return Builder this object
         */
        public Builder status(final String status) {
            this.status = status;
            return this;
        }

        /**
         * Set mode string value
         *
         * @param mode string value
         * @return Builder this object
         */
        public Builder mode(final String mode) {
            this.mode = mode;
            return this;
        }

        /**
         * Set dev long value
         *
         * @param dev long value
         * @return Builder this object
         */
        public Builder dev(final Long dev) {
            this.dev = dev;
            return this;
        }

        /**
         * Set fstype long value
         *
         * @param fstype long value
         * @return Builder this object
         */
        public Builder fstype(final Long fstype) {
            this.fstype = fstype;
            return this;
        }

        /**
         * Set bsize long value
         *
         * @param bsize long value
         * @return Builder this object
         */
        public Builder bsize(final Long bsize) {
            this.bsize = bsize;
            return this;
        }

        /**
         * Set bavail long value
         *
         * @param bavail long value
         * @return Builder this object
         */
        public Builder bavail(final Long bavail) {
            this.bavail = bavail;
            return this;
        }

        /**
         * Set blocks long value
         *
         * @param blocks long value
         * @return Builder this object
         */
        public Builder blocks(final Long blocks) {
            this.blocks = blocks;
            return this;
        }

        /**
         * Set sysname string value
         *
         * @param sysname string value
         * @return Builder this object
         */
        public Builder sysname(final String sysname) {
            this.sysname = sysname;
            return this;
        }

        /**
         * Set readibc long value
         *
         * @param readibc long value
         * @return Builder this object
         */
        public Builder readibc(final Long readibc) {
            this.readibc = readibc;
            return this;
        }

        /**
         * Set writeibc long value
         *
         * @param writeibc long value
         * @return Builder this object
         */
        public Builder writeibc(final Long writeibc) {
            this.writeibc = writeibc;
            return this;
        }

        /**
         * Set diribc long value
         *
         * @param diribc long value
         * @return Builder this object
         */
        public Builder diribc(final Long diribc) {
            this.diribc = diribc;
            return this;
        }

        /**
         * Set returnedRows long value
         *
         * @param returnedRows long value
         * @return Builder this object
         */
        public Builder returnedRows(final Long returnedRows) {
            this.returnedRows = returnedRows;
            return this;
        }

        /**
         * Set totalRows long value
         *
         * @param totalRows long value
         * @return Builder this object
         */
        public Builder totalRows(final Long totalRows) {
            this.totalRows = totalRows;
            return this;
        }

        /**
         * Set moreRows boolean value
         *
         * @param moreRows boolean true or false value
         * @return Builder this object
         */
        public Builder moreRows(final boolean moreRows) {
            this.moreRows = moreRows;
            return this;
        }

        /**
         * Return UnixZfs object based on Builder this object
         *
         * @return UnixZfs this object
         */
        public UnixZfs build() {
            return new UnixZfs(this);
        }

    }

}
