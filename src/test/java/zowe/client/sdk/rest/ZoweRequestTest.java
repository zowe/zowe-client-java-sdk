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
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.rest.type.ZosmfRequestType;

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
        ZosConnection connection = ZosConnectionFactory.createSslConnection("host", "port",
                "/file/file1", "");
        String errMsg = "java.io.FileNotFoundException: \\file\\file1 (The system cannot find the path specified)";
        try {
            ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        } catch (Exception e) {
            assertEquals(errMsg, e.getMessage());
        }
    }

    @Test
    public void tstZoweRequestInitializeSslSetupCertPasswordFailure() {
        ZosConnection connection = ZosConnectionFactory.createSslConnection("host", "port",
                "src/test/resources/certs/badssl.com-client.p12", "");
        String errMsg = "java.io.IOException: keystore password was incorrect";
        try {
            ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        } catch (Exception e) {
            assertEquals(errMsg, e.getMessage());
        }
    }

    @Test
    public void tstZoweRequestInitializeSslSetupCertPasswordSuccess() {
        ZosConnection connection = ZosConnectionFactory.createSslConnection("host", "port",
                "src/test/resources/certs/badssl.com-client.p12", "badssl.com");
        try {
            ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void tstZoweRequestInitializeBasicSetupSuccess() {
        ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("host", "port", "user", "password");
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        assertNotNull(request.getHeaders().get("Authorization"));
    }

    @Test
    public void tstZoweRequestInitializeTokenSetupSuccess() {
        ZosConnection connection = ZosConnectionFactory
                .createTokenConnection("host", "port", new Cookie("hello", "world"));
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);
        assertNull(request.getHeaders().get("Authorization"));
    }

    @Test
    public void tstUrlConstructionWithBasePathSuccess() {
        // Create a connection with a base path
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test.host", "443", "user", "password");
        connection.setBasePath("/custom/base/path");

        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);

        // Set URL for a hypothetical endpoint
        final String RESOURCE_PATH = "/zosmf/resource/endpoint";
        request.setUrl("https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                RESOURCE_PATH);

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
        final String RESOURCE_PATH = "/zosmf/resource/endpoint";
        request.setUrl("https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                RESOURCE_PATH);

        // Verify the constructed URL does not contain a base path
        final String expectedUrl = "https://test.host:443/zosmf/resource/endpoint";
        assertEquals(expectedUrl, request.getUrl());
    }

    @Test
    public void tstUrlConstructionWithEmptyBasePathSuccess() {
        // Create a connection and set an empty base path
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test.host", "443", "user", "password");
        connection.setBasePath("");

        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);

        // Set URL for a hypothetical endpoint
        final String RESOURCE_PATH = "/zosmf/resource/endpoint";
        request.setUrl("https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                RESOURCE_PATH);

        // Verify the constructed URL does not contain a base path
        final String expectedUrl = "https://test.host:443/zosmf/resource/endpoint";
        assertEquals(expectedUrl, request.getUrl());
    }

    @Test
    public void tstUrlConstructionWithInvalidBasePathFailure() {
        // Create a connection and set an empty base path
        final ZosConnection connection = ZosConnectionFactory
                .createBasicConnection("test.host", "443", "user", "password");
        // set an invalid base path value
        connection.setBasePath("frank//");

        // Create a mock request to verify URL
        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.GET_JSON);

        // Set URL for a hypothetical endpoint
        final String RESOURCE_PATH = "/zosmf/resource/endpoint";
        assertThrows(IllegalArgumentException.class,
                () -> request.setUrl("https://" + connection.getHost() + ":" + connection.getZosmfPort() +
                        (connection.getBasePath().isPresent() ? connection.getBasePath().get() : "") +
                        RESOURCE_PATH));
    }

}
