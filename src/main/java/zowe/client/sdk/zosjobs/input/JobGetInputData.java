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

import zowe.client.sdk.zosjobs.JobsConstants;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameters for JobGet API input data
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobGetInputData {

    /**
     * Owner for which to get jobs for.
     */
    private final String owner;

    /**
     * Prefix to filter when getting jobs.
     * Default: *
     */
    private final String prefix;

    /**
     * Max jobs to return in a list
     * Default: JobsConstants.DEFAULT_MAX_JOBS
     */
    private final Integer maxJobs;

    /**
     * job id for a job
     */
    private final String jobId;

    /**
     * JobGetInputData constructor
     *
     * @param builder JobGetInputData.Builder object
     * @author Frank Giordano
     */
    private JobGetInputData(final JobGetInputData.Builder builder) {
        this.owner = builder.owner;
        this.prefix = builder.prefix;
        this.maxJobs = builder.maxJobs;
        this.jobId = builder.jobId;
    }

    /**
     * Retrieve jobId specified
     *
     * @return jobId value
     */
    public Optional<String> getJobId() {
        return Optional.ofNullable(jobId);
    }

    /**
     * Retrieve maxJobs specified
     *
     * @return maxJobs value
     */
    public OptionalInt getMaxJobs() {
        return (maxJobs == null) ? OptionalInt.empty() : OptionalInt.of(maxJobs);
    }

    /**
     * Retrieve an owner specified
     *
     * @return owner value
     */
    public Optional<String> getOwner() {
        return Optional.ofNullable(owner);
    }

    /**
     * Retrieve prefix specified
     *
     * @return prefix value
     */
    public Optional<String> getPrefix() {
        return Optional.ofNullable(prefix);
    }

    /**
     * Return string value representing a JobGetInputData object
     *
     * @return string representation of JobGetInputData
     */
    @Override
    public String toString() {
        return "JobGetInputData{" +
                "owner=" + owner +
                ", prefix=" + prefix +
                ", maxJobs=" + maxJobs +
                ", jobId=" + jobId +
                '}';
    }

    /**
     * Builder class for JobGetInputData
     */
    public static class Builder {

        /**
         * Owner for which to get jobs for.
         */
        private String owner = "*";

        /**
         * Prefix to filter when getting jobs.
         * Default: *
         */
        private String prefix = "*";

        /**
         * Max jobs to return in a list
         * Default: JobsConstants.DEFAULT_MAX_JOBS
         */
        private Integer maxJobs = JobsConstants.DEFAULT_MAX_JOBS;

        /**
         * job id for a job
         */
        private String jobId;

        /**
         * Default Builder constructor
         */
        public Builder() {
        }

        /**
         * Builder constructor
         *
         * @param owner owner value
         */
        public Builder(final String owner) {
            this.owner = owner;
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
         * Set maxJobs int value
         *
         * @param maxJobs int value
         * @return Builder this object
         */
        public Builder maxJobs(final Integer maxJobs) {
            this.maxJobs = maxJobs;
            return this;
        }

        /**
         * Set prefix string value
         *
         * @param prefix string value
         * @return Builder this object
         */
        public Builder prefix(final String prefix) {
            this.prefix = prefix;
            return this;
        }

        /**
         * Return GetJobParams object based on Builder this object
         *
         * @return GetJobParams this object
         */
        public JobGetInputData build() {
            return new JobGetInputData(this);
        }

    }

}
