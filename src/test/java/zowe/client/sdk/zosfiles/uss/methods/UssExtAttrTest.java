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
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssExtAttr.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class UssExtAttrTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestToken;
    public UssExtAttr ussExtAttr;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockJsonPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        Mockito.when(mockJsonPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequest).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequest).getUrl();

        mockJsonPutRequestToken = Mockito.mock(PutJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockJsonPutRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockJsonPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockJsonPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockJsonPutRequestToken).getHeaders();
        doCallRealMethod().when(mockJsonPutRequestToken).getUrl();

        ussExtAttr = new UssExtAttr(connection);
    }

    @Test
    public void tstUssExtAttrSuccess() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(connection, mockJsonPutRequest);
        final Response response = ussExtAttr.set("/test", "a");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Ftest", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssExtAttrTokenSuccess() throws ZosmfRequestException {
        final UssExtAttr ussExtAttr = new UssExtAttr(tokenConnection, mockJsonPutRequestToken);
        final Response response = ussExtAttr.set("/test", "a");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/fs%2Ftest", mockJsonPutRequestToken.getUrl());
    }

    @Test
    public void tstUssExtAttrSetValueFailure1() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "pp");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure2() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "at");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure3() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "yu");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrSetValueFailure4() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.set("/xxx/xx/xx", "apap");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure1() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "pp");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure2() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "at");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure3() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "yu");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetValueFailure4() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.reset("/xxx/xx/xx", "apap");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specified valid value character sequence", errMsg);
    }

    @Test
    public void tstUssExtAttrResetNullTargetPathFailure4() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.reset(null, "a");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is either null or empty", errMsg);
    }

    @Test
    public void tstUssExtAttrSetNullTargetPathFailure4() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.set(null, "a");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is either null or empty", errMsg);
    }

    @Test
    public void tstUssExtAttrDisplayNullTargetPathFailure4() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussExtAttr.display(null);
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is either null or empty", errMsg);
    }

    @Test
    public void tstUssExtAttrNullConnectionFailure() {
        try {
            new UssExtAttr(null);
        } catch (NullPointerException e) {
            assertEquals("connection is null", e.getMessage());
        }
    }

    @Test
    public void tstUssExtAttrSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        UssExtAttr ussExtAttr = new UssExtAttr(connection, request);
        assertNotNull(ussExtAttr);
    }

    @Test
    public void tstUssExtAttrSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssExtAttr(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstUssExtAttrSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssExtAttr(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstUssExtAttrSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new UssExtAttr(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

}
