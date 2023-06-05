# z/OS Jobs Package

Contains APIs to interact with jobs on z/OS (using z/OSMF jobs REST endpoints).

## API Examples

**Cancel a job**

````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosjobs.methods.JobCancel;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.response.Job;

/**
 * Class example to showcase CancelJobs functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class CancelJobsTst extends TstZosConnection {

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
        System.out.println(cancelJobsCommonWithVersion("2.0"));
        System.out.println(cancelJobsCommon());
        System.out.println(cancelJobForJob());
        System.out.println(cancelJob());
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
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        return new JobCancel(connection).cancelJobsCommon(params);
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
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        return new JobCancel(connection).cancelJobsCommon(params);
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
        jobId = "xxx";
        jobName = "xxx";
        return new JobCancel(connection).cancelJobForJob(
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
        jobId = "xxx";
        jobName = "xxx";
        return new JobCancel(connection).cancelJob(jobName, jobId, null);
    }

}
`````

**Delete a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosjobs.methods.JobDelete;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.response.Job;

/**
 * Class example to showcase DeleteJobs functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class DeleteJobsTst extends TstZosConnection {

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
        System.out.println(deleteJobsCommonWithVersion("2.0"));
        System.out.println(deleteJobsCommon());
        System.out.println(deleteJobForJob());
        System.out.println(deleteJob());
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
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        return new JobDelete(connection).deleteJobCommon(params);
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
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        return new JobDelete(connection).deleteJobCommon(params);
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
        jobId = "xxx";
        jobName = "xxx";
        return new JobDelete(connection).deleteJobForJob(
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
        jobId = "xxx";
        jobName = "xxx";
        return new JobDelete(connection).deleteJob(jobName, jobId, null);
    }

}
`````

**Get a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosjobs.methods.JobGet;
import zowe.client.sdk.zosjobs.input.CommonJobParams;
import zowe.client.sdk.zosjobs.input.GetJobParams;
import zowe.client.sdk.zosjobs.input.JobFile;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.Arrays;
import java.util.List;

/**
 * Class example to showcase GetJobs functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class GetJobsTst extends TstZosConnection {

    private static JobGet getJobs;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * GetJobs functionality. Calls GetJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String prefix = "xxx";
        String owner = "xxx";
        String jobId = "xxx";

        ZOSConnection connection = new ZOSConnection(hostName, zosmfPort, userName, password);
        getJobs = new JobGet(connection);

        GetJobsTst.getJobsCommon(prefix);
        GetJobsTst.getSpoolFiles(prefix);
        GetJobsTst.getSpoolFilesForJob(prefix);
        GetJobsTst.getJobsByOwner(owner);
        GetJobsTst.getSpoolContent(prefix);
        GetJobsTst.getJobs();
        GetJobsTst.getJobsByPrefix(prefix);
        GetJobsTst.getJobsByOwnerAndPrefix("*", prefix);
        GetJobsTst.getJob(prefix);
        GetJobsTst.nonExistentGetJob(jobId);
        GetJobsTst.getStatusCommon(prefix);
        GetJobsTst.getStatus(prefix);
        GetJobsTst.getStatusForJob(prefix);
        GetJobsTst.getJcl(prefix);
        GetJobsTst.getJclForJob(prefix);
        GetJobsTst.getJclCommon(prefix);
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
        System.out.println(getJobs.getJclCommon(
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
        System.out.println(getJobs.getJclForJob(jobs.get(0)));
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
        System.out.println(
                getJobs.getJcl(jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
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
            System.out.println(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example on how to call GetJobs getStatus method with StepData flag.
     * getStatus is given a jobName and jobId value to use for retrieval of its status with StepData flag set to true
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getStatusCommon(String prefix) throws Exception {
        List<Job> jobs = getJobs.getJobsByPrefix(prefix);
        try {
            Job job = getJobs.getStatusCommon(
                    new CommonJobParams(jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")),
                            jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")), true));
            System.out.println(job.toString());
            Arrays.stream(job.getStepData().orElseThrow(() -> new Exception("no step data found"))).forEach(i -> System.out.println(i.toString()));
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
            System.out.println(job.toString());
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
            System.out.println(e.getMessage());
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
            System.out.println(job.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        jobs.forEach(i -> System.out.println(i.toString()));
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
        jobs.forEach(i -> System.out.println(i.toString()));
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
        jobs.forEach(i -> System.out.println(i.toString()));
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
            System.out.println(output[i]);
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
        jobs.forEach(i -> System.out.println(i.toString()));
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
        files.forEach(i -> System.out.println(i.toString()));
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
        files.forEach(i -> System.out.println(i.toString()));
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
        jobs.forEach(i -> System.out.println(i.toString()));
    }

}

`````

**Monitor a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosjobs.methods.JobSubmit;
import zowe.client.sdk.zosjobs.methods.SubmitJobs;
import zowe.client.sdk.zosjobs.input.MonitorJobWaitForParams;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.types.JobStatus;

/**
 * Class example to showcase MonitorJobs functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class MonitorJobsTst extends TstZosConnection {

    private static JobSubmit submitJobs;
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
        submitJobs = new JobSubmit(connection);
        MonitorJobsTst.monitorJobsForOutputStatusByJobObject();
        MonitorJobsTst.monitorJobsForOutputStatusByJobNameAndId();
        MonitorJobsTst.monitorJobsForStatusByJobObject(JobStatus.Type.INPUT);
        MonitorJobsTst.monitorJobsForStatusByJobNameAndId(JobStatus.Type.ACTIVE);
        MonitorJobsTst.monitorWaitForJobMessage("xxx");
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
        zowe.client.sdk.zosjobs.methods.JobMonitor monitorJobs = new zowe.client.sdk.zosjobs.methods.JobMonitor(connection);
        MonitorJobWaitForParams monitorParams = new MonitorJobWaitForParams.Builder("XXX", "XXX").build();
        System.out.println(monitorJobs.isJobRunning(monitorParams));
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
        zowe.client.sdk.zosjobs.methods.JobMonitor monitorJobs = new zowe.client.sdk.zosjobs.methods.JobMonitor(connection);
        job = monitorJobs.waitForJobOutputStatus(job);
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
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
        zowe.client.sdk.zosjobs.methods.JobMonitor monitorJobs = new zowe.client.sdk.zosjobs.methods.JobMonitor(connection);
        job = monitorJobs.waitForJobOutputStatus(
                job.getJobName().orElseThrow(() -> new Exception("job name not specified")),
                job.getJobId().orElseThrow(() -> new Exception("job id not specified")));
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
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
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        zowe.client.sdk.zosjobs.methods.JobMonitor monitorJobs = new zowe.client.sdk.zosjobs.methods.JobMonitor(connection);
        job = monitorJobs.waitForJobStatus(job, statusType);
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
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
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        zowe.client.sdk.zosjobs.methods.JobMonitor monitorJobs = new zowe.client.sdk.zosjobs.methods.JobMonitor(connection);
        job = monitorJobs.waitForJobStatus(
                job.getJobName().orElseThrow(() -> new Exception("job name not specified")),
                job.getJobId().orElseThrow(() -> new Exception("job id not specified")), statusType);
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
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
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        zowe.client.sdk.zosjobs.methods.JobMonitor monitorJobs = new zowe.client.sdk.zosjobs.methods.JobMonitor(connection);
        System.out.println("Found message = " + monitorJobs.waitForJobMessage(job, message));
    }

}

`````

**Submit a job**

````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosjobs.methods.JobMonitor;
import zowe.client.sdk.zosjobs.methods.JobSubmit;
import zowe.client.sdk.zosjobs.methods.MonitorJobs;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.types.JobStatus;

/**
 * Class example to showcase SubmitJobs functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class SubmitJobsTst extends TstZosConnection {

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
        System.out.println(SubmitJobsTst.submitJob(connection, "xxx.xxx.xxx.xxx(xxx)"));

        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\n// EXEC PGM=IEFBR14";
        Job submitJobsTest = SubmitJobsTst.submitJclJob(connection, jclString);
        // Wait for the job to complete
        JobMonitor monitorJobs = new JobMonitor(connection);
        submitJobsTest = monitorJobs.waitForJobStatus(submitJobsTest, JobStatus.Type.OUTPUT);
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
    public static Job submitJclJob(zowe.client.sdk.core.ZOSConnection connection, String jclString) throws Exception {
        JobSubmit submitJobs = new JobSubmit(connection);
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
    public static Job submitJob(zowe.client.sdk.core.ZOSConnection connection, String dsMember) throws Exception {
        JobSubmit submitJobs = new JobSubmit(connection);
        return submitJobs.submitJob(dsMember);
    }

}

`````

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final String zosmfPort = "xxx";
    public static final String userName = "xxx";
    public static final String password = "xxx";

    // or use the following method to retrieve Zowe OS credential store for your
    // secure Zowe V2 credentials you entered when you initially set up Zowe Global Team Configuration.
    public static ZOSConnection getSecureZosConnection() throws Exception {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZOSConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````