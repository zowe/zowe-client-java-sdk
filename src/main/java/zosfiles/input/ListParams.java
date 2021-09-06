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
 * This interface defines the options that can be sent into the list data set function
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class ListParams {

    /**
     * The volume where the data set resides
     */
    private final Optional<String> volume;

    /**
     * The indicator that we want to show more attributes
     */
    private final Optional<String> attributes;

    /**
     * The indicator that we want to show less data sets or members
     */
    private final Optional<String> maxLength;

    /**
     * An optional search parameter that specifies the first data set name to return to the response document
     */
    private final Optional<String> start;

    /**
     * An optional parameter that specifies how to handle migrated data sets
     */
    private final Optional<String> recall;

    /**
     * An optional pattern for restricting the response list
     */
    private final Optional<String> pattern;

    /**
     * Response time out value
     */
    private final Optional<String> responseTimeout;

    private ListParams(ListParams.Builder builder) {
        this.volume = Optional.ofNullable(builder.volume);
        this.attributes = Optional.ofNullable(builder.attributes);
        this.maxLength = Optional.ofNullable(builder.maxLength);
        this.start = Optional.ofNullable(builder.start);
        this.recall = Optional.ofNullable(builder.recall);
        this.pattern = Optional.ofNullable(builder.pattern);
        this.responseTimeout = Optional.ofNullable(builder.responseTimeout);
    }

    /**
     * Retrieve volume value
     *
     * @return volume value
     * @author Leonid Baranov
     */
    public Optional<String> getVolume() {
        return volume;
    }

    /**
     * Retrieve attributes value
     *
     * @return attributes value
     * @author Leonid Baranov
     */
    public Optional<String> getAttributes() {
        return attributes;
    }

    /**
     * Retrieve maxLength value
     *
     * @return maxLength value
     * @author Leonid Baranov
     */
    public Optional<String> getMaxLength() {
        return maxLength;
    }

    /**
     * Retrieve start value
     *
     * @return start value
     * @author Leonid Baranov
     */
    public Optional<String> getStart() {
        return start;
    }

    /**
     * Retrieve recall value
     *
     * @return recall value
     * @author Leonid Baranov
     */
    public Optional<String> getRecall() {
        return recall;
    }

    /**
     * Retrieve pattern value
     *
     * @return pattern value
     * @author Leonid Baranov
     */
    public Optional<String> getPattern() {
        return pattern;
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     * @author Leonid Baranov
     */
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
