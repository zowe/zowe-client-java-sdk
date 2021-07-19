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
        if (builder.dsname != null)
            this.dsname = Optional.of(builder.dsname);
        else this.dsname = Optional.empty();

        if (builder.blksz != null)
            this.blksz = Optional.of(builder.blksz);
        else this.blksz = Optional.empty();

        if (builder.catnm != null)
            this.catnm = Optional.of(builder.catnm);
        else this.catnm = Optional.empty();

        if (builder.cdate != null)
            this.cdate = Optional.of(builder.cdate);
        else this.cdate = Optional.empty();

        if (builder.dev != null)
            this.dev = Optional.of(builder.dev);
        else this.dev = Optional.empty();

        if (builder.dsntp != null)
            this.dsntp = Optional.of(builder.dsntp);
        else this.dsntp = Optional.empty();

        if (builder.dsorg != null)
            this.dsorg = Optional.of(builder.dsorg);
        else this.dsorg = Optional.empty();

        if (builder.edate != null)
            this.edate = Optional.of(builder.edate);
        else this.edate = Optional.empty();

        if (builder.extx != null)
            this.extx = Optional.of(builder.extx);
        else this.extx = Optional.empty();

        if (builder.lrectl != null)
            this.lrectl = Optional.of(builder.lrectl);
        else this.lrectl = Optional.empty();

        if (builder.migr != null)
            this.migr = Optional.of(builder.migr);
        else this.migr = Optional.empty();

        if (builder.mvol != null)
            this.mvol = Optional.of(builder.mvol);
        else this.mvol = Optional.empty();

        if (builder.ovf != null)
            this.ovf = Optional.of(builder.ovf);
        else this.ovf = Optional.empty();

        if (builder.rdate != null)
            this.rdate = Optional.of(builder.rdate);
        else this.rdate = Optional.empty();

        if (builder.recfm != null)
            this.recfm = Optional.of(builder.recfm);
        else this.recfm = Optional.empty();

        if (builder.sizex != null)
            this.sizex = Optional.of(builder.sizex);
        else this.sizex = Optional.empty();

        if (builder.spacu != null)
            this.spacu = Optional.of(builder.spacu);
        else this.spacu = Optional.empty();

        if (builder.used != null)
            this.used = Optional.of(builder.used);
        else this.used = Optional.empty();

        if (builder.vol != null)
            this.vol = Optional.of(builder.vol);
        else this.vol = Optional.empty();

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

    public Optional<String> getCdate() { return cdate; }

    public Optional<String> getDev() {
        return dev;
    }

    public Optional<String> getDsntp() {
        return dsntp;
    }

    public Optional<String> getDsorg() {
        return dsorg;
    }

    public Optional<String> getEdate() { return edate; }

    public Optional<String> getExtx() {
        return extx;
    }

    public Optional<String> getLrectl() { return lrectl; }

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

    public Optional<String> getSizex() { return sizex; }

    public Optional<String> getSpacu() { return spacu; }

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
