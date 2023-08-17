/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.input;

import java.util.Optional;
import java.util.OptionalLong;

/**
 * Represents the name and details of an output (spool) DD for a z/OS batch job
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class JobFile {

    /**
     * Job id for a job. Uniquely identifies a job on a z/OS system
     */
    private final Optional<String> jobId;

    /**
     * Job Name for a job
     */
    private final Optional<String> jobName;

    /**
     * Record format of the spool file (DD)
     */
    private final Optional<String> recfm;

    /**
     * Total bytes in the spool file
     */
    private final OptionalLong byteCount;

    /**
     * Total records (roughly equivalent to lines) in the spool file
     */
    private final OptionalLong recordCount;

    /**
     * Unique identifier of job (substitute of job name and job id)
     */
    private final Optional<String> jobCorrelator;

    /**
     * Job class for which job ran
     */
    private final Optional<String> classs;

    /**
     * Identifier for this spool file. Each JobFile for a single batch job will have a unique ID
     */
    private final OptionalLong id;

    /**
     * DD name of job spool file
     */
    private final Optional<String> ddName;

    /**
     * Direct access to job record content
     */
    private final Optional<String> recordsUrl;

    /**
     * Job DD lrecl (logical record length - how many bytes each record is)
     */
    private final OptionalLong lrecl;

    /**
     * The primary or secondary JES subsystem. If this value is null, the job was processed by the primary subsystem.
     */
    private final Optional<String> subSystem;

    /**
     * The name of the job step during which this spool file was produced
     */
    private final Optional<String> stepName;

    /**
     * If this spool file was produced during a job procedure step, the name of the step will be here.
     */
    private final Optional<String> procStep;

    private JobFile(Builder builder) {
        this.jobId = Optional.ofNullable(builder.jobId);
        this.jobName = Optional.ofNullable(builder.jobName);
        this.recfm = Optional.ofNullable(builder.recfm);
        if (builder.byteCount == null) {
            this.byteCount = OptionalLong.empty();
        } else {
            this.byteCount = OptionalLong.of(builder.byteCount);
        }
        if (builder.recordCount == null) {
            this.recordCount = OptionalLong.empty();
        } else {
            this.recordCount = OptionalLong.of(builder.recordCount);
        }
        this.jobCorrelator = Optional.ofNullable(builder.jobCorrelator);
        this.classs = Optional.ofNullable(builder.classs);
        if (builder.id == null) {
            this.id = OptionalLong.empty();
        } else {
            this.id = OptionalLong.of(builder.id);
        }
        this.ddName = Optional.ofNullable(builder.ddName);
        this.recordsUrl = Optional.ofNullable(builder.recordsUrl);
        if (builder.lrecl == null) {
            this.lrecl = OptionalLong.empty();
        } else {
            this.lrecl = OptionalLong.of(builder.lrecl);
        }
        this.subSystem = Optional.ofNullable(builder.subSystem);
        this.stepName = Optional.ofNullable(builder.stepName);
        this.procStep = Optional.ofNullable(builder.procStep);
    }

    /**
     * Retrieve byteCount specified
     *
     * @return byteCount value
     * @author Frank Giordano
     */
    public OptionalLong getByteCount() {
        return byteCount;
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
     * Retrieve ddName specified
     *
     * @return ddName value
     * @author Frank Giordano
     */
    public Optional<String> getDdName() {
        return ddName;
    }

    /**
     * Retrieve id specified
     *
     * @return id value
     * @author Frank Giordano
     */
    public OptionalLong getId() {
        return id;
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
     * Retrieve lrecl specified
     *
     * @return lrecl value
     * @author Frank Giordano
     */
    public OptionalLong getLrecl() {
        return lrecl;
    }

    /**
     * Retrieve procStep specified
     *
     * @return procStep value
     * @author Frank Giordano
     */
    public Optional<String> getProcStep() {
        return procStep;
    }

    /**
     * Retrieve recfm specified
     *
     * @return recfm value
     * @author Frank Giordano
     */
    public Optional<String> getRecfm() {
        return recfm;
    }

    /**
     * Retrieve recordCount specified
     *
     * @return recordCount value
     * @author Frank Giordano
     */
    public OptionalLong getRecordCount() {
        return recordCount;
    }

    /**
     * Retrieve recordsUrl specified
     *
     * @return recordsUrl value
     * @author Frank Giordano
     */
    public Optional<String> getRecordsUrl() {
        return recordsUrl;
    }

    /**
     * Retrieve stepName specified
     *
     * @return stepName value
     * @author Frank Giordano
     */
    public Optional<String> getStepName() {
        return stepName;
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

        public JobFile build() {
            return new JobFile(this);
        }

        public Builder byteCount(Long byteCount) {
            this.byteCount = byteCount;
            return this;
        }

        public Builder classs(String classs) {
            this.classs = classs;
            return this;
        }

        public Builder ddName(String ddName) {
            this.ddName = ddName;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
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

        public Builder lrecl(Long lrecl) {
            this.lrecl = lrecl;
            return this;
        }

        public Builder procStep(String procStep) {
            this.procStep = procStep;
            return this;
        }

        public Builder recfm(String recfm) {
            this.recfm = recfm;
            return this;
        }

        public Builder recordCount(Long recordCount) {
            this.recordCount = recordCount;
            return this;
        }

        public Builder recordsUrl(String recordsUrl) {
            this.recordsUrl = recordsUrl;
            return this;
        }

        public Builder stepName(String stepName) {
            this.stepName = stepName;
            return this;
        }

        public Builder subSystem(String subSystem) {
            this.subSystem = subSystem;
            return this;
        }

    }

}
