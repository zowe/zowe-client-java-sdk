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

public class TsoMessages {

    public Optional<TsoMessage> tsoMessage = Optional.empty();
    public Optional<TsoPromptMessage> tsoPrompt = Optional.empty();
    public Optional<TsoResponseMessage> tsoResponse = Optional.empty();

    public Optional<TsoMessage> getTsoMessage() {
        return tsoMessage;
    }

    public void setTsoMessage(Optional<TsoMessage> tsoMessage) {
        this.tsoMessage = tsoMessage;
    }

    public Optional<TsoPromptMessage> getTsoPrompt() {
        return tsoPrompt;
    }

    public void setTsoPrompt(Optional<TsoPromptMessage> tsoPrompt) {
        this.tsoPrompt = tsoPrompt;
    }

    public Optional<TsoResponseMessage> getTsoResponse() {
        return tsoResponse;
    }

    public void setTsoResponse(Optional<TsoResponseMessage> tsoResponse) {
        this.tsoResponse = tsoResponse;
    }

}
