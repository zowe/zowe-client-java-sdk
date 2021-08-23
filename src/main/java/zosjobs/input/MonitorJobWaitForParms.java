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
 * @verion 1.0.0
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

    public MonitorJobWaitForParms(Optional<String> jobName, Optional<String> jobId, JobStatus.Type jobStatus,
                                  Optional<Integer> attempts, Optional<Integer> watchDelay) {
        this.jobName = jobName;
        this.jobId = jobId;
        this.jobStatus = Optional.ofNullable(jobStatus);
        this.attempts = attempts;
        this.watchDelay = watchDelay;
    }

    public Optional<String> getJobId() {
        return jobId;
    }

    public Optional<String> getJobName() {
        return jobName;
    }

    public Optional<Integer> getWatchDelay() {
        return watchDelay;
    }

    public Optional<JobStatus.Type> getJobStatus() {
        return jobStatus;
    }

    public Optional<Integer> getAttempts() {
        return attempts;
    }

    public void setJobStatus(Optional<JobStatus.Type> jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setAttempts(Optional<Integer> attempts) {
        this.attempts = attempts;
    }

}
