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
package zosjobs.input;

import zosjobs.types.JobStatus;

import java.util.Optional;

/**
 * MonitorJobs "waitFor..." APIs parameters interface
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class MonitorJobWaitForParms {

    /**
     * The z/OS JOBID for the job to monitor. No pre-validation of the JOBID (other than its presence) is performed.
     * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
     * JOBID specification adheres to the z/OS standards.
     */
    private Optional<String> jobId;

    /**
     * The z/OS JOBNAME for the job to monitor. No pre-validation of the JOBNAME (other than its presence) is performed.
     * Any errors that you receive regarding invalid JOBID/JOBNAME will be surfaced by z/OSMF. Ensure that your
     * JOBNAME specification adheres to the z/OS standards.
     */
    private Optional<String> jobName;

    /**
     * Watch delay is the polling delay in milliseconds. MonitorJobs will poll every "watchDelay" milliseconds for the
     * status of the job that is being monitored. Use in conjunction with "attempts" to specify your maximum wait
     * for the expected status.
     * Default: MonitorJobs.DEFAULT_WATCHER_DELAY
     */
    private Optional<Integer> watchDelay;

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
    private Optional<Integer> attempts;

    /**
     * Number of lines to inspect from job output.
     * Default: MonitorJobs.DEFAULT_LINE_LIMIT.
     */
    private Optional<Integer> lineLimit;

    public MonitorJobWaitForParms(MonitorJobWaitForParms.Builder builder) {
        this.jobId = Optional.ofNullable(builder.jobId);
        this.jobName = Optional.ofNullable(builder.jobName);
        this.watchDelay = Optional.ofNullable(builder.watchDelay);
        this.jobStatus = Optional.ofNullable(builder.jobStatus);
        this.attempts = Optional.ofNullable(builder.attempts);
        this.lineLimit = Optional.ofNullable(builder.lineLimit);
    }

    /**
     * Retrieve job id
     *
     * @return jobId value
     * @author Frank Giordano
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve job name
     *
     * @return jobName value
     * @author Frank Giordano
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve job status
     *
     * @return jobStatus value
     * @author Frank Giordano
     */
    public Optional<JobStatus.Type> getJobStatus() {
        return jobStatus;
    }

    /**
     * Retrieve watch delay
     *
     * @return watchDelay value
     * @author Frank Giordano
     */
    public Optional<Integer> getWatchDelay() {
        return watchDelay;
    }

    /**
     * Retrieve attempts
     *
     * @return attempts value
     * @author Frank Giordano
     */
    public Optional<Integer> getAttempts() {
        return attempts;
    }

    /**
     * Retrieve line limit
     *
     * @return lineLimit value
     * @author Frank Giordano
     */
    public Optional<Integer> getLineLimit() {
        return lineLimit;
    }

    /**
     * Assign watch delay
     *
     * @param watchDelay delay of polling operation in milliseconds
     * @author Frank Giordano
     */
    public void setWatchDelay(Optional<Integer> watchDelay) {
        this.watchDelay = watchDelay;
    }

    /**
     * Assign job status
     *
     * @param jobStatus job status type, see JobStatus.Type object
     * @author Frank Giordano
     */
    public void setJobStatus(Optional<JobStatus.Type> jobStatus) {
        this.jobStatus = jobStatus;
    }

    /**
     * Assign attempts
     *
     * @param attempts number of attempts to get status
     * @author Frank Giordano
     */
    public void setAttempts(Optional<Integer> attempts) {
        this.attempts = attempts;
    }

    /**
     * Assign line limit
     *
     * @param lineLimit number of lines to inspect
     * @author Frank Giordano
     */
    public void setLineLimit(Optional<Integer> lineLimit) {
        this.lineLimit = lineLimit;
    }

    @Override
    public String toString() {
        return "MonitorJobWaitForParms{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", watchDelay=" + watchDelay +
                ", jobStatus=" + jobStatus +
                ", attempts=" + attempts +
                ", lineLimit=" + lineLimit +
                '}';
    }

    public static class Builder {

        private String jobId;
        private String jobName;
        private int watchDelay;
        private JobStatus.Type jobStatus;
        private int attempts;
        private int lineLimit;

        public MonitorJobWaitForParms.Builder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public MonitorJobWaitForParms.Builder jobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        public MonitorJobWaitForParms.Builder watchDelay(int watchDelay) {
            this.watchDelay = watchDelay;
            return this;
        }

        public MonitorJobWaitForParms.Builder jobStatus(JobStatus.Type jobStatus) {
            this.jobStatus = jobStatus;
            return this;
        }

        public MonitorJobWaitForParms.Builder attempts(int attempts) {
            this.attempts = attempts;
            return this;
        }

        public MonitorJobWaitForParms.Builder lineLimit(int lineLimit) {
            this.lineLimit = lineLimit;
            return this;
        }

        public MonitorJobWaitForParms build() {
            return new MonitorJobWaitForParms(this);
        }

    }

}
