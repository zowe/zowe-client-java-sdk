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
import org.apache.http.*;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class JsonRequestTest {

    private HttpClient httpClient;
    private JsonRequest getRequest;
    private JsonRequest putRequest;
    private HttpResponse httpResponse;

    @Before
    public void init() {
        httpResponse = Mockito.mock(HttpResponse.class);
        HttpGet httpGet = Mockito.mock(HttpGet.class);
        httpClient = Mockito.mock(HttpClient.class);
        ZOSConnection connection = new ZOSConnection("", "", "", "");

        getRequest = new JsonRequest(connection, httpGet);
        Whitebox.setInternalState(getRequest, "client", httpClient);
        Whitebox.setInternalState(getRequest, "httpResponse", httpResponse);

        HttpPut httpPut = Mockito.mock(HttpPut.class);
        putRequest = new JsonRequest(connection, httpPut, Optional.empty());
        Whitebox.setInternalState(putRequest, "client", httpClient);
        Whitebox.setInternalState(putRequest, "httpResponse", httpResponse);
    }

    @Test
    public void tstHttpGetNullIOException() throws Exception {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
               .thenThrow(new IOException());

        Assertions.assertEquals((Byte) getRequest.httpGet(), null);
        Mockito.verify(httpClient, Mockito.times(1))
               .execute(any(HttpGet.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpGetReturnsNullForInvalidJson() throws Exception {
        HttpResponseInvalidJsonMock httpResponseInvalidJsonMock = new HttpResponseInvalidJsonMock();
        Mockito.when(httpResponse.getEntity()).thenReturn(httpResponseInvalidJsonMock.getHttpResponse().getEntity());
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
               .thenReturn(httpResponseInvalidJsonMock.getHttpResponse());

        Assertions.assertNull(getRequest.httpGet());
        Mockito.verify(httpClient, Mockito.times(1))
               .execute(any(HttpGet.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpGetReturnsJson() throws Exception {
        String json = "{\"data\":{}}";

        HttpResponseMock httpResponseMock = new HttpResponseMock();
        Mockito.when(httpResponse.getEntity()).thenReturn(httpResponseMock.getHttpResponse().getEntity());
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
               .thenReturn(httpResponseMock.getHttpResponse());

        Response response = getRequest.httpGet();
        Mockito.verify(httpClient, Mockito.times(1))
               .execute(any(HttpGet.class), any(BasicHttpContext.class));

        Assertions.assertEquals(json, response.getResponsePhrase().get().toString());
    }

    @Test
    public void tstHttpPutNullIOException() throws Exception {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenThrow(new IOException());

        Assertions.assertEquals((Byte) putRequest.httpPut(), null);
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpPut.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpPutReturnsNullForInvalidJson() throws Exception {
        HttpResponseInvalidJsonMock httpResponseInvalidJsonMock = new HttpResponseInvalidJsonMock();
        Mockito.when(httpResponse.getEntity()).thenReturn(httpResponseInvalidJsonMock.getHttpResponse().getEntity());
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenReturn(httpResponseInvalidJsonMock.getHttpResponse());

        Assertions.assertNull(putRequest.httpPut());
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpPut.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpPutReturnsJson() throws Exception {
        String json = "{\"data\":{}}";

        HttpResponseMock httpResponseMock = new HttpResponseMock();
        Mockito.when(httpResponse.getEntity()).thenReturn(httpResponseMock.getHttpResponse().getEntity());
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenReturn(httpResponseMock.getHttpResponse());

        Response response = putRequest.httpPut();
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpPut.class), any(BasicHttpContext.class));

        Assertions.assertEquals(json, response.getResponsePhrase().get().toString());
    }

}
