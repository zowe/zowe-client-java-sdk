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

public class MonitorJobsTest {

    private static final Logger LOG = LogManager.getLogger(MonitorJobsTest.class);

    private static SubmitJobs submitJobs;
    private static ZOSConnection connection;

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";

        connection = new ZOSConnection(hostName, port, userName, password);
        submitJobs = new SubmitJobs(connection);

        MonitorJobsTest.tstMonitorJobsForOutputStatusByJobObject();
        MonitorJobsTest.tstMonitorJobsForOutputStatusByJobNameAndId();
    }

    private static void tstMonitorJobsForOutputStatusByJobObject() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJobs.submitJcl(jclString, null, null);
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        job = monitorJobs.waitForJobOutputStatus(job);
        LOG.info("Job status for Job " + job.getJobName().get() + ":" + job.getJobId().get() + " is " + job.getStatus().get());
    }

    private static void tstMonitorJobsForOutputStatusByJobNameAndId() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJobs.submitJcl(jclString, null, null);
        MonitorJobs monitorJobs = new MonitorJobs(connection);
        job = monitorJobs.waitForJobOutputStatus(job.getJobName().get(), job.getJobId().get());
        LOG.info("Job status for Job " + job.getJobName().get() + ":" + job.getJobId().get() + " is " + job.getStatus().get());
    }

}
