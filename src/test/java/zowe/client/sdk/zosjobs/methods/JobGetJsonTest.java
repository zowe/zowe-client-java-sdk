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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.GetTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.JobsConstants;
import zowe.client.sdk.zosjobs.input.CommonJobInputData;
import zowe.client.sdk.zosjobs.model.Job;
import zowe.client.sdk.zosjobs.model.JobStepData;

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
            .createBasicConnection("1", 443, "1", "1");
    private GetJsonZosmfRequest mockJsonGetRequest;
    private JobGet getJobs;
    private JSONObject jobJson;
    private JSONArray stepDataArray;

    @BeforeEach
    @SuppressWarnings("unchecked")
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

        // step data initialize
        final Map<String, String> stepDataMap = new HashMap<>();
        stepDataMap.put("smfid", "SP21");
        stepDataMap.put("active", "true");
        stepDataMap.put("step-number", "1");
        stepDataMap.put("proc-step-name", "STARTING");
        stepDataMap.put("step-name", "IEFPROC ");
        stepDataMap.put("program-name", "BLSQPRMI");
        final JSONObject stepData = new JSONObject(stepDataMap);
        stepDataArray = new JSONArray();
        stepDataArray.add(stepData);
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
        assertEquals("https://1:443/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
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
        assertEquals("https://1:443/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
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
        assertEquals("https://1:443/zosmf/restjobs/jobs?owner=*&jobid=1", getJobs.getUrl());
        assertEquals("job", job.getJobId());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstJobGetJsonStatusWithOutStepDataAndSomeEmptyValuesSuccess() throws ZosmfRequestException {
        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("retcode", "null");
        jsonResponse.put("jobname", "BLSJPRMI");
        jsonResponse.put("status", "ACTIVE");
        jsonResponse.put("step-data", null);
        jsonResponse.put("owner", "IBMUSER");
        jsonResponse.put("subsystem", "JES2");
        jsonResponse.put("phase", 20);

        final Response response = new Response(jsonResponse, 200, "success");
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(response);

        final Job job = getJobs.getStatus("BLSJPRMI", "STC00052");

        // Validate URL built correctly (no step-data param for getStatus)
        assertEquals("https://1:443/zosmf/restjobs/jobs/BLSJPRMI/STC00052?step-data=Y", getJobs.getUrl());

        // Core job fields
        assertEquals("BLSJPRMI", job.getJobName());
        assertEquals("", job.getJobId());
        assertEquals("ACTIVE", job.getStatus());
        assertEquals("", job.getType());
        assertEquals("", job.getClasss());
        assertEquals("IBMUSER", job.getOwner());
        assertEquals("JES2", job.getSubSystem());
        assertEquals("", job.getJobCorrelator());
        assertEquals("", job.getPhaseName());
        assertEquals("null", job.getRetCode());

        // URLs and timing fields
        assertEquals("", job.getUrl());
        assertEquals("", job.getFilesUrl());
        assertEquals("", job.getExecSystem());
        assertEquals("", job.getExecMember());
        assertEquals("", job.getExecSubmitted());
        assertEquals("", job.getExecStarted());
        assertEquals("", job.getExecEnded());

        // Step data validation
        assertNotNull(job.getStepData());
        assertEquals(0, job.getStepData().length);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstJobGetJsonStatusWithStepDataSuccess() throws ZosmfRequestException {
        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("retcode", "null");
        jsonResponse.put("jobname", "BLSJPRMI");
        jsonResponse.put("status", "ACTIVE");
        jsonResponse.put("job-correlator", "S0000052SY1.....CE35BDE8.......:");
        jsonResponse.put("class", "STC");
        jsonResponse.put("type", "STC");
        jsonResponse.put("jobid", "STC00052");
        jsonResponse.put("url", "https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A");
        jsonResponse.put("phase-name", "Job is on the hard copy queue");
        jsonResponse.put("step-data", stepDataArray);
        jsonResponse.put("owner", "IBMUSER");
        jsonResponse.put("subsystem", "JES2");
        jsonResponse.put("files-url", "https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A/files");
        jsonResponse.put("phase", 20);
        jsonResponse.put("exec-system", "SY1");
        jsonResponse.put("exec-member", "SY1");
        jsonResponse.put("exec-submitted", "2018-11-03T09:05:15.000Z");
        jsonResponse.put("exec-started", "2018-11-03T09:05:18.010Z");
        jsonResponse.put("exec-ended", "2018-11-03T09:05:25.332Z");

        final Response response = new Response(jsonResponse, 200, "success");
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(response);

        final Job job = getJobs.getStatus("BLSJPRMI", "STC00052");

        // Validate URL built correctly (no step-data param for getStatus)
        assertEquals("https://1:443/zosmf/restjobs/jobs/BLSJPRMI/STC00052?step-data=Y", getJobs.getUrl());

        // Core job fields
        assertEquals("BLSJPRMI", job.getJobName());
        assertEquals("STC00052", job.getJobId());
        assertEquals("ACTIVE", job.getStatus());
        assertEquals("STC", job.getType());
        assertEquals("STC", job.getClasss());
        assertEquals("IBMUSER", job.getOwner());
        assertEquals("JES2", job.getSubSystem());
        assertEquals("S0000052SY1.....CE35BDE8.......:", job.getJobCorrelator());
        assertEquals("Job is on the hard copy queue", job.getPhaseName());
        assertEquals("null", job.getRetCode());

        // URLs and timing fields
        assertEquals("https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A", job.getUrl());
        assertEquals("https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A/files", job.getFilesUrl());
        assertEquals("SY1", job.getExecSystem());
        assertEquals("SY1", job.getExecMember());
        assertEquals("2018-11-03T09:05:15.000Z", job.getExecSubmitted());
        assertEquals("2018-11-03T09:05:18.010Z", job.getExecStarted());
        assertEquals("2018-11-03T09:05:25.332Z", job.getExecEnded());

        // Step data validation
        assertNotNull(job.getStepData());
        assertEquals(1, job.getStepData().length);
        assertEquals("SP21", job.getStepData()[0].getSmfid());
        assertTrue(job.getStepData()[0].isActive());
        assertEquals(1L, job.getStepData()[0].getStepNumber());
        assertEquals("STARTING", job.getStepData()[0].getProcStepName());
        assertEquals("IEFPROC ", job.getStepData()[0].getStepName());
        assertEquals("BLSQPRMI", job.getStepData()[0].getProgramName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void tstJobGetJsonStatusWithStepDataFlagTrue() throws ZosmfRequestException {
        final JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("jobname", "BLSJPRMI");
        jsonResponse.put("jobid", "STC00052");
        jsonResponse.put("status", "ACTIVE");
        jsonResponse.put("step-data", stepDataArray);

        final Response response = new Response(jsonResponse, 200, "success");
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(response);

        final CommonJobInputData input = new CommonJobInputData("STC00052", "BLSJPRMI", true);
        final Job job = getJobs.getStatusCommon(input);

        assertEquals("https://1:443/zosmf/restjobs/jobs/BLSJPRMI/STC00052?step-data=Y", getJobs.getUrl());
        assertEquals("BLSJPRMI", job.getJobName());
        assertEquals("STC00052", job.getJobId());
        assertEquals("ACTIVE", job.getStatus());

        // Step data validation
        assertNotNull(job.getStepData());
        assertEquals(1, job.getStepData().length);
        assertEquals("SP21", job.getStepData()[0].getSmfid());
        assertTrue(job.getStepData()[0].isActive());
        assertEquals(1L, job.getStepData()[0].getStepNumber());
        assertEquals("STARTING", job.getStepData()[0].getProcStepName());
        assertEquals("IEFPROC ", job.getStepData()[0].getStepName());
        assertEquals("BLSQPRMI", job.getStepData()[0].getProgramName());
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdJobIdNullExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", null, 1L);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_ID_NULL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdJobNameNullExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent(null, "1", 1L);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_NULL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdSpoolIdNegativeNumberExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", "1", -11L);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spool id not specified", errorMsg);
    }

    @Test
    public void tstJobGetJsonSpoolContentByIdSpoolIdZeroExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getSpoolContent("jobName", "1", 0L);
        } catch (IllegalArgumentException e) {
            errorMsg = e.getMessage();
        }
        assertEquals("spool id not specified", errorMsg);
    }

    @Test
    public void tstJobGetJsonStatusForJobNoParamsExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(Job.builder().build());
        } catch (IllegalStateException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonStatusForJobSuccess() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(jobJson, 200, "success"));

        final Job job = getJobs.getStatusByJob(Job.builder().jobId("1").jobName("jobName").build());
        assertEquals("https://1:443/zosmf/restjobs/jobs/jobName/1?step-data=Y", getJobs.getUrl());
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
    public void tstDeserializeFullJobJsonWithTwoStepDataEntriesSuccess() throws Exception {
        final String jsonString =
                "{"
                        + "\"retcode\": \"null\","
                        + "\"jobname\": \"BLSJPRMI\","
                        + "\"status\": \"ACTIVE\","
                        + "\"job-correlator\": \"S0000052SY1.....CE35BDE8.......:\","
                        + "\"class\": \"STC\","
                        + "\"type\": \"STC\","
                        + "\"jobid\": \"STC00052\","
                        + "\"url\": \"https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A\","
                        + "\"phase-name\": \"Job is on the hard copy queue\","
                        + "\"step-data\": ["
                        + "  {"
                        + "    \"smfid\": \"SP21\","
                        + "    \"active\": true,"
                        + "    \"step-number\": 1,"
                        + "    \"proc-step-name\": \"STARTING\","
                        + "    \"step-name\": \"IEFPROC \","
                        + "    \"program-name\": \"BLSQPRMI\""
                        + "  },"
                        + "  {"
                        + "    \"smfid\": \"SP22\","
                        + "    \"active\": false,"
                        + "    \"step-number\": 2,"
                        + "    \"proc-step-name\": \"ENDING\","
                        + "    \"step-name\": \"IEFPROC2\","
                        + "    \"program-name\": \"IEFBR14\""
                        + "  }"
                        + "],"
                        + "\"owner\": \"IBMUSER\","
                        + "\"subsystem\": \"JES2\","
                        + "\"files-url\": \"https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A/files\","
                        + "\"phase\": 20,"
                        + "\"exec-system\": \"SY1\","
                        + "\"exec-member\": \"SY1\","
                        + "\"exec-submitted\": \"2018-11-03T09:05:15.000Z\","
                        + "\"exec-started\": \"2018-11-03T09:05:18.010Z\","
                        + "\"exec-ended\": \"2018-11-03T09:05:25.332Z\""
                        + "}";

        // Deserialize JSON into Job using Jackson
        ObjectMapper mapper = new ObjectMapper();
        Job job = mapper.readValue(jsonString, Job.class);

        // Validate core fields
        assertEquals("BLSJPRMI", job.getJobName());
        assertEquals("STC00052", job.getJobId());
        assertEquals("ACTIVE", job.getStatus());
        assertEquals("STC", job.getType());
        assertEquals("STC", job.getClasss());
        assertEquals("IBMUSER", job.getOwner());
        assertEquals("JES2", job.getSubSystem());
        assertEquals("null", job.getRetCode());
        assertEquals("S0000052SY1.....CE35BDE8.......:", job.getJobCorrelator());
        assertEquals("Job is on the hard copy queue", job.getPhaseName());
        assertEquals(20L, job.getPhase());

        // Validate execution timing fields
        assertEquals("SY1", job.getExecSystem());
        assertEquals("SY1", job.getExecMember());
        assertEquals("2018-11-03T09:05:15.000Z", job.getExecSubmitted());
        assertEquals("2018-11-03T09:05:18.010Z", job.getExecStarted());
        assertEquals("2018-11-03T09:05:25.332Z", job.getExecEnded());
        assertEquals("", job.getReasonNotRunning());

        // Validate URLs
        assertEquals("https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A", job.getUrl());
        assertEquals("https://host:port/zosmf/restjobs/jobs/S0000052SY1.....CE35BDE8.......%3A/files", job.getFilesUrl());

        // Step-data array validation
        assertNotNull(job.getStepData());
        assertEquals(2, job.getStepData().length);

        JobStepData first = job.getStepData()[0];
        assertEquals("SP21", first.getSmfid());
        assertTrue(first.isActive());
        assertEquals(1, first.getStepNumber());
        assertEquals("STARTING", first.getProcStepName());
        assertEquals("IEFPROC ", first.getStepName());
        assertEquals("BLSQPRMI", first.getProgramName());

        JobStepData second = job.getStepData()[1];
        assertEquals("SP22", second.getSmfid());
        assertFalse(second.isActive());
        assertEquals(2, second.getStepNumber());
        assertEquals("ENDING", second.getProcStepName());
        assertEquals("IEFPROC2", second.getStepName());
        assertEquals("IEFBR14", second.getProgramName());
    }

    @Test
    public void tstJobGetJsonStatusForJobWithJobIdOnlyExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(Job.builder().jobId("1").build());
        } catch (IllegalStateException e) {
            errorMsg = e.getMessage();
        }
        assertEquals(JobsConstants.JOB_NAME_ILLEGAL_MSG, errorMsg);
    }

    @Test
    public void tstJobGetJsonStatusForJobWithJobNameOnlyExceptionFailure() throws ZosmfRequestException {
        String errorMsg = "";
        try {
            getJobs.getStatusByJob(Job.builder().jobName("jobName").build());
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
