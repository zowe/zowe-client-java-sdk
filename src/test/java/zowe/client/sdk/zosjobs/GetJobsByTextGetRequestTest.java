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
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import zowe.client.sdk.core.AuthenicationType;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.GetTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosjobs.methods.JobGet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for JobGet.
 *
 * @author Frank Giordano
 * @version 3.0
 */
public class GetJobsByTextGetRequestTest {

    private final ZosConnection connection = new ZosConnection.Builder(AuthenicationType.CLASSIC)
            .host("1").password("1").user("1").zosmfPort("1").build();
    private final ZosConnection cookieConnection = new ZosConnection.Builder(AuthenicationType.COOKIE)
            .host("1").zosmfPort("1").cookie(new Cookie("hello=hello")).build();
    private GetTextZosmfRequest mockTextGetRequest;

    @Before
    public void init() {
        mockTextGetRequest = Mockito.mock(GetTextZosmfRequest.class);
    }

    @Test
    public void tstGetSpoolContentByIdSuccess() throws ZosmfRequestException {
        JobGet getJobs = new JobGet(connection);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response("1\n2\n3\n", 200, "success"));
        Whitebox.setInternalState(getJobs, "request", mockTextGetRequest);

        final String results = getJobs.getSpoolContent("jobName", "jobId", 1);
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records", getJobs.getUrl());
        assertEquals("1\n2\n3\n", results);
    }

    @Test
    public void tstGetSpoolContentByIdToggleAuthSuccess() throws ZosmfRequestException {
        JobGet getJobs = new JobGet(cookieConnection);
        GetTextZosmfRequest mockTextGetRequestAuth = Mockito.mock(GetTextZosmfRequest.class,
                withSettings().useConstructor(cookieConnection));
        Whitebox.setInternalState(getJobs, "request", mockTextGetRequestAuth);
        Mockito.when(mockTextGetRequestAuth.executeRequest()).thenReturn(
                new Response("1\n2\n3\n", 200, "success"));
        doCallRealMethod().when(mockTextGetRequestAuth).setHeaders(anyMap());
        doCallRealMethod().when(mockTextGetRequestAuth).setStandardHeaders();
        doCallRealMethod().when(mockTextGetRequestAuth).setUrl(any());
        doCallRealMethod().when(mockTextGetRequestAuth).getHeaders();

        String results = getJobs.getSpoolContent("jobName", "jobId", 1);
        Assertions.assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=text/plain; charset=UTF-8}",
                mockTextGetRequestAuth.getHeaders().toString());
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records", getJobs.getUrl());
        assertEquals("1\n2\n3\n", results);
    }

}