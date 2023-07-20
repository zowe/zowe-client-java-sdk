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

import java.util.Optional;

/**
 * Parameter container class forUnix System Services (USS) copy operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class CopyParams {
    /**
     * The file or directory to be copied. May not be specified with 'from-dataset'.
     */
    private Optional<String> from;
    /**
     * The dataset to be copied. May not be specified with 'from'
     */
    private Optional<String> from_dataset;
    /**
     * The default is true. May not be specified with 'from_dataset'
     */
    private boolean overwrite;
    /**
     * The default is false.
     * When 'true', copies all the files and subdirectories that are specified by source into a directory (cp -R).
     * May not be specified with 'from-dataset'.
     */
    private boolean recursive;


    public CopyParams(CopyParams.Builder builder) {
        this.from = Optional.ofNullable(builder.from);
        this.from_dataset = Optional.ofNullable(builder.from_dataset);
        this.overwrite = builder.overwrite;
        this.recursive = builder.recursive;
    }

    public Optional<String> getFrom() {
        return from;
    }

    public Optional<String> getFrom_dataset() {
        return from_dataset;
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
                ", from_dataset=" + this.getFrom_dataset() +
                ", overwrite=" + this.isOverwrite() +
                ", recursive=" + this.isRecursive() +
                "}";
    }
    public static class Builder {
        private String from;
        private String from_dataset;
        private boolean overwrite = true;
        private boolean recursive = false;

        public CopyParams build() {
            return new CopyParams(this);
        }

        public CopyParams.Builder from(String from) throws Exception {
            if (this.from_dataset != null) {
                throw new Exception("Cannot specify both 'from' and 'from_dataset'");
            }
            this.from = from;
            return this;
        }

        public CopyParams.Builder from_dataset(String from_dataset) {
            this.from_dataset = from_dataset;
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
