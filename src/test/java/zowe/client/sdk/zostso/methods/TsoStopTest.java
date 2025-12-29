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
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.TsoUtils;
import zowe.client.sdk.zostso.response.TsoCommonResponse;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TsoStop class.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class TsoStopTest {

    private final ZosConnection mockConnection = mock(ZosConnection.class);
    private final DeleteJsonZosmfRequest mockDeleteRequest = mock(DeleteJsonZosmfRequest.class);

    public TsoStopTest() {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf:1443");
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMocks();
        // This ensures any leftover static mocks are cleared between tests
        Mockito.clearAllCaches();
    }

    /**
     * Test TsoStop sets the correct URL in the request.
     */
    @Test
    public void tstTsoStopSetsCorrectUrlSuccess() throws Exception {
        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();

        try (MockedStatic<TsoUtils> mockResponseUtil = mockStatic(TsoUtils.class)) {
            mockResponseUtil.when(() -> TsoUtils.getResponseStr(any()))
                    .thenReturn("{}");

            final TsoStop tsoStop = new TsoStop(mockConnection, mockDeleteRequest);
            tsoStop.stop("SERVKEY123");

            final String actualUrl = mockDeleteRequest.getUrl();
            assertEquals("https://zosmf:1443/tsoApp/tso/SERVKEY123", actualUrl);
        }
    }

    /**
     * Test TsoStop sets the correct headers in the request.
     */
    @Test
    public void tstTsoStopSetsCorrectHeadersSuccess() throws Exception {
        DeleteJsonZosmfRequest deleteJsonZosmfRequest = Mockito.mock(DeleteJsonZosmfRequest.class,
                withSettings().useConstructor(ZosConnectionFactory
                        .createBasicConnection("1", 443, "1", "1")));

        doCallRealMethod().when(deleteJsonZosmfRequest).setStandardHeaders();
        doCallRealMethod().when(deleteJsonZosmfRequest).setHeaders(anyMap());
        doCallRealMethod().when(deleteJsonZosmfRequest).getHeaders();

        try (MockedStatic<TsoUtils> mockResponseUtil = mockStatic(TsoUtils.class)) {
            mockResponseUtil.when(() -> TsoUtils.getResponseStr(any()))
                    .thenReturn("{}");

            final TsoStop tsoStop = new TsoStop(mockConnection, deleteJsonZosmfRequest);
            tsoStop.stop("SERVKEY123");

            Map<String, String> headers = deleteJsonZosmfRequest.getHeaders();

            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("true", headers.get("X-CSRF-ZOSMF-HEADER"));
            final String authHeaderValue = headers.get("Authorization");
            assertTrue(authHeaderValue.startsWith("Basic "));
        }
    }

    /**
     * Test TsoStop succeeds when response is valid.
     */
    @Test
    public void tstTsoStopSuccess() throws ZosmfRequestException {
        final String payload = "{\"servletKey\":\"ZOSMFAD-71-aabcaaaf\",\"ver\":\"0100\",\"timeout\":false,\"reused\":true}";
        try (MockedStatic<TsoUtils> mockResponseUtil = mockStatic(TsoUtils.class)) {
            mockResponseUtil.when(() -> TsoUtils.getResponseStr(any()))
                    .thenReturn(payload);

            final TsoStop tsoStop = new TsoStop(mockConnection, mockDeleteRequest);
            TsoCommonResponse tsoCommonResponse = tsoStop.stop("SERVKEY123");
            assertEquals("ZOSMFAD-71-aabcaaaf", tsoCommonResponse.getServletKey());
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
                () -> new TsoStop(null, mockDeleteRequest)
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
                () -> new TsoStop(mockConnection, null)
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
                () -> new TsoStop(mockConnection, wrongRequest)
        );
        assertEquals("DELETE_JSON request type required", ex.getMessage());
    }

    /**
     * Test that TsoStop throws when sessionId is null or empty.
     */
    @Test
    public void tstTsoStopNullSessionIdFailure() {
        final TsoStop tsoStop = new TsoStop(mockConnection, mockDeleteRequest);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> tsoStop.stop(null)
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
                () -> new TsoStop(null)
        );
        assertEquals("connection is null", ex.getMessage());
    }

}
