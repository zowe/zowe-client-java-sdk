/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfworkflow.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

/**
 * Workflow jobInfo information returned for a template step that submits a job.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowJobInfo {

    /**
     * The jobstatus object that contains details about the job.
     */
    private final WorkflowJobStatus jobstatus;

    /**
     * Array of objects that contain details about each of the files created by the job.
     */
    private final List<WorkflowJobFile> jobfiles;

    /**
     * WorkflowJobInfo Jackson constructor.
     *
     * @param jobstatus details about the job
     * @param jobfiles  details about each file created by the job
     */
    @JsonCreator
    public WorkflowJobInfo(
            @JsonProperty("jobstatus") final WorkflowJobStatus jobstatus,
            @JsonProperty("jobfiles") final List<WorkflowJobFile> jobfiles) {
        this.jobstatus = jobstatus;
        this.jobfiles = orEmpty(jobfiles);
    }

    /* Null-handling helpers */

    private static <T> List<T> orEmpty(final List<T> value) {
        return value == null ? Collections.emptyList() : value;
    }

    /**
     * Retrieve jobstatus value.
     *
     * @return jobstatus value
     */
    public WorkflowJobStatus getJobstatus() {
        return jobstatus;
    }

    /**
     * Retrieve jobfiles value.
     *
     * @return jobfiles value
     */
    public List<WorkflowJobFile> getJobfiles() {
        return jobfiles;
    }

    /**
     * Return string value representing WorkflowJobInfo object.
     *
     * @return string representation of WorkflowJobInfo
     */
    @Override
    public String toString() {
        return "WorkflowJobInfo{" +
                "jobstatus=" + jobstatus +
                ", jobfiles=" + jobfiles +
                '}';
    }

}
