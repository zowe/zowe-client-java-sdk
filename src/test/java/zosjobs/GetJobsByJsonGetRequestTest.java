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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import rest.JsonGetRequest;
import rest.Response;
import zosjobs.response.Job;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class GetJobsByJsonGetRequestTest {

    private JsonGetRequest request;
    private ZOSConnection connection;
    private GetJobs getJobs;
    private JSONObject jobJson;

    @Before
    public void init() {
        request = Mockito.mock(JsonGetRequest.class);
        connection = new ZOSConnection("1", "1", "1", "1");
        getJobs = new GetJobs(connection);
        Whitebox.setInternalState(getJobs, "request", request);

        var jsonMap = new HashMap<String, String>();
        jsonMap.put("jobid", "jobid");
        jsonMap.put("jobname", "jobname");
        jsonMap.put("subsystem", "subsystem");
        jsonMap.put("owner", "owner");
        jsonMap.put("status", "status");
        jsonMap.put("type", "type");
        jsonMap.put("class", "class");
        jsonMap.put("retcode", "retcode");
        jsonMap.put("url", "url");
        jsonMap.put("files-url", "files-url");
        jsonMap.put("job-correlator", "job-correlator");
        jsonMap.put("phase-name", "phase-name");
        jobJson = new JSONObject(jsonMap);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstGetJobFromMultipleJobsResultsExceptionFailure() throws Exception {
        String msg = "Expected 1 job returned but received 2 jobs.";
        JSONArray jsonArray = new JSONArray();

        var jsonJobMap1 = new HashMap<String, String>();
        jsonJobMap1.put("jobid", "job1");
        var jsonJob1 = new JSONObject(jsonJobMap1);
        jsonArray.add(jsonJob1);

        var jsonJobMap2 = new HashMap<String, String>();
        jsonJobMap2.put("jobid", "job2");
        var jsonJob2 = new JSONObject(jsonJobMap2);
        jsonArray.add(jsonJob2);

        Response response = new Response(jsonArray, 200);
        Mockito.when(request.executeHttpRequest()).thenReturn(response);

        String msgResult = null;
        try {
            getJobs.getJob("1");
        } catch (Exception e) {
            msgResult = e.getMessage();

        }
        assertEquals("https://1:1/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals(msg, msgResult);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstGetJobWithJobIdOnlySuccess() throws Exception {
        JSONArray jsonArray = new JSONArray();

        var jsonJobMap = new HashMap<String, String>();
        jsonJobMap.put("jobid", "job");
        var jsonJob = new JSONObject(jsonJobMap);
        jsonArray.add(jsonJob);

        Response response = new Response(jsonArray, 200);
        Mockito.when(request.executeHttpRequest()).thenReturn(response);

        Job job = getJobs.getJob("1");
        assertEquals("https://1:1/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals("job", job.getJobId().get());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstGetJobWithAllJobMembersSuccess() throws Exception {
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jobJson);

        Response response = new Response(jsonArray, 200);
        Mockito.when(request.executeHttpRequest()).thenReturn(response);

        Job job = getJobs.getJob("1");
        assertEquals("https://1:1/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals("jobid", job.getJobId().get());
        assertEquals("jobname", job.getJobName().get());
        assertEquals("subsystem", job.getSubSystem().get());
        assertEquals("owner", job.getOwner().get());
        assertEquals("status", job.getStatus().get());
        assertEquals("type", job.getType().get());
        assertEquals("class", job.getClasss().get());
        assertEquals("retcode", job.getRetCode().get());
        assertEquals("url", job.getUrl().get());
        assertEquals("files-url", job.getFilesUrl().get());
        assertEquals("job-correlator", job.getJobCorrelator().get());
        assertEquals("phase-name", job.getPhaseName().get());
    }

    @Test
    public void tstGetStatusForJobWithJobIdOnlyExceptionFailure() {
        String errorMsg = "";
        try {
            getJobs.getStatusForJob(new Job.Builder().jobId("1").build());
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        assertEquals("jobName not specified", errorMsg);
    }

    @Test
    public void tstGetStatusForJobWithJobNameOnlyExceptionFailure() {
        String errorMsg = "";
        try {
            getJobs.getStatusForJob(new Job.Builder().jobName("jobName").build());
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        assertEquals("jobId not specified", errorMsg);
    }

    @Test
    public void tstGetStatusForJobNoParamsExceptionFailure() {
        String errorMsg = "";
        try {
            getJobs.getStatusForJob(new Job.Builder().build());
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        assertEquals("jobId not specified", errorMsg);
    }

    @Test
    public void tstGetStatusForJobSuccess() throws Exception {
        Response response = new Response(jobJson, 200);
        Mockito.when(request.executeHttpRequest()).thenReturn(response);

        Job job = getJobs.getStatusForJob(new Job.Builder().jobId("1").jobName("jobName").build());
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/1", getJobs.getUrl());
        assertEquals("jobid", job.getJobId().get());
        assertEquals("jobname", job.getJobName().get());
        assertEquals("subsystem", job.getSubSystem().get());
        assertEquals("owner", job.getOwner().get());
        assertEquals("status", job.getStatus().get());
        assertEquals("type", job.getType().get());
        assertEquals("class", job.getClasss().get());
        assertEquals("retcode", job.getRetCode().get());
        assertEquals("url", job.getUrl().get());
        assertEquals("files-url", job.getFilesUrl().get());
        assertEquals("job-correlator", job.getJobCorrelator().get());
        assertEquals("phase-name", job.getPhaseName().get());
    }

    @Test
    public void tstGetSpoolContentByIdJobNameNullExceptionFailure() {
        String errorMsg = "";
        try {
            getJobs.getSpoolContentById(null, "1", 1);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        assertEquals("jobName is null", errorMsg);
    }

    @Test
    public void tstGetSpoolContentByIdJobIdNullExceptionFailure() {
        String errorMsg = "";
        try {
            getJobs.getSpoolContentById("jobName", null, 1);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        assertEquals("jobId is null", errorMsg);
    }

    @Test
    public void tstGetSpoolContentByIdSpoolIdZeroExceptionFailure() {
        String errorMsg = "";
        try {
            getJobs.getSpoolContentById("jobName", "1", 0);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spoolId not specified", errorMsg);
    }

    @Test
    public void tstGetSpoolContentByIdSpoolIdNegativeNumberExceptionFailure() {
        String errorMsg = "";
        try {
            getJobs.getSpoolContentById("jobName", "1", -11);
        } catch (Exception e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spoolId not specified", errorMsg);
    }

}
