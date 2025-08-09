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
    private PutJsonZosmfRequest mockPutJsonRequest;
    private PutJsonZosmfRequest mockPutJsonRequestToken;

    @Before
    public void init() throws ZosmfRequestException {
        mockPutJsonRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockPutJsonRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockPutJsonRequest).setUrl(any());
        doCallRealMethod().when(mockPutJsonRequest).getUrl();
        doCallRealMethod().when(mockPutJsonRequest).setupRequest();

        mockPutJsonRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPutJsonRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockPutJsonRequestToken).setupRequest();
        doCallRealMethod().when(mockPutJsonRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPutJsonRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPutJsonRequestToken).setUrl(any());
        doCallRealMethod().when(mockPutJsonRequestToken).getHeaders();
        doCallRealMethod().when(mockPutJsonRequestToken).getUrl();
        doCallRealMethod().when(mockPutJsonRequestToken).setupRequest();
    }

    @Test
    public void tstJobCancelSuccess() throws ZosmfRequestException {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonRequestToken);
        final Response response = jobCancel.cancel("JOBNAME", "JOBID", "1.0");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restjobs/jobs/JOBNAME/JOBID", mockPutJsonRequestToken.getUrl());
    }

    @Test
    public void tstJobCancelTokenSuccess() throws ZosmfRequestException {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonRequestToken);
        final Response response = jobCancel.cancel("JOBNAME", "JOBID", "1.0");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutJsonRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restjobs/jobs/JOBNAME/JOBID", mockPutJsonRequestToken.getUrl());
    }

    @Test
    public void tstJobCancelWithVersion2TokenSuccess() throws ZosmfRequestException {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonRequestToken);
        final Response response = jobCancel.cancel("JOBNAME", "JOBID", "2.0");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPutJsonRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restjobs/jobs/JOBNAME/JOBID", mockPutJsonRequestToken.getUrl());
    }

    @Test
    public void tstJobCancelWithInvalidVersionFailure() {
        final JobCancel jobCancel = new JobCancel(connection, mockPutJsonRequestToken);
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> jobCancel.cancel("JOBNAME", "JOBID", "3.0")
        );
        assertEquals("invalid version specified", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        JobCancel jobCancel = new JobCancel(connection, request);
        assertNotNull(jobCancel);
    }

    @Test
    public void tstSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobCancel(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobCancel(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new JobCancel(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        JobCancel jobCancel = new JobCancel(connection);
        assertNotNull(jobCancel);
    }

    @Test
    public void tstPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new JobCancel(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
