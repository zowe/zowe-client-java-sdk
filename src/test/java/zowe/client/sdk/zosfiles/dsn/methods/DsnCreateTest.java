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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosfiles.dsn.input.DsnCreateInputData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for DsnCreate.
 *
 * @author Frank Giordano
 * @version 6.0
 */
public class DsnCreateTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", 443, new Cookie("hello=hello"));
    private PostJsonZosmfRequest mockPostRequest;
    private PostJsonZosmfRequest mockPostRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockPostRequest = Mockito.mock(PostJsonZosmfRequest.class);
        Mockito.when(mockPostRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockPostRequest).setUrl(any());
        doCallRealMethod().when(mockPostRequest).getUrl();

        mockPostRequestToken = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPostRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockPostRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPostRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPostRequestToken).setUrl(any());
        doCallRealMethod().when(mockPostRequestToken).getHeaders();
        doCallRealMethod().when(mockPostRequestToken).getUrl();
    }

    @Test
    public void tstDsnCreateSequentialDatasetSuccess() throws ZosmfRequestException {
        final DsnCreate dsnCreate = new DsnCreate(connection, mockPostRequest);
        final Response response = dsnCreate.create("TEST.PDS", sequential());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.PDS", mockPostRequest.getUrl());
    }

    @Test
    public void tstDsnCreateSequentialDatasetTokenSuccess() throws ZosmfRequestException {
        final DsnCreate dsnCreate = new DsnCreate(connection, mockPostRequestToken);
        final Response response = dsnCreate.create("TEST.PDS", sequential());
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPostRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.PDS", mockPostRequestToken.getUrl());
    }

    @Test
    public void tstDsnCreatePartitionedDatasetSuccess() throws ZosmfRequestException {
        final DsnCreate dsnCreate = new DsnCreate(connection, mockPostRequest);
        final Response response = dsnCreate.create("TEST.PDS", partitioned());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.PDS", mockPostRequest.getUrl());
    }

    @Test
    public void tstDsnCreateClassicDatasetSuccess() throws ZosmfRequestException {
        final DsnCreate dsnCreate = new DsnCreate(connection, mockPostRequest);
        final Response response = dsnCreate.create("TEST.PDS", classic());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/restfiles/ds/TEST.PDS", mockPostRequest.getUrl());
    }

    public static DsnCreateInputData classic() {
        return new DsnCreateInputData.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(25)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    public static DsnCreateInputData partitioned() {
        return new DsnCreateInputData.Builder()
                .dsorg("PO")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .dirblk(5)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    public static DsnCreateInputData sequential() {
        return new DsnCreateInputData.Builder()
                .dsorg("PS")
                .alcunit("CYL")
                .primary(1)
                .secondary(1)
                .recfm("FB")
                .blksize(6160)
                .lrecl(80)
                .build();
    }

    @Test
    public void tstDsnCreateSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        DsnCreate dsnCreate = new DsnCreate(connection, request);
        assertNotNull(dsnCreate);
    }

    @Test
    public void tstDsnCreateSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PostJsonZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnCreate(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstDsnCreateSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnCreate(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstDsnCreateSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PostJsonZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnCreate(connection, request)
        );
        assertEquals("POST_JSON request type required", exception.getMessage());
    }

    @Test
    public void tstDsnCreatePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DsnCreate dsnCreate = new DsnCreate(connection);
        assertNotNull(dsnCreate);
    }

    @Test
    public void tstDsnCreatePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnCreate(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
