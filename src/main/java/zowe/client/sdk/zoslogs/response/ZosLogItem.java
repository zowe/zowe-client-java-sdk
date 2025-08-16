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
package zowe.client.sdk.zoslogs.response;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Represents the details of one log item.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosLogItem {

    /**
     * Eight-character command and response token (CART).
     */
    private final String cart;

    /**
     * The color of the message.
     */
    private final String color;

    /**
     * The name of the job that generates the message.
     */
    private final String jobName;

    /**
     * The content of the message.
     */
    private final String message;

    /**
     * The message ID.
     */
    private final String messageId;

    /**
     * Reply ID, in decimal.
     */
    private final String replyId;

    /**
     * Original eight-character system name.
     */
    private final String system;

    /**
     * Type variable
     */
    private final String type;

    /**
     * Indicate whether the message is a DOM, WTOR, or HOLD message.
     */
    private final String subType;

    /**
     * For example, "Thu Feb 03 03:00 GMT 2021".
     */
    private final String time;

    /**
     * UNIX timestamp. For example, 1621920830109.
     */
    private final Long timeStamp;

    /**
     * ZosLogItem constructor
     *
     * @param builder Builder Object
     * @author Frank Giordano
     */
    private ZosLogItem(final Builder builder) {
        this.cart = builder.cart;
        this.color = builder.color;
        this.jobName = builder.jobName;
        this.message = builder.message;
        this.messageId = builder.messageId;
        this.replyId = builder.replyId;
        this.system = builder.system;
        this.type = builder.type;
        this.subType = builder.subType;
        this.time = builder.time;
        this.timeStamp = builder.timeStamp;
    }

    /**
     * Retrieve cart optional value
     *
     * @return cart optional value
     */
    public Optional<String> getCart() {
        return Optional.ofNullable(cart);
    }

    /**
     * Retrieve color optional value
     *
     * @return color optional value
     */
    public Optional<String> getColor() {
        return Optional.ofNullable(color);
    }

    /**
     * Retrieve jobName optional value
     *
     * @return jobName optional value
     */
    public Optional<String> getJobName() {
        return Optional.ofNullable(jobName);
    }

    /**
     * Retrieve message optional value
     *
     * @return message optional value
     */
    public Optional<String> getMessage() {
        return Optional.ofNullable(message);
    }

    /**
     * Retrieve messageId optional value
     *
     * @return messageId optional value
     */
    public Optional<String> getMessageId() {
        return Optional.ofNullable(messageId);
    }

    /**
     * Retrieve replyId optional value
     *
     * @return replyId optional value
     */
    public Optional<String> getReplyId() {
        return Optional.ofNullable(replyId);
    }

    /**
     * Retrieve system optional value
     *
     * @return system optional value
     */
    public Optional<String> getSystem() {
        return Optional.ofNullable(system);
    }

    /**
     * Retrieve type optional value
     *
     * @return type optional value
     */
    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    /**
     * Retrieve subType optional value
     *
     * @return subType optional value
     */
    public Optional<String> getSubType() {
        return Optional.ofNullable(subType);
    }

    /**
     * Retrieve time optional value
     *
     * @return time optional value
     */
    public Optional<String> getTime() {
        return Optional.ofNullable(time);
    }

    /**
     * Retrieve number optional value
     *
     * @return number optional value
     */
    public OptionalLong getTimeStamp() {
        return (timeStamp == null) ? OptionalLong.empty() : OptionalLong.of(timeStamp);
    }

    /**
     * Builder class for ZosLogItem
     */
    public static class Builder {

        /**
         * Eight-character command and response token (CART).
         */
        private String cart;

        /**
         * The color of the message.
         */
        private String color;

        /**
         * The name of the job that generates the message.
         */
        private String jobName;

        /**
         * The content of the message.
         */
        private String message;

        /**
         * The message ID.
         */
        private String messageId;

        /**
         * Reply ID, in decimal.
         */
        private String replyId;

        /**
         * Original eight-character system name.
         */
        private String system;

        /**
         * Type variable
         */
        private String type;

        /**
         * Indicate whether the message is a DOM, WTOR, or HOLD message.
         */
        private String subType;

        /**
         * For example, "Thu Feb 03 03:00 GMT 2021".
         */
        private String time;

        /**
         * UNIX timestamp. For example, 1621920830109.
         */
        private Long timeStamp;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set cart string value from response
         *
         * @param cart string value
         * @return Builder object
         */
        public Builder cart(final String cart) {
            this.cart = cart;
            return this;
        }

        /**
         * Set a color string value from response
         *
         * @param color string value
         * @return Builder object
         */
        public Builder color(final String color) {
            this.color = color;
            return this;
        }

        /**
         * Set jobName string value from response
         *
         * @param jobName string value
         * @return Builder object
         */
        public Builder jobName(final String jobName) {
            this.jobName = jobName;
            return this;
        }

        /**
         * Set a message string value from response
         *
         * @param message string value
         * @return Builder object
         */
        public Builder message(final String message) {
            this.message = message;
            return this;
        }

        /**
         * Set messageId string value from response
         *
         * @param messageId string value
         * @return Builder object
         */
        public Builder messageId(final String messageId) {
            this.messageId = messageId;
            return this;
        }

        /**
         * Set replyId string value from response
         *
         * @param replyId string value
         * @return Builder object
         */
        public Builder replyId(final String replyId) {
            this.replyId = replyId;
            return this;
        }

        /**
         * Set system string value from response
         *
         * @param system string value
         * @return Builder object
         */
        public Builder system(final String system) {
            this.system = system;
            return this;
        }

        /**
         * Set type string value from response
         *
         * @param type string value
         * @return Builder object
         */
        public Builder type(final String type) {
            this.type = type;
            return this;
        }

        /**
         * Set subType string value from response
         *
         * @param subType string value
         * @return Builder object
         */
        public Builder subType(final String subType) {
            this.subType = subType;
            return this;
        }

        /**
         * Set time string value from response
         *
         * @param time string value
         * @return Builder object
         */
        public Builder time(final String time) {
            this.time = time;
            return this;
        }

        /**
         * Set timeStamp long value from response
         *
         * @param timeStamp long value
         * @return Builder object
         */
        public Builder timeStamp(final long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        /**
         * Return ZosLogItem object based on Builder this object
         *
         * @return ZosLogItem this object
         */
        public ZosLogItem build() {
            return new ZosLogItem(this);
        }

    }

}