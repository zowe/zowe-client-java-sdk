/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * The console response for a z/OSMF synchronous issue console command request.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties
public class ConsoleGetResponse {

    /**
     * Command response text.
     */
    @JsonProperty("cmd-response")
    private String cmdResponse;

    /**
     * If the solicited keyword is specified, indicates that the keyword was detected.
     */
    @JsonProperty("sol_key_detected")
    private boolean keywordDetected = false;

    /**
     * Retrieve cmdResponse specified
     *
     * @return cmdResponse value
     */
    public Optional<String> getCmdResponse() {
        return Optional.ofNullable(cmdResponse);
    }

    /**
     * Assign cmdResponse value
     *
     * @param cmdResponse value
     */
    public void setCmdResponse(final String cmdResponse) {
        this.cmdResponse = cmdResponse;
    }

    /**
     * Retrieve the keywordDetected specified
     *
     * @return keywordDetected true or false is keywordDetected seen
     */
    public boolean isKeywordDetected() {
        return keywordDetected;
    }

    /**
     * Assign keywordDetected value
     *
     * @param keywordDetected value
     */
    public void setKeywordDetected(boolean keywordDetected) {
        this.keywordDetected = keywordDetected;
    }

    @Override
    public String toString() {
        return "ConsoleResponse{" +
                "cmdResponse='" + cmdResponse + '\'' +
                ", keywordDetected=" + keywordDetected +
                '}';
    }

}
