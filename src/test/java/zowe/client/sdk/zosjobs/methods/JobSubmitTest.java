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
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.PutTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.model.Job;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for JobSubmit.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class JobSubmitTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockPutJsonZosmfRequest;
    private PutTextZosmfRequest mockPutTextZosmfRequest;
    private PutJsonZosmfRequest mockPutJsonZosmfRequestToken;
    @TempDir
    private Path tempDir;

    @BeforeEach
    public void init() throws ZosmfRequestException {
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
        JSONObject jobJson = new JSONObject(jsonMap);

        mockPutJsonZosmfRequest = Mockito.mock(PutJsonZosmfRequest.class);
        mockPutTextZosmfRequest = Mockito.mock(PutTextZosmfRequest.class);
        Mockito.when(mockPutJsonZosmfRequest.executeRequest()).thenReturn(
                new Response(jobJson, 200, "success"));
        doCallRealMethod().when(mockPutJsonZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequest).getUrl();
        Mockito.when(mockPutTextZosmfRequest.executeRequest()).thenReturn(
                new Response(jobJson, 200, "success"));
        doCallRealMethod().when(mockPutTextZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockPutTextZosmfRequest).getUrl();

        mockPutJsonZosmfRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPutJsonZosmfRequestToken.executeRequest()).thenReturn(
                new Response(jobJson, 200, "success"));
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).getHeaders();
        doCallRealMethod().when(mockPutJsonZosmfRequestToken).getUrl();
    }

    @Test
    public void tstJobSubmitSuccess() throws ZosmfRequestException {
        final JobSubmit jobSubmit = new JobSubmit(connection, mockPutJsonZosmfRequest);
        final Job job = jobSubmit.submit("TEST.DATASET");
        assertEquals("https://1:443/zosmf/restjobs/jobs", mockPutJsonZosmfRequest.getUrl());
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
    public void tstJobSubmitTokenSuccess() throws ZosmfRequestException {
        final JobSubmit jobSubmit = new JobSubmit(connection, mockPutJsonZosmfRequestToken);
        final Job job = jobSubmit.submit("TEST.DATASET");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutJsonZosmfRequestToken.getHeaders().toString());
        assertEquals("https://1:443/zosmf/restjobs/jobs", mockPutJsonZosmfRequestToken.getUrl());
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
    void testSubmitByLocalFileSuccess() throws Exception {
        // set up the local file
        Path jclFile = tempDir.resolve("test.jcl");
        String jclContent = "//TESTJOB JOB ...";
        Files.writeString(jclFile, jclContent);

        // initialize JobSubmit using existing mock request (from @BeforeEach)
        final JobSubmit jobSubmit = new JobSubmit(connection, mockPutTextZosmfRequest);
        final Job job = jobSubmit.submitByLocalFile(jclFile.toString());

        // verify file contents reached the request layer
        verify(mockPutTextZosmfRequest).setBody(jclContent);

        // verified against @BeforeEach mock data
        assertEquals("jobid", job.getJobId());
        assertNotNull(job);
    }

    @Test
    public void tstJobSubmitSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        JobSubmit jobSubmit = new JobSubmit(connection, request);
        assertNotNull(jobSubmit);
    }

    @Test
    public void tstJobSubmitSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobSubmit(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstJobSubmitSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobSubmit(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstJobSubmitSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JobSubmit(connection, request)
        );
        assertEquals("PUT_JSON or PUT_TEXT request type required", exception.getMessage());
    }

    @Test
    public void tstJobSubmitPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobSubmit jobSubmit = new JobSubmit(connection);
        assertNotNull(jobSubmit);
    }

    @Test
    public void tstJobSubmitPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobSubmit(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    void tstSubmitByLocalFileFileNotFoundFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobSubmit jobSubmit = new JobSubmit(connection);
        String nonExistentPath = tempDir.resolve("missing.jcl").toString();

        ZosmfRequestException exception = assertThrows(ZosmfRequestException.class,
                () -> jobSubmit.submitByLocalFile(nonExistentPath));

        assertTrue(exception.getMessage().contains("local file provided was not found"));
    }

    @Test
    void tstSubmitByLocalFileInvalidParameterFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobSubmit jobSubmit = new JobSubmit(connection);

        assertThrows(IllegalArgumentException.class,
                () -> jobSubmit.submitByLocalFile(null));
    }

}
