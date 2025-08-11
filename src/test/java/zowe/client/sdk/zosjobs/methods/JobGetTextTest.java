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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for JobGet.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class JobGetTextTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private GetTextZosmfRequest mockTextGetRequest;

    @Before
    public void init() {
        mockTextGetRequest = Mockito.mock(GetTextZosmfRequest.class);
    }

    @Test
    public void tstJobGetTextSpoolContentByIdSuccess() throws ZosmfRequestException {
        JobGet getJobs = new JobGet(connection);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response("1\n2\n3\n", 200, "success"));
        Whitebox.setInternalState(getJobs, "request", mockTextGetRequest);

        final String results = getJobs.getSpoolContent("jobName", "jobId", 1);
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records", getJobs.getUrl());
        assertEquals("1\n2\n3\n", results);
    }

    @Test
    public void tstJobGetTextSpoolContentCmdResponseWithInvalidBasePathFailure() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("1", "1", "1", "1", "consoles//");
        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_TEXT);
        final JobGet getJobs = new JobGet(connection, request);
        assertThrows(IllegalArgumentException.class, () -> getJobs.getSpoolContent("jobName", "jobId", 1));
    }

    @Test
    public void tstJobGetTextSpoolContentByIdToggleTokenSuccess() throws ZosmfRequestException {
        JobGet getJobs = new JobGet(tokenConnection);
        GetTextZosmfRequest mockTextGetRequestToken = Mockito.mock(GetTextZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Whitebox.setInternalState(getJobs, "request", mockTextGetRequestToken);
        Mockito.when(mockTextGetRequestToken.executeRequest()).thenReturn(
                new Response("1\n2\n3\n", 200, "success"));
        doCallRealMethod().when(mockTextGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockTextGetRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockTextGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockTextGetRequestToken).getHeaders();

        String results = getJobs.getSpoolContent("jobName", "jobId", 1);
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=text/plain; charset=UTF-8}",
                mockTextGetRequestToken.getHeaders().toString());
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records", getJobs.getUrl());
        assertEquals("1\n2\n3\n", results);
    }

}
