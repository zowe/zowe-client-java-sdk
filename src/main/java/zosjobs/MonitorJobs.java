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
package zosjobs;

import core.ZOSConnection;
import utility.Util;
import zosjobs.input.MonitorJobWaitForParms;
import zosjobs.response.CheckJobStatus;
import zosjobs.response.Job;
import zosjobs.types.JobStatus;

import java.util.Optional;

/**
 * APIs for monitoring the status of a job. Use these APIs to wait for a job to enter the specified status. All APIs
 * in monitor jobs invoke z/OSMF jobs REST endpoints to obtain job status information.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class MonitorJobs {

    private final ZOSConnection connection;
    private int attempts = DEFAULT_ATTEMPTS;
    private int watchDelay = DEFAULT_WATCH_DELAY;

    /**
     * The default amount of time (in 3000 milliseconds is 3 seconds) to wait until the next job status poll.
     */
    public static int DEFAULT_WATCH_DELAY = 3000;

    /**
     * Default expected job status ("OUTPUT")
     */
    public static JobStatus.Type DEFAULT_STATUS = JobStatus.Type.OUTPUT;

    /**
     * Default number of poll attempts to check for the specified job status.
     */
    public static int DEFAULT_ATTEMPTS = 1000;

    /**
     * MonitorJobs constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public MonitorJobs(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * MonitorJobs constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @param attempts   number of attempts to get status
     * @author Frank Giordano
     */
    public MonitorJobs(ZOSConnection connection, int attempts) {
        this.connection = connection;
        this.attempts = attempts;
    }

    /**
     * MonitorJobs constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @param attempts   number of attempts to get status
     * @param watchDelay delay time in milliseconds to wait each time requesting status
     * @author Frank Giordano
     */
    public MonitorJobs(ZOSConnection connection, int attempts, int watchDelay) {
        this.connection = connection;
        this.attempts = attempts;
        this.watchDelay = watchDelay;
    }

    /**
     * Given an IJob (has jobname/jobid), waits for the status of the job to be "OUTPUT". This API will poll for
     * the OUTPUT status once every 3 seconds indefinitely. If the polling interval/duration is NOT sufficient, use
     * "waitForStatusCommon" to adjust.
     * <p>
     * See JSDoc for "waitForStatusCommon" for full details on polling and other logic.
     *
     * @param job document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
     * @return job document
     * @throws Exception error processing wait check request
     * @author Frank Giordano
     */
    public Job waitForJobOutputStatus(Job job) throws Exception {
        return waitForStatusCommon(new MonitorJobWaitForParms(job.getJobName(), job.getJobId(), JobStatus.Type.OUTPUT,
                Optional.ofNullable(attempts), Optional.ofNullable(watchDelay)));
    }

    /**
     * Given the jobname/jobid, waits for the status of the job to be "OUTPUT". This API will poll for the OUTPUT status
     * once every 3 seconds indefinitely. If the polling interval/duration is NOT sufficient, use
     * "waitForStatusCommon" to adjust.
     * <p>
     * See JSDoc for "waitForStatusCommon" for full details on polling and other logic.
     *
     * @param jobName the z/OS jobname of the job to wait for output status (see z/OSMF Jobs APIs for details)
     * @param jobId   the z/OS jobid of the job to wait for output status (see z/OSMF Jobs APIS for details)
     * @return job document
     * @throws Exception error processing wait check request
     * @author Frank Giordano
     */
    public Job waitForJobOutputStatus(String jobName, String jobId) throws Exception {
        return waitForStatusCommon(new MonitorJobWaitForParms(Optional.ofNullable(jobName), Optional.ofNullable(jobId),
                JobStatus.Type.OUTPUT, Optional.ofNullable(attempts), Optional.ofNullable(watchDelay)));
    }

    /**
     * Given jobname/jobid, checks for the desired "status" (default is "OUTPUT") continuously (based on the interval
     * and attempts specified).
     * <p>
     * The "order" of natural job status is INPUT > ACTIVE > OUTPUT. If the requested status is earlier in the sequence
     * than the current status of the job, then the method returns immediately (since the job will never enter the
     * requested status) with the current status of the job.
     *
     * @param parms monitor jobs parameters, see MonitorJobWaitForParms object
     * @return job document
     * @throws Exception error processing wait check request
     * @author Frank Giordano
     */
    public Job waitForStatusCommon(MonitorJobWaitForParms parms) throws Exception {
        Util.checkStateParameter(parms.getJobName().isEmpty(), "job name not specified");
        Util.checkStateParameter(parms.getJobName().get().isEmpty(), "job name not specified");
        Util.checkStateParameter(parms.getJobId().isEmpty(), "job id not specified");
        Util.checkStateParameter(parms.getJobId().get().isEmpty(), "job id not specified");
        Util.checkNullParameter(parms == null, "parms is null");

        if (parms.getJobStatus().isEmpty())
            parms.setJobStatus(Optional.of(DEFAULT_STATUS));

        if (parms.getAttempts().isEmpty())
            parms.setAttempts(Optional.of(attempts));

        return pollForStatus(parms);
    }

    /**
     * "Polls" (sets timeouts and continuously checks) for the status of the job to match the desired status.
     *
     * @param parms monitor jobs parms, see MonitorJobWaitForParms
     * @return job document
     * @throws Exception error processing poll check request
     * @author Frank Giordano
     */
    private Job pollForStatus(MonitorJobWaitForParms parms) throws Exception {
        int timeoutVal = parms.getWatchDelay().get();
        boolean expectedStatus;  // no assigment means by default it is false
        boolean shouldContinue; // no assigment means by default it is false
        int numOfAttempts = 0;
        int maxAttempts = parms.getAttempts().get();

        CheckJobStatus checkJobStatus;
        do {
            numOfAttempts++;

            checkJobStatus = checkStatus(parms);
            expectedStatus = checkJobStatus.isStatusFound();

            shouldContinue = !expectedStatus && (maxAttempts > 0 && numOfAttempts < maxAttempts);

            if (shouldContinue) {
                Util.wait(timeoutVal);
            }
        } while (shouldContinue);

        if (numOfAttempts == maxAttempts)
            throw new Exception("Desired status not seen. The number of maximum attempts reached.");

        return checkJobStatus.getJob();
    }

    /**
     * Checks the status of the job for the expected status (OR that the job has progressed passed the expected status).
     *
     * @param parms monitor jobs parms, see MonitorJobWaitForParms
     * @return boolean true when the job status is obtained (or imperative error)
     * @throws Exception error processing check request
     * @author Frank Giordano
     */
    private CheckJobStatus checkStatus(MonitorJobWaitForParms parms) throws Exception {
        GetJobs getJobs = new GetJobs(connection);
        String statusNameCheck = parms.getJobStatus().get().toString();

        Job job = getJobs.getStatus(parms.getJobName().get(), parms.getJobId().get());
        if (statusNameCheck.equals(job.getStatus().get()))
            return new CheckJobStatus(true, job);

        String invalidStatusMsg = "Invalid status when checking for status ordering.";
        int orderIndexOfDesiredJobStatus = getOrderIndexOfStatus(statusNameCheck);
        if (orderIndexOfDesiredJobStatus == -1) // this should never happen but let's check for it.
            throw new Exception(invalidStatusMsg);

        int orderIndexOfCurrRunningJobStatus = getOrderIndexOfStatus(job.getStatus().get());
        if (orderIndexOfCurrRunningJobStatus == -1) // this should never happen but let's check for it.
            throw new Exception(invalidStatusMsg);

        if (orderIndexOfCurrRunningJobStatus > orderIndexOfDesiredJobStatus)
            return new CheckJobStatus(true, job);

        return new CheckJobStatus(false, job);
    }

    private int getOrderIndexOfStatus(String statusName) {
        for (int i = 0; i < JobStatus.Order.length; i++)
            if (statusName.equals(JobStatus.Order[i])) {
                return i;
            }
        return -1;
    }

}
