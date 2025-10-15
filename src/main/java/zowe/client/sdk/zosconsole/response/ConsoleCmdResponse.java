package zowe.client.sdk.zosconsole.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

/**
 * z/OSMF synchronous console command response messages. See the z/OSMF REST API publication for complete details.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ConsoleCmdResponse {

    /**
     * Key that can be used to retrieve the command response.
     */
    @JsonProperty("cmd-response-key")
    private String cmdResponseKey;

    /**
     * URL that can be used to retrieve the command response later
     * when the value for cmd-response is empty.
     */
    @JsonProperty("cmd-response-url")
    private String cmdResponseUrl;

    /**
     * URI that can be used to retrieve the command response later
     * when the value for cmd-response is empty. The URI starts with /zosmf.
     */
    @JsonProperty("cmd-response-uri")
    private String cmdResponseUri;

    /**
     * Command response text.
     */
    @JsonProperty("cmd-response")
    private String cmdResponse;

    /**
     * Returned when a sol-key was specified, and unsol-detect-sync was specified
     * as N or not specified. If the keyword was detected in the command response,
     * the value is true. Otherwise, the value is false.
     */
    @JsonProperty("sol-key-detected")
    private String solKeyDetected;

    /**
     * Retrieve cmdResponseKey value
     *
     * @return cmdResponseKey value
     */
    public Optional<String> getCmdResponseKey() {
        return Optional.ofNullable(cmdResponseKey);
    }

    /**
     * Assign cmdResponseKey value
     *
     * @param cmdResponseKey value
     */
    public void setCmdResponseKey(final String cmdResponseKey) {
        this.cmdResponseKey = cmdResponseKey;
    }

    /**
     * Retrieve cmdResponseUrl value
     *
     * @return cmdResponseUrl value
     */
    public Optional<String> getCmdResponseUrl() {
        return Optional.ofNullable(cmdResponseUrl);
    }

    /**
     * Assign cmdResponseUrl value
     *
     * @param cmdResponseUrl value
     */
    public void setCmdResponseUrl(final String cmdResponseUrl) {
        this.cmdResponseUrl = cmdResponseUrl;
    }

    /**
     * Retrieve cmdResponseUri value
     *
     * @return cmdResponseUri value
     */
    public Optional<String> getCmdResponseUri() {
        return Optional.ofNullable(cmdResponseUri);
    }

    /**
     * Assign cmdResponseUri value
     *
     * @param cmdResponseUri value
     */
    public void setCmdResponseUri(final String cmdResponseUri) {
        this.cmdResponseUri = cmdResponseUri;
    }

    /**
     * Retrieve cmdResponse value
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
     * Retrieve solKeyDetected value
     *
     * @return solKeyDetected value
     */
    public Optional<String> getSolKeyDetected() {
        return Optional.ofNullable(solKeyDetected);
    }

    /**
     * Assign solKeyDetected value
     *
     * @param solKeyDetected value
     */
    public void setSolKeyDetected(final String solKeyDetected) {
        this.solKeyDetected = solKeyDetected;
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
