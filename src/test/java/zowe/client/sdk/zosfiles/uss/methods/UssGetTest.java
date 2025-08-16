/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.uss.methods;

import kong.unirest.core.Cookie;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetStreamZosmfRequest;
import zowe.client.sdk.rest.GetTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.GetInputData;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssGet.
 *
 * @author Frank Giordano
 * @version 4.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssGetTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private GetTextZosmfRequest mockTextGetRequest;
    private GetTextZosmfRequest mockTextGetRequestToken;
    private UssGet ussGet;

    @Before
    public void init() throws ZosmfRequestException {
        mockTextGetRequest = Mockito.mock(GetTextZosmfRequest.class);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response("text", 200, "success"));
        doCallRealMethod().when(mockTextGetRequest).setUrl(any());
        doCallRealMethod().when(mockTextGetRequest).getUrl();

        mockTextGetRequestToken = Mockito.mock(GetTextZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockTextGetRequestToken.executeRequest()).thenReturn(
                new Response("text", 200, "success"));
        doCallRealMethod().when(mockTextGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockTextGetRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockTextGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockTextGetRequestToken).getHeaders();
        doCallRealMethod().when(mockTextGetRequestToken).getUrl();

        ussGet = new UssGet(connection);
    }

    @Test
    public void tstUssGetTextFileTargetPathSuccess() throws ZosmfRequestException {
        final UssGet ussGet = new UssGet(connection, mockTextGetRequest);
        final String response = ussGet.getText("/xxx/xx");
        assertEquals("text", response);
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx", mockTextGetRequest.getUrl());
    }

    @Test
    public void tstUssGetTextFileTargetPathToggleTokenSuccess() throws ZosmfRequestException {
        final UssGet ussGet = new UssGet(connection, mockTextGetRequestToken);
        String response = ussGet.getText("/xxx/xx");
        String expectedResp = "{X-IBM-Data-Type=text, X-CSRF-ZOSMF-HEADER=true, " +
                "Content-Type=text/plain; charset=UTF-8}";
        assertEquals(expectedResp, mockTextGetRequestToken.getHeaders().toString());
        assertEquals("text", response);
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx", mockTextGetRequestToken.getUrl());
    }

    @Test
    public void tstUssGetTextFileTargetPathDefaultResponseSuccess() throws ZosmfRequestException {
        final GetTextZosmfRequest mockTextGetRequest = Mockito.mock(GetTextZosmfRequest.class);
        Mockito.when(mockTextGetRequest.executeRequest()).thenReturn(
                new Response(null, 200, "success"));
        doCallRealMethod().when(mockTextGetRequest).setUrl(any());
        doCallRealMethod().when(mockTextGetRequest).getUrl();
        final UssGet ussGet = new UssGet(connection, mockTextGetRequest);
        final String response = ussGet.getText("/xxx/xx");
        assertEquals("", response);
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx", mockTextGetRequest.getUrl());
    }

    @Test
    public void tstUssGetBinaryFileTargetPathSuccess() throws ZosmfRequestException {
        final GetStreamZosmfRequest mockStreamGetRequest = Mockito.mock(GetStreamZosmfRequest.class);
        final byte[] data = "data".getBytes();
        Mockito.when(mockStreamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        doCallRealMethod().when(mockStreamGetRequest).setUrl(any());
        doCallRealMethod().when(mockStreamGetRequest).getUrl();
        final UssGet ussGet = new UssGet(connection, mockStreamGetRequest);
        final byte[] response = ussGet.getBinary("/xxx/xx");
        assertEquals(data, response);
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx", mockStreamGetRequest.getUrl());
    }

    @Test
    public void tstUssGetBinaryFileTargetPathDefaultResponseSuccess() throws ZosmfRequestException {
        final GetStreamZosmfRequest mockStreamGetRequest = Mockito.mock(GetStreamZosmfRequest.class);
        final byte[] data = new byte[0];
        Mockito.when(mockStreamGetRequest.executeRequest()).thenReturn(
                new Response(data, 200, "success"));
        doCallRealMethod().when(mockStreamGetRequest).setUrl(any());
        doCallRealMethod().when(mockStreamGetRequest).getUrl();
        final UssGet ussGet = new UssGet(connection, mockStreamGetRequest);
        final byte[] response = ussGet.getBinary("/xxx/xx");
        assertEquals(data, response);
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx", mockStreamGetRequest.getUrl());
    }

    @Test
    public void tstUssGetTextFileInvalidPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText("name");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssGetTextFileNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText(null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssGetTextFileEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssGetTextFileEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getText("  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary(null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary("name");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary("");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssGetBinaryFileEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getBinary("   ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("fileNamePath is either null or empty", errMsg);
    }

    @Test
    public void tstUssGetCommonNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getCommon("/xxx/xx/x", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("params is null", errMsg);
    }

    @Test
    public void tstUssGetCommonInvalidTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussGet.getCommon("name", new GetInputData.Builder().build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssGetNullConnectionFailure() {
        try {
            new UssGet(null);
        } catch (NullPointerException e) {
            assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

    @Test
    public void tstUssGetSecondaryConstructorWithValidTextRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetTextZosmfRequest.class);
        UssGet ussGet = new UssGet(connection, request);
        assertNotNull(ussGet);
    }

    @Test
    public void tstUssGetSecondaryConstructorWithValidStreamRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetStreamZosmfRequest.class);
        UssGet ussGet = new UssGet(connection, request);
        assertNotNull(ussGet);
    }

    @Test
    public void tstUssGetSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssGet(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

}
