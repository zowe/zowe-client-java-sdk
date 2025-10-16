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
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.TsoUtils;
import zowe.client.sdk.zostso.response.TsoCommonResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TsoPing class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class TsoPingTest {

    private final ZosConnection mockConnection = mock(ZosConnection.class);
    private final PutJsonZosmfRequest mockPutRequest = mock(PutJsonZosmfRequest.class);

    public TsoPingTest() {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf:1443");
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMocks();
        // Ensure static mocks are cleared between tests
        Mockito.clearAllCaches();
    }

    /**
     * Test TsoPing sets the correct URL in the request.
     */
    @Test
    public void tstTsoPingSetsCorrectUrlSuccess() throws Exception {
        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();

        try (MockedStatic<TsoUtils> mockResponseUtil = mockStatic(TsoUtils.class)) {
            mockResponseUtil.when(() -> TsoUtils.getResponseStr(any()))
                    .thenReturn("{}");

            final TsoPing tsoPing = new TsoPing(mockConnection, mockPutRequest);
            tsoPing.ping("SERVKEY123");

            final String actualUrl = mockPutRequest.getUrl();
            assertEquals("https://zosmf:1443/tsoApp/tso/ping/SERVKEY123", actualUrl);
        }
    }

    /**
     * Test TsoPing sets the correct headers in the request.
     */
    @Test
    public void tstTsoPingSetsCorrectHeadersSuccess() throws Exception {
        PutJsonZosmfRequest putJsonZosmfRequest = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(ZosConnectionFactory
                        .createBasicConnection("1", "1", "1", "1")));

        doCallRealMethod().when(putJsonZosmfRequest).setStandardHeaders();
        doCallRealMethod().when(putJsonZosmfRequest).setHeaders(anyMap());
        doCallRealMethod().when(putJsonZosmfRequest).getHeaders();

        try (MockedStatic<TsoUtils> mockResponseUtil = mockStatic(TsoUtils.class)) {
            mockResponseUtil.when(() -> TsoUtils.getResponseStr(any()))
                    .thenReturn("{}");

            final TsoPing tsoPing = new TsoPing(mockConnection, putJsonZosmfRequest);
            tsoPing.ping("SERVKEY123");

            Map<String, String> headers = putJsonZosmfRequest.getHeaders();

            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("true", headers.get("X-CSRF-ZOSMF-HEADER"));
            final String authHeaderValue = headers.get("Authorization");
            assertTrue(authHeaderValue.startsWith("Basic "));
        }
    }

    /**
     * Test TsoPing succeeds when response is valid.
     */
    @Test
    public void tstTsoPingSuccess() throws ZosmfRequestException {
        final String payload = "{\"servletKey\":\"ZOSMFAD-71-xyz\",\"ver\":\"0100\",\"timeout\":false,\"reused\":true}";
        try (MockedStatic<TsoUtils> mockResponseUtil = mockStatic(TsoUtils.class)) {
            mockResponseUtil.when(() -> TsoUtils.getResponseStr(any()))
                    .thenReturn(payload);

            final TsoPing tsoPing = new TsoPing(mockConnection, mockPutRequest);
            TsoCommonResponse tsoCommonResponse = tsoPing.ping("SERVKEY123");
            assertEquals("ZOSMFAD-71-xyz", tsoCommonResponse.getServletKey());
            assertEquals("0100", tsoCommonResponse.getVer());
            assertEquals(false, tsoCommonResponse.getTimeout());
            assertEquals(true, tsoCommonResponse.getReused());
        }
    }

    /**
     * Test that the alternative constructor throws an exception if the connection is null.
     */
    @Test
    public void tstAlternativeConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoPing(null, mockPutRequest)
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
                () -> new TsoPing(mockConnection, null)
        );
        assertEquals("request is null", ex.getMessage());
    }

    /**
     * Test that the alternative constructor throws an exception if a request type is not PutJsonZosmfRequest.
     * Verifies both the exception type and the error message.
     */
    @Test
    public void tstAlternativeConstructorWrongRequestTypeFailure() {
        final ZosmfRequest wrongRequest = mock(ZosmfRequest.class);
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> new TsoPing(mockConnection, wrongRequest)
        );
        assertEquals("PUT_JSON request type required", ex.getMessage());
    }

    /**
     * Test that TsoPing throws when sessionId is null or empty.
     */
    @Test
    public void tstTsoPingNullSessionIdFailure() {
        final TsoPing tsoPing = new TsoPing(mockConnection, mockPutRequest);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> tsoPing.ping(null)
        );
        assertEquals("sessionId is either null or empty", ex.getMessage());
    }

    /**
     * Test that the public constructor throws when the connection is null.
     */
    @Test
    public void tstPublicConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new TsoPing(null)
        );
        assertEquals("connection is null", ex.getMessage());
    }

}
