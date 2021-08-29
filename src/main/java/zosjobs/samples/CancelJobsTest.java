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

public class CancelJobsTest {

    private static final Logger LOG = LogManager.getLogger(CancelJobsTest.class);

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String jobId = "XXX";
        String jobName = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);
        LOG.info(tstCancelJobsCommon(connection, jobName, jobId));
        LOG.info(tstCancelJobForJob(connection, jobName, jobId));
        LOG.info(tstCancelJob(connection, jobName, jobId));
    }

    public static Response tstCancelJobsCommon(ZOSConnection connection, String jobName, String jobId) throws Exception {
        CancelJobParams params = new CancelJobParams.Builder().jobId(jobId).jobName(jobName).build();
        return new CancelJobs(connection).cancelJobsCommon(params);
    }

    public static Response tstCancelJobForJob(ZOSConnection connection, String jobName, String jobId) throws Exception {
        return new CancelJobs(connection).cancelJobForJob(
                new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
    }

    public static Response tstCancelJob(ZOSConnection connection, String jobName, String jobId) throws Exception {
        return new CancelJobs(connection).cancelJob(jobName, jobId, null);
    }

}
