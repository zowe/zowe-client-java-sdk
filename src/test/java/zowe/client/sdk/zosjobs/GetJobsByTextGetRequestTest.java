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
import zowe.client.sdk.core.ZOSConnection;
import zowe.client.sdk.rest.unirest.Response;
import zowe.client.sdk.rest.unirest.TextGetRequest;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit tests for GetJobs.
 *
 * @author Frank Giordano
 */
public class GetJobsByTextGetRequestTest {

    private TextGetRequest request;
    private GetJobs getJobs;

    @Before
    public void init() {
        request = Mockito.mock(TextGetRequest.class);
        ZOSConnection connection = new ZOSConnection("1", "1", "1", "1");
        getJobs = new GetJobs(connection);
        Whitebox.setInternalState(getJobs, "request", request);
    }

    @Test
    public void tstGetSpoolContentByIdSuccess() throws Exception {
        Response response = new Response("1\n2\n3\n", 200, "text");
        Mockito.when(request.executeRequest()).thenReturn(response);

        String results = getJobs.getSpoolContentById("jobName", "jobId", 1);
        assertEquals("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records", getJobs.getUrl());
        assertEquals("1\n2\n3\n", results);
    }

}