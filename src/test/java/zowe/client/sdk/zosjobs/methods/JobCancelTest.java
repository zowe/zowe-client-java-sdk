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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for JobCancel.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobCancelTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockPutJsonZosmfRequest;
    private PutJsonZosmfRequest mockPutJsonZosmfRequestToken;

    @Before
    public void init() throws ZosmfRequestException {
        mockPutJsonZosmfRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutJsonZosmfRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockPutJsonZosmfRequest).setUrl(any());
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
    }

    @Test
    public void tstJobCancelSuccess() throws ZosmfRequestException {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonZosmfRequest);
        final Response response = jobCancel.cancel("JOBNAME", "JOBID", "1.0");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restjobs/jobs/JOBNAME/JOBID", mockPutJsonZosmfRequest.getUrl());
    }

    @Test
    public void tstJobCancelTokenSuccess() throws ZosmfRequestException {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonZosmfRequestToken);
        final Response response = jobCancel.cancel("JOBNAME", "JOBID", "1.0");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutJsonZosmfRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restjobs/jobs/JOBNAME/JOBID", mockPutJsonZosmfRequestToken.getUrl());
    }

    @Test
    public void tstJobCancelWithVersionTokenSuccess() throws ZosmfRequestException {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonZosmfRequestToken);
        final Response response = jobCancel.cancel("JOBNAME", "JOBID", "2.0");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutJsonZosmfRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restjobs/jobs/JOBNAME/JOBID", mockPutJsonZosmfRequestToken.getUrl());
    }

    @Test
    public void tstJobCancelWithInvalidVersionFailure() {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonZosmfRequest);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jobCancel.cancel("JOBNAME", "JOBID", "3.0")
        );
        assertEquals("invalid version specified", exception.getMessage());
    }

    @Test
    public void tstJobCancelSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        JobCancel jobCancel = new JobCancel(connection, request);
        assertNotNull(jobCancel);
    }

    @Test
    public void tstJobCancelSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobCancel(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstJobCancelSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobCancel(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstJobCancelSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JobCancel(connection, request)
        );
        assertEquals("PUT_JSON or DELETE_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstJobCancelPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobCancel jobCancel = new JobCancel(connection);
        assertNotNull(jobCancel);
    }

    @Test
    public void tstJobCancelPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobCancel(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
