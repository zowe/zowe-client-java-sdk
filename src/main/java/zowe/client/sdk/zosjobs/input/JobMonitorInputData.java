/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 *
 */
package zowe.client.sdk.zosjobs.input;

import zowe.client.sdk.zosjobs.types.JobStatus;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * Parameters for MonitorJobs "waitFor..." API input data
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobMonitorInputData {

    /**
     * The z/OS JOBID for the job to monitor. No pre-validation of the JOBID (other than its presence) is performed.
     * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
     * JOBID specification adheres to the z/OS standards.
     */
    private final String jobId;

    /**
     * The z/OS JOBNAME for the job to monitor. No pre-validation of the JOBNAME (other than its presence) is performed.
     * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
     * JOBNAME specification adheres to the z/OS standards.
     */
    private final String jobName;

    /**
     * Watch delay is the polling delay in milliseconds. MonitorJobs will poll every "watchDelay" milliseconds for the
     * status of the job that is being monitored. Use in conjunction with "attempts" to specify your maximum wait
     * for the expected status.
     * Default: MonitorJobs.DEFAULT_WATCHER_DELAY
     */
    private Integer watchDelay;

    /**
     * The job status (see z/OSMF Jobs REST APIs documentation - and the JOB_STATUS type for possible options) to
     * wait for. Note that if the job's status is "further" along in the logical progression (see the JOB_STATUS
     * documentation for full details) the "waitFor..." API methods will return immediately with the current status.
     * Default: MonitorJobs.DEFAULT_STATUS.
     */
    private JobStatus.Type jobStatus;

    /**
     * Maximum number of poll attempts. Use in conjunction with "watchDelay" to specify your maximum wait
     * for the expected status.
     * Default: MonitorJobs.DEFAULT_ATTEMPTS.
     */
    private Integer attempts;

    /**
     * Number of lines to inspect from job output.
     * Default: MonitorJobs.DEFAULT_LINE_LIMIT.
     */
    private Integer lineLimit;

    /**
     * MonitorJobWaitForParams constructor
     *
     * @param builder MonitorJobWaitForParams.Builder object
     * @author Frank Giordano
     */
    private JobMonitorInputData(final JobMonitorInputData.Builder builder) {
        this.jobId = builder.jobId;
        this.jobName = builder.jobName;
        this.watchDelay = builder.watchDelay;
        this.jobStatus = builder.jobStatus;
        this.attempts = builder.attempts;
        this.lineLimit = builder.lineLimit;
    }

    /**
     * Retrieve attempts
     *
     * @return attempts value
     */
    public OptionalInt getAttempts() {
        return (attempts == null) ? OptionalInt.empty() : OptionalInt.of(attempts);
    }

    /**
     * Assign attempts
     *
     * @param attempts number of attempts to get status
     */
    public void setAttempts(final int attempts) {
        this.attempts = attempts;
    }

    /**
     * Retrieve job id
     *
     * @return jobId value
     */
    public Optional<String> getJobId() {
        return Optional.ofNullable(jobId);
    }

    /**
     * Retrieve job name
     *
     * @return jobName value
     */
    public Optional<String> getJobName() {
        return Optional.ofNullable(jobName);
    }

    /**
     * Retrieve job status
     *
     * @return jobStatus value
     */
    public Optional<JobStatus.Type> getJobStatus() {
        return Optional.ofNullable(jobStatus);
    }

    /**
     * Assign job status
     *
     * @param jobStatus job status type, see JobStatus.Type object
     */
    public void setJobStatus(final JobStatus.Type jobStatus) {
        this.jobStatus = jobStatus;
    }

    /**
     * Retrieve line limit
     *
     * @return lineLimit value
     */
    public OptionalInt getLineLimit() {
        return (lineLimit == null) ? OptionalInt.empty() : OptionalInt.of(lineLimit);
    }

    /**
     * Assign line limit
     *
     * @param lineLimit number of lines to inspect
     */
    public void setLineLimit(final int lineLimit) {
        this.lineLimit = lineLimit;
    }

    /**
     * Retrieve watch delay
     *
     * @return watchDelay value
     */
    public OptionalInt getWatchDelay() {
        return (watchDelay == null) ? OptionalInt.empty() : OptionalInt.of(watchDelay);
    }

    /**
     * Assign watch delay
     *
     * @param watchDelay delay of polling operation in milliseconds
     */
    public void setWatchDelay(final int watchDelay) {
        this.watchDelay = watchDelay;
    }

    /**
     * Return string value representing MonitorJobWaitForParams object
     *
     * @return string representation of MonitorJobWaitForParams
     */
    @Override
    public String toString() {
        return "MonitorJobWaitForParams{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", watchDelay=" + watchDelay +
                ", jobStatus=" + jobStatus +
                ", attempts=" + attempts +
                ", lineLimit=" + lineLimit +
                '}';
    }

    /**
     * Builder class for MonitorJobWaitForParams
     */
    public static class Builder {

        /**
         * The z/OS JOBID for the job to monitor. No pre-validation of the JOBID (other than its presence) is performed.
         * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
         * JOBID specification adheres to the z/OS standards.
         */
        private final String jobId;

        /**
         * The z/OS JOBNAME for the job to monitor. No pre-validation of the JOBNAME (other than its presence) is performed.
         * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
         * JOBNAME specification adheres to the z/OS standards.
         */
        private final String jobName;

        /**
         * Watch delay is the polling delay in milliseconds. MonitorJobs will poll every "watchDelay" milliseconds for the
         * status of the job that is being monitored. Use in conjunction with "attempts" to specify your maximum wait
         * for the expected status.
         * Default: MonitorJobs.DEFAULT_WATCHER_DELAY
         */
        private Integer watchDelay;

        /**
         * The job status (see z/OSMF Jobs REST APIs documentation - and the JOB_STATUS type for possible options) to
         * wait for. Note that if the job's status is "further" along in the logical progression (see the JOB_STATUS
         * documentation for full details) the "waitFor..." API methods will return immediately with the current status.
         * Default: MonitorJobs.DEFAULT_STATUS.
         */
        private JobStatus.Type jobStatus;

        /**
         * Maximum number of poll attempts. Use in conjunction with "watchDelay" to specify your maximum wait
         * for the expected status.
         * Default: MonitorJobs.DEFAULT_ATTEMPTS.
         */
        private Integer attempts;

        /**
         * Number of lines to inspect from job output.
         * Default: MonitorJobs.DEFAULT_LINE_LIMIT.
         */
        private Integer lineLimit;

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
         * Set attempts int value
         *
         * @param attempts number of attempts to get status
         * @return Builder object
         */
        public JobMonitorInputData.Builder attempts(final int attempts) {
            this.attempts = attempts;
            return this;
        }

        /**
         * Set a job status type
         *
         * @param jobStatus job status type, see JobStatus.Type object
         * @return Builder object
         */
        public JobMonitorInputData.Builder jobStatus(final JobStatus.Type jobStatus) {
            this.jobStatus = jobStatus;
            return this;
        }

        /**
         * Set line limit int value
         *
         * @param lineLimit number of lines to inspect
         * @return Builder object
         */
        public JobMonitorInputData.Builder lineLimit(final int lineLimit) {
            this.lineLimit = lineLimit;
            return this;
        }

        /**
         * Set watch delay int value
         *
         * @param watchDelay delay of polling operation in milliseconds
         * @return Builder object
         */
        public JobMonitorInputData.Builder watchDelay(final int watchDelay) {
            this.watchDelay = watchDelay;
            return this;
        }

        /**
         * Return MonitorJobWaitForParams object based on Builder this object
         *
         * @return MonitorJobWaitForParams object
         */
        public JobMonitorInputData build() {
            return new JobMonitorInputData(this);
        }

    }

}
