/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.zosmf;

import java.util.Optional;

/**
 * Interface for TSO/E messages
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class TsoMessages {

    /**
     * Tso message type of TSO/E messages
     */
    private Optional<TsoMessage> tsoMessage = Optional.empty();

    /**
     * Tso prompt message type of TSO/E messages
     */
    private Optional<TsoPromptMessage> tsoPrompt = Optional.empty();

    /**
     * Tso response message type of TSO/E messages
     */
    private Optional<TsoResponseMessage> tsoResponse = Optional.empty();

    /**
     * Retrieve tsoMessage specified
     *
     * @return tsoMessage value
     * @author Frank Giordano
     */
    public Optional<TsoMessage> getTsoMessage() {
        return tsoMessage;
    }

    /**
     * Assign tsoMessage value
     *
     * @param tsoMessage tso message value
     * @author Frank Giordano
     */
    public void setTsoMessage(TsoMessage tsoMessage) {
        this.tsoMessage = Optional.ofNullable(tsoMessage);
    }

    /**
     * Retrieve tsoPrompt specified
     *
     * @return tsoPrompt value
     * @author Frank Giordano
     */
    public Optional<TsoPromptMessage> getTsoPrompt() {
        return tsoPrompt;
    }

    /**
     * Assign tsoPrompt value
     *
     * @param tsoPrompt tso prompt value
     * @author Frank Giordano
     */
    public void setTsoPrompt(TsoPromptMessage tsoPrompt) {
        this.tsoPrompt = Optional.ofNullable(tsoPrompt);
    }

    /**
     * Retrieve tsoResponse specified
     *
     * @return tsoResponse value
     * @author Frank Giordano
     */
    public Optional<TsoResponseMessage> getTsoResponse() {
        return tsoResponse;
    }

    /**
     * Assign tsoResponse value
     *
     * @param tsoResponse tso response value
     * @author Frank Giordano
     */
    public void setTsoResponse(TsoResponseMessage tsoResponse) {
        this.tsoResponse = Optional.ofNullable(tsoResponse);
    }

    @Override
    public String toString() {
        return "TsoMessages{" +
                "tsoMessage=" + tsoMessage +
                ", tsoPrompt=" + tsoPrompt +
                ", tsoResponse=" + tsoResponse +
                '}';
    }

}
