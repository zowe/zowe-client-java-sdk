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

import java.io.*;
import java.util.*;

import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import rest.IZoweRequest;
import rest.JsonRequest;
import rest.TextRequest;
import utility.Util;
import zosjobs.input.CommonJobParms;
import zosjobs.input.GetJobParms;
import zosjobs.input.JobFile;
import zosjobs.response.Job;

public class GetJobs {

    private static final Logger LOG = LogManager.getLogger(GetJobs.class);

    public static List<Job> getJobs(ZOSConnection connection) throws IOException {
        return GetJobs.getJobsCommon(connection, null);
    }

    public static List<Job> getJobsByPrefix(ZOSConnection connection, String prefix) throws IOException {
        Util.checkNullParameter(prefix == null, "prefix is null");
        Util.checkStateParameter(prefix.isEmpty(), "prefix not specified");

        return GetJobs.getJobsCommon(connection, new GetJobParms.Builder().owner("*").prefix(prefix).build());
    }

    public static List<Job> getJobsByOwner(ZOSConnection connection, String owner) throws IOException {
        Util.checkNullParameter(owner == null, "owner is null");
        Util.checkStateParameter(owner.isEmpty(), "owner not specified");

        return GetJobs.getJobsCommon(connection, new GetJobParms.Builder().owner(owner).build());
    }

    public static List<Job> getJobsByOwnerAndPrefix(ZOSConnection connection, String owner, String prefix) throws IOException {
        Util.checkNullParameter(owner == null, "owner is null");
        Util.checkStateParameter(owner.isEmpty(), "owner not specified");
        Util.checkNullParameter(prefix == null, "prefix is null");
        Util.checkStateParameter(prefix.isEmpty(), "prefix not specified");

        return GetJobs.getJobsCommon(connection, new GetJobParms.Builder().owner(owner).prefix(prefix).build());
    }

    public static Job getJob(ZOSConnection connection, String jobId) throws Exception {
        Util.checkNullParameter(jobId == null, "jobId is null");
        Util.checkStateParameter(jobId.isEmpty(), "jobId not specified");

        List<Job> jobs = getJobsCommon(connection, new GetJobParms.Builder().owner("*").jobId(jobId).build());
        if (jobs.isEmpty()) throw new Exception("Job not found");
        if (jobs.size() > 1) throw new Exception("Expected 1 job returned but received " + jobs.size() + " jobs.");

        return jobs.get(0);
    }

    public static List<Job> getJobsCommon(ZOSConnection connection, GetJobParms parms) throws IOException {
        Util.checkConnection(connection);

        List<Job> jobs = new ArrayList<>();
        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + JobsConstants.RESOURCE + JobsConstants.QUERY_ID;

        if (parms != null) {
            if (parms.getOwner().isPresent()) {
                url += JobsConstants.QUERY_OWNER + parms.getOwner().get();
            }
            if (parms.getPrefix().isPresent()) {
                if (parms.getPrefix().get() != JobsConstants.DEFAULT_PREFIX) {
                    if (url.contains(JobsConstants.QUERY_ID)) {
                        url += JobsConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_PREFIX + parms.getPrefix().get();
                }
            }
            if (parms.getMaxJobs().isPresent()) {
                if (parms.getMaxJobs().get() != JobsConstants.DEFAULT_MAX_JOBS) {
                    if (url.contains(JobsConstants.QUERY_ID)) {
                        url += JobsConstants.COMBO_ID;
                    }
                    url += JobsConstants.QUERY_MAX_JOBS + parms.getMaxJobs().get();
                }
            }
            if (parms.getJobId().isPresent()) {
                if (url.contains(JobsConstants.QUERY_ID)) {
                    url += JobsConstants.COMBO_ID;
                }
                url += JobsConstants.QUERY_JOBID + parms.getJobId().get();
            }
        } else {
            url += JobsConstants.QUERY_OWNER + connection.getUser();
        }

        LOG.debug(url);

        IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
        JSONArray results = request.httpGet();
        results.forEach(item -> {
            JSONObject jobObj = (JSONObject) item;
            jobs.add(Util.createJobObjFromJson(jobObj));
        });

        return jobs;
    }

    public static Job getStatus(ZOSConnection connection, String jobName, String jobId) throws Exception {
        Util.checkNullParameter(jobName == null, "jobName is null");
        Util.checkNullParameter(jobId == null, "jobId is null");

        return GetJobs.getStatusCommon(connection, new CommonJobParms(jobId, jobName));
    }

    public static Job getStatusForJob(ZOSConnection connection, Job job) throws Exception {
        Util.checkNullParameter(job == null, "job is null");

        return GetJobs.getStatusCommon(connection, new CommonJobParms(job.getJobId().isPresent() ?
                job.getJobId().get() : null, job.getJobName().isPresent() ? job.getJobName().get() : null));
    }

    public static Job getStatusCommon(ZOSConnection connection, CommonJobParms parms) throws Exception {
        Util.checkConnection(connection);
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobId not specified");
        Util.checkStateParameter(!parms.getJobName().isPresent(), "jobName not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort()
                + JobsConstants.RESOURCE + "/" + parms.getJobName().get() + "/" + parms.getJobId().get();

        LOG.debug(url);

        IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
        JSONObject result = request.httpGet();

        if (result == null) {
            throw new Exception("Job not found");
        }

        return Util.createJobObjFromJson(result);
    }

    public static List<JobFile> getSpoolFiles(ZOSConnection connection, String jobName, String jobId)
            throws IOException {
        return GetJobs.getSpoolFilesCommon(connection, new CommonJobParms(jobId, jobName));
    }

    public static List<JobFile> getSpoolFilesForJob(ZOSConnection connection, Job job) throws IOException {
        return GetJobs.getSpoolFilesCommon(connection,
                new CommonJobParms(job.getJobId().get(), job.getJobName().get()));
    }

    public static List<JobFile> getSpoolFilesCommon(ZOSConnection connection, CommonJobParms parms)
            throws IOException {
        Util.checkConnection(connection);
        Util.checkNullParameter(parms == null, "prams is null");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobId not defined");
        Util.checkStateParameter(!parms.getJobName().isPresent(), "jobName not defined");

        List<JobFile> files = new ArrayList<>();

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE
                + "/" + parms.getJobName().get() + "/" + parms.getJobId().get() + "/files";

        LOG.debug(url);

        IZoweRequest request = new JsonRequest(connection, new HttpGet(url));
        JSONArray results = request.httpGet();
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

    public static String getJcl(ZOSConnection connection, String jobName, String jobId) throws IOException {
        return GetJobs.getJclCommon(connection, new CommonJobParms(jobId, jobName));
    }

    public static String getJclForJob(ZOSConnection connection, Job job) throws IOException {
        return GetJobs.getJclCommon(connection, new CommonJobParms(
                job.getJobId().isPresent() ? job.getJobId().get() : null,
                job.getJobName().isPresent() ? job.getJobName().get() : null));
    }

    public static String getJclCommon(ZOSConnection connection, CommonJobParms parms) throws IOException {
        Util.checkConnection(connection);
        Util.checkNullParameter(parms == null, "parms is null");
        Util.checkStateParameter(!parms.getJobName().isPresent(), "jobName not specified");
        Util.checkStateParameter(!parms.getJobId().isPresent(), "jobId not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE + "/" +
                parms.getJobName().get() + "/" + parms.getJobId().get() + JobsConstants.RESOURCE_SPOOL_FILES +
                JobsConstants.RESOURCE_JCL_CONTENT + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        IZoweRequest request = new TextRequest(connection, new HttpGet(url));
        return request.httpGet();
    }

    public static String getSpoolContent(ZOSConnection connection, JobFile jobFile) throws IOException {
        return GetJobs.getSpoolContentCommon(connection, jobFile);
    }

    public static String getSpoolContentById(ZOSConnection connection, String jobName, String jobId, int spoolId)
            throws IOException {
        Util.checkConnection(connection);
        Util.checkNullParameter(jobName == null, "jobName is null");
        Util.checkNullParameter(jobId == null, "jobId is null");
        Util.checkStateParameter(!Optional.ofNullable(spoolId).isPresent(), "spoolId not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE + "/" +
                jobName + "/" + jobId + JobsConstants.RESOURCE_SPOOL_FILES + "/" +
                spoolId + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        IZoweRequest request = new TextRequest(connection, new HttpGet(url));
        return request.httpGet();
    }

    public static String getSpoolContentCommon(ZOSConnection connection, JobFile jobFile) throws IOException {
        Util.checkConnection(connection);
        Util.checkNullParameter(jobFile == null, "jobFile is null");
        Util.checkStateParameter(!jobFile.getJobName().isPresent(), "jobName not specified");
        Util.checkStateParameter(!jobFile.getJobId().isPresent(), "jobId not specified");
        Util.checkStateParameter(!jobFile.getId().isPresent(), "id not specified");

        String url = "https://" + connection.getHost() + ":" + connection.getPort() + JobsConstants.RESOURCE + "/" +
                jobFile.getJobName().get() + "/" + jobFile.getJobId().get() + JobsConstants.RESOURCE_SPOOL_FILES + "/" +
                jobFile.getId().get() + JobsConstants.RESOURCE_SPOOL_CONTENT;

        LOG.debug(url);

        IZoweRequest request = new TextRequest(connection, new HttpGet(url));
        return request.httpGet();
    }

}
