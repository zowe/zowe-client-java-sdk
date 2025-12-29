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
 * Represents the name and details of an output (spool) DD for a z/OS batch job
 *
 * @author Frank Giordano
 * @version 6.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobFile {

    /**
     * Job id for a job. Uniquely identifies a job on a z/OS system
     */
    private final String jobId;

    /**
     * Job Name for a job
     */
    private final String jobName;

    /**
     * Record format of the spool file (DD)
     */
    private final String recfm;

    /**
     * Total bytes in the spool file
     */
    private final Long byteCount;

    /**
     * Total records (roughly equivalent to lines) in the spool file
     */
    private final Long recordCount;

    /**
     * Unique identifier of a job (substitute of job name and job id)
     */
    private final String jobCorrelator;

    /**
     * Job class for which a job ran
     */
    private final String classs;

    /**
     * Identifier for this spool file. Each JobFile for a single batch job will have a unique ID
     */
    private final Long id;

    /**
     * DD name of a job spool file
     */
    private final String ddName;

    /**
     * Direct access to job record content
     */
    private final String recordsUrl;

    /**
     * Job DD lrecl (logical record length - how many bytes each record is)
     */
    private final Long lrecl;

    /**
     * The primary or secondary JES subsystem.
     * If this value is null, the job was processed by the primary subsystem.
     */
    private final String subSystem;

    /**
     * The name of the job step during which this spool file was produced
     */
    private final String stepName;

    /**
     * If this spool file was produced during a job procedure step,
     * the name of the step will be here.
     */
    private final String procStep;

    /**
     * Convenience constructor for creating a JobFile with only jobName, jobId, and id.
     * All other fields will be set to default values.
     *
     * @param jobName Name of the job
     * @param jobId   Job ID
     * @param spoolId Unique identifier for this spool file
     * @author Frank Giordano
     */
    public JobFile(final String jobName, final String jobId, final Long spoolId) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.id = spoolId;
        this.recfm = null;
        this.byteCount = null;
        this.recordCount = null;
        this.jobCorrelator = null;
        this.classs = null;
        this.ddName = null;
        this.recordsUrl = null;
        this.lrecl = null;
        this.subSystem = null;
        this.stepName = null;
        this.procStep = null;
    }

    /**
     * JobFile constructor for Jackson JSON parsing.
     *
     * @param jobId         Job ID
     * @param jobName       Job name
     * @param recfm         Record format
     * @param byteCount     Total bytes in a spool file
     * @param recordCount   Total records in a spool file
     * @param jobCorrelator Unique job identifier
     * @param classs        Job class
     * @param id            Spool file ID
     * @param ddName        DD name
     * @param recordsUrl    Records URL
     * @param lrecl         Logical record length
     * @param subSystem     JES subsystem
     * @param stepName      Job step name
     * @param procStep      Procedure step name
     * @author Frank Giordano
     */
    @JsonCreator
    public JobFile(
            @JsonProperty("jobid") String jobId,
            @JsonProperty("jobname") String jobName,
            @JsonProperty("recfm") String recfm,
            @JsonProperty("byte-count") Long byteCount,
            @JsonProperty("record-count") Long recordCount,
            @JsonProperty("job-correlator") String jobCorrelator,
            @JsonProperty("class") String classs,
            @JsonProperty("id") Long id,
            @JsonProperty("ddname") String ddName,
            @JsonProperty("records-url") String recordsUrl,
            @JsonProperty("lrecl") Long lrecl,
            @JsonProperty("subsystem") String subSystem,
            @JsonProperty("stepname") String stepName,
            @JsonProperty("procstep") String procStep
    ) {
        this.jobId = jobId != null ? jobId : "";
        this.jobName = jobName != null ? jobName : "";
        this.recfm = recfm != null ? recfm : "";
        this.byteCount = byteCount != null ? byteCount : 0;
        this.recordCount = recordCount != null ? recordCount : 0;
        this.jobCorrelator = jobCorrelator != null ? jobCorrelator : "";
        this.classs = classs != null ? classs : "";
        this.id = id != null ? id : 0;
        this.ddName = ddName != null ? ddName : "";
        this.recordsUrl = recordsUrl != null ? recordsUrl : "";
        this.lrecl = lrecl != null ? lrecl : 0;
        this.subSystem = subSystem != null ? subSystem : "";
        this.stepName = stepName != null ? stepName : "";
        this.procStep = procStep != null ? procStep : "";
    }

    /**
     * Retrieve byteCount specified
     *
     * @return byteCount value
     */
    public Long getByteCount() {
        return byteCount;
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
     * Retrieve ddName specified
     *
     * @return ddName value
     */
    public String getDdName() {
        return ddName;
    }

    /**
     * Retrieve id specified
     *
     * @return id value
     */
    public Long getId() {
        return id;
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
     * Retrieve lrecl specified
     *
     * @return lrecl value
     */
    public Long getLrecl() {
        return lrecl;
    }

    /**
     * Retrieve procStep specified
     *
     * @return procStep value
     */
    public String getProcStep() {
        return procStep;
    }

    /**
     * Retrieve recfm specified
     *
     * @return recfm value
     */
    public String getRecfm() {
        return recfm;
    }

    /**
     * Retrieve recordCount specified
     *
     * @return recordCount value
     */
    public Long getRecordCount() {
        return recordCount;
    }

    /**
     * Retrieve recordsUrl specified
     *
     * @return recordsUrl value
     */
    public String getRecordsUrl() {
        return recordsUrl;
    }

    /**
     * Retrieve stepName specified
     *
     * @return stepName value
     */
    public String getStepName() {
        return stepName;
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
     * Return string value representing JobFile object
     *
     * @return string representation of JobFile
     */
    @Override
    public String toString() {
        return "JobFile{" +
                "jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", recfm='" + recfm + '\'' +
                ", byteCount=" + byteCount +
                ", recordCount=" + recordCount +
                ", jobCorrelator='" + jobCorrelator + '\'' +
                ", classs='" + classs + '\'' +
                ", id=" + id +
                ", ddName='" + ddName + '\'' +
                ", recordsUrl='" + recordsUrl + '\'' +
                ", lrecl=" + lrecl +
                ", subSystem='" + subSystem + '\'' +
                ", stepName='" + stepName + '\'' +
                ", procStep='" + procStep + '\'' +
                '}';
    }

}
