/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles.response;

import java.util.Optional;

/**
 * Represents a z/OS data set
 *
 * @version 1.0
 */
public class Dataset {

    private Optional<String> dsname;
    private Optional<String> blksz;
    private Optional<String> catnm;
    private Optional<String> cdate;
    private Optional<String> dev;
    private Optional<String> dsntp;
    private Optional<String> dsorg;
    private Optional<String> edate;
    private Optional<String> extx;
    private Optional<String> lrectl;
    private Optional<String> migr;
    private Optional<String> mvol;
    private Optional<String> ovf;
    private Optional<String> rdate;
    private Optional<String> recfm;
    private Optional<String> sizex;
    private Optional<String> spacu;
    private Optional<String> used;
    private Optional<String> vol;

    public Dataset(Dataset.Builder builder) {
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

    public Optional<String> getDsname() {
        return dsname;
    }

    public Optional<String> getBlksz() {
        return blksz;
    }

    public Optional<String> getCatnm() {
        return catnm;
    }

    public Optional<String> getCdate() {
        return cdate;
    }

    public Optional<String> getDev() {
        return dev;
    }

    public Optional<String> getDsntp() {
        return dsntp;
    }

    public Optional<String> getDsorg() {
        return dsorg;
    }

    public Optional<String> getEdate() {
        return edate;
    }

    public Optional<String> getExtx() {
        return extx;
    }

    public Optional<String> getLrectl() {
        return lrectl;
    }

    public Optional<String> getMigr() {
        return migr;
    }

    public Optional<String> getMvol() {
        return mvol;
    }

    public Optional<String> getOvf() {
        return ovf;
    }

    public Optional<String> getRdate() {
        return rdate;
    }

    public Optional<String> getRecfm() {
        return recfm;
    }

    public Optional<String> getSizex() {
        return sizex;
    }

    public Optional<String> getSpacu() {
        return spacu;
    }

    public Optional<String> getUsed() {
        return used;
    }

    public Optional<String> getVol() {
        return vol;
    }

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
