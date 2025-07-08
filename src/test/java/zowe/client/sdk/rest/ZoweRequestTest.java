/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.rest;

import kong.unirest.core.Cookie;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.AuthType;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;

import java.util.Objects;

import static org.junit.Assert.*;

/**
 * Class containing unit test for ZoweRequest.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class ZoweRequestTest {

    private HttpResponse<JsonNode> mockReply;
    private ZosConnection connection;

    @SuppressWarnings("unchecked")
    @Before
    public void init() {
        mockReply = Mockito.mock(HttpResponse.class);
        connection = new ZosConnection.Builder(AuthType.BASIC)
                .host("1").password("1").user("1").zosmfPort("1").build();
    }

    @Test
    public void tstZoweRequestBuildResponseWithNullPhraseFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn("error");
        Mockito.when(mockReply.getStatus()).thenReturn(300);
        Mockito.when(mockReply.getBody()).thenReturn(null);

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (ZosmfRequestException e) {
            errMsg = e.getMessage();
        }
        final String expectedErrMsg = "http status error code: 300, status text: error, response phrase: null";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstZoweRequestBuildResponseWithEmptyJsonPhraseFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn("error");
        Mockito.when(mockReply.getStatus()).thenReturn(300);
        Mockito.when(mockReply.getBody()).thenReturn(new JsonNode(""));

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (ZosmfRequestException e) {
            errMsg = e.getMessage();
        }
        final String expectedErrMsg = "http status error code: 300, status text: error, response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstZoweRequestBuildResponseWithStatusTextAndPhraseSameValueFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn("{\"error\":\"error\"}");
        Mockito.when(mockReply.getStatus()).thenReturn(600);
        Mockito.when(mockReply.getBody()).thenReturn(new JsonNode("{\"error\":\"error\"}"));

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (ZosmfRequestException e) {
            errMsg = e.getMessage();
        }
        assertEquals("http status error code: 600, status text: " + "{\"error\":\"error\"}", errMsg);
    }

    @Test
    public void tstZoweRequestBuildResponseWithEmptyStatusTextFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn("");
        Mockito.when(mockReply.getStatus()).thenReturn(600);
        Mockito.when(mockReply.getBody()).thenReturn(new JsonNode("{}"));

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (ZosmfRequestException e) {
            errMsg = e.getMessage();
        }
        final String expectedErrMsg = "http status error code: 600, status text: n\\a, response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstZoweRequestBuildResponseWithNullStatusTextFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn(null);
        Mockito.when(mockReply.getStatus()).thenReturn(600);
        Mockito.when(mockReply.getBody()).thenReturn(new JsonNode("{}"));

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (ZosmfRequestException e) {
            errMsg = e.getMessage();
        }
        final String expectedErrMsg = "http status error code: 600, status text: n\\a, response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstZoweRequestInitializeSslSetupFailure() {
        ZosConnection connection = new ZosConnection.Builder(AuthType.SSL)
                .host("host").zosmfPort("port").certFilePath("/file/file1").certPassword("").build();
        String errMsg = "java.io.FileNotFoundException: \\file\\file1 (The system cannot find the path specified)";
        try {
            ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        } catch (Exception e) {
            assertEquals(errMsg, e.getMessage());
        }
    }

    @Test
    public void tstZoweRequestInitializeBasicSetupSuccess() {
        ZosConnection connection = new ZosConnection.Builder(AuthType.BASIC)
                .host("host").zosmfPort("port").user("user").password("password").build();
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        assertNotNull(request.getHeaders().get("Authorization"));
    }

    @Test
    public void tstZoweRequestInitializeTokenSetupSuccess() {
        ZosConnection connection = new ZosConnection.Builder(AuthType.TOKEN)
                .host("host").zosmfPort("port").token(new Cookie("hello", "world")).build();
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        assertNull(request.getHeaders().get("Authorization"));
    }
    
}
