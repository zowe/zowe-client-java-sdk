/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosfiles.input;

import java.util.Optional;

public class CopyParams {

    private final Optional<String> fromVolser;
    private final Optional<String> fromDataSet;
    private final Optional<String> toVolser;
    private final Optional<String> toDataSet;
    private final boolean replace;

    public CopyParams(CopyParams.Builder builder) {
        fromVolser = Optional.ofNullable(builder.fromVolser);
        fromDataSet = Optional.ofNullable(builder.fromDataSet);
        toVolser = Optional.ofNullable(builder.toVolser);
        toDataSet = Optional.ofNullable(builder.toDataSet);
        replace = builder.replace;
    }

    public Optional<String> getFromVolser() {
        return fromVolser;
    }

    public Optional<String> getFromDataSet() {
        return fromDataSet;
    }

    public Optional<String> getToVolser() {
        return toVolser;
    }

    public Optional<String> getToDataSet() {
        return toDataSet;
    }

    public boolean isReplace() {
        return replace;
    }

    public static class Builder {

        private String fromVolser;
        private String fromDataSet;
        private String toVolser;
        private String toDataSet;
        private boolean replace;

        public CopyParams.Builder fromVolser(String volser) {
            this.fromVolser = volser;
            return this;
        }

        public CopyParams.Builder fromDataSet(String dataSet) {
            this.fromDataSet = dataSet;
            return this;
        }

        public CopyParams.Builder toVolser(String volser) {
            this.toVolser = volser;
            return this;
        }

        public CopyParams.Builder toDataSet(String dataSet) {
            this.toDataSet = dataSet;
            return this;
        }

        public CopyParams.Builder replace(boolean replace) {
            this.replace = replace;
            return this;
        }

        public CopyParams build() {
            return new CopyParams(this);
        }
    }

}
