/*
 * This program and the accompanying materials are made available under the terms of the
 * Eclipse Public License v2.0 which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Copyright Contributors to the Zowe Project.
 */
package zowe.client.sdk.zosvariables.methods;

import kong.unirest.core.Cookie;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import zowe.client.sdk.core.ZosConnection;
import zowe.client.sdk.core.ZosConnectionFactory;
import zowe.client.sdk.rest.PostJsonZosmfRequest;
import zowe.client.sdk.rest.Response;
import zowe.client.sdk.rest.ZosmfRequest;
import zowe.client.sdk.rest.exception.ZosmfRequestException;
import zowe.client.sdk.zosvariables.model.SystemVariable;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Class containing unit tests for VariableCreate.
 *
 * @author Muhammad Imran
 * @version 7.0
 */
public class VariableCreateTest {

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
                new Response(new JSONObject(), 204, "No Content"));
        doCallRealMethod().when(mockPostRequest).setUrl(any());
        doCallRealMethod().when(mockPostRequest).getUrl();
        doCallRealMethod().when(mockPostRequest).setBody(any());

        mockPostRequestToken = Mockito.mock(PostJsonZosmfRequest.class,
                withSettings().useConstructor(tokenConnection));
        Mockito.when(mockPostRequestToken.executeRequest()).thenReturn(
                new Response(new JSONObject(), 204, "No Content"));
        doCallRealMethod().when(mockPostRequestToken).setHeaders(anyMap());
        doCallRealMethod().when(mockPostRequestToken).setStandardHeaders();
        doCallRealMethod().when(mockPostRequestToken).setUrl(any());
        doCallRealMethod().when(mockPostRequestToken).getHeaders();
        doCallRealMethod().when(mockPostRequestToken).getUrl();
        doCallRealMethod().when(mockPostRequestToken).setBody(any());
    }

    @Test
    public void tstVariableCreateSuccess() throws ZosmfRequestException {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final List<SystemVariable> variables = List.of(new SystemVariable("var1", "value1", "desc1"));
        final Response response = variableCreate.create("PLEX1", "SYS1", variables);

        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("No Content", response.getStatusText().orElse("n\\a"));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1", mockPostRequest.getUrl());

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPostRequest).setBody(bodyCaptor.capture());
        assertEquals("{\"system-variable-list\":[{\"name\":\"var1\",\"value\":\"value1\",\"description\":\"desc1\"}]}",
                bodyCaptor.getValue().toString());
    }

    @Test
    public void tstVariableCreateNoDescriptionSuccess() throws ZosmfRequestException {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final List<SystemVariable> variables = List.of(new SystemVariable("var1", "value1"));
        variableCreate.create("PLEX1", "SYS1", variables);

        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPostRequest).setBody(bodyCaptor.capture());
        assertEquals("{\"system-variable-list\":[{\"name\":\"var1\",\"value\":\"value1\"}]}",
                bodyCaptor.getValue().toString());
    }

    @Test
    public void tstVariableCreateEmptyVariablesSuccess() throws ZosmfRequestException {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final Response response = variableCreate.create("PLEX1", "SYS1", Collections.emptyList());

        assertEquals(204, response.getStatusCode().orElse(-1));
        final ArgumentCaptor<Object> bodyCaptor = ArgumentCaptor.forClass(Object.class);
        verify(mockPostRequest).setBody(bodyCaptor.capture());
        assertEquals("{\"system-variable-list\":[]}", bodyCaptor.getValue().toString());
    }

    @Test
    public void tstVariableCreateTokenSuccess() throws ZosmfRequestException {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequestToken);
        final List<SystemVariable> variables = List.of(new SystemVariable("var1", "value1", "desc1"));
        final Response response = variableCreate.create("PLEX1", "SYS1", variables);

        assertEquals("{X-CSRF-ZOSMF-HEADER=true, Content-Type=application/json}",
                mockPostRequestToken.getHeaders().toString());
        assertEquals(204, response.getStatusCode().orElse(-1));
        assertEquals("https://1:443/zosmf/variables/rest/1.0/systems/PLEX1.SYS1", mockPostRequestToken.getUrl());
    }

    @Test
    public void tstVariableCreateNullSysplexNameFailure() {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final List<SystemVariable> variables = List.of(new SystemVariable("var1", "value1"));
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> variableCreate.create(null, "SYS1", variables));
        assertEquals("sysplexName is either null or empty", e.getMessage());
    }

    @Test
    public void tstVariableCreateEmptySysplexNameFailure() {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final List<SystemVariable> variables = List.of(new SystemVariable("var1", "value1"));
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> variableCreate.create("", "SYS1", variables));
        assertEquals("sysplexName is either null or empty", e.getMessage());
    }

    @Test
    public void tstVariableCreateNullSystemNameFailure() {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final List<SystemVariable> variables = List.of(new SystemVariable("var1", "value1"));
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> variableCreate.create("PLEX1", null, variables));
        assertEquals("systemName is either null or empty", e.getMessage());
    }

    @Test
    public void tstVariableCreateEmptySystemNameFailure() {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final List<SystemVariable> variables = List.of(new SystemVariable("var1", "value1"));
        final IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> variableCreate.create("PLEX1", "", variables));
        assertEquals("systemName is either null or empty", e.getMessage());
    }

    @Test
    public void tstVariableCreateNullVariablesFailure() {
        final VariableCreate variableCreate = new VariableCreate(connection, mockPostRequest);
        final NullPointerException e = assertThrows(NullPointerException.class,
                () -> variableCreate.create("PLEX1", "SYS1", null));
        assertEquals("variables is null", e.getMessage());
    }

    @Test
    public void tstVariableCreateSecondaryConstructorWithValidRequestTypeSuccess() {
        final ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        final ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        new VariableCreate(mockConnection, mockRequest);
    }

    @Test
    public void tstVariableCreateSecondaryConstructorWithNullConnectionFailure() {
        final ZosmfRequest mockRequest = Mockito.mock(PostJsonZosmfRequest.class);
        final NullPointerException e = assertThrows(NullPointerException.class,
                () -> new VariableCreate(null, mockRequest));
        assertEquals("connection is null", e.getMessage());
    }

    @Test
    public void tstVariableCreateSecondaryConstructorWithNullRequestFailure() {
        final ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        final NullPointerException e = assertThrows(NullPointerException.class,
                () -> new VariableCreate(mockConnection, null));
        assertEquals("request is null", e.getMessage());
    }

    @Test
    public void tstVariableCreateSecondaryConstructorWithInvalidRequestTypeFailure() {
        final ZosConnection mockConnection = Mockito.mock(ZosConnection.class);
        final ZosmfRequest mockRequest = Mockito.mock(ZosmfRequest.class);
        final IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> new VariableCreate(mockConnection, mockRequest));
        assertEquals("POST_JSON request type required", e.getMessage());
    }

    @Test
    public void tstVariableCreatePrimaryConstructorWithValidConnectionSuccess() {
        new VariableCreate(connection);
    }

    @Test
    public void tstVariableCreatePrimaryConstructorWithNullConnectionFailure() {
        final NullPointerException e = assertThrows(NullPointerException.class,
                () -> new VariableCreate(null));
        assertEquals("connection is null", e.getMessage());
    }

}
