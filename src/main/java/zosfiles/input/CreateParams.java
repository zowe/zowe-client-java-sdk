/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles.input;

import java.util.Optional;

public class CreateParams {

    private final Optional<String> volser;
    private final Optional<String> unit;
    private final Optional<String> dsorg;
    private final Optional<String> alcunit;
    private final Optional<Integer> primary;
    private final Optional<Integer> secondary;
    private final Optional<Integer> dirblk;
    private final Optional<Integer> avgblk;
    private final Optional<String> recfm;
    private final Optional<Integer> blksize;
    private final Optional<Integer> lrecl;
    private final Optional<String> storclass;
    private final Optional<String> mgntclass;
    private final Optional<String> dataclass;
    private final Optional<String> dsntype;
    private final Optional<Boolean> showAttributes;
    private final Optional<String> size;
    private final Optional<String> responseTimeout;

    public CreateParams(CreateParams.Builder builder) {
        this.volser = Optional.ofNullable(builder.volser);
        this.unit = Optional.ofNullable(builder.unit);
        this.dsorg = Optional.ofNullable(builder.dsorg);
        this.alcunit = Optional.ofNullable(builder.alcunit);
        this.primary = Optional.ofNullable(builder.primary);
        this.secondary = Optional.ofNullable(builder.secondary);
        this.dirblk = Optional.ofNullable(builder.dirblk);
        this.avgblk = Optional.ofNullable(builder.avgblk);
        this.recfm = Optional.ofNullable(builder.recfm);
        this.blksize = Optional.ofNullable(builder.blksize);
        this.lrecl = Optional.ofNullable(builder.lrecl);
        this.storclass = Optional.ofNullable(builder.storclass);
        this.mgntclass = Optional.ofNullable(builder.mgntclass);
        this.dataclass = Optional.ofNullable(builder.dataclass);
        this.dsntype = Optional.ofNullable(builder.dsntype);
        this.showAttributes = Optional.ofNullable(builder.showAttributes);
        this.size = Optional.ofNullable(builder.size);
        this.responseTimeout = Optional.ofNullable(builder.responseTimeout);
    }

    public Optional<String> getVolser() {
        return volser;
    }

    public Optional<String> getUnit() {
        return unit;
    }

    public Optional<String> getDsorg() {
        return dsorg;
    }

    public Optional<String> getAlcunit() {
        return alcunit;
    }

    public Optional<Integer> getPrimary() {
        return primary;
    }

    public Optional<Integer> getSecondary() {
        return secondary;
    }

    public Optional<Integer> getDirblk() {
        return dirblk;
    }

    public Optional<Integer> getAvgblk() {
        return avgblk;
    }

    public Optional<String> getRecfm() {
        return recfm;
    }

    public Optional<Integer> getBlksize() {
        return blksize;
    }

    public Optional<Integer> getLrecl() {
        return lrecl;
    }

    public Optional<String> getStorclass() {
        return storclass;
    }

    public Optional<String> getMgntclass() {
        return mgntclass;
    }

    public Optional<String> getDataclass() {
        return dataclass;
    }

    public Optional<String> getDsntype() {
        return dsntype;
    }

    public Optional<Boolean> getShowAttributes() {
        return showAttributes;
    }

    public Optional<String> getSize() {
        return size;
    }

    public Optional<String> getResponseTimeout() {
        return responseTimeout;
    }

    @Override
    public String toString() {
        return "CreateOptions{" +
                ", volser=" + volser +
                ", unit=" + unit +
                ", dsorg=" + dsorg +
                ", alcunit=" + alcunit +
                ", primary=" + primary +
                ", secondary=" + secondary +
                ", dirblk=" + dirblk +
                ", avgblk=" + avgblk +
                ", recfm=" + recfm +
                ", blksize=" + blksize +
                ", lrecl=" + lrecl +
                ", storclass=" + storclass +
                ", mgntclass=" + mgntclass +
                ", dataclass=" + dataclass +
                ", dsntype=" + dsntype +
                ", showAttributes=" + showAttributes +
                ", size=" + size +
                ", responseTimeout=" + responseTimeout +
                '}';
    }

    public static class Builder {

        private String volser;
        private String unit;
        private String dsorg;
        private String alcunit;
        private Integer primary;
        private Integer secondary;
        private Integer dirblk;
        private Integer avgblk;
        private String recfm;
        private Integer blksize;
        private Integer lrecl;
        private String storclass;
        private String mgntclass;
        private String dataclass;
        private String dsntype;
        private Boolean showAttributes;
        private String size;
        private String responseTimeout;


        public zosfiles.input.CreateParams.Builder volser(String volser) {
            this.volser = volser;
            return this;
        }

        public zosfiles.input.CreateParams.Builder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public zosfiles.input.CreateParams.Builder dsorg(String dsorg) {
            this.dsorg = dsorg;
            return this;
        }

        public zosfiles.input.CreateParams.Builder alcunit(String alcunit) {
            this.alcunit = alcunit;
            return this;
        }

        public zosfiles.input.CreateParams.Builder primary(Integer primary) {
            this.primary = primary;
            return this;
        }

        public zosfiles.input.CreateParams.Builder secondary(Integer secondary) {
            this.secondary = secondary;
            return this;
        }

        public zosfiles.input.CreateParams.Builder dirblk(Integer dirblk) {
            this.dirblk = dirblk;
            return this;
        }

        public zosfiles.input.CreateParams.Builder avgblk(Integer avgblk) {
            this.avgblk = avgblk;
            return this;
        }

        public zosfiles.input.CreateParams.Builder recfm(String recfm) {
            this.recfm = recfm;
            return this;
        }

        public zosfiles.input.CreateParams.Builder blksize(Integer blksize) {
            this.blksize = blksize;
            return this;
        }

        public zosfiles.input.CreateParams.Builder lrecl(Integer lrecl) {
            this.lrecl = lrecl;
            return this;
        }

        public zosfiles.input.CreateParams.Builder storclass(String storclass) {
            this.storclass = storclass;
            return this;
        }

        public zosfiles.input.CreateParams.Builder mgntclass(String mgntclass) {
            this.mgntclass = mgntclass;
            return this;
        }

        public zosfiles.input.CreateParams.Builder dataclass(String dataclass) {
            this.dataclass = dataclass;
            return this;
        }

        public zosfiles.input.CreateParams.Builder dsntype(String dsntype) {
            this.dsntype = dsntype;
            return this;
        }

        public zosfiles.input.CreateParams.Builder showAttributes(Boolean showAttributes) {
            this.showAttributes = showAttributes;
            return this;
        }

        public zosfiles.input.CreateParams.Builder size(String size) {
            this.size = size;
            return this;
        }

        public zosfiles.input.CreateParams.Builder responseTimeout(String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        public zosfiles.input.CreateParams build() {
            return new zosfiles.input.CreateParams(this);
        }

    }

    public static CreateParams binary() {
        return new Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(10)
                .secondary(10)
                .dirblk(25)
                .recfm("U")
                .blksize(27998)
                .lrecl(27998)
                .build();
    }

    public static CreateParams c() {
        return new Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(25)
                .recfm("VB")
                .blksize(32760)
                .lrecl(260)
                .build();
    }

    public static CreateParams classic() {
        return new Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(25)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    public static CreateParams partitioned() {
        return new Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(5)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    public static CreateParams sequential() {
        return new Builder()
                .dsorg("PS")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

}
