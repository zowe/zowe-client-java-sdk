/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package examples.zosjobs;

import core.ZOSConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosjobs.input.CommonJobParams;
import zosjobs.input.GetJobParams;
import zosjobs.input.JobFile;
import zosjobs.response.Job;

import java.util.List;

/**
 * Class example to showcase GetJobs functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class GetJobs {

    private static final Logger LOG = LogManager.getLogger(GetJobs.class);

    private static zosjobs.GetJobs getJobs;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * GetJobs functionality. Calls GetJobs example methods.
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
        String prefix = "XXX";
        String owner = "XXX";
        String jobId = "XXX";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        getJobs = new zosjobs.GetJobs(connection);

        GetJobs.getJobsCommon(prefix);
        GetJobs.getSpoolFiles(prefix);
        GetJobs.getSpoolFilesForJob(prefix);
        GetJobs.getJobsByOwner(owner);
        GetJobs.getSpoolContent(prefix);
        GetJobs.getJobs();
        GetJobs.getJobsByPrefix(prefix);
        GetJobs.getJobsByOwnerAndPrefix("*", prefix);
        GetJobs.getJob(prefix);
        GetJobs.nonExistentGetJob(jobId);
        GetJobs.getStatus(prefix);
        GetJobs.getStatusForJob(prefix);
        GetJobs.getJcl(prefix);
        GetJobs.getJclForJob(prefix);
        GetJobs.getJclCommon(prefix);
    }

    /**
     * Example on how to call GetJobs getJclCommon method.
     * getJclCommon is given CommonJobParams object filled with information on the given job to
     * use for retrieval of its JCL content
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJclCommon(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        LOG.info(getJobs.getJclCommon(
                new CommonJobParams(jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")),
                        jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")))));
    }

    /**
     * Example on how to call GetJobs getJclForJob method.
     * getJclForJob is given a job object to use for retrieval of its JCL content
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJclForJob(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        LOG.info(getJobs.getJclForJob(jobs.get(0)));
    }

    /**
     * Example on how to call GetJobs getJcl method.
     * getJcl is given a jobName and jobId values to use for retrieval of its JCL content
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJcl(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        LOG.info(getJobs.getJcl(jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
                jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified"))));
    }

    /**
     * Example on how to call GetJobs getStatusForJob method.
     * getStatusForJob is given a job object to use for retrieval of its status
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getStatusForJob(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        try {
            Job job = getJobs.getStatusForJob(jobs.get(0));
            LOG.info(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example on how to call GetJobs getStatus method.
     * getStatus is given a jobName and jobId value to use for retrieval of its status
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getStatus(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        try {
            Job job = getJobs.getStatus(jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
                    jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")));
            LOG.info(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example on how to call GetJobs getJob method.
     * getJob is given a jobId value for a non-existing job which will return an exception
     *
     * @param jobId jobId value
     * @author Frank Giordano
     */
    private static void nonExistentGetJob(String jobId) {
        try {
            getJobs.getJob(jobId);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Example on how to call GetJobs getJob method.
     * getJob is given a jobId value which will return a job document/object
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJob(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        String jobId = jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified"));
        try {
            Job job = getJobs.getJob(jobId);
            LOG.info(job);
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
    }

    /**
     * Example on how to call GetJobs getJobsByOwnerAndPrefix method.
     * getJobsByOwnerAndPrefix is given an owner and prefix values which will return a
     * list of common job document/object
     *
     * @param owner  owner value
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJobsByOwnerAndPrefix(String owner, String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByOwnerAndPrefix(owner, prefix);
        jobs.forEach(LOG::info);
    }

    /**
     * Example on how to call GetJobs getJobsByPrefix method.
     * getJobsByPrefix is given a prefix value which will return a
     * list of common job document/object
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJobsByPrefix(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        jobs.forEach(LOG::info);
    }

    /**
     * Example on how to call GetJobs' getJobs method. It returns a list of all
     * jobs available for the logged-in user.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJobs() throws Exception {
        // get any jobs out there for the logged-in user
        List<Job> jobs = getJobs.getJobs();
        jobs.forEach(LOG::info);
    }

    /**
     * Example on how to call GetJobs getSpoolContent method.
     * getSpoolContent is given a job spool file name to retrieve its content.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getSpoolContent(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(params);
        List<JobFile> files = getJobs.getSpoolFilesForJob(jobs.get(0));
        String[] output = getJobs.getSpoolContent(files.get(0)).split("\n");
        // get last 10 lines of output
        for (int i = output.length - 10; i < output.length; i++)
            LOG.info(output[i]);
    }

    /**
     * Example on how to call GetJobs getJobsByOwner method.
     * getJobsByOwner is given an owner value to use retrieve a list of its available jobs.
     *
     * @param owner owner value
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getJobsByOwner(String owner) throws Exception {
        List<Job> jobs = getJobs.getJobsByOwner(owner);
        jobs.forEach(LOG::info);
    }

    /**
     * Example on how to call GetJobs getSpoolFilesForJob method.
     * getSpoolFilesForJob is given a job document/object retrieve a list of all its spool names.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getSpoolFilesForJob(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(params);
        List<JobFile> files = getJobs.getSpoolFilesForJob(jobs.get(0));
        files.forEach(LOG::info);
    }

    /**
     * Example on how to call GetJobs getSpoolFiles method.
     * getSpoolFiles is given a jobName and jobId values to retrieve a list of all its spool file names.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    private static void getSpoolFiles(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(params);
        List<JobFile> files =
                getJobs.getSpoolFiles(jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
                        jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")));
        files.forEach(LOG::info);
    }

    /**
     * Example on how to call GetJobs getJobsCommon method.
     * getJobsCommon is given a GetJobParams object filled with search parameters which will retrieve a list of all jobs.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getJobsCommon(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(params);
        jobs.forEach(LOG::info);
    }

}
