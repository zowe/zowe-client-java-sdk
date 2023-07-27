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
 * Parameter container class for Unix System Services (USS) chmod operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class ChgModeParams {

    /**
     * The mode value, which is specified as the POSIX symbolic form or octal value (as a JSON string).
     */
    private final Optional<String> mode;

    /**
     * The default is false. When 'true', the file mode bits of the directory and all files in the
     * file hierarchy below it are changed (chmod -R)
     */
    private final boolean recursive;

    public ChgModeParams(ChgModeParams.Builder builder) {
        this.mode = Optional.of(builder.mode);
        this.recursive = builder.recursive;
    }

    public Optional<String> getMode() {
        return mode;
    }

    public boolean isRecursive() {
        return recursive;
    }

    @Override
    public String toString() {
        return "ChangeModeParams{" +
                "mode=" + mode +
                ", recursive=" + recursive +
                '}';
    }

    public static class Builder {

        private String mode;
        private boolean recursive = false;

        public ChgModeParams build() {
            return new ChgModeParams(this);
        }

        public ChgModeParams.Builder mode(String mode) {
            ValidateUtils.checkNullParameter(mode == null, "mode is null");
            ValidateUtils.checkIllegalParameter(mode.isEmpty(), "mode not specified");
            this.mode = mode;
            return this;
        }

        public ChgModeParams.Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

    }

}
