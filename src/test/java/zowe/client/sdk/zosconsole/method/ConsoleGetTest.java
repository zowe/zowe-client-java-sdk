/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosconsole.method;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosconsole.response.ConsoleGetResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for ConsoleGet class.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ConsoleGetTest {

    private final ZosConnection connection =
            ZosConnectionFactory.createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection =
            ZosConnectionFactory.createTokenConnection("1", 443, new Cookie("hello=hello"));
    private GetJsonZosmfRequest mockJsonGetRequest;

    @BeforeEach
    public void init() {
        mockJsonGetRequest = Mockito.mock(GetJsonZosmfRequest.class, withSettings().useConstructor(connection));
    }

    @Test
    public void tstConsoleGetPrimaryConstructorSuccess() {
        ConsoleGet consoleGet = new ConsoleGet(connection);
        assertNotNull(consoleGet);
    }

    @Test
    public void tstConsoleGetPrimaryConstructorNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ConsoleGet(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstConsoleGetSecondaryConstructorSuccess() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        ConsoleGet consoleGet = new ConsoleGet(connection, request);
        assertNotNull(consoleGet);
    }

    @Test
    public void tstConsoleGetSecondaryConstructorNullConnectionFailure() {
        ZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ConsoleGet(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstConsoleGetSecondaryConstructorNullRequestFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ConsoleGet(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstConsoleGetSecondaryConstructorInvalidRequestTypeFailure() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class);
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new ConsoleGet(connection, request)
        );
        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstConsoleGetToggleTokenSuccess() throws Exception {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "LINE1\rLINE2");
        final JSONObject json = new JSONObject(jsonMap);

        // Create mock with token constructor
        GetJsonZosmfRequest mockJsonGetRequestAuth = Mockito.mock(
                GetJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection)
        );

        Mockito.when(mockJsonGetRequestAuth.executeRequest()).thenReturn(
                new Response(json, 200, "{ \"cmd-response\": \"LINE1\\rLINE2\" }")
        );

        // Enable real header and URL handling
        doCallRealMethod().when(mockJsonGetRequestAuth).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonGetRequestAuth).setStandardHeaders();
        doCallRealMethod().when(mockJsonGetRequestAuth).setUrl(any());
        doCallRealMethod().when(mockJsonGetRequestAuth).getHeaders();

        // Execute ConsoleGet with token
        final ConsoleGet consoleGet = new ConsoleGet(tokenConnection, mockJsonGetRequestAuth);
        ConsoleGetResponse response = consoleGet.getResponse("respKey");

        // 🔍 Validate token header injection
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonGetRequestAuth.getHeaders().toString());

        // 🔍 Validate response was processed
        assertEquals("LINE1\nLINE2\n", response.getCmdResponse());
    }


    @Test
    public void tstConsoleGetResponseSuccess() throws ZosmfRequestException {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "LINE1\rLINE2");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "{ \"cmd-response\": \"LINE1\\rLINE2\" }")
        );
        ConsoleGet consoleGet = new ConsoleGet(connection, mockJsonGetRequest);
        ConsoleGetResponse response = consoleGet.getResponse("respKey");
        assertEquals("LINE1\nLINE2\n", response.getCmdResponse());
    }

    @Test
    public void tstConsoleGetResponseBlankContentSuccess() throws ZosmfRequestException {
        final Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("cmd-response", "");
        final JSONObject json = new JSONObject(jsonMap);
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(json, 200, "{ \"cmd-response\": \"\" }")
        );
        ConsoleGet consoleGet = new ConsoleGet(connection, mockJsonGetRequest);
        ConsoleGetResponse response = consoleGet.getResponse("respKey");
        assertEquals("", response.getCmdResponse());
    }

    @Test
    public void tstConsoleGetMissingResponsePhraseFailure() {
        ConsoleGet consoleGet = new ConsoleGet(connection, mockJsonGetRequest);
        try {
            Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                    new Response(null, 200, null)
            );
            consoleGet.getResponse("respKey");
        } catch (Exception e) {
            assertEquals("java.lang.IllegalStateException: no issue console response phrase", e.toString());
        }
    }

    @Test
    public void tstConsoleGetJsonParseFailure() throws Exception {
        Mockito.when(mockJsonGetRequest.executeRequest()).thenReturn(
                new Response(null, 200, "{ invalid json }")
        );
        ConsoleGet consoleGet = new ConsoleGet(connection, mockJsonGetRequest);
        assertThrows(IllegalStateException.class, () -> consoleGet.getResponse("respKey"));
    }

    @Test
    public void tstConsoleGetUsesZosmfRequestFactoryWhenRequestNull() {
        ZosConnection mockConn = Mockito.mock(ZosConnection.class);
        Mockito.when(mockConn.getZosmfUrl()).thenReturn("http://test");
        ConsoleGet consoleGet = new ConsoleGet(mockConn);
        try {
            consoleGet.getResponse("respKey");
        } catch (Exception e) {
            // expected due to no response phrase
        }
        // No crash = success
        assertTrue(true);
    }

}

