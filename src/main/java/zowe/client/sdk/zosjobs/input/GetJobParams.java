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

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Interface for various GetJobs APIs
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class GetJobParams {

    /**
     * Owner for which to get jobs for.
     */
    private final Optional<String> owner;

    /**
     * Prefix to filter when getting jobs.
     * Default: *
     */
    private final Optional<String> prefix;

    /**
     * Max jobs to return in a list
     * Default: JobsConstants.DEFAULT_MAX_JOBS
     */
    private final OptionalInt maxJobs;

    /**
     * job id for a job
     */
    private final Optional<String> jobId;

    /**
     * GetJobParams constructor
     *
     * @param builder GetJobParams.Builder object
     * @author Frank Giordano
     */
    private GetJobParams(final Builder builder) {
        this.owner = Optional.ofNullable(builder.owner);
        this.prefix = Optional.ofNullable(builder.prefix);
        if (builder.maxJobs == null) {
            this.maxJobs = OptionalInt.empty();
        } else {
            this.maxJobs = OptionalInt.of(builder.maxJobs);
        }
        this.jobId = Optional.ofNullable(builder.jobId);
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
     * Retrieve maxJobs specified
     *
     * @return maxJobs value
     */
    public OptionalInt getMaxJobs() {
        return maxJobs;
    }

    /**
     * Retrieve owner specified
     *
     * @return owner value
     */
    public Optional<String> getOwner() {
        return owner;
    }

    /**
     * Retrieve prefix specified
     *
     * @return prefix value
     */
    public Optional<String> getPrefix() {
        return prefix;
    }

    /**
     * Return string value representing a GetJobParams object
     *
     * @return string representation of GetJobParams
     */
    @Override
    public String toString() {
        return "GetJobParams{" +
                "owner=" + owner +
                ", prefix=" + prefix +
                ", maxJobs=" + maxJobs +
                ", jobId=" + jobId +
                '}';
    }

    /**
     * Builder class for GetJobParams
     */
    public static class Builder {

        /**
         * Owner for which to obtain jobs for.
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
            ValidateUtils.checkNullParameter(owner == null, "owner is null");
            ValidateUtils.checkIllegalParameter(owner.isBlank(), "owner not specified");
            this.owner = owner;
        }

        /**
         * Set jobId string value
         *
         * @param jobId string value
         * @return Builder this object
         */
        public Builder jobId(final String jobId) {
            ValidateUtils.checkNullParameter(jobId == null, "job id is null");
            ValidateUtils.checkIllegalParameter(jobId.isBlank(), "job id not specified");
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
            ValidateUtils.checkNullParameter(prefix == null, "prefix is null");
            ValidateUtils.checkIllegalParameter(prefix.isBlank(), "prefix not specified");
            this.prefix = prefix;
            return this;
        }

        /**
         * Return GetJobParams object based on Builder this object
         *
         * @return GetJobParams this object
         */
        public GetJobParams build() {
            return new GetJobParams(this);
        }

    }

}
