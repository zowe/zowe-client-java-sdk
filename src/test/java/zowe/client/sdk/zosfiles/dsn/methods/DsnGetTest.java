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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetStreamZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnDownloadInputData;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for DsnGet.
 *
 * @author Frank Giordano
 * @version 4.0
 */
public class DsnGetTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private GetStreamZosmfRequest mockGetRequest;
    private GetStreamZosmfRequest mockGetRequestToken;

    @Before
    public void init() throws ZosmfRequestException {
        mockGetRequest = Mockito.mock(GetStreamZosmfRequest.class);
        Mockito.when(mockGetRequest.executeRequest()).thenReturn(
                new Response("test data".getBytes(), 200, "success"));
        doCallRealMethod().when(mockGetRequest).setUrl(any());
        doCallRealMethod().when(mockGetRequest).getUrl();

        mockGetRequestToken = Mockito.mock(GetStreamZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockGetRequestToken.executeRequest()).thenReturn(
                new Response("test data".getBytes(), 200, "success"));
        doCallRealMethod().when(mockGetRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockGetRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockGetRequestToken).setUrl(any());
        doCallRealMethod().when(mockGetRequestToken).getHeaders();
        doCallRealMethod().when(mockGetRequestToken).getUrl();
    }

    @Test
    public void tstDsnGetSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequest);
        final DsnDownloadInputData params = new DsnDownloadInputData.Builder().binary(true).build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", params);
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockGetRequest.getUrl());
    }

    @Test
    public void tstDsnGetTokenSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData params = new DsnDownloadInputData.Builder().binary(true).build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", params);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, X-CSRF-ZOSMF-HEADER=true, " +
                "Content-Type=application/json}", mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData params = new DsnDownloadInputData.Builder()
                .binary(true)
                .encoding(1047L)
                .returnEtag(true)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", params);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=1047, " +
                        "X-IBM-Return-Etag=true, X-IBM-Response-Timeout=30, " +
                        "X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData params = new DsnDownloadInputData.Builder()
                .binary(true)
                .returnEtag(true)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", params);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, " +
                        "X-IBM-Return-Etag=true, X-IBM-Response-Timeout=30, " +
                        "X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingAndEtagSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData params = new DsnDownloadInputData.Builder()
                .binary(true)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", params);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, " +
                        "X-IBM-Response-Timeout=30, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingAndEtagFalseSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData params = new DsnDownloadInputData.Builder()
                .binary(true)
                .returnEtag(false)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", params);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, " +
                        "X-IBM-Response-Timeout=30, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingAndBinaryAndEtagFalseSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData params = new DsnDownloadInputData.Builder()
                .binary(false)
                .returnEtag(false)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", params);
        assertEquals("{Accept-Encoding=gzip, X-IBM-Response-Timeout=30, X-CSRF-ZOSMF-HEADER=true," +
                        " Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(GetStreamZosmfRequest.class);
        DsnGet dsnGet = new DsnGet(connection, request);
        assertNotNull(dsnGet);
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(GetStreamZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnGet(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnGet(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a GetZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnGet(connection, request)
        );
        assertEquals("GET_STREAM request type required", exception.getMessage());
    }

    @Test
    public void tstDsnGetPrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DsnGet dsnGet = new DsnGet(connection);
        assertNotNull(dsnGet);
    }

    @Test
    public void tstDsnGetPrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnGet(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
