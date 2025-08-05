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

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.methods.JobSubmit;
import zowe.client.sdk.zosjobs.response.Job;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for JobSubmit.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobSubmitTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockPutJsonZosmfRequest;
    private PutJsonZosmfRequest mockPutJsonZosmfRequestToken;

    @Before
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
        Mockito.when(mockPutJsonZosmfRequest.executeRequest()).thenReturn(
                new Response(jobJson, 200, "success"));
        doCallRealMethod().when(mockPutJsonZosmfRequest).setUrl(any());
        doCallRealMethod().when(mockPutJsonZosmfRequest).getUrl();

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
    public void tstJobDeleteSuccess() throws ZosmfRequestException {
        final JobSubmit jobSubmit = new JobSubmit(connection, mockPutJsonZosmfRequest);
        final Job job = jobSubmit.submit("TEST.DATASET");
        assertEquals("https://1:1/zosmf/restjobs/jobs", mockPutJsonZosmfRequest.getUrl());
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
    public void tstJobDeleteTokenSuccess() throws ZosmfRequestException {
        final JobSubmit jobSubmit = new JobSubmit(connection, mockPutJsonZosmfRequestToken);
        final Job job = jobSubmit.submit("TEST.DATASET");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutJsonZosmfRequestToken.getHeaders().toString());
        assertEquals("https://1:1/zosmf/restjobs/jobs", mockPutJsonZosmfRequestToken.getUrl());
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
    public void tstSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        JobSubmit jobSubmit = new JobSubmit(connection, request);
        assertNotNull(jobSubmit);
    }

    @Test
    public void tstSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobSubmit(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobSubmit(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); 
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JobSubmit(connection, request)
        );
        assertEquals("PUT_JSON or PUT_TEXT request type required", exception.getMessage());
    }

    @Test
    public void tstPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobSubmit jobSubmit = new JobSubmit(connection);
        assertNotNull(jobSubmit);
    }

    @Test
    public void tstPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobSubmit(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
