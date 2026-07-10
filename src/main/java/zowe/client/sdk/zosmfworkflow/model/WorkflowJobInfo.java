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
 * @author Shantanu Danej
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowJobInfo {

    /**
     * The jobStatus object that contains details about the job.
     */
    private final WorkflowJobStatus jobStatus;

    /**
     * Array of objects that contain details about each of the files created by the job.
     */
    private final List<WorkflowJobFile> jobFiles;

    /**
     * WorkflowJobInfo Jackson constructor.
     *
     * @param jobStatus details about the job
     * @param jobFiles  details about each file created by the job
     */
    @JsonCreator
    public WorkflowJobInfo(
            @JsonProperty("jobstatus") final WorkflowJobStatus jobStatus,
            @JsonProperty("jobfiles") final List<WorkflowJobFile> jobFiles) {
        this.jobStatus = jobStatus;
        this.jobFiles = orEmpty(jobFiles);
    }

    /* Null-handling helpers */

    private static <T> List<T> orEmpty(final List<T> value) {
        return value == null ? Collections.emptyList() : value;
    }

    /**
     * Retrieve jobStatus value.
     *
     * @return jobStatus value
     */
    public WorkflowJobStatus getJobStatus() {
        return jobStatus;
    }

    /**
     * Retrieve jobFiles value.
     *
     * @return jobFiles value
     */
    public List<WorkflowJobFile> getJobFiles() {
        return jobFiles;
    }

    /**
     * Return a string value representing a WorkflowJobInfo object.
     *
     * @return string representation of WorkflowJobInfo
     */
    @Override
    public String toString() {
        return "WorkflowJobInfo{" +
                "jobStatus=" + jobStatus +
                ", jobFiles=" + jobFiles +
                '}';
    }

}
