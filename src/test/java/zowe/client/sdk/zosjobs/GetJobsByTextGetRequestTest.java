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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.GetTextZosmfRequest;
import zowe.client.sdk.zosjobs.methods.JobGet;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for JobGet.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class GetJobsByTextGetRequestTest {

    private final ZosConnection connection = new ZosConnection("1", "1", "1", "1");
    private GetTextZosmfRequest mockTextGetRequest;
    private JobGet getJobs;

    @Before
    public void init() {
        mockTextGetRequest = Mockito.mock(GetTextZosmfRequest.class);
        getJobs = new JobGet(connection);
        Whitebox.setInternalState(getJobs, "request", mockTextGetRequest);
    }

    @Test
    public void tstGetSpoolContentByIdSuccess() throws Exception {
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response("1\n2\n3\n", 200, "success"));

        final String results = getJobs.getSpoolContent("jobName", "jobId", 1);
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records", getJobs.getUrl());
        assertEquals("1\n2\n3\n", results);
    }

}