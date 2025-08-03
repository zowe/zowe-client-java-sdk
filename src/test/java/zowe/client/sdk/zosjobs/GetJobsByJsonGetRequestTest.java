/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosjobs;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.zosjobs.methods.JobGet;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Class containing unit tests for JobGet.
 *
 * @author Frank Giordano
 * @version 4.0
 */
@RunWith(MockitoJUnitRunner.class)
public class GetJobsByJsonGetRequestTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private GetJsonZosmfRequest mockJsonGetRequest;
    private JobGet getJobs;
    private JSONObject jobJson;

    @Before
    public void init() {
        mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        getJobs = new JobGet(connection);
        Whitebox.setInternalState(getJobs, "request", mockJsonGetRequest);

        final Map<String, String> jsonMap = new HashMap<>();
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
    public void tstGetJobFromMultipleJobsResultsExceptionFailure() throws ZosmfRequestException {
        final String msg = "expected 1 job returned but received 2 jobs.";
        final JSONArray jsonArray = new JSONArray();

        final Map<String, String> jsonJobMap1 = new HashMap<>();
        jsonJobMap1.put("jobid", "job1");
        final Map<String, String> jsonJob1 = new JSONObject(jsonJobMap1);
        jsonArray.add(jsonJob1);

        final Map<String, String> jsonJobMap2 = new HashMap<>();
        jsonJobMap2.put("jobid", "job2");
        final Map<String, String> jsonJob2 = new JSONObject(jsonJobMap2);
        jsonArray.add(jsonJob2);

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jsonArray, 200, "success"));

        String msgResult = null;
        try {
            getJobs.getById("1");
        } catch (Exception e) {
            msgResult = e.getMessage();

        }
        assertEquals("https://1:1/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals(msg, msgResult);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstGetJobWithAllJobMembersSuccess() throws ZosmfRequestException {
        final JSONArray jsonArray = new JSONArray();
        jsonArray.add(jobJson);

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jsonArray, 200, "success"));

        final Job job = getJobs.getById("1");
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
    @SuppressWarnings("unchecked")
    public void tstGetJobWithJobIdOnlySuccess() throws ZosmfRequestException {
        final JSONArray jsonArray = new JSONArray();

        final Map<String, String> jsonJobMap = new HashMap<>();
        jsonJobMap.put("jobid", "job");
        final JSONObject jsonJob = new JSONObject(jsonJobMap);
        jsonArray.add(jsonJob);

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jsonArray, 200, "success"));

        final Job job = getJobs.getById("1");
        assertEquals("https://1:1/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals("job", job.getJobId().get());
    }

    @Test
    public void tstGetByIdCmdResponseWithInvalidBasePathFailure() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("1", "1", "1", "1");
        connection.setBasePath("consoles//");
        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        final JobGet getJobs = new JobGet(connection, request);
        assertThrows(IllegalArgumentException.class, () -> getJobs.getById("1"));
    }

    @Test
    public void tstGetSpoolContentByIdJobIdNullExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", null, 1);
        } catch (NullPointerException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_ID_NULL_MSG, errorMsg);
    }

    @Test
    public void tstGetSpoolContentByIdJobNameNullExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent(null, "1", 1);
        } catch (NullPointerException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_NULL_MSG, errorMsg);
    }

    @Test
    public void tstGetSpoolContentByIdSpoolIdNegativeNumberExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", "1", -11);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spool id not specified", errorMsg);
    }

    @Test
    public void tstGetSpoolContentByIdSpoolIdZeroExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", "1", 0);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spool id not specified", errorMsg);
    }

    @Test
    public void tstGetStatusForJobNoParamsExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(new Job.Builder().build());
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_ID_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstGetStatusForJobSuccess() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jobJson, 200, "success"));

        final Job job = getJobs.getStatusByJob(new Job.Builder().jobId("1").jobName("jobName").build());
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/1?step-data=Y", getJobs.getUrl());
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
    public void tstGetStatusForJobWithJobIdOnlyExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(new Job.Builder().jobId("1").build());
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstGetStatusForJobWithJobNameOnlyExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(new Job.Builder().jobName("jobName").build());
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_ID_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstNullConnectionFailure() {
        try {
            new JobGet(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

}
