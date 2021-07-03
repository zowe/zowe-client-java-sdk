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

public class TsoMessages {

    public TsoMessage tsoMessage;
    public TsoPromptMessage tsoPrompt;
    public TsoResponseMessage tsoRespsonse;

    public TsoMessage getTsoMessage() {
        return tsoMessage;
    }

    public void setTsoMessage(TsoMessage tsoMessage) {
        this.tsoMessage = tsoMessage;
    }

    public TsoPromptMessage getTsoPrompt() {
        return tsoPrompt;
    }

    public void setTsoPrompt(TsoPromptMessage tsoPrompt) {
        this.tsoPrompt = tsoPrompt;
    }

    public TsoResponseMessage getTsoRespsonse() {
        return tsoRespsonse;
    }

    public void setTsoRespsonse(TsoResponseMessage tsoRespsonse) {
        this.tsoRespsonse = tsoRespsonse;
    }

}
