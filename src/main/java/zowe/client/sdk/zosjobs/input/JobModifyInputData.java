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

import java.util.Optional;

/**
 * Parameters for delete (JobDelete) and cancel (JobCancel) job operations
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class JobModifyInputData {

    /**
     * Job name value specified for the request
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
    private final String version;

    /**
     * JobModifyInputData constructor
     *
     * @param builder JobModifyInputData.Builder object
     * @author Nikunj Goyal
     */
    private JobModifyInputData(final JobModifyInputData.Builder builder) {
        this.jobName = builder.jobName;
        this.jobId = builder.jobId;
        this.version = builder.version;
    }

    /**
     * Retrieve jobId value
     *
     * @return jobId value
     */
    public Optional<String> getJobId() {
        if (jobId == null || jobId.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(jobId);
    }

    /**
     * Retrieve jobName value
     *
     * @return jobName value
     */
    public Optional<String> getJobName() {
        if (jobName == null || jobName.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(jobName);
    }

    /**
     * Retrieve version value
     *
     * @return version value
     */
    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
    }

    /**
     * Return string value representing JobModifyInputData object
     *
     * @return string representation of JobModifyInputData
     */
    @Override
    public String toString() {
        return "JobModifyInputData{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", version=" + version +
                '}';
    }

    /**
     * Builder class for JobModifyInputData
     */
    public static class Builder {

        /**
         * Job name value specified for the request
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
         * Builder constructor
         *
         * @param jobName job name value
         * @param jobId   job id value
         */
        public Builder(final String jobName, final String jobId) {
            this.jobName = jobName;
            this.jobId = jobId;
        }

        /**
         * Set version value
         *
         * @param version version value
         * @return Builder object
         */
        public JobModifyInputData.Builder version(final String version) {
            this.version = version;
            return this;
        }

        /**
         * Return JobModifyInputData object based on Builder this object
         *
         * @return JobModifyInputData this object
         */
        public JobModifyInputData build() {
            return new JobModifyInputData(this);
        }

    }

}
