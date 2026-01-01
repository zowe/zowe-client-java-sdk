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

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.input.JobModifyInputData;
import zowe.client.sdk.zosjobs.model.Job;
import zowe.client.sdk.zosjobs.response.JobFeedback;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for JobChange.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class JobChangeTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockPutJsonZosmfRequest;
    private PutJsonZosmfRequest mockPutJsonZosmfRequestToken;
    private String classs;
    private String version;
    private final JobChange jobChange = new JobChange(connection);

    @BeforeEach
    public void init() throws ZosmfRequestException {
        String jsonResponse = "{\"jobid\":\"JOBID\",\"jobname\":\"JOBNAME\",\"original-jobid\":\"JOB00023\",\"owner\":" +
                "\"IBMUSER\",\"member\":\"JES2\",\"sysname\":\"SY1\",\"job-correlator\":\"J0000023SY1.....CC20F378.......:" +
                "\",\"status\":\"0\"}";
        classs = "A";
        version = "2.0";

        mockPutJsonZosmfRequest = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(connection));
        doCallRealMethod().when(mockPutJsonZosmfRequest).setHeaders(anyMap());
        doCallRealMethod().when(mockPutJsonZosmfRequest).setStandardHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequest).getHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequest).getUrl();

        mockPutJsonZosmfRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPutJsonZosmfRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).getHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).getUrl();

        Mockito.when(mockPutJsonZosmfRequest.executeRequest()).thenReturn(
                new Response(jsonResponse, 200, "success"));
        Mockito.when(mockPutJsonZosmfRequestToken.executeRequest()).thenReturn(
                new Response(jsonResponse, 200, "success"));
    }

    @Test
    public void tstChangeClassCommonSuccess() throws ZosmfRequestException {
        JobModifyInputData inputData = new JobModifyInputData.Builder("JOBNAME", "JOBID")
                .jobClass(classs)
                .version(version)
                .build();

        final JobChange jobChange = new JobChange(connection, mockPutJsonZosmfRequest);
        final JobFeedback result = jobChange.changeClassCommon(inputData);
        assertNotNull(result);
        assertEquals("JOBNAME", result.getJobName());
        assertEquals("JOBID", result.getJobId());
        assertEquals("IBMUSER", result.getOwner());
        assertEquals("JES2", result.getMember());
        assertEquals("SY1", result.getSysname());
        assertEquals("J0000023SY1.....CC20F378.......:", result.getJobCorrelator());
        assertEquals("0", result.getStatus());
        assertTrue(
                mockPutJsonZosmfRequest.getHeaders().containsKey("Authorization"),
                "Authorization header should be present"
        );

        final Map<String, String> changeMap = new HashMap<>();
        changeMap.put("class", classs);
        changeMap.put("version", version);

        // verify request setup
        verify(mockPutJsonZosmfRequest).setUrl(anyString());
        verify(mockPutJsonZosmfRequest).setBody(new JSONObject(changeMap).toString());
        verify(mockPutJsonZosmfRequest).executeRequest();
    }

    @Test
    public void tstChangeClassCommonWithTokenSuccess() throws ZosmfRequestException {
        JobModifyInputData inputData = new JobModifyInputData.Builder("JOBNAME", "JOBID")
                .jobClass(classs)
                .version(version)
                .build();

        final JobChange jobChange = new JobChange(tokenConnection, mockPutJsonZosmfRequestToken);
        final JobFeedback result = jobChange.changeClassCommon(inputData);

        assertNotNull(result);
        assertEquals("JOBNAME", result.getJobName());
        assertEquals("JOBID", result.getJobId());
        assertEquals("IBMUSER", result.getOwner());
        assertEquals("JES2", result.getMember());
        assertEquals("SY1", result.getSysname());
        assertEquals("J0000023SY1.....CC20F378.......:", result.getJobCorrelator());
        assertEquals("0", result.getStatus());
        assertFalse(
                mockPutJsonZosmfRequestToken.getHeaders().containsKey("Authorization"),
                "Authorization header should not be present"
        );

        final Map<String, String> changeMap = new HashMap<>();
        changeMap.put("class", classs);
        changeMap.put("version", version);

        // verify request setup
        verify(mockPutJsonZosmfRequestToken).setUrl(anyString());
        verify(mockPutJsonZosmfRequestToken).setBody(new JSONObject(changeMap).toString());
        verify(mockPutJsonZosmfRequestToken).executeRequest();
    }

    @Test
    public void tstChangeClassSuccess() throws ZosmfRequestException {
        final JobChange jobChange = new JobChange(connection, mockPutJsonZosmfRequest);
        final JobFeedback result = jobChange.changeClass("JOBNAME", "JOBID", classs, version);

        assertNotNull(result);
        assertEquals("JOBNAME", result.getJobName());
        assertEquals("JOBID", result.getJobId());
        assertEquals("IBMUSER", result.getOwner());
        assertEquals("JES2", result.getMember());
        assertEquals("SY1", result.getSysname());
        assertEquals("J0000023SY1.....CC20F378.......:", result.getJobCorrelator());
        assertEquals("0", result.getStatus());
        assertTrue(
                mockPutJsonZosmfRequest.getHeaders().containsKey("Authorization"),
                "Authorization header should be present"
        );

        final Map<String, String> changeMap = new HashMap<>();
        changeMap.put("class", classs);
        changeMap.put("version", version);

        // verify request setup
        verify(mockPutJsonZosmfRequest).setUrl(anyString());
        verify(mockPutJsonZosmfRequest).setBody(new JSONObject(changeMap).toString());
        verify(mockPutJsonZosmfRequest).executeRequest();
    }

    @Test
    public void tstChangeClassByJobSuccess() throws ZosmfRequestException {
        final JobChange jobChange = new JobChange(connection, mockPutJsonZosmfRequest);
        final Job job = Job.builder().jobName("JOBNAME").jobId("JOBID").build();
        final JobFeedback result = jobChange.changeClassByJob(job, classs, version);

        assertNotNull(result);
        assertEquals("JOBNAME", result.getJobName());
        assertEquals("JOBID", result.getJobId());
        assertEquals("IBMUSER", result.getOwner());
        assertEquals("JES2", result.getMember());
        assertEquals("SY1", result.getSysname());
        assertEquals("J0000023SY1.....CC20F378.......:", result.getJobCorrelator());
        assertEquals("0", result.getStatus());

        final Map<String, String> changeMap = new HashMap<>();
        changeMap.put("class", classs);
        changeMap.put("version", version);

        // verify request setup
        verify(mockPutJsonZosmfRequest).setUrl(anyString());
        verify(mockPutJsonZosmfRequest).setBody(new JSONObject(changeMap).toString());
        verify(mockPutJsonZosmfRequest).executeRequest();
    }

    @Test
    public void tstConstructorWithNullConnectionFailure() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> new JobChange(null));
        assertEquals("connection is null", ex.getMessage());
    }

    @Test
    public void tstChangeClassWithNullJobNameFailure() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> jobChange.changeClass(null, "JOBID", "A", "1.0"));
        assertEquals("jobName is either null or empty", ex.getMessage());
    }

    @Test
    public void tstChangeClassWithNullJobIdFailure() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> jobChange.changeClass("JOBNAME", null, "A", "1.0"));
        assertEquals("jobId is either null or empty", ex.getMessage());
    }

    @Test
    public void tstChangeClassWithNullJobClassFailure() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> jobChange.changeClass("JOBNAME", "JOBID", null, "1.0"));
        assertEquals("jobClass is either null or empty", ex.getMessage());
    }

    @Test
    public void tstChangeClassCommonWithInvalidVersionFailure() {
        JobModifyInputData inputData = new JobModifyInputData.Builder("JOBNAME", "JOBID")
                .jobClass("A")
                .version("3.0")
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> jobChange.changeClassCommon(inputData));
        assertEquals("invalid version specified", ex.getMessage());
    }

    @Test
    public void tstChangeClassByJobWithNullJobFailure() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> jobChange.changeClassByJob(null, "A", "1.0"));
        assertEquals("job is null", ex.getMessage());
    }

}
