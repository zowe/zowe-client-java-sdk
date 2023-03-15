/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.response;

import java.util.Optional;

/**
 * Represents a z/OS data set
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class Dataset {

    /**
     * The name of the dataset
     */
    private final Optional<String> dsname;

    /**
     * The block size of the dataset
     */
    private final Optional<String> blksz;

    /**
     * The catalog in which the dataset entry is stored
     */
    private final Optional<String> catnm;

    /**
     * The dataset creation date
     */
    private final Optional<String> cdate;

    /**
     * The type of the device the dataset is stored on
     */
    private final Optional<String> dev;

    /**
     * The type of the dataset
     */
    private final Optional<String> dsntp;

    /**
     * The organization of the data set as physical sequential (PS), partitioned (PO), or direct (DA)
     */
    private final Optional<String> dsorg;

    /**
     * The dataset expiration date
     */
    private final Optional<String> edate;

    /**
     * The number of extensions the dataset has
     */
    private final Optional<String> extx;

    /**
     * The length, in bytes, of each record in the data set
     */
    private final Optional<String> lrectl;

    /**
     * Indicates if automatic migration to a lower level of storage is active for this dataset
     */
    private final Optional<String> migr;

    /**
     * Indicates if the dataset is multi-volume
     */
    private final Optional<String> mvol;

    /**
     * Open virtualization format
     */
    private final Optional<String> ovf;

    /**
     * The date of the last time the dataset was referred to
     */
    private final Optional<String> rdate;

    /**
     * The record format of the dataset
     */
    private final Optional<String> recfm;

    /**
     * The size of the first extent in tracks
     */
    private final Optional<String> sizex;

    /**
     * The type of space units measurement
     */
    private final Optional<String> spacu;

    /**
     * The percentage of used space in the dataset
     */
    private final Optional<String> used;

    /**
     * The volume name on which the dataset is stored
     */
    private final Optional<String> vol;

    private Dataset(Dataset.Builder builder) {
        this.dsname = Optional.ofNullable(builder.dsname);
        this.blksz = Optional.ofNullable(builder.blksz);
        this.catnm = Optional.ofNullable(builder.catnm);
        this.cdate = Optional.ofNullable(builder.cdate);
        this.dev = Optional.ofNullable(builder.dev);
        this.dsntp = Optional.ofNullable(builder.dsntp);
        this.dsorg = Optional.ofNullable(builder.dsorg);
        this.edate = Optional.ofNullable(builder.edate);
        this.extx = Optional.ofNullable(builder.extx);
        this.lrectl = Optional.ofNullable(builder.lrectl);
        this.migr = Optional.ofNullable(builder.migr);
        this.mvol = Optional.ofNullable(builder.mvol);
        this.ovf = Optional.ofNullable(builder.ovf);
        this.rdate = Optional.ofNullable(builder.rdate);
        this.recfm = Optional.ofNullable(builder.recfm);
        this.sizex = Optional.ofNullable(builder.sizex);
        this.spacu = Optional.ofNullable(builder.spacu);
        this.used = Optional.ofNullable(builder.used);
        this.vol = Optional.ofNullable(builder.vol);
    }

    /**
     * Retrieve name of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getDsname() {
        return dsname;
    }

    /**
     * Retrieve block size of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getBlksz() {
        return blksz;
    }

    /**
     * Retrieve catnm of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getCatnm() {
        return catnm;
    }

    /**
     * Retrieve creation date of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getCdate() {
        return cdate;
    }

    /**
     * Retrieve dev of the dataset
     *
     * @return Optional string value
     */
    public Optional<String> getDev() {
        return dev;
    }

    /**
     * Retrieve dsntp of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getDsntp() {
        return dsntp;
    }

    /**
     * Retrieve dsorg of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getDsorg() {
        return dsorg;
    }

    /**
     * Retrieve edate of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getEdate() {
        return edate;
    }

    /**
     * Retrieve extx of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getExtx() {
        return extx;
    }

    /**
     * Retrieve lrectl of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getLrectl() {
        return lrectl;
    }

    /**
     * Retrieve migr of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getMigr() {
        return migr;
    }

    /**
     * Retrieve mvol of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getMvol() {
        return mvol;
    }

    /**
     * Retrieve ovf of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getOvf() {
        return ovf;
    }

    /**
     * Retrieve rdate of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getRdate() {
        return rdate;
    }

    /**
     * Retrieve recfm of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getRecfm() {
        return recfm;
    }

    /**
     * Retrieve sizex of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getSizex() {
        return sizex;
    }

    /**
     * Retrieve spacu of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getSpacu() {
        return spacu;
    }

    /**
     * Retrieve used of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getUsed() {
        return used;
    }

    /**
     * Retrieve volume of the dataset
     *
     * @return Optional string value
     * @author Nikunj Goyal
     */
    public Optional<String> getVol() {
        return vol;
    }

    /**
     * String representation of the Dataset object
     *
     * @return string value
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

    public static class Builder {

        private String dsname;
        private String blksz;
        private String catnm;
        private String cdate;
        private String dev;
        private String dsntp;
        private String dsorg;
        private String edate;
        private String extx;
        private String lrectl;
        private String migr;
        private String mvol;
        private String ovf;
        private String rdate;
        private String recfm;
        private String sizex;
        private String spacu;
        private String used;
        private String vol;

        public Dataset.Builder dsname(String dsname) {
            this.dsname = dsname;
            return this;
        }

        public Dataset.Builder blksz(String blksz) {
            this.blksz = blksz;
            return this;
        }

        public Dataset.Builder catnm(String catnm) {
            this.catnm = catnm;
            return this;
        }

        public Dataset.Builder cdate(String cdate) {
            this.cdate = cdate;
            return this;
        }

        public Dataset.Builder dev(String dev) {
            this.dev = dev;
            return this;
        }

        public Dataset.Builder dsntp(String dsntp) {
            this.dsntp = dsntp;
            return this;
        }

        public Dataset.Builder dsorg(String dsorg) {
            this.dsorg = dsorg;
            return this;
        }

        public Dataset.Builder edate(String edate) {
            this.edate = edate;
            return this;
        }

        public Dataset.Builder extx(String extx) {
            this.extx = extx;
            return this;
        }

        public Dataset.Builder lrectl(String lrectl) {
            this.lrectl = lrectl;
            return this;
        }

        public Dataset.Builder migr(String migr) {
            this.migr = migr;
            return this;
        }

        public Dataset.Builder mvol(String mvol) {
            this.mvol = mvol;
            return this;
        }

        public Dataset.Builder ovf(String ovf) {
            this.ovf = ovf;
            return this;
        }

        public Dataset.Builder rdate(String rdate) {
            this.rdate = rdate;
            return this;
        }

        public Dataset.Builder recfm(String recfm) {
            this.recfm = recfm;
            return this;
        }

        public Dataset.Builder sizex(String sizex) {
            this.sizex = sizex;
            return this;
        }

        public Dataset.Builder spacu(String spacu) {
            this.spacu = spacu;
            return this;
        }

        public Dataset.Builder used(String used) {
            this.used = used;
            return this;
        }

        public Dataset.Builder vol(String vol) {
            this.vol = vol;
            return this;
        }

        public Dataset build() {
            return new Dataset(this);
        }

    }

}
