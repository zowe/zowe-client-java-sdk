package rest;

import core.ZOSConnection;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

public class JsonRequestTest {

    private HttpClient httpClient;
    private JsonRequest request;

    @BeforeEach
    public void init() {
        HttpGet httpGet = Mockito.mock(HttpGet.class);
        httpClient = Mockito.mock(HttpClient.class);
        ZOSConnection connection = new ZOSConnection("", "", "", "");

        request = new JsonRequest(connection, httpGet);
        Whitebox.setInternalState(request, "client", httpClient);

        HttpPut httpPut = Mockito.mock(HttpPut.class);
        request = new JsonRequest(connection, httpPut, "");
        Whitebox.setInternalState(request, "client", httpClient);
    }

    @Test
    public void testHttpGetThrowsException() throws IOException {
        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class)))
            .thenThrow(new IOException());

        assertThrows(IOException.class, request::httpGet);
    }

    @Test
    public void testHttpGetReturnsNullForInvalidJson() throws IOException {
        String invalidJson = UUID.randomUUID().toString();

        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class)))
            .thenReturn(invalidJson);

        Assertions.assertNull(request.httpGet());
    }

    @Test
    public void testHttpGetReturnsJson() throws IOException {
        String json = "{\"data\":{}}";

        Mockito.when(httpClient.execute(any(HttpUriRequest.class), any(ResponseHandler.class)))
            .thenReturn(json);

        JSONObject jsonObject = request.httpGet();
        Assertions.assertEquals(json, jsonObject.toString());
    }
}
