/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs;

import core.ZOSConnection;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.*;
import utility.Util;
import utility.UtilJobs;
import utility.UtilRest;
import zosjobs.input.CommonJobParms;
import zosjobs.input.GetJobParms;
import zosjobs.input.JobFile;
import zosjobs.response.Job;

public class GetJobs {

    private static final Logger LOG = LogManager.getLogger(GetJobs.class);

    private ZOSConnection connection;
    private ZoweRequest request;
    private String url;

    /**
     * Get jobs Constructor.
     *
     * @param connection ZOSConnection object
     * @author Frank Giordano
     * @memberof GetJobs
     */
    public GetJobs(ZOSConnection connection) {
        this.connection = connection;
    }

    /**
     * Get jobs (defaults to the user ID of the session as owner).
     *
     * @author Frank Giordano
     * @returns A list of job objects (matching jobs)
     * @memberof GetJobs
     */
    public List<Job> getJobs() throws Exception {
        return getJobsCommon(null);
    }

    /**
     * Get jobs that match a job name by prefix. Defaults to jobs owned by the user ID in the session.
     *
     * @param prefix Job name prefix for which to list jobs. Supports wildcard e.g. JOBNM*
     * @author Frank Giordano
     * @returns A list of job objects (matching jobs)
     * @memberof GetJobs
     */
    public List<Job> getJobsByPrefix(String prefix) throws Exception {
        Util.checkNullParameter(prefix == null, "prefix is null");
        Util.checkStateParameter(prefix.isEmpty(), "prefix not specified");

        return getJobsCommon(new GetJobParms.Builder().owner("*").prefix(prefix).build());
    }

    /**
     * Get jobs that are owned by a certain user or pattern of users.
     *
     * @param owner Owner for which to get jobs. Supports wildcard e.g. IBMU* returns jobs owned by all users whose ID beings with "IBMU"
     * @author Frank Giordano
     * @returns A list of job objects (matching jobs)
     * @memberof GetJobs
     */
    public List<Job> getJobsByOwner(String owner) throws Exception {
        Util.checkNullParameter(owner == null, "owner is null");
        Util.checkStateParameter(owner.isEmpty(), "owner not specified");

        return getJobsCommon(new GetJobParms.Builder().owner(owner).build());
    }

    /**
     * Get a list of jobs that match an owner and prefix.
     *
     * @param owner  Owner for which to get jobs. Supports wildcard e.g. IBMU* returns jobs owned by all users whose ID beings with "IBMU"
     * @param prefix Prefix for which to get jobs. Supports wildcard e.g. JOBNM* returns jobs with names starting with "JOBNM"
     * @author Frank Giordano
     * @returns A list of job objects (matching jobs)
     * @memberof GetJobs
     */
    public List<Job> getJobsByOwnerAndPrefix(String owner, String prefix) throws Exception {
        Util.checkNullParameter(owner == null, "owner is null");
        Util.checkStateParameter(owner.isEmpty(), "owner not specified");
        Util.checkNullParameter(prefix == null, "prefix is null");
        Util.checkStateParameter(prefix.isEmpty(), "prefix not specified");

        return getJobsCommon(new GetJobParms.Builder().owner(owner).prefix(prefix).build());
    }

    /**
     * Get a single job object from an input job id.
     *
     * @param jobId The job ID for the job for which you want to get status
     * @author Frank Giordano
     * @returns A list of job objects (matching jobs)
     * @memberof GetJobs
     */
    public Job getJob(String jobId) throws Exception {
        Util.checkNullParameter(jobId == null, "jobId is null");
        Util.checkStateParameter(jobId.isEmpty(), "jobId not specified");

        List<Job> jobs = getJobsCommon(new GetJobParms.Builder().owner("*").jobId(jobId).build());
        if (jobs.isEmpty()) throw new Exception("Job not found");
        if (jobs.size() > 1) throw new Exception("Expected 1 job returned but received " + jobs.size() + " jobs.");

        return jobs.get(0);
    }

    /**
     * Get jobs filtered by owner and prefix.
     *
     * @param parms GetJobParms object
     * @author Frank Giordano
     * @returns A list of job objects (matching jobs)
     * @memberof GetJobs
     */
    public List<Job> getJobsCommon(GetJobParms parms) throws Exception {
        Util.checkConnection(connection);

        List<Job> jobs = new ArrayList<>();
        url = "https://" + connection.getHost() + ":" + connection.getPort()
                + JobsConstants.RESOURCE + QueryConstants.QUERY_ID;

        if (parms != null) {
            if (parms.getOwner().isPresent()) {
                url += JobsConstants.QUERY_OWNER + parms.getOwner().get();
            }
            if (parms.getPrefix().isPresent()) {
                if (parms.getPrefix().get() != JobsConstants.DEFAULT_PREFIX) {
                    if (url.contains(QueryConstants.QUERY_ID)) {
                        url += QueryConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_PREFIX + parms.getPrefix().get();
                }
            }
            if (parms.getMaxJobs().isPresent()) {
                if (parms.getMaxJobs().get() != JobsConstants.DEFAULT_MAX_JOBS) {
                    if (url.contains(QueryConstants.QUERY_ID)) {
                        url += QueryConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_MAX_JOBS + parms.getMaxJobs().get();
                }
            }
            if (parms.getJobId().isPresent()) {
                if (url.contains(QueryConstants.QUERY_ID)) {
                    url += QueryConstants.COMBO_ID;
                }
                url += JobsConstants.QUERY_JOBID + parms.getJobId().get();
            }
        } else {
            url += JobsConstants.QUERY_OWNER + connection.getUser();
        }

        LOG.debug(url);

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.RequestType.GET_JSON);
        } else {
            request.setRequest(url);
        }

        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return jobs;
        UtilRest.checkHttpErrors(response);
        JSONArray results = (JSONArray) response.getResponsePhrase().orElse(null);
        if (results == null)
            return jobs;

        results.forEach(item -> {
            JSONObject jobObj = (JSONObject) item;
            jobs.add(UtilJobs.createJobObjFromJson(jobObj));
        });

        return jobs;
    }

    /**
     * Get the status and other details (e.g. owner, return code) for a job.
     *
     * @param jobName The job name for the job for which you want to get status
     * @param jobId   The job ID for the job for which you want to get status
     * @author Frank Giordano
     * @returns Job object (matching job)
     * @memberof GetJobs
     */
    public Job getStatus(String jobName, String jobId) throws Exception {
        Util.checkNullParameter(jobName == null, "jobName is null");
        Util.checkNullParameter(jobId == null, "jobId is null");

        return getStatusCommon(new CommonJobParms(jobId, jobName));
    }

    /**
     * Get the status and other details (e.g. owner, return code) for a job
     * Alternate version of the API that accepts an Job object returned by
     * other APIs such as SubmitJobs. Even though the parameter and return
     * value are of the same type, the Job object returned will have the
     * current status of the job.
     *
     * @param job Job object
     * @author Frank Giordano
     * @returns Job object (matching job)
     * @memberof GetJobs
     */
    public Job getStatusForJob(Job job) throws Exception {
        Util.checkNullParameter(job == null, "job is null");

        return getStatusCommon(new CommonJobParms(job.getJobId().isPresent() ?
                job.getJobId().get() : null, job.getJobName().isPresent() ? job.getJobName().get() : null));
    }

    /**
     * Get the status and other details (e.g. owner, return code) for a job.
     *
     * @param parms CommonJobParms object
     * @author Frank Giordano
     * @returns Job object (matching job)
     * @memberof GetJobs
     */
    public Job getStatusCommon(CommonJobParms parms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobId not specified");
        Util.checkStateParameter(!parms.getJobName().isPresent(), "jobName not specified");

        url = "https://" + connection.getHost() + ":" + connection.getPort()
                + JobsConstants.RESOURCE + "/" + parms.getJobName().get() + "/" + parms.getJobId().get();

        LOG.debug(url);

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.RequestType.GET_JSON);
        } else {
            request.setRequest(url);
        }
        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return new Job.Builder().build();
        UtilRest.checkHttpErrors(response);
        JSONObject result = (JSONObject) response.getResponsePhrase().orElse(null);
        if (result == null)
            return new Job.Builder().build();

        return UtilJobs.createJobObjFromJson(result);
    }

    /**
     * Get a list of all spool files for a job.
     *
     * @param jobName The job name for the job for which you want to get a list of spool files
     * @param jobId   The job ID for the job for which you want to get a list of spool files
     * @author Frank Giordano
     * @returns A list JobFile objects
     * @memberof GetJobs
     */
    public List<JobFile> getSpoolFiles(String jobName, String jobId)
            throws Exception {
        return getSpoolFilesCommon(new CommonJobParms(jobId, jobName));
    }

    /**
     * Get a list of all job spool files for a job.
     * Alternate version of the API that accepts an Job object returned by
     * other APIs such as SubmitJobs.
     *
     * @param job The job for which you would like to get a list of job spool files
     * @author Frank Giordano
     * @returns A list JobFile objects
     * @memberof GetJobs
     */
    public List<JobFile> getSpoolFilesForJob(Job job) throws Exception {
        return getSpoolFilesCommon(new CommonJobParms(job.getJobId().get(), job.getJobName().get()));
    }

    /**
     * Get a list of all job spool files for a job.
     *
     * @param parms CommonJobParms object
     * @author Frank Giordano
     * @returns A list JobFile objectsl
     * @memberof GetJobs
     */
    public List<JobFile> getSpoolFilesCommon(CommonJobParms parms)
            throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(parms == null, "prams is null");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobId not defined");
        Util.checkStateParameter(!parms.getJobName().isPresent(), "jobName not defined");

        List<JobFile> files = new ArrayList<>();

        url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE
                + "/" + parms.getJobName().get() + "/" + parms.getJobId().get() + "/files";

        LOG.debug(url);

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.RequestType.GET_JSON);
        } else {
            request.setRequest(url);
        }

        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return files;
        UtilRest.checkHttpErrors(response);
        JSONArray results = (JSONArray) response.getResponsePhrase().orElse(null);
        if (results == null)
            return files;

        results.forEach(item -> {
            JSONObject fileObj = (JSONObject) item;
            files.add(new JobFile.Builder().jobId((String) fileObj.get("jobid"))
                    .jobName((String) fileObj.get("jobname"))
                    .recfm((String) fileObj.get("recfm"))
                    .byteCount((Long) fileObj.get("byteCount"))
                    .recordCount((Long) fileObj.get("recordCount"))
                    .jobCorrelator((String) fileObj.get("job-correlator"))
                    .classs((String) fileObj.get("class"))
                    .id((Long) fileObj.get("id"))
                    .ddName((String) fileObj.get("ddname"))
                    .recordsUrl((String) fileObj.get("records-url"))
                    .lrecl((Long) fileObj.get("lrecl"))
                    .subSystem((String) fileObj.get("subsystem"))
                    .stepName((String) fileObj.get("stepname"))
                    .procStep((String) fileObj.get("procstep"))
                    .build());
        });

        return files;
    }

    /**
     * Get JCL from a job.
     *
     * @param jobName The job name for the job for which you want to retrieve JCL
     * @param jobId   The job ID for the job for which you want to retrieve JCL
     * @author Frank Giordano
     * @returns A string representing job document on resolve
     * @memberof GetJobs
     */
    public String getJcl(String jobName, String jobId) throws Exception {
        return getJclCommon(new CommonJobParms(jobId, jobName));
    }

    /**
     * Get JCL from a job.
     * Alternate version of the API that accepts an Job object returned by
     * other APIs such as SubmitJobs.
     *
     * @param job The job for which you would like to retrieve JCL
     * @author Frank Giordano
     * @returns A string representing the JCL content
     * @memberof GetJobs
     */
    public String getJclForJob(Job job) throws Exception {
        return getJclCommon(new CommonJobParms(job.getJobId().isPresent() ? job.getJobId().get() : null,
                job.getJobName().isPresent() ? job.getJobName().get() : null));
    }

    /**
     * Get the JCL that was used to submit a job.
     *
     * @param parms CommonJobParms object
     * @author Frank Giordano
     * @returns A string representing the JCL content
     * @memberof GetJobs
     */
    public String getJclCommon(CommonJobParms parms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(!parms.getJobName().isPresent(), "jobName not specified");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobId not specified");

        url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE + "/" +
                parms.getJobName().get() + "/" + parms.getJobId().get() + JobsConstants.RESOURCE_SPOOL_FILES +
                JobsConstants.RESOURCE_JCL_CONTENT + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        if (request == null || !(request instanceof TextGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.RequestType.GET_TEXT);
        } else {
            request.setRequest(url);
        }

        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return "";
        UtilRest.checkHttpErrors(response);
        return (String) response.getResponsePhrase().orElse("");
    }

    /**
     * Get spool content from a job (keeping naming convention patter with this duplication function).
     *
     * @param jobFile The spool file for which you want to retrieve the content
     * @author Frank Giordano
     * @returns A string representing the spool content
     * @memberof GetJobs
     */
    public String getSpoolContent(JobFile jobFile) throws Exception {
        return getSpoolContentCommon(jobFile);
    }

    /**
     * Get spool content from a job using the job name, job ID, and spool ID number from z/OSMF.
     *
     * @param jobName The job name for the job containing the spool content
     * @param jobId   The job id for the job containing the spool content
     * @param spoolId The Id number assigned by zosmf that identifies the particular job spool file (DD)
     * @author Frank Giordano
     * @returns A string representing the spool content
     * @memberof GetJobs
     */
    public String getSpoolContentById(String jobName, String jobId, int spoolId)
            throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(jobName == null, "jobName is null");
        Util.checkNullParameter(jobId == null, "jobId is null");
        Util.checkStateParameter(spoolId <= 0, "spoolId not specified");

        url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE + "/" +
                jobName + "/" + jobId + JobsConstants.RESOURCE_SPOOL_FILES + "/" +
                spoolId + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        if (request == null || !(request instanceof TextGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.RequestType.GET_TEXT);
        } else {
            request.setRequest(url);
        }
        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return "";
        UtilRest.checkHttpErrors(response);
        return (String) response.getResponsePhrase().orElse("");
    }

    /**
     * Get spool content from a job.
     *
     * @param jobFile The spool file for which you want to retrieve the content
     * @author Frank Giordano
     * @returns A string representing the spool content
     * @memberof GetJobs
     */
    public String getSpoolContentCommon(JobFile jobFile) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(jobFile == null, "jobFile is null");
        Util.checkStateParameter(!jobFile.getJobName().isPresent(), "jobName not specified");
        Util.checkStateParameter(!jobFile.getJobId().isPresent(), "jobId not specified");
        Util.checkStateParameter(!jobFile.getId().isPresent(), "id not specified");

        url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE + "/" +
                jobFile.getJobName().get() + "/" + jobFile.getJobId().get() + JobsConstants.RESOURCE_SPOOL_FILES + "/" +
                jobFile.getId().get() + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        if (request == null || !(request instanceof TextGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, url, null, ZoweRequestType.RequestType.GET_TEXT);
        } else {
            request.setRequest(url);
        }
        Response response = request.executeHttpRequest();
        if (response.isEmpty())
            return "";
        UtilRest.checkHttpErrors(response);
        return (String) response.getResponsePhrase().orElse("");
    }

    /**
     * Get url specified for rest processing.
     *
     * @author Frank Giordano
     * @returns A string representing a url
     * @memberof GetJobs
     */
    public String getUrl() {
        return url;
    }

}
