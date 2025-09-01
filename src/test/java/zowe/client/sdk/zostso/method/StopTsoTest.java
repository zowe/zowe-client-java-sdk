/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zostso.method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.utility.ResponseUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TsoStopService class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class StopTsoTest {

    private final ZosConnection mockConnection = mock(ZosConnection.class);
    private final DeleteJsonZosmfRequest mockDeleteRequest = mock(DeleteJsonZosmfRequest.class);

    public StopTsoTest() {
        when(mockConnection.getZosmfUrl()).thenReturn("https://zosmf:1443");
    }

    @AfterEach
    void tearDown() {
        Mockito.framework().clearInlineMocks();
        // This ensures any leftover static mocks are cleared between tests
        Mockito.clearAllCaches();
    }

    /**
     * Test stopTso sets the correct URL in the request.
     */
    @Test
    public void tstStopTsoSetsCorrectUrlSuccess() throws Exception {
        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();

        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{}");

            final StopTso stopTso = new StopTso(mockConnection, mockDeleteRequest);
            stopTso.stop("SERVKEY123");

            final String actualUrl = mockDeleteRequest.getUrl();
            assertEquals("https://zosmf:1443/tsoApp/tso/SERVKEY123", actualUrl);
        }
    }

    /**
     * Test stopTso sets the correct headers in the request.
     */
    @Test
    public void tstStopTsoSetsCorrectHeadersSuccess() throws Exception {
        DeleteJsonZosmfRequest deleteJsonZosmfRequest = Mockito.mock(DeleteJsonZosmfRequest.class,
                withSettings().useConstructor(ZosConnectionFactory
                        .createBasicConnection("1", "1", "1", "1")));

        doCallRealMethod().when(deleteJsonZosmfRequest).setStandardHeaders();
        doCallRealMethod().when(deleteJsonZosmfRequest).setHeaders(anyMap());
        doCallRealMethod().when(deleteJsonZosmfRequest).getHeaders();

        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{}");

            final StopTso stopTso = new StopTso(mockConnection, deleteJsonZosmfRequest);
            stopTso.stop("SERVKEY123");

            Map<String, String> headers = deleteJsonZosmfRequest.getHeaders();

            assertEquals("application/json", headers.get("Content-Type"));
            assertEquals("true", headers.get("X-CSRF-ZOSMF-HEADER"));
            final String authHeaderValue = headers.get("Authorization");
            assertTrue(authHeaderValue.startsWith("Basic "));
        }
    }

    /**
     * Test stopTso succeeds when response is valid.
     */
    @Test
    public void tstStopTsoSuccess() {
        try (MockedStatic<ResponseUtil> mockResponseUtil = mockStatic(ResponseUtil.class)) {
            mockResponseUtil.when(() -> ResponseUtil.getResponseStr(any(), anyString()))
                    .thenReturn("{\"status\":\"ok\"}");

            final StopTso stopTso = new StopTso(mockConnection, mockDeleteRequest);
            assertDoesNotThrow(() -> stopTso.stop("SERVKEY123"));
        }
    }

    /**
     * Test that the alternative constructor throws an exception if the connection is null.
     */
    @Test
    public void tstAlternativeConstructorNullConnectionFailure() {
        NullPointerException ex = assertThrows(
                NullPointerException.class,
                () -> new StopTso(null, mockDeleteRequest)
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
                () -> new StopTso(mockConnection, null)
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
                () -> new StopTso(mockConnection, wrongRequest)
        );
        assertEquals("DELETE_JSON request type required", ex.getMessage());
    }

    /**
     * Test that stopTso throws when sessionId is null or empty.
     */
    @Test
    public void tstStopTsoNullSessionIdFailure() {
        final StopTso stopTso = new StopTso(mockConnection, mockDeleteRequest);
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> stopTso.stop(null)
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
                () -> new StopTso(null)
        );
        assertEquals("connection is null", ex.getMessage());
    }

}
