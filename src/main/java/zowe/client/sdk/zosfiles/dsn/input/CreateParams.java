/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.input;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Interface for create dataset API
 * zOSMF REST API information:
 * <a href="https://www.ibm.com/support/knowledgecenter/SSLTBW_2.3.0/com.ibm.zos.v2r3.izua700/IZUHPINFO_API_CreateDataSet.htm#CreateDataSet">...</a>
 *
 * @author Leonid Baranov
 * @version 2.0
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
    private final OptionalInt primary;

    /**
     * The secondary space allocation
     */
    private final OptionalInt secondary;

    /**
     * The number of directory blocks
     */
    private final OptionalInt dirblk;

    /**
     * The average block
     */
    private final OptionalInt avgblk;

    /**
     * The record format
     */
    private final Optional<String> recfm;

    /**
     * The block size
     */
    private final OptionalInt blksize;

    /**
     * The record length
     */
    private final OptionalInt lrecl;

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
    private final boolean showAttributes;

    /**
     * The abstraction of Allocation unit and Primary Space
     * DO NOT SEND THIS TO ZOSMF
     */
    private final Optional<String> size;

    /**
     * Response time out value
     */
    private final Optional<String> responseTimeout;

    /**
     * CreateParams constructor
     *
     * @param builder CreateParams.Builder object
     * @author Leonid Baranov
     */
    private CreateParams(final CreateParams.Builder builder) {
        this.volser = Optional.ofNullable(builder.volser);
        this.unit = Optional.ofNullable(builder.unit);
        this.dsorg = Optional.ofNullable(builder.dsorg);
        this.alcunit = Optional.ofNullable(builder.alcunit);
        if (builder.primary == null) {
            this.primary = OptionalInt.empty();
        } else {
            this.primary = OptionalInt.of(builder.primary);
        }
        if (builder.secondary == null) {
            this.secondary = OptionalInt.empty();
        } else {
            this.secondary = OptionalInt.of(builder.secondary);
        }
        if (builder.dirblk == null) {
            this.dirblk = OptionalInt.empty();
        } else {
            this.dirblk = OptionalInt.of(builder.dirblk);
        }
        if (builder.avgblk == null) {
            this.avgblk = OptionalInt.empty();
        } else {
            this.avgblk = OptionalInt.of(builder.avgblk);
        }
        this.recfm = Optional.ofNullable(builder.recfm);
        if (builder.blksize == null) {
            this.blksize = OptionalInt.empty();
        } else {
            this.blksize = OptionalInt.of(builder.blksize);
        }
        if (builder.lrecl == null) {
            this.lrecl = OptionalInt.empty();
        } else {
            this.lrecl = OptionalInt.of(builder.lrecl);
        }
        this.storclass = Optional.ofNullable(builder.storclass);
        this.mgntclass = Optional.ofNullable(builder.mgntclass);
        this.dataclass = Optional.ofNullable(builder.dataclass);
        this.dsntype = Optional.ofNullable(builder.dsntype);
        this.showAttributes = builder.showAttributes;
        this.size = Optional.ofNullable(builder.size);
        this.responseTimeout = Optional.ofNullable(builder.responseTimeout);
    }

    /**
     * Retrieve alcunit value
     *
     * @return alcunit value
     */
    public Optional<String> getAlcunit() {
        return alcunit;
    }

    /**
     * Retrieve avgblk value
     *
     * @return avgblk value
     */
    public OptionalInt getAvgblk() {
        return avgblk;
    }

    /**
     * Retrieve blksize value
     *
     * @return blksize value
     */
    public OptionalInt getBlksize() {
        return blksize;
    }

    /**
     * Retrieve dataclass value
     *
     * @return dataclass value
     */
    public Optional<String> getDataclass() {
        return dataclass;
    }

    /**
     * Retrieve dirblk value
     *
     * @return dirblk value
     */
    public OptionalInt getDirblk() {
        return dirblk;
    }

    /**
     * Retrieve dsntype value
     *
     * @return dsntype value
     */
    public Optional<String> getDsntype() {
        return dsntype;
    }

    /**
     * Retrieve dsorg value
     *
     * @return dsorg value
     */
    public Optional<String> getDsorg() {
        return dsorg;
    }

    /**
     * Retrieve lrecl value
     *
     * @return lrecl value
     */
    public OptionalInt getLrecl() {
        return lrecl;
    }

    /**
     * Retrieve mgntclass value
     *
     * @return mgntclass value
     */
    public Optional<String> getMgntclass() {
        return mgntclass;
    }

    /**
     * Retrieve primary value
     *
     * @return primary value
     */
    public OptionalInt getPrimary() {
        return primary;
    }

    /**
     * Retrieve recfm value
     *
     * @return recfm value
     */
    public Optional<String> getRecfm() {
        return recfm;
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     */
    public Optional<String> getResponseTimeout() {
        return responseTimeout;
    }

    /**
     * Retrieve secondary value
     *
     * @return secondary value
     */
    public OptionalInt getSecondary() {
        return secondary;
    }

    /**
     * Retrieve is showAttributes specified
     *
     * @return boolean true or false
     */
    public boolean isShowAttributes() {
        return showAttributes;
    }

    /**
     * Retrieve size value
     *
     * @return size value
     */
    public Optional<String> getSize() {
        return size;
    }

    /**
     * Retrieve storclass value
     *
     * @return storclass value
     */
    public Optional<String> getStorclass() {
        return storclass;
    }

    /**
     * Retrieve unit value
     *
     * @return unit value
     */
    public Optional<String> getUnit() {
        return unit;
    }

    /**
     * Retrieve volser value
     *
     * @return volser value
     */
    public Optional<String> getVolser() {
        return volser;
    }

    /**
     * Return string value representing CreateOptions object
     *
     * @return string representation of CreateOptions
     */
    @Override
    public String toString() {
        return "CreateOptions{" +
                "volser=" + volser +
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

    /**
     * Builder class for CreateParams
     */
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
        private boolean showAttributes;
        private String size;
        private String responseTimeout;

        public CreateParams.Builder alcunit(final String alcunit) {
            this.alcunit = alcunit;
            return this;
        }

        public CreateParams.Builder avgblk(final Integer avgblk) {
            this.avgblk = avgblk;
            return this;
        }

        public CreateParams.Builder blksize(final Integer blksize) {
            this.blksize = blksize;
            return this;
        }

        public CreateParams.Builder dataclass(final String dataclass) {
            this.dataclass = dataclass;
            return this;
        }

        public CreateParams.Builder dirblk(final Integer dirblk) {
            this.dirblk = dirblk;
            return this;
        }

        public CreateParams.Builder dsntype(final String dsntype) {
            this.dsntype = dsntype;
            return this;
        }

        public CreateParams.Builder dsorg(final String dsorg) {
            this.dsorg = dsorg;
            return this;
        }

        public CreateParams.Builder lrecl(final Integer lrecl) {
            this.lrecl = lrecl;
            return this;
        }

        public CreateParams.Builder mgntclass(final String mgntclass) {
            this.mgntclass = mgntclass;
            return this;
        }

        public CreateParams.Builder primary(final Integer primary) {
            this.primary = primary;
            return this;
        }

        public CreateParams.Builder recfm(final String recfm) {
            this.recfm = recfm;
            return this;
        }

        public CreateParams.Builder responseTimeout(final String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        public CreateParams.Builder secondary(final Integer secondary) {
            this.secondary = secondary;
            return this;
        }

        public CreateParams.Builder showAttributes(final boolean showAttributes) {
            this.showAttributes = showAttributes;
            return this;
        }

        public CreateParams.Builder size(final String size) {
            this.size = size;
            return this;
        }

        public CreateParams.Builder storclass(final String storclass) {
            this.storclass = storclass;
            return this;
        }

        public CreateParams.Builder unit(final String unit) {
            this.unit = unit;
            return this;
        }

        public CreateParams.Builder volser(final String volser) {
            this.volser = volser;
            return this;
        }

        public CreateParams build() {
            return new CreateParams(this);
        }

    }

}
