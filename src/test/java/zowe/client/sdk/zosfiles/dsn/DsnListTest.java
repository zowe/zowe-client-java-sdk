package zowe.client.sdk.zosfiles.dsn;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.ListParams;
import zowe.client.sdk.zosfiles.dsn.methods.DsnList;
import zowe.client.sdk.zosfiles.dsn.response.Member;
import zowe.client.sdk.zosfiles.dsn.types.AttributeType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

public class DsnListTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private GetJsonZosmfRequest mockGetRequest;
    private GetJsonZosmfRequest mockGetRequestToken;

    @Before
    public void init() throws ZosmfRequestException {
        mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        mockGetRequestToken = Mockito.mock(GetJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockGetRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockGetRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockGetRequestToken).getHeaders();
        doCallRealMethod().when(mockGetRequestToken).getUrl();
    }

    @Test
    public void tstDsnListSuccess() throws ZosmfRequestException {
        final DsnList dsnList = new DsnList(connection, mockGetRequest);
        final ListParams params = new ListParams.Builder().responseTimeout("10").volume("vol1").build();
        final List<Member> members = dsnList.getMembers("TEST.DATASET", params);
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET/member", mockGetRequest.getUrl());
    }

    @Test
    public void tstDsnListTokenSuccess() throws ZosmfRequestException {
        final DsnList dsnList = new DsnList(connection, mockGetRequestToken);
        final ListParams params = new ListParams.Builder().responseTimeout("10").volume("vol1").build();
        final List<Member> members = dsnList.getMembers("TEST.DATASET", params);
        assertEquals("{X-IBM-Max-Items=0, Accept-Encoding=gzip, X-IBM-Response-Timeout=10, " +
                        "X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET/member", mockGetRequestToken.getUrl());
    }

//    @Test
//    public void tstDsnListWithVolserSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder().responseTimeout("10").volume("vol1").build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("https://1:1/zosmf/restfiles/ds/-(VOL123)/TEST.DATASET", mockGetRequest.getUrl());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithBaseAttributeSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .attribute(AttributeType.BASE)
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Attributes=base, X-IBM-Max-Items=0}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithVolAttributeSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .attribute(AttributeType.VOL)
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Attributes=vol, X-IBM-Max-Items=0}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithMaxLengthSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .maxLength("100")
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Max-Items=100}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithResponseTimeoutSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .responseTimeout("30")
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Response-Timeout=30, X-IBM-Max-Items=0}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithRecallWaitSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .recall("wait")
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Migrated-Recall=wait, X-IBM-Max-Items=0}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithRecallNoWaitSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .recall("nowait")
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Migrated-Recall=nowait, X-IBM-Max-Items=0}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithRecallErrorSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .recall("error")
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Migrated-Recall=error, X-IBM-Max-Items=0}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }
//
//    @Test
//    public void tstDsnListWithMultipleParamsSuccess() throws ZosmfRequestException {
//        final DsnList dsnList = new DsnList(connection, mockGetRequest);
//        final ListParams params = new ListParams.Builder()
//                .attribute(AttributeType.BASE)
//                .maxLength("100")
//                .responseTimeout("30")
//                .recall("wait")
//                .build();
//        final Response response = dsnList.listCommon("TEST.DATASET", params);
//        assertEquals("{Accept-Encoding=gzip, X-IBM-Attributes=base, X-IBM-Max-Items=100, " +
//                "X-IBM-Response-Timeout=30, X-IBM-Migrated-Recall=wait}",
//                mockGetRequest.getHeaders().toString());
//        assertEquals(200, response.getStatusCode().orElse(-1));
//    }

    @Test
    public void tstSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        GetJsonZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        DsnList dsnList = new DsnList(connection, request);
        assertNotNull(dsnList);
    }

    @Test
    public void tstSecondaryConstructorWithNullConnection() {
        GetJsonZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnList(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnList(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a GetJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnList(connection, request)
        );
        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DsnList dsnList = new DsnList(connection);
        assertNotNull(dsnList);
    }

    @Test
    public void tstPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnList(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}