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

/**
 * Workflow job status information returned within a step jobInfo object.
 * <p>
 * <a href="https://www.ibm.com/docs/en/zos/3.2.0?topic=services-get-properties-workflow">z/OSMF REST API</a>
 *
 * @author Ashish Kumar Dash
 * @version 7.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class WorkflowJobStatus {

    /**
     * Job completion code.
     */
    private final String retcode;

    /**
     * Job name.
     */
    private final String jobName;

    /**
     * Job status, which is one of INPUT, ACTIVE, or OUTPUT.
     */
    private final String status;

    /**
     * The z/OS user ID associated with the job.
     */
    private final String owner;

    /**
     * The primary or secondary JES subsystem.
     */
    private final String subsystem;

    /**
     * Job execution class.
     */
    private final String jobClass;

    /**
     * Job type, which is one of JOB, STC, or TSU.
     */
    private final String type;

    /**
     * Job identifier.
     */
    private final String jobId;

    /**
     * WorkflowJobStatus Jackson constructor.
     *
     * @param retcode   job completion code
     * @param jobName   job name
     * @param status    job status
     * @param owner     z/OS user ID associated with the job
     * @param subsystem JES subsystem
     * @param jobClass  job execution class
     * @param type      job type
     * @param jobId     job identifier
     */
    @JsonCreator
    public WorkflowJobStatus(
            @JsonProperty("retcode") final String retcode,
            @JsonProperty("jobname") final String jobName,
            @JsonProperty("status") final String status,
            @JsonProperty("owner") final String owner,
            @JsonProperty("subsystem") final String subsystem,
            @JsonProperty("class") final String jobClass,
            @JsonProperty("type") final String type,
            @JsonProperty("jobid") final String jobId) {
        this.retcode = orEmpty(retcode);
        this.jobName = orEmpty(jobName);
        this.status = orEmpty(status);
        this.owner = orEmpty(owner);
        this.subsystem = orEmpty(subsystem);
        this.jobClass = orEmpty(jobClass);
        this.type = orEmpty(type);
        this.jobId = orEmpty(jobId);
    }

    /* Null-handling helpers */

    private static String orEmpty(final String value) {
        return value == null ? "" : value;
    }

    /**
     * Retrieve retcode value.
     *
     * @return retcode value
     */
    public String getRetcode() {
        return retcode;
    }

    /**
     * Retrieve jobname value.
     *
     * @return jobname value
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Retrieve status value.
     *
     * @return status value
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retrieve owner value.
     *
     * @return owner value
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Retrieve subsystem value.
     *
     * @return subsystem value
     */
    public String getSubsystem() {
        return subsystem;
    }

    /**
     * Retrieve class value.
     *
     * @return class value
     */
    public String getJobClass() {
        return jobClass;
    }

    /**
     * Retrieve type value.
     *
     * @return type value
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve jobid value.
     *
     * @return jobid value
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Return string value representing WorkflowJobStatus object.
     *
     * @return string representation of WorkflowJobStatus
     */
    @Override
    public String toString() {
        return "WorkflowJobStatus{" +
                "retcode='" + retcode + '\'' +
                ", jobName='" + jobName + '\'' +
                ", status='" + status + '\'' +
                ", owner='" + owner + '\'' +
                ", subsystem='" + subsystem + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", type='" + type + '\'' +
                ", jobId='" + jobId + '\'' +
                '}';
    }

}
