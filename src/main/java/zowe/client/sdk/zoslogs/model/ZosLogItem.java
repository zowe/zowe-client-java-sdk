/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zoslogs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the details of one log item.
 *
 * @author Frank Giordano
 * @version 6.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
     * @param cart      Eight-character command and response token (CART)
     * @param color     The color of the message
     * @param jobName   The name of the job that generates the message
     * @param message   The content of the message
     * @param messageId The message ID
     * @param replyId   Reply ID, in decimal
     * @param system    Original eight-character system name
     * @param type      Type variable
     * @param subType   Indicate whether the message is a DOM, WTOR, or HOLD message
     * @param time      For example, "Thu Feb 03 03:00 GMT 2021"
     * @param timeStamp UNIX timestamp. For example, 1621920830109
     */
    @JsonCreator
    public ZosLogItem(
            @JsonProperty("cart") final String cart,
            @JsonProperty("color") final String color,
            @JsonProperty("jobName") final String jobName,
            @JsonProperty("message") final String message,
            @JsonProperty("messageId") final String messageId,
            @JsonProperty("replyId") final String replyId,
            @JsonProperty("system") final String system,
            @JsonProperty("type") final String type,
            @JsonProperty("subType") final String subType,
            @JsonProperty("time") final String time,
            @JsonProperty("timeStamp") final Long timeStamp) {
        this.cart = cart == null ? "" : cart;
        this.color = color == null ? "" : color;
        this.jobName = jobName == null ? "" : jobName;
        this.message = message == null ? "" : message;
        this.messageId = messageId == null ? "" : messageId;
        this.replyId = replyId == null ? "" : replyId;
        this.system = system == null ? "" : system;
        this.type = type == null ? "" : type;
        this.subType = subType == null ? "" : subType;
        this.time = time == null ? "" : time;
        this.timeStamp = timeStamp == null ? 0L : timeStamp;
    }

    /**
     * Retrieve cart value.
     *
     * @return cart value
     */
    public String getCart() {
        return cart;
    }

    /**
     * Retrieve color value.
     *
     * @return color value
     */
    public String getColor() {
        return color;
    }

    /**
     * Retrieve jobName value.
     *
     * @return jobName value
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Retrieve message value.
     *
     * @return message value
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieve messageId value.
     *
     * @return messageId value
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Retrieve replyId value.
     *
     * @return replyId value
     */
    public String getReplyId() {
        return replyId;
    }

    /**
     * Retrieve system value.
     *
     * @return system value
     */
    public String getSystem() {
        return system;
    }

    /**
     * Retrieve type value.
     *
     * @return type value
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve subType value.
     *
     * @return subType value
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Retrieve time value.
     *
     * @return time value
     */
    public String getTime() {
        return time;
    }

    /**
     * Retrieve timeStamp value.
     *
     * @return timeStamp value (0L if absent in JSON)
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Return string value representing ZosLogItem object.
     *
     * @return string representation of ZosLogItem
     */
    @Override
    public String toString() {
        return "ZosLogItem{" +
                "cart='" + cart + '\'' +
                ", color='" + color + '\'' +
                ", jobName='" + jobName + '\'' +
                ", message='" + message + '\'' +
                ", messageId='" + messageId + '\'' +
                ", replyId='" + replyId + '\'' +
                ", system='" + system + '\'' +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", time='" + time + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }

}
