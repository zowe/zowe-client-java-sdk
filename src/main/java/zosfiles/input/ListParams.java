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

public class ListParams {
    private Optional<String> volume;
    private Optional<String> attributes;
    private Optional<String> maxLength;
    private Optional<String> start;
    private Optional<String> recall;
    private Optional<String> pattern;
    private Optional<String> responseTimeout;

    public ListParams(ListParams.Builder builder) {
        if (builder.volume != null)
            this.volume = Optional.ofNullable(builder.volume);
        else this.volume = Optional.empty();

        if (builder.attributes != null)
            this.attributes = Optional.ofNullable(builder.attributes);
        else this.attributes = Optional.empty();

        if (builder.maxLength != null)
            this.maxLength = Optional.ofNullable(builder.maxLength);
        else this.maxLength = Optional.empty();

        if (builder.start != null)
            this.start = Optional.ofNullable(builder.start);
        else this.start = Optional.empty();

        if (builder.recall != null)
            this.recall = Optional.ofNullable(builder.recall);
        else this.recall = Optional.empty();

        if (builder.pattern != null)
            this.pattern = Optional.ofNullable(builder.pattern);
        else this.pattern = Optional.empty();

        if (builder.responseTimeout != null)
            this.responseTimeout = Optional.ofNullable(builder.responseTimeout);
        else this.responseTimeout = Optional.empty();
    }

    public Optional<String> getVolume() {
        return volume;
    }

    public Optional<String> getAttributes() {
        return attributes;
    }

    public Optional<String> getMaxLength() {
        return maxLength;
    }

    public Optional<String> getStart() {
        return start;
    }

    public Optional<String> getRecall() {
        return recall;
    }

    public Optional<String> getPattern() {
        return pattern;
    }

    public Optional<String> getResponseTimeout() {
        return responseTimeout;
    }

    @Override
    public String toString() {
        return "ListOptions{" +
                "volume=" + volume +
                ", attributes=" + attributes +
                ", maxLength=" + maxLength +
                ", start=" + start +
                ", recall=" + recall +
                ", pattern=" + pattern +
                ", responseTimeout=" + responseTimeout +
                '}';
    }

    public static class Builder {

        private String volume;
        private String attributes;
        private String maxLength;
        private String start;
        private String recall;
        private String pattern;
        private String responseTimeout;

        public ListParams.Builder volume(String volume) {
            this.volume = volume;
            return this;
        }

        public ListParams.Builder attributes(String attributes) {
            this.attributes = attributes;
            return this;
        }

        public ListParams.Builder maxLength(String maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public ListParams.Builder start(String start) {
            this.start = start;
            return this;
        }

        public ListParams.Builder recall(String recall) {
            this.recall = recall;
            return this;
        }

        public ListParams.Builder pattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public ListParams.Builder responseTimeout(String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        public ListParams build() {
            return new ListParams(this);
        }
    }
}
