/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.input;

import java.util.Optional;

/**
 * This interface defines the options that can be sent into the copy data set function.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class CopyParams {

    /**
     * The volume to copy from.
     */
    private final Optional<String> fromVolser;

    /**
     * The dataset to copy from.
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

    private CopyParams(CopyParams.Builder builder) {
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
     * @author Leonid Baranov
     */
    public Optional<String> getFromDataSet() {
        return fromDataSet;
    }

    /**
     * Retrieve fromVolser value
     *
     * @return fromVolser value
     * @author Leonid Baranov
     */
    public Optional<String> getFromVolser() {
        return fromVolser;
    }

    /**
     * Retrieve toDataSet value
     *
     * @return toDataSet value
     * @author Leonid Baranov
     */
    public Optional<String> getToDataSet() {
        return toDataSet;
    }

    /**
     * Retrieve toVolser value
     *
     * @return toVolser value
     * @author Leonid Baranov
     */
    public Optional<String> getToVolser() {
        return toVolser;
    }

    /**
     * Retrieve copyAllMembers value
     *
     * @return copyAllMembers value
     * @author Frank Giordano
     */
    public boolean isCopyAllMembers() {
        return copyAllMembers;
    }

    /**
     * Retrieve replace value
     *
     * @return replace value
     * @author Leonid Baranov
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

        public CopyParams.Builder copyAllMembers(boolean value) {
            this.copyAllMembers = value;
            return this;
        }

        public CopyParams.Builder fromDataSet(String dataSet) {
            this.fromDataSet = dataSet;
            return this;
        }

        public CopyParams.Builder fromVolser(String volser) {
            this.fromVolser = volser;
            return this;
        }

        public CopyParams.Builder replace(boolean replace) {
            this.replace = replace;
            return this;
        }

        public CopyParams.Builder toDataSet(String dataSet) {
            this.toDataSet = dataSet;
            return this;
        }

        public CopyParams.Builder toVolser(String volser) {
            this.toVolser = volser;
            return this;
        }
    }

}
