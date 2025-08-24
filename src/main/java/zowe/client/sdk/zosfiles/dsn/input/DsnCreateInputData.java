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
 * Parameter container class for create dataset API
 * zOSMF REST API information:
 * <a href="https://www.ibm.com/support/knowledgecenter/SSLTBW_2.3.0/com.ibm.zos.v2r3.izua700/IZUHPINFO_API_CreateDataSet.htm#CreateDataSet">...</a>
 *
 * @author Leonid Baranov
 * @version 5.0
 */
public class DsnCreateInputData {

    /**
     * The volume serial
     */
    private final String volser;

    /**
     * The device type
     */
    private final String unit;

    /**
     * The data set organization
     */
    private final String dsorg;

    /**
     * The unit of space allocation
     */
    private final String alcunit;

    /**
     * The primary space allocation
     */
    private final Integer primary;

    /**
     * The secondary space allocation
     */
    private final Integer secondary;

    /**
     * The number of directory blocks
     */
    private final Integer dirblk;

    /**
     * The average block
     */
    private final Integer avgblk;

    /**
     * The record format
     */
    private final String recfm;

    /**
     * The block size
     */
    private final Integer blksize;

    /**
     * The record length
     */
    private final Integer lrecl;

    /**
     * The storage class
     */
    private final String storclass;

    /**
     * The management class
     */
    private final String mgntclass;

    /**
     * The data class
     */
    private final String dataclass;

    /**
     * The data set type
     */
    private final String dsntype;

    /**
     * The indicator that we need to show the attributes
     * DO NOT SEND THIS TO ZOSMF
     */
    private final boolean showAttributes;

    /**
     * The abstraction of Allocation unit and Primary Space
     * DO NOT SEND THIS TO ZOSMF
     */
    private final String size;

    /**
     * Response time out value
     */
    private final String responseTimeout;

    /**
     * DsnCreateInputData constructor
     *
     * @param builder DsnCreateInputData.Builder object
     * @author Leonid Baranov
     */
    private DsnCreateInputData(final DsnCreateInputData.Builder builder) {
        this.volser = builder.volser;
        this.unit = builder.unit;
        this.dsorg = builder.dsorg;
        this.alcunit = builder.alcunit;
        this.primary = builder.primary;
        this.secondary = builder.secondary;
        this.dirblk = builder.dirblk;
        this.avgblk = builder.avgblk;
        this.recfm = builder.recfm;
        this.blksize = builder.blksize;
        this.lrecl = builder.lrecl;
        this.storclass = builder.storclass;
        this.mgntclass = builder.mgntclass;
        this.dataclass = builder.dataclass;
        this.dsntype = builder.dsntype;
        this.showAttributes = builder.showAttributes;
        this.size = builder.size;
        this.responseTimeout = builder.responseTimeout;
    }

    /**
     * Retrieve alcunit value
     *
     * @return alcunit value
     */
    public Optional<String> getAlcunit() {
        return Optional.ofNullable(alcunit);
    }

    /**
     * Retrieve avgblk value
     *
     * @return avgblk value
     */
    public OptionalInt getAvgblk() {
        return (avgblk == null) ? OptionalInt.empty() : OptionalInt.of(avgblk);
    }

    /**
     * Retrieve blksize value
     *
     * @return blksize value
     */
    public OptionalInt getBlksize() {
        return (blksize == null) ? OptionalInt.empty() : OptionalInt.of(blksize);
    }

    /**
     * Retrieve dataclass value
     *
     * @return dataclass value
     */
    public Optional<String> getDataclass() {
        return Optional.ofNullable(dataclass);
    }

    /**
     * Retrieve dirblk value
     *
     * @return dirblk value
     */
    public OptionalInt getDirblk() {
        return (dirblk == null) ? OptionalInt.empty() : OptionalInt.of(dirblk);
    }

    /**
     * Retrieve dsntype value
     *
     * @return dsntype value
     */
    public Optional<String> getDsntype() {
        return Optional.ofNullable(dsntype);
    }

    /**
     * Retrieve dsorg value
     *
     * @return dsorg value
     */
    public Optional<String> getDsorg() {
        return Optional.ofNullable(dsorg);
    }

    /**
     * Retrieve lrecl value
     *
     * @return lrecl value
     */
    public OptionalInt getLrecl() {
        return (lrecl == null) ? OptionalInt.empty() : OptionalInt.of(lrecl);
    }

    /**
     * Retrieve mgntclass value
     *
     * @return mgntclass value
     */
    public Optional<String> getMgntclass() {
        return Optional.ofNullable(mgntclass);
    }

    /**
     * Retrieve primary value
     *
     * @return primary value
     */
    public OptionalInt getPrimary() {
        return (primary == null) ? OptionalInt.empty() : OptionalInt.of(primary);
    }

    /**
     * Retrieve recfm value
     *
     * @return recfm value
     */
    public Optional<String> getRecfm() {
        return Optional.ofNullable(recfm);
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     */
    public Optional<String> getResponseTimeout() {
        return Optional.ofNullable(responseTimeout);
    }

    /**
     * Retrieve secondary value
     *
     * @return secondary value
     */
    public OptionalInt getSecondary() {
        return (secondary == null) ? OptionalInt.empty() : OptionalInt.of(secondary);
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
        return Optional.ofNullable(size);
    }

    /**
     * Retrieve storclass value
     *
     * @return storclass value
     */
    public Optional<String> getStorclass() {
        return Optional.ofNullable(storclass);
    }

    /**
     * Retrieve unit value
     *
     * @return unit value
     */
    public Optional<String> getUnit() {
        return Optional.ofNullable(unit);
    }

    /**
     * Retrieve volser value
     *
     * @return volser value
     */
    public Optional<String> getVolser() {
        return Optional.ofNullable(volser);
    }

    /**
     * Return string value representing DsnCreateInputData object
     *
     * @return string representation of DsnCreateInputData
     */
    @Override
    public String toString() {
        return "DsnCreateInputData{" +
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
     * Builder class for DsnCreateInputData
     */
    public static class Builder {

        /**
         * The volume serial
         */
        private String volser;

        /**
         * The device type
         */
        private String unit;

        /**
         * The data set organization
         */
        private String dsorg;

        /**
         * The unit of space allocation
         */
        private String alcunit;

        /**
         * The primary space allocation
         */
        private Integer primary;

        /**
         * The secondary space allocation
         */
        private Integer secondary;

        /**
         * The number of directory blocks
         */
        private Integer dirblk;

        /**
         * The average block
         */
        private Integer avgblk;

        /**
         * The record format
         */
        private String recfm;

        /**
         * The block size
         */
        private Integer blksize;

        /**
         * The record length
         */
        private Integer lrecl;

        /**
         * The storage class
         */
        private String storclass;

        /**
         * The management class
         */
        private String mgntclass;

        /**
         * The data class
         */
        private String dataclass;

        /**
         * The data set type
         */
        private String dsntype;

        /**
         * The indicator that we need to show the attributes
         * DO NOT SEND THIS TO ZOSMF
         */
        private boolean showAttributes;

        /**
         * The abstraction of Allocation unit and Primary Space
         * DO NOT SEND THIS TO ZOSMF
         */
        private String size;

        /**
         * Response time out value
         */
        private String responseTimeout;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set alcunit string value
         *
         * @param alcunit string value
         * @return Builder this object
         */
        public Builder alcunit(final String alcunit) {
            this.alcunit = alcunit;
            return this;
        }

        /**
         * Set avgblk int value
         *
         * @param avgblk int value
         * @return Builder this object
         */
        public Builder avgblk(final Integer avgblk) {
            this.avgblk = avgblk;
            return this;
        }


        /**
         * Set blksize int value
         *
         * @param blksize int value
         * @return Builder this object
         */
        public Builder blksize(final Integer blksize) {
            this.blksize = blksize;
            return this;
        }

        /**
         * Set dataclass string value
         *
         * @param dataclass string value
         * @return Builder this object
         */
        public Builder dataclass(final String dataclass) {
            this.dataclass = dataclass;
            return this;
        }

        /**
         * Set dirblk int value
         *
         * @param dirblk int value
         * @return Builder this object
         */
        public Builder dirblk(final Integer dirblk) {
            this.dirblk = dirblk;
            return this;
        }

        /**
         * Set dsntype string value
         *
         * @param dsntype string value
         * @return Builder this object
         */
        public Builder dsntype(final String dsntype) {
            this.dsntype = dsntype;
            return this;
        }

        /**
         * Set dsorg string value
         *
         * @param dsorg string value
         * @return Builder this object
         */
        public Builder dsorg(final String dsorg) {
            this.dsorg = dsorg;
            return this;
        }

        /**
         * Set lrecl int value
         *
         * @param lrecl int value
         * @return Builder this object
         */
        public Builder lrecl(final Integer lrecl) {
            this.lrecl = lrecl;
            return this;
        }

        /**
         * Set mgntclass string value
         *
         * @param mgntclass string value
         * @return Builder this object
         */
        public Builder mgntclass(final String mgntclass) {
            this.mgntclass = mgntclass;
            return this;
        }

        /**
         * Set a primary int value
         *
         * @param primary int value
         * @return Builder this object
         */
        public Builder primary(final Integer primary) {
            this.primary = primary;
            return this;
        }

        /**
         * Set recfm string value
         *
         * @param recfm string value
         * @return Builder this object
         */
        public Builder recfm(final String recfm) {
            this.recfm = recfm;
            return this;
        }

        /**
         * Set responseTimeout string value
         *
         * @param responseTimeout string value
         * @return Builder this object
         */
        public Builder responseTimeout(final String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        /**
         * Set secondary int value
         *
         * @param secondary int value
         * @return Builder this object
         */
        public Builder secondary(final Integer secondary) {
            this.secondary = secondary;
            return this;
        }

        /**
         * Set showAttributes boolean value
         *
         * @param showAttributes boolean true or false value
         * @return Builder this object
         */
        public Builder showAttributes(final boolean showAttributes) {
            this.showAttributes = showAttributes;
            return this;
        }

        /**
         * Set size string value
         *
         * @param size string value
         * @return Builder this object
         */
        public Builder size(final String size) {
            this.size = size;
            return this;
        }

        /**
         * Set storclass string value
         *
         * @param storclass string value
         * @return Builder this object
         */
        public Builder storclass(final String storclass) {
            this.storclass = storclass;
            return this;
        }

        /**
         * Set unit string value
         *
         * @param unit string value
         * @return Builder this object
         */
        public Builder unit(final String unit) {
            this.unit = unit;
            return this;
        }

        /**
         * Set volser string value
         *
         * @param volser string value
         * @return Builder this object
         */
        public Builder volser(final String volser) {
            this.volser = volser;
            return this;
        }

        /**
         * Return DsnCreateInputData object based on Builder this object
         *
         * @return DsnCreateInputData this object
         */
        public DsnCreateInputData build() {
            return new DsnCreateInputData(this);
        }

    }

}
