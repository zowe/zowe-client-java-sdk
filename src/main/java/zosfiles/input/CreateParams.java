package zosfiles.input;

import java.util.Optional;

public class CreateParams {

    private Optional<String> volser;
    private Optional<String> unit;
    private Optional<String> dsorg;
    private Optional<String> alcunit;
    private Optional<Integer> primary;
    private Optional<Integer> secondary;
    private Optional<Integer> dirblk;
    private Optional<Integer> avgblk;
    private Optional<String> recfm;
    private Optional<Integer> blksize;
    private Optional<Integer> lrecl;
    private Optional<String> storclass;
    private Optional<String> mgntclass;
    private Optional<String> dataclass;
    private Optional<String> dsntype;
    private Optional<Boolean> showAttributes;
    private Optional<String> size;
    private Optional<String> responseTimeout;

    public CreateParams(CreateParams.Builder builder) {
        if (builder.volser != null)
            this.volser = Optional.ofNullable(builder.volser);
        else this.volser = Optional.empty();

        if (builder.unit != null)
            this.unit = Optional.ofNullable(builder.unit);
        else this.unit = Optional.empty();

        if (builder.dsorg != null)
            this.dsorg = Optional.ofNullable(builder.dsorg);
        else this.dsorg = Optional.empty();

        if (builder.alcunit != null)
            this.alcunit = Optional.ofNullable(builder.alcunit);
        else this.alcunit = Optional.empty();

        if (builder.primary != null)
            this.primary = Optional.ofNullable(builder.primary);
        else this.primary = Optional.empty();

        if (builder.secondary != null)
            this.secondary = Optional.ofNullable(builder.secondary);
        else this.secondary = Optional.empty();

        if (builder.dirblk != null)
            this.dirblk = Optional.ofNullable(builder.dirblk);
        else this.dirblk = Optional.empty();

        if (builder.avgblk != null)
            this.avgblk = Optional.ofNullable(builder.avgblk);
        else this.avgblk = Optional.empty();

        if (builder.recfm != null)
            this.recfm = Optional.ofNullable(builder.recfm);
        else this.recfm = Optional.empty();

        if (builder.blksize != null)
            this.blksize = Optional.ofNullable(builder.blksize);
        else this.blksize = Optional.empty();

        if (builder.lrecl != null)
            this.lrecl = Optional.ofNullable(builder.lrecl);
        else this.lrecl = Optional.empty();

        if (builder.storclass != null)
            this.storclass = Optional.ofNullable(builder.storclass);
        else this.storclass = Optional.empty();

        if (builder.mgntclass != null)
            this.mgntclass = Optional.ofNullable(builder.mgntclass);
        else this.mgntclass = Optional.empty();

        if (builder.dataclass != null)
            this.dataclass = Optional.ofNullable(builder.dataclass);
        else this.dataclass = Optional.empty();

        if (builder.dsntype != null)
            this.dsntype = Optional.ofNullable(builder.dsntype);
        else this.dsntype = Optional.empty();

        if (builder.showAttributes != null)
            this.showAttributes = Optional.ofNullable(builder.showAttributes);
        else this.showAttributes = Optional.empty();

        if (builder.size != null)
            this.size = Optional.ofNullable(builder.size);
        else this.size = Optional.empty();

        if (builder.responseTimeout != null)
            this.responseTimeout = Optional.ofNullable(builder.responseTimeout);
        else this.responseTimeout = Optional.empty();
    }

    public Optional<String> getVolser() { return volser;}

    public Optional<String> getUnit() { return unit;}

    public Optional<String> getDsorg() { return dsorg;}

    public Optional<String> getAlcunit() { return alcunit;}

    public Optional<Integer> getPrimary() { return primary;}

    public Optional<Integer> getSecondary() { return secondary;}

    public Optional<Integer> getDirblk() { return dirblk;}

    public Optional<Integer> getAvgblk() { return avgblk;}

    public Optional<String> getRecfm() { return recfm;}

    public Optional<Integer> getBlksize() { return blksize;}

    public Optional<Integer> getLrecl() { return lrecl;}

    public Optional<String> getStorclass() { return storclass;}

    public Optional<String> getMgntclass() { return mgntclass;}

    public Optional<String> getDataclass() { return dataclass;}

    public Optional<String> getDsntype() { return dsntype;}

    public Optional<Boolean> getShowAttributes() { return showAttributes;}

    public Optional<String> getSize() { return size;}

    public Optional<String> getResponseTimeout() { return responseTimeout;}

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

}
