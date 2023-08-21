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

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.rest.type.ZosmfRequestType;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit test for ZoweRequest.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class ZoweRequestTest {

    private HttpResponse<JsonNode> mockReply;
    private ZosConnection connection;

    @SuppressWarnings("unchecked")
    @Before
    public void init() {
        mockReply = Mockito.mock(HttpResponse.class);
        connection = new ZosConnection("1", "1", "1", "1");
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        final String expectedErrMsg = "http status error code: 300, status text: error, response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstZoweRequestBuildResponseWithStatusTextAndPhraseSameValueFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn("{\"error\":\"error\"}");
        Mockito.when(mockReply.getStatus()).thenReturn(300);
        Mockito.when(mockReply.getBody()).thenReturn(new JsonNode("{\"error\":\"error\"}"));

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("http status error code: 300, status text: " + "{\"error\":\"error\"}", errMsg);
    }

    @Test
    public void tstZoweRequestBuildResponseWithEmptyStatusTextFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn("");
        Mockito.when(mockReply.getStatus()).thenReturn(300);
        Mockito.when(mockReply.getBody()).thenReturn(new JsonNode("{}"));

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        final String expectedErrMsg = "http status error code: 300, status text: , response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstZoweRequestBuildResponseWithNullStatusTextFailure() {
        Mockito.when(mockReply.getStatusText()).thenReturn(null);
        Mockito.when(mockReply.getStatus()).thenReturn(300);
        Mockito.when(mockReply.getBody()).thenReturn(new JsonNode("{}"));

        final ZosmfRequest request = ZosmfRequestFactory.buildRequest(connection, ZosmfRequestType.PUT_JSON);

        String errMsg = "";
        try {
            request.buildResponse(mockReply);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        final String expectedErrMsg = "http status error code: 300, status text: n\\a, response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

}
