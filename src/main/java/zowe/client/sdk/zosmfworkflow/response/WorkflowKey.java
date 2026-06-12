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
 * WorkflowKey class represents an archived workflow item returned by z/OSMF.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowKey {

    /**
     * Workflow key
     */
    private final String workflowKey;

    /**
     * Workflow description
     */
    private final String workflowDescription;

    /**
     * Workflow ID
     */
    private final String workflowID;

    private static String orEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * WorkflowKey constructor used by Jackson for JSON deserialization.
     *
     * @param workflowKey         workflow key
     * @param workflowDescription workflow description
     * @param workflowID          workflow ID
     * @author Muhammad Imran
     */
    @JsonCreator
    public WorkflowKey(
            @JsonProperty("workflowKey") final String workflowKey,
            @JsonProperty("workflowDescription") final String workflowDescription,
            @JsonProperty("workflowID") final String workflowID) {
        this.workflowKey = orEmpty(workflowKey);
        this.workflowDescription = orEmpty(workflowDescription);
        this.workflowID = orEmpty(workflowID);
    }

    /**
     * Get workflow key.
     *
     * @return workflow key
     */
    public String getWorkflowKey() {
        return workflowKey;
    }

    /**
     * Get workflow description.
     *
     * @return workflow description
     */
    public String getWorkflowDescription() {
        return workflowDescription;
    }

    /**
     * Get workflow ID.
     *
     * @return workflow ID
     */
    public String getWorkflowID() {
        return workflowID;
    }

    /**
     * Return string value representing a WorkflowKey object.
     *
     * @return string representation of WorkflowKey
     */
    @Override
    public String toString() {
        return "WorkflowKey{" +
                "workflowKey='" + workflowKey + '\'' +
                ", workflowDescription='" + workflowDescription + '\'' +
                ", workflowID='" + workflowID + '\'' +
                '}';
    }

}
