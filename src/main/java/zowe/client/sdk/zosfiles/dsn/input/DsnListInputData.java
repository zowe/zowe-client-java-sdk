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
package zowe.client.sdk.zosfiles.dsn.input;

import zowe.client.sdk.zosfiles.dsn.types.AttributeType;

import java.util.Optional;

/**
 * Parameter container class for the list data set function
 *
 * @author Leonid Baranov
 * @version 4.0
 */
public class DsnListInputData {

    /**
     * The volume where the data set resides
     */
    private final String volume;

    /**
     * The indicator that we want to show more attributes
     */
    private final AttributeType attribute;

    /**
     * The indicator that we want to show fewer data sets or members
     */
    private final String maxLength;

    /**
     * An optional search parameter that specifies the first data set name to return to the response document
     */
    private final String start;

    /**
     * An optional parameter that specifies how to handle migrated data sets
     */
    private final String recall;

    /**
     * An optional pattern for restricting the response list
     */
    private final String pattern;

    /**
     * Response time out value
     */
    private final String responseTimeout;

    /**
     * ListParams constructor
     *
     * @param builder Builder object
     * @author Nikunj Goyal
     */
    private DsnListInputData(final Builder builder) {
        this.volume = builder.volume;
        this.attribute = builder.attribute;
        this.maxLength = builder.maxLength;
        this.start = builder.start;
        this.recall = builder.recall;
        this.pattern = builder.pattern;
        this.responseTimeout = builder.responseTimeout;
    }

    /**
     * Retrieve attributes value. The indicator that we want to show more attributes.
     *
     * @return attributes value
     */
    public Optional<AttributeType> getAttribute() {
        return Optional.ofNullable(attribute);
    }

    /**
     * Retrieve maxLength value
     *
     * @return maxLength value
     */
    public Optional<String> getMaxLength() {
        return Optional.ofNullable(maxLength);
    }

    /**
     * Retrieve pattern value
     *
     * @return pattern value
     */
    public Optional<String> getPattern() {
        return Optional.ofNullable(pattern);
    }

    /**
     * Retrieve recall value
     *
     * @return recall value
     */
    public Optional<String> getRecall() {
        return Optional.ofNullable(recall);
    }

    /**
     * Retrieve responseTimeout value
     *
     * @return responseTimeout value
     */
    public Optional<String> getResponseTimeout() {
        return Optional.ofNullable(responseTimeout);
    }

    /**
     * Retrieve start value
     *
     * @return start value
     */
    public Optional<String> getStart() {
        return Optional.ofNullable(start);
    }

    /**
     * Retrieve volume value
     *
     * @return volume value
     */
    public Optional<String> getVolume() {
        return Optional.ofNullable(volume);
    }

    /**
     * Return string value representing ListParams object
     *
     * @return string representation of ListParams
     */
    @Override
    public String toString() {
        return "ListParams{" +
                "volume=" + volume +
                ", attribute=" + attribute +
                ", maxLength=" + maxLength +
                ", start=" + start +
                ", recall=" + recall +
                ", pattern=" + pattern +
                ", responseTimeout=" + responseTimeout +
                '}';
    }

    /**
     * Builder class for ListParams
     */
    public static class Builder {

        /**
         * The volume where the data set resides
         */
        private String volume;

        /**
         * The indicator that we want to show more attributes
         */
        private AttributeType attribute;

        /**
         * The indicator that we want to show fewer data sets or members
         */
        private String maxLength;

        /**
         * An optional search parameter that specifies the first data set name to return to the response document
         */
        private String start;

        /**
         * An optional parameter that specifies how to handle migrated data sets
         */
        private String recall;

        /**
         * An optional pattern for restricting the response list
         */
        private String pattern;

        /**
         * Response time out value
         */
        private String responseTimeout;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * The volume where the data set resides
         *
         * @param volume The volume where the data set resides
         * @return Builder this object
         */
        public Builder volume(final String volume) {
            this.volume = volume;
            return this;
        }

        /**
         * Set indicator that we want to show more attributes
         *
         * @param attribute AttributeType value
         * @return Builder this object
         */
        public Builder attribute(final AttributeType attribute) {
            this.attribute = attribute;
            return this;
        }

        /**
         * The indicator that we want to show more attributes
         *
         * @param maxLength int value
         * @return Builder this object
         */
        public Builder maxLength(final String maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        /**
         * An optional search parameter that specifies the first data set name to return to the response document
         *
         * @param start string value
         * @return Builder this object
         */
        public Builder start(final String start) {
            this.start = start;
            return this;
        }

        /**
         * An optional parameter that specifies how to handle migrated data sets
         *
         * @param recall string value
         * @return Builder this object
         */
        public Builder recall(final String recall) {
            this.recall = recall;
            return this;
        }

        /**
         * An optional pattern for restricting the response list
         *
         * @param pattern string value
         * @return Builder this object
         */
        public Builder pattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }

        /**
         * Response time out value
         *
         * @param responseTimeout string value
         * @return Builder this object
         */
        public Builder responseTimeout(final String responseTimeout) {
            this.responseTimeout = responseTimeout;
            return this;
        }

        /**
         * Return ListParams object based on Builder this object
         *
         * @return ListParams this object
         */
        public DsnListInputData build() {
            return new DsnListInputData(this);
        }

    }

}
