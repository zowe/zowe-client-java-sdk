# z/OS Jobs Package

Contains APIs to interact with jobs on z/OS (using z/OSMF jobs REST endpoints).

## API Examples

**Cancel a job**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosjobs.input.ModifyJobParams;
import zosjobs.response.Job;

/**
 * Class example to showcase CancelJobs functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class CancelJobs extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(CancelJobs.class);

    private static ZOSConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * CancelJobs functionality. Calls CancelJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        LOG.info(cancelJobsCommonWithVersion("2.0"));
        LOG.info(cancelJobsCommon());
        LOG.info(cancelJobForJob());
        LOG.info(cancelJob());
    }

    /**
     * Example on how to call CancelJobs cancelJobsCommon method.
     * cancelJobsCommon accepts a CancelJobParams object with parameters filled needed to cancel a given job and
     * the version to indicate 1.0 for async or 2.0 for sync processing of the request
     *
     * @param version version value
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response cancelJobsCommonWithVersion(String version) throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        return new zosjobs.CancelJobs(connection).cancelJobsCommon(params);
    }

    /**
     * Example on how to call CancelJobs cancelJobsCommon method.
     * cancelJobsCommon accepts a CancelJobParams object with parameters filled needed to cancel a given job.
     *
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response cancelJobsCommon() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        return new zosjobs.CancelJobs(connection).cancelJobsCommon(params);
    }

    /**
     * Example on how to call CancelJobs cancelJobForJob method.
     * cancelJobForJob accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response cancelJobForJob() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        return new zosjobs.CancelJobs(connection).cancelJobForJob(
                new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
    }

    /**
     * Example on how to call CancelJobs cancelJob method.
     * cancelJob accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response cancelJob() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        return new zosjobs.CancelJobs(connection).cancelJob(jobName, jobId, null);
    }

}
`````

**Delete a job**

`````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rest.Response;
import zosjobs.input.ModifyJobParams;
import zosjobs.response.Job;

/**
 * Class example to showcase DeleteJobs functionality.
 *
 * @author Leonid Baranov
 * @version 1.0
 */
public class DeleteJobs extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(DeleteJobs.class);

    private static ZOSConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * DeleteJobs functionality. Calls DeleteJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        connection = new ZOSConnection(hostName, zosmfPort, userName, password);

        LOG.info(deleteJobsCommonWithVersion("2.0"));
        LOG.info(deleteJobsCommon());
        LOG.info(deleteJobForJob());
        LOG.info(deleteJob());
    }

    /**
     * Example on how to call DeleteJobs deleteJobCommon method.
     * deleteJobCommon accepts a DeleteJobParams object with parameters filled needed to delete a given job and
     * the version to indicate 1.0 for async or 2.0 for sync processing of the request
     *
     * @param version value to indicate sync or async request processing
     * @return response http response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response deleteJobsCommonWithVersion(String version) throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        return new zosjobs.DeleteJobs(connection).deleteJobCommon(params);
    }

    /**
     * Example on how to call DeleteJobs deleteJobCommon method.
     * deleteJobCommon accepts a DeleteJobParams object with parameters filled needed to delete a given job.
     *
     * @return response http response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response deleteJobsCommon() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        return new zosjobs.DeleteJobs(connection).deleteJobCommon(params);
    }

    /**
     * Example on how to call DeleteJobs deleteJobForJob method.
     * deleteJobForJob accepts a jobName and jobId values which will be used to delete its job.
     *
     * @return response http response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response deleteJobForJob() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        return new zosjobs.DeleteJobs(connection).deleteJobForJob(
                new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
    }

    /**
     * Example on how to call DeleteJobs deleteJob method.
     * deleteJob accepts a jobName and jobId values which will be used to delete its job.
     *
     * @return response http response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response deleteJob() throws Exception {
        jobId = "XXX";
        jobName = "XXX";
        return new zosjobs.DeleteJobs(connection).deleteJob(jobName, jobId, null);
    }

}
`````

**Get a job**

`````java
import core.ZOSConnection;
import examples.ZosConnection;
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
public class GetJobs extends ZosConnection {

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
    public static void getJclCommon(String prefix) throws Exception {
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
    public static void getJclForJob(String prefix) throws Exception {
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
    public static void getJcl(String prefix) throws Exception {
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
    public static void getStatusForJob(String prefix) throws Exception {
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
    public static void getStatus(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        try {
            Job job = getJobs.getStatus(
                    jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
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
    public static void nonExistentGetJob(String jobId) {
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
    public static void getJob(String prefix) throws Exception {
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
    public static void getJobsByOwnerAndPrefix(String owner, String prefix) throws Exception {
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
    public static void getJobsByPrefix(String prefix) throws Exception {
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
    public static void getJobs() throws Exception {
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
    public static void getSpoolContent(String prefix) throws Exception {
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
    public static void getJobsByOwner(String owner) throws Exception {
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
    public static void getSpoolFilesForJob(String prefix) throws Exception {
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
    public static void getSpoolFiles(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJobs.getJobsCommon(params);
        List<JobFile> files =
                getJobs.getSpoolFiles(
                        jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
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
`````

**Monitor a job**

`````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosjobs.SubmitJobs;
import zosjobs.input.MonitorJobWaitForParams;
import zosjobs.response.Job;
import zosjobs.types.JobStatus;

/**
 * Class example to showcase MonitorJobs functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class MonitorJobs extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(MonitorJobs.class);

    private static SubmitJobs submitJobs;
    private static ZOSConnection connection;

    /**
     * Main method defines z/OSMF host and user connection needed to showcase
     * MonitorJobs functionality. Calls MonitorJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        submitJobs = new SubmitJobs(connection);

        MonitorJobs.monitorJobsForOutputStatusByJobObject();
        MonitorJobs.monitorJobsForOutputStatusByJobNameAndId();
        MonitorJobs.monitorJobsForStatusByJobObject(JobStatus.Type.INPUT);
        MonitorJobs.monitorJobsForStatusByJobNameAndId(JobStatus.Type.ACTIVE);
        MonitorJobs.monitorWaitForJobMessage("XXX");
        monitorIsJobRunning();
    }

    /**
     * Example on how to call MonitorJobs isJobRunning method.
     * isJobRunning accepts a MonitorJobWaitForParams object
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorIsJobRunning() throws Exception {
        zosjobs.MonitorJobs monitorJobs = new zosjobs.MonitorJobs(connection);
        MonitorJobWaitForParams monitorParams = new MonitorJobWaitForParams.Builder("XXX", "XXX").build();
        LOG.info(monitorJobs.isJobRunning(monitorParams));
    }

    /**
     * Example on how to call MonitorJobs waitForJobOutputStatus method.
     * waitForJobOutputStatus accepts a job object which will monitor the job status
     * until it reaches OUTPUT status or times out if not reached.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorJobsForOutputStatusByJobObject() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJobs.submitJcl(jclString, null, null);
        zosjobs.MonitorJobs monitorJobs = new zosjobs.MonitorJobs(connection);
        job = monitorJobs.waitForJobOutputStatus(job);
        LOG.info("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call MonitorJobs waitForJobOutputStatus method.
     * waitForJobOutputStatus accepts a jobName and jobId values which will
     * monitor the job status until it reaches OUTPUT status or times out if not reached.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorJobsForOutputStatusByJobNameAndId() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJobs.submitJcl(jclString, null, null);
        zosjobs.MonitorJobs monitorJobs = new zosjobs.MonitorJobs(connection);
        job = monitorJobs.waitForJobOutputStatus(
                job.getJobName().orElseThrow(() -> new Exception("job name not specified")),
                job.getJobId().orElseThrow(() -> new Exception("job id not specified")));
        LOG.info("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
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
    public static void monitorJobsForStatusByJobObject(JobStatus.Type statusType) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("XXX").jobId("XXX").build();
        zosjobs.MonitorJobs monitorJobs = new zosjobs.MonitorJobs(connection);
        job = monitorJobs.waitForJobStatus(job, statusType);
        LOG.info("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
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
    public static void monitorJobsForStatusByJobNameAndId(JobStatus.Type statusType) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("XXX").jobId("XXX").build();
        zosjobs.MonitorJobs monitorJobs = new zosjobs.MonitorJobs(connection);
        job = monitorJobs.waitForJobStatus(
                job.getJobName().orElseThrow(() -> new Exception("job name not specified")),
                job.getJobId().orElseThrow(() -> new Exception("job id not specified")), statusType);
        LOG.info("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
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
    public static void monitorWaitForJobMessage(String message) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("XXX").jobId("XXX").build();
        zosjobs.MonitorJobs monitorJobs = new zosjobs.MonitorJobs(connection);
        LOG.info("Found message = " + monitorJobs.waitForJobMessage(job, message));
    }

}
`````

**Submit a job**

````java
import core.ZOSConnection;
import examples.ZosConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zosjobs.response.Job;

/**
 * Class example to showcase SubmitJobs functionality.
 *
 * @author Frank Giordano
 * @version 1.0
 */
public class SubmitJobs extends ZosConnection {

    private static final Logger LOG = LogManager.getLogger(SubmitJobs.class);

    /**
     * Main method defines z/OSMF host and user connection needed to showcase
     * SubmitJobs functionality. Calls SubmitJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        LOG.info(SubmitJobs.submitJob(connection, "xxx.xxx.xxx.xxx(xxx)"));

        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\n// EXEC PGM=IEFBR14";
        Job submitJobsTest = SubmitJobs.submitJclJob(connection, jclString);
        // Wait for the job to complete
        zosjobs.MonitorJobs monitorJobs = new zosjobs.MonitorJobs(connection);
        submitJobsTest = monitorJobs.waitForJobStatus(submitJobsTest, zosjobs.types.JobStatus.Type.OUTPUT);
        System.out.println(submitJobsTest);
        // Get the return code
        String retCode = submitJobsTest.getRetCode().orElse("n/a");
        System.out.println("Expected Return Code = CC 0000 [" + retCode + "]");
    }

    /**
     * Example on how to call SubmitJobs submitJcl method.
     * submitJcl is given a jcl string to use to submit it as a job.
     *
     * @param connection ZOSConnection object
     * @param jclString  jcl formatted string
     * @return job document
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Job submitJclJob(ZOSConnection connection, String jclString) throws Exception {
        zosjobs.SubmitJobs submitJobs = new zosjobs.SubmitJobs(connection);
        return submitJobs.submitJcl(jclString, null, null);
    }

    /**
     * Example on how to call SubmitJobs submitJcl method.
     * submitJcl is given a Dataset member value to use to submit it as a job.
     *
     * @param connection ZOSConnection object
     * @param dsMember   dataset member value
     * @return job document
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Job submitJob(ZOSConnection connection, String dsMember) throws Exception {
        zosjobs.SubmitJobs submitJobs = new zosjobs.SubmitJobs(connection);
        return submitJobs.submitJob(dsMember);
    }

}
`````

````java
package examples;

/**
* Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
* duplicating connection details in each example.
*
* @author Frank Giordano
* @version 1.0
*/
public class ZosConnection {

    public static final String hostName = "XXX";
    public static final String zosmfPort = "XXX";
    public static final String userName = "XXX";
    public static final String password = "XXX";

}
`````