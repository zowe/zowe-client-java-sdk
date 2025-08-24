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

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Optional;

/**
 * Parameter container class for the copy data set function.
 *
 * @author Leonid Baranov
 * @version 5.0
 */
public class DsnCopyInputData {

    /**
     * The volume to copy from
     */
    private final String fromVolser;

    /**
     * The dataset to copy from
     */
    private final String fromDataSet;

    /**
     * The volume to copy too
     */
    private final String toVolser;

    /**
     * The dataset to copy too
     */
    private final String toDataSet;

    /**
     * Replace option
     */
    private final boolean replace;

    /**
     * Specify as true to indicate a copying of all members in a partial dataset to another partial dataset request
     */
    private final boolean copyAllMembers;

    /**
     * DsnCopyInputData constructor
     *
     * @param builder DsnCopyInputData.Builder object
     * @author Leonid Baranov
     */
    private DsnCopyInputData(final DsnCopyInputData.Builder builder) {
        this.fromVolser = builder.fromVolser;
        this.fromDataSet = builder.fromDataSet;
        this.toVolser = builder.toVolser;
        this.toDataSet = builder.toDataSet;
        this.replace = builder.replace;
        this.copyAllMembers = builder.copyAllMembers;
    }

    /**
     * Retrieve fromDataSet value
     *
     * @return fromDataSet value
     */
    public Optional<String> getFromDataSet() {
        return Optional.ofNullable(fromDataSet);
    }

    /**
     * Retrieve fromVolser value
     *
     * @return fromVolser value
     */
    public Optional<String> getFromVolser() {
        return Optional.ofNullable(fromVolser);
    }

    /**
     * Retrieve toDataSet value
     *
     * @return toDataSet value
     */
    public Optional<String> getToDataSet() {
        return Optional.ofNullable(toDataSet);
    }

    /**
     * Retrieve toVolser value
     *
     * @return toVolser value
     */
    public Optional<String> getToVolser() {
        return Optional.ofNullable(toVolser);
    }

    /**
     * Retrieve is copyAllMembers specified
     *
     * @return boolean true or false
     */
    public boolean isCopyAllMembers() {
        return copyAllMembers;
    }

    /**
     * Retrieve replace boolean value
     *
     * @return boolean true or false
     */
    public boolean isReplace() {
        return replace;
    }

    /**
     * Return string value representing DsnCopyInputData object
     *
     * @return string representation of DsnCopyInputData
     */
    @Override
    public String toString() {
        return "DsnCopyInputData{" +
                "fromVolser=" + fromVolser +
                ", fromDataSet=" + fromDataSet +
                ", toVolser=" + toVolser +
                ", toDataSet=" + toDataSet +
                ", replace=" + replace +
                '}';
    }

    /**
     * Builder class for DsnCopyInputData
     */
    public static class Builder {

        /**
         * The volume to copy from
         */
        private String fromVolser;

        /**
         * The dataset to copy from
         */
        private String fromDataSet;

        /**
         * The volume to copy too
         */
        private String toVolser;

        /**
         * The dataset to copy too
         */
        private String toDataSet;

        /**
         * Replace option
         */
        private boolean replace = true;

        /**
         * Specify as true to indicate a copying of all members in a partial dataset to another partial dataset request
         */
        private boolean copyAllMembers = false;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set copyAllMembers boolean value
         *
         * @param value boolean true or false value
         * @return Builder this object
         */
        public Builder copyAllMembers(final boolean value) {
            this.copyAllMembers = value;
            return this;
        }

        /**
         * Set fromDataSet string value
         *
         * @param dataSet string value
         * @return Builder this object
         */
        public Builder fromDataSet(final String dataSet) {
            ValidateUtils.checkNullParameter(dataSet == null, "fromDataSet is null");
            ValidateUtils.checkNullParameter(dataSet.isEmpty(), "fromDataSet not specified");
            this.fromDataSet = dataSet;
            return this;
        }

        /**
         * Set fromVolser string value
         *
         * @param volser string value
         * @return Builder this object
         */
        public Builder fromVolser(final String volser) {
            this.fromVolser = volser;
            return this;
        }

        /**
         * Set replace string value
         *
         * @param replace string value
         * @return Builder this object
         */
        public Builder replace(final boolean replace) {
            this.replace = replace;
            return this;
        }

        /**
         * Set dataSet string value
         *
         * @param dataSet string value
         * @return Builder this object
         */
        public Builder toDataSet(final String dataSet) {
            ValidateUtils.checkNullParameter(dataSet == null, "toDataSet is null");
            ValidateUtils.checkNullParameter(dataSet.isEmpty(), "toDataSet not specified");
            this.toDataSet = dataSet;
            return this;
        }

        /**
         * Set volser string value
         *
         * @param volser string value
         * @return Builder this object
         */
        public Builder toVolser(final String volser) {
            this.toVolser = volser;
            return this;
        }

        /**
         * Return DsnCopyInputData object based on Builder this object
         *
         * @return DsnCopyInputData this object
         */
        public DsnCopyInputData build() {
            return new DsnCopyInputData(this);
        }

    }

}
