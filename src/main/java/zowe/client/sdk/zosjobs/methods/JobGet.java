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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.CommonJobParams;
import zowe.client.sdk.zosjobs.input.GetJobParams;
import zowe.client.sdk.zosjobs.input.JobFile;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle obtaining job and started task information
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class JobGet {

    private final ZosConnection connection;

    private ZosmfRequest request;

    private String url;

    /**
     * GetJobs Constructor.
     *
     * @param connection connection information, see ZOSConnection object
     * @author Frank Giordano
     */
    public JobGet(final ZosConnection connection) {
        ValidateUtils.checkConnection(connection);
        this.connection = connection;
    }

    /**
     * Alternative GetJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     *
     * @param connection connection information, see ZOSConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    public JobGet(final ZosConnection connection, final ZosmfRequest request) {
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
     * @author Frank Giordano
     */
    public String getJcl(final String jobName, final String jobId) {
        return getJclCommon(new CommonJobParams(jobId, jobName));
    }

    /**
     * Get JCL from a job.
     * Alternate version of the API that accepts a Job object returned by
     * other APIs such as SubmitJobs.
     *
     * @param job job for which you would like to retrieve JCL
     * @return JCL content
     * @author Frank Giordano
     */
    public String getJclByJob(final Job job) {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return getJclCommon(new CommonJobParams(job.getJobId().orElse(""), job.getJobName().orElse("")));
    }

    /**
     * Get the JCL that was used to submit a job.
     *
     * @param params common job parameters, see CommonJobParams object
     * @return JCL content
     * @author Frank Giordano
     */
    public String getJclCommon(final CommonJobParams params) {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName()
                        .orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ERROR_MSG))) + "/" +
                params.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ERROR_MSG)) +
                JobsConstants.RESOURCE_SPOOL_FILES + JobsConstants.RESOURCE_JCL_CONTENT +
                JobsConstants.RESOURCE_SPOOL_CONTENT;

        if (request == null || !(request instanceof GetTextZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_TEXT);
        }
        request.setUrl(url);

        return (String) request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job jcl response phrase"));
    }

    /**
     * Get a job by job id.
     *
     * @param jobId job ID for the job for which you want to get status
     * @return one job object, matching the given job ID, without step-data
     * @throws Exception error on getting job
     * @author Frank Giordano
     */
    public Job getById(final String jobId) throws Exception {
        final List<Job> jobs = getCommon(new GetJobParams.Builder("*").jobId(jobId).build());
        if (jobs.isEmpty()) {
            throw new IllegalStateException("job not found");
        }
        if (jobs.size() > 1) {
            throw new IllegalStateException("expected 1 job returned but received " + jobs.size() + " jobs.");
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
    public List<Job> getByOwner(final String owner) throws Exception {
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
    public List<Job> getByOwnerAndPrefix(final String owner, final String prefix) throws Exception {
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
    public List<Job> getByPrefix(final String prefix) throws Exception {
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
    public List<Job> getCommon(final GetJobParams params) throws Exception {
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

        if (request == null || !(request instanceof GetJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no get job response phrase")).toString();
        final JSONArray results = (JSONArray) new JSONParser().parse(jsonStr);
        for (final Object jsonObj : results) {
            jobs.add((Job) JsonParseFactory.buildParser(ParseType.JOB).parseResponse(jsonObj));
        }

        return jobs;
    }

    /**
     * Get spool content from a job (keeping naming convention pattern with this duplication function).
     *
     * @param jobFile spool file for which you want to retrieve the content
     * @return spool content
     * @author Frank Giordano
     */
    public String getSpoolContent(final JobFile jobFile) {
        return getSpoolContentCommon(jobFile);
    }

    /**
     * Get spool content from a job using the job name, job ID, and spool ID number.
     *
     * @param jobName job name for the job containing the spool content
     * @param jobId   job id for the job containing the spool content
     * @param spoolId id number assigned by zosmf that identifies the particular job spool file (DD)
     * @return spool content
     * @author Frank Giordano
     */
    public String getSpoolContent(final String jobName, final String jobId, final int spoolId) {
        ValidateUtils.checkIllegalParameter(spoolId <= 0, "spool id not specified");

        CommonJobParams params = new CommonJobParams(jobId, jobName);
        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName()
                        .orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ERROR_MSG))) + "/" +
                params.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ERROR_MSG)) +
                JobsConstants.RESOURCE_SPOOL_FILES + "/" + spoolId + JobsConstants.RESOURCE_SPOOL_CONTENT;

        if (request == null || !(request instanceof GetTextZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_TEXT);
        }
        request.setUrl(url);


        final String spoolErrMsg = "no job spool content response phrase";
        return (String) request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(spoolErrMsg));
    }

    /**
     * Get spool file content from a job file definition.
     *
     * @param jobFile spool file for which you want to retrieve the content
     * @return spool content
     * @author Frank Giordano
     */
    public String getSpoolContentCommon(final JobFile jobFile) {
        ValidateUtils.checkNullParameter(jobFile == null, "jobFile is null");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(jobFile.getJobName()
                        .orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ERROR_MSG))) + "/" +
                jobFile.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ERROR_MSG)) +
                JobsConstants.RESOURCE_SPOOL_FILES + "/" +
                jobFile.getId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_FILE_ID_ERROR_MSG)) +
                JobsConstants.RESOURCE_SPOOL_CONTENT;

        if (request == null || !(request instanceof GetTextZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_TEXT);
        }
        request.setUrl(url);

        final String spoolErrMsg = "no job spool file content response phrase";
        return (String) request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException(spoolErrMsg));
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
    public List<JobFile> getSpoolFiles(final String jobName, final String jobId) throws Exception {
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
    public List<JobFile> getSpoolFilesCommon(final CommonJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName()
                        .orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ERROR_MSG))) + "/" +
                params.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ERROR_MSG)) +
                "/files";

        if (request == null || !(request instanceof GetJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final List<JobFile> files = new ArrayList<>();
        final String jsonStr = request.executeRequest().getResponsePhrase().orElse("").toString();
        if (jsonStr.isBlank()) {
            return files;
        }

        final JSONArray results = (JSONArray) new JSONParser().parse(jsonStr);
        for (final Object obj : results) {
            final JSONObject jsonObj = (JSONObject) obj;
            files.add((JobFile) JsonParseFactory.buildParser(ParseType.JOB_FILE).parseResponse(jsonObj));
        }

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
    public List<JobFile> getSpoolFilesByJob(final Job job) throws Exception {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return getSpoolFilesCommon(new CommonJobParams(job.getJobId().orElse(""),
                job.getJobName().orElse("")));
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
    public Job getStatus(final String jobName, final String jobId) throws Exception {
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
    public Job getStatusCommon(final CommonJobParams params) throws Exception {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        url = "https://" + connection.getHost() + ":" + connection.getZosmfPort() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName()
                        .orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ERROR_MSG))) + "/" +
                params.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ERROR_MSG));

        if (params.isStepData()) {
            url += JobsConstants.QUERY_ID + JobsConstants.STEP_DATA;
        }

        if (request == null || !(request instanceof GetJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no job get response phrase")).toString();
        final JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonStr);
        return (Job) JsonParseFactory.buildParser(ParseType.JOB).parseResponse(jsonObject);
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
    public Job getStatusByJob(final Job job) throws Exception {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return getStatusCommon(new CommonJobParams(job.getJobId().orElse(""),
                job.getJobName().orElse(""), true));
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
    public String getStatusValue(final String jobName, final String jobId) throws Exception {
        final Job job = getStatusCommon(new CommonJobParams(jobId, jobName));
        return job.getStatus().orElseThrow(() -> new IllegalStateException("job status not returned"));
    }

    /**
     * Get the status value for a given job object.
     *
     * @param job job document
     * @return status value
     * @throws Exception error getting job status
     * @author Frank Giordano
     */
    public String getStatusValueByJob(final Job job) throws Exception {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        final Job result = getStatusCommon(new CommonJobParams(job.getJobId().orElse(""),
                job.getJobName().orElse("")));
        return result.getStatus().orElseThrow(() -> new IllegalStateException("job status not returned"));
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

