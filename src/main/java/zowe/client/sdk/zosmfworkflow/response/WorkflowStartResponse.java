/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API response for start workflow.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-start-workflow">z/OSMF REST API</a>
 *
 * @author Eshaan Gupta
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowStartResponse {

    /**
     * Status message from the start workflow operation.
     */
    private final String statusMessage;

    /**
     * WorkflowStartResponse Jackson constructor.
     *
     * @param statusMessage status message
     */
    @JsonCreator
    public WorkflowStartResponse(@JsonProperty("statusMessage") final String statusMessage) {
        this.statusMessage = statusMessage == null ? "" : statusMessage;
    }

    /**
     * Retrieve statusMessage value.
     *
     * @return statusMessage value
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Return string value representing WorkflowStartResponse object.
     *
     * @return string representation of WorkflowStartResponse
     */
    @Override
    public String toString() {
        return "WorkflowStartResponse{" +
                "statusMessage='" + statusMessage + '\'' +
                '}';
    }

}
