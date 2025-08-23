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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.uss.input.UssChangeOwnerInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for UssChangeOwner.
 *
 * @author James Kostrewski
 * @version 4.0
 */
@SuppressWarnings("DataFlowIssue")
public class UssChangeOwnerTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PutJsonZosmfRequest mockJsonPutRequest;
    private PutJsonZosmfRequest mockJsonPutRequestToken;
    private UssChangeOwner ussChangeOwner;

    @Before
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

        ussChangeOwner = new UssChangeOwner(connection);
    }

    @Test
    public void tstUssChangeOwnerSuccess() throws ZosmfRequestException {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, mockJsonPutRequest);
        final Response response = ussChangeOwner.change("/xxx/xx/xx", "user");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeOwnerToggleTokenSuccess() throws ZosmfRequestException {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(tokenConnection, mockJsonPutRequestToken);
        Response response = ussChangeOwner.change("/xxx/xx/xx", "user");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockJsonPutRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequestToken.getUrl());
    }

    @Test
    public void tstUssChangeOwnerRecursiveSuccess() throws ZosmfRequestException {
        final UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, mockJsonPutRequest);
        final Response response = ussChangeOwner.changeCommon("/xxx/xx/xx",
                new UssChangeOwnerInputData.Builder().owner("user").recursive(true).build());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/fs%2Fxxx%2Fxx%2Fxx", mockJsonPutRequest.getUrl());
    }

    @Test
    public void tstUssChangeOwnerInvalidTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("name", "user");
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change(null, "user");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyTargetPathFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("", "user");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyTargetPathWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("   ", "user");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("targetPath is either null or empty", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.changeCommon("/xxx/xx/xx", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("changeOwnerInputData is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNoOwnerSpecifiedInParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.changeCommon("/xxx/xx/xx", new UssChangeOwnerInputData.Builder().build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerInvalidTargetPathWithParamsFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.changeCommon("name", new UssChangeOwnerInputData.Builder().build());
        } catch (IllegalStateException e) {
            errMsg = e.getMessage();
        }
        assertEquals("specify valid path value", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullOwnerFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", null);
        } catch (NullPointerException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner is null", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyOwnerFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", "");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerEmptyOwnerWithSpacesFailure() throws ZosmfRequestException {
        String errMsg = "";
        try {
            ussChangeOwner.change("/xxx/xx/xx", "  ");
        } catch (IllegalArgumentException e) {
            errMsg = e.getMessage();
        }
        assertEquals("owner not specified", errMsg);
    }

    @Test
    public void tstUssChangeOwnerNullConnectionFailure() {
        try {
            new UssChangeOwner(null);
        } catch (NullPointerException e) {
            Assert.assertEquals("Should throw IllegalArgumentException when connection is null",
                    "connection is null", e.getMessage());
        }
    }

    @Test
    public void tstUssChangeOwnerSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        UssChangeOwner ussChangeOwner = new UssChangeOwner(connection, request);
        assertNotNull(ussChangeOwner);
    }

    @Test
    public void tstUssChangeOwnerSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssChangeOwner(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstUssChangeOwnerSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new UssChangeOwner(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstUssChangeOwnerSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new UssChangeOwner(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

}
