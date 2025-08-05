package zowe.client.sdk.zosfiles.dsn;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.methods.DsnDelete;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

public class DsnDeleteTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private DeleteJsonZosmfRequest mockDeleteRequest;
    private DeleteJsonZosmfRequest mockDeleteRequestToken;

    @Before
    public void init() throws ZosmfRequestException {
        mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        Mockito.when(mockDeleteRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();

        mockDeleteRequestToken = Mockito.mock(DeleteJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockDeleteRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockDeleteRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockDeleteRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockDeleteRequestToken).setUrl(any());
        doCallRealMethod().when(mockDeleteRequestToken).getHeaders();
        doCallRealMethod().when(mockDeleteRequestToken).getUrl();
    }

    @Test
    public void tstDsnDeleteDatasetSuccess() throws ZosmfRequestException {
        final DsnDelete dsnDelete = new DsnDelete(connection, mockDeleteRequest);
        final Response response = dsnDelete.delete("TEST.DATASET");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockDeleteRequest.getUrl());
    }

    @Test
    public void tstDsnDeleteTokenSuccess() throws ZosmfRequestException {
        final DsnDelete dsnDelete = new DsnDelete(connection, mockDeleteRequestToken);
        final Response response = dsnDelete.delete("TEST.DATASET");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}", mockDeleteRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockDeleteRequestToken.getUrl());
    }

    @Test
    public void tstDsnDeleteWithDatasetMemberNotationSuccess() throws ZosmfRequestException {
        final DsnDelete dsnDelete = new DsnDelete(connection, mockDeleteRequest);
        final Response response = dsnDelete.delete("TEST.DATASET(MEMBER)");
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET(MEMBER)", mockDeleteRequest.getUrl());
        assertEquals(200, response.getStatusCode().orElse(-1));
    }

    @Test
    public void tstDsnDeleteWithDatasetMemberSeparateParametersSuccess() throws ZosmfRequestException {
        final DsnDelete dsnDelete = new DsnDelete(connection, mockDeleteRequest);
        final Response response = dsnDelete.delete("TEST.DATASET", "MEMBER");
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET(MEMBER)", mockDeleteRequest.getUrl());
        assertEquals(200, response.getStatusCode().orElse(-1));
    }

    @Test
    public void tstSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        DsnDelete dsnDelete = new DsnDelete(connection, request);
        assertNotNull(dsnDelete);
    }

    @Test
    public void tstSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(DeleteJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnDelete(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnDelete(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnDelete(connection, request)
        );
        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DsnDelete dsnDelete = new DsnDelete(connection);
        assertNotNull(dsnDelete);
    }

    @Test
    public void tstPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnDelete(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }
}
