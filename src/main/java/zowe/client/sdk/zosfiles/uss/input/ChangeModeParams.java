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
import zowe.client.sdk.zosfiles.uss.types.LinkType;

import java.util.Optional;

/**
 * Parameter container class for Unix System Services (USS) chmod operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class ChangeModeParams {

    /**
     * The mode value, which is specified as the POSIX symbolic form or octal value (as a JSON string).
     */
    private final Optional<String> mode;

    /**
     * The default is false. When 'true', the file mode bits of the directory and all files in the
     * file hierarchy below it are changed (chmod -R)
     */
    private final boolean recursive;

    /**
     * The default is 'follow' encountered links. This applies a mode change to the file or directory pointed
     * to by any encountered links. 'suppress' is a mode change for the file or directory pointed to by any
     * encountered symbolic links.
     */
    private final Optional<LinkType> links;

    /**
     * ChangeModeParams constructor
     *
     * @param builder ChangeModeParams.Builder builder
     * @author James Kostrewski
     */
    public ChangeModeParams(final ChangeModeParams.Builder builder) {
        this.mode = Optional.of(builder.mode);
        this.recursive = builder.recursive;
        this.links = Optional.ofNullable(builder.links);
    }

    /**
     * Retrieve mode value
     *
     * @return mode value
     */
    public Optional<String> getMode() {
        return mode;
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
     * Retrieve linkType value
     *
     * @return linkType value
     */
    public Optional<LinkType> getLinks() {
        return links;
    }

    /**
     * Return string value representing ChangeModeParams object
     *
     * @return string representation of ChangeModeParams
     */
    @Override
    public String toString() {
        return "ChangeModeParams{" +
                "mode=" + mode +
                ", recursive=" + recursive +
                ", links=" + links +
                '}';
    }

    /**
     * Builder class for ChangeModeParams
     */
    public static class Builder {

        /**
         * The mode value, which is specified as the POSIX symbolic form or octal value (as a JSON string).
         */
        private String mode;

        /**
         * The default is false. When 'true', the file mode bits of the directory and all files in the
         * file hierarchy below it are changed (chmod -R)
         */
        private boolean recursive = false;

        /**
         * The default is 'follow' encountered links. This applies a mode change to the file or directory pointed
         * to by any encountered links. 'suppress' is a mode change for the file or directory pointed to by any
         * encountered symbolic links.
         */
        private LinkType links;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set mode string value
         *
         * @param mode the mode value
         * @return Builder this object
         */
        public Builder mode(final String mode) {
            ValidateUtils.checkNullParameter(mode == null, "mode is null");
            ValidateUtils.checkIllegalParameter(mode.isBlank(), "mode not specified");
            this.mode = mode;
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
         * Set links LinkType value
         *
         * @param type LinkType type object
         * @return Builder this object
         */
        public Builder links(final LinkType type) {
            this.links = type;
            return this;
        }

        /**
         * Return ChangeModeParams object based on Builder this object
         *
         * @return ChangeModeParams object
         */
        public ChangeModeParams build() {
            return new ChangeModeParams(this);
        }

    }

}
