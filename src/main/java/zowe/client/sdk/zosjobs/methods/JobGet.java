/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs.methods;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.type.ZoweRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JobUtils;
import zowe.client.sdk.utility.RestUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.utility.unirest.UniRestUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.CommonJobParams;
import zowe.client.sdk.zosjobs.input.GetJobParams;
import zowe.client.sdk.zosjobs.input.JobFile;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle obtaining of z/OS batch job information
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class JobGet {

    private static final Logger LOG = LoggerFactory.getLogger(JobGet.class);
    private final ZOSConnection connection;
    private ZoweRequest request;
    private String url;

    /**
     * GetJobs Constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public JobGet(ZOSConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative GetJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface type object
     * @author Frank Giordano
     */
    public JobGet(ZOSConnection connection, ZoweRequest request) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
        this.request = request;
    }

    /**
     * Get JCL from a job.
     *
     * @param jobName job name for the job for which you want to retrieve JCL
     * @param jobId   job ID for the job for which you want to retrieve JCL
     * @return job document on resolve
     * @throws Exception error on getting jcl content
     * @author Frank Giordano
     */
    public String getJcl(String jobName, String jobId) throws Exception {
        return getJclCommon(new CommonJobParams(jobId, jobName));
    }

    /**
     * Get the JCL that was used to submit a job.
     *
     * @param params common job parameters, see CommonJobParams object
     * @return JCL content
     * @throws Exception error on getting jcl content
     * @author Frank Giordano
     */
    public String getJclCommon(CommonJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJobName().isEmpty(), "jobName not specified");
        ValidateUtils.checkIllegalParameter(params.getJobName().get().isEmpty(), "jobName not specified");
        ValidateUtils.checkIllegalParameter(params.getJobId().isEmpty(), "jobId not specified");
        ValidateUtils.checkIllegalParameter(params.getJobId().get().isEmpty(), "jobId not specified");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName().get()) + "/" + params.getJobId().get() +
                JobsConstants.RESOURCE_SPOOL_FILES + JobsConstants.RESOURCE_JCL_CONTENT +
                JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        if (request == null || !(request instanceof TextGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_TEXT);
        }
        request.setUrl(url);

        final Response response = UniRestUtils.getResponse(request);
        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return (String) response.getResponsePhrase().get();
    }

    /**
     * Get JCL from a job.
     * Alternate version of the API that accepts a Job object returned by
     * other APIs such as SubmitJobs.
     *
     * @param job job for which you would like to retrieve JCL
     * @return JCL content
     * @throws Exception error on getting jcl content
     * @author Frank Giordano
     */
    public String getJclByJob(Job job) throws Exception {
        return getJclCommon(new CommonJobParams(job.getJobId().orElse(null), job.getJobName().orElse(null)));
    }

    /**
     * Get a single job object from an input job id.
     *
     * @param jobId job ID for the job for which you want to get status
     * @return one job object, matching the given job ID, without step-data
     * @throws Exception error on getting job
     * @author Frank Giordano
     */
    public Job getById(String jobId) throws Exception {
        ValidateUtils.checkNullParameter(jobId == null, "jobId is null");
        ValidateUtils.checkIllegalParameter(jobId.isEmpty(), "jobId not specified");

        final List<Job> jobs = getCommon(new GetJobParams.Builder("*").jobId(jobId).build());
        if (jobs.isEmpty()) {
            throw new Exception("Job not found");
        }
        if (jobs.size() > 1) {
            throw new Exception("Expected 1 job returned but received " + jobs.size() + " jobs.");
        }

        return jobs.get(0);
    }

    /**
     * Get jobs (defaults to the user ID of the session as owner).
     *
     * @return list of job objects (matching jobs), without step-data
     * @throws Exception error on getting a list of jobs
     * @author Frank Giordano
     */
    public List<Job> getAll() throws Exception {
        return getCommon(null);
    }

    /**
     * Get jobs that are owned by a certain user or pattern of users.
     *
     * @param owner owner for which to get jobs. Supports wildcard e.g.
     *              IBMU* returns jobs owned by all users whose ID beings with "IBMU"
     * @return list of job objects (matching jobs), without step-data
     * @throws Exception error on getting a list of jobs
     * @author Frank Giordano
     */
    public List<Job> getByOwner(String owner) throws Exception {
        ValidateUtils.checkNullParameter(owner == null, "owner is null");
        ValidateUtils.checkIllegalParameter(owner.isEmpty(), "owner not specified");

        return getCommon(new GetJobParams.Builder(owner).build());
    }

    /**
     * Get a list of jobs that match an owner and prefix.
     *
     * @param owner  owner for which to get jobs. Supports wildcard e.g.
     *               IBMU* returns jobs owned by all users whose ID beings with "IBMU"
     * @param prefix prefix for which to get jobs. Supports wildcard e.g.
     *               JOBNM* returns jobs with names starting with "JOBNM"
     * @return list of job objects (matching jobs), without step-data
     * @throws Exception error on getting a list of jobs
     * @author Frank Giordano
     */
    public List<Job> getByOwnerAndPrefix(String owner, String prefix) throws Exception {
        ValidateUtils.checkNullParameter(owner == null, "owner is null");
        ValidateUtils.checkIllegalParameter(owner.isEmpty(), "owner not specified");
        ValidateUtils.checkNullParameter(prefix == null, "prefix is null");
        ValidateUtils.checkIllegalParameter(prefix.isEmpty(), "prefix not specified");
        return getCommon(new GetJobParams.Builder(owner).prefix(prefix).build());
    }

    /**
     * Get jobs that match a job name by prefix. Defaults to job(s) owned by the user ID in the session.
     *
     * @param prefix job name prefix for which to list jobs. Supports wildcard e.g. JOBNM*
     * @return list of job objects (matching jobs), without step-data
     * @throws Exception error on getting a list of jobs
     * @author Frank Giordano
     */
    public List<Job> getByPrefix(String prefix) throws Exception {
        ValidateUtils.checkNullParameter(prefix == null, "prefix is null");
        ValidateUtils.checkIllegalParameter(prefix.isEmpty(), "prefix not specified");
        return getCommon(new GetJobParams.Builder("*").prefix(prefix).build());
    }

    /**
     * Get jobs filtered by owner and prefix.
     *
     * @param params get job parameters, see GetJobParams object
     * @return list of job objects (matching jobs), without step-data
     * @throws Exception error on getting a list of jobs
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    public List<Job> getCommon(GetJobParams params) throws Exception {
        List<Job> jobs = new ArrayList<>();

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort()
                + JobsConstants.RESOURCE + QueryConstants.QUERY_ID;

        if (params != null) {
            if (params.getOwner().isPresent()) {
                url += JobsConstants.QUERY_OWNER + params.getOwner().get();
            }
            if (params.getPrefix().isPresent()) {
                if (!JobsConstants.DEFAULT_PREFIX.equals(params.getPrefix().get())) {
                    if (url.contains(QueryConstants.QUERY_ID)) {
                        url += QueryConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_PREFIX + EncodeUtils.encodeURIComponent(params.getPrefix().get());
                }
            }
            if (params.getMaxJobs().isPresent()) {
                if (params.getMaxJobs().getAsInt() != JobsConstants.DEFAULT_MAX_JOBS) {
                    if (url.contains(QueryConstants.QUERY_ID)) {
                        url += QueryConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_MAX_JOBS + params.getMaxJobs().getAsInt();
                }
            }
            if (params.getJobId().isPresent()) {
                if (url.contains(QueryConstants.QUERY_ID)) {
                    url += QueryConstants.COMBO_ID;
                }
                url += JobsConstants.QUERY_JOBID + params.getJobId().get();
            }
        } else {
            url += JobsConstants.QUERY_OWNER + connection.getUser();
        }

        LOG.debug(url);

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }
        request.setUrl(url);

        Response response;
        try {
            response = UniRestUtils.getResponse(request);
        } catch (Exception e) {
            LOG.debug("GetJobs::getJobsCommon - {}", e.getMessage());
            if (e.getMessage().contains("no response phrase returned")) {
                return jobs;
            }
            throw e;
        }

        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        final JSONArray results = (JSONArray) new JSONParser().parse(response.getResponsePhrase().get().toString());
        results.forEach(item -> {
            JSONObject jobObj = (JSONObject) item;
            jobs.add(JobUtils.parseJsonJobResponse(jobObj));
        });

        return jobs;
    }

    /**
     * Get spool content from a job (keeping naming convention patter with this duplication function).
     *
     * @param jobFile spool file for which you want to retrieve the content
     * @return spool content
     * @throws Exception error on getting spool content
     * @author Frank Giordano
     */
    public String getSpoolContent(JobFile jobFile) throws Exception {
        return getSpoolContentCommon(jobFile);
    }

    /**
     * Get spool content from a job using the job name, job ID, and spool ID number from z/OSMF.
     *
     * @param jobName job name for the job containing the spool content
     * @param jobId   job id for the job containing the spool content
     * @param spoolId id number assigned by zosmf that identifies the particular job spool file (DD)
     * @return spool content
     * @throws Exception error on getting spool content
     * @author Frank Giordano
     */
    public String getSpoolContent(String jobName, String jobId, int spoolId) throws Exception {
        ValidateUtils.checkNullParameter(jobName == null, "jobName is null");
        ValidateUtils.checkNullParameter(jobId == null, "jobId is null");
        ValidateUtils.checkIllegalParameter(spoolId <= 0, "spoolId not specified");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(jobName) + "/" + jobId + JobsConstants.RESOURCE_SPOOL_FILES + "/" +
                spoolId + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        if (request == null || !(request instanceof TextGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_TEXT);
        }
        request.setUrl(url);

        final Response response = UniRestUtils.getResponse(request);
        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return (String) response.getResponsePhrase().get();
    }

    /**
     * Get spool content from a job.
     *
     * @param jobFile spool file for which you want to retrieve the content
     * @return spool content
     * @throws Exception error on getting spool content
     * @author Frank Giordano
     */
    public String getSpoolContentCommon(JobFile jobFile) throws Exception {
        ValidateUtils.checkNullParameter(jobFile == null, "jobFile is null");
        ValidateUtils.checkIllegalParameter(jobFile.getJobName().isEmpty(), "jobName not specified");
        ValidateUtils.checkIllegalParameter(jobFile.getJobId().isEmpty(), "jobId not specified");
        ValidateUtils.checkIllegalParameter(jobFile.getId().isEmpty(), "id not specified");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(jobFile.getJobName().get()) + "/" + jobFile.getJobId().get() +
                JobsConstants.RESOURCE_SPOOL_FILES + "/" + jobFile.getId().get() + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        if (request == null || !(request instanceof TextGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_TEXT);
        }
        request.setUrl(url);

        final Response response = UniRestUtils.getResponse(request);
        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return (String) response.getResponsePhrase().get();
    }

    /**
     * Get a list of all spool files for a job.
     *
     * @param jobName job name for the job for which you want to get a list of spool files
     * @param jobId   job ID for the job for which you want to get a list of spool files
     * @return list of JobFile objects
     * @throws Exception error on getting spool files info
     * @author Frank Giordano
     */
    public List<JobFile> getSpoolFiles(String jobName, String jobId) throws Exception {
        return getSpoolFilesCommon(new CommonJobParams(jobId, jobName));
    }

    /**
     * Get a list of all job spool files for a job.
     *
     * @param params common job parameters, see CommonJobParams object
     * @return list of JobFile objects
     * @throws Exception error on getting spool files info
     * @author Frank Giordano
     */
    @SuppressWarnings("unchecked")
    public List<JobFile> getSpoolFilesCommon(CommonJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJobId().isEmpty(), "jobId not specified");
        ValidateUtils.checkIllegalParameter(params.getJobId().get().isEmpty(), "jobId not specified");
        ValidateUtils.checkIllegalParameter(params.getJobName().isEmpty(), "jobName not specified");
        ValidateUtils.checkIllegalParameter(params.getJobName().get().isEmpty(), "jobName not specified");

        List<JobFile> files = new ArrayList<>();

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName().get()) + "/" + params.getJobId().get() + "/files";

        LOG.debug(url);

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }
        request.setUrl(url);

        Response response;
        try {
            response = UniRestUtils.getResponse(request);
        } catch (Exception e) {
            LOG.debug("GetJobs::getSpoolFilesCommon - {}", e.getMessage());
            if (e.getMessage().contains("no response phrase returned")) {
                return files;
            }
            throw e;
        }

        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        final JSONArray results = (JSONArray) new JSONParser().parse(response.getResponsePhrase().get().toString());
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
     * Get a list of all job spool files for a job.
     * Alternate version of the API that accepts a Job object returned by
     * other APIs such as SubmitJobs.
     *
     * @param job job for which you would like to get a list of job spool files
     * @return list of JobFile objects
     * @throws Exception error on getting spool files info
     * @author Frank Giordano
     */
    public List<JobFile> getSpoolFilesByJob(Job job) throws Exception {
        return getSpoolFilesCommon(
                new CommonJobParams(job.getJobId().orElseThrow(() -> new Exception("job id not specified")),
                        job.getJobName().orElseThrow(() -> new Exception("job name not specified"))));
    }

    /**
     * Get the status and other details (e.g. owner, return code) for a job, including step-data.
     *
     * @param jobName job name for the job for which you want to get status
     * @param jobId   job ID for the job for which you want to get status
     * @return job document (matching job)
     * @throws Exception error getting job status
     * @author Frank Giordano
     */
    public Job getStatus(String jobName, String jobId) throws Exception {
        ValidateUtils.checkNullParameter(jobName == null, "jobName is null");
        ValidateUtils.checkNullParameter(jobId == null, "jobId is null");
        return getStatusCommon(new CommonJobParams(jobId, jobName, true));
    }

    /**
     * Get the status and other details (e.g. owner, return code) for a job, including step-data.
     *
     * @param params common job parameters, see CommonJobParams object
     * @return job document (matching job)
     * @throws Exception error getting job status
     * @author Frank Giordano
     */
    public Job getStatusCommon(CommonJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");
        ValidateUtils.checkIllegalParameter(params.getJobId().isEmpty(), "jobId not specified");
        ValidateUtils.checkIllegalParameter(params.getJobId().get().isEmpty(), "jobId not specified");
        ValidateUtils.checkIllegalParameter(params.getJobName().isEmpty(), "jobName not specified");
        ValidateUtils.checkIllegalParameter(params.getJobName().get().isEmpty(), "jobName not specified");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName().get()) + "/" + params.getJobId().get();

        if (params.isStepData()) {
            url += JobsConstants.QUERY_ID + JobsConstants.STEP_DATA;
        }

        LOG.debug(url);

        if (request == null || !(request instanceof JsonGetRequest)) {
            request = ZoweRequestFactory.buildRequest(connection, ZoweRequestType.GET_JSON);
        }
        request.setUrl(url);

        final Response response = UniRestUtils.getResponse(request);
        if (RestUtils.isHttpError(response.getStatusCode().get())) {
            throw new Exception(response.getResponsePhrase().get().toString());
        }

        return JobUtils.parseJsonJobResponse(
                ((JSONObject) new JSONParser().parse(response.getResponsePhrase().get().toString())));
    }

    /**
     * Get the status and other details (e.g. owner, return code) for a job, including step-data.
     * Alternate version of the API that accepts a Job object returned by
     * other APIs such as SubmitJobs. Even though the parameter and return
     * value are of the same type, the Job object returned will have the
     * current status of the job and will contain step-data.
     *
     * @param job job document
     * @return job document (matching job)
     * @throws Exception error getting job status
     * @author Frank Giordano
     */
    public Job getStatusByJob(Job job) throws Exception {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return getStatusCommon(new CommonJobParams(job.getJobId().orElse(null),
                job.getJobName().orElse(null), true));
    }

    /**
     * Get the status value only for a given job name and id.
     *
     * @param jobName job name for the job for which you want to get status
     * @param jobId   job ID for the job for which you want to get status
     * @return status value
     * @throws Exception error getting job status
     * @author Frank Giordano
     */
    public String getStatusValue(String jobName, String jobId) throws Exception {
        ValidateUtils.checkNullParameter(jobName == null, "jobName is null");
        ValidateUtils.checkNullParameter(jobId == null, "jobId is null");
        final Job job = getStatusCommon(new CommonJobParams(jobId, jobName));
        return job.getStatus().orElseThrow(() -> new Exception("job status is missing"));
    }

    /**
     * Get the status value for a given job object.
     *
     * @param job job document
     * @return status value
     * @throws Exception error getting job status
     * @author Frank Giordano
     */
    public String getStatusValueByJob(Job job) throws Exception {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        final Job result = getStatusCommon(
                new CommonJobParams(job.getJobId().orElse(null), job.getJobName().orElse(null)));
        return result.getStatus().orElseThrow(() -> new Exception("job status is missing"));
    }

    /**
     * Get url specified for rest processing.
     *
     * @return url
     * @author Frank Giordano
     */
    public String getUrl() {
        return url;
    }

}
