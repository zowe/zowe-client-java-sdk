/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs.response;

import java.util.Optional;

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
    private Optional<String> jobId;

    /**
     * Job name for a job
     */
    private Optional<String> jobName;

    /**
     * The primary or secondary JES subsystem. If this value is null, the job was processed by the primary subsystem.
     */
    private Optional<String> subSystem;

    /**
     * Owner of the job
     */
    private Optional<String> owner;

    /**
     * Status of the job
     */
    private Optional<String> status;

    /**
     * Type of job
     */
    private Optional<String> type;

    /**
     * Job class
     */
    private Optional<String> classs;

    /**
     * Return code of the job
     */
    private Optional<String> retCode;

    /**
     * Detailed job step data
     */
    private Optional<JobStepData[]> stepData;

    /**
     * Url for direct reference of job info
     */
    private Optional<String> url;

    /**
     * Spool files url for direct reference
     */
    private Optional<String> filesUrl;

    /**
     * Unique identifier of job (substitute of job name and job id). If this value is null, the job was
     * submitted to JES3.
     */
    private Optional<String> jobCorrelator;

    /**
     * Job phase
     */
    private Optional<Integer> phase;

    /**
     * Job phase name
     */
    private Optional<String> phaseName;

    /**
     * Explanation of error
     */
    private Optional<String> reasonNotRunning;

    public Job(Job.Builder builder) {
        if (builder.jobId != null)
            this.jobId = Optional.of(builder.jobId);
        else this.jobId = Optional.empty();

        if (builder.jobName != null)
            this.jobName = Optional.of(builder.jobName);
        else this.jobName = Optional.empty();

        if (builder.subSystem != null)
            this.subSystem = Optional.of(builder.subSystem);
        else this.subSystem = Optional.empty();

        if (builder.owner != null)
            this.owner = Optional.of(builder.owner);
        else this.owner = Optional.empty();

        if (builder.status != null)
            this.status = Optional.of(builder.status);
        else this.status = Optional.empty();

        if (builder.type != null)
            this.type = Optional.of(builder.type);
        else this.type = Optional.empty();

        if (builder.classs != null)
            this.classs = Optional.of(builder.classs);
        else this.classs = Optional.empty();

        if (builder.retCode != null)
            this.retCode = Optional.of(builder.retCode);
        else this.retCode = Optional.empty();

        if (builder.stepData != null)
            this.stepData = Optional.of(builder.stepData);
        else this.stepData = Optional.empty();

        if (builder.url != null)
            this.url = Optional.of(builder.url);
        else this.url = Optional.empty();

        if (builder.filesUrl != null)
            this.filesUrl = Optional.of(builder.filesUrl);
        else this.filesUrl = Optional.empty();

        if (builder.jobCorrelator != null)
            this.jobCorrelator = Optional.of(builder.jobCorrelator);
        else this.jobCorrelator = Optional.empty();

        if (builder.phase != null)
            this.phase = Optional.of(builder.phase);
        else this.phase = Optional.empty();

        if (builder.phaseName != null)
            this.phaseName = Optional.of(builder.phaseName);
        else this.phaseName = Optional.empty();

        if (builder.reasonNotRunning != null)
            this.reasonNotRunning = Optional.of(builder.reasonNotRunning);
        else this.reasonNotRunning = Optional.empty();
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getJobName() {
        return jobName;
    }

    public Optional<String> getSubSystem() {
        return subSystem;
    }

    public Optional<String> getOwner() {
        return owner;
    }

    public Optional<String> getStatus() {
        return status;
    }

    public Optional<String> getType() {
        return type;
    }

    public Optional<String> getClasss() {
        return classs;
    }

    public Optional<String> getRetCode() {
        return retCode;
    }

    public Optional<JobStepData[]> getStepData() {
        return stepData;
    }

    public Optional<String> getUrl() {
        return url;
    }

    public Optional<String> getFilesUrl() {
        return filesUrl;
    }

    public Optional<String> getJobCorrelator() {
        return jobCorrelator;
    }

    public Optional<Integer> getPhase() {
        return phase;
    }

    public Optional<String> getPhaseName() {
        return phaseName;
    }

    public Optional<String> getReasonNotRunning() {
        return reasonNotRunning;
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
                ", reasonNotRunning=" + reasonNotRunning +
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
        private Integer phase;
        private String phaseName;
        private String reasonNotRunning;

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

        public Builder phase(Integer phase) {
            this.phase = phase;
            return this;
        }

        public Builder phaseName(String phaseName) {
            this.phaseName = phaseName;
            return this;
        }

        public Builder reasonNotRunning(String reasonNotRunning) {
            this.reasonNotRunning = reasonNotRunning;
            return this;
        }

        public Job build() {
            return new Job(this);
        }

    }

}