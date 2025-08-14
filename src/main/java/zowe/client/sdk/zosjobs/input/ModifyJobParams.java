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

/**
 * ModifyJobParams APIs parameters interface for delete and cancel job operations
 *
 * @author Frank Giordano
 * @version 4.0
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
     * Version value specified for the request.
     * <p>
     * To request asynchronous processing for this service (the default), set the "version" property to 1.0
     * or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
     * the system will attempt to process the request synchronously if such processing is supported on
     * the target JES2 subsystem.
     */
    private final Optional<String> version;

    /**
     * Boolean value representing whether to purge the canceled job output
     */
    public boolean purge;

    /**
     * ModifyJobParams constructor
     *
     * @param builder ModifyJobParams.Builder object
     * @author Nikunj Goyal
     */
    private ModifyJobParams(final ModifyJobParams.Builder builder) {
        this.jobName = Optional.ofNullable(builder.jobName);
        this.jobId = Optional.ofNullable(builder.jobId);
        this.version = Optional.ofNullable(builder.version);
        this.purge = builder.purge;
    }

    /**
     * Retrieve jobId value
     *
     * @return jobId value
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve jobName value
     *
     * @return jobName value
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve version value
     *
     * @return version value
     */
    public Optional<String> getVersion() {
        return version;
    }

    /**
     * Retrieve purge value
     *
     * @return boolean value
     */
    public boolean isPurge() {
        return purge;
    }

    /**
     * Return string value representing CancelJobParams object
     *
     * @return string representation of CancelJobParams
     */
    @Override
    public String toString() {
        return "ModifyJobParams{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", version=" + version +
                '}';
    }

    /**
     * Builder class for ModifyJobParams
     */
    public static class Builder {

        /**
         * Job name value specified for request
         */
        private final String jobName;

        /**
         * Job id value specified for request
         */
        private final String jobId;

        /**
         * Version value specified for the request.
         * <p>
         * To request asynchronous processing for this service (the default), set the "version" property to 1.0
         * or omit the property from the request. To request synchronous processing, set "version" to 2.0. If so,
         * the system will attempt to process the request synchronously if such processing is supported on
         * the target JES2 subsystem.
         */
        private String version;

        /**
         * Boolean value representing whether to purge the canceled job output
         */
        private boolean purge = false;

        /**
         * Builder constructor
         *
         * @param jobName job name value
         * @param jobId   job id value
         */
        public Builder(final String jobName, final String jobId) {
            ValidateUtils.checkNullParameter(jobName == null, JobsConstants.JOB_NAME_NULL_MSG);
            ValidateUtils.checkIllegalParameter(jobName.isBlank(), JobsConstants.JOB_NAME_ILLEGAL_MSG);
            ValidateUtils.checkNullParameter(jobId == null, JobsConstants.JOB_ID_NULL_MSG);
            ValidateUtils.checkIllegalParameter(jobId.isBlank(), JobsConstants.JOB_ID_ILLEGAL_MSG);
            this.jobName = jobName;
            this.jobId = jobId;
        }

        /**
         * Set version value
         *
         * @param version version value
         * @return Builder object
         */
        public ModifyJobParams.Builder version(final String version) {
            this.version = version;
            return this;
        }

        /**
         * Set purge value
         *
         * @param purge boolean value
         * @return Builder object
         */
        public ModifyJobParams.Builder purge(boolean purge) {
            this.purge = purge;
            return this;
        }

        /**
         * Return ModifyJobParams object based on Builder this object
         *
         * @return ModifyJobParams this object
         */
        public ModifyJobParams build() {
            return new ModifyJobParams(this);
        }

    }

}
