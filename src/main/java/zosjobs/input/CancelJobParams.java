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

    private Optional<String> jobname;
    private Optional<String> jobId;
    private Optional<String> version;

    public CancelJobParams(CancelJobParams.Builder builder) {
        this.jobname = Optional.ofNullable(builder.jobname);
        this.jobId = Optional.ofNullable(builder.jobId);
        this.version = Optional.ofNullable(builder.version);
    }

    public Optional<String> getJobname() {
        return jobname;
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
                ", jobname=" + jobname +
                ", version=" + version +
                '}';
    }

    public static class Builder {

        private String jobname;
        private String jobId;
        private String version;

        public CancelJobParams.Builder jobname(String jobname) {
            this.jobname = jobname;
            return this;
        }

        public CancelJobParams.Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
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
