/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs.samples;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosjobs.DeleteJobs;
import zosjobs.input.DeleteJobParams;
import zosjobs.response.Job;

/**
 * Class example to showcase DeleteJobs functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class DeleteJobsTest {

    private static final Logger LOG = LogManager.getLogger(DeleteJobsTest.class);

    private static ZOSConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * Main method defines host and user connection and other parameters needed to showcase
     * DeleteJobs functionality. Calls DeleteJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String zosmfPort = "XXX";
        String userName = "XXX";
        String password = "XXX";

        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        LOG.info(tstDeleteJobsCommon().getResponsePhrase().get());
        LOG.info(tstDeleteJobForJob().getResponsePhrase().get());
        LOG.info(tstDeleteJob().getResponsePhrase().get());
    }

    /**
     * Example on how to call DeleteJobs deleteJobCommon method.
     * deleteJobCommon accepts a DeleteJobParams object with parameters filled needed to delete a given job.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response tstDeleteJobsCommon() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        DeleteJobParams params = new DeleteJobParams.Builder().jobId(jobId).jobName(jobName).build();
        return new DeleteJobs(connection).deleteJobCommon(params);
    }

    /**
     * Example on how to call DeleteJobs deleteJobForJob method.
     * deleteJobForJob accepts a jobName and jobId values which will be used to delete its job.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response tstDeleteJobForJob() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        return new DeleteJobs(connection).deleteJobForJob(
                new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
    }

    /**
     * Example on how to call DeleteJobs deleteJob method.
     * deleteJob accepts a jobName and jobId values which will be used to delete its job.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response tstDeleteJob() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        return new DeleteJobs(connection).deleteJob(jobName, jobId, null);
    }

}
