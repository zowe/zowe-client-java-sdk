/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosfiles.dsn.methods;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.ListParams;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for DsnList.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class DsnListTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private GetJsonZosmfRequest mockGetRequest;
    private GetJsonZosmfRequest mockGetRequestToken;

    @Before
    public void init() throws ZosmfRequestException {
        mockGetRequest = Mockito.mock(GetJsonZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        mockGetRequestToken = Mockito.mock(GetJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockGetRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockGetRequestToken).setupRequest();
        doCallRealMethod().when(mockGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockGetRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockGetRequestToken).getHeaders();
        doCallRealMethod().when(mockGetRequestToken).getUrl();
    }

    @Test
    public void tstDsnListSuccess() throws ZosmfRequestException {
        final DsnList dsnList = new DsnList(connection, mockGetRequest);
        final ListParams params = new ListParams.Builder().responseTimeout("10").volume("vol1").build();
        dsnList.getMembers("TEST.DATASET", params);
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET/member", mockGetRequest.getUrl());
    }

    @Test
    public void tstDsnListTokenSuccess() throws ZosmfRequestException {
        final DsnList dsnList = new DsnList(connection, mockGetRequestToken);
        final ListParams params = new ListParams.Builder().responseTimeout("10").volume("vol1").build();
        dsnList.getMembers("TEST.DATASET", params);
        assertEquals("{X-IBM-Max-Items=0, Accept-Encoding=gzip, X-IBM-Response-Timeout=10, " +
                        "X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET/member", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        GetJsonZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        DsnList dsnList = new DsnList(connection, request);
        assertNotNull(dsnList);
    }

    @Test
    public void tstSecondaryConstructorWithNullConnection() {
        GetJsonZosmfRequest request = Mockito.mock(GetJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnList(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnList(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a GetJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnList(connection, request)
        );
        assertEquals("GET_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DsnList dsnList = new DsnList(connection);
        assertNotNull(dsnList);
    }

    @Test
    public void tstPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnList(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
