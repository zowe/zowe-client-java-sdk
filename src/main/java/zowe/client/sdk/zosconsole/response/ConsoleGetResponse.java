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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * The console response for a z/OSMF synchronous issue console command request.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsoleGetResponse {

    /**
     * Command response text.
     */
    @JsonProperty("cmd-response")
    private final String cmdResponse;

    /**
     * If the solicited keyword is specified, indicates that the keyword was detected.
     */
    @JsonProperty("sol_key_detected")
    private final boolean keywordDetected;

    /**
     * Constructor for JSON deserialization
     *
     * @param cmdResponse Command response text
     * @param keywordDetected Indicates if the solicited keyword was detected
     */
    @JsonCreator
    public ConsoleGetResponse(
            @JsonProperty("cmd-response") final String cmdResponse,
            @JsonProperty("sol_key_detected") final boolean keywordDetected) {
        this.cmdResponse = cmdResponse;
        this.keywordDetected = keywordDetected;
    }

    /**
     * Retrieve cmdResponse specified
     *
     * @return cmdResponse value
     */
    public Optional<String> getCmdResponse() {
        return Optional.ofNullable(cmdResponse);
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
     * Create a new ConsoleGetResponse with a different cmdResponse value
     *
     * @param newCmdResponse the new command response value
     * @return a new ConsoleGetResponse instance with the updated cmdResponse
     */
    public ConsoleGetResponse withCmdResponse(final String newCmdResponse) {
        return new ConsoleGetResponse(newCmdResponse, this.keywordDetected);
    }

    @Override
    public String toString() {
        return "ConsoleResponse{" +
                "cmdResponse='" + cmdResponse + '\'' +
                ", keywordDetected=" + keywordDetected +
                '}';
    }

}
