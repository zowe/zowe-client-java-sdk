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
 *
 * @author James Kostrewski
 * @version 2.0
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

    public CopyParams(CopyParams.Builder builder) {
        this.from = Optional.ofNullable(builder.from);
        this.overwrite = builder.overwrite;
        this.recursive = builder.recursive;
    }

    public Optional<String> getFrom() {
        return from;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    public boolean isRecursive() {
        return recursive;
    }

    @Override
    public String toString() {
        return "CopyParams{" +
                "from=" + this.getFrom() +
                ", overwrite=" + this.isOverwrite() +
                ", recursive=" + this.isRecursive() +
                "}";
    }

    public static class Builder {

        private String from;
        private boolean overwrite = true;
        private boolean recursive = false;

        public CopyParams build() {
            return new CopyParams(this);
        }

        public CopyParams.Builder from(String from) {
            ValidateUtils.checkNullParameter(from == null, "from is null");
            ValidateUtils.checkIllegalParameter(from.isEmpty(), "from not specified");
            this.from = from;
            return this;
        }

        public CopyParams.Builder overwrite(boolean overwrite) {
            this.overwrite = overwrite;
            return this;
        }

        public CopyParams.Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

    }

}
