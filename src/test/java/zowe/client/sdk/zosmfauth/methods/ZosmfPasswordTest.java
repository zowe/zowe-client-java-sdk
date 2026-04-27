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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PutJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosmfauth.input.PasswordInputData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;

/**
 * Class containing unit tests for ZosmfPassword.
 *
 * @author Ashish Kumar Dash
 * @version 6.0
 */
public class ZosmfPasswordTest {

    private final ZosConnection connection = ZosConnectionFactory
            .createBasicConnection("1", 443, "1", "1");
    private PutJsonZosmfRequest mockPutRequest;

    @BeforeEach
    public void init() {
        mockPutRequest = Mockito.mock(PutJsonZosmfRequest.class);
        doCallRealMethod().when(mockPutRequest).setUrl(any());
        doCallRealMethod().when(mockPutRequest).getUrl();
    }

    @Test
    public void tstChangePasswordSuccess() throws ZosmfRequestException {
        final PasswordInputData inputData = new PasswordInputData("USER1", "OLDPWD", "NEWPWD");
        final Response expectedResponse = new Response("{}", 200, "ok");
        Mockito.when(mockPutRequest.executeRequest()).thenReturn(expectedResponse);

        final ZosmfPassword password = new ZosmfPassword(connection, mockPutRequest);
        final Response response = password.changePassword(inputData);
        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);

        Mockito.verify(mockPutRequest).setBody(bodyCaptor.capture());
        final String requestBody = String.valueOf(bodyCaptor.getValue());

        assertEquals(expectedResponse, response);
        assertEquals("https://1:443/zosmf/services/authenticate", mockPutRequest.getUrl());
        org.junit.jupiter.api.Assertions.assertTrue(requestBody.contains("\"userID\":\"USER1\""));
        org.junit.jupiter.api.Assertions.assertTrue(requestBody.contains("\"oldPwd\":\"OLDPWD\""));
        org.junit.jupiter.api.Assertions.assertTrue(requestBody.contains("\"newPwd\":\"NEWPWD\""));
    }

    @Test
    public void tstPasswordPrimaryConstructorWithValidConnection() {
        final ZosmfPassword password = new ZosmfPassword(connection);
        assertNotNull(password);
    }

    @Test
    public void tstPasswordNullConnectionFailure() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ZosmfPassword(null)
        );
        assertEquals("connection is null", exception.getMessage());
    }

    @Test
    public void tstChangePasswordNullInputDataFailure() {
        final ZosmfPassword password = new ZosmfPassword(connection, mockPutRequest);

        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> password.changePassword(null)
        );
        assertEquals("pwdInputData is null", exception.getMessage());
    }

    @Test
    public void tstPasswordSecondaryConstructorWithValidRequestType() {
        final ZosmfPassword password = new ZosmfPassword(connection, mockPutRequest);
        assertNotNull(password);
    }

    @Test
    public void tstPasswordSecondaryConstructorWithNullRequest() {
        final NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new ZosmfPassword(connection, null)
        );
        assertEquals("request is null", exception.getMessage());
    }

    @Test
    public void tstPasswordSecondaryConstructorWithInvalidRequestType() {
        final ZosmfRequest request = Mockito.mock(ZosmfRequest.class);

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ZosmfPassword(connection, request)
        );
        assertEquals("PUT_JSON request type required", exception.getMessage());
    }

}
