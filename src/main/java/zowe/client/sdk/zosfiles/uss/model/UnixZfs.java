/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * UssZfsItem object representing a zfs item from Unix System Services (USS) list operation.
 * Immutable class using Jackson for JSON parsing.
 *
 * @author Frank Giordano
 * @version 6.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class UnixZfs {

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

    /**
     * Device identifier
     */
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
     * Count of read I/O operations
     */
    private final Long readibc;

    /**
     * Count of write I/O operations
     */
    private final Long writeibc;

    /**
     * Count of directory I/O operations
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
     * Jackson constructor for UnixZfs
     *
     * @param name         zfs name
     * @param mountpoint   mount point
     * @param fstname      FST name
     * @param status       status value
     * @param modeList     mode list string
     * @param dev          device id
     * @param fstype       file system type
     * @param bsize        block size
     * @param bavail       available blocks
     * @param blocks       block count
     * @param sysname      target system name
     * @param readibc      read I/O count
     * @param writeibc     write I/O count
     * @param diribc       directory I/O count
     * @param returnedRows returned rows
     * @param totalRows    total rows
     * @param moreRows     true if more rows are available
     */
    @JsonCreator
    public UnixZfs(
            @JsonProperty("name") String name,
            @JsonProperty("mountpoint") String mountpoint,
            @JsonProperty("fstname") String fstname,
            @JsonProperty("status") String status,
            @JsonProperty("mode") List<String> modeList,
            @JsonProperty("dev") Long dev,
            @JsonProperty("fstype") Long fstype,
            @JsonProperty("bsize") Long bsize,
            @JsonProperty("bavail") Long bavail,
            @JsonProperty("blocks") Long blocks,
            @JsonProperty("sysname") String sysname,
            @JsonProperty("readibc") Long readibc,
            @JsonProperty("writeibc") Long writeibc,
            @JsonProperty("diribc") Long diribc,
            @JsonProperty("returnedRows") Long returnedRows,
            @JsonProperty("totalRows") Long totalRows,
            @JsonProperty("moreRows") boolean moreRows) {

        this.name = name == null ? "" : name;
        this.mountpoint = mountpoint == null ? "" : mountpoint;
        this.fstname = fstname == null ? "" : fstname;
        this.status = status == null ? "" : status;
        this.mode = modeList != null ? String.join(",", modeList) : null;
        this.dev = dev == null ? 0L : dev;
        this.fstype = fstype == null ? 0L : fstype;
        this.bsize = bsize == null ? 0L : bsize;
        this.bavail = bavail == null ? 0L : bavail;
        this.blocks = blocks == null ? 0L : blocks;
        this.sysname = sysname == null ? "" : sysname;
        this.readibc = readibc == null ? 0L : readibc;
        this.writeibc = writeibc == null ? 0L : writeibc;
        this.diribc = diribc == null ? 0L : diribc;
        this.returnedRows = returnedRows == null ? 0L : returnedRows;
        this.totalRows = totalRows == null ? 0L : totalRows;
        this.moreRows = moreRows;
    }

    /**
     * Retrieve name value
     *
     * @return name string value
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieve mountpoint value
     *
     * @return mountpoint string value
     */
    public String getMountpoint() {
        return mountpoint;
    }

    /**
     * Retrieve fstname value
     *
     * @return fstname string value
     */
    public String getFstname() {
        return fstname;
    }

    /**
     * Retrieve status value
     *
     * @return status string value
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retrieve mode value
     *
     * @return mode string value
     */
    public String getMode() {
        return mode;
    }

    /**
     * Retrieve dev value
     *
     * @return dev long value
     */
    public Long getDev() {
        return dev;
    }

    /**
     * Retrieve fstype value
     *
     * @return fstype long value
     */
    public Long getFstype() {
        return fstype;
    }

    /**
     * Retrieve bsize value
     *
     * @return bsize long value
     */
    public Long getBsize() {
        return bsize;
    }

    /**
     * Retrieve bavail value
     *
     * @return bavail long value
     */
    public Long getBavail() {
        return bavail;
    }

    /**
     * Retrieve blocks value
     *
     * @return blocks long value
     */
    public Long getBlocks() {
        return blocks;
    }

    /**
     * Retrieve sysname value
     *
     * @return sysname string value
     */
    public String getSysname() {
        return sysname;
    }

    /**
     * Retrieve readibc value
     *
     * @return readibc long value
     */
    public Long getReadibc() {
        return readibc;
    }

    /**
     * Retrieve writeibc value
     *
     * @return writeibc long value
     */
    public Long getWriteibc() {
        return writeibc;
    }

    /**
     * Retrieve diribc value
     *
     * @return diribc long value
     */
    public Long getDiribc() {
        return diribc;
    }

    /**
     * Retrieve returnedRows value
     *
     * @return returnedRows long value
     */
    public Long getReturnedRows() {
        return returnedRows;
    }

    /**
     * Retrieve totalRows value
     *
     * @return totalRows long value
     */
    public Long getTotalRows() {
        return totalRows;
    }

    /**
     * Retrieve moreRows value
     *
     * @return moreRows boolean value
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

}
