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

public class GetJobParms {

    private Optional<String> owner;
    private Optional<String> prefix;
    private Optional<Integer> maxJobs;
    private Optional<String> jobId;

    public GetJobParms(Builder builder) {
        this.owner = Optional.ofNullable(builder.owner);
        this.prefix = Optional.ofNullable(builder.prefix);
        this.maxJobs = Optional.ofNullable(builder.maxJobs);
        this.jobId = Optional.ofNullable(builder.jobId);
    }

    public Optional<String> getOwner() {
        return owner;
    }

    public Optional<String> getPrefix() {
        return prefix;
    }

    public Optional<Integer> getMaxJobs() {
        return maxJobs;
    }

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
        private String prefix;
        private Integer maxJobs;
        private String jobId;

        public Builder owner(String owner) {
            this.owner = owner;
            return this;
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
