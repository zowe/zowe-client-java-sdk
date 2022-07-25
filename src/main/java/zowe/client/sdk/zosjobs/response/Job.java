/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.response;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Standard job response document that represents the attributes and status of a z/OS batch job
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class Job {

    /**
     * Job id for a job. Uniquely identifies a job on a z/OS system
     */
    private final Optional<String> jobId;

    /**
     * Job Name for a job
     */
    private final Optional<String> jobName;

    /**
     * The primary or secondary JES subsystem. If this value is null, the job was processed by the primary subsystem.
     */
    private final Optional<String> subSystem;

    /**
     * Owner of the job
     */
    private final Optional<String> owner;

    /**
     * Status of the job
     */
    private final Optional<String> status;

    /**
     * Type of job
     */
    private final Optional<String> type;

    /**
     * Job class
     */
    private final Optional<String> classs;

    /**
     * Return code of the job
     */
    private final Optional<String> retCode;

    /**
     * Detailed job step data
     */
    private final Optional<JobStepData[]> stepData;

    /**
     * Url for direct reference of job info
     */
    private final Optional<String> url;

    /**
     * Spool files url for direct reference
     */
    private final Optional<String> filesUrl;

    /**
     * Unique identifier of job (substitute of job name and job id). If this value is null, the job was
     * submitted to JES3.
     */
    private final Optional<String> jobCorrelator;

    /**
     * Job phase
     */
    private final OptionalLong phase;

    /**
     * Job phase name
     */
    private final Optional<String> phaseName;

    private Job(Job.Builder builder) {
        this.jobId = Optional.ofNullable(builder.jobId);
        this.jobName = Optional.ofNullable(builder.jobName);
        this.subSystem = Optional.ofNullable(builder.subSystem);
        this.owner = Optional.ofNullable(builder.owner);
        this.status = Optional.ofNullable(builder.status);
        this.type = Optional.ofNullable(builder.type);
        this.classs = Optional.ofNullable(builder.classs);
        this.retCode = Optional.ofNullable(builder.retCode);
        this.stepData = Optional.ofNullable(builder.stepData);
        this.url = Optional.ofNullable(builder.url);
        this.filesUrl = Optional.ofNullable(builder.filesUrl);
        this.jobCorrelator = Optional.ofNullable(builder.jobCorrelator);
        if (builder.phase == null) {
            this.phase = OptionalLong.empty();
        } else {
            this.phase = OptionalLong.of(builder.phase);
        }
        this.phaseName = Optional.ofNullable(builder.phaseName);
    }

    /**
     * Retrieve classs specified
     *
     * @return classs value
     * @author Frank Giordano
     */
    public Optional<String> getClasss() {
        return classs;
    }

    /**
     * Retrieve filesUrl specified
     *
     * @return filesUrl value
     * @author Frank Giordano
     */
    public Optional<String> getFilesUrl() {
        return filesUrl;
    }

    /**
     * Retrieve jobCorrelator specified
     *
     * @return jobCorrelator value
     * @author Frank Giordano
     */
    public Optional<String> getJobCorrelator() {
        return jobCorrelator;
    }

    /**
     * Retrieve jobId specified
     *
     * @return jobId value
     * @author Frank Giordano
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve jobName specified
     *
     * @return jobName value
     * @author Frank Giordano
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve owner specified
     *
     * @return owner value
     * @author Frank Giordano
     */
    public Optional<String> getOwner() {
        return owner;
    }

    /**
     * Retrieve phase specified
     *
     * @return phase value
     * @author Frank Giordano
     */
    public OptionalLong getPhase() {
        return phase;
    }

    /**
     * Retrieve phaseName specified
     *
     * @return phaseName value
     * @author Frank Giordano
     */
    public Optional<String> getPhaseName() {
        return phaseName;
    }

    /**
     * Retrieve retCode specified
     *
     * @return retCode value
     * @author Frank Giordano
     */
    public Optional<String> getRetCode() {
        return retCode;
    }

    /**
     * Retrieve status specified
     *
     * @return status value
     * @author Frank Giordano
     */
    public Optional<String> getStatus() {
        return status;
    }

    /**
     * Retrieve stepData specified
     *
     * @return stepData value
     * @author Frank Giordano
     */
    public Optional<JobStepData[]> getStepData() {
        return stepData;
    }

    /**
     * Retrieve subSystem specified
     *
     * @return subSystem value
     * @author Frank Giordano
     */
    public Optional<String> getSubSystem() {
        return subSystem;
    }

    /**
     * Retrieve type specified
     *
     * @return type value
     * @author Frank Giordano
     */
    public Optional<String> getType() {
        return type;
    }

    /**
     * Retrieve url specified
     *
     * @return url value
     * @author Frank Giordano
     */
    public Optional<String> getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", subSystem=" + subSystem +
                ", owner=" + owner +
                ", status=" + status +
                ", type=" + type +
                ", classs=" + classs +
                ", retCode=" + retCode +
                ", stepData=" + stepData +
                ", url=" + url +
                ", filesUrl=" + filesUrl +
                ", jobCorrelator=" + jobCorrelator +
                ", phase=" + phase +
                ", phaseName=" + phaseName +
                '}';
    }

    public static class Builder {

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

        public Job build() {
            return new Job(this);
        }

        public Builder classs(String classs) {
            this.classs = classs;
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

        public Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Builder owner(String owner) {
            this.owner = owner;
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

        public Builder retCode(String retCode) {
            this.retCode = retCode;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder stepData(JobStepData[] stepData) {
            this.stepData = stepData;
            return this;
        }

        public Builder subSystem(String subSystem) {
            this.subSystem = subSystem;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

    }

}