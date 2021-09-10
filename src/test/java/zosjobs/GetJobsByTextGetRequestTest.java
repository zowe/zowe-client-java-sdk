/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zosjobs;

import core.ZOSConnection;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import rest.Response;
import rest.TextGetRequest;

import static org.junit.Assert.assertTrue;

public class GetJobsByTextGetRequestTest {

    private TextGetRequest request;
    private ZOSConnection connection;
    private GetJobs getJobs;

    @Before
    public void init() {
        request = Mockito.mock(TextGetRequest.class);
        connection = new ZOSConnection("1", "1", "1", "1");
        getJobs = new GetJobs(connection);
        Whitebox.setInternalState(getJobs, "request", request);
    }

    @Test
    public void tstGetSpoolContentByIdSuccess() throws Exception {
        Response response = new Response("1\n2\n3\n", 200);
        Mockito.when(request.executeHttpRequest()).thenReturn(response);

        String results = getJobs.getSpoolContentById("jobName", "jobId", 1);
        assertTrue("https://1:1/zosmf/restjobs/jobs/jobName/jobId/files/1/records".equals(getJobs.getUrl()));
        assertTrue("1\n2\n3\n".equals(results));
    }

}