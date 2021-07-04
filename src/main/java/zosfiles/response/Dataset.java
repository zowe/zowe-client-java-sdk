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
    private Optional<String> spaceu;
    private Optional<String> used;
    private Optional<String> vol;
    private Optional<String> vols;

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

        if (builder.spaceu != null)
            this.spaceu = Optional.of(builder.spaceu);
        else this.spaceu = Optional.empty();

        if (builder.used != null)
            this.used = Optional.of(builder.used);
        else this.used = Optional.empty();

        if (builder.vol != null)
            this.vol = Optional.of(builder.vol);
        else this.vol = Optional.empty();

        if (builder.vols != null)
            this.vols = Optional.of(builder.vols);
        else this.vols = Optional.empty();
    }

    public Optional<String> getDsname() {
        return dsname;
    }

    public Optional<String> getblksz() {
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

    public Optional<String> getSpaceu() {
        return spaceu;
    }

    public Optional<String> getUsed() {
        return used;
    }

    public Optional<String> getVol() {
        return vol;
    }

    public Optional<String> getVols() {
        return vols;
    }
}
