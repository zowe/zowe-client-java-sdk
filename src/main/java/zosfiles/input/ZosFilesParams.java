/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zosfiles.input;

import java.util.Optional;

/**
 * This interface defines the global options that apply to all zosfiles APIs
 *
 * @version 1.0
 */
public class ZosFilesParams {

    private Optional<String> responseTimeout;

    public ZosFilesParams(Builder builder) {
        if (builder.responseTimeout != null)
            this.responseTimeout = Optional.ofNullable(builder.responseTimeout);
        else this.responseTimeout = Optional.empty();

    }

    public Optional<String> getResponseTimeout() {
        return responseTimeout;
    }

    @Override
    public String toString() {
        return "ZosFilesOptions{" +
                "responseTimeout=" + responseTimeout +
                '}';
    }

    public static class Builder {

        private String responseTimeout;

        public ZosFilesParams.Builder responseTimeout(String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        public ZosFilesParams build() {
            return new ZosFilesParams(this);
        }
    }

}
