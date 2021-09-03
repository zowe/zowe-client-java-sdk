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
package zosjobs.samples;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosjobs.MonitorJobs;
import zosjobs.SubmitJobs;
import zosjobs.response.Job;
import zosjobs.types.JobStatus;

/**
 * Class example to showcase MonitorJobs functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class MonitorJobsTest {

    private static final Logger LOG = LogManager.getLogger(MonitorJobsTest.class);

    private static SubmitJobs submitJobs;
    private static ZOSConnection connection;

    /**
     * Main method defines host and user connection needed to showcase
     * MonitorJobs functionality. Calls MonitorJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String zosmfPort = "XXX";
        String userName = "XXX";
        String password = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        submitJobs = new SubmitJobs(connection);

        MonitorJobsTest.tstMonitorJobsForOutputStatusByJobObject();
        MonitorJobsTest.tstMonitorJobsForOutputStatusByJobNameAndId();
        MonitorJobsTest.tstMonitorJobsForStatusByJobObject(JobStatus.Type.INPUT);
        MonitorJobsTest.tstMonitorJobsForStatusByJobNameAndId(JobStatus.Type.ACTIVE);
        MonitorJobsTest.tstMonitorWaitForJobMessage("XXX");
    }

    /**
     * Example on how to call MonitorJobs waitForJobOutputStatus method.
     * waitForJobOutputStatus accepts a job object which will monitor the job status
     * until it reaches OUTPUT status or times out if not reached.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void tstMonitorJobsForOutputStatusByJobObject() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJobs.submitJcl(jclString, null, null);
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        job = monitorJobs.waitForJobOutputStatus(job);
        LOG.info("Job status for Job " + job.getJobName().get() + ":" +
                job.getJobId().get() + " is " + job.getStatus().get());
    }

    /**
     * Example on how to call MonitorJobs waitForJobOutputStatus method.
     * waitForJobOutputStatus accepts a jobName and jobId values which will
     * monitor the job status until it reaches OUTPUT status or times out if not reached.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void tstMonitorJobsForOutputStatusByJobNameAndId() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJobs.submitJcl(jclString, null, null);
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        job = monitorJobs.waitForJobOutputStatus(job.getJobName().get(), job.getJobId().get());
        LOG.info("Job status for Job " + job.getJobName().get() + ":" +
                job.getJobId().get() + " is " + job.getStatus().get());
    }

    /**
     * Example on how to call MonitorJobs waitForJobStatus method.
     * waitForJobStatus accepts a job object and status value which will monitor the
     * job status until it reaches the given status value or times out if not reached.
     *
     * @param statusType given status type to use for monitoring
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void tstMonitorJobsForStatusByJobObject(JobStatus.Type statusType) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("XXX").jobId("XXX").build();
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        job = monitorJobs.waitForJobStatus(job, statusType);
        LOG.info("Job status for Job " + job.getJobName().get() + ":" +
                job.getJobId().get() + " is " + job.getStatus().get());
    }

    /**
     * Example on how to call MonitorJobs waitForJobStatus method.
     * waitForJobStatus accepts a jobName and jobId values and status value which will monitor the
     * job status until it reaches the given status value or times out if not reached.
     *
     * @param statusType given status type to use for monitoring
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void tstMonitorJobsForStatusByJobNameAndId(JobStatus.Type statusType) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("XXX").jobId("XXX").build();
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        job = monitorJobs.waitForJobStatus(job.getJobName().get(), job.getJobId().get(), statusType);
        LOG.info("Job status for Job " + job.getJobName().get() + ":" +
                job.getJobId().get() + " is " + job.getStatus().get());
    }

    /**
     * Example on how to call MonitorJobs tstMonitorWaitForJobMessage method.
     * tstMonitorWaitForJobMessage accepts a message value which will monitor the
     * job output until the message is seen or times out if not seen.
     *
     * @param message given message text to monitor job output
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void tstMonitorWaitForJobMessage(String message) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("XXX").jobId("XXX").build();
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        LOG.info("Found message = " + monitorJobs.waitForJobMessage(job, message));
    }

}
