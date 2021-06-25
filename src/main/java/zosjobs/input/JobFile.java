/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs.input;

import java.util.Optional;

/**
 * Represents the name and details of an output (spool) DD
 * for a z/OS batch job
 */
public class JobFile {

    /**
     * job id for a job
     * Uniquely identifies a job on a z/OS system
     *
     * @type {string}
     * @memberof Job
     */
    private Optional<String> jobId;

    /**
     * job name for a job
     *
     * @type {string}
     * @memberof Job
     */
    private Optional<String> jobName;

    /**
     * Record format of the spool file (DD)
     *
     * @type {string}
     * @memberof JobFile
     */
    private Optional<String> recfm;

    /**
     * Total bytes in the spool file
     *
     * @type {number}
     * @memberof JobFile
     */
    private Optional<Long> byteCount;

    /**
     * Total records (roughly equivalent to lines) in the spool file
     *
     * @type {number}
     * @memberof JobFile
     */
    private Optional<Long> recordCount;

    /**
     * unique identifier of job (substitute of job name and job id)
     *
     * @type {string}
     * @memberof Job
     */
    private Optional<String> jobCorrelator;

    /**
     * Job class for which job ran
     *
     * @type {string}
     * @memberof JobFile
     */
    private Optional<String> classs;

    /**
     * Identifier for this spool file.
     * each IJobFile for a single batch job will have a unique ID
     *
     * @type {number}
     * @memberof JobFileSimple
     */
    private Optional<Long> id;

    /**
     * DD name of job spool file
     *
     * @type {string}
     * @memberof JobFileSimple
     */
    private Optional<String> ddName;

    /**
     * Direct access to job record content
     *
     * @type {string}
     * @memberof JobFile
     */
    private Optional<String> recordsUrl;

    /**
     * Job DD lrecl (logical record length - how many bytes each record is)
     *
     * @type {number}
     * @memberof JobFile
     */
    private Optional<Long> lrecl;

    /**
     * The primary or secondary JES subsystem.
     * If this value is null, the job was processed by the primary subsystem.
     *
     * @type {string}
     * @memberof JobFile
     */
    private Optional<String> subSystem;

    /**
     * The name of the job step during which this spool file was produced
     *
     * @type {string}
     * @memberof JobFileSimple
     */
    private Optional<String> stepName;

    /**
     * If this spool file was produced during a job procedure step, the
     * name of the step will be here.
     *
     * @type {string}
     * @memberof JobStepData
     */
    private Optional<String> procStep;

    public JobFile(Builder builder) {
        if (builder.jobId != null)
            this.jobId = Optional.ofNullable(builder.jobId);
        else this.jobId = Optional.empty();

        if (builder.jobName != null)
            this.jobName = Optional.ofNullable(builder.jobName);
        else this.jobName = Optional.empty();

        if (builder.recfm != null)
            this.recfm = Optional.ofNullable(builder.recfm);
        else this.recfm = Optional.empty();

        if (builder.byteCount != null)
            this.byteCount = Optional.ofNullable(builder.byteCount);
        else this.byteCount = Optional.empty();

        if (builder.recordCount != null)
            this.recordCount = Optional.ofNullable(builder.recordCount);
        else this.recordCount = Optional.empty();

        if (builder.jobCorrelator != null)
            this.jobCorrelator = Optional.ofNullable(builder.jobCorrelator);
        else this.jobCorrelator = Optional.empty();

        if (builder.classs != null)
            this.classs = Optional.ofNullable(builder.classs);
        else this.classs = Optional.empty();

        if (builder.id != null)
            this.id = Optional.ofNullable(builder.id);
        else this.id = Optional.empty();

        if (builder.ddName != null)
            this.ddName = Optional.ofNullable(builder.ddName);
        else this.ddName = Optional.empty();

        if (builder.recordsUrl != null)
            this.recordsUrl = Optional.ofNullable(builder.recordsUrl);
        else this.recordsUrl = Optional.empty();

        if (builder.lrecl != null)
            this.lrecl = Optional.ofNullable(builder.lrecl);
        else this.lrecl = Optional.empty();

        if (builder.subSystem != null)
            this.subSystem = Optional.ofNullable(builder.subSystem);
        else this.subSystem = Optional.empty();

        if (builder.stepName != null)
            this.stepName = Optional.ofNullable(builder.stepName);
        else this.stepName = Optional.empty();

        if (builder.procStep != null)
            this.procStep = Optional.ofNullable(builder.procStep);
        else this.procStep = Optional.empty();
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getJobName() {
        return jobName;
    }

    public Optional<String> getRecfm() {
        return recfm;
    }

    public Optional<Long> getByteCount() {
        return byteCount;
    }

    public Optional<Long> getRecordCount() {
        return recordCount;
    }

    public Optional<String> getJobCorrelator() {
        return jobCorrelator;
    }

    public Optional<String> getClasss() {
        return classs;
    }

    public Optional<Long> getId() {
        return id;
    }

    public Optional<String> getDdName() {
        return ddName;
    }

    public Optional<String> getRecordsUrl() {
        return recordsUrl;
    }

    public Optional<Long> getLrecl() {
        return lrecl;
    }

    public Optional<String> getSubSystem() {
        return subSystem;
    }

    public Optional<String> getStepName() {
        return stepName;
    }

    public Optional<String> getProcStep() {
        return procStep;
    }

    @Override
    public String toString() {
        return "JobFile{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", recfm=" + recfm +
                ", byteCount=" + byteCount +
                ", recordCount=" + recordCount +
                ", jobCorrelator=" + jobCorrelator +
                ", classs=" + classs +
                ", id=" + id +
                ", ddName=" + ddName +
                ", recordsUrl=" + recordsUrl +
                ", lrecl=" + lrecl +
                ", subSystem=" + subSystem +
                ", stepName=" + stepName +
                ", procStep=" + procStep +
                '}';
    }

    public static class Builder {

        private String jobId;
        private String jobName;
        private String recfm;
        private Long byteCount;
        private Long recordCount;
        private String jobCorrelator;
        private String classs;
        private Long id;
        private String ddName;
        private String recordsUrl;
        private Long lrecl;
        private String subSystem;
        private String stepName;
        private String procStep;

        public Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public Builder recfm(String recfm) {
            this.recfm = recfm;
            return this;
        }

        public Builder byteCount(Long byteCount) {
            this.byteCount = byteCount;
            return this;
        }

        public Builder recordCount(Long recordCount) {
            this.recordCount = recordCount;
            return this;
        }

        public Builder jobCorrelator(String jobCorrelator) {
            this.jobCorrelator = jobCorrelator;
            return this;
        }

        public Builder classs(String classs) {
            this.classs = classs;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder ddName(String ddName) {
            this.ddName = ddName;
            return this;
        }

        public Builder recordsUrl(String recordsUrl) {
            this.recordsUrl = recordsUrl;
            return this;
        }

        public Builder lrecl(Long lrecl) {
            this.lrecl = lrecl;
            return this;
        }

        public Builder subSystem(String subSystem) {
            this.subSystem = subSystem;
            return this;
        }

        public Builder stepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

        public Builder procStep(String procStep) {
            this.procStep = procStep;
            return this;
        }

        public JobFile build() {
            return new JobFile(this);
        }

    }

}
