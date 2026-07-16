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
 * API response for cancel workflow.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-cancel-workflow">z/OSMF REST API</a>
 *
 * @author Jorge Samaniego
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowCancelResponse {

    /**
     * Descriptive name of the canceled workflow. The name is appended with the
     * canceled state and a timestamp, for example,
     * "AutomationExample|Canceled|1423679433714".
     */
    private final String workflowName;

    /**
     * WorkflowCancelResponse Jackson constructor.
     *
     * @param workflowName canceled workflow name
     */
    @JsonCreator
    public WorkflowCancelResponse(@JsonProperty("workflowName") final String workflowName) {
        this.workflowName = workflowName == null ? "" : workflowName;
    }

    /**
     * Retrieve workflowName value.
     *
     * @return workflowName value
     */
    public String getWorkflowName() {
        return workflowName;
    }

    /**
     * Return a string value representing a WorkflowCancelResponse object.
     *
     * @return string representation of WorkflowCancelResponse
     */
    @Override
    public String toString() {
        return "WorkflowCancelResponse{" +
                "workflowName='" + workflowName + '\'' +
                '}';
    }

}
