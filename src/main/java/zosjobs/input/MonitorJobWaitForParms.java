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
 * @@version 1.0
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
     * MonitorJobWaitForParms constructor.
     *
     * @param jobName    job name
     * @param jobId      job id
     * @param jobStatus  job status type, see JobStatus.Type object
     * @param attempts   number of attempts to get status
     * @param watchDelay delay time in milliseconds to wait each time requesting status
     * @author Frank Giordano
     */
    public MonitorJobWaitForParms(Optional<String> jobName, Optional<String> jobId, JobStatus.Type jobStatus,
                                  Optional<Integer> attempts, Optional<Integer> watchDelay) {
        this.jobName = jobName;
        this.jobId = jobId;
        this.jobStatus = Optional.ofNullable(jobStatus);
        this.attempts = attempts;
        this.watchDelay = watchDelay;
    }

    /**
     * Retrieve job id
     *
     * @author Frank Giordano
     */
    public Optional<String> getJobId() {
        return jobId;
    }

    /**
     * Retrieve job name
     *
     * @author Frank Giordano
     */
    public Optional<String> getJobName() {
        return jobName;
    }

    /**
     * Retrieve watch delay
     *
     * @author Frank Giordano
     */
    public Optional<Integer> getWatchDelay() {
        return watchDelay;
    }

    /**
     * Retrieve job status
     *
     * @author Frank Giordano
     */
    public Optional<JobStatus.Type> getJobStatus() {
        return jobStatus;
    }

    /**
     * Retrieve attempts
     *
     * @author Frank Giordano
     */
    public Optional<Integer> getAttempts() {
        return attempts;
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

    @Override
    public String toString() {
        return "MonitorJobWaitForParms{" +
                "jobId=" + jobId +
                ", jobName=" + jobName +
                ", watchDelay=" + watchDelay +
                ", jobStatus=" + jobStatus +
                ", attempts=" + attempts +
                '}';
    }

}
