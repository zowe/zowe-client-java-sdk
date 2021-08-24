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

public class ZosmfMessages {

    private Optional<String> messageText;
    private Optional<String> messageId;
    private Optional<String> stackTrace;

    public ZosmfMessages(Optional<String> messageText, Optional<String> messageId, Optional<String> stackTrace) {
        this.messageText = messageText;
        this.messageId = messageId;
        this.stackTrace = stackTrace;
    }

    public Optional<String> getMessageText() {
        return messageText;
    }

    public void setMessageText(Optional<String> messageText) {
        this.messageText = messageText;
    }

    public Optional<String> getMessageId() {
        return messageId;
    }

    public void setMessageId(Optional<String> messageId) {
        this.messageId = messageId;
    }

    public Optional<String> getStackTrace() {
        return stackTrace;
    }

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
