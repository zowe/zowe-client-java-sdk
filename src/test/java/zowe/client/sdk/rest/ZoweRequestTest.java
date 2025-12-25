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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class containing unit test for ZoweRequest.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class ZoweRequestTest {

    private HttpResponse<JsonNode> mockReply;
    private ZosConnection connection;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void init() {
        mockReply = Mockito.mock(HttpResponse.class);
        connection = ZosConnectionFactory.createBasicConnection("1", "1", "1", "1");
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
        final String expectedErrMsg = "http status error code: 300, status text: error, response phrase: ";
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
        final ZosConnection connection = ZosConnectionFactory.createSslConnection("host", "port",
                "/file/file1", "dummy");
        final String errMsgWindows = "kong.unirest.core.UnirestConfigException: " +
                "java.io.FileNotFoundException: \\file\\file1 (The system cannot find the path specified)";
        final String errMsgMacOS = "kong.unirest.core.UnirestConfigException: " +
                "java.io.FileNotFoundException: /file/file1 (No such file or directory)";
        try {
            ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains(errMsgWindows) || e.getMessage().contains(errMsgMacOS));
        }
    }

    @Test
    public void tstZoweRequestInitializeSslSetupCertPasswordFailure() {
        final ZosConnection connection = ZosConnectionFactory.createSslConnection("host", "port",
                "src/test/resources/certs/badssl.com-client.p12", "dummy");
        final String errMsg = "java.io.IOException: keystore password was incorrect";
        try {
            ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        } catch (Exception e) {
            assertEquals(errMsg, e.getMessage());
        }
    }

    @Test
    public void tstZoweRequestInitializeSslSetupCertPasswordSuccess() {
        final ZosConnection connection = ZosConnectionFactory.createSslConnection("host", "port",
                "src/test/resources/certs/badssl.com-client.p12", "badssl.com");
        try {
            ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void tstZoweRequestInitializeBasicSetupSuccess() {
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("host", "port", "user", "password");
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        assertNotNull(request.getHeaders().get("Authorization"));
    }

    @Test
    public void tstZoweRequestInitializeTokenSetupSuccess() {
        final ZosConnection connection = ZosConnectionFactory
                .createTokenConnection("host", "port", new Cookie("hello", "world"));
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        assertNull(request.getHeaders().get("Authorization"));
    }

    @Test
    public void tstUrlConstructionWithBasePathSuccess() {
        // Create a connection with a base path
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test.host", "443", "user", "password", "/custom/base/path");

        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);

        // Set URL for a hypothetical endpoint
        final String RESOURCE_PATH = "/resource/endpoint";
        request.setUrl(connection.getZosmfUrl() + RESOURCE_PATH);

        // Verify the constructed URL contains the base path
        final String expectedUrl = "https://test.host:443/custom/base/path/zosmf/resource/endpoint";
        assertEquals(expectedUrl, request.getUrl());
    }

    @Test
    public void tstUrlConstructionWithoutBasePathSuccess() {
        // Create a connection without setting a base path
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test.host", "443", "user", "password");

        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);

        // Set URL for a hypothetical endpoint
        final String RESOURCE_PATH = "/resource/endpoint";
        request.setUrl(connection.getZosmfUrl() + RESOURCE_PATH);

        // Verify the constructed URL does not contain a base path
        final String expectedUrl = "https://test.host:443/zosmf/resource/endpoint";
        assertEquals(expectedUrl, request.getUrl());
    }

    @Test
    public void tstUrlConstructionWithInvalidBasePathFailure() {
        // Create a connection and set an empty base path
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test.host:123foo", "443", "user", "password", "frank///");
        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);

        // Set URL for a hypothetical endpoint
        final String RESOURCE_PATH = "/resource/endpoint";
        assertThrows(IllegalArgumentException.class,
                () -> request.setUrl(connection.getZosmfUrl() + RESOURCE_PATH));
    }

    @Test
    public void tstBuildResponseThrowsOnHttpErrorFailure() {
        // Create a minimal concrete ZosmfRequest
        final ZosmfRequest request = new ZosmfRequest(connection) {
            @Override
            public Response executeRequest() {
                return null; // not used in this test
            }

            @Override
            public void setBody(Object body) {
            }

            @Override
            public void setStandardHeaders() {
            }
        };

        // Mock HttpResponse to simulate 404 error
        @SuppressWarnings("unchecked")
        HttpResponse<String> httpResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(httpResponse.getStatus()).thenReturn(404);
        Mockito.when(httpResponse.getStatusText()).thenReturn("Not Found");
        Mockito.when(httpResponse.getBody()).thenReturn("Error occurred");
        Mockito.when(httpResponse.getCookies()).thenReturn(null);

        // Assert that buildResponse throws ZosmfRequestException
        ZosmfRequestException thrown = assertThrows(
                ZosmfRequestException.class,
                () -> request.buildResponse(httpResponse),
                "Expected buildResponse() to throw ZosmfRequestException"
        );

        // Verify correct message content
        assertTrue(thrown.getMessage().contains("http status error code: 404"));
        assertTrue(thrown.getMessage().contains("Not Found"));
    }

}
