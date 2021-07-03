/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosjobs.input.CommonJobParms;
import zosjobs.input.GetJobParms;
import zosjobs.GetJobs;
import zosjobs.input.JobFile;
import zosjobs.response.Job;

import java.io.IOException;
import java.util.List;

public class GetJobsTest {

    private static final Logger LOG = LogManager.getLogger(GetJobsTest.class);

    public static void main(String[] args) throws IOException {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String prefix = "XXX";
        String owner = "XXX";
        String jobId = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);

        GetJobsTest.tstGetJobsCommon(connection, prefix);
        GetJobsTest.tstGetSpoolFiles(connection, prefix);
        GetJobsTest.tstGetSpoolFilesForJob(connection, prefix);
        GetJobsTest.tstGetJobsByOwner(connection, owner);
        GetJobsTest.tstGetSpoolContent(connection, prefix);
        GetJobsTest.tstGetJobs(connection);
        GetJobsTest.tstGetJobsByPrefix(connection, prefix);
        GetJobsTest.tstGetJobsByOwnerAndPrefix(connection, "*", prefix);
        GetJobsTest.tstGetJob(connection, prefix);
        GetJobsTest.tstNonExistentGetJob(connection, jobId);
        GetJobsTest.tstGetStatus(connection, prefix);
        GetJobsTest.tstGetStatusForJob(connection, prefix);
        GetJobsTest.tstGetJcl(connection, prefix);
        GetJobsTest.tstGetJclForJob(connection, prefix);
        GetJobsTest.tstGetJclCommon(connection, prefix);
    }

    private static void tstGetJclCommon(ZOSConnection connection, String prefix) throws IOException {
        List<Job> jobs = GetJobs.getJobsByPrefix(connection, prefix);
        LOG.info(GetJobs.getJclCommon(connection,
                new CommonJobParms(jobs.get(0).getJobId().get(), jobs.get(0).getJobName().get())));
    }

    private static void tstGetJclForJob(ZOSConnection connection, String prefix) throws IOException {
        List<Job> jobs = GetJobs.getJobsByPrefix(connection, prefix);
        LOG.info(GetJobs.getJclForJob(connection, jobs.get(0)));
    }

    private static void tstGetJcl(ZOSConnection connection, String prefix) throws IOException {
        List<Job> jobs = GetJobs.getJobsByPrefix(connection, prefix);
        LOG.info(GetJobs.getJcl(connection, jobs.get(0).getJobName().get(), jobs.get(0).getJobId().get()));
    }

    private static void tstGetStatusForJob(ZOSConnection connection, String prefix) throws IOException {
        List<Job> jobs = GetJobs.getJobsByPrefix(connection, prefix);
        try {
            Job job = GetJobs.getStatusForJob(connection, jobs.get(0));
            LOG.info(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tstGetStatus(ZOSConnection connection, String prefix) throws IOException {
        List<Job> jobs = GetJobs.getJobsByPrefix(connection, prefix);
        try {
            Job job = GetJobs.getStatus(connection, jobs.get(0).getJobName().get(), jobs.get(0).getJobId().get());
            LOG.info(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tstNonExistentGetJob(ZOSConnection connection, String jobId) {
        try {
            GetJobs.getJob(connection, jobId);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private static void tstGetJob(ZOSConnection connection, String prefix) throws IOException {
        List<Job> jobs = GetJobs.getJobsByPrefix(connection, prefix);
        String jobId = jobs.get(0).getJobId().get();
        try {
            Job job = GetJobs.getJob(connection, jobId);
            LOG.info(job);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private static void tstGetJobsByOwnerAndPrefix(ZOSConnection connection, String owner, String prefix)
            throws IOException {
        List<Job> jobs = GetJobs.getJobsByOwnerAndPrefix(connection, owner, prefix);
        jobs.forEach(LOG::info);
    }

    private static void tstGetJobsByPrefix(ZOSConnection connection, String prefix) throws IOException {
        List<Job> jobs = GetJobs.getJobsByPrefix(connection, prefix);
        jobs.forEach(LOG::info);
    }

    private static void tstGetJobs(ZOSConnection connection) throws IOException {
        // get any jobs out there for the logged in user..
        List<Job> jobs = GetJobs.getJobs(connection);
        jobs.forEach(LOG::info);
    }

    private static void tstGetSpoolContent(ZOSConnection connection, String prefix) throws IOException {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = GetJobs.getJobsCommon(connection, parms);
        List<JobFile> files = GetJobs.getSpoolFilesForJob(connection, jobs.get(0));
        String[] output = GetJobs.getSpoolContent(connection, files.get(0)).split("\n");
        // get last 10 lines of output
        for (int i = output.length - 10; i < output.length; i++)
            LOG.info(output[i]);
    }

    private static void tstGetJobsByOwner(ZOSConnection connection, String owner) throws IOException {
        List<Job> jobs = GetJobs.getJobsByOwner(connection, owner);
        jobs.forEach(LOG::info);
    }

    private static void tstGetSpoolFilesForJob(ZOSConnection connection, String prefix) throws IOException {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = GetJobs.getJobsCommon(connection, parms);
        List<JobFile> files = GetJobs.getSpoolFilesForJob(connection, jobs.get(0));
        files.forEach(LOG::info);
    }

    private static void tstGetSpoolFiles(ZOSConnection connection, String prefix) throws IOException {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = GetJobs.getJobsCommon(connection, parms);
        List<JobFile> files = GetJobs.getSpoolFiles(connection, jobs.get(0).getJobName().get(),
                jobs.get(0).getJobId().get());
        files.forEach(LOG::info);
    }

    public static void tstGetJobsCommon(ZOSConnection connection, String prefix) throws IOException {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = GetJobs.getJobsCommon(connection, parms);
        jobs.forEach(LOG::info);
    }

}
