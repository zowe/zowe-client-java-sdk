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

import kong.unirest.core.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.DeleteJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * Class containing unit tests for ZosmfLogout.
 *
 * @author Ashish Kumar Dash
 * @version 6.2
 */
public class ZosmfLogoutTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private DeleteJsonZosmfRequest mockDeleteRequest;

    @BeforeEach
    public void init() {
        mockDeleteRequest = Mockito.mock(DeleteJsonZosmfRequest.class);
        doCallRealMethod().when(mockDeleteRequest).setUrl(any());
        doCallRealMethod().when(mockDeleteRequest).getUrl();
    }

    @Test
    public void tstLogoutSuccess() throws ZosmfRequestException {
        final Cookie token = Mockito.mock(Cookie.class);
        final Response expectedResponse = new Response("{}", 200, "ok");
        Mockito.when(mockDeleteRequest.executeRequest()).thenReturn(expectedResponse);

        final ZosmfLogout logout = new ZosmfLogout(connection, mockDeleteRequest);
        final Response response = logout.logout(token);

        assertEquals(expectedResponse, response);
        assertEquals("https://1:443/zosmf/services/authenticate", mockDeleteRequest.getUrl());
    }

    @Test
    public void tstLogoutPrimaryConstructorWithValidConnection() {
        final ZosmfLogout logout = new ZosmfLogout(connection);
        assertNotNull(logout);
    }

    @Test
    public void tstLogoutNullConnectionFailure() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ZosmfLogout(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstLogoutNullTokenFailure() {
        final ZosmfLogout logout = new ZosmfLogout(connection, mockDeleteRequest);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> logout.logout(null)
        );
        assertEquals("token is null", exception.getMessage());
    }

    @Test
    public void tstLogoutSecondaryConstructorWithValidRequestType() {
        final ZosmfLogout logout = new ZosmfLogout(connection, mockDeleteRequest);
        assertNotNull(logout);
    }

    @Test
    public void tstLogoutSecondaryConstructorWithNullRequest() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ZosmfLogout(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstLogoutSecondaryConstructorWithInvalidRequestType() {
        final ZosmfRequest request = Mockito.mock(ZosmfRequest.class);

        final IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> new ZosmfLogout(connection, request)
        );
        assertEquals("DELETE_JSON request type required", exception.getMessage());
    }

}
