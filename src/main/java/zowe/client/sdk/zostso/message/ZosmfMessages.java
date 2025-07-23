/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.message;

import java.util.Optional;

/**
 * The z/OSMF Ping API error message parameters. See the z/OSMF REST API documentation for full details.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZosmfMessages {

    /**
     * Error message text.
     */
    private Optional<String> messageText;

    /**
     * Error message text ID.
     */
    private Optional<String> messageId;

    /**
     * Error message stack trace.
     */
    private Optional<String> stackTrace;

    /**
     * ZosmfMessages constructor
     *
     * @param messageText error message text value
     * @param messageId   error message text id value
     * @param stackTrace  error message stack trace value
     * @author Frank Giordano
     */
    public ZosmfMessages(final String messageText, final String messageId, final String stackTrace) {
        this.messageText = Optional.ofNullable(messageText);
        this.messageId = Optional.ofNullable(messageId);
        this.stackTrace = Optional.ofNullable(stackTrace);
    }

    /**
     * Retrieve messageText specified
     *
     * @return messageText value
     */
    public Optional<String> getMessageText() {
        return messageText;
    }

    /**
     * Assign messageText value
     *
     * @param messageText value
     */
    public void setMessageText(final String messageText) {
        this.messageText = Optional.ofNullable(messageText);
    }

    /**
     * Retrieve messageId specified
     *
     * @return messageId value
     */
    public Optional<String> getMessageId() {
        return messageId;
    }

    /**
     * Assign messageId value
     *
     * @param messageId value
     */
    public void setMessageId(final String messageId) {
        this.messageId = Optional.ofNullable(messageId);
    }

    /**
     * Retrieve stackTrace specified
     *
     * @return stackTrace value
     */
    public Optional<String> getStackTrace() {
        return stackTrace;
    }

    /**
     * Assign stackTrace value
     *
     * @param stackTrace value
     */
    public void setStackTrace(final String stackTrace) {
        this.stackTrace = Optional.ofNullable(stackTrace);
    }

    /**
     * Return string value representing ZosmfMessages object
     *
     * @return string representation of ZosmfMessages
     */
    @Override
    public String toString() {
        return "ZosmfMessages{" +
                "messageText=" + messageText +
                ", messageId=" + messageId +
                ", stackTrace=" + stackTrace +
                '}';
    }

}
