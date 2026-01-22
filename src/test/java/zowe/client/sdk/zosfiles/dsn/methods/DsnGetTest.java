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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.GetStreamZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnDownloadInputData;
import zowe.client.sdk.zosfiles.dsn.model.Dataset;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for DsnGet.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnGetTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private GetStreamZosmfRequest mockGetRequest;
    private GetStreamZosmfRequest mockGetRequestToken;

    @BeforeEach
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
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder().binary(true).build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequest.getUrl());
    }

    @Test
    public void tstDsnGetTokenSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder().binary(true).build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, X-CSRF-ZOSMF-HEADER=true, " +
                "Content-Type=application/json}", mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder()
                .binary(true)
                .encoding(1047L)
                .returnEtag(true)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=1047, " +
                        "X-IBM-Return-Etag=true, X-IBM-Response-Timeout=30, " +
                        "X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithOnlyEncodingParamsSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder()
                .encoding(1047L)
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("{X-IBM-Data-Type=text;fileEncoding=IBM-1047, " +
                        "Accept-Encoding=1047, X-CSRF-ZOSMF-HEADER=true, " +
                        "Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder()
                .binary(true)
                .returnEtag(true)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, " +
                        "X-IBM-Return-Etag=true, X-IBM-Response-Timeout=30, " +
                        "X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingAndEtagSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder()
                .binary(true)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, " +
                        "X-IBM-Response-Timeout=30, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingAndEtagFalseSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder()
                .binary(true)
                .returnEtag(false)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("{X-IBM-Data-Type=binary, Accept-Encoding=gzip, " +
                        "X-IBM-Response-Timeout=30, X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetTokenWithMultipleParamsWithoutEncodingAndBinaryAndEtagFalseSuccess() throws ZosmfRequestException, IOException {
        final DsnGet dsnGet = new DsnGet(connection, mockGetRequestToken);
        final DsnDownloadInputData downloadInputData = new DsnDownloadInputData.Builder()
                .binary(false)
                .returnEtag(false)
                .responseTimeout("30")
                .build();
        final InputStream inputStream = dsnGet.get("TEST.DATASET", downloadInputData);
        assertEquals("{Accept-Encoding=gzip, X-IBM-Response-Timeout=30, X-CSRF-ZOSMF-HEADER=true," +
                        " Content-Type=application/json}",
                mockGetRequestToken.getHeaders().toString());
        assertEquals("test data", new String(inputStream.readAllBytes()));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.DATASET", mockGetRequestToken.getUrl());
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithValidRequestTypeSuccess() {
        final ZosConnection connection = Mockito.mock(ZosConnection.class);
        final ZosmfRequest request = Mockito.mock(GetStreamZosmfRequest.class);
        final DsnGet dsnGet = new DsnGet(connection, request);
        assertNotNull(dsnGet);
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithNullConnectionFailure() {
        final ZosmfRequest request = Mockito.mock(GetStreamZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnGet(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithNullRequestFailure() {
        final ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnGet(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstDsnGetSecondaryConstructorWithInvalidRequestTypeFailure() {
        final ZosConnection connection = Mockito.mock(ZosConnection.class);
        final ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a GetZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnGet(connection, request)
        );
        assertEquals("GET_STREAM request type required", exception.getMessage());
    }

    @Test
    public void tstDsnGetPrimaryConstructorWithValidConnectionSuccess() {
        final ZosConnection connection = Mockito.mock(ZosConnection.class);
        final DsnGet dsnGet = new DsnGet(connection);
        assertNotNull(dsnGet);
    }

    @Test
    public void tstDsnGetPrimaryConstructorWithNullConnectionFailure() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnGet(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstGetDsnInfoSuccess() throws Exception {
        final Dataset mockDataset = new Dataset("TEST.DATA.SET", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "",
                "", "", "", "");
        final List<Dataset> mockList = Collections.singletonList(mockDataset);

        try (MockedConstruction<DsnList> mocked = Mockito.mockConstruction(DsnList.class,
                (mock, context) ->
                        Mockito.when(mock.getDatasets(Mockito.anyString(), Mockito.any()))
                                .thenReturn(mockList))) {

            DsnGet dsnGet = new DsnGet(connection);
            Dataset result = dsnGet.getDsnInfo("TEST.DATA.SET");

            assertNotNull(result);
            assertEquals("TEST.DATA.SET", result.getDsname());
            assertEquals(1, mocked.constructed().size());
        }
    }

    @Test
    public void tstGetDsnInfoDatasetNotFoundFailure() {
        try (MockedConstruction<DsnList> ignored = Mockito.mockConstruction(DsnList.class,
                (mock, context) -> Mockito.when(mock.getDatasets(Mockito.anyString(), Mockito.any()))
                        .thenReturn(Collections.emptyList()))) {

            final DsnGet dsnGet = new DsnGet(connection);
            ZosmfRequestException exception = assertThrows(ZosmfRequestException.class,
                    () -> dsnGet.getDsnInfo("TEST.DATA.SET"));
            assertEquals("dataset not found", exception.getMessage());
        }
    }

    @Test
    public void tstGetDsnInfoInvalidDatasetNameFailure() {
        final DsnGet dsnGet = new DsnGet(connection);
        assertThrows(IllegalArgumentException.class, () -> dsnGet.getDsnInfo("BADNAME"));
    }

    @Test
    public void tstGetDsnInfoBlankDatasetNameFailure() {
        final DsnGet dsnGet = new DsnGet(connection);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> dsnGet.getDsnInfo(" "));
        assertEquals("dataSetName not specified", ex.getMessage());
    }

}
