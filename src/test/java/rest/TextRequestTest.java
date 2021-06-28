package rest;

import core.ZOSConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class TextRequestTest {

    private HttpClient httpClient;
    private JsonRequest getRequest;
    private JsonRequest putRequest;

    @Before
    public void init() {
        HttpGet httpGet = Mockito.mock(HttpGet.class);
        httpClient = Mockito.mock(HttpClient.class);
        ZOSConnection connection = new ZOSConnection("", "", "", "");

        getRequest = new JsonRequest(connection, httpGet);
        Whitebox.setInternalState(getRequest, "client", httpClient);

        HttpPut httpPut = Mockito.mock(HttpPut.class);
        putRequest = new JsonRequest(connection, httpPut, "");
        Whitebox.setInternalState(putRequest, "client", httpClient);
    }

    @Test
    public void tstHttpGetThrowsException() throws IOException {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class)))
               .thenThrow(new IOException());

        assertThrows(IOException.class, getRequest::httpGet);
        Mockito.verify(httpClient, Mockito.times(1))
               .execute(any(HttpGet.class), any(ResponseHandler.class));
    }

    @Test
    public void tstHttpPutThrowsException() throws IOException {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class)))
               .thenThrow(new IOException());

        assertThrows(IOException.class, putRequest::httpPut);
        Mockito.verify(httpClient, Mockito.times(1))
               .execute(any(HttpPut.class), any(ResponseHandler.class));
    }

}
