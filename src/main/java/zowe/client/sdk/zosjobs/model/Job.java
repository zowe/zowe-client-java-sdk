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

import com.fasterxml.jackson.annotation.*;

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
    @JsonSetter(value = "url", nulls = Nulls.AS_EMPTY)
    private final String url;

    /**
     * Spool files url for direct reference
     */
    @JsonSetter(value = "filesUrl", nulls = Nulls.AS_EMPTY)
    private final String filesUrl;

    /**
     * Unique identifier of a job
     */
    @JsonSetter(value = "jobCorrelator", nulls = Nulls.AS_EMPTY)
    private final String jobCorrelator;

    /**
     * Job phase
     */
    private final Long phase;

    /**
     * Job phase name
     */
    @JsonSetter(value = "phaseName", nulls = Nulls.AS_EMPTY)
    private final String phaseName;

    /**
     * Job constructor
     *
     * @param jobId         job id value
     * @param jobName       job name value
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
            @JsonProperty("jobid") final String jobId,
            @JsonProperty("jobname") final String jobName,
            @JsonProperty("subsystem") final String subSystem,
            @JsonProperty("owner") final String owner,
            @JsonProperty("status") final String status,
            @JsonProperty("type") final String type,
            @JsonProperty("class") final String classs,
            @JsonProperty("retcode") final String retCode,
            @JsonProperty("stepData") final JobStepData[] stepData,
            @JsonProperty("url") final String url,
            @JsonProperty("files-url") final String filesUrl,
            @JsonProperty("job-correlator") final String jobCorrelator,
            @JsonProperty("phase") final Long phase,
            @JsonProperty("phase-name") final String phaseName) {
        this.jobId = jobId == null ? "" : jobId;
        this.jobName = jobName == null ? "" : jobName;
        this.subSystem = subSystem == null ? "" : subSystem;
        this.owner = owner == null ? "" : owner;
        this.status = status == null ? "" : status;
        this.type = type == null ? "" : type;
        this.classs = classs == null ? "" : classs;
        this.retCode = retCode == null ? "" : retCode;
        this.stepData = stepData == null ? new JobStepData[0] : stepData;
        this.url = url;
        this.filesUrl = filesUrl == null ? "" : filesUrl;
        this.jobCorrelator = jobCorrelator == null ? "" : jobCorrelator;
        this.phase = phase == null ? 0L : phase;
        this.phaseName = phaseName == null ? "" : phaseName;
    }

    /**
     * Private constructor for Builder
     *
     * @param builder Builder object
     */
    private Job(final Builder builder) {
        this.jobId = builder.jobId;
        this.jobName = builder.jobName;
        this.subSystem = builder.subSystem;
        this.owner = builder.owner;
        this.status = builder.status;
        this.type = builder.type;
        this.classs = builder.classs;
        this.retCode = builder.retCode;
        this.stepData = builder.stepData;
        this.url = builder.url;
        this.filesUrl = builder.filesUrl;
        this.jobCorrelator = builder.jobCorrelator;
        this.phase = builder.phase;
        this.phaseName = builder.phaseName;
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

    /**
     * Builder class for Job
     */
    public static class Builder {

        /**
         * Job id value specified for request
         */
        private String jobId;

        /**
         * Job name value specified for the request
         */
        private String jobName;

        /**
         * The primary or secondary JES subsystem. If this value is null,
         * the job was processed by the primary subsystem.
         */
        private String subSystem;

        /**
         * Owner of the job
         */
        private String owner;

        /**
         * Status of the job
         */
        private String status;

        /**
         * Type of job
         */
        private String type;

        /**
         * Job class
         */
        private String classs;

        /**
         * Return code of the job
         */
        private String retCode;

        /**
         * Detailed job step data
         */
        private JobStepData[] stepData;

        /**
         * Url for direct reference of job info
         */
        private String url;

        /**
         * Spool files url for direct reference
         */
        private String filesUrl;

        /**
         * Unique identifier of a job (substitute of job name and job id).
         * If this value is null, the job was
         * submitted to JES3.
         */
        private String jobCorrelator;

        /**
         * Job phase
         */
        private Long phase;

        /**
         * Job phase name
         */
        private String phaseName;

        /**
         * Builder constructor
         */
        public Builder() {
        }

        /**
         * Set filesUrl string value
         *
         * @param classs string value
         * @return Builder this object
         */
        public Builder classs(final String classs) {
            this.classs = classs;
            return this;
        }

        /**
         * Set filesUrl string value
         *
         * @param filesUrl string value
         * @return Builder this object
         */
        public Builder filesUrl(final String filesUrl) {
            this.filesUrl = filesUrl;
            return this;
        }

        /**
         * Set jobCorrelator string value
         *
         * @param jobCorrelator string value
         * @return Builder this object
         */
        public Builder jobCorrelator(final String jobCorrelator) {
            this.jobCorrelator = jobCorrelator;
            return this;
        }

        /**
         * Set jobId string value
         *
         * @param jobId string value
         * @return Builder this object
         */
        public Builder jobId(final String jobId) {
            this.jobId = jobId;
            return this;
        }

        /**
         * Set jobName string value
         *
         * @param jobName string value
         * @return Builder this object
         */
        public Builder jobName(final String jobName) {
            this.jobName = jobName;
            return this;
        }

        /**
         * Set owner string value
         *
         * @param owner string value
         * @return Builder this object
         */
        public Builder owner(final String owner) {
            this.owner = owner;
            return this;
        }

        /**
         * Set phase long value
         *
         * @param phase long value
         * @return Builder this object
         */
        public Builder phase(final Long phase) {
            this.phase = phase;
            return this;
        }

        /**
         * Set phaseName string value
         *
         * @param phaseName string value
         * @return Builder this object
         */
        public Builder phaseName(final String phaseName) {
            this.phaseName = phaseName;
            return this;
        }

        /**
         * Set retCode string value
         *
         * @param retCode string value
         * @return Builder this object
         */
        public Builder retCode(final String retCode) {
            this.retCode = retCode;
            return this;
        }

        /**
         * Set status string value
         *
         * @param status string value
         * @return Builder this object
         */
        public Builder status(final String status) {
            this.status = status;
            return this;
        }

        /**
         * Set stepData JobStepData array value
         *
         * @param stepData JobStepData array value
         * @return Builder this object
         */
        public Builder stepData(final JobStepData[] stepData) {
            this.stepData = stepData;
            return this;
        }

        /**
         * Set subSystem string value
         *
         * @param subSystem string value
         * @return Builder this object
         */
        public Builder subSystem(final String subSystem) {
            this.subSystem = subSystem;
            return this;
        }

        /**
         * Set type string value
         *
         * @param type string value
         * @return Builder this object
         */
        public Builder type(final String type) {
            this.type = type;
            return this;
        }

        /**
         * Set url string value
         *
         * @param url string value
         * @return Builder this object
         */
        public Builder url(final String url) {
            this.url = url;
            return this;
        }

        /**
         * Return a Job object based on Builder this object
         *
         * @return Job this object
         */
        public Job build() {
            return new Job(this);
        }

    }

}