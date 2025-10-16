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

import org.junit.jupiter.api.Test;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import java.util.Optional;
import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class containing unit test for TsoUtils.
 *
 * @author Frank Giordano
 * @version 5.0
 */
class TsoUtilsTest {

    @Test
    void tstGetResponseStrSuccess() throws Exception {
        // Mock Response
        Response mockResponse = mock(Response.class);
        when(mockResponse.getResponsePhrase()).thenReturn(Optional.of("OK"));
        when(mockResponse.getStatusCode()).thenReturn(OptionalInt.of(200));

        // Mock Request
        ZosmfRequest mockRequest = mock(ZosmfRequest.class);
        when(mockRequest.executeRequest()).thenReturn(mockResponse);

        String result = TsoUtils.getResponseStr(mockRequest);
        assertEquals("OK", result);
    }

    @Test
    void tstGetResponseStrMissingResponsePhraseFailure() throws Exception {
        Response mockResponse = mock(Response.class);
        when(mockResponse.getResponsePhrase()).thenReturn(Optional.empty());
        when(mockResponse.getStatusCode()).thenReturn(OptionalInt.of(200));

        ZosmfRequest mockRequest = mock(ZosmfRequest.class);
        when(mockRequest.executeRequest()).thenReturn(mockResponse);

        assertThrows(ZosmfRequestException.class, () -> TsoUtils.getResponseStr(mockRequest));
    }

    @Test
    void tstGetResponseStrErrorStatusCodeFailure() throws Exception {
        Response mockResponse = mock(Response.class);
        when(mockResponse.getResponsePhrase()).thenReturn(Optional.of("ERROR"));
        when(mockResponse.getStatusCode()).thenReturn(OptionalInt.of(500));

        ZosmfRequest mockRequest = mock(ZosmfRequest.class);
        when(mockRequest.executeRequest()).thenReturn(mockResponse);

        assertThrows(ZosmfRequestException.class, () -> TsoUtils.getResponseStr(mockRequest));
    }

    @Test
    void testGetMsgDataTextMessageSuccess() {
        String json = "{ \"msgData\": [{ \"messageText\": \"Error occurred\" }] }";
        String result = TsoUtils.getMsgDataText(json);
        assertEquals("Error occurred", result);
    }

    @Test
    void tstGetMsgDataTextMultipleMessagesSuccess() {
        String json = "{ \"msgData\": [{ \"messageText\": \"One\" }, { \"messageText\": \"Two\" }] }";
        String result = TsoUtils.getMsgDataText(json);
        assertTrue(result.contains("One"));
        assertTrue(result.contains("Two"));
    }

    @Test
    void tstGetMsgDataTextCustomMessageSuccess() {
        String json = "{ \"msgData\": { \"custom\": \"data\" } }";
        String result = TsoUtils.getMsgDataText(json);
        assertTrue(result.contains("custom"));
    }

    @Test
    void tstGetMsgDataTextInvalidJsonFailure() {
        String result = TsoUtils.getMsgDataText("Not a JSON");
        assertEquals("", result);
    }

}
