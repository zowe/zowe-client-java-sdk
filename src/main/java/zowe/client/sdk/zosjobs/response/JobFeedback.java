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

    private final String jobId;
    private final String jobName;
    private final String originalJobId;
    private final String owner;
    private final String member;
    private final String sysname;
    private final String jobCorrelator;
    private final String status;

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
            @JsonProperty("status") final String status) {
        this.jobId = jobId;
        this.jobName = jobName;
        this.originalJobId = originalJobId;
        this.owner = owner;
        this.member = member;
        this.sysname = sysname;
        this.jobCorrelator = jobCorrelator;
        this.status = status;
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

}
