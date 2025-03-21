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
package zowe.client.sdk.zosjobs.methods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.timer.WaitUtil;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.CommonJobParams;
import zowe.client.sdk.zosjobs.input.GetJobParams;
import zowe.client.sdk.zosjobs.input.JobFile;
import zowe.client.sdk.zosjobs.input.MonitorJobWaitForParams;
import zowe.client.sdk.zosjobs.response.CheckJobStatus;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.types.JobStatus;

import java.util.List;

/**
 * APIs for monitoring the status of a job. Use these APIs to wait for a job to enter the specified status. All APIs
 * in MonitorJobs invoke z/OSMF jobs REST endpoints to obtain job status information.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class JobMonitor {

    private static final Logger LOG = LoggerFactory.getLogger(JobMonitor.class);

    /**
     * Default number of poll attempts to check for the specified job status.
     */
    public static final int DEFAULT_ATTEMPTS = 1000;
    /**
     * The default amount of lines to check from job output.
     */
    public static final int DEFAULT_LINE_LIMIT = 1000;
    /**
     * Default expected job status ("OUTPUT")
     */
    public static final JobStatus.Type DEFAULT_STATUS = JobStatus.Type.OUTPUT;
    /**
     * The default amount of time (in 3000 milliseconds is 3 seconds) to wait until the next job status poll.
     */
    public static final int DEFAULT_WATCH_DELAY = 3000;
    private final ZosConnection connection;
    // double settings from DEFAULTS variables to allow constructor to control them also
    private int attempts = DEFAULT_ATTEMPTS;
    private int watchDelay = DEFAULT_WATCH_DELAY;
    private int lineLimit = DEFAULT_LINE_LIMIT;

    /**
     * MonitorJobs constructor.
     *
     * @param connection connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public JobMonitor(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * MonitorJobs constructor.
     *
     * @param connection connection information, see ZosConnection object
     * @param attempts   number of attempts to get status
     * @author Frank Giordano
     */
    public JobMonitor(final ZosConnection connection, final int attempts) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.attempts = attempts;
    }

    /**
     * MonitorJobs constructor.
     *
     * @param connection connection information, see ZosConnection object
     * @param attempts   number of attempts to get status
     * @param watchDelay delay time in milliseconds to wait each time requesting status
     * @author Frank Giordano
     */
    public JobMonitor(final ZosConnection connection, final int attempts, final int watchDelay) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.attempts = attempts;
        this.watchDelay = watchDelay;
    }

    /**
     * MonitorJobs constructor.
     *
     * @param connection connection information, see ZosConnection object
     * @param attempts   number of attempts to get status
     * @param watchDelay delay time in milliseconds to wait each time requesting status
     * @param lineLimit  number of line to inspect job output
     * @author Frank Giordano
     */
    public JobMonitor(final ZosConnection connection, final int attempts, final int watchDelay, final int lineLimit) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.attempts = attempts;
        this.watchDelay = watchDelay;
        this.lineLimit = lineLimit;
    }

    /**
     * Check if the given message is within the job output line limit.
     *
     * @param params  monitor jobs params, see MonitorJobWaitForParams
     * @param message message string
     * @return boolean message found status
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private boolean checkMessage(final MonitorJobWaitForParams params, final String message) throws ZosmfRequestException {
        final JobGet getJobs = new JobGet(connection);
        final GetJobParams filter =
                new GetJobParams.Builder("*")
                        .jobId(params.getJobId().orElse(""))
                        .prefix(params.getJobName().orElse(""))
                        .build();
        final List<Job> jobs = getJobs.getCommon(filter);
        if (jobs.isEmpty()) {
            throw new IllegalStateException("job does not exist");
        }
        final List<JobFile> files = getJobs.getSpoolFilesByJob(jobs.get(0));
        final String[] output = getJobs.getSpoolContent(files.get(0)).split("\n");

        final int lineLimit = params.getLineLimit().orElse(DEFAULT_LINE_LIMIT);
        final int size = output.length, start;

        if (size < lineLimit) {
            start = 0;
        } else {
            start = size - lineLimit;
        }

        for (int i = start; i < size; i++) {
            LOG.debug(output[i]);
            if (output[i].contains(message)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check the status of the job for the expected status or that the job has progressed and passed the expected status.
     *
     * @param params monitor jobs params, see MonitorJobWaitForParams
     * @return boolean true when the job status is obtained
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private CheckJobStatus checkStatus(final MonitorJobWaitForParams params) throws ZosmfRequestException {
        return checkStatus(params, false);
    }

    /**
     * Check the status of the job for the expected status or that the job has progressed and passed the expected status.
     *
     * @param params monitor jobs params, see MonitorJobWaitForParams
     * @return boolean true when the job status is obtained
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private CheckJobStatus checkStatus(final MonitorJobWaitForParams params, final boolean isStepData)
            throws ZosmfRequestException {
        final JobGet getJobs = new JobGet(connection);
        final String statusNameCheck = params.getJobStatus().orElse(DEFAULT_STATUS).toString();

        final Job job = getJobs.getStatusCommon(new CommonJobParams(
                params.getJobId().orElse(""), params.getJobName().orElse(""), isStepData));

        if (statusNameCheck.equals(job.getStatus().orElse(DEFAULT_STATUS.toString()))) {
            return new CheckJobStatus(true, job);
        }

        final String invalidStatusMsg = "Invalid status when checking for status ordering.";
        final int orderIndexOfDesiredJobStatus = getOrderIndexOfStatus(statusNameCheck);
        if (orderIndexOfDesiredJobStatus == -1) { // this should never happen but let's check for it.
            throw new IllegalStateException(invalidStatusMsg);
        }

        final int orderIndexOfCurrRunningJobStatus = getOrderIndexOfStatus(
                job.getStatus().orElseThrow(() -> new IllegalStateException("job status not specified")));
        if (orderIndexOfCurrRunningJobStatus == -1) {  // this should never happen but let's check for it.
            throw new IllegalStateException(invalidStatusMsg);
        }

        if (orderIndexOfCurrRunningJobStatus > orderIndexOfDesiredJobStatus) {
            return new CheckJobStatus(true, job);
        }

        return new CheckJobStatus(false, job);
    }

    /**
     * Check the status order of the given status name.
     *
     * @param statusName status name
     * @return int index of status order or -1 if none found
     * @author Frank Giordano
     */
    private int getOrderIndexOfStatus(final String statusName) {
        for (int i = 0; i < JobStatus.Order.length; i++) {
            if (statusName.equals(JobStatus.Order[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Determines if a given job is in a running state or not.
     *
     * @param params monitor jobs params, see MonitorJobWaitForParams
     * @return true if in running state
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public boolean isRunning(final MonitorJobWaitForParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        final JobGet getJobs = new JobGet(connection);
        final String jobName = params.getJobName().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ERROR_MSG));
        final String jobId = params.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ERROR_MSG));
        final String status = getJobs.getStatusValue(jobName, jobId);
        return !JobStatus.Type.INPUT.toString().equals(status) && !JobStatus.Type.OUTPUT.toString().equals(status);
    }

    /**
     * "Polls" (sets timeouts and continuously checks) for the given message within the job output.
     *
     * @param params  monitor jobs params, see MonitorJobWaitForParams
     * @param message message string
     * @return boolean message found status
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private boolean pollByMessage(final MonitorJobWaitForParams params, final String message)
            throws ZosmfRequestException {
        final int timeoutVal = params.getWatchDelay().orElse(DEFAULT_WATCH_DELAY);
        boolean messageFound;  // no assigment boolean means by default it is false
        boolean shouldContinue;
        int numOfAttempts = 0;
        final int maxAttempts = params.getAttempts().orElse(DEFAULT_ATTEMPTS);

        LOG.info("Waiting for message \"{}\"", message);

        do {
            numOfAttempts++;
            messageFound = checkMessage(params, message);
            shouldContinue = !messageFound && (maxAttempts > 0 && numOfAttempts < maxAttempts);

            if (shouldContinue) {
                WaitUtil.wait(timeoutVal);
                if (!isRunning(params)) {
                    return false;
                }
                LOG.info("Waiting for message \"{}\"", message);
            }
        } while (shouldContinue);

        return numOfAttempts != maxAttempts;
    }

    /**
     * "Polls" (sets timeouts and continuously checks) for the status of the job to match the desired status.
     *
     * @param params monitor jobs params, see MonitorJobWaitForParams
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    private Job pollByStatus(final MonitorJobWaitForParams params) throws ZosmfRequestException {
        final int timeoutVal = params.getWatchDelay().orElse(DEFAULT_WATCH_DELAY);
        boolean expectedStatus;  // no assigment boolean means by default it is false
        boolean shouldContinue;
        int numOfAttempts = 0;
        final int maxAttempts = params.getAttempts().orElse(DEFAULT_ATTEMPTS);

        String statusName = params.getJobStatus().orElse(DEFAULT_STATUS).toString();
        LOG.info("Waiting for status \"{}\"", statusName);

        CheckJobStatus checkJobStatus;
        do {
            numOfAttempts++;
            checkJobStatus = checkStatus(params);
            expectedStatus = checkJobStatus.isStatusFound();
            shouldContinue = !expectedStatus && (maxAttempts > 0 && numOfAttempts < maxAttempts);

            if (shouldContinue) {
                WaitUtil.wait(timeoutVal);
                LOG.info("Waiting for status \"{}\"", statusName);
            } else {
                // Get the stepData
                try {
                    checkJobStatus = checkStatus(params, true);
                } catch (Exception ignore) {
                    // JCL error, return without stepData
                }
            }
        } while (shouldContinue);

        if (numOfAttempts == maxAttempts) {
            throw new IllegalStateException("Desired status not seen. The number of maximum attempts reached.");
        }

        return checkJobStatus.getJob();
    }

    /**
     * Given a Job document (has jobname/jobid), wait for the given message from the job. This API will poll for
     * the given message once every 3 seconds for at least 1000 times. If the polling interval/duration is NOT
     * sufficient, use "waitForMessageCommon" method to adjust.
     * <p>
     * See JavaDoc for "waitForMessageCommon" for full details on polling and other logic.
     *
     * @param job     document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
     * @param message message string
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public boolean waitByMessage(final Job job, final String message) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return waitMessageCommon(
                new MonitorJobWaitForParams.Builder(job.getJobName().orElse(""), job.getJobId().orElse(""))
                        .jobStatus(JobStatus.Type.OUTPUT)
                        .attempts(attempts)
                        .watchDelay(watchDelay)
                        .build(), message);
    }

    /**
     * Given the jobname/jobid, wait for the given message from the job. This API will poll for
     * the given message once every 3 seconds for at least 1000 times. If the polling interval/duration is NOT
     * sufficient, use "waitForMessageCommon" method to adjust.
     * <p>
     * See JavaDoc for "waitForMessageCommon" for full details on polling and other logic.
     *
     * @param jobName the z/OS jobname of the job to wait for output status (see z/OSMF Jobs APIs for details)
     * @param jobId   the z/OS jobid of the job to wait for output status (see z/OSMF Jobs APIS for details)
     * @param message message string
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public boolean waitByMessage(final String jobName, final String jobId, final String message)
            throws ZosmfRequestException {
        return waitMessageCommon(
                new MonitorJobWaitForParams.Builder(jobName, jobId)
                        .jobStatus(JobStatus.Type.OUTPUT)
                        .attempts(attempts)
                        .watchDelay(watchDelay)
                        .build(), message);
    }

    /**
     * Given a Job document (has jobname/jobid), wait for the status of the job to be "OUTPUT". This API will poll for
     * the OUTPUT status once every 3 seconds for at least 1000 times. If the polling interval/duration is NOT
     * sufficient, use "waitForStatusCommon" to adjust.
     * <p>
     * See JSDoc for "waitForStatusCommon" for full details on polling and other logic.
     *
     * @param job document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job waitByOutputStatus(final Job job) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return waitStatusCommon(
                new MonitorJobWaitForParams.Builder(job.getJobName().orElse(""), job.getJobId().orElse(""))
                        .jobStatus(JobStatus.Type.OUTPUT)
                        .attempts(attempts)
                        .watchDelay(watchDelay)
                        .build());
    }

    /**
     * Given the jobname/jobid, wait for the status of the job to be "OUTPUT". This API will poll for the OUTPUT status
     * once every 3 seconds for at least 1000 times. If the polling interval/duration is NOT sufficient, use
     * "waitForStatusCommon" to adjust.
     * <p>
     * See JavaDoc for "waitForStatusCommon" for full details on polling and other logic.
     *
     * @param jobName the z/OS jobname of the job to wait for output status (see z/OSMF Jobs APIs for details)
     * @param jobId   the z/OS jobid of the job to wait for output status (see z/OSMF Jobs APIS for details)
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job waitByOutputStatus(final String jobName, final String jobId) throws ZosmfRequestException {
        return waitStatusCommon(
                new MonitorJobWaitForParams.Builder(jobName, jobId)
                        .jobStatus(JobStatus.Type.OUTPUT)
                        .attempts(attempts)
                        .watchDelay(watchDelay)
                        .build());
    }

    /**
     * Given a Job document (has jobname/jobid), wait for the given status of the job. This API will poll for
     * the given status once every 3 seconds for at least 1000 times. If the polling interval/duration is NOT
     * sufficient, use "waitForStatusCommon" method to adjust.
     * <p>
     * See JavaDoc for "waitForStatusCommon" for full details on polling and other logic.
     *
     * @param job        document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
     * @param statusType status type, see JobStatus.Type object
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job waitByStatus(final Job job, final JobStatus.Type statusType) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return waitStatusCommon(
                new MonitorJobWaitForParams.Builder(job.getJobName().orElse(""), job.getJobId().orElse(""))
                        .jobStatus(statusType)
                        .attempts(attempts)
                        .watchDelay(watchDelay)
                        .build());
    }

    /**
     * Given the jobname/jobid, wait for the given status of the job. This API will poll for the given status once
     * every 3 seconds for at least 1000 times. If the polling interval/duration is NOT sufficient, use
     * "waitForStatusCommon" method to adjust.
     * <p>
     * See JavaDoc for "waitForStatusCommon" for full details on polling and other logic.
     *
     * @param jobName    the z/OS jobname of the job to wait for output status (see z/OSMF Jobs APIs for details)
     * @param jobId      the z/OS jobid of the job to wait for output status (see z/OSMF Jobs APIS for details)
     * @param statusType status type, see JobStatus.Type object
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job waitByStatus(final String jobName, final String jobId, final JobStatus.Type statusType)
            throws ZosmfRequestException {
        return waitStatusCommon(
                new MonitorJobWaitForParams.Builder(jobName, jobId)
                        .jobStatus(statusType)
                        .attempts(attempts)
                        .watchDelay(watchDelay)
                        .build());
    }

    /**
     * Given jobname/jobid, check for the desired message continuously (based on the interval and attempts specified).
     *
     * @param params  monitor jobs parameters, see MonitorJobWaitForParams object
     * @param message message string
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public boolean waitMessageCommon(final MonitorJobWaitForParams params, final String message)
            throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJobName().isEmpty(), JobsConstants.JOB_NAME_ERROR_MSG);
        ValidateUtils.checkIllegalParameter(params.getJobId().isEmpty(), JobsConstants.JOB_ID_ERROR_MSG);

        if (params.getAttempts().isEmpty()) {
            params.setAttempts(attempts);
        }
        if (params.getWatchDelay().isEmpty()) {
            params.setWatchDelay(watchDelay);
        }
        if (params.getLineLimit().isEmpty()) {
            params.setLineLimit(lineLimit);
        }

        return pollByMessage(params, message);
    }

    /**
     * Given jobname/jobid, check for the desired "status" (default is "OUTPUT") continuously (based on the interval
     * and attempts specified).
     * <p>
     * The "order" of natural job status is: INPUT ACTIVE OUTPUT. If the requested status is earlier in the sequence
     * than the current status of the job, then the method returns immediately (since the job will never enter the
     * requested status) with the current status of the job.
     *
     * @param params monitor jobs parameters, see MonitorJobWaitForParams object
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public Job waitStatusCommon(final MonitorJobWaitForParams params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJobName().isEmpty(), JobsConstants.JOB_NAME_ERROR_MSG);
        ValidateUtils.checkIllegalParameter(params.getJobId().isEmpty(), JobsConstants.JOB_ID_ERROR_MSG);

        if (params.getJobStatus().isEmpty()) {
            params.setJobStatus(DEFAULT_STATUS);
        }
        if (params.getAttempts().isEmpty()) {
            params.setAttempts(attempts);
        }
        if (params.getWatchDelay().isEmpty()) {
            params.setWatchDelay(watchDelay);
        }

        return pollByStatus(params);
    }

}