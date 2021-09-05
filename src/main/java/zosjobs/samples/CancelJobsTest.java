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
import zosjobs.CancelJobs;
import zosjobs.input.CancelJobParams;
import zosjobs.response.Job;

/**
 * Class example to showcase CancelJobs functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class CancelJobsTest {

    private static final Logger LOG = LogManager.getLogger(CancelJobsTest.class);

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CancelJobs functionality. Calls CancelJobs example methods.
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
        String jobId = "XXX";
        String jobName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        LOG.info(tstCancelJobsCommon(connection, jobName, jobId));
        LOG.info(tstCancelJobForJob(connection, jobName, jobId));
        LOG.info(tstCancelJob(connection, jobName, jobId));
    }

    /**
     * Example on how to call CancelJobs cancelJobsCommon method.
     * cancelJobsCommon accepts a CancelJobParams object with parameters filled needed to cancel a given job.
     *
     * @param connection ZOSConnection
     * @param jobName    job name value
     * @param jobId      job id value
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response tstCancelJobsCommon(ZOSConnection connection, String jobName, String jobId) throws Exception {
        CancelJobParams params = new CancelJobParams.Builder(jobName, jobId).build();
        return new CancelJobs(connection).cancelJobsCommon(params);
    }

    /**
     * Example on how to call CancelJobs cancelJobForJob method.
     * cancelJobForJob accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @param connection ZOSConnection
     * @param jobName    job name value
     * @param jobId      job id value
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response tstCancelJobForJob(ZOSConnection connection, String jobName, String jobId) throws Exception {
        return new CancelJobs(connection).cancelJobForJob(
                new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
    }

    /**
     * Example on how to call CancelJobs cancelJob method.
     * cancelJob accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @param connection ZOSConnection
     * @param jobName    job name value
     * @param jobId      job id value
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response tstCancelJob(ZOSConnection connection, String jobName, String jobId) throws Exception {
        return new CancelJobs(connection).cancelJob(jobName, jobId, null);
    }

}
