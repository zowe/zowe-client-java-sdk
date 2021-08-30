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
 * DeleteJobParams APIs parameters interface for delete job operation
 *
 * @author Nikunj Goyal
 * @version 1.0
 */
public class DeleteJobParams {

    private Optional<String> jobName;
    private Optional<String> jobId;
    private Optional<String> modifyVersion;

    public DeleteJobParams(DeleteJobParams.Builder builder) {
        this.jobName = Optional.ofNullable(builder.jobName);
        this.jobId = Optional.ofNullable(builder.jobId);
        this.modifyVersion = Optional.ofNullable(builder.modifyVersion);
    }

    public Optional<String> getJobName() {
        return jobName;
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getModifyVersion() {
        return modifyVersion;
    }

    @Override
    public String toString() {
        return "DeleteJobParms{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", modifyVersion=" + modifyVersion +
                '}';
    }

    public static class Builder {

        private String jobName;
        private String jobId;
        private String modifyVersion;

        public DeleteJobParams.Builder jobName(String jobname) {
            this.jobName = jobname;
            return this;
        }

        public DeleteJobParams.Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public DeleteJobParams.Builder modifyVersion(String modifyVersion) {
            this.modifyVersion = modifyVersion;
            return this;
        }

        public DeleteJobParams build() {
            return new DeleteJobParams(this);
        }

    }

}