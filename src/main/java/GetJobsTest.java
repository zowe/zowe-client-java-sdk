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

import java.util.List;

public class GetJobsTest {

    private static final Logger LOG = LogManager.getLogger(GetJobsTest.class);
    private static GetJobs getJobs;

    public static void main(String[] args) throws Exception {
        String hostName = "XXX";
        String port = "XXX";
        String userName = "XXX";
        String password = "XXX";
        String prefix = "XXX";
        String owner = "XXX";
        String jobId = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, port, userName, password);
        getJobs = new GetJobs(connection);

        GetJobsTest.tstGetJobsCommon(prefix);
        GetJobsTest.tstGetSpoolFiles(prefix);
        GetJobsTest.tstGetSpoolFilesForJob(prefix);
        GetJobsTest.tstGetJobsByOwner(owner);
        GetJobsTest.tstGetSpoolContent(prefix);
        GetJobsTest.tstGetJobs();
        GetJobsTest.tstGetJobsByPrefix(prefix);
        GetJobsTest.tstGetJobsByOwnerAndPrefix("*", prefix);
        GetJobsTest.tstGetJob(prefix);
        GetJobsTest.tstNonExistentGetJob(jobId);
        GetJobsTest.tstGetStatus(prefix);
        GetJobsTest.tstGetStatusForJob(prefix);
        GetJobsTest.tstGetJcl(prefix);
        GetJobsTest.tstGetJclForJob(prefix);
        GetJobsTest.tstGetJclCommon(prefix);
    }

    private static void tstGetJclCommon(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        LOG.info(getJobs.getJclCommon(
                new CommonJobParms(jobs.get(0).getJobId().get(), jobs.get(0).getJobName().get())));
    }

    private static void tstGetJclForJob(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        LOG.info(getJobs.getJclForJob(jobs.get(0)));
    }

    private static void tstGetJcl(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        LOG.info(getJobs.getJcl(jobs.get(0).getJobName().get(), jobs.get(0).getJobId().get()));
    }

    private static void tstGetStatusForJob(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        try {
            Job job = getJobs.getStatusForJob(jobs.get(0));
            LOG.info(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tstGetStatus(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        try {
            Job job = getJobs.getStatus(jobs.get(0).getJobName().get(), jobs.get(0).getJobId().get());
            LOG.info(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void tstNonExistentGetJob(String jobId) {
        try {
            getJobs.getJob(jobId);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private static void tstGetJob(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        String jobId = jobs.get(0).getJobId().get();
        try {
            Job job = getJobs.getJob(jobId);
            LOG.info(job);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    private static void tstGetJobsByOwnerAndPrefix(String owner, String prefix)
            throws Exception {
        List<Job> jobs = getJobs.getJobsByOwnerAndPrefix(owner, prefix);
        jobs.forEach(LOG::info);
    }

    private static void tstGetJobsByPrefix(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        jobs.forEach(LOG::info);
    }

    private static void tstGetJobs() throws Exception {
        // get any jobs out there for the logged in user..
        List<Job> jobs = getJobs.getJobs();
        jobs.forEach(LOG::info);
    }

    private static void tstGetSpoolContent(String prefix) throws Exception {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(parms);
        List<JobFile> files = getJobs.getSpoolFilesForJob(jobs.get(0));
        String[] output = getJobs.getSpoolContent(files.get(0)).split("\n");
        // get last 10 lines of output
        for (int i = output.length - 10; i < output.length; i++)
            LOG.info(output[i]);
    }

    private static void tstGetJobsByOwner(String owner) throws Exception {
        List<Job> jobs = getJobs.getJobsByOwner(owner);
        jobs.forEach(LOG::info);
    }

    private static void tstGetSpoolFilesForJob(String prefix) throws Exception {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(parms);
        List<JobFile> files = getJobs.getSpoolFilesForJob(jobs.get(0));
        files.forEach(LOG::info);
    }

    private static void tstGetSpoolFiles(String prefix) throws Exception {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(parms);
        List<JobFile> files = getJobs.getSpoolFiles(jobs.get(0).getJobName().get(),
                jobs.get(0).getJobId().get());
        files.forEach(LOG::info);
    }

    public static void tstGetJobsCommon(String prefix) throws Exception {
        GetJobParms parms = new GetJobParms.Builder().owner("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(parms);
        jobs.forEach(LOG::info);
    }

}
