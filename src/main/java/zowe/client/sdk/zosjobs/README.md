# z/OS Jobs Package

Contains APIs to interact with jobs on z/OS (using z/OSMF jobs REST endpoints).

APIs are located in the methods package.

## API Examples

**Cancel a job**

````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.JobModifyInputData;
import zowe.client.sdk.zosjobs.methods.JobCancel;
import zowe.client.sdk.zosjobs.model.Job;

/**
 * Class example to showcase JobCancel class functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 5.0
 */
public class JobCancelExp extends TstZosConnection {

    private static ZosConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobCancel functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        System.out.println(cancelCommonWithVersion("2.0"));
        System.out.println(cancelCommon());
        System.out.println(cancelByJob());
        System.out.println(cancel());
    }

    /**
     * Example on how to call JobCancel cancelCommon method.
     * The cancelCommon method accepts a JobModifyInputData object with parameters filled needed
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
        JobModifyInputData modifyInputData = new JobModifyInputData.Builder(jobName, jobId).version(version).build();
        try {
            return new JobCancel(connection).cancelCommon(modifyInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * Example on how to call JobCancel cancelCommon method.
     * The cancelCommon method accepts a JobModifyInputData object with parameters filled needed
     * to cancel a given job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response cancelCommon() {
        jobId = "xxx";
        jobName = "xxx";
        JobModifyInputData modifyInputData = new JobModifyInputData.Builder(jobName, jobId).build();
        try {
            return new JobCancel(connection).cancelCommon(modifyInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            Job job = new Job(Job.builder().jobId(jobId).jobName("jobName").build());
            return new JobCancel(connection).cancelByJob(job, null);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

}
`````

**Delete a job**

`````java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.JobModifyInputData;
import zowe.client.sdk.zosjobs.methods.JobDelete;
import zowe.client.sdk.zosjobs.model.Job;

/**
 * Class example to showcase JobDelete class functionality.
 *
 * @author Leonid Baranov
 * @author Frank Giordano
 * @version 5.0
 */
public class JobDeleteExp extends TstZosConnection {

    private static ZosConnection connection;
    private static String jobName;
    private static String jobId;

    /**
     * The main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobDelete functionality.
     *
     * @param args for main not used
     * @author Leonid Baranov
     */
    public static void main(String[] args) {
        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        System.out.println(deleteCommonWithVersion("2.0"));
        System.out.println(deleteCommon());
        System.out.println(deleteByJob());
        System.out.println(deleteJob());
    }

    /**
     * Example on how to call JobDelete deleteCommon method.
     * The deleteCommon method accepts a JobModifyInputData object with parameters filled needed
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
        JobModifyInputData jobModifyInputData = new JobModifyInputData.Builder(jobName, jobId).version(version).build();
        try {
            return new JobDelete(connection).deleteCommon(jobModifyInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * Example on how to call JobDelete deleteCommon method.
     * The deleteCommon method accepts a JobModifyInputData object with parameters
     * filled needed to delete a given job.
     *
     * @return response object
     * @author Frank Giordano
     */
    public static Response deleteCommon() {
        jobId = "xxx";
        jobName = "xxx";
        JobModifyInputData jobModifyInputData = new JobModifyInputData.Builder(jobName, jobId).build();
        try {
            return new JobDelete(connection).deleteCommon(jobModifyInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            Job job = new Job(Job.builder().jobId(jobId).jobName("jobName").build());
            return new JobDelete(connection).deleteByJob(job, null);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

}
`````

**Get a job**

```java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.CommonJobInputData;
import zowe.client.sdk.zosjobs.input.JobGetInputData;
import zowe.client.sdk.zosjobs.methods.JobGet;
import zowe.client.sdk.zosjobs.model.Job;
import zowe.client.sdk.zosjobs.model.JobFile;

import java.util.Arrays;
import java.util.List;

/**
 * Class example to showcase JobGet class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class JobGetExp extends TstZosConnection {

    private static JobGet jobGet;

    /**
     * The Main method defines z/OSMF host and user connection and other parameters needed to showcase
     * JobGet class functionality.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        String prefix = "xxx";
        String owner = "xxx";
        String jobId = "xxx";

        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        jobGet = new JobGet(connection);

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
     * The getJclCommon method is given CommonJobInputData object filled with information on the given job to
     * use for retrieval of its JCL content.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getJclCommon(String prefix) {
        try {
            List<Job> jobs = jobGet.getByPrefix(prefix);
            String jobId = jobs.get(0).getJobId();
            String jobName = jobs.get(0).getJobName();
            CommonJobInputData commonJobInputData = new CommonJobInputData(jobId, jobName);
            String response = jobGet.getJclCommon(commonJobInputData);
            System.out.println(response);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            List<Job> jobs = jobGet.getByPrefix(prefix);
            System.out.println(jobGet.getJclByJob(jobs.get(0)));
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            List<Job> jobs = jobGet.getByPrefix(prefix);
            String jobName = jobs.get(0).getJobName();
            String jobId = jobs.get(0).getJobId();
            System.out.println(jobGet.getJcl(jobName, jobId));
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            List<Job> jobs = jobGet.getByPrefix(prefix);
            Job job = jobGet.getStatusByJob(jobs.get(0));
            System.out.println(job);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * Example on how to call JobGet getStatusCommon method with StepData flag.
     * The getStatusCommon method is given a jobName and jobId value to use for retrieval
     * of its status with the StepData flag set to true.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getStatusCommon(String prefix) {
        try {
            List<Job> jobs = jobGet.getByPrefix(prefix);
            String jobId = jobs.get(0).getJobId();
            String jobName = jobs.get(0).getJobName();
            CommonJobInputData commonJobInputData = new CommonJobInputData(jobId, jobName, true);
            Job job = jobGet.getStatusCommon(commonJobInputData);
            System.out.println(job.toString());
            Arrays.stream(job.getStepData()).forEach(System.out::println);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            List<Job> jobs = jobGet.getByPrefix(prefix);
            String jobName = jobs.get(0).getJobName();
            String jobId = jobs.get(0).getJobId();
            Job job = jobGet.getStatus(jobName, jobId);
            System.out.println(job.toString());
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            jobGet.getById(jobId);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            List<Job> jobs = jobGet.getByPrefix(prefix);
            String jobId = jobs.get(0).getJobId();
            Job job = jobGet.getById(jobId);
            System.out.println(job.toString());
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

    /**
     * Example on how to call JobGet getByOwnerAndPrefix method.
     * The getByOwnerAndPrefix method is given an owner and prefix value that will return a
     * list of common job documents / objects.
     *
     * @param owner  owner value
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getByOwnerAndPrefix(String owner, String prefix) {
        List<Job> jobs;
        try {
            jobs = jobGet.getByOwnerAndPrefix(owner, prefix);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        jobs.forEach(System.out::println);
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
            jobs = jobGet.getByPrefix(prefix);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        jobs.forEach(System.out::println);
    }

    /**
     * Example on how to call JobGet getAll method.
     * The getAll method returns a list of all jobs available for the logged-in user.
     *
     * @author Frank Giordano
     */
    public static void getAll() {
        // get any jobs out there for the logged-in user or the user specified within connection
        List<Job> jobs;
        try {
            jobs = jobGet.getAll();
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        jobs.forEach(System.out::println);
    }

    /**
     * Example on how to call JobGet getSpoolContent method.
     * The getSpoolContent method is given a job spool file name to retrieve its content.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getSpoolContent(String prefix) {
        JobGetInputData getInputData = new JobGetInputData.Builder("*").prefix(prefix).build();
        String[] output;
        try {
            List<Job> jobs = jobGet.getCommon(getInputData);
            List<JobFile> files = jobGet.getSpoolFilesByJob(jobs.get(0));
            output = jobGet.getSpoolContent(files.get(0)).split("\n");
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        // get last 10 lines of output
        for (int i = output.length - 10; i < output.length; i++) {
            System.out.println(output[i]);
        }
    }

    /**
     * Example on how to call JobGet getByOwner method.
     * The getByOwner method is given an owner value to use to retrieve a list of its available jobs.
     *
     * @param owner owner value
     * @author Frank Giordano
     */
    public static void getByOwner(String owner) {
        List<Job> jobs;
        try {
            jobs = jobGet.getByOwner(owner);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        jobs.forEach(System.out::println);
    }

    /**
     * Example on how to call JobGet getSpoolFilesByJob method.
     * The getSpoolFilesByJob method is given a job document/object retrieve a list of all its spool names.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getSpoolFilesForJob(String prefix) {
        JobGetInputData jobGetInputData = new JobGetInputData.Builder("*").prefix(prefix).build();
        List<JobFile> files;
        try {
            List<Job> jobs = jobGet.getCommon(jobGetInputData);
            files = jobGet.getSpoolFilesByJob(jobs.get(0));
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        files.forEach(System.out::println);
    }

    /**
     * Example on how to call JobGet getSpoolFiles method.
     * The getSpoolFiles method is given a jobName and jobId values to retrieve a list of all its spool file names.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getSpoolFiles(String prefix) {
        JobGetInputData jobGetInputData = new JobGetInputData.Builder("*").prefix(prefix).build();
        List<JobFile> files;
        try {
            List<Job> jobs = jobGet.getCommon(jobGetInputData);
            String jobName = jobs.get(0).getJobName();
            String jobId = jobs.get(0).getJobId();
            files = jobGet.getSpoolFiles(jobName, jobId);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        files.forEach(System.out::println);
    }

    /**
     * Example on how to call JobGet getCommon method.
     * The getCommon method is given a JobGetInputData object filled with search parameters which will
     * retrieve a list of all jobs.
     *
     * @param prefix partial or full job name to use for searching
     * @author Frank Giordano
     */
    public static void getCommon(String prefix) {
        JobGetInputData jobGetInputData = new JobGetInputData.Builder("*").prefix(prefix).build();
        List<Job> jobs;
        try {
            jobs = jobGet.getCommon(jobGetInputData);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        jobs.forEach(System.out::println);
    }

}
```

**Monitor a job**

```java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.JobMonitorInputData;
import zowe.client.sdk.zosjobs.methods.JobMonitor;
import zowe.client.sdk.zosjobs.methods.JobSubmit;
import zowe.client.sdk.zosjobs.model.Job;
import zowe.client.sdk.zosjobs.types.JobStatus;

/**
 * Class example to showcase JobMonitor class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class JobMonitorExp extends TstZosConnection {

    private static JobSubmit jobSubmit;
    private static ZosConnection connection;

    /**
     * The main method defines z/OSMF host and user connection needed to showcase
     * JobMonitor functionality.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        jobSubmit = new JobSubmit(connection);
        JobMonitorExp.monitorJobForOutputStatusByJobObject();
        JobMonitorExp.monitorJobForOutputStatusByJobNameAndId();
        JobMonitorExp.monitorJobForStatusByJobObject(JobStatus.Type.INPUT);
        JobMonitorExp.monitorJobForStatusByJobNameAndId(JobStatus.Type.ACTIVE);
        JobMonitorExp.monitorWaitForJobMessage("xxx");
        monitorIsJobRunning();
    }

    /**
     * Example on how to call JobMonitor isJobRunning method.
     * The isJobRunning method accepts a JobMonitorInputData object.
     *
     * @author Frank Giordano
     */
    public static void monitorIsJobRunning() {
        JobMonitor jobMonitor = new JobMonitor(connection);
        JobMonitorInputData jobMonitorInputData = new JobMonitorInputData.Builder("XXX", "XXX").build();
        try {
            System.out.println(jobMonitor.isRunning(jobMonitorInputData));
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
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
            job = jobSubmit.submitByJcl(jclString, null, null);
            JobMonitor jobMonitor = new JobMonitor(connection);
            job = jobMonitor.waitByOutputStatus(job);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        System.out.println(job.getStatus());
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
            job = jobSubmit.submitByJcl(jclString, null, null);
            JobMonitor jobMonitor = new JobMonitor(connection);
            String jobName = job.getJobName();
            String jobId = job.getJobId();
            job = jobMonitor.waitByOutputStatus(jobName, jobId);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        System.out.println(job.getStatus());
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
        // define an existing job in your system that is in execute queue
        Job job = new Job(Job.builder().jobId("xxx").jobName("xxx").build());
        JobMonitor jobMonitor = new JobMonitor(connection);
        try {
            job = jobMonitor.waitByStatus(job, statusType);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        System.out.println(job.getStatus());
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
        // define an existing job in your system that is in execute queue
        Job job = new Job(Job.builder().jobId("xxx").jobName("xxx").build());
        JobMonitor jobMonitor = new JobMonitor(connection);
        try {
            String jobName = job.getJobName();
            String jobId = job.getJobId();
            job = jobMonitor.waitByStatus(jobName, jobId, statusType);
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
        System.out.println(job.getStatus());
    }

    /**
     * Example on how to call JobMonitor waitByMessage method.
     * The waitByMessage method accepts a message value which will monitor the job output until
     * the message is seen or times out if not seen.
     *
     * @param message given a message text to monitor job output
     * @author Frank Giordano
     */
    public static void monitorWaitForJobMessage(String message) {
        // define an existing job in your system that is in execute queue
        Job job = new Job(Job.builder().jobId("xxx").jobName("xxx").build());
        JobMonitor jobMonitor = new JobMonitor(connection);
        try {
            System.out.println("Found message = " + jobMonitor.waitByMessage(job, message));
        } catch (ZosmfRequestException e) {
            String errMsg = (String) e.getResponse().getResponsePhrase().orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }
    }

}
```

**Submit a job**

```java
package zowe.client.sdk.examples.zosjobs;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.examples.TstZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.methods.JobMonitor;
import zowe.client.sdk.zosjobs.methods.JobSubmit;
import zowe.client.sdk.zosjobs.model.Job;
import zowe.client.sdk.zosjobs.types.JobStatus;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Class example to showcase JobSubmit class functionality.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class JobSubmitExp extends TstZosConnection {

    /**
     * The main method defines z/OSMF connection and showcases the JobSubmit class functionality.
     *
     * @param args for main not used
     * @author Frank Giordano
     */
    public static void main(String[] args) {
        ZosConnection connection = ZosConnectionFactory.createBasicConnection(hostName, zosmfPort, userName, password);
        try {
            // Submit a Job from PDS member
            Job job = JobSubmitExp.submitJob(connection, "ccsqa.asm.jcl.insthelp(receive)");
            System.out.println("submitJob output: \n" + job);

            // Submit a Job from text representing a JCL job
            String jclString = "//TESTJOBX JOB (105200000),MSGCLASS=H\n// EXEC PGM=IEFBR14";
            job = JobSubmitExp.submitJclJob(connection, jclString);

            // Wait for the job to complete
            JobMonitor jobMonitor = new JobMonitor(connection);
            job = jobMonitor.waitByStatus(job, JobStatus.Type.OUTPUT);
            System.out.println("submitJclJob output: \n" + job);

            // Get the return code
            String retCode = job.getRetCode();
            System.out.println("Expected Return Code for submitJclJob = CC 0000");
            System.out.println("The return code is [" + retCode + "]");

            // Submit a Job from local file
            String filePath = "C:\\ZosShell\\CCSQA.ASM.JCL.INSTHELP\\RECEIVE";
            job = JobSubmitExp.submitByLocalFile(connection, filePath);
            System.out.println("submitByLocalFile output: \n" + job);
        } catch (ZosmfRequestException e) {
            // Safely extracts the response phrase as a string, ensuring it is neither null nor blank nor empty JSON object;
            // otherwise, falls back to the exception's default message.
            Predicate<String> isNotBlankStr = s -> !s.isBlank();
            Predicate<String> isNotEmptyJson = s -> !s.equals("{}");
            final String errMsg = Optional.ofNullable(e.getResponse())
                    .flatMap(Response::getResponsePhrase)
                    .map(Object::toString)
                    .filter(isNotBlankStr.and(isNotEmptyJson))
                    .orElse(e.getMessage());
            throw new RuntimeException(errMsg);
        }

    }

    /**
     * Example on how to call JobSubmit submitByJcl method.
     * The submitByJcl method is given a jcl string to use to submit it as a job.
     *
     * @param connection ZosConnection object
     * @param jclString  jcl formatted string
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public static Job submitJclJob(ZosConnection connection, String jclString) throws ZosmfRequestException {
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
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public static Job submitJob(ZosConnection connection, String dsMember) throws ZosmfRequestException {
        JobSubmit jobSubmit = new JobSubmit(connection);
        return jobSubmit.submit(dsMember);
    }

    /**
     * Submits the contents of a local JCL file.
     *
     * @param connection ZosConnection object
     * @param filePath   path to the local file where the JCL is located
     * @return job document
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public static Job submitByLocalFile(ZosConnection connection, String filePath) throws ZosmfRequestException {
        JobSubmit jobSubmit = new JobSubmit(connection);
        return jobSubmit.submitByLocalFile(filePath);
    }

}
```

**Connection setup**

````java
package zowe.client.sdk.examples;

import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.teamconfig.TeamConfig;
import zowe.client.sdk.teamconfig.exception.TeamConfigException;
import zowe.client.sdk.teamconfig.model.ProfileDao;

/**
 * Base class with connection member static variables for use by examples to provide a means of a shortcut to avoid
 * duplicating connection details in each example.
 *
 * @author Frank Giordano
 * @version 5.0
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
        TeamConfig teamConfig = new TeamConfig();
        ProfileDao profile = teamConfig.getDefaultProfile("zosmf");
        return (ZosConnectionFactory.createBasicConnection(
                profile.getHost(), profile.getPort(), profile.getUser(), profile.getPassword()));
    }

}
`````
