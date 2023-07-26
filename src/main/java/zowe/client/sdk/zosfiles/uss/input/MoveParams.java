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
 * Parameter container class for Unix System Services (USS) move operation
 *
 * @author James Kostrewski
 * @version 2.0
 */
public class MoveParams {

    /**
     * The file or directory to be moved
     */
    private final Optional<String> from;

    /**
     * The default is true
     */
    private final boolean overwrite;

    public MoveParams(MoveParams.Builder builder) {
        this.from = Optional.ofNullable(builder.from);
        this.overwrite = builder.overwrite;
    }

    public Optional<String> getFrom() {
        return from;
    }

    public boolean isOverwrite() {
        return overwrite;
    }

    @Override
    public String toString() {
        return "MoveParams{" +
                "from='" + this.getFrom().get() + '\'' +
                ", overwrite=" + this.isOverwrite() +
                '}';
    }

    public static class Builder {
        private String from;
        private boolean overwrite = true;

        public MoveParams build() {
            return new MoveParams(this);
        }

        public Builder from(String from) {
            ValidateUtils.checkNullParameter(from == null, "from is null");
            ValidateUtils.checkIllegalParameter(from.isEmpty(), "from not specified");
            this.from = from;
            return this;
        }

        public Builder overwrite(boolean overwrite) {
            this.overwrite = overwrite;
            return this;
        }

    }

}
