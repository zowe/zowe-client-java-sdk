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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.Util;
import zosjobs.input.MonitorJobWaitForParms;
import zosjobs.response.Job;
import zosjobs.types.JobStatus;

import java.util.Optional;

/**
 * APIs for monitoring the status of a job. Use these APIs to wait for a job to enter the specified status. All APIs
 * in monitor jobs invoke z/OSMF jobs REST endpoints to obtain job status information.
 *
 * @author Frank Giordano
 * @verion 1.0.0
 */
public class MonitorJobs {

    private static final Logger LOG = LogManager.getLogger(MonitorJobs.class);

    private final ZOSConnection connection;
    private int attempts = DEFAULT_ATTEMPTS;
    private int wakeDelay = DEFAULT_WATCH_DELAY;

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
    public static int DEFAULT_ATTEMPTS = 100000;

    /**
     * MonitorJobs Constructor.
     *
     * @param connection connection object, see ZOSConnection object
     * @author Frank Giordano
     */
    public MonitorJobs(ZOSConnection connection) {
        this.connection = connection;
    }

    public MonitorJobs(ZOSConnection connection, int attempts) {
        this.connection = connection;
        this.attempts = attempts;
    }

    public MonitorJobs(ZOSConnection connection, int attempts, int wakeDelay) {
        this.connection = connection;
        this.attempts = attempts;
        this.wakeDelay = wakeDelay;
    }

    /**
     * Given an IJob (has jobname/jobid), waits for the status of the job to be "OUTPUT". This API will poll for
     * the OUTPUT status once every 3 seconds indefinitely. If the polling interval/duration is NOT sufficient, use
     * "waitForStatusCommon" to adjust.
     * <p>
     * See JSDoc for "waitForStatusCommon" for full details on polling and other logic.
     *
     * @param job document of the z/OS job to wait for (see z/OSMF Jobs APIs for details)
     * @returns job document
     * @author Frank Giordano
     */
    public Job waitForJobOutputStatus(Job job) throws Exception {
        return waitForStatusCommon(
                new MonitorJobWaitForParms(job.getJobName(), job.getJobId(), JobStatus.Type.OUTPUT,
                        Optional.ofNullable(attempts), Optional.ofNullable(wakeDelay)));
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
     * @returns job document
     * @author Frank Giordano
     */
    public Job waitForOutputStatus(String jobName, String jobId) throws Exception {
        return waitForStatusCommon(new MonitorJobWaitForParms(Optional.ofNullable(jobName), Optional.ofNullable(jobId),
                JobStatus.Type.OUTPUT, Optional.ofNullable(attempts), Optional.ofNullable(wakeDelay)));
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
     * @returns job document
     * @author Frank Giordano
     */
    public Job waitForStatusCommon(MonitorJobWaitForParms parms) throws Exception {
        Util.checkStateParameter(parms.getJobName().isEmpty(), "job name not specified");
        Util.checkStateParameter(parms.getJobId().isEmpty(), "job id not specified");
        Util.checkNullParameter(parms == null, "parms is null");

        if (parms.getJobStatus().isEmpty())
            parms.setJobStatus(Optional.of(DEFAULT_STATUS));

        if (parms.getAttempts().isEmpty())
            parms.setAttempts(Optional.of(attempts));

        Job job = pollForStatus(parms);

        return job;
    }

    /**
     * "Polls" (sets timeouts and continuously checks) for the status of the job to match the desired status.
     *
     * @param parms monitor jobs parms, see MonitorJobWaitForParms
     * @returns job document
     * @author Frank Giordano
     */
    private Job pollForStatus(MonitorJobWaitForParms parms) throws Exception {
        int timeoutVal = parms.getWatchDelay().get();
        boolean expectedStatus;  // default is false;
        boolean shouldContinue;
        int numOfAttempts = 0;
        int maxAttempts = parms.getAttempts().get();

        do {
            numOfAttempts++;

            expectedStatus = checkStatus(parms);
            shouldContinue = !expectedStatus && (maxAttempts > 0 && numOfAttempts < maxAttempts);

            if (shouldContinue)
                Thread.sleep(timeoutVal);
        } while (shouldContinue);

        return null;
    }

    /**
     * Checks the status of the job for the expected status (OR that the job has progressed passed the expected status).
     *
     * @param parms monitor jobs parms, see MonitorJobWaitForParms
     * @returns boolean true when the job status is obtained (or imperative error)
     * @author Frank Giordano
     */
    private boolean checkStatus(MonitorJobWaitForParms parms) throws Exception {
        GetJobs getJobs = new GetJobs(connection);
        String statusNameCheck = parms.getJobStatus().get().toString();

        Job job = getJobs.getStatus(parms.getJobName().get(), parms.getJobId().get());
        if (statusNameCheck.equals(job.getStatus().get()))
            return true;

        // return exception if any return -1
        int orderNumOfStatusCheck = getOrderIndexOfStatus(statusNameCheck);
        int orderNumOfCurrRunningJob = getOrderIndexOfStatus(job.getStatus().get());
        if (orderNumOfCurrRunningJob > orderNumOfStatusCheck)
            return true;

        return false;
    }

    private int getOrderIndexOfStatus(String statusName) {
        for (int i = 0; i < JobStatus.Order.length; i++)
            if (statusName.equals(JobStatus.Order[i])) {
                return i;
            }
        return -1;
    }

}
