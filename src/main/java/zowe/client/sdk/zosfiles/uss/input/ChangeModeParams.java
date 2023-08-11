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
    private final Optional<LinkType> linkType;

    /**
     * ChangeModeParams constructor
     *
     * @param builder ChangeModeParams.Builder builder
     * @author James Kostrewski
     */
    public ChangeModeParams(ChangeModeParams.Builder builder) {
        this.mode = Optional.of(builder.mode);
        this.recursive = builder.recursive;
        this.linkType = Optional.ofNullable(builder.linkType);
    }

    /**
     * Retrieve mode value
     *
     * @return mode value
     * @author James Kostrewski
     */
    public Optional<String> getMode() {
        return mode;
    }

    /**
     * Is recursive value true or false
     *
     * @return recursive value
     * @author James Kostrewski
     */
    public boolean isRecursive() {
        return recursive;
    }

    /**
     * Retrieve linkType value
     *
     * @return linkType value
     * @author James Kostrewski
     */
    public Optional<LinkType> getLinkType() {
        return linkType;
    }

    @Override
    public String toString() {
        return "ChangeModeParams{" +
                "mode=" + mode +
                ", recursive=" + recursive +
                ", linkType=" + linkType +
                '}';
    }

    public static class Builder {

        private String mode;
        private boolean recursive = false;
        private LinkType linkType;

        public ChangeModeParams build() {
            return new ChangeModeParams(this);
        }

        public ChangeModeParams.Builder mode(String mode) {
            ValidateUtils.checkNullParameter(mode == null, "mode is null");
            ValidateUtils.checkIllegalParameter(mode.isBlank(), "mode not specified");
            this.mode = mode;
            return this;
        }

        public ChangeModeParams.Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        public ChangeModeParams.Builder linktype(LinkType type) {
            this.linkType = type;
            return this;
        }

    }

}
