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

/**
 * Interface for create dataset API
 * zOSMF REST API information:
 * https://www.ibm.com/support/knowledgecenter/SSLTBW_2.3.0/com.ibm.zos.v2r3.izua700/IZUHPINFO_API_CreateDataSet.htm#CreateDataSet
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class CreateParams {

    /**
     * The volume serial
     */
    private final Optional<String> volser;

    /**
     * The device type
     */
    private final Optional<String> unit;

    /**
     * The data set organization
     */
    private final Optional<String> dsorg;

    /**
     * The unit of space allocation
     */
    private final Optional<String> alcunit;

    /**
     * The primary space allocation
     */
    private final Optional<Integer> primary;

    /**
     * The secondary space allocation
     */
    private final Optional<Integer> secondary;

    /**
     * The number of directory blocks
     */
    private final Optional<Integer> dirblk;

    /**
     * The average block
     */
    private final Optional<Integer> avgblk;

    /**
     * The record format
     */
    private final Optional<String> recfm;

    /**
     * The block size
     */
    private final Optional<Integer> blksize;

    /**
     * The record length
     */
    private final Optional<Integer> lrecl;

    /**
     * The storage class
     */
    private final Optional<String> storclass;

    /**
     * The management class
     */
    private final Optional<String> mgntclass;

    /**
     * The data class
     */
    private final Optional<String> dataclass;

    /**
     * The data set type
     */
    private final Optional<String> dsntype;

    /**
     * The indicator that we need to show the attributes
     * DO NOT SEND THIS TO ZOSMF
     */
    private final Optional<Boolean> showAttributes;

    /**
     * The abstraction of Allocation unit and Primary Space
     * DO NOT SEND THIS TO ZOSMF
     */
    private final Optional<String> size;

    /**
     * Response time out value
     */
    private final Optional<String> responseTimeout;

    private CreateParams(CreateParams.Builder builder) {
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

    /**
     * Retrieve volser value
     *
     * @return volser value
     * @author Leonid Baranov
     */
    public Optional<String> getVolser() {
        return volser;
    }

    /**
     * Retrieve unit value
     *
     * @return unit value
     * @author Leonid Baranov
     */
    public Optional<String> getUnit() {
        return unit;
    }

    /**
     * Retrieve dsorg value
     *
     * @return dsorg value
     * @author Leonid Baranov
     */
    public Optional<String> getDsorg() {
        return dsorg;
    }

    /**
     * Retrieve alcunit value
     *
     * @return alcunit value
     * @author Leonid Baranov
     */
    public Optional<String> getAlcunit() {
        return alcunit;
    }

    /**
     * Retrieve primary value
     *
     * @return primary value
     * @author Leonid Baranov
     */
    public Optional<Integer> getPrimary() {
        return primary;
    }

    /**
     * Retrieve secondary value
     *
     * @return secondary value
     * @author Leonid Baranov
     */
    public Optional<Integer> getSecondary() {
        return secondary;
    }

    /**
     * Retrieve dirblk value
     *
     * @return dirblk value
     * @author Leonid Baranov
     */
    public Optional<Integer> getDirblk() {
        return dirblk;
    }

    /**
     * Retrieve avgblk value
     *
     * @return avgblk value
     * @author Leonid Baranov
     */
    public Optional<Integer> getAvgblk() {
        return avgblk;
    }

    /**
     * Retrieve recfm value
     *
     * @return recfm value
     * @author Leonid Baranov
     */
    public Optional<String> getRecfm() {
        return recfm;
    }

    /**
     * Retrieve blksize value
     *
     * @return blksize value
     * @author Leonid Baranov
     */
    public Optional<Integer> getBlksize() {
        return blksize;
    }

    /**
     * Retrieve lrecl value
     *
     * @return lrecl value
     * @author Leonid Baranov
     */
    public Optional<Integer> getLrecl() {
        return lrecl;
    }

    /**
     * Retrieve storclass value
     *
     * @return storclass value
     * @author Leonid Baranov
     */
    public Optional<String> getStorclass() {
        return storclass;
    }

    /**
     * Retrieve mgntclass value
     *
     * @return mgntclass value
     * @author Leonid Baranov
     */
    public Optional<String> getMgntclass() {
        return mgntclass;
    }

    /**
     * Retrieve dataclass value
     *
     * @return dataclass value
     * @author Leonid Baranov
     */
    public Optional<String> getDataclass() {
        return dataclass;
    }

    /**
     * Retrieve dsntype value
     *
     * @return dsntype value
     * @author Leonid Baranov
     */
    public Optional<String> getDsntype() {
        return dsntype;
    }

    /**
     * Retrieve showAttributes value
     *
     * @return showAttributes value
     * @author Leonid Baranov
     */
    public Optional<Boolean> getShowAttributes() {
        return showAttributes;
    }

    /**
     * Retrieve size value
     *
     * @return size value
     * @author Leonid Baranov
     */
    public Optional<String> getSize() {
        return size;
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     * @author Leonid Baranov
     */
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
