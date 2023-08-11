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

import java.util.Optional;

/**
 * ModifyJobParams APIs parameters interface for delete and cancel job operations
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ModifyJobParams {

    /**
     * Job name value specified for request
     */
    private final Optional<String> jobName;

    /**
     * Job id value specified for request
     */
    private final Optional<String> jobId;

    /**
     * Version value specified for request.
     * <p>
     * To request asynchronous processing for this service (the default), set the "version" property to 1.0
     * or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
     * the system will attempt to process the request synchronously, if such processing is supported on
     * the target JES2 subsystem.
     */
    private final Optional<String> version;

    private ModifyJobParams(ModifyJobParams.Builder builder) {
        this.jobName = Optional.ofNullable(builder.jobName);
        this.jobId = Optional.ofNullable(builder.jobId);
        this.version = Optional.ofNullable(builder.version);
    }

    /**
     * Retrieve jobId value
     *
     * @return jobId value
     * @author Frank Giordano
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve jobName value
     *
     * @return jobName value
     * @author Frank Giordano
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve version value
     *
     * @return version value
     * @author Frank Giordano
     */
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

        private final String jobName;
        private final String jobId;
        private String version;

        public Builder(String jobName, String jobId) {
            ValidateUtils.checkNullParameter(jobName == null, "job name is null");
            ValidateUtils.checkIllegalParameter(jobName.isBlank(), "job name not specified");
            ValidateUtils.checkNullParameter(jobId == null, "job id is null");
            ValidateUtils.checkIllegalParameter(jobId.isBlank(), "job id not specified");
            this.jobName = jobName;
            this.jobId = jobId;
        }

        public ModifyJobParams build() {
            return new ModifyJobParams(this);
        }

        public ModifyJobParams.Builder version(String version) {
            this.version = version;
            return this;
        }

    }

}
