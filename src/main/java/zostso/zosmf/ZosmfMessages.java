/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zostso.zosmf;

import java.util.Optional;

/**
 * The z/OSMF Ping API error message parameters. See the z/OSMF REST API documentation for full details.
 *
 * @author Frank Giordano
 * @version 1.0
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
    public ZosmfMessages(Optional<String> messageText, Optional<String> messageId, Optional<String> stackTrace) {
        this.messageText = messageText;
        this.messageId = messageId;
        this.stackTrace = stackTrace;
    }

    /**
     * Retrieve messageText specified
     *
     * @return messageText value
     * @author Frank Giordano
     */
    public Optional<String> getMessageText() {
        return messageText;
    }

    /**
     * Assign messageText value
     *
     * @param messageText value
     * @author Frank Giordano
     */
    public void setMessageText(Optional<String> messageText) {
        this.messageText = messageText;
    }

    /**
     * Retrieve messageId specified
     *
     * @return messageId value
     * @author Frank Giordano
     */
    public Optional<String> getMessageId() {
        return messageId;
    }

    /**
     * Assign messageId value
     *
     * @param messageId value
     * @author Frank Giordano
     */
    public void setMessageId(Optional<String> messageId) {
        this.messageId = messageId;
    }

    /**
     * Retrieve stackTrace specified
     *
     * @return stackTrace value
     * @author Frank Giordano
     */
    public Optional<String> getStackTrace() {
        return stackTrace;
    }

    /**
     * Assign stackTrace value
     *
     * @param stackTrace value
     * @author Frank Giordano
     */
    public void setStackTrace(Optional<String> stackTrace) {
        this.stackTrace = stackTrace;
    }

    @Override
    public String toString() {
        return "ZosmfMessages{" +
                "messageText=" + messageText +
                ", messageId=" + messageId +
                ", stackTrace=" + stackTrace +
                '}';
    }

}
