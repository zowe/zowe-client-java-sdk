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

import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.types.JobStatus;

import java.util.Optional;
import java.util.OptionalInt;

/**
 * MonitorJobs "waitFor..." APIs parameters interface
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class MonitorJobWaitForParams {

    /**
     * The z/OS JOBID for the job to monitor. No pre-validation of the JOBID (other than its presence) is performed.
     * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
     * JOBID specification adheres to the z/OS standards.
     */
    private final Optional<String> jobId;

    /**
     * The z/OS JOBNAME for the job to monitor. No pre-validation of the JOBNAME (other than its presence) is performed.
     * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
     * JOBNAME specification adheres to the z/OS standards.
     */
    private final Optional<String> jobName;

    /**
     * Watch delay is the polling delay in milliseconds. MonitorJobs will poll every "watchDelay" milliseconds for the
     * status of the job that is being monitored. Use in conjunction with "attempts" to specify your maximum wait
     * for the expected status.
     * Default: MonitorJobs.DEFAULT_WATCHER_DELAY
     */
    private OptionalInt watchDelay;

    /**
     * The job status (see z/OSMF Jobs REST APIs documentation - and the JOB_STATUS type for possible options) to
     * wait for. Note that if the job's status is "further" along in the logical progression (see the JOB_STATUS
     * documentation for full details) the "waitFor..." API methods will return immediately with the current status.
     * Default: MonitorJobs.DEFAULT_STATUS.
     */
    private Optional<JobStatus.Type> jobStatus;

    /**
     * Maximum number of poll attempts. Use in conjunction with "watchDelay" to specify your maximum wait
     * for the expected status.
     * Default: MonitorJobs.DEFAULT_ATTEMPTS.
     */
    private OptionalInt attempts;

    /**
     * Number of lines to inspect from job output.
     * Default: MonitorJobs.DEFAULT_LINE_LIMIT.
     */
    private OptionalInt lineLimit;

    /**
     * MonitorJobWaitForParams constructor
     *
     * @param builder MonitorJobWaitForParams.Builder object
     * @author Frank Giordano
     */
    private MonitorJobWaitForParams(final MonitorJobWaitForParams.Builder builder) {
        this.jobId = Optional.ofNullable(builder.jobId);
        this.jobName = Optional.ofNullable(builder.jobName);
        this.watchDelay = builder.watchDelay;
        this.jobStatus = Optional.ofNullable(builder.jobStatus);
        this.attempts = builder.attempts;
        this.lineLimit = builder.lineLimit;
    }

    /**
     * Retrieve attempts
     *
     * @return attempts value
     */
    public OptionalInt getAttempts() {
        return attempts;
    }

    /**
     * Assign attempts
     *
     * @param attempts number of attempts to get status
     */
    public void setAttempts(final int attempts) {
        this.attempts = OptionalInt.of(attempts);
    }

    /**
     * Retrieve job id
     *
     * @return jobId value
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve job name
     *
     * @return jobName value
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve job status
     *
     * @return jobStatus value
     */
    public Optional<JobStatus.Type> getJobStatus() {
        return jobStatus;
    }

    /**
     * Assign job status
     *
     * @param jobStatus job status type, see JobStatus.Type object
     */
    public void setJobStatus(final JobStatus.Type jobStatus) {
        this.jobStatus = Optional.ofNullable(jobStatus);
    }

    /**
     * Retrieve line limit
     *
     * @return lineLimit value
     */
    public OptionalInt getLineLimit() {
        return lineLimit;
    }

    /**
     * Assign line limit
     *
     * @param lineLimit number of lines to inspect
     */
    public void setLineLimit(final int lineLimit) {
        this.lineLimit = OptionalInt.of(lineLimit);
    }

    /**
     * Retrieve watch delay
     *
     * @return watchDelay value
     */
    public OptionalInt getWatchDelay() {
        return watchDelay;
    }

    /**
     * Assign watch delay
     *
     * @param watchDelay delay of polling operation in milliseconds
     */
    public void setWatchDelay(final int watchDelay) {
        this.watchDelay = OptionalInt.of(watchDelay);
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

        private final String jobId;
        private final String jobName;
        private OptionalInt watchDelay = OptionalInt.empty();
        private JobStatus.Type jobStatus;
        private OptionalInt attempts = OptionalInt.empty();
        private OptionalInt lineLimit = OptionalInt.empty();

        public Builder(final String jobName, final String jobId) {
            ValidateUtils.checkNullParameter(jobName == null, "job name is null");
            ValidateUtils.checkIllegalParameter(jobName.isBlank(), JobsConstants.JOB_NAME_ERROR_MSG);
            ValidateUtils.checkNullParameter(jobId == null, "job id is null");
            ValidateUtils.checkIllegalParameter(jobId.isBlank(), JobsConstants.JOB_NAME_ERROR_MSG);
            this.jobName = jobName;
            this.jobId = jobId;
        }

        public MonitorJobWaitForParams.Builder attempts(final int attempts) {
            this.attempts = OptionalInt.of(attempts);
            return this;
        }

        public MonitorJobWaitForParams.Builder jobStatus(final JobStatus.Type jobStatus) {
            this.jobStatus = jobStatus;
            return this;
        }

        public MonitorJobWaitForParams.Builder lineLimit(final int lineLimit) {
            this.lineLimit = OptionalInt.of(lineLimit);
            return this;
        }

        public MonitorJobWaitForParams.Builder watchDelay(final int watchDelay) {
            this.watchDelay = OptionalInt.of(watchDelay);
            return this;
        }

        public MonitorJobWaitForParams build() {
            return new MonitorJobWaitForParams(this);
        }

    }

}
