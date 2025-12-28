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
     * System name of the z/OS system on which the job ran (up to 8 characters)
     */
    private final String execSystem;

    /**
     * Member name of the z/OS system on which the job ran (up to 8 characters)
     */
    private final String execMember;

    /**
     * Time when the job was submitted to run (the input end time)
     */
    private final String execSubmitted;

    /**
     * Time when job execution started
     */
    private final String execStarted;

    /**
     * Time when job execution ended
     */
    private final String execEnded;

    /**
     * Text identifying one or more reasons why the job is not running
     */
    private final String reasonNotRunning;

    /* Null-handling helpers */

    private static String orEmpty(String value) {
        return value == null ? "" : value;
    }

    private static JobStepData[] orEmpty(JobStepData[] value) {
        return value == null ? new JobStepData[0] : value;
    }

    private static Long orZero(Long value) {
        return value == null ? 0L : value;
    }

    /**
     * Job constructor for Jackson JSON parsing.
     *
     * @param id               job id value
     * @param name             job name value
     * @param subSystem        JES subsystem
     * @param owner            job owner
     * @param status           job status
     * @param type             job type
     * @param classs           job class
     * @param retCode          job return code
     * @param stepData         array of step data
     * @param url              job URL
     * @param filesUrl         job spool files URL
     * @param jobCorrelator    job correlator
     * @param phase            job phase
     * @param phaseName        job phase name
     * @param execSystem       z/OS system name on which job ran
     * @param execMember       member name of the z/OS system
     * @param execSubmitted    time when the job was submitted
     * @param execStarted      time when job execution started
     * @param execEnded        time when job execution ended
     * @param reasonNotRunning reason job is not running
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
            @JsonProperty("phase-name") final String phaseName,
            @JsonProperty("exec-system") final String execSystem,
            @JsonProperty("exec-member") final String execMember,
            @JsonProperty("exec-submitted") final String execSubmitted,
            @JsonProperty("exec-started") final String execStarted,
            @JsonProperty("exec-ended") final String execEnded,
            @JsonProperty("reason-not-running") final String reasonNotRunning) {

        this.jobId = orEmpty(id);
        this.jobName = orEmpty(name);
        this.subSystem = orEmpty(subSystem);
        this.owner = orEmpty(owner);
        this.status = orEmpty(status);
        this.type = orEmpty(type);
        this.classs = orEmpty(classs);
        this.retCode = orEmpty(retCode);
        this.stepData = orEmpty(stepData);
        this.url = orEmpty(url);
        this.filesUrl = orEmpty(filesUrl);
        this.jobCorrelator = orEmpty(jobCorrelator);
        this.phase = orZero(phase);
        this.phaseName = orEmpty(phaseName);
        this.execSystem = orEmpty(execSystem);
        this.execMember = orEmpty(execMember);
        this.execSubmitted = orEmpty(execSubmitted);
        this.execStarted = orEmpty(execStarted);
        this.execEnded = orEmpty(execEnded);
        this.reasonNotRunning = orEmpty(reasonNotRunning);
    }

    /* Builder */

    /**
     * Create a new {@link Builder} for {@link Job}.
     *
     * @return builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for {@link Job}.
     * <p>
     * Required fields:
     * <ul>
     *   <li>{@code jobId}</li>
     *   <li>{@code jobName}</li>
     * </ul>
     */
    public static final class Builder {

        private String jobId;
        private String jobName;
        private String subSystem;
        private String owner;
        private String status;
        private String type;
        private String classs;
        private String retCode;
        private JobStepData[] stepData;
        private String url;
        private String filesUrl;
        private String jobCorrelator;
        private Long phase;
        private String phaseName;
        private String execSystem;
        private String execMember;
        private String execSubmitted;
        private String execStarted;
        private String execEnded;
        private String reasonNotRunning;

        private Builder() {
        }

        public Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Builder subSystem(String subSystem) {
            this.subSystem = subSystem;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder classs(String classs) {
            this.classs = classs;
            return this;
        }

        public Builder retCode(String retCode) {
            this.retCode = retCode;
            return this;
        }

        public Builder stepData(JobStepData[] stepData) {
            this.stepData = stepData;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder filesUrl(String filesUrl) {
            this.filesUrl = filesUrl;
            return this;
        }

        public Builder jobCorrelator(String jobCorrelator) {
            this.jobCorrelator = jobCorrelator;
            return this;
        }

        public Builder phase(Long phase) {
            this.phase = phase;
            return this;
        }

        public Builder phaseName(String phaseName) {
            this.phaseName = phaseName;
            return this;
        }

        public Builder execSystem(String execSystem) {
            this.execSystem = execSystem;
            return this;
        }

        public Builder execMember(String execMember) {
            this.execMember = execMember;
            return this;
        }

        public Builder execSubmitted(String execSubmitted) {
            this.execSubmitted = execSubmitted;
            return this;
        }

        public Builder execStarted(String execStarted) {
            this.execStarted = execStarted;
            return this;
        }

        public Builder execEnded(String execEnded) {
            this.execEnded = execEnded;
            return this;
        }

        public Builder reasonNotRunning(String reasonNotRunning) {
            this.reasonNotRunning = reasonNotRunning;
            return this;
        }

        /**
         * Build a {@link Job}.
         *
         * @return immutable {@link Job}
         */
        public Job build() {
            return new Job(
                    jobId,
                    jobName,
                    subSystem,
                    owner,
                    status,
                    type,
                    classs,
                    retCode,
                    stepData,
                    url,
                    filesUrl,
                    jobCorrelator,
                    phase,
                    phaseName,
                    execSystem,
                    execMember,
                    execSubmitted,
                    execStarted,
                    execEnded,
                    reasonNotRunning
            );
        }
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
     * Retrieve subSystem specified
     *
     * @return subSystem value
     */
    public String getSubSystem() {
        return subSystem;
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
     * Retrieve status specified
     *
     * @return status value
     */
    public String getStatus() {
        return status;
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
     * Retrieve classs specified
     *
     * @return classs value
     */
    public String getClasss() {
        return classs;
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
     * Retrieve stepData specified
     *
     * @return stepData value
     */
    public JobStepData[] getStepData() {
        return stepData;
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
     * Retrieve system name on which the job ran
     *
     * @return system name
     */
    public String getExecSystem() {
        return execSystem;
    }

    /**
     * Retrieve system member name on which the job ran
     *
     * @return member name
     */
    public String getExecMember() {
        return execMember;
    }

    /**
     * Retrieve time when the job was submitted
     *
     * @return time when the job was submitted
     */
    public String getExecSubmitted() {
        return execSubmitted;
    }

    /**
     * Retrieve time when job execution started
     *
     * @return time when job execution started
     */
    public String getExecStarted() {
        return execStarted;
    }

    /**
     * Retrieve time when job execution ended
     *
     * @return time when job execution ended
     */
    public String getExecEnded() {
        return execEnded;
    }

    /**
     * Retrieve reason text why the job is not running
     *
     * @return reason text why the job is not running
     */
    public String getReasonNotRunning() {
        return reasonNotRunning;
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
                ", stepData=" + stepData.length +
                ", url='" + url + '\'' +
                ", filesUrl='" + filesUrl + '\'' +
                ", jobCorrelator='" + jobCorrelator + '\'' +
                ", phase=" + phase +
                ", phaseName='" + phaseName + '\'' +
                ", execSystem='" + execSystem + '\'' +
                ", execMember='" + execMember + '\'' +
                ", execSubmitted='" + execSubmitted + '\'' +
                ", execStarted='" + execStarted + '\'' +
                ", execEnded='" + execEnded + '\'' +
                ", reasonNotRunning='" + reasonNotRunning + '\'' +
                '}';
    }

}
