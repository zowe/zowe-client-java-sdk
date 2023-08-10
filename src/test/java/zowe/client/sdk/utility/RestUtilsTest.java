/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.utility;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZoweRequest;

import static org.junit.Assert.assertEquals;

/**
 * Class containing unit test for RestUtils.
 *
 * @author Frank Giordano
 * @version 2.0
 */
public class RestUtilsTest {

    private ZoweRequest mockRequest;

    @Before
    public void init() {
        mockRequest = Mockito.mock(ZoweRequest.class);
    }

    /**
     * Validate class structure
     */
    @Test
    public void tstRestUtilsClassStructureSuccess() {
        final String privateConstructorExceptionMsg = "Utility class";
        Utils.validateClass(RestUtils.class, privateConstructorExceptionMsg);
    }

    @Test
    public void tstRestUtilsGetResponseWithNullPhraseFailure() {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response(null, 300, "error"));
        String errMsg = "";
        try {
            RestUtils.getResponse(mockRequest);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        String expectedErrMsg = "http status error code: 300, status text: error, response phrase: null";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstRestUtilsGetResponseWithEmptyPhraseFailure() {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response("", 300, "error"));
        String errMsg = "";
        try {
            RestUtils.getResponse(mockRequest);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        String expectedErrMsg = "http status error code: 300, status text: error, response phrase: ";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstRestUtilsGetResponseWithEmptyJsonPhraseFailure() {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 300, "error"));
        String errMsg = "";
        try {
            RestUtils.getResponse(mockRequest);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("http status error code: 300, status text: error, response phrase: {}", errMsg);
    }

    @Test
    public void tstRestUtilsGetResponseWithStatusTextAndPhraseSameValueFailure() {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response("error", 300, "error"));
        String errMsg = "";
        try {
            RestUtils.getResponse(mockRequest);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("http status error code: 300, status text: error", errMsg);
    }

    @Test
    public void tstRestUtilsGetResponseWithEmptyStatusTextFailure() {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 300, ""));
        String errMsg = "";
        try {
            RestUtils.getResponse(mockRequest);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        String expectedErrMsg = "http status error code: 300, status text: , response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstRestUtilsGetResponseWithNullStatusTextFailure() {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 300, null));
        String errMsg = "";
        try {
            RestUtils.getResponse(mockRequest);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        String expectedErrMsg = "http status error code: 300, status text: n\\a, response phrase: {}";
        assertEquals(expectedErrMsg, errMsg);
    }

    @Test
    public void tstRestUtilsGetResponseWithNullStatusCodeFailure() {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), null, ""));
        String errMsg = "";
        try {
            RestUtils.getResponse(mockRequest);
        } catch (Exception e) {
            errMsg = e.getMessage();
        }
        assertEquals("no response status code returned", errMsg);
    }

    @Test
    public void tstRestUtilsGetResponseSuccess() throws Exception {
        Mockito.when(mockRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        Response response = RestUtils.getResponse(mockRequest);
        assertEquals("200", response.getStatusCode().get().toString());
        assertEquals("{}", response.getResponsePhrase().get().toString());
        assertEquals("success", response.getStatusText().get().toString());
    }

}
