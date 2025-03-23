# z/OS Jobs Package

Contains APIs to interact with jobs on z/OS (using z/OSMF jobs REST endpoints).

APIs located in methods package.

## API Examples

**Cancel a job**

````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.methods.JobCancel;
import zowe.client.sdk.zosjobs.response.Job;

/**
 * Class example to showcase JobCancel class functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 3.0
 */
public class JobCancelExp extends TstZosConnection {

    private static ZosConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobCancel functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
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
     * @return response object
     * @author Frank Giordano
     */
    public static Response cancelCommonWithVersion(String version) {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        try {
            return new JobCancel(connection).cancelCommon(params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobCancel cancelCommon method.
     * The cancelCommon method accepts a CancelJobParams object with parameters filled needed
     * to cancel a given job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response cancelCommon() {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        try {
            return new JobCancel(connection).cancelCommon(params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobCancel cancelByJob method.
     * The cancelByJob method accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response cancelByJob() {
        jobId = "xxx";
        jobName = "xxx";
        try {
            return new JobCancel(connection)
                    .cancelByJob(new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobCancel cancel method.
     * The cancel method accepts a jobName and jobId values which will be used to cancel its job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response cancel() {
        jobId = "xxx";
        jobName = "xxx";
        try {
            return new JobCancel(connection).cancel(jobName, jobId, null);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

}
`````

**Delete a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.ModifyJobParams;
import zowe.client.sdk.zosjobs.methods.JobDelete;
import zowe.client.sdk.zosjobs.response.Job;

/**
 * Class example to showcase JobDelete class functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 3.0
 */
public class JobDeleteExp extends TstZosConnection {

    private static ZosConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobDelete functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
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
     * @return response object
     * @author Frank Giordano
     */
    public static Response deleteCommonWithVersion(String version) {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).version(version).build();
        try {
            return new JobDelete(connection).deleteCommon(params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobDelete deleteCommon method.
     * The deleteCommon method accepts a DeleteJobParams object with parameters
     * filled needed to delete a given job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response deleteCommon() {
        jobId = "xxx";
        jobName = "xxx";
        ModifyJobParams params = new ModifyJobParams.Builder(jobName, jobId).build();
        try {
            return new JobDelete(connection).deleteCommon(params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobDelete deleteByJob method.
     * The deleteByJob method accepts a jobName and jobId values which will be used to delete its job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response deleteByJob() {
        jobId = "xxx";
        jobName = "xxx";
        try {
            return new JobDelete(connection).deleteByJob(
                    new Job.Builder().jobName(jobName).jobId(jobId).build(), null);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobDelete delete method.
     * The delete method accepts a jobName and jobId values which will be used to delete its job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response deleteJob() {
        jobId = "xxx";
        jobName = "xxx";
        try {
            return new JobDelete(connection).delete(jobName, jobId, null);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

}
`````

**Get a job**

```java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
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
 * @version 3.0
 */
public class JobGetExp extends TstZosConnection {

    private static JobGet getJob;

    /**
     * Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobGet class functionality.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String prefix = "xxx";
        String owner = "xxx";
        String jobId = "xxx";

        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
        getJob = new JobGet(connection);

        JobGetExp.getCommon(prefix);
        JobGetExp.getSpoolFiles(prefix);
        JobGetExp.getSpoolFilesForJob(prefix);
        JobGetExp.getByOwner(owner);
        JobGetExp.getSpoolContent(prefix);
        JobGetExp.getAll();
        JobGetExp.getByPrefix(prefix);
        JobGetExp.getByOwnerAndPrefix("*", prefix);
        JobGetExp.getById(prefix);
        JobGetExp.nonExistentGetJob(jobId);
        JobGetExp.getStatusCommon(prefix);
        JobGetExp.getStatus(prefix);
        JobGetExp.getStatusByJob(prefix);
        JobGetExp.getJcl(prefix);
        JobGetExp.getJclByJob(prefix);
        JobGetExp.getJclCommon(prefix);
    }

    /**
     * Example on how to call JobGet getJclCommon method.
     * The getJclCommon method is given CommonJobParams object filled with information on the given job to
     * use for retrieval of its JCL content.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getJclCommon(String prefix) {
        try {
            List<Job> jobs = getJob.getByPrefix(prefix);
            System.out.println(getJob.getJclCommon(
                    new CommonJobParams(jobs.get(0).getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified")),
                            jobs.get(0).getJobName().orElseThrow(() -> new ZosmfRequestException("job name not specified")))));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobGet getJclByJob method.
     * The getJclByJob method is given a job object to use for retrieval of its JCL content.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getJclByJob(String prefix) {
        try {
            List<Job> jobs = getJob.getByPrefix(prefix);
            System.out.println(getJob.getJclByJob(jobs.get(0)));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobGet getJcl method.
     * The getJcl method is given a jobName and jobId values to use for retrieval of its JCL content.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getJcl(String prefix) {
        try {
            List<Job> jobs = getJob.getByPrefix(prefix);
            System.out.println(
                    getJob.getJcl(jobs.get(0).getJobName().orElseThrow(() -> new ZosmfRequestException("job name not specified")),
                            jobs.get(0).getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified"))));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobGet getStatusByJob method.
     * The getStatusByJob method is given a job object to use for retrieval of its status.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getStatusByJob(String prefix) {
        try {
            List<Job> jobs = getJob.getByPrefix(prefix);
            Job job = getJob.getStatusByJob(jobs.get(0));
            System.out.println(job);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobGet getStatusCommon method with StepData flag.
     * The getStatusCommon method is given a jobName and jobId value to use for retrieval
     * of its status with StepData flag set to true.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getStatusCommon(String prefix) {
        try {
            List<Job> jobs = getJob.getByPrefix(prefix);
            Job job = getJob.getStatusCommon(
                    new CommonJobParams(jobs.get(0).getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified")),
                            jobs.get(0).getJobName().orElseThrow(() -> new ZosmfRequestException("job name not specified")), true));
            System.out.println(job.toString());
            Arrays.stream(job.getStepData().orElseThrow(() -> new ZosmfRequestException("no step data found"))).forEach(i -> System.out.println(i.toString()));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobGet getStatus method.
     * The getStatus method is given a jobName and jobId value to use for retrieval of its status.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getStatus(String prefix) {
        try {
            List<Job> jobs = getJob.getByPrefix(prefix);
            Job job = getJob.getStatus(
                    jobs.get(0).getJobName().orElseThrow(() -> new ZosmfRequestException("job name not specified")),
                    jobs.get(0).getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified")));
            System.out.println(job.toString());
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
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
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobGet getById method.
     * The getById method is given a jobId value which will return a job document/object.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getById(String prefix) {
        try {
            List<Job> jobs = getJob.getByPrefix(prefix);
            String jobId = jobs.get(0).getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified"));
            Job job = getJob.getById(jobId);
            System.out.println(job.toString());
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobGet getByOwnerAndPrefix method.
     * The getByOwnerAndPrefix method is given an owner and prefix values which will return a
     * list of common job document/object.
     *
     * @param owner  owner value
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getByOwnerAndPrefix(String owner, String prefix) {
        List<Job> jobs;
        try {
            jobs = getJob.getByOwnerAndPrefix(owner, prefix);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getByPrefix method.
     * The getByPrefix method is given a prefix value which will return a list of common job document/object.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getByPrefix(String prefix) {
        List<Job> jobs;
        try {
            jobs = getJob.getByPrefix(prefix);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getAll method.
     * The getAll method returns a list of all jobs available for the logged-in user.
     *
     * @author Frank Giordano
     */
    public static void getAll() {
        // get any jobs out there for the logged-in user
        List<Job> jobs;
        try {
            jobs = getJob.getAll();
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getSpoolContent method.
     * The getSpoolContent method is given a job spool file name to retrieve its content.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getSpoolContent(String prefix) {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        String[] output;
        try {
            List<Job> jobs = getJob.getCommon(params);
            List<JobFile> files = getJob.getSpoolFilesByJob(jobs.get(0));
            output = getJob.getSpoolContent(files.get(0)).split("\n");
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        // get last 10 lines of output
        for (int i = output.length - 10; i < output.length; i++) {
            System.out.println(output[i]);
        }
    }

    /**
     * Example on how to call JobGet getByOwner method.
     * The getByOwner method is given an owner value to use retrieve a list of its available jobs.
     *
     * @param owner owner value
     * @author Frank Giordano
     */
    public static void getByOwner(String owner) {
        List<Job> jobs;
        try {
            jobs = getJob.getByOwner(owner);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        jobs.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getSpoolFilesByJob method.
     * The getSpoolFilesByJob method is given a job document/object retrieve a list of all its spool names.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getSpoolFilesForJob(String prefix) {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<JobFile> files = null;
        try {
            List<Job> jobs = getJob.getCommon(params);
            files = getJob.getSpoolFilesByJob(jobs.get(0));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        files.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getSpoolFiles method.
     * The getSpoolFiles method is given a jobName and jobId values to retrieve a list of all its spool file names.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getSpoolFiles(String prefix) {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<JobFile> files;
        try {
            List<Job> jobs = getJob.getCommon(params);
            files = getJob.getSpoolFiles(
                    jobs.get(0).getJobName().orElseThrow(() -> new ZosmfRequestException("job name not specified")),
                    jobs.get(0).getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified")));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        files.forEach(i -> System.out.println(i.toString()));
    }

    /**
     * Example on how to call JobGet getCommon method.
     * The getCommon method is given a GetJobParams object filled with search parameters which will
     * retrieve a list of all jobs.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getCommon(String prefix) {
        GetJobParams params = new GetJobParams.Builder("*").prefix(prefix).build();
        List<Job> jobs;
        try {
            jobs = getJob.getCommon(params);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        jobs.forEach(i -> System.out.println(i.toString()));
    }

}
```

**Monitor a job**

```java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.MonitorJobWaitForParams;
import zowe.client.sdk.zosjobs.methods.JobMonitor;
import zowe.client.sdk.zosjobs.methods.JobSubmit;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.types.JobStatus;

/**
 * Class example to showcase JobMonitor class functionality.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class JobMonitorExp extends TstZosConnection {

    private static JobSubmit submitJob;
    private static ZosConnection connection;

    /**
     * Main method defines z/OSMF host and user connection needed to showcase
     * JobMonitor functionality.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        connection = new ZosConnection(hostName, zosmfPort, userName, password);
        submitJob = new JobSubmit(connection);
        JobMonitorExp.monitorJobForOutputStatusByJobObject();
        JobMonitorExp.monitorJobForOutputStatusByJobNameAndId();
        JobMonitorExp.monitorJobForStatusByJobObject(JobStatus.Type.INPUT);
        JobMonitorExp.monitorJobForStatusByJobNameAndId(JobStatus.Type.ACTIVE);
        JobMonitorExp.monitorWaitForJobMessage("xxx");
        monitorIsJobRunning();
    }

    /**
     * Example on how to call JobMonitor isJobRunning method.
     * The isJobRunning method accepts a MonitorJobWaitForParams object.
     *
     * @author Frank Giordano
     */
    public static void monitorIsJobRunning() {
        JobMonitor jobMonitor = new JobMonitor(connection);
        MonitorJobWaitForParams monitorParams = new MonitorJobWaitForParams.Builder("XXX", "XXX").build();
        try {
            System.out.println(jobMonitor.isRunning(monitorParams));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobMonitor waitByOutputStatus method.
     * The waitByOutputStatus method accepts a job object which will monitor the job status
     * until it reaches OUTPUT status or times out if not reached.
     *
     * @author Frank Giordano
     */
    public static void monitorJobForOutputStatusByJobObject() {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job;
        try {
            job = submitJob.submitByJcl(jclString, null, null);
            JobMonitor jobMonitor = new JobMonitor(connection);
            job = jobMonitor.waitByOutputStatus(job);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByOutputStatus method.
     * The waitByOutputStatus method accepts a jobName and jobId values which will
     * monitor the job status until it reaches OUTPUT status or times out if not reached.
     *
     * @author Frank Giordano
     */
    public static void monitorJobForOutputStatusByJobNameAndId() {
        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\r // EXEC PGM=IEFBR14";
        Job job;
        try {
            job = submitJob.submitByJcl(jclString, null, null);
            JobMonitor jobMonitor = new JobMonitor(connection);
            job = jobMonitor.waitByOutputStatus(
                    job.getJobName().orElseThrow(() -> new ZosmfRequestException("job name not specified")),
                    job.getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified")));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByStatus method.
     * The waitByStatus method accepts a job object and status value which will monitor the
     * job status until it reaches the given status value or times out if not reached.
     *
     * @param statusType given status type to use for monitoring
     * @author Frank Giordano
     */
    public static void monitorJobForStatusByJobObject(JobStatus.Type statusType) {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        JobMonitor jobMonitor = new JobMonitor(connection);
        try {
            job = jobMonitor.waitByStatus(job, statusType);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByStatus method.
     * The waitByStatus method accepts a jobName and jobId values and status value which will monitor the
     * job status until it reaches the given status value or times out if not reached.
     *
     * @param statusType given status type to use for monitoring
     * @author Frank Giordano
     */
    public static void monitorJobForStatusByJobNameAndId(JobStatus.Type statusType) {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        JobMonitor jobMonitor = new JobMonitor(connection);
        try {
            job = jobMonitor.waitByStatus(
                    job.getJobName().orElseThrow(() -> new ZosmfRequestException("job name not specified")),
                    job.getJobId().orElseThrow(() -> new ZosmfRequestException("job id not specified")), statusType);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
        System.out.println("Job status for Job " + job.getJobName().orElse("n/a") + ":" +
                job.getJobId().orElse("n/a") + " is " + job.getStatus().orElse("n/a"));
    }

    /**
     * Example on how to call JobMonitor waitByMessage method.
     * The waitByMessage method accepts a message value which will monitor the job output until
     * the message is seen or times out if not seen.
     *
     * @param message given message text to monitor job output
     * @author Frank Giordano
     */
    public static void monitorWaitForJobMessage(String message) {
        // determine an existing job in your system that is in execute queue and make a Job for it
        Job job = new Job.Builder().jobName("xxx").jobId("xxx").build();
        JobMonitor jobMonitor = new JobMonitor(connection);
        try {
            System.out.println("Found message = " + jobMonitor.waitByMessage(job, message));
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

}
```

**Submit a job**

````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.examples.utility.Util;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.methods.JobMonitor;
import zowe.client.sdk.zosjobs.methods.JobSubmit;
import zowe.client.sdk.zosjobs.response.Job;
import zowe.client.sdk.zosjobs.types.JobStatus;

/**
 * Class example to showcase JobSubmit class functionality.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class JobSubmitExp extends TstZosConnection {

    /**
     * Main method defines z/OSMF host and user connection needed to showcase
     * JobSubmit functionality.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        ZosConnection connection = new ZosConnection(hostName, zosmfPort, userName, password);
        System.out.println(JobSubmitExp.submitJob(connection, "xxx.xxx.xxx.xxx(xxx)"));

        String jclString = "//TESTJOBX JOB (),MSGCLASS=H\n// EXEC PGM=IEFBR14";
        Job submitJobsTest = JobSubmitExp.submitJclJob(connection, jclString);
        // Wait for the job to complete
        JobMonitor jobMonitor = new JobMonitor(connection);
        try {
            submitJobsTest = jobMonitor.waitByStatus(submitJobsTest, JobStatus.Type.OUTPUT);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
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
     * @author Frank Giordano
     */
    public static Job submitJclJob(ZosConnection connection, String jclString) {
        JobSubmit jobSubmit = new JobSubmit(connection);
        try {
            return jobSubmit.submitByJcl(jclString, null, null);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

    /**
     * Example on how to call JobSubmit submit method.
     * The submit method is given a Dataset member value to use to submit it as a job.
     *
     * @param connection ZosConnection object
     * @param dsMember   dataset member value
     * @return job document
     * @author Frank Giordano
     */
    public static Job submitJob(ZosConnection connection, String dsMember) {
        JobSubmit jobSubmit = new JobSubmit(connection);
        try {
            return jobSubmit.submit(dsMember);
        } catch (ZosmfRequestException e) {
            final String errMsg = Util.getResponsePhrase(e.getResponse());
            throw new RuntimeException((errMsg != null ? errMsg : e.getMessage()));
        }
    }

}
`````

````java
package zowe.client.sdk.examples.utility;

import zowe.client.sdk.rest.Response;

/**
 * Utility class containing helper method(s).
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class Util {

    /**
     * Extract response phrase string value if any from Response object.
     *
     * @param response object
     * @return string value
     * @author Frank Giordano
     */
    public static String getResponsePhrase(Response response) {
        if (response == null || response.getResponsePhrase().isEmpty()) {
            return null;
        }
        return response.getResponsePhrase().get().toString();
    }

}
`````

**Connection setup**

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.keytar.KeyTarImpl;
import zowe.client.sdk.teamconfig.model.ProfileDao;
import zowe.client.sdk.teamconfig.service.KeyTarService;
import zowe.client.sdk.teamconfig.service.TeamConfigService;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class TstZosConnection {

    // replace "xxx" with hard coded values to execute the examples in this project
    public static final String hostName = "xxx";
    public static final String zosmfPort = "xxx";
    public static final String userName = "xxx";
    public static final String password = "xxx";

    // or use the following method to retrieve Zowe OS credential store for your
    // secure Zowe V2 credentials you entered when you initially set up Zowe Global Team Configuration.
    public static ZosConnection getSecureZosConnection() throws TeamConfigException {
        TeamConfig teamConfig = new TeamConfig(new KeyTarService(new KeyTarImpl()), new TeamConfigService());
        ProfileDao profile = teamConfig.getDefaultProfileByName("zosmf");
        return (new ZosConnection(profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
