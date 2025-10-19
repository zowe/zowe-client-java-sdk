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

/**
 * z/OSMF synchronous console command response messages. See the z/OSMF REST API publication for complete details.
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsoleCmdResponse {

    /**
     * Key that can be used to retrieve the command response.
     */
    @JsonProperty("cmd-response-key")
    private final String cmdResponseKey;

    /**
     * URL that can be used to retrieve the command response later
     * when the value for cmd-response is empty.
     */
    @JsonProperty("cmd-response-url")
    private final String cmdResponseUrl;

    /**
     * URI that can be used to retrieve the command response later
     * when the value for cmd-response is empty. The URI starts with /zosmf.
     */
    @JsonProperty("cmd-response-uri")
    private final String cmdResponseUri;

    /**
     * Command response text.
     */
    @JsonProperty("cmd-response")
    private final String cmdResponse;

    /**
     * Returned when a sol-key was specified, and unsol-detect-sync was specified
     * as N or not specified. If the keyword was detected in the command response,
     * the value is true. Otherwise, the value is false.
     */
    @JsonProperty("sol-key-detected")
    private final String solKeyDetected;

    /**
     * ConsoleCmdResponse constructor for JSON deserialization
     *
     * @param cmdResponseKey Key that can be used to retrieve the command response
     * @param cmdResponseUrl URL that can be used to retrieve the command response later
     * @param cmdResponseUri URI that can be used to retrieve the command response later
     * @param cmdResponse    Command response text
     * @param solKeyDetected Indicates if the keyword was detected in the command response
     */
    @JsonCreator
    public ConsoleCmdResponse(
            @JsonProperty("cmd-response-key") final String cmdResponseKey,
            @JsonProperty("cmd-response-url") final String cmdResponseUrl,
            @JsonProperty("cmd-response-uri") final String cmdResponseUri,
            @JsonProperty("cmd-response") final String cmdResponse,
            @JsonProperty("sol-key-detected") final String solKeyDetected) {
        this.cmdResponseKey = cmdResponseKey;
        this.cmdResponseUrl = cmdResponseUrl;
        this.cmdResponseUri = cmdResponseUri;
        this.cmdResponse = cmdResponse;
        this.solKeyDetected = solKeyDetected;
    }

    /**
     * Retrieve cmdResponseKey value
     *
     * @return cmdResponseKey value
     */
    public String getCmdResponseKey() {
        return cmdResponseKey;
    }

    /**
     * Retrieve cmdResponseUrl value
     *
     * @return cmdResponseUrl value
     */
    public String getCmdResponseUrl() {
        return cmdResponseUrl;
    }

    /**
     * Retrieve cmdResponseUri value
     *
     * @return cmdResponseUri value
     */
    public String getCmdResponseUri() {
        return cmdResponseUri;
    }

    /**
     * Retrieve cmdResponse value
     *
     * @return cmdResponse value
     */
    public String getCmdResponse() {
        return cmdResponse;
    }

    /**
     * Retrieve solKeyDetected value
     *
     * @return solKeyDetected value
     */
    public String getSolKeyDetected() {
        return solKeyDetected;
    }

    /**
     * Create a new ConsoleCmdResponse with a different cmdResponse value
     *
     * @param newCmdResponse the new command response value
     * @return a new ConsoleCmdResponse instance with the updated cmdResponse
     */
    public ConsoleCmdResponse withCmdResponse(final String newCmdResponse) {
        return new ConsoleCmdResponse(
                this.cmdResponseKey,
                this.cmdResponseUrl,
                this.cmdResponseUri,
                newCmdResponse,
                this.solKeyDetected
        );
    }

    /**
     * Return string value representing IssueCommandResponse object
     *
     * @return string representation of IssueCommandResponse
     */
    @Override
    public String toString() {
        return "IssueCommandResponse{" +
                "cmdResponseKey='" + cmdResponseKey + '\'' +
                ", cmdResponseUrl='" + cmdResponseUrl + '\'' +
                ", cmdResponseUri='" + cmdResponseUri + '\'' +
                ", cmdResponse='" + cmdResponse + '\'' +
                ", solKeyDetected='" + solKeyDetected + '\'' +
                '}';
    }

}
