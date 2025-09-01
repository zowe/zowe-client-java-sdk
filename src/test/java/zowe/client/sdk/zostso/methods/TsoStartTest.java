package zowe.client.sdk.zostso.methods;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ResponseUtil;
import zowe.client.sdk.zostso.TsoConstants;
import zowe.client.sdk.zostso.input.StartTsoInputData;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TsoStart class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoStartTest {

    private ZosConnection mockConnection;
    private PostJsonZosmfRequest mockRequest;
    private TsoStart tsoStart;

    /**
     * Setup executed before each test.
     * Creates mock connection and request objects for testing.
     */
    @BeforeEach
    public void setUp() {
        mockConnection = mock(ZosConnection.class);
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf:1234");
        mockRequest = mock(PostJsonZosmfRequest.class);
        tsoStart = new TsoStart(mockConnection, mockRequest);
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMocks();
        // This ensures any leftover static mocks are cleared between tests
        Mockito.clearAllCaches();
    }

    /**
     * Test successfully TsoStart execution when valid input and response are provided.
     * Ensures servletKey is correctly extracted from JSON response.
     */
    @Test
    public void tstTsoStartSuccess() throws Exception {
        StartTsoInputData input = new StartTsoInputData("LOGONPROC", "UTF-8",
                "1047", "24", "80", "ACCT123", "4096");

        // Mock ResponseUtil static call
        try (var mocked = Mockito.mockStatic(ResponseUtil.class)) {
            mocked.when(() -> ResponseUtil.getResponseStr(any(), any()))
                    .thenReturn("{\"servletKey\":\"mySession123\"}");

            final String result = tsoStart.start(input);
            assertEquals("mySession123", result);

            verify(mockRequest).setUrl(contains("acct=ACCT123"));
            verify(mockRequest).setBody("");
        }
    }

    /**
     * Test that TsoStart throws an exception when JSON parsing fails.
     * Ensures proper handling of invalid response formats.
     */
    @Test
    public void tstTsoStartInvalidJsonThrowsZosmfRequestExceptionWithCorrectMessageFailure() {
        StartTsoInputData input = new StartTsoInputData("LOGONPROC", "UTF-8",
                "1047", "24", "80", "ACCT123", "4096");

        try (var mocked = Mockito.mockStatic(ResponseUtil.class)) {
            mocked.when(() -> ResponseUtil.getResponseStr(any(), any()))
                    .thenReturn("invalid json response");

            ZosmfRequestException ex = assertThrows(ZosmfRequestException.class,
                    () -> tsoStart.start(input));
            assertTrue(ex.getMessage().contains("invalid json response"));
        }
    }

    /**
     * Test that TsoStart throws an exception when servletKey is null in the response.
     * Ensures invalid servletKey is detected properly.
     */
    @Test
    public void tstTsoStartNullServletKeyThrowsZosmfRequestExceptionWithCorrectMessageFailure() {
        StartTsoInputData input = new StartTsoInputData("LOGONPROC", "UTF-8",
                "1047", "24", "80", "ACCT123", "4096");

        try (var mocked = Mockito.mockStatic(ResponseUtil.class)) {
            mocked.when(() -> ResponseUtil.getResponseStr(any(), any()))
                    .thenReturn("{\"servletKey\":\"null\"}");

            ZosmfRequestException ex = assertThrows(ZosmfRequestException.class,
                    () -> tsoStart.start(input));
            assertTrue(ex.getMessage().contains("Response servletKey is empty"));
        }
    }

    /**
     * Test that TsoStart throws an exception when servletKey is missing in the response.
     */
    @Test
    public void tstTsoStartMissingServletKeyThrowsZosmfRequestExceptionFailure() {
        StartTsoInputData input = new StartTsoInputData("LOGONPROC", "UTF-8",
                "1047", "24", "80", "ACCT123", "4096");

        try (var mocked = Mockito.mockStatic(ResponseUtil.class)) {
            mocked.when(() -> ResponseUtil.getResponseStr(any(), any()))
                    .thenReturn("{}\n");

            ZosmfRequestException ex = assertThrows(ZosmfRequestException.class,
                    () -> tsoStart.start(input));
            assertTrue(ex.getMessage().contains("Response missing servletKey"));
        }
    }

    /**
     * Test that TsoStart throws an exception when servletKey is empty in the response.
     */
    @Test
    public void tstTsoStartEmptyServletKeyThrowsZosmfRequestExceptionFailure() {
        StartTsoInputData input = new StartTsoInputData("LOGONPROC", "UTF-8",
                "1047", "24", "80", "ACCT123", "4096");

        try (var mocked = Mockito.mockStatic(ResponseUtil.class)) {
            mocked.when(() -> ResponseUtil.getResponseStr(any(), any()))
                    .thenReturn("{\"servletKey\":\"\"}");

            ZosmfRequestException ex = assertThrows(ZosmfRequestException.class,
                    () -> tsoStart.start(input));
            System.out.println(ex.getMessage());
            assertTrue(ex.getMessage().contains("Response servletKey is empty"));
        }
    }

    /**
     * Test that the public constructor throws an exception if the connection is null.
     */
    @Test
    public void tstPublicConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoStart(null)
        );
        assertEquals("connection is null", ex.getMessage());
    }

    /**
     * Test that the alternative constructor throws an exception if the connection is null (TsoStartService).
     */
    @Test
    public void tstAlternativeConstructorNullConnectionForStartServiceFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoStart(null, mockRequest)
        );
        assertEquals("connection is null", ex.getMessage());
    }

    /**
     * Test successfully TsoStart execution when valid input and response are provided.
     * Ensures servletKey is correctly extracted from JSON response.
     */
    @Test
    public void tstTsoStartWithStartTsoInputDataSettersSuccess() throws Exception {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf");
        StartTsoInputData inputData = new StartTsoInputData();
        inputData.setAccount("ACCT123");
        inputData.setLogonProcedure("LOGONPROC");
        inputData.setCharacterSet("697");
        inputData.setCodePage("1047");
        inputData.setRows("24");
        inputData.setColumns("80");
        inputData.setRegionSize("4096");

        final String jsonResponse = "{\"servletKey\":\"TSO12345\"}";
        mockStatic(ResponseUtil.class);
        when(ResponseUtil.getResponseStr(eq(mockRequest), eq(TsoConstants.START_TSO_FAIL_MSG))).thenReturn(jsonResponse);

        final String result = tsoStart.start(inputData);
        assertEquals("TSO12345", result);
    }

    /**
     * Test that the URL set in the request matches the expected TSO start URL.
     */
    @Test
    public void tstTsoStartSetsCorrectUrlSuccess() throws Exception {
        StartTsoInputData inputData = new StartTsoInputData("PROC1", "UTF-8",
                "037", "24", "80", "ACCT123", "4096");

        doCallRealMethod().when(mockRequest).setUrl(any());
        doCallRealMethod().when(mockRequest).getUrl();

        // Mock response to return servletKey
        final Map<String, Object> map = new HashMap<>();
        map.put("servletKey", "SERVKEY123");
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(map), 200, "success"));

        final String result = tsoStart.start(inputData);
        assertEquals("SERVKEY123", result);

        final String expectedUrl = "https://zosmf:1234/tsoApp/tso?acct=ACCT123&proc=PROC1&chset=UTF-8" +
                "&cpage=037&rows=24&cols=80&rsize=4096";
        final String actualUrl = mockRequest.getUrl();
        assertEquals(expectedUrl, actualUrl);
    }

    /**
     * Test that the headers set in the request contain the expected values.
     */
    @Test
    public void tstTsoStartSetsCorrectHeadersSuccess() throws Exception {
        StartTsoInputData inputData = new StartTsoInputData("PROC1", "UTF-8",
                "037", "24", "80", "ACCT123", "4096");

        // to reach the ZoweRequest constructor where it performs setStandardHeaders using
        // withSettings().useConstructor() to force constructor being used.
        PostJsonZosmfRequest postJsonZosmfRequest = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(ZosConnectionFactory
                        .createBasicConnection("1", "1", "1", "1")));

        doCallRealMethod().when(postJsonZosmfRequest).setStandardHeaders();
        doCallRealMethod().when(postJsonZosmfRequest).setHeaders(anyMap());
        doCallRealMethod().when(postJsonZosmfRequest).getHeaders();

        // Mock response to return servletKey
        try (var mocked = Mockito.mockStatic(ResponseUtil.class)) {
            mocked.when(() -> ResponseUtil.getResponseStr(any(), any()))
                    .thenReturn("{\"servletKey\":\"SERVKEY123\"}");

            tsoStart = new TsoStart(mockConnection, postJsonZosmfRequest);

            final String result = tsoStart.start(inputData);
            assertEquals("SERVKEY123", result);

            Map<String, String> headers = postJsonZosmfRequest.getHeaders();

            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("true", headers.get("X-CSRF-ZOSMF-HEADER"));
        }
    }

    /**
     * Test that the TsoStart throws an exception if the inputData is null.
     */
    @Test
    public void tstTsoStartNullInputDataFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> tsoStart.start(null)
        );
        assertEquals("inputData is null", ex.getMessage());
    }

    /**
     * Test that the TsoStart throws an exception if the account number is not specified.
     */
    @Test
    public void tstTsoStartMissingAccountFailure() {
        StartTsoInputData input = new StartTsoInputData();
        input.setLogonProcedure("LOGONPROC");

        ZosmfRequestException ex = assertThrows(ZosmfRequestException.class,
                () -> tsoStart.start(input));
        assertEquals("accountNumber is not specified", ex.getMessage());
    }

    /**
     * Test that the alternative constructor throws an exception if the connection is null.
     */
    @Test
    public void tstAlternativeConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoStop(null, mockRequest)
        );
        assertEquals("connection is null", ex.getMessage());
    }


    /**
     * Test that the alternative constructor throws an exception if the request is null.
     */
    @Test
    public void tstAlternativeConstructorNullRequestFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoStart(mockConnection, null)
        );
        assertEquals("request is null", ex.getMessage());
    }

    /**
     * Test that the alternative constructor throws an exception if a request type is not PostJsonZosmfRequest.
     * Verifies both the exception type and the error message.
     */
    @Test
    public void tstAlternativeConstructorWrongRequestTypeFailure() {
        final ZosmfRequest wrongRequest = mock(ZosmfRequest.class);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> new TsoStart(mockConnection, wrongRequest)
        );
        assertEquals("POST_JSON request type required", ex.getMessage());
    }

}
