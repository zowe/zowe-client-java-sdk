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
 * @version 2.0
 */
public class ZosLogItem {

    /**
     * Eight character command and response token (CART).
     */
    private final Optional<String> cart;

    /**
     * The color of the message.
     */
    private final Optional<String> color;

    /**
     * The name of the job that generates the message.
     */
    private final Optional<String> jobName;

    /**
     * The content of the message.
     */
    private final Optional<String> message;

    /**
     * The message ID.
     */
    private final Optional<String> messageId;

    /**
     * Reply ID, in decimal.
     */
    private final Optional<String> replyId;

    /**
     * Original eight character system name.
     */
    private final Optional<String> system;

    /**
     * Type variable
     */
    private final Optional<String> type;

    /**
     * Indicate whether the message is a DOM, WTOR, or HOLD message.
     */
    private final Optional<String> subType;

    /**
     * For example, "Thu Feb 03 03:00 GMT 2021".
     */
    private final Optional<String> time;

    /**
     * UNIX timestamp. For example, 1621920830109.
     */
    private final OptionalLong timeStamp;

    /**
     * ZosLogItem constructor
     *
     * @param builder ZosLogItem.Builder Object
     * @author Frank Giordano
     */
    private ZosLogItem(final ZosLogItem.Builder builder) {
        this.cart = Optional.ofNullable(builder.cart);
        this.color = Optional.ofNullable(builder.color);
        this.jobName = Optional.ofNullable(builder.jobName);
        this.message = Optional.ofNullable(builder.message);
        this.messageId = Optional.ofNullable(builder.messageId);
        this.replyId = Optional.ofNullable(builder.replyId);
        this.system = Optional.ofNullable(builder.system);
        this.type = Optional.ofNullable(builder.type);
        this.subType = Optional.ofNullable(builder.subType);
        this.time = Optional.ofNullable(builder.time);
        if (builder.timeStamp == 0) {
            this.timeStamp = OptionalLong.empty();
        } else {
            this.timeStamp = OptionalLong.of(builder.timeStamp);
        }
    }

    /**
     * Retrieve cart optional value
     *
     * @return cart optional value
     */
    public Optional<String> getCart() {
        return cart;
    }

    /**
     * Retrieve color optional value
     *
     * @return color optional value
     */
    public Optional<String> getColor() {
        return color;
    }

    /**
     * Retrieve jobName optional value
     *
     * @return jobName optional value
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve message optional value
     *
     * @return message optional value
     */
    public Optional<String> getMessage() {
        return message;
    }

    /**
     * Retrieve messageId optional value
     *
     * @return messageId optional value
     */
    public Optional<String> getMessageId() {
        return messageId;
    }

    /**
     * Retrieve replyId optional value
     *
     * @return replyId optional value
     */
    public Optional<String> getReplyId() {
        return replyId;
    }

    /**
     * Retrieve system optional value
     *
     * @return system optional value
     */
    public Optional<String> getSystem() {
        return system;
    }

    /**
     * Retrieve type optional value
     *
     * @return type optional value
     */
    public Optional<String> getType() {
        return type;
    }

    /**
     * Retrieve subType optional value
     *
     * @return subType optional value
     */
    public Optional<String> getSubType() {
        return subType;
    }

    /**
     * Retrieve time optional value
     *
     * @return time optional value
     */
    public Optional<String> getTime() {
        return time;
    }

    /**
     * Retrieve number optional value
     *
     * @return number optional value
     */
    public OptionalLong getTimeStamp() {
        return timeStamp;
    }

    public static class Builder {

        private String cart;
        private String color;
        private String jobName;
        private String message;
        private String messageId;
        private String replyId;
        private String system;
        private String type;
        private String subType;
        private String time;
        private Long timeStamp;

        public ZosLogItem build() {
            return new ZosLogItem(this);
        }

        /**
         * Set cart string value from response
         *
         * @param cart string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder cart(final String cart) {
            this.cart = cart;
            return this;
        }

        /**
         * Set color string value from response
         *
         * @param color string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder color(final String color) {
            this.color = color;
            return this;
        }

        /**
         * Set jobName string value from response
         *
         * @param jobName string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder jobName(final String jobName) {
            this.jobName = jobName;
            return this;
        }

        /**
         * Set message string value from response
         *
         * @param message string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder message(final String message) {
            this.message = message;
            return this;
        }

        /**
         * Set messageId string value from response
         *
         * @param messageId string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder messageId(final String messageId) {
            this.messageId = messageId;
            return this;
        }

        /**
         * Set replyId string value from response
         *
         * @param replyId string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder replyId(final String replyId) {
            this.replyId = replyId;
            return this;
        }

        /**
         * Set system string value from response
         *
         * @param system string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder system(final String system) {
            this.system = system;
            return this;
        }

        /**
         * Set type string value from response
         *
         * @param type string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder type(final String type) {
            this.type = type;
            return this;
        }

        /**
         * Set subType string value from response
         *
         * @param subType string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder subType(final String subType) {
            this.subType = subType;
            return this;
        }

        /**
         * Set time string value from response
         *
         * @param time string value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder time(final String time) {
            this.time = time;
            return this;
        }

        /**
         * Set timeStamp long value from response
         *
         * @param timeStamp long value
         * @return ZosLogItem.Builder object
         */
        public ZosLogItem.Builder timeStamp(final long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

    }

}