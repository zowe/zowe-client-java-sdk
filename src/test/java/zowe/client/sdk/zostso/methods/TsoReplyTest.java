/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.methods;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequestFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;
import zowe.client.sdk.utility.ResponseUtil;
import zowe.client.sdk.zostso.TsoConstants;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TsoReply class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoReplyTest {

    private final ZosConnection mockConnection = mock(ZosConnection.class);
    private final PutJsonZosmfRequest mockPutRequest = mock(PutJsonZosmfRequest.class);

    public TsoReplyTest() {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf:1443");
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMocks();
        // This ensures any leftover static mocks are cleared between tests
        Mockito.clearAllCaches();
    }

    /**
     * Test reply sets the correct URL in the request.
     */
    @Test
    public void tstTsoReplySetsCorrectUrlSuccess() throws Exception {
        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();

        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{}");

            final TsoReply tsoReply = new TsoReply(mockConnection, mockPutRequest);
            tsoReply.reply("SESSION123");

            final String actualUrl = mockPutRequest.getUrl();
            assertEquals("https://zosmf:1443/tsoApp/tso/SESSION123", actualUrl);
        }
    }

    /**
     * Test reply sets the correct headers in the request.
     */
    @Test
    public void tstTsoReplySetsCorrectHeadersSuccess() throws Exception {
        PutJsonZosmfRequest putJsonZosmfRequest = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(ZosConnectionFactory
                        .createBasicConnection("1", "1", "1", "1")));

        doCallRealMethod().when(putJsonZosmfRequest).setStandardHeaders();
        doCallRealMethod().when(putJsonZosmfRequest).setHeaders(anyMap());
        doCallRealMethod().when(putJsonZosmfRequest).getHeaders();

        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{}");

            final TsoReply tsoReply = new TsoReply(mockConnection, putJsonZosmfRequest);
            tsoReply.reply("SESSION123");

            Map<String, String> headers = putJsonZosmfRequest.getHeaders();

            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("true", headers.get("X-CSRF-ZOSMF-HEADER"));
            final String authHeaderValue = headers.get("Authorization");
            assertTrue(authHeaderValue.startsWith("Basic "));
        }
    }

    /**
     * Test TsoReply propagates ZosmfRequestException from ResponseUtil.
     */
    @Test
    public void tstTsoReplyThrowsZosmfRequestExceptionOnResponseErrorFailure() {
        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenThrow(new ZosmfRequestException("Reply failed"));

            final TsoReply tsoReply = new TsoReply(mockConnection, mockPutRequest);

            ZosmfRequestException ex = assertThrows(
                    ZosmfRequestException.class,
                    () -> tsoReply.reply("SESSION123")
            );

            assertEquals("Reply failed", ex.getMessage());
        }
    }

    /**
     * Test TsoReply succeeds when the response is valid.
     */
    @Test
    public void tstTsoReplySuccess() {
        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{\"status\":\"ok\"}");

            final TsoReply tsoReply = new TsoReply(mockConnection, mockPutRequest);
            assertDoesNotThrow(() -> tsoReply.reply("SESSION123"));
        }
    }

    /**
     * Test that the alternative constructor throws an exception if the connection is null.
     */
    @Test
    public void tstAlternativeConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoReply(null, mockPutRequest)
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
                () -> new TsoReply(mockConnection, null)
        );
        assertEquals("request is null", ex.getMessage());
    }

    /**
     * Test that the alternative constructor throws an exception if a request type is not PutJsonZosmfRequest.
     */
    @Test
    public void tstAlternativeConstructorWrongRequestTypeFailure() {
        final ZosmfRequest wrongRequest = mock(ZosmfRequest.class);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> new TsoReply(mockConnection, wrongRequest)
        );
        assertEquals("PUT_JSON request type required", ex.getMessage());
    }

    /**
     * Test that when the TsoReply is constructed without an initial request (alternative constructor),
     * the reply method builds a new PutJsonZosmfRequest using the factory, sets the correct
     * URL and body, and calls ResponseUtil.getResponseStr. Verifies that the mocked response
     * is returned as the result.
     * <p>
     * This ensures the service correctly handles the case where no request
     * object was provided through the alternative constructor.
     */
    @Test
    public void tstTsoReplyBuildsNewRequestWhenRequestIsNullSuccess() throws Exception {
        try (MockedStatic<ZosmfRequestFactory> factoryMock = mockStatic(ZosmfRequestFactory.class);
             MockedStatic<ResponseUtil> responseMock = mockStatic(ResponseUtil.class)) {

            factoryMock.when(() -> ZosmfRequestFactory.buildRequest(mockConnection, ZosmfRequestType.PUT_JSON))
                    .thenReturn(mockPutRequest);
            responseMock.when(() -> ResponseUtil.getResponseStr(mockPutRequest, TsoConstants.SEND_TSO_FAIL_MSG))
                    .thenReturn("{\"status\":\"ok\"}");

            final TsoReply tsoReply = new TsoReply(mockConnection);
            String result = tsoReply.reply("SESSION999");

            assertEquals("{\"status\":\"ok\"}", result);
        }
    }

    /**
     * Test that TsoReply throws when sessionId is null or empty.
     */
    @Test
    public void tstTsoReplyNullSessionIdFailure() {
        final TsoReply tsoReply = new TsoReply(mockConnection, mockPutRequest);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> tsoReply.reply(null)
        );
        assertEquals("sessionId is either null or empty", ex.getMessage());
    }

    /**
     * Test that the public constructor throws when connection is null.
     */
    @Test
    public void tstPublicConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoReply(null)
        );
        assertEquals("connection is null", ex.getMessage());
    }
}
