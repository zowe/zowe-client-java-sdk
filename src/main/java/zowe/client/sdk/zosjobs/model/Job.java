/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard job response document that represents the attributes and status of a z/OS batch job
 *
 * @author Frank Giordano
 * @version 5.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {

    /**
     * Job id for a job. Uniquely identifies a job on a z/OS system
     */
    private final String jobId;

    /**
     * Job Name for a job
     */
    private final String jobName;

    /**
     * The primary or secondary JES subsystem.
     */
    private final String subSystem;

    /**
     * Owner of the job
     */
    private final String owner;

    /**
     * Status of the job
     */
    private final String status;

    /**
     * Type of job
     */
    private final String type;

    /**
     * Job class
     */
    private final String classs;

    /**
     * Return code of the job
     */
    private final String retCode;

    /**
     * Detailed job step data
     */
    private final JobStepData[] stepData;

    /**
     * Url for direct reference of job info
     */
    private final String url;

    /**
     * Spool files url for direct reference
     */
    private final String filesUrl;

    /**
     * Unique identifier of a job
     */
    private final String jobCorrelator;

    /**
     * Job phase
     */
    private final Long phase;

    /**
     * Job phase name
     */
    private final String phaseName;

    /**
     * Job constructor for Jackson JSON parsing.
     *
     * @param id            job id value
     * @param name          job name value
     * @param subSystem     JES subsystem
     * @param owner         job owner
     * @param status        job status
     * @param type          job type
     * @param classs        job class
     * @param retCode       job return code
     * @param stepData      array of step data
     * @param url           job URL
     * @param filesUrl      job spool files URL
     * @param jobCorrelator job correlator
     * @param phase         job phase
     * @param phaseName     job phase name
     */
    @JsonCreator
    public Job(
            @JsonProperty("jobid") final String id,
            @JsonProperty("jobname") final String name,
            @JsonProperty("subsystem") final String subSystem,
            @JsonProperty("owner") final String owner,
            @JsonProperty("status") final String status,
            @JsonProperty("type") final String type,
            @JsonProperty("class") final String classs,
            @JsonProperty("retcode") final String retCode,
            @JsonProperty("step-data") final JobStepData[] stepData,
            @JsonProperty("url") final String url,
            @JsonProperty("files-url") final String filesUrl,
            @JsonProperty("job-correlator") final String jobCorrelator,
            @JsonProperty("phase") final Long phase,
            @JsonProperty("phase-name") final String phaseName) {
        this.jobId = id == null ? "" : id;
        this.jobName = name == null ? "" : name;
        this.subSystem = subSystem == null ? "" : subSystem;
        this.owner = owner == null ? "" : owner;
        this.status = status == null ? "" : status;
        this.type = type == null ? "" : type;
        this.classs = classs == null ? "" : classs;
        this.retCode = retCode == null ? "" : retCode;
        this.stepData = stepData == null ? new JobStepData[0] : stepData;
        this.url = url == null ? "" : url;
        this.filesUrl = filesUrl == null ? "" : filesUrl;
        this.jobCorrelator = jobCorrelator == null ? "" : jobCorrelator;
        this.phase = phase == null ? 0L : phase;
        this.phaseName = phaseName == null ? "" : phaseName;
    }

    /**
     * Retrieve classs specified
     *
     * @return classs value
     */
    public String getClasss() {
        return classs;
    }

    /**
     * Retrieve filesUrl specified
     *
     * @return filesUrl value
     */
    public String getFilesUrl() {
        return filesUrl;
    }

    /**
     * Retrieve jobCorrelator specified
     *
     * @return jobCorrelator value
     */
    public String getJobCorrelator() {
        return jobCorrelator;
    }

    /**
     * Retrieve jobId specified
     *
     * @return jobId value
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Retrieve jobName specified
     *
     * @return jobName value
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Retrieve an owner specified
     *
     * @return owner value
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Retrieve phase specified
     *
     * @return phase value
     */
    public Long getPhase() {
        return phase;
    }

    /**
     * Retrieve phaseName specified
     *
     * @return phaseName value
     */
    public String getPhaseName() {
        return phaseName;
    }

    /**
     * Retrieve retCode specified
     *
     * @return retCode value
     */
    public String getRetCode() {
        return retCode;
    }

    /**
     * Retrieve status specified
     *
     * @return status value
     */
    public String getStatus() {
        return status;
    }

    /**
     * Retrieve stepData specified
     *
     * @return stepData value
     */
    public JobStepData[] getStepData() {
        return stepData;
    }

    /**
     * Retrieve subSystem specified
     *
     * @return subSystem value
     */
    public String getSubSystem() {
        return subSystem;
    }

    /**
     * Retrieve type specified
     *
     * @return type value
     */
    public String getType() {
        return type;
    }

    /**
     * Retrieve url specified
     *
     * @return url value
     */
    public String getUrl() {
        return url;
    }

    /**
     * Return string value representing a Job object
     *
     * @return string representation of Job
     */
    @Override
    public String toString() {
        return "Job{" +
                "jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", subSystem='" + subSystem + '\'' +
                ", owner='" + owner + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", classs='" + classs + '\'' +
                ", retCode='" + retCode + '\'' +
                ", stepData=" + (stepData != null ? stepData.length : "null") +
                ", url='" + url + '\'' +
                ", filesUrl='" + filesUrl + '\'' +
                ", jobCorrelator='" + jobCorrelator + '\'' +
                ", phase=" + phase +
                ", phaseName='" + phaseName + '\'' +
                '}';
    }

}
