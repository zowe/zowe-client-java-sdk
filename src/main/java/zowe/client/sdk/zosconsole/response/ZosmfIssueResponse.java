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

import java.util.Optional;

/**
 * z/OSMF synchronous console command response messages. See the z/OSMF REST API publication for complete details.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZosmfIssueResponse {

    /**
     * Follow-up response URL.
     */
    private String cmdResponseUrl;

    /**
     * Command response text.
     */
    private String cmdResponse;

    /**
     * The follow-up response URI.
     */
    private String cmdResponseUri;

    /**
     * The command response key used for follow-up requests.
     */
    private String cmdResponseKey;

    /**
     * True if the solicited keyword requested is present.
     */
    private String solKeyDetected;

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
     * Return string value representing ZosmfIssueResponse object
     *
     * @return string representation of ZosmfIssueResponse
     */
    @Override
    public String toString() {
        return "ZosmfIssueResponse{" +
                "cmdResponseUrl=" + cmdResponseUrl +
                ", cmdResponse=" + cmdResponse +
                ", cmdResponseUri=" + cmdResponseUri +
                ", cmdResponseKey=" + cmdResponseKey +
                ", solKeyDetected=" + solKeyDetected +
                '}';
    }

}
