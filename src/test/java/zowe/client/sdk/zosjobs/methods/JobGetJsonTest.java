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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.*;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.model.Job;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Class containing unit tests for JobGet.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class JobGetJsonTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private GetJsonZosmfRequest mockJsonGetRequest;
    private JobGet getJobs;
    private JSONObject jobJson;

    @BeforeEach
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
    public void tstJobGetJsonFromMultipleJobsResultsExceptionFailure() throws ZosmfRequestException {
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
    public void tstJobGetJsonWithAllJobMembersSuccess() throws ZosmfRequestException {
        final JSONArray jsonArray = new JSONArray();
        jsonArray.add(jobJson);

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jsonArray, 200, "success"));

        final Job job = getJobs.getById("1");
        assertEquals("https://1:1/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals("jobid", job.getJobId());
        assertEquals("jobname", job.getJobName());
        assertEquals("subsystem", job.getSubSystem());
        assertEquals("owner", job.getOwner());
        assertEquals("status", job.getStatus());
        assertEquals("type", job.getType());
        assertEquals("class", job.getClasss());
        assertEquals("retcode", job.getRetCode());
        assertEquals("url", job.getUrl());
        assertEquals("files-url", job.getFilesUrl());
        assertEquals("job-correlator", job.getJobCorrelator());
        assertEquals("phase-name", job.getPhaseName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstJobGetJsonWithJobIdOnlySuccess() throws ZosmfRequestException {
        final JSONArray jsonArray = new JSONArray();

        final Map<String, String> jsonJobMap = new HashMap<>();
        jsonJobMap.put("jobid", "job");
        final JSONObject jsonJob = new JSONObject(jsonJobMap);
        jsonArray.add(jsonJob);

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jsonArray, 200, "success"));

        final Job job = getJobs.getById("1");
        assertEquals("https://1:1/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals("job", job.getJobId());
    }

    @Test
    public void tstJobGetJsonByIdCmdResponseWithInvalidBasePathFailure() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("1", "1", "1", "1", "consoles//");
        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);
        final JobGet getJobs = new JobGet(connection, request);
        assertThrows(IllegalArgumentException.class, () -> getJobs.getById("1"));
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdJobIdNullExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", null, 1);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_ID_NULL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdJobNameNullExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent(null, "1", 1);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_NULL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdSpoolIdNegativeNumberExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", "1", -11);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spool id not specified", errorMsg);
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdSpoolIdZeroExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", "1", 0);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spool id not specified", errorMsg);
    }

    @Test
    public void tstJobGetJsonStatusForJobNoParamsExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(new Job.Builder().build());
        } catch (IllegalStateException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonStatusForJobSuccess() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jobJson, 200, "success"));

        final Job job = getJobs.getStatusByJob(new Job.Builder().jobId("1").jobName("jobName").build());
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/1?step-data=Y", getJobs.getUrl());
        assertEquals("jobid", job.getJobId());
        assertEquals("jobname", job.getJobName());
        assertEquals("subsystem", job.getSubSystem());
        assertEquals("owner", job.getOwner());
        assertEquals("status", job.getStatus());
        assertEquals("type", job.getType());
        assertEquals("class", job.getClasss());
        assertEquals("retcode", job.getRetCode());
        assertEquals("url", job.getUrl());
        assertEquals("files-url", job.getFilesUrl());
        assertEquals("job-correlator", job.getJobCorrelator());
        assertEquals("phase-name", job.getPhaseName());
    }

    @Test
    public void tstJobGetJsonStatusForJobWithJobIdOnlyExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(new Job.Builder().jobId("1").build());
        } catch (IllegalStateException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonStatusForJobWithJobNameOnlyExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(new Job.Builder().jobName("jobName").build());
        } catch (IllegalStateException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_ID_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonNullConnectionFailure() {
        try {
            new JobGet(null);
        } catch (NullPointerException e) {
            assertEquals("connection is null", e.getMessage());
        }
    }

    @Test
    public void tstJobGetJsonSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetTextZosmfRequest.class);
        JobGet jobGet = new JobGet(connection, request);
        assertNotNull(jobGet);
    }

    @Test
    public void tstJobGetJsonSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(GetTextZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobGet(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstJobGetJsonSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobGet(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstJobGetJsonSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JobGet(connection, request)
        );
        assertEquals("GET_JSON or GET_TEXT request type required", exception.getMessage());
    }

    @Test
    public void tstJobGetJsonPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobGet jobGet = new JobGet(connection);
        assertNotNull(jobGet);
    }

    @Test
    public void tstJobGetJsonPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobGet(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
