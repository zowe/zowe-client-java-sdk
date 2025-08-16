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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.parse.JsonParseFactory;
import zowe.client.sdk.parse.type.ParseType;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.EncodeUtils;
import zowe.client.sdk.utility.JsonParserUtil;
import zowe.client.sdk.utility.ValidateUtils;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.CommonJobInput;
import zowe.client.sdk.zosjobs.input.JobGet.JobInput;
import zowe.client.sdk.zosjobs.response.JobDocument;
import zowe.client.sdk.zosjobs.response.SpoolDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle getting a job and started task information
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobGet {

    private final ZosConnection connection;

    private ZosmfRequest request;

    private String url;

    /**
     * GetJobs Constructor.
     *
     * @param connection for connection information, see ZosConnection object
     * @author Frank Giordano
     */
    public JobGet(final ZosConnection connection) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        this.connection = connection;
    }

    /**
     * Alternative GetJobs constructor with ZoweRequest object. This is mainly used for internal code unit testing
     * with mockito, and it is not recommended to be used by the larger community.
     * <p>
     * This constructor is package-private
     *
     * @param connection for connection information, see ZosConnection object
     * @param request    any compatible ZoweRequest Interface object
     * @author Frank Giordano
     */
    JobGet(final ZosConnection connection, final ZosmfRequest request) {
        ValidateUtils.checkNullParameter(connection == null, "connection is null");
        ValidateUtils.checkNullParameter(request == null, "request is null");
        this.connection = connection;
        this.request = request;
        if (!(request instanceof GetJsonZosmfRequest) && !(request instanceof GetTextZosmfRequest)) {
            throw new IllegalStateException("GET_JSON or GET_TEXT request type required");
        }
    }

    /**
     * Get JCL from a job.
     *
     * @param jobName job name for the job for which you want to retrieve JCL
     * @param jobId   job ID for the job for which you want to retrieve JCL
     * @return job document on resolve
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String getJcl(final String jobName, final String jobId) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(jobName, "jobName");
        ValidateUtils.checkIllegalParameter(jobId, "jobId");
        return getJclCommon(new CommonJobInput(jobId, jobName));
    }

    /**
     * Get JCL from a job.
     * Alternate version of the API that accepts a Job object returned by
     * other APIs such as SubmitJobs.
     *
     * @param job job for which you would like to retrieve JCL
     * @return JCL content
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String getJclByJob(final JobDocument job) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return getJclCommon(new CommonJobInput(job.getJobId().orElse(""), job.getJobName().orElse("")));
    }

    /**
     * Get the JCL that was used to submit a job.
     *
     * @param params for common job parameters, see CommonJobParams object
     * @return JCL content
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") // due to ValidateUtils done in CommonJobParams
    public String getJclCommon(final CommonJobInput params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        url = connection.getZosmfUrl() + JobsConstants.RESOURCE + "/" +
                EncodeUtils.encodeURIComponent(params.getJobName().get()) + "/" + params.getJobId().get() +
                JobsConstants.RESOURCE_SPOOL_FILES + JobsConstants.RESOURCE_JCL_CONTENT + JobsConstants.RESOURCE_SPOOL_CONTENT;

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
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public JobDocument getById(final String jobId) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(jobId, "jobId");
        final List<JobDocument> jobs = getCommon(new JobInput.Builder("*").jobId(jobId).build());
        if (jobs.isEmpty()) {
            throw new IllegalStateException("job not found");
        }
        if (jobs.size() > 1) {
            throw new IllegalStateException("expected 1 job returned but received " + jobs.size() + " jobs.");
        }
        return jobs.get(0);
    }

    /**
     * Get jobs (defaults to the user ID of the session as an owner).
     *
     * @return list of job objects (matching jobs), without step-data
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<JobDocument> getAll() throws ZosmfRequestException {
        return getCommon(null);
    }

    /**
     * Get jobs that are owned by a certain user or pattern of users.
     *
     * @param owner owner for which to get jobs. Supports wildcard e.g.,
     *              IBMU* returns jobs owned by all users whose ID beings with "IBMU"
     * @return list of job objects (matching jobs), without step-data
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<JobDocument> getByOwner(final String owner) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(owner, "owner");
        return getCommon(new JobInput.Builder(owner).build());
    }

    /**
     * Get a list of jobs that match an owner and prefix.
     *
     * @param owner  owner for which to get jobs. Supports wildcard e.g.,
     *               IBMU* returns jobs owned by all users whose ID beings with "IBMU"
     * @param prefix prefix for which to get jobs. Supports wildcard e.g.,
     *               JOBNM* returns jobs with names starting with "JOBNM"
     * @return list of job objects (matching jobs), without step-data
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<JobDocument> getByOwnerAndPrefix(final String owner, final String prefix) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(owner, "owner");
        ValidateUtils.checkIllegalParameter(prefix, "prefix");
        return getCommon(new JobInput.Builder(owner).prefix(prefix).build());
    }

    /**
     * Get jobs that match a job name by prefix. Defaults to job(s) owned by the user ID in the session.
     *
     * @param prefix job name prefix for which to list jobs. Supports wildcard e.g., JOBNM*
     * @return list of job objects (matching jobs), without step-data
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<JobDocument> getByPrefix(final String prefix) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(prefix, "prefix");
        return getCommon(new JobInput.Builder("*").prefix(prefix).build());
    }

    /**
     * Get jobs filtered by owner and prefix.
     *
     * @param params to get job parameters, see GetJobParams object
     * @return list of job objects (matching jobs), without step-data
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<JobDocument> getCommon(final JobInput params) throws ZosmfRequestException {
        List<JobDocument> jobs = new ArrayList<>();

        url = connection.getZosmfUrl() +
                JobsConstants.RESOURCE + QueryConstants.QUERY_ID;

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
            // if no user defined in ZosConnection then query jobs by owner=*
            if (connection.getUser() != null && !connection.getUser().isEmpty()) {
                url += JobsConstants.QUERY_OWNER + connection.getUser();
            }
        }

        if (request == null || !(request instanceof GetJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String jsonStr = request.executeRequest().getResponsePhrase()
                .orElseThrow(() -> new IllegalStateException("no get job response phrase")).toString();
        final JSONArray results = JsonParserUtil.parseArray(jsonStr);
        for (final Object jsonObj : results) {
            jobs.add((JobDocument) JsonParseFactory.buildParser(ParseType.JOB).parseResponse(jsonObj));
        }

        return jobs;
    }

    /**
     * Get spool content from a job (keeping a naming convention pattern with this duplication function).
     *
     * @param jobFile spool file for which you want to retrieve the content
     * @return spool content
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String getSpoolContent(final SpoolDocument jobFile) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(jobFile == null, "jobFile is null");
        return getSpoolContentCommon(jobFile);
    }

    /**
     * Get spool content from a job using the job name, job ID, and spool ID number.
     *
     * @param jobName job name for the job containing the spool content
     * @param jobId   job id for the job containing the spool content
     * @param spoolId id number assigned by zosmf that identifies the particular job spool file (DD)
     * @return spool content
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") // due to ValidateUtils done in CommonJobParams
    public String getSpoolContent(final String jobName, final String jobId, final int spoolId)
            throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(jobName, "jobName");
        ValidateUtils.checkIllegalParameter(jobId, "jobId");
        ValidateUtils.checkIllegalParameter(spoolId <= 0, "spool id not specified");

        // use CommonJobParams container class that does all the ValidateUtils checks
        final CommonJobInput params = new CommonJobInput(jobId, jobName);
        url = connection.getZosmfUrl() +
                JobsConstants.RESOURCE + "/" + EncodeUtils.encodeURIComponent(params.getJobName().get()) + "/" +
                params.getJobId().get() + JobsConstants.RESOURCE_SPOOL_FILES + "/" + spoolId +
                JobsConstants.RESOURCE_SPOOL_CONTENT;

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
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String getSpoolContentCommon(final SpoolDocument jobFile) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(jobFile == null, "jobFile is null");

        if (jobFile.getJobName().isEmpty() || jobFile.getJobId().isEmpty() || jobFile.getId().isEmpty()) {
            throw new ZosmfRequestException("jobFileName, JobId or jobFileId is either null or empty");
        }

        url = connection.getZosmfUrl() +
                JobsConstants.RESOURCE + "/" + EncodeUtils.encodeURIComponent(jobFile.getJobName().get()) + "/" +
                jobFile.getJobId().get() + JobsConstants.RESOURCE_SPOOL_FILES + "/" + jobFile.getId().getAsLong() +
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
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<SpoolDocument> getSpoolFiles(final String jobName, final String jobId) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(jobName, "jobName");
        ValidateUtils.checkIllegalParameter(jobId, "jobId");
        return getSpoolFilesCommon(new CommonJobInput(jobId, jobName));
    }

    /**
     * Get a list of all job spool files for a job.
     *
     * @param params for common job parameters, see CommonJobParams object
     * @return list of JobFile objects
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") // due to ValidateUtils done in CommonJobParams
    public List<SpoolDocument> getSpoolFilesCommon(final CommonJobInput params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        url = connection.getZosmfUrl() +
                JobsConstants.RESOURCE + "/" + EncodeUtils.encodeURIComponent(params.getJobName().get()) + "/" +
                params.getJobId().get() + "/files";

        if (request == null || !(request instanceof GetJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final List<SpoolDocument> files = new ArrayList<>();
        final String jsonStr = request.executeRequest().getResponsePhrase().orElse("").toString();
        if (jsonStr.isBlank()) {
            return files;
        }

        final JSONArray results = JsonParserUtil.parseArray(jsonStr);
        for (final Object obj : results) {
            final JSONObject jsonObj = (JSONObject) obj;
            files.add((SpoolDocument) JsonParseFactory.buildParser(ParseType.JOB_FILE).parseResponse(jsonObj));
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
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public List<SpoolDocument> getSpoolFilesByJob(final JobDocument job) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return getSpoolFilesCommon(new CommonJobInput(
                job.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ILLEGAL_MSG)),
                job.getJobName().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ILLEGAL_MSG))));
    }

    /**
     * Get the status and other details (e.g., owner, return code) for a job, including step-data.
     *
     * @param jobName job name for the job for which you want to get status
     * @param jobId   job ID for the job for which you want to get status
     * @return job document (matching job)
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public JobDocument getStatus(final String jobName, final String jobId) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(jobName, "jobName");
        ValidateUtils.checkIllegalParameter(jobId, "jobId");
        return getStatusCommon(new CommonJobInput(jobId, jobName, true));
    }

    /**
     * Get the status and other details (e.g., owner, return code) for a job, including step-data.
     *
     * @param params for common job parameters, see CommonJobParams object
     * @return job document (matching job)
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent") // due to ValidateUtils done in CommonJobParams
    public JobDocument getStatusCommon(final CommonJobInput params) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(params == null, "params is null");

        url = connection.getZosmfUrl() +
                JobsConstants.RESOURCE + "/" + EncodeUtils.encodeURIComponent(params.getJobName().get()) + "/"
                + params.getJobId().get();

        if (params.isStepData()) {
            url += JobsConstants.QUERY_ID + JobsConstants.STEP_DATA;
        }

        if (request == null || !(request instanceof GetJsonZosmfRequest)) {
            request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        }
        request.setUrl(url);

        final String jsonStr = request.executeRequest()
                .getResponsePhrase().orElseThrow(() -> new IllegalStateException("no job get response phrase"))
                .toString();
        final JSONObject jsonObject = JsonParserUtil.parse(jsonStr);
        return (JobDocument) JsonParseFactory.buildParser(ParseType.JOB).parseResponse(jsonObject);
    }

    /**
     * Get the status and other details (e.g., owner, return code) for a job, including step-data.
     * Alternate version of the API that accepts a Job object returned by
     * other APIs such as SubmitJobs. Even though the parameter and return
     * value are of the same type, the Job object returned will have the
     * current status of the job and will contain step-data.
     *
     * @param job job document
     * @return job document (matching job)
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public JobDocument getStatusByJob(final JobDocument job) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        return getStatusCommon(new CommonJobInput(
                job.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ILLEGAL_MSG)),
                job.getJobName().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ILLEGAL_MSG)),
                true));
    }

    /**
     * Get the status value only for a given job name and id.
     *
     * @param jobName job name for the job for which you want to get status
     * @param jobId   job ID for the job for which you want to get status
     * @return status value
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String getStatusValue(final String jobName, final String jobId) throws ZosmfRequestException {
        ValidateUtils.checkIllegalParameter(jobName, "jobName");
        ValidateUtils.checkIllegalParameter(jobId, "jobId");
        final JobDocument job = getStatusCommon(new CommonJobInput(jobId, jobName));
        return job.getStatus().orElseThrow(() -> new IllegalStateException("job status not returned"));
    }

    /**
     * Get the status value for a given job object.
     *
     * @param job job document
     * @return status value
     * @throws ZosmfRequestException request error state
     * @author Frank Giordano
     */
    public String getStatusValueByJob(final JobDocument job) throws ZosmfRequestException {
        ValidateUtils.checkNullParameter(job == null, "job is null");
        final JobDocument result = getStatusCommon(new CommonJobInput(
                job.getJobId().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_ID_ILLEGAL_MSG)),
                job.getJobName().orElseThrow(() -> new IllegalArgumentException(JobsConstants.JOB_NAME_ILLEGAL_MSG))));
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

