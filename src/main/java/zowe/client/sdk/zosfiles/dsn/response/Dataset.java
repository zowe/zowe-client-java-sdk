/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.response;

import java.util.Optional;

/**
 * Represents a z/OS data set
 *
 * @author Nikunj Goyal
 * @version 4.0
 */
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
     * The organization of the data set as physical sequential (PS), partitioned (PO), or direct (DA)
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
     * Indicates if automatic migration to a lower level of storage is active for this dataset
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
     * Dataset constructor
     *
     * @param builder Builder object
     * @author Nikunj Goyal
     */
    private Dataset(final Builder builder) {
        this.dsname = builder.dsname;
        this.blksz = builder.blksz;
        this.catnm = builder.catnm;
        this.cdate = builder.cdate;
        this.dev = builder.dev;
        this.dsntp = builder.dsntp;
        this.dsorg = builder.dsorg;
        this.edate = builder.edate;
        this.extx = builder.extx;
        this.lrectl = builder.lrectl;
        this.migr = builder.migr;
        this.mvol = builder.mvol;
        this.ovf = builder.ovf;
        this.rdate = builder.rdate;
        this.recfm = builder.recfm;
        this.sizex = builder.sizex;
        this.spacu = builder.spacu;
        this.used = builder.used;
        this.vol = builder.vol;
    }

    /**
     * Retrieve the name of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getDsname() {
        return Optional.ofNullable(dsname);
    }

    /**
     * Retrieve block size of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getBlksz() {
        return Optional.ofNullable(blksz);
    }

    /**
     * Retrieve catnm of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getCatnm() {
        return Optional.ofNullable(catnm);
    }

    /**
     * Retrieve creation date of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getCdate() {
        return Optional.ofNullable(cdate);
    }

    /**
     * Retrieve dev of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getDev() {
        return Optional.ofNullable(dev);
    }

    /**
     * Retrieve dsntp of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getDsntp() {
        return Optional.ofNullable(dsntp);
    }

    /**
     * Retrieve dsorg of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getDsorg() {
        return Optional.ofNullable(dsorg);
    }

    /**
     * Retrieve edate of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getEdate() {
        return Optional.ofNullable(edate);
    }

    /**
     * Retrieve extx of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getExtx() {
        return Optional.ofNullable(extx);
    }

    /**
     * Retrieve lrectl of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getLrectl() {
        return Optional.ofNullable(lrectl);
    }

    /**
     * Retrieve migr of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getMigr() {
        return Optional.ofNullable(migr);
    }

    /**
     * Retrieve mvol of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getMvol() {
        return Optional.ofNullable(mvol);
    }

    /**
     * Retrieve ovf of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getOvf() {
        return Optional.ofNullable(ovf);
    }

    /**
     * Retrieve rdate of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getRdate() {
        return Optional.ofNullable(rdate);
    }

    /**
     * Retrieve recfm of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getRecfm() {
        return Optional.ofNullable(recfm);
    }

    /**
     * Retrieve sizex of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getSizex() {
        return Optional.ofNullable(sizex);
    }

    /**
     * Retrieve spacu of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getSpacu() {
        return Optional.ofNullable(spacu);
    }

    /**
     * Retrieve used of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getUsed() {
        return Optional.ofNullable(used);
    }

    /**
     * Retrieve volume of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getVol() {
        return Optional.ofNullable(vol);
    }

    /**
     * Return string value representing a Dataset object
     *
     * @return string representation of Dataset
     */
    @Override
    public String toString() {
        return "Dataset{" +
                "dsname=" + dsname +
                ", blksz=" + blksz +
                ", catnm=" + catnm +
                ", cdate=" + cdate +
                ", dev=" + dev +
                ", dsntp=" + dsntp +
                ", dsorg=" + dsorg +
                ", edate=" + edate +
                ", extx=" + extx +
                ", lrectl=" + lrectl +
                ", migr=" + migr +
                ", mvol=" + mvol +
                ", ovf=" + ovf +
                ", rdate=" + rdate +
                ", recfm=" + recfm +
                ", sizex=" + sizex +
                ", spacu=" + spacu +
                ", used=" + used +
                ", vol=" + vol +
                '}';
    }

    /**
     * Builder class for Dataset
     */
    public static class Builder {

        /**
         * The name of the dataset
         */
        private String dsname;

        /**
         * The block size of the dataset
         */
        private String blksz;

        /**
         * The catalog in which the dataset entry is stored
         */
        private String catnm;

        /**
         * The dataset creation date
         */
        private String cdate;

        /**
         * The type of the device the dataset is stored on
         */
        private String dev;

        /**
         * The type of the dataset
         */
        private String dsntp;

        /**
         * The organization of the data set as physical sequential (PS), partitioned (PO), or direct (DA)
         */
        private String dsorg;

        /**
         * The dataset expiration date
         */
        private String edate;

        /**
         * The number of extensions the dataset has
         */
        private String extx;

        /**
         * The length, in bytes, of each record in the data set
         */
        private String lrectl;

        /**
         * Indicates if automatic migration to a lower level of storage is active for this dataset
         */
        private String migr;

        /**
         * Indicates if the dataset is multi-volume
         */
        private String mvol;

        /**
         * Open virtualization format
         */
        private String ovf;

        /**
         * The date of the last time the dataset was referred to
         */
        private String rdate;

        /**
         * The record format of the dataset
         */
        private String recfm;

        /**
         * The size of the first extent in tracks
         */
        private String sizex;

        /**
         * The type of space units measurement
         */
        private String spacu;

        /**
         * The percentage of used space in the dataset
         */
        private String used;

        /**
         * The volume name on which the dataset is stored
         */
        private String vol;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set the name of the dataset
         *
         * @param dsname string value
         * @return Builder this object
         */
        public Builder dsname(final String dsname) {
            this.dsname = dsname;
            return this;
        }

        /**
         * Set block size of the dataset
         *
         * @param blksz string value
         * @return Builder this object
         */
        public Builder blksz(final String blksz) {
            this.blksz = blksz;
            return this;
        }

        /**
         * Set catnm of the dataset
         *
         * @param catnm string value
         * @return Builder this object
         */
        public Builder catnm(final String catnm) {
            this.catnm = catnm;
            return this;
        }

        /**
         * Set cdate of the dataset
         *
         * @param cdate string value
         * @return Builder this object
         */
        public Builder cdate(final String cdate) {
            this.cdate = cdate;
            return this;
        }

        /**
         * Set dev of the dataset
         *
         * @param dev string value
         * @return Builder this object
         */
        public Builder dev(final String dev) {
            this.dev = dev;
            return this;
        }

        /**
         * Set dsntp of the dataset
         *
         * @param dsntp string value
         * @return Builder this object
         */
        public Builder dsntp(final String dsntp) {
            this.dsntp = dsntp;
            return this;
        }

        /**
         * Set dsorg of the dataset
         *
         * @param dsorg string value
         * @return Builder this object
         */
        public Builder dsorg(final String dsorg) {
            this.dsorg = dsorg;
            return this;
        }

        /**
         * Set edate of the dataset
         *
         * @param edate string value
         * @return Builder this object
         */
        public Builder edate(final String edate) {
            this.edate = edate;
            return this;
        }

        /**
         * Set extx of the dataset
         *
         * @param extx string value
         * @return Builder this object
         */
        public Builder extx(final String extx) {
            this.extx = extx;
            return this;
        }

        /**
         * Set lrectl of the dataset
         *
         * @param lrectl string value
         * @return Builder this object
         */
        public Builder lrectl(final String lrectl) {
            this.lrectl = lrectl;
            return this;
        }

        /**
         * Set migr of the dataset
         *
         * @param migr string value
         * @return Builder this object
         */
        public Builder migr(final String migr) {
            this.migr = migr;
            return this;
        }

        /**
         * Set mvol of the dataset
         *
         * @param mvol string value
         * @return Builder this object
         */
        public Builder mvol(final String mvol) {
            this.mvol = mvol;
            return this;
        }

        /**
         * Set ovf of the dataset
         *
         * @param ovf string value
         * @return Builder this object
         */
        public Builder ovf(final String ovf) {
            this.ovf = ovf;
            return this;
        }

        /**
         * Set rdate of the dataset
         *
         * @param rdate string value
         * @return Builder this object
         */
        public Builder rdate(final String rdate) {
            this.rdate = rdate;
            return this;
        }

        /**
         * Set recfm of the dataset
         *
         * @param recfm string value
         * @return Builder this object
         */
        public Builder recfm(final String recfm) {
            this.recfm = recfm;
            return this;
        }

        /**
         * Set sizex of the dataset
         *
         * @param sizex string value
         * @return Builder this object
         */
        public Builder sizex(final String sizex) {
            this.sizex = sizex;
            return this;
        }

        /**
         * Set spacu of the dataset
         *
         * @param spacu string value
         * @return Builder this object
         */
        public Builder spacu(final String spacu) {
            this.spacu = spacu;
            return this;
        }

        /**
         * Set used of the dataset
         *
         * @param used string value
         * @return Builder this object
         */
        public Builder used(final String used) {
            this.used = used;
            return this;
        }

        /**
         * Set vol of the dataset
         *
         * @param vol string value
         * @return Builder this object
         */
        public Builder vol(final String vol) {
            this.vol = vol;
            return this;
        }

        /**
         * Return Dataset object based on Builder this object
         *
         * @return Dataset object
         */
        public Dataset build() {
            return new Dataset(this);
        }

    }

}
