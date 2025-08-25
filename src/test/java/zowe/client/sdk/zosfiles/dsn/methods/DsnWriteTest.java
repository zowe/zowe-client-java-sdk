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
import zowe.client.sdk.rest.PutTextZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.withSettings;

/**
 * Class containing unit tests for DsnWrite.
 *
 * @author Frank Giordano
 * @version 5.0
 */
public class DsnWriteTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", "1", "1", "1");
    private final ZosConnection tokenConnection = ZosConnectionFactory
            .createTokenConnection("1", "1", new Cookie("hello=hello"));
    private PutTextZosmfRequest mockTextPutRequest;
    private PutTextZosmfRequest mockTextPutRequestToken;

    @BeforeEach
    public void init() throws ZosmfRequestException {
        mockTextPutRequest = Mockito.mock(PutTextZosmfRequest.class);
        Mockito.when(mockTextPutRequest.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockTextPutRequest).setUrl(any());
        doCallRealMethod().when(mockTextPutRequest).getUrl();

        mockTextPutRequestToken = Mockito.mock(PutTextZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockTextPutRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 200, "success"));
        doCallRealMethod().when(mockTextPutRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockTextPutRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockTextPutRequestToken).setUrl(any());
        doCallRealMethod().when(mockTextPutRequestToken).getHeaders();
        doCallRealMethod().when(mockTextPutRequestToken).getUrl();
    }

    @Test
    public void tstDsnWriteDatasetSuccess() throws ZosmfRequestException {
        final DsnWrite dsnWrite = new DsnWrite(connection, mockTextPutRequest);
        final Response response = dsnWrite.write("TEXT_PDS", "data");
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEXT_PDS", mockTextPutRequest.getUrl());
    }

    @Test
    public void tstDsnWriteMemberTokenSuccess() throws ZosmfRequestException {
        final DsnWrite dsnWrite = new DsnWrite(connection, mockTextPutRequestToken);
        final Response response = dsnWrite.write("TEXT_PDS", "data");
        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=text/plain; charset=UTF-8}",
                mockTextPutRequestToken.getHeaders().toString());
        assertEquals("{}", response.getResponsePhrase().orElse("n\\a").toString());
        assertEquals(200, response.getStatusCode().orElse(-1));
        assertEquals("success", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:1/zosmf/restfiles/ds/TEXT_PDS", mockTextPutRequestToken.getUrl());
    }

    @Test
    public void tstDsnWriteSecondaryConstructorWithValidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(PutTextZosmfRequest.class);
        DsnWrite dsnWrite = new DsnWrite(connection, request);
        assertNotNull(dsnWrite);
    }

    @Test
    public void tstDsnWriteSecondaryConstructorWithNullConnection() {
        ZosmfRequest request = Mockito.mock(PutTextZosmfRequest.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnWrite(null, request)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstDsnWriteSecondaryConstructorWithNullRequest() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnWrite(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstDsnWriteSecondaryConstructorWithInvalidRequestType() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        ZosmfRequest request = Mockito.mock(ZosmfRequest.class); // Not a PutTextZosmfRequest
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new DsnWrite(connection, request)
        );
        assertEquals("PUT_TEXT request type required", exception.getMessage());
    }

    @Test
    public void tstDsnWritePrimaryConstructorWithValidConnection() {
        ZosConnection connection = Mockito.mock(ZosConnection.class);
        DsnWrite dsnWrite = new DsnWrite(connection);
        assertNotNull(dsnWrite);
    }

    @Test
    public void tstDsnWritePrimaryConstructorWithNullConnection() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new DsnWrite(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

}
