/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.input;

import zowe.client.sdk.utility.ValidateUtils;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) copy operation
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/2.4.0?topic=interface-zos-unix-file-utilities">z/OSMF REST API</a>
 *
 * @author James Kostrewski
 * @version 4.0
 */
public class CopyParams {

    /**
     * The file or directory to be copied. May not be specified with 'from-dataset'.
     */
    private final Optional<String> from;

    /**
     * The default is true.
     */
    private final boolean overwrite;

    /**
     * The default is false.
     * When 'true', copies all the files and subdirectories that are specified by source into a directory (cp -R).
     */
    private final boolean recursive;

    /**
     * CopyParams constructor
     *
     * @param builder CopyParams.Builder builder
     * @author James Kostrewski
     */
    public CopyParams(final CopyParams.Builder builder) {
        this.from = Optional.ofNullable(builder.from);
        this.overwrite = builder.overwrite;
        this.recursive = builder.recursive;
    }

    /**
     * Retrieve from value
     *
     * @return from value
     */
    public Optional<String> getFrom() {
        return from;
    }

    /**
     * Retrieve is overwrite specified
     *
     * @return boolean true or false
     */
    public boolean isOverwrite() {
        return overwrite;
    }

    /**
     * Is recursive specified
     *
     * @return boolean true or false
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * Return string value representing CopyParams object
     *
     * @return string representation of CopyParams
     */
    @Override
    public String toString() {
        return "CopyParams{" +
                "from=" + this.getFrom() +
                ", overwrite=" + this.isOverwrite() +
                ", recursive=" + this.isRecursive() +
                "}";
    }

    /**
     * Builder class for CopyParams
     */
    public static class Builder {

        /**
         * The file or directory to be copied. May not be specified with 'from-dataset'.
         */
        private String from;

        /**
         * The default is true.
         */
        private boolean overwrite = true;

        /**
         * The default is false.
         * When 'true', copies all the files and subdirectories that are specified by source into a directory (cp -R).
         */
        private boolean recursive = false;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set from string value
         *
         * @param from the 'from' string value
         * @return Builder this object
         */
        public Builder from(final String from) {
            ValidateUtils.checkNullParameter(from == null, "from is null");
            ValidateUtils.checkIllegalParameter(from.isBlank(), "from not specified");
            this.from = from;
            return this;
        }

        /**
         * Set overwrite boolean value
         *
         * @param overwrite boolean true or false value
         * @return Builder this object
         */
        public Builder overwrite(final boolean overwrite) {
            this.overwrite = overwrite;
            return this;
        }

        /**
         * Set recursive boolean value
         *
         * @param recursive boolean true or false value
         * @return Builder this object
         */
        public Builder recursive(final boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        /**
         * Return CopyParams object based on Builder this object
         *
         * @return CopyParams object
         */
        public CopyParams build() {
            return new CopyParams(this);
        }

    }

}
