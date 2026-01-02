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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * JobFeedback class represents the JSON response returned from job change
 * and job modification operations in z/OSMF.
 *
 * <p>This class uses constructor-based Jackson deserialization and maps
 * hyphenated JSON attribute names to Java-friendly field names.</p>
 *
 * @author Frank Giordano
 * @version 6.0
 */
public final class JobFeedback {

    /**
     * Job id
     */
    private final String jobId;

    /**
     * Job name
     */
    private final String jobName;

    /**
     * Original job ID.
     * <p>
     * If the job was processed on another system, this value represents the
     * original job identifier that was assigned when the job was submitted on the host system.
     * If the target system cannot assign the original job identifier, the target system assigns
     * a new ID to the job, which is indicated as "jobid" in this document.
     */
    private final String originalJobId;

    /**
     * z/OS user ID associated with the job.
     */
    private final String owner;

    /**
     * JES2 multi-access spool (MAS) member name.
     */
    private final String member;

    /**
     * z/OS system name.
     */
    private final String sysname;

    /**
     * Job correlator.
     * <p>
     * If this value is null, the job was submitted to JES3.
     */
    private final String jobCorrelator;

    /**
     * Job processing status.
     * <p>
     * If set to zero (0), the request was processed successfully.
     * Otherwise, there was an error. See the message property for a description of the error.
     */
    private final String status;

    /**
     * If job processing status indicates an error (a value other than 0),
     * this property contains the internal service routine return code.
     * Otherwise, this property is omitted.
     */
    private final String internalCode;

    /**
     * If job processing status indicates an error (a value other than 0),
     * this property contains a description of the error. Otherwise, this property is omitted.
     */
    private final String message;

    /* Null-handling helpers */

    private static String orEmpty(String value) {
        return value == null ? "" : value;
    }

    /**
     * JobFeedback constructor used by Jackson for JSON deserialization.
     *
     * @param jobId           job id
     * @param jobName         job name
     * @param originalJobId   original job id
     * @param owner           job owner
     * @param member          JES member name
     * @param sysname         system name
     * @param jobCorrelator   job correlator value
     * @param status          job status
     * @author Frank Giordano
     */
    @JsonCreator
    public JobFeedback(
            @JsonProperty("jobid") final String jobId,
            @JsonProperty("jobname") final String jobName,
            @JsonProperty("original-jobid") final String originalJobId,
            @JsonProperty("owner") final String owner,
            @JsonProperty("member") final String member,
            @JsonProperty("sysname") final String sysname,
            @JsonProperty("job-correlator") final String jobCorrelator,
            @JsonProperty("status") final String status,
            @JsonProperty("internal-code") final String internalCode,
            @JsonProperty("message") final String message) {
        this.jobId = orEmpty(jobId);
        this.jobName = orEmpty(jobName);
        this.originalJobId = orEmpty(originalJobId);
        this.owner = orEmpty(owner);
        this.member = orEmpty(member);
        this.sysname = orEmpty(sysname);
        this.jobCorrelator = orEmpty(jobCorrelator);
        this.status = orEmpty(status);
        this.internalCode = orEmpty(internalCode);
        this.message = orEmpty(message);
    }

    /**
     * Get job id.
     *
     * @return job id
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * Get job name.
     *
     * @return job name
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Get original job id.
     *
     * @return original job id
     */
    public String getOriginalJobId() {
        return originalJobId;
    }

    /**
     * Get job owner.
     *
     * @return job owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Get JES member name.
     *
     * @return JES member
     */
    public String getMember() {
        return member;
    }

    /**
     * Get system name.
     *
     * @return system name
     */
    public String getSysname() {
        return sysname;
    }

    /**
     * Get job correlator.
     *
     * @return job correlator
     */
    public String getJobCorrelator() {
        return jobCorrelator;
    }

    /**
     * Get job status.
     *
     * @return job status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Get internal code.
     *
     * @return internal code
     */
    public String getInternalCode() {
        return internalCode;
    }

    /**
     * Get message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

}
