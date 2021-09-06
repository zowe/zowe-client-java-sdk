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

import zosjobs.JobsConstants;

import java.util.Optional;

/**
 * Interface for various GetJobs APIs
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class GetJobParms {

    /**
     * Owner for which to obtain jobs for.
     */
    private final Optional<String> owner;

    /**
     * Prefix to filter when obtaining jobs.
     * Default: *
     */
    private final Optional<String> prefix;

    /**
     * Max jobs to return in a list
     * Default: JobsConstants.DEFAULT_MAX_JOBS
     */
    private final Optional<Integer> maxJobs;

    /**
     * job id for a job
     */
    private final Optional<String> jobId;

    private GetJobParms(Builder builder) {
        this.owner = Optional.ofNullable(builder.owner);
        this.prefix = Optional.ofNullable(builder.prefix);
        this.maxJobs = Optional.ofNullable(builder.maxJobs);
        this.jobId = Optional.ofNullable(builder.jobId);
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
     * Retrieve prefix specified
     *
     * @return prefix value
     * @author Frank Giordano
     */
    public Optional<String> getPrefix() {
        return prefix;
    }

    /**
     * Retrieve maxJobs specified
     *
     * @return maxJobs value
     * @author Frank Giordano
     */
    public Optional<Integer> getMaxJobs() {
        return maxJobs;
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

    @Override
    public String toString() {
        return "GetJobParms{" +
                "owner=" + owner +
                ", prefix=" + prefix +
                ", maxJobs=" + maxJobs +
                ", jobId=" + jobId +
                '}';
    }

    public static class Builder {

        private String owner;
        private String prefix = "*";
        private Integer maxJobs = JobsConstants.DEFAULT_MAX_JOBS;
        private String jobId;

        public Builder(String owner) {
            this.owner = owner;
        }

        public Builder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public Builder maxJobs(Integer maxJobs) {
            this.maxJobs = maxJobs;
            return this;
        }

        public Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public GetJobParms build() {
            return new GetJobParms(this);
        }

    }

}
