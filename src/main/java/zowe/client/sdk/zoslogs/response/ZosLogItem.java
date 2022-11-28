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
 * @version 1.0
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
    private final OptionalLong number;

    public Optional<String> getCart() {
        return cart;
    }

    public Optional<String> getColor() {
        return color;
    }

    public Optional<String> getJobName() {
        return jobName;
    }

    public Optional<String> getMessage() {
        return message;
    }

    public Optional<String> getMessageId() {
        return messageId;
    }

    public Optional<String> getReplyId() {
        return replyId;
    }

    public Optional<String> getSystem() {
        return system;
    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<String> getSubType() {
        return subType;
    }

    public Optional<String> getTime() {
        return time;
    }

    public OptionalLong getNumber() {
        return number;
    }

    /**
     * ZosLogItem Constructor.
     *
     * @param builder ZosLogItem.Builder Object
     * @author Frank Giordano
     */
    private ZosLogItem(ZosLogItem.Builder builder) {
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
        if (builder.number == 0) {
            this.number = OptionalLong.empty();
        } else {
            this.number = OptionalLong.of(builder.number);
        }
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
        private Long number;

        public ZosLogItem build() {
            return new ZosLogItem(this);
        }

        public ZosLogItem.Builder cart(String cart) {
            this.cart = cart;
            return this;
        }

        public ZosLogItem.Builder color(String color) {
            this.color = color;
            return this;
        }

        public ZosLogItem.Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public ZosLogItem.Builder message(String message) {
            this.message = message;
            return this;
        }

        public ZosLogItem.Builder messageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public ZosLogItem.Builder replyId(String replyId) {
            this.replyId = replyId;
            return this;
        }

        public ZosLogItem.Builder system(String system) {
            this.system = system;
            return this;
        }

        public ZosLogItem.Builder type(String type) {
            this.type = type;
            return this;
        }

        public ZosLogItem.Builder subType(String subType) {
            this.subType = subType;
            return this;
        }

        public ZosLogItem.Builder time(String time) {
            this.time = time;
            return this;
        }

        public ZosLogItem.Builder number(long number) {
            this.number = number;
            return this;
        }

    }

}