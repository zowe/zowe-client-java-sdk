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
 * This interface defines the options that can be sent into the copy data set function.
 *
 * @author Leonid Baranov
 * @version 2.0
 */
public class CopyParams {

    /**
     * The volume to copy from
     */
    private final Optional<String> fromVolser;

    /**
     * The dataset to copy from
     */
    private final Optional<String> fromDataSet;

    /**
     * The volume to copy too
     */
    private final Optional<String> toVolser;

    /**
     * The dataset to copy too
     */
    private final Optional<String> toDataSet;

    /**
     * Replace option
     */
    private final boolean replace;

    /**
     * Specified as true to indicate a copying of all members in partial dataset to another partial dataset request
     */
    private final boolean copyAllMembers;

    private CopyParams(final CopyParams.Builder builder) {
        this.fromVolser = Optional.ofNullable(builder.fromVolser);
        this.fromDataSet = Optional.ofNullable(builder.fromDataSet);
        this.toVolser = Optional.ofNullable(builder.toVolser);
        this.toDataSet = Optional.ofNullable(builder.toDataSet);
        this.replace = builder.replace;
        this.copyAllMembers = builder.copyAllMembers;
    }

    /**
     * Retrieve fromDataSet value
     *
     * @return fromDataSet value
     */
    public Optional<String> getFromDataSet() {
        return fromDataSet;
    }

    /**
     * Retrieve fromVolser value
     *
     * @return fromVolser value
     */
    public Optional<String> getFromVolser() {
        return fromVolser;
    }

    /**
     * Retrieve toDataSet value
     *
     * @return toDataSet value
     */
    public Optional<String> getToDataSet() {
        return toDataSet;
    }

    /**
     * Retrieve toVolser value
     *
     * @return toVolser value
     */
    public Optional<String> getToVolser() {
        return toVolser;
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
     * Retrieve is replace specified
     *
     * @return boolean true or false
     */
    public boolean isReplace() {
        return replace;
    }

    @Override
    public String toString() {
        return "CopyParams{" +
                "fromVolser=" + fromVolser +
                ", fromDataSet=" + fromDataSet +
                ", toVolser=" + toVolser +
                ", toDataSet=" + toDataSet +
                ", replace=" + replace +
                '}';
    }

    public static class Builder {

        private String fromVolser;
        private String fromDataSet;
        private String toVolser;
        private String toDataSet;
        private boolean replace = true;
        private boolean copyAllMembers = false;

        public CopyParams build() {
            return new CopyParams(this);
        }

        public CopyParams.Builder copyAllMembers(final boolean value) {
            this.copyAllMembers = value;
            return this;
        }

        public CopyParams.Builder fromDataSet(final String dataSet) {
            ValidateUtils.checkNullParameter(dataSet == null, "fromDataSet is null");
            ValidateUtils.checkNullParameter(dataSet.isEmpty(), "fromDataSet not specified");
            this.fromDataSet = dataSet;
            return this;
        }

        public CopyParams.Builder fromVolser(final String volser) {
            this.fromVolser = volser;
            return this;
        }

        public CopyParams.Builder replace(final boolean replace) {
            this.replace = replace;
            return this;
        }

        public CopyParams.Builder toDataSet(final String dataSet) {
            ValidateUtils.checkNullParameter(dataSet == null, "toDataSet is null");
            ValidateUtils.checkNullParameter(dataSet.isEmpty(), "toDataSet not specified");
            this.toDataSet = dataSet;
            return this;
        }

        public CopyParams.Builder toVolser(final String volser) {
            this.toVolser = volser;
            return this;
        }
    }

}
