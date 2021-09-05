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
 * CancelJobParams APIs parameters interface for delete job operation
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class CancelJobParams {

    private final Optional<String> jobName;
    private final Optional<String> jobId;
    private final Optional<String> version;

    private CancelJobParams(CancelJobParams.Builder builder) {
        this.jobName = Optional.ofNullable(builder.jobName);
        this.jobId = Optional.ofNullable(builder.jobId);
        this.version = Optional.ofNullable(builder.version);
    }

    public Optional<String> getJobName() {
        return jobName;
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "CancelJobParams{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", version=" + version +
                '}';
    }

    public static class Builder {

        private String jobName;
        private String jobId;
        private String version;

        public Builder(String jobName, String jobId) {
            this.jobName = jobName;
            this.jobId = jobId;
        }

        public CancelJobParams.Builder version(String version) {
            this.version = version;
            return this;
        }

        public CancelJobParams build() {
            return new CancelJobParams(this);
        }

    }

}
