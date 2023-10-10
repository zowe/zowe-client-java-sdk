# z/OS Jobs Package

Contains APIs to interact with jobs on z/OS (using z/OSMF jobs REST endpoints).

APIs located in methods package.

## API Examples

**Cancel a job**

````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.methods.JobCancel;
import zowe.client.sdk.zosjobs.response.Job;

/**
 * Class example to showcase JobCancel class functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class CancelJobTst extends TstZosConnection {

    private static ZosConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobCancel functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        System.out.println(cancelCommonWithVersion("2.0"));
        System.out.println(cancelCommon());
        System.out.println(cancelByJob());
        System.out.println(cancel());
    }

    /**
     * Example on how to call JobCancel cancelCommon method.
     * The cancelCommon method accepts a CancelJobParams object with parameters filled needed
     * to cancel a given job and the version to indicate 1.0 for async or 2.0 for sync processing
     * of the request.
     *
     * @param version version value
     * @return response http Response object
     * @author Frank Giordano
     */
    public static Response cancelCommonWithVersion(String version) {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        return new JobCancel(connection).cancelCommon(params);
    }

    /**
     * Example on how to call JobCancel cancelCommon method.
     * The cancelCommon method accepts a CancelJobParams object with parameters filled needed
     * to cancel a given job.
     *
     * @return response http Response object
     * @author Frank Giordano
     */
    public static Response cancelCommon() {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        return new JobCancel(connection).cancelCommon(params);
    }

    /**
     * Example on how to call JobCancel cancelByJob method.
     * The cancelByJob method accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @return response http Response object
     * @author Frank Giordano
     */
    public static Response cancelByJob() {
        jobId = "xxx";
        jobName = "xxx";
        return new JobCancel(connection).cancelByJob(
                new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
    }

    /**
     * Example on how to call JobCancel cancel method.
     * The cancel method accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @return response http Response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response cancel() throws Exception {
        jobId = "xxx";
        jobName = "xxx";
        return new JobCancel(connection).cancel(jobName, jobId, null);
    }

}
`````

**Delete a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.methods.JobDelete;
import zowe.client.sdk.zosjobs.response.Job;

/**
 * Class example to showcase JobDelete class functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 2.0
 */
public class DeleteJobTst extends TstZosConnection {

    private static ZosConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobDelete functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Leonid Baranov
     */
    public static void main(String[] args) throws Exception {
        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        System.out.println(deleteCommonWithVersion("2.0"));
        System.out.println(deleteCommon());
        System.out.println(deleteByJob());
        System.out.println(deleteJob());
    }

    /**
     * Example on how to call JobDelete deleteCommon method.
     * The deleteCommon method accepts a DeleteJobParams object with parameters filled needed
     * to delete a given job and the version to indicate 1.0 for async or 2.0 for sync
     * processing of the request.
     *
     * @param version value to indicate sync or async request processing
     * @return response http response object
     * @author Frank Giordano
     */
    public static Response deleteCommonWithVersion(String version) {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        return new JobDelete(connection).deleteCommon(params);
    }

    /**
     * Example on how to call JobDelete deleteCommon method.
     * The deleteCommon method accepts a DeleteJobParams object with parameters
     * filled needed to delete a given job.
     *
     * @return response http response object
     * @author Frank Giordano
     */
    public static Response deleteCommon() {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        return new JobDelete(connection).deleteCommon(params);
    }

    /**
     * Example on how to call JobDelete deleteByJob method.
     * The deleteByJob method accepts a jobName and jobId values which will be used to delete its job.
     *
     * @return response http response object
     * @author Frank Giordano
     */
    public static Response deleteByJob() {
        jobId = "xxx";
        jobName = "xxx";
        return new JobDelete(connection).deleteByJob(
                new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
    }

    /**
     * Example on how to call JobDelete delete method.
     * The delete method accepts a jobName and jobId values which will be used to delete its job.
     *
     * @return response http response object
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Response deleteJob() throws Exception {
        jobId = "xxx";
        jobName = "xxx";
        return new JobDelete(connection).delete(jobName, jobId, null);
    }

}
`````

**Get a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.zosjobs.input.CommonJobParams;
import zowe.client.sdk.zosjobs.input.GetJobParams;
import zowe.client.sdk.zosjobs.input.JobFile;
import zowe.client.sdk.zosjobs.methods.JobGet;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.Arrays;
import java.util.List;

/**
 * Class example to showcase JobGet class functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class GetJobTst extends TstZosConnection {

    private static JobGet getJob;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobGet class functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        String prefix = "xxx";
        String owner = "xxx";
        String jobId = "xxx";

        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
        getJob = new JobGet(connection);

        GetJobTst.getCommon(prefix);
        GetJobTst.getSpoolFiles(prefix);
        GetJobTst.getSpoolFilesForJob(prefix);
        GetJobTst.getByOwner(owner);
        GetJobTst.getSpoolContent(prefix);
        GetJobTst.getAll();
        GetJobTst.getByPrefix(prefix);
        GetJobTst.getByOwnerAndPrefix("*", prefix);
        GetJobTst.getById(prefix);
        GetJobTst.nonExistentGetJob(jobId);
        GetJobTst.getStatusCommon(prefix);
        GetJobTst.getStatus(prefix);
        GetJobTst.getStatusByJob(prefix);
        GetJobTst.getJcl(prefix);
        GetJobTst.getJclByJob(prefix);
        GetJobTst.getJclCommon(prefix);
    }

    /**
     * Example on how to call JobGet getJclCommon method.
     * The getJclCommon method is given CommonJobParams object filled with information on the given job to
     * use for retrieval of its JCL content.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getJclCommon(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        System.out.println(getJob.getJclCommon(
                new CommonJobParams(jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")),
                        jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")))));
    }

    /**
     * Example on how to call JobGet getJclByJob method.
     * The getJclByJob method is given a job object to use for retrieval of its JCL content.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getJclByJob(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        System.out.println(getJob.getJclByJob(jobs.get(0)));
    }

    /**
     * Example on how to call JobGet getJcl method.
     * The getJcl method is given a jobName and jobId values to use for retrieval of its JCL content.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getJcl(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        System.out.println(
                getJob.getJcl(jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
                        jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified"))));
    }

    /**
     * Example on how to call JobGet getStatusByJob method.
     * The getStatusByJob method is given a job object to use for retrieval of its status.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getStatusByJob(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        try {
            Job job = getJob.getStatusByJob(jobs.get(0));
            System.out.println(job);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example on how to call JobGet getStatusCommon method with StepData flag.
     * The getStatusCommon method is given a jobName and jobId value to use for retrieval
     * of its status with StepData flag set to true.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getStatusCommon(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        try {
            Job job = getJob.getStatusCommon(
                    new CommonJobParams(jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")),
                            jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")), true));
            System.out.println(job.toString());
            Arrays.stream(job.getStepData().orElseThrow(() -> new Exception("no step data found"))).forEach(i -> System.out.println(i.toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example on how to call JobGet getStatus method.
     * The getStatus method is given a jobName and jobId value to use for retrieval of its status.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getStatus(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        try {
            Job job = getJob.getStatus(
                    jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
                    jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")));
            System.out.println(job.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example on how to call JobGet getById method.
     * The getById method is given a jobId value for a non-existing job which will return an exception.
     *
     * @param jobId jobId value
     * @author Frank Giordano
     */
    public static void nonExistentGetJob(String jobId) {
        try {
            getJob.getById(jobId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Example on how to call JobGet getById method.
     * The getById method is given a jobId value which will return a job document/object.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getById(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        String jobId = jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified"));
        try {
            Job job = getJob.getById(jobId);
            System.out.println(job.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Example on how to call JobGet getByOwnerAndPrefix method.
     * The getByOwnerAndPrefix method is given an owner and prefix values which will return a
     * list of common job document/object.
     *
     * @param owner  owner value
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getByOwnerAndPrefix(String owner, String prefix) throws Exception {
        List<Job> jobs = getJob.getByOwnerAndPrefix(owner, prefix);
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getByPrefix method.
     * The getByPrefix method is given a prefix value which will return a list of common job document/object.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getByPrefix(String prefix) throws Exception {
        List<Job> jobs = getJob.getByPrefix(prefix);
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getAll method.
     * The getAll method returns a list of all jobs available for the logged-in user.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getAll() throws Exception {
        // get any jobs out there for the logged-in user
        List<Job> jobs = getJob.getAll();
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getSpoolContent method.
     * The getSpoolContent method is given a job spool file name to retrieve its content.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getSpoolContent(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJob.getCommon(params);
        List<JobFile> files = getJob.getSpoolFilesByJob(jobs.get(0));
        String[] output = getJob.getSpoolContent(files.get(0)).split("\n");
        // get last 10 lines of output
        for (int i = output.length - 10; i < output.length; i++)
            System.out.println(output[i]);
    }

    /**
     * Example on how to call JobGet getByOwner method.
     * The getByOwner method is given an owner value to use retrieve a list of its available jobs.
     *
     * @param owner owner value
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getByOwner(String owner) throws Exception {
        List<Job> jobs = getJob.getByOwner(owner);
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getSpoolFilesByJob method.
     * The getSpoolFilesByJob method is given a job document/object retrieve a list of all its spool names.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getSpoolFilesForJob(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJob.getCommon(params);
        List<JobFile> files = getJob.getSpoolFilesByJob(jobs.get(0));
        files.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getSpoolFiles method.
     * The getSpoolFiles method is given a jobName and jobId values to retrieve a list of all its spool file names.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getSpoolFiles(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJob.getCommon(params);
        List<JobFile> files =
                getJob.getSpoolFiles(
                        jobs.get(0).getJobName().orElseThrow(() -> new Exception("job name not specified")),
                        jobs.get(0).getJobId().orElseThrow(() -> new Exception("job id not specified")));
        files.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getCommon method.
     * The getCommon method is given a GetJobParams object filled with search parameters which will
     * retrieve a list of all jobs.
     *
     * @param prefix partial or full job name to use for searching
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void getCommon(String prefix) throws Exception {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs = getJob.getCommon(params);
        jobs.forEach(i -> System.out.println(i.toString()));
    }

}
`````

**Monitor a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
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
    private static ZosConnection connection;

    /**
     * Main method defines z/OSMF host and user connection needed to showcase
     * MonitorJobs functionality. Calls MonitorJobs example methods.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        connection = new ZosConnection(hostName, zosmfPort, userName, password);
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

}import zowe.client.sdk.core.ZosConnection;
        import zowe.client.sdk.examples.TstZosConnection;
        import zowe.client.sdk.zosjobs.input.MonitorJobWaitForParams;
        import zowe.client.sdk.zosjobs.methods.JobMonitor;
        import zowe.client.sdk.zosjobs.methods.JobSubmit;
        import zowe.client.sdk.zosjobs.response.Job;
        import zowe.client.sdk.zosjobs.types.JobStatus;

/**
 * Class example to showcase JobMonitor class functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class MonitorJobTst extends TstZosConnection {

    private static JobSubmit submitJob;
    private static ZosConnection connection;

    /**
     * Main method defines z/OSMF host and user connection needed to showcase
     * JobMonitor functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        submitJob = new JobSubmit(connection);
        MonitorJobTst.monitorJobForOutputStatusByJobObject();
        MonitorJobTst.monitorJobForOutputStatusByJobNameAndId();
        MonitorJobTst.monitorJobForStatusByJobObject(JobStatus.Type.INPUT);
        MonitorJobTst.monitorJobForStatusByJobNameAndId(JobStatus.Type.ACTIVE);
        MonitorJobTst.monitorWaitForJobMessage("xxx");
        monitorIsJobRunning();
    }

    /**
     * Example on how to call JobMonitor isJobRunning method.
     * The isJobRunning method accepts a MonitorJobWaitForParams object.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorIsJobRunning() throws Exception {
        JobMonitor jobMonitor = new JobMonitor(connection);
        MonitorJobWaitForParams monitorParams = new MonitorJobWaitForParams.Builder("XXX", "XXX").build();
        System.out.println(jobMonitor.isRunning(monitorParams));
    }

    /**
     * Example on how to call JobMonitor waitByOutputStatus method.
     * The waitByOutputStatus method accepts a job object which will monitor the job status
     * until it reaches OUTPUT status or times out if not reached.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorJobForOutputStatusByJobObject() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJob.submitByJcl(jclString, null, null);
        JobMonitor jobMonitor = new JobMonitor(connection);
        job = jobMonitor.waitByOutputStatus(job);
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByOutputStatus method.
     * The waitByOutputStatus method accepts a jobName and jobId values which will
     * monitor the job status until it reaches OUTPUT status or times out if not reached.
     *
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorJobForOutputStatusByJobNameAndId() throws Exception {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job = submitJob.submitByJcl(jclString, null, null);
        JobMonitor jobMonitor = new JobMonitor(connection);
        job = jobMonitor.waitByOutputStatus(
                job.getJobName().orElseThrow(() -> new Exception("job name not specified")),
                job.getJobId().orElseThrow(() -> new Exception("job id not specified")));
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByStatus method.
     * The waitByStatus method accepts a job object and status value which will monitor the
     * job status until it reaches the given status value or times out if not reached.
     *
     * @param statusType given status type to use for monitoring
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorJobForStatusByJobObject(JobStatus.Type statusType) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        JobMonitor jobMonitor = new JobMonitor(connection);
        job = jobMonitor.waitByStatus(job, statusType);
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByStatus method.
     * The waitByStatus method accepts a jobName and jobId values and status value which will monitor the
     * job status until it reaches the given status value or times out if not reached.
     *
     * @param statusType given status type to use for monitoring
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorJobForStatusByJobNameAndId(JobStatus.Type statusType) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        JobMonitor jobMonitor = new JobMonitor(connection);
        job = jobMonitor.waitByStatus(
                job.getJobName().orElseThrow(() -> new Exception("job name not specified")),
                job.getJobId().orElseThrow(() -> new Exception("job id not specified")), statusType);
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByMessage method.
     * The waitByMessage method accepts a message value which will monitor the job output until
     * the message is seen or times out if not seen.
     *
     * @param message given message text to monitor job output
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void monitorWaitForJobMessage(String message) throws Exception {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        JobMonitor jobMonitor = new JobMonitor(connection);
        System.out.println("Found message = " + jobMonitor.waitByMessage(job, message));
    }

}
`````

**Submit a job**

````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
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
        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
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
    public static Job submitJclJob(zowe.client.sdk.core.ZosConnection connection, String jclString) throws Exception {
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
    public static Job submitJob(zowe.client.sdk.core.ZosConnection connection, String dsMember) throws Exception {
        JobSubmit submitJobs = new JobSubmit(connection);
        return submitJobs.submitJob(dsMember);
    }

}import zowe.client.sdk.core.ZosConnection;
        import zowe.client.sdk.examples.TstZosConnection;
        import zowe.client.sdk.zosjobs.methods.JobMonitor;
        import zowe.client.sdk.zosjobs.methods.JobSubmit;
        import zowe.client.sdk.zosjobs.response.Job;
        import zowe.client.sdk.zosjobs.types.JobStatus;

/**
 * Class example to showcase JobSubmit class functionality.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class SubmitJobTst extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection needed to showcase
     * JobSubmit functionality.
     *
     * @param args for main not used
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static void main(String[] args) throws Exception {
        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
        System.out.println(SubmitJobTst.submitJob(connection, "xxx.xxx.xxx.xxx(xxx)"));

        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\n// EXEC PGM=IEFBR14";
        Job submitJobsTest = SubmitJobTst.submitJclJob(connection, jclString);
        // Wait for the job to complete
        JobMonitor jobMonitor = new JobMonitor(connection);
        submitJobsTest = jobMonitor.waitByStatus(submitJobsTest, JobStatus.Type.OUTPUT);
        System.out.println(submitJobsTest);
        // Get the return code
        String retCode = submitJobsTest.getRetCode().orElse("n/a");
        System.out.println("Expected Return Code = CC 0000 [" + retCode + "]");
    }

    /**
     * Example on how to call JobSubmit submitByJcl method.
     * The submitByJcl method is given a jcl string to use to submit it as a job.
     *
     * @param connection ZosConnection object
     * @param jclString  jcl formatted string
     * @return job document
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Job submitJclJob(ZosConnection connection, String jclString) throws Exception {
        JobSubmit jobSubmit = new JobSubmit(connection);
        return jobSubmit.submitByJcl(jclString, null, null);
    }

    /**
     * Example on how to call JobSubmit submit method.
     * The submit method is given a Dataset member value to use to submit it as a job.
     *
     * @param connection ZosConnection object
     * @param dsMember   dataset member value
     * @return job document
     * @throws Exception error in processing request
     * @author Frank Giordano
     */
    public static Job submitJob(ZosConnection connection, String dsMember) throws Exception {
        JobSubmit jobSubmit = new JobSubmit(connection);
        return jobSubmit.submit(dsMember);
    }

}
`````

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
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
    public static ZosConnection getSecureZosConnection() throws Exception {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZosConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````