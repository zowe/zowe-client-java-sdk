/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a z/OS data set
 *
 * @author Nikunj Goyal
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Dataset {

    /**
     * The name of the dataset
     */
    private final String dsname;

    /**
     * The block size of the dataset
     */
    private final String blksz;

    /**
     * The catalog in which the dataset entry is stored
     */
    private final String catnm;

    /**
     * The dataset creation date
     */
    private final String cdate;

    /**
     * The type of the device the dataset is stored on
     */
    private final String dev;

    /**
     * The type of the dataset
     */
    private final String dsntp;

    /**
     * The organization of the data set as physical sequential (PS),
     * partitioned (PO), or direct (DA)
     */
    private final String dsorg;

    /**
     * The dataset expiration date
     */
    private final String edate;

    /**
     * The number of extensions the dataset has
     */
    private final String extx;

    /**
     * The length, in bytes, of each record in the data set
     */
    private final String lrectl;

    /**
     * Indicates if automatic migration to a lower level of
     * storage is active for this dataset
     */
    private final String migr;

    /**
     * Indicates if the dataset is multi-volume
     */
    private final String mvol;

    /**
     * Open virtualization format
     */
    private final String ovf;

    /**
     * The date of the last time the dataset was referred to
     */
    private final String rdate;

    /**
     * The record format of the dataset
     */
    private final String recfm;

    /**
     * The size of the first extent in tracks
     */
    private final String sizex;

    /**
     * The type of space units measurement
     */
    private final String spacu;

    /**
     * The percentage of used space in the dataset
     */
    private final String used;

    /**
     * The volume name on which the dataset is stored
     */
    private final String vol;

    /**
     * Dataset constructor for Jackson JSON parsing
     *
     * @param dsname the dataset name
     * @param blksz  block size
     * @param catnm  catalog name
     * @param cdate  creation date
     * @param dev    device type
     * @param dsntp  dataset type
     * @param dsorg  organization type
     * @param edate  expiration date
     * @param extx   number of extensions
     * @param lrectl record length
     * @param migr   migration indicator
     * @param mvol   multi-volume indicator
     * @param ovf    open virtualization format
     * @param rdate  last referenced date
     * @param recfm  record format
     * @param sizex  size of the first extent
     * @param spacu  space units
     * @param used   used percentage
     * @param vol    volume name
     * @author Nikunj Goyal
     */
    @JsonCreator
    public Dataset(
            @JsonProperty("dsname") String dsname,
            @JsonProperty("blksz") String blksz,
            @JsonProperty("catnm") String catnm,
            @JsonProperty("cdate") String cdate,
            @JsonProperty("dev") String dev,
            @JsonProperty("dsntp") String dsntp,
            @JsonProperty("dsorg") String dsorg,
            @JsonProperty("edate") String edate,
            @JsonProperty("extx") String extx,
            @JsonProperty("lrectl") String lrectl,
            @JsonProperty("migr") String migr,
            @JsonProperty("mvol") String mvol,
            @JsonProperty("ovf") String ovf,
            @JsonProperty("rdate") String rdate,
            @JsonProperty("recfm") String recfm,
            @JsonProperty("sizex") String sizex,
            @JsonProperty("spacu") String spacu,
            @JsonProperty("used") String used,
            @JsonProperty("vol") String vol
    ) {
        this.dsname = dsname != null ? dsname : "";
        this.blksz = blksz != null ? blksz : "";
        this.catnm = catnm != null ? catnm : "";
        this.dev = dev != null ? dev : "";
        this.dsntp = dsntp != null ? dsntp : "";
        this.dsorg = dsorg != null ? dsorg : "";
        this.edate = edate != null ? edate : "";
        this.extx = extx != null ? extx : "";
        this.lrectl = lrectl != null ? lrectl : "";
        this.cdate = cdate != null ? cdate : "";
        this.migr = migr != null ? migr : "";
        this.mvol = mvol != null ? mvol : "";
        this.ovf = ovf != null ? ovf : "";
        this.rdate = rdate != null ? rdate : "";
        this.recfm = recfm != null ? recfm : "";
        this.sizex = sizex != null ? sizex : "";
        this.spacu = spacu != null ? spacu : "";
        this.used = used != null ? used : "";
        this.vol = vol != null ? vol : "";
    }

    /**
     * Retrieve the name of the dataset
     *
     * @return string value
     */
    public String getDsname() {
        return dsname;
    }

    /**
     * Retrieve block size of the dataset
     *
     * @return string value
     */
    public String getBlksz() {
        return blksz;
    }

    /**
     * Retrieve catnm of the dataset
     *
     * @return string value
     */
    public String getCatnm() {
        return catnm;
    }

    /**
     * Retrieve creation date of the dataset
     *
     * @return string value
     */
    public String getCdate() {
        return cdate;
    }

    /**
     * Retrieve dev of the dataset
     *
     * @return string value
     */
    public String getDev() {
        return dev;
    }

    /**
     * Retrieve dsntp of the dataset
     *
     * @return string value
     */
    public String getDsntp() {
        return dsntp;
    }

    /**
     * Retrieve dsorg of the dataset
     *
     * @return string value
     */
    public String getDsorg() {
        return dsorg;
    }

    /**
     * Retrieve edate of the dataset
     *
     * @return string value
     */
    public String getEdate() {
        return edate;
    }

    /**
     * Retrieve extx of the dataset
     *
     * @return string value
     */
    public String getExtx() {
        return extx;
    }

    /**
     * Retrieve lrectl of the dataset
     *
     * @return string value
     */
    public String getLrectl() {
        return lrectl;
    }

    /**
     * Retrieve migr of the dataset
     *
     * @return string value
     */
    public String getMigr() {
        return migr;
    }

    /**
     * Retrieve mvol of the dataset
     *
     * @return string value
     */
    public String getMvol() {
        return mvol;
    }

    /**
     * Retrieve ovf of the dataset
     *
     * @return string value
     */
    public String getOvf() {
        return ovf;
    }

    /**
     * Retrieve rdate of the dataset
     *
     * @return string value
     */
    public String getRdate() {
        return rdate;
    }

    /**
     * Retrieve recfm of the dataset
     *
     * @return string value
     */
    public String getRecfm() {
        return recfm;
    }

    /**
     * Retrieve sizex of the dataset
     *
     * @return string value
     */
    public String getSizex() {
        return sizex;
    }

    /**
     * Retrieve spacu of the dataset
     *
     * @return string value
     */
    public String getSpacu() {
        return spacu;
    }

    /**
     * Retrieve used of the dataset
     *
     * @return string value
     */
    public String getUsed() {
        return used;
    }

    /**
     * Retrieve volume of the dataset
     *
     * @return string value
     */
    public String getVol() {
        return vol;
    }

    /**
     * Return string value representing a Dataset object
     *
     * @return string representation of Dataset
     */
    @Override
    public String toString() {
        return "Dataset{" +
                "dsname='" + dsname + '\'' +
                ", blksz='" + blksz + '\'' +
                ", catnm='" + catnm + '\'' +
                ", cdate='" + cdate + '\'' +
                ", dev='" + dev + '\'' +
                ", dsntp='" + dsntp + '\'' +
                ", dsorg='" + dsorg + '\'' +
                ", edate='" + edate + '\'' +
                ", extx='" + extx + '\'' +
                ", lrectl='" + lrectl + '\'' +
                ", migr='" + migr + '\'' +
                ", mvol='" + mvol + '\'' +
                ", ovf='" + ovf + '\'' +
                ", rdate='" + rdate + '\'' +
                ", recfm='" + recfm + '\'' +
                ", sizex='" + sizex + '\'' +
                ", spacu='" + spacu + '\'' +
                ", used='" + used + '\'' +
                ", vol='" + vol + '\'' +
                '}';
    }

}
