/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package rest;

import core.ZOSConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.BasicHttpContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TextRequestTest {

    private HttpClient httpClient;
    private TextGetRequest getRequest;
    private TextPutRequest putRequest;

    @Before
    public void init() throws Exception {
        httpClient = Mockito.mock(HttpClient.class);
        ZOSConnection connection = new ZOSConnection("1", "1", "1", "1");

        getRequest = new TextGetRequest(connection, "http://url:23");
        Whitebox.setInternalState(getRequest, "client", httpClient);

        putRequest = new TextPutRequest(connection, "http://url:23", "body");
        Whitebox.setInternalState(putRequest, "client", httpClient);
    }

    @Test
    public void tstHttpGetEmptyResponseThrowsException() throws Exception {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenThrow(new IOException());

        Response response = getRequest.executeHttpRequest();
        Assertions.assertTrue(response.isEmpty());
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpGet.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpPutEmptyResponseThrowsException() throws Exception {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenThrow(new IOException());

        Response response = putRequest.executeHttpRequest();
        Assertions.assertTrue(response.isEmpty());
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpPut.class), any(BasicHttpContext.class));
    }

}
