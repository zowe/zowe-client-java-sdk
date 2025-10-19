package zowe.client.sdk.zosmfinfo.methods;

import kong.unirest.core.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfinfo.response.ZosmfInfoResponse;

import static org.junit.jupiter.api.Assertions.*;

public class ZosmfStatusTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private GetJsonZosmfRequest mockJsonGetRequest;

    @BeforeEach
    public void init() {
        mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
    }

    @Test
    public void tstGetParsesValidResponseSuccess() throws ZosmfRequestException {
        String json = "{\n" +
                " \"zosmf_saf_realm\": \"REALM\",\n" +
                " \"zosmf_port\": \"1234\",\n" +
                " \"zosmf_full_version\": \"v1\",\n" +
                " \"plugins\": [{\"pluginVersion\":\"1\",\"pluginStatus\":\"active\",\"pluginDefaultName\":\"pluginA\"}],\n" +
                " \"api_version\": \"2\",\n" +
                " \"zos_version\": \"3\",\n" +
                " \"zosmf_version\": \"4\",\n" +
                " \"zosmf_hostname\": \"host\"\n" +
                "}";

        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "success"));

        ZosmfStatus status = new ZosmfStatus(connection, mockJsonGetRequest);
        ZosmfInfoResponse info = status.get();

        assertNotNull(info);
        assertEquals("https://1:1/zosmf", connection.getZosmfUrl());
        assertEquals("REALM", info.getZosmfSafRealm());
        assertEquals("1234", info.getZosmfPort());
        assertEquals("v1", info.getZosmfFullVersion());
        assertEquals(1, info.getZosmfPluginsInfo().length);
        assertEquals("pluginA", info.getZosmfPluginsInfo()[0].getPluginDefaultName());
    }

    @Test
    public void tstGetThrowsOnMissingResponseFailure() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response("{frank}", 200, "success"));
        ZosmfStatus status = new ZosmfStatus(connection, mockJsonGetRequest);
        try {
            assertThrows(ZosmfRequestException.class, status::get);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    public void tstGetThrowsOnMissingResponseMessageCheckOnFailure() throws ZosmfRequestException {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response("{frank}", 200, "success"));
        ZosmfStatus status = new ZosmfStatus(connection, mockJsonGetRequest);
        try {
            status.get();
        } catch (Exception e) {
            String errMsg = "Failed to parse JSON response for [get] into ZosmfInfoResponse";
            assertEquals(errMsg, e.getMessage());
        }
    }

}
