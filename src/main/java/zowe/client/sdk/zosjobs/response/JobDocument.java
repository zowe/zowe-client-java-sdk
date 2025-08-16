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
 * @version 4.0
 */
public class JobDocument {

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
    private final Optional<StepDataDocument[]> stepData;

    /**
     * Url for direct reference of job info
     */
    private final Optional<String> url;

    /**
     * Spool files url for direct reference
     */
    private final Optional<String> filesUrl;

    /**
     * Unique identifier of a job (substitute of job name and job id). If this value is null, the job was
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

    /**
     * Job constructor
     *
     * @param builder Job.Builder object
     * @author Frank Giordano
     */
    private JobDocument(final JobDocument.Builder builder) {
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
     */
    public Optional<String> getClasss() {
        return classs;
    }

    /**
     * Retrieve filesUrl specified
     *
     * @return filesUrl value
     */
    public Optional<String> getFilesUrl() {
        return filesUrl;
    }

    /**
     * Retrieve jobCorrelator specified
     *
     * @return jobCorrelator value
     */
    public Optional<String> getJobCorrelator() {
        return jobCorrelator;
    }

    /**
     * Retrieve jobId specified
     *
     * @return jobId value
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve jobName specified
     *
     * @return jobName value
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve an owner specified
     *
     * @return owner value
     */
    public Optional<String> getOwner() {
        return owner;
    }

    /**
     * Retrieve phase specified
     *
     * @return phase value
     */
    public OptionalLong getPhase() {
        return phase;
    }

    /**
     * Retrieve phaseName specified
     *
     * @return phaseName value
     */
    public Optional<String> getPhaseName() {
        return phaseName;
    }

    /**
     * Retrieve retCode specified
     *
     * @return retCode value
     */
    public Optional<String> getRetCode() {
        return retCode;
    }

    /**
     * Retrieve status specified
     *
     * @return status value
     */
    public Optional<String> getStatus() {
        return status;
    }

    /**
     * Retrieve stepData specified
     *
     * @return stepData value
     */
    public Optional<StepDataDocument[]> getStepData() {
        return stepData;
    }

    /**
     * Retrieve subSystem specified
     *
     * @return subSystem value
     */
    public Optional<String> getSubSystem() {
        return subSystem;
    }

    /**
     * Retrieve type specified
     *
     * @return type value
     */
    public Optional<String> getType() {
        return type;
    }

    /**
     * Retrieve url specified
     *
     * @return url value
     */
    public Optional<String> getUrl() {
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
         * The primary or secondary JES subsystem. If this value is null, the job was processed by the primary subsystem.
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
        private StepDataDocument[] stepData;

        /**
         * Url for direct reference of job info
         */
        private String url;

        /**
         * Spool files url for direct reference
         */
        private String filesUrl;

        /**
         * Unique identifier of a job (substitute of job name and job id). If this value is null, the job was
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
        public Builder stepData(final StepDataDocument[] stepData) {
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
        public JobDocument build() {
            return new JobDocument(this);
        }

    }

}