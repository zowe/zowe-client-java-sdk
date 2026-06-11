/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosworkflow.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * API response for create workflow.
 * <p><a href="https://www.ibm.com/docs/en/zos/2.5.0?topic=services-create-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowCreateResponse {

    /**
     * Workflow key.
     */
    private final String workflowKey;

    /**
     * Workflow description.
     */
    private final String workflowDescription;

    /**
     * Workflow ID.
     */
    private final String workflowID;

    /**
     * Workflow definition version.
     */
    private final String workflowVersion;

    /**
     * Vendor name.
     */
    private final String vendor;

    /**
     * WorkflowCreateResponse Jackson constructor.
     *
     * @param workflowKey         workflow key
     * @param workflowDescription workflow description
     * @param workflowID          workflow id
     * @param workflowVersion     workflow version
     * @param vendor              vendor name
     */
    @JsonCreator
    public WorkflowCreateResponse(
            @JsonProperty("workflowKey") final String workflowKey,
            @JsonProperty("workflowDescription") final String workflowDescription,
            @JsonProperty("workflowID") final String workflowID,
            @JsonProperty("workflowVersion") final String workflowVersion,
            @JsonProperty("vendor") final String vendor) {
        this.workflowKey = workflowKey == null ? "" : workflowKey;
        this.workflowDescription = workflowDescription == null ? "" : workflowDescription;
        this.workflowID = workflowID == null ? "" : workflowID;
        this.workflowVersion = workflowVersion == null ? "" : workflowVersion;
        this.vendor = vendor == null ? "" : vendor;
    }

    /**
     * Retrieve workflowKey value.
     *
     * @return workflowKey value
     */
    public String getWorkflowKey() {
        return workflowKey;
    }

    /**
     * Retrieve workflowDescription value.
     *
     * @return workflowDescription value
     */
    public String getWorkflowDescription() {
        return workflowDescription;
    }

    /**
     * Retrieve workflowID value.
     *
     * @return workflowID value
     */
    public String getWorkflowID() {
        return workflowID;
    }

    /**
     * Retrieve workflowVersion value.
     *
     * @return workflowVersion value
     */
    public String getWorkflowVersion() {
        return workflowVersion;
    }

    /**
     * Retrieve vendor value.
     *
     * @return vendor value
     */
    public String getVendor() {
        return vendor;
    }

    /**
     * Return string value representing WorkflowCreateResponse object.
     *
     * @return string representation of WorkflowCreateResponse
     */
    @Override
    public String toString() {
        return "WorkflowCreateResponse{" +
                "workflowKey='" + workflowKey + '\'' +
                ", workflowDescription='" + workflowDescription + '\'' +
                ", workflowID='" + workflowID + '\'' +
                ", workflowVersion='" + workflowVersion + '\'' +
                ", vendor='" + vendor + '\'' +
                '}';
    }

}
