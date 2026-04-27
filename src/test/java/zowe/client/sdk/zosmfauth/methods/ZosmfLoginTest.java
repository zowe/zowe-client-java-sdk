/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosmfauth.methods;

import kong.unirest.core.Cookies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfauth.response.ZosmfLoginResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * Class containing unit tests for ZosmfLogin.
 *
 * @author Ashish Kumar Dash
 * @version 6.0
 */
public class ZosmfLoginTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private PostJsonZosmfRequest mockPostRequest;

    @BeforeEach
    public void init() {
        mockPostRequest = Mockito.mock(PostJsonZosmfRequest.class);
        doCallRealMethod().when(mockPostRequest).setUrl(any());
        doCallRealMethod().when(mockPostRequest).getUrl();
    }

    @Test
    public void tstLoginSuccess() throws ZosmfRequestException {
        final Cookies tokens = Mockito.mock(Cookies.class);
        Mockito.when(mockPostRequest.executeRequest()).thenReturn(
                new Response("{}", 200, "ok", tokens));

        final ZosmfLogin login = new ZosmfLogin(connection, mockPostRequest);
        final ZosmfLoginResponse response = login.login();

        assertNotNull(response);
        assertEquals(tokens, response.getTokens());
        assertEquals("https://1:443/zosmf/services/authenticate", mockPostRequest.getUrl());
        Mockito.verify(mockPostRequest).setBody("");
    }

    @Test
    public void tstLoginPrimaryConstructorWithValidConnection() {
        final ZosmfLogin login = new ZosmfLogin(connection);
        assertNotNull(login);
    }

    @Test
    public void tstLoginNullConnectionFailure() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ZosmfLogin(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstLoginSecondaryConstructorWithValidRequestType() {
        final ZosmfLogin login = new ZosmfLogin(connection, mockPostRequest);
        assertNotNull(login);
    }

    @Test
    public void tstLoginSecondaryConstructorWithNullRequest() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ZosmfLogin(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstLoginSecondaryConstructorWithInvalidRequestType() {
        final ZosmfRequest request = Mockito.mock(ZosmfRequest.class);

        final IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new ZosmfLogin(connection, request)
        );
        assertEquals("POST_JSON request type required", exception.getMessage());
    }

}
