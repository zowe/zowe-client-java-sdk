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

/**
 * Parameter container class for Unix System Services (USS) chmod operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class ChModParams {

    /**
     * The mode value
     */
    private final String mode;

    /**
     * The default is false.
     * When 'true', the file mode bits of the directory and all files in the file hierarchy below it are changed
     * (chmod -R)
     */
    private final boolean recursive;

    public ChModParams(ChModParams.Builder builder) {
        this.mode = builder.mode;
        this.recursive = builder.recursive;
    }

    public String getMode() {
        return mode;
    }

    public boolean isRecursive() {
        return recursive;
    }

    @Override
    public String toString() {
        return "ChModParams{" +
                "mode='" + this.getMode() + '\'' +
                ", recursive=" + this.isRecursive() +
                '}';
    }

    public static class Builder {
        private String mode;
        private boolean recursive = false;

        public ChModParams build() {return new ChModParams(this);}

        public ChModParams.Builder mode(String mode) {
            this.mode = mode;
            return this;
        }

        public ChModParams.Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

    }
}
