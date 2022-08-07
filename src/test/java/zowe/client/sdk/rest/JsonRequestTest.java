/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

import org.apache.http.HttpResponse;
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
import zowe.client.sdk.core.ZOSConnection;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(MockitoJUnitRunner.class)
public class JsonRequestTest {

    private HttpClient httpClient;
    private JsonGetRequest getRequest;
    private JsonPutRequest putRequest;

    @Before
    public void init() throws Exception {
        HttpResponse httpResponse = Mockito.mock(HttpResponse.class);
        httpClient = Mockito.mock(HttpClient.class);
        ZOSConnection connection = new ZOSConnection("1", "1", "1", "1");

        getRequest = new JsonGetRequest(connection);
        getRequest.setRequest("http://url:23");
        Whitebox.setInternalState(getRequest, "client", httpClient);
        Whitebox.setInternalState(getRequest, "httpResponse", httpResponse);

        putRequest = new JsonPutRequest(connection);
        putRequest.setRequest("http://url:23", "body");
        Whitebox.setInternalState(putRequest, "client", httpClient);
        Whitebox.setInternalState(putRequest, "httpResponse", httpResponse);
    }

    @Test
    public void tstHttpGetEmptyResponseIOExceptionFailure() throws Exception {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenThrow(new IOException());

        Response response = null;
        try {
            response = getRequest.executeRequest();
        } catch (Exception ignored) {
        }
        Assertions.assertNull(response);
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpGet.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpGetReturnsEmptyResponsePhraseForInvalidJsonFailure() throws Exception {
        HttpResponseInvalidJsonMock httpResponseInvalidJsonMock = new HttpResponseInvalidJsonMock();
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenReturn(httpResponseInvalidJsonMock.getHttpResponse());

        Response response = getRequest.executeRequest();
        Assertions.assertTrue(response.getResponsePhrase().isEmpty());
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpGet.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpGetReturnsJsonSuccess() throws Exception {
        String json = "{\"data\":{}}";

        HttpResponseMock httpResponseMock = new HttpResponseMock();
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenReturn(httpResponseMock.getHttpResponse());

        Response response = getRequest.executeRequest();
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpGet.class), any(BasicHttpContext.class));

        Assertions.assertEquals(json, response.getResponsePhrase().get().toString());
    }

    @Test
    public void tstHttpPutEmptyResponseIOExceptionFailure() throws Exception {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenThrow(new IOException());

        Response response = null;
        try {
            response = putRequest.executeRequest();
        } catch (Exception ignored) {
        }
        Assertions.assertNull(response);
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpPut.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpPutReturnsEmptyResponsePhraseForInvalidJsonFailure() throws Exception {
        HttpResponseInvalidJsonMock httpResponseInvalidJsonMock = new HttpResponseInvalidJsonMock();
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenReturn(httpResponseInvalidJsonMock.getHttpResponse());

        Response response = putRequest.executeRequest();
        Assertions.assertTrue(response.getResponsePhrase().isEmpty());
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpPut.class), any(BasicHttpContext.class));
    }

    @Test
    public void tstHttpPutReturnsJsonSuccess() throws Exception {
        String json = "{\"data\":{}}";

        HttpResponseMock httpResponseMock = new HttpResponseMock();
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(BasicHttpContext.class)))
                .thenReturn(httpResponseMock.getHttpResponse());

        Response response = putRequest.executeRequest();
        Mockito.verify(httpClient, Mockito.times(1))
                .execute(any(HttpPut.class), any(BasicHttpContext.class));

        Assertions.assertEquals(json, response.getResponsePhrase().get().toString());
    }

}
